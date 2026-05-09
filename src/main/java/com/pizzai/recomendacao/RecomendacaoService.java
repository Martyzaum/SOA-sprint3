package com.pizzai.recomendacao;

import com.pizzai.recomendacao.dto.CombinacaoSugeridaDTO;
import com.pizzai.recomendacao.dto.CombinarRequestDTO;
import com.pizzai.recomendacao.dto.SaborRecomendadoDTO;
import com.pizzai.sabor.Sabor;
import com.pizzai.sabor.SaborRepository;
import com.pizzai.shared.enums.CategoriaSabor;
import com.pizzai.shared.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RecomendacaoService {

    private static final int MAX_SUGESTOES = 5;

    private final SaborRepository saborRepository;

    public RecomendacaoService(SaborRepository saborRepository) {
        this.saborRepository = saborRepository;
    }

    public List<SaborRecomendadoDTO> recomendarSaboresPorIngredientes(Collection<String> ingredientesBuscados,
                                                                      CategoriaSabor filtroCategoria) {
        if (ingredientesBuscados == null || ingredientesBuscados.isEmpty()) {
            throw new BusinessException("Informe ao menos um ingrediente para a recomendacao");
        }

        Set<String> alvos = ingredientesBuscados.stream()
                .map(s -> s.trim().toLowerCase(Locale.ROOT))
                .filter(s -> !s.isBlank())
                .collect(Collectors.toCollection(HashSet::new));

        List<Sabor> candidatos = saborRepository.findAtivosComIngredientes(alvos);
        if (candidatos.isEmpty()) {
            return List.of();
        }

        return candidatos.stream()
                .filter(s -> filtroCategoria == null || s.getCategoria() == filtroCategoria)
                .map(s -> avaliar(s, alvos))
                .sorted(Comparator.comparingDouble(SaborRecomendadoDTO::scoreCompatibilidade).reversed()
                        .thenComparing(SaborRecomendadoDTO::nome))
                .limit(10)
                .toList();
    }

    public List<CombinacaoSugeridaDTO> sugerirCombinacoes(CombinarRequestDTO req) {
        BigDecimal precoBase = req.tamanho().getPrecoBase();
        if (req.orcamento().compareTo(precoBase) < 0) {
            throw new BusinessException(String.format(
                    "Orcamento %s e menor que o preco base do tamanho %s (%s)",
                    req.orcamento(), req.tamanho(), precoBase));
        }

        BigDecimal saldoSabores = req.orcamento().subtract(precoBase);

        List<Sabor> sabores = saborRepository.findAll().stream()
                .filter(Sabor::isAtivo)
                .filter(s -> req.categoriasPreferidas() == null || req.categoriasPreferidas().isEmpty()
                        || req.categoriasPreferidas().contains(s.getCategoria()))
                .filter(s -> !contemProibidos(s, req.ingredientesProibidos()))
                .toList();

        if (sabores.isEmpty()) {
            return List.of();
        }

        int min = req.minSaboresOrDefault();
        int max = Math.min(req.maxSaboresOrDefault(), sabores.size());

        List<List<Sabor>> combinacoes = new ArrayList<>();
        for (int k = min; k <= max; k++) {
            gerarCombinacoes(sabores, k, 0, new ArrayList<>(), combinacoes);
        }

        Set<String> preferidos = normalizar(req.ingredientesPreferidos());

        return combinacoes.stream()
                .map(combo -> avaliarCombo(combo, req, saldoSabores, preferidos))
                .filter(c -> c != null)
                .sorted(Comparator.comparingDouble(CombinacaoSugeridaDTO::scoreCompatibilidade).reversed())
                .limit(MAX_SUGESTOES)
                .toList();
    }

    private SaborRecomendadoDTO avaliar(Sabor sabor, Set<String> alvos) {
        Set<String> ingredientesSabor = sabor.getIngredientes().stream()
                .map(s -> s.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());
        Set<String> intersec = new HashSet<>(ingredientesSabor);
        intersec.retainAll(alvos);

        int acertos = intersec.size();
        int totalIngredientes = ingredientesSabor.size();
        double cobertura = totalIngredientes == 0 ? 0 : (double) acertos / totalIngredientes;
        double precisao = (double) acertos / alvos.size();
        double score = Math.round(((cobertura * 0.4 + precisao * 0.6) * 100.0) * 100.0) / 100.0;

        return new SaborRecomendadoDTO(
                sabor.getId(),
                sabor.getNome(),
                sabor.getCategoria(),
                sabor.getPrecoAdicional(),
                acertos,
                intersec,
                score
        );
    }

    private CombinacaoSugeridaDTO avaliarCombo(List<Sabor> combo, CombinarRequestDTO req,
                                               BigDecimal saldoSabores, Set<String> preferidos) {
        BigDecimal somaSabores = combo.stream()
                .map(Sabor::getPrecoAdicional)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (somaSabores.compareTo(saldoSabores) > 0) {
            return null;
        }

        BigDecimal precoTotal = req.tamanho().getPrecoBase().add(somaSabores);

        Set<String> ingredientesCombo = combo.stream()
                .flatMap(s -> s.getIngredientes().stream())
                .map(s -> s.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());

        long preferidosAtendidos = preferidos.isEmpty() ? 0
                : preferidos.stream().filter(ingredientesCombo::contains).count();

        double aproveitamentoOrcamento = precoTotal.divide(req.orcamento(),
                4, java.math.RoundingMode.HALF_UP).doubleValue();
        double scorePreferencias = preferidos.isEmpty() ? 0.5
                : (double) preferidosAtendidos / preferidos.size();

        double score = Math.round((scorePreferencias * 0.6 + aproveitamentoOrcamento * 0.4) * 10000.0) / 100.0;

        List<CombinacaoSugeridaDTO.SaborSimplificadoDTO> saboresDto = combo.stream()
                .map(s -> new CombinacaoSugeridaDTO.SaborSimplificadoDTO(
                        s.getId(), s.getNome(), s.getPrecoAdicional()))
                .toList();

        String justificativa = construirJustificativa(combo, req, preferidosAtendidos, preferidos);

        return new CombinacaoSugeridaDTO(
                req.tamanho(),
                req.tamanho().getFatias(),
                saboresDto,
                precoTotal,
                score,
                justificativa
        );
    }

    private String construirJustificativa(List<Sabor> combo, CombinarRequestDTO req,
                                          long preferidosAtendidos, Set<String> preferidos) {
        StringBuilder sb = new StringBuilder();
        sb.append("Combinacao de ").append(combo.size()).append(" sabor(es) ");
        sb.append("dentro do orcamento de R$ ").append(req.orcamento()).append(". ");
        if (!preferidos.isEmpty()) {
            sb.append("Atende ").append(preferidosAtendidos).append("/")
              .append(preferidos.size()).append(" ingredientes preferidos.");
        }
        return sb.toString().trim();
    }

    private boolean contemProibidos(Sabor sabor, Set<String> proibidos) {
        if (proibidos == null || proibidos.isEmpty()) return false;
        Set<String> alvo = normalizar(proibidos);
        return sabor.getIngredientes().stream()
                .map(s -> s.toLowerCase(Locale.ROOT))
                .anyMatch(alvo::contains);
    }

    private Set<String> normalizar(Set<String> entrada) {
        if (entrada == null) return Set.of();
        return entrada.stream()
                .map(s -> s.trim().toLowerCase(Locale.ROOT))
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());
    }

    private void gerarCombinacoes(List<Sabor> origem, int k, int inicio,
                                  List<Sabor> atual, List<List<Sabor>> destino) {
        if (atual.size() == k) {
            destino.add(new ArrayList<>(atual));
            return;
        }
        for (int i = inicio; i < origem.size(); i++) {
            atual.add(origem.get(i));
            gerarCombinacoes(origem, k, i + 1, atual, destino);
            atual.remove(atual.size() - 1);
        }
    }
}
