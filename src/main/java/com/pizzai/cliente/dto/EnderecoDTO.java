package com.pizzai.cliente.dto;

import com.pizzai.shared.vo.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoDTO(
        @NotBlank(message = "logradouro e obrigatorio")
        @Size(max = 150)
        String logradouro,

        @NotBlank(message = "numero e obrigatorio")
        @Size(max = 10)
        String numero,

        @Size(max = 100)
        String complemento,

        @NotBlank(message = "bairro e obrigatorio")
        @Size(max = 80)
        String bairro,

        @NotBlank(message = "cidade e obrigatoria")
        @Size(max = 80)
        String cidade,

        @NotBlank(message = "uf e obrigatoria")
        @Pattern(regexp = "^[A-Z]{2}$", message = "uf deve ter 2 letras maiusculas")
        String uf,

        @NotBlank(message = "cep e obrigatorio")
        @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "cep deve estar no formato 00000-000")
        String cep
) {
    public Endereco toEntity() {
        return new Endereco(logradouro, numero, complemento, bairro, cidade, uf, cep);
    }

    public static EnderecoDTO fromEntity(Endereco e) {
        if (e == null) return null;
        return new EnderecoDTO(e.getLogradouro(), e.getNumero(), e.getComplemento(),
                e.getBairro(), e.getCidade(), e.getUf(), e.getCep());
    }
}
