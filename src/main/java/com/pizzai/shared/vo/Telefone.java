package com.pizzai.shared.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Telefone {

    @NotBlank(message = "ddd e obrigatorio")
    @Pattern(regexp = "^\\d{2}$", message = "ddd deve ter 2 digitos")
    @Column(name = "telefone_ddd", length = 2)
    private String ddd;

    @NotBlank(message = "numero e obrigatorio")
    @Pattern(regexp = "^\\d{8,9}$", message = "numero deve ter 8 ou 9 digitos")
    @Column(name = "telefone_numero", length = 9)
    private String numero;

    public String formatado() {
        if (numero.length() == 9) {
            return String.format("(%s) %s-%s", ddd, numero.substring(0, 5), numero.substring(5));
        }
        return String.format("(%s) %s-%s", ddd, numero.substring(0, 4), numero.substring(4));
    }
}
