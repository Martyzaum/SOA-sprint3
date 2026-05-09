package com.pizzai.cliente.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequestDTO(
        @NotBlank(message = "nome e obrigatorio")
        @Size(min = 3, max = 120, message = "nome deve ter entre 3 e 120 caracteres")
        String nome,

        @NotBlank(message = "cpf e obrigatorio")
        @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$",
                 message = "cpf deve estar no formato 000.000.000-00")
        String cpf,

        @NotBlank(message = "email e obrigatorio")
        @Email(message = "email invalido")
        @Size(max = 150)
        String email,

        @NotNull(message = "telefone e obrigatorio")
        @Valid
        TelefoneDTO telefone,

        @NotNull(message = "endereco e obrigatorio")
        @Valid
        EnderecoDTO endereco
) {}
