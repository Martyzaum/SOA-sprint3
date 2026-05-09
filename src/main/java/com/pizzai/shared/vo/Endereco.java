package com.pizzai.shared.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class Endereco {

    @NotBlank(message = "logradouro e obrigatorio")
    @Size(max = 150)
    @Column(name = "logradouro", length = 150)
    private String logradouro;

    @NotBlank(message = "numero e obrigatorio")
    @Size(max = 10)
    @Column(name = "numero", length = 10)
    private String numero;

    @Size(max = 100)
    @Column(name = "complemento", length = 100)
    private String complemento;

    @NotBlank(message = "bairro e obrigatorio")
    @Size(max = 80)
    @Column(name = "bairro", length = 80)
    private String bairro;

    @NotBlank(message = "cidade e obrigatoria")
    @Size(max = 80)
    @Column(name = "cidade", length = 80)
    private String cidade;

    @NotBlank(message = "uf e obrigatoria")
    @Pattern(regexp = "^[A-Z]{2}$", message = "uf deve ter 2 letras maiusculas")
    @Column(name = "uf", length = 2)
    private String uf;

    @NotBlank(message = "cep e obrigatorio")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "cep deve estar no formato 00000-000")
    @Column(name = "cep", length = 9)
    private String cep;
}
