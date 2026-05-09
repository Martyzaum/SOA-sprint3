package com.pizzai.cliente;

import com.pizzai.cliente.dto.ClienteRequestDTO;
import com.pizzai.cliente.dto.ClienteResponseDTO;
import com.pizzai.shared.exception.BusinessException;
import com.pizzai.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public ClienteResponseDTO criar(ClienteRequestDTO dto) {
        validarUnicidade(dto.cpf(), dto.email(), null);
        Cliente novo = Cliente.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .email(dto.email())
                .telefone(dto.telefone().toEntity())
                .endereco(dto.endereco().toEntity())
                .build();
        Cliente salvo = repository.save(novo);
        return ClienteResponseDTO.fromEntity(salvo);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        return ClienteResponseDTO.fromEntity(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> listar(String nome, Pageable pageable) {
        Page<Cliente> pagina = (nome == null || nome.isBlank())
                ? repository.findAll(pageable)
                : repository.findByNomeContainingIgnoreCase(nome, pageable);
        return pagina.map(ClienteResponseDTO::fromEntity);
    }

    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = buscarEntidade(id);
        validarUnicidade(dto.cpf(), dto.email(), id);
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());
        cliente.setTelefone(dto.telefone().toEntity());
        cliente.setEndereco(dto.endereco().toEntity());
        return ClienteResponseDTO.fromEntity(repository.save(cliente));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", id);
        }
        repository.deleteById(id);
    }

    public Cliente buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }

    private void validarUnicidade(String cpf, String email, Long idAtual) {
        repository.findByCpf(cpf).ifPresent(c -> {
            if (idAtual == null || !c.getId().equals(idAtual)) {
                throw new BusinessException("Ja existe cliente cadastrado com o CPF informado");
            }
        });
        repository.findByEmail(email).ifPresent(c -> {
            if (idAtual == null || !c.getId().equals(idAtual)) {
                throw new BusinessException("Ja existe cliente cadastrado com o email informado");
            }
        });
    }
}
