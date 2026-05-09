package com.pizzai.cliente.dto;

import com.pizzai.cliente.Cliente;

import java.time.LocalDateTime;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        TelefoneDTO telefone,
        EnderecoDTO endereco,
        LocalDateTime dataCadastro
) {
    public static ClienteResponseDTO fromEntity(Cliente c) {
        return new ClienteResponseDTO(
                c.getId(),
                c.getNome(),
                c.getCpf(),
                c.getEmail(),
                TelefoneDTO.fromEntity(c.getTelefone()),
                EnderecoDTO.fromEntity(c.getEndereco()),
                c.getDataCadastro()
        );
    }
}
