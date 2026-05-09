package com.pizzai.sabor;

import com.pizzai.sabor.dto.SaborRequestDTO;
import com.pizzai.sabor.dto.SaborResponseDTO;
import com.pizzai.shared.enums.CategoriaSabor;
import com.pizzai.shared.exception.BusinessException;
import com.pizzai.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class SaborService {

    private final SaborRepository repository;

    public SaborService(SaborRepository repository) {
        this.repository = repository;
    }

    public SaborResponseDTO criar(SaborRequestDTO dto) {
        if (repository.existsByNomeIgnoreCase(dto.nome())) {
            throw new BusinessException("Ja existe sabor com o nome '" + dto.nome() + "'");
        }
        Sabor novo = Sabor.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .categoria(dto.categoria())
                .precoAdicional(dto.precoAdicional())
                .ativo(dto.ativo() == null || dto.ativo())
                .ingredientes(normalizar(dto.ingredientes()))
                .build();
        return SaborResponseDTO.fromEntity(repository.save(novo));
    }

    @Transactional(readOnly = true)
    public SaborResponseDTO buscarPorId(Long id) {
        return SaborResponseDTO.fromEntity(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Page<SaborResponseDTO> listar(CategoriaSabor categoria, Boolean apenasAtivos, Pageable pageable) {
        Page<Sabor> pagina;
        if (categoria != null) {
            pagina = repository.findByCategoria(categoria, pageable);
        } else if (Boolean.TRUE.equals(apenasAtivos)) {
            pagina = repository.findByAtivoTrue(pageable);
        } else {
            pagina = repository.findAll(pageable);
        }
        return pagina.map(SaborResponseDTO::fromEntity);
    }

    public SaborResponseDTO atualizar(Long id, SaborRequestDTO dto) {
        Sabor sabor = buscarEntidade(id);
        repository.findByNomeIgnoreCase(dto.nome()).ifPresent(s -> {
            if (!s.getId().equals(id)) {
                throw new BusinessException("Ja existe sabor com o nome '" + dto.nome() + "'");
            }
        });
        sabor.setNome(dto.nome());
        sabor.setDescricao(dto.descricao());
        sabor.setCategoria(dto.categoria());
        sabor.setPrecoAdicional(dto.precoAdicional());
        sabor.setAtivo(dto.ativo() == null || dto.ativo());
        sabor.getIngredientes().clear();
        sabor.getIngredientes().addAll(normalizar(dto.ingredientes()));
        return SaborResponseDTO.fromEntity(repository.save(sabor));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Sabor", id);
        }
        repository.deleteById(id);
    }

    public Sabor buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sabor", id));
    }

    private java.util.Set<String> normalizar(java.util.Set<String> ingredientes) {
        return ingredientes.stream()
                .map(s -> s.trim().toLowerCase(Locale.ROOT))
                .filter(s -> !s.isBlank())
                .collect(Collectors.toCollection(HashSet::new));
    }
}
