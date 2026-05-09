package com.pizzai.cliente.dto;

import com.pizzai.shared.vo.Telefone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TelefoneDTO(
        @NotBlank(message = "ddd e obrigatorio")
        @Pattern(regexp = "^\\d{2}$", message = "ddd deve ter 2 digitos")
        String ddd,

        @NotBlank(message = "numero e obrigatorio")
        @Pattern(regexp = "^\\d{8,9}$", message = "numero deve ter 8 ou 9 digitos")
        String numero
) {
    public Telefone toEntity() {
        return new Telefone(ddd, numero);
    }

    public static TelefoneDTO fromEntity(Telefone t) {
        if (t == null) return null;
        return new TelefoneDTO(t.getDdd(), t.getNumero());
    }
}
