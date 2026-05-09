package com.pizzai.pizza;

import com.pizzai.pizza.dto.PizzaRequestDTO;
import com.pizzai.pizza.dto.PizzaResponseDTO;
import com.pizzai.sabor.Sabor;
import com.pizzai.sabor.SaborRepository;
import com.pizzai.shared.enums.TamanhoPizza;
import com.pizzai.shared.exception.BusinessException;
import com.pizzai.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PizzaService {

    private final PizzaRepository repository;
    private final SaborRepository saborRepository;

    public PizzaService(PizzaRepository repository, SaborRepository saborRepository) {
        this.repository = repository;
        this.saborRepository = saborRepository;
    }

    public PizzaResponseDTO criar(PizzaRequestDTO dto) {
        Set<Sabor> sabores = carregarSabores(dto.saborIds());
        Pizza nova = Pizza.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .tamanho(dto.tamanho())
                .sabores(sabores)
                .disponivel(dto.disponivel() == null || dto.disponivel())
                .build();
        return PizzaResponseDTO.fromEntity(repository.save(nova));
    }

    @Transactional(readOnly = true)
    public PizzaResponseDTO buscarPorId(Long id) {
        return PizzaResponseDTO.fromEntity(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Page<PizzaResponseDTO> listar(String nome, TamanhoPizza tamanho, Boolean apenasDisponiveis, Pageable pageable) {
        Page<Pizza> pagina;
        if (nome != null && !nome.isBlank()) {
            pagina = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else if (tamanho != null) {
            pagina = repository.findByTamanho(tamanho, pageable);
        } else if (Boolean.TRUE.equals(apenasDisponiveis)) {
            pagina = repository.findByDisponivelTrue(pageable);
        } else {
            pagina = repository.findAll(pageable);
        }
        return pagina.map(PizzaResponseDTO::fromEntity);
    }

    public PizzaResponseDTO atualizar(Long id, PizzaRequestDTO dto) {
        Pizza pizza = buscarEntidade(id);
        Set<Sabor> sabores = carregarSabores(dto.saborIds());
        pizza.setNome(dto.nome());
        pizza.setDescricao(dto.descricao());
        pizza.setTamanho(dto.tamanho());
        pizza.setDisponivel(dto.disponivel() == null || dto.disponivel());
        pizza.getSabores().clear();
        pizza.getSabores().addAll(sabores);
        return PizzaResponseDTO.fromEntity(repository.save(pizza));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pizza", id);
        }
        repository.deleteById(id);
    }

    public Pizza buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza", id));
    }

    private Set<Sabor> carregarSabores(Set<Long> ids) {
        List<Sabor> encontrados = saborRepository.findAllById(ids);
        if (encontrados.size() != ids.size()) {
            Set<Long> idsEncontrados = new HashSet<>();
            encontrados.forEach(s -> idsEncontrados.add(s.getId()));
            Set<Long> faltantes = new HashSet<>(ids);
            faltantes.removeAll(idsEncontrados);
            throw new ResourceNotFoundException("Sabor(es) nao encontrado(s): " + faltantes);
        }
        boolean inativo = encontrados.stream().anyMatch(s -> !s.isAtivo());
        if (inativo) {
            throw new BusinessException("A pizza nao pode conter sabores inativos");
        }
        return new HashSet<>(encontrados);
    }
}
