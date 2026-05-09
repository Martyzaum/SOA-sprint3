package com.pizzai.sabor.dto;

import com.pizzai.shared.enums.CategoriaSabor;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;

public record SaborRequestDTO(
        @NotBlank(message = "nome e obrigatorio")
        @Size(min = 2, max = 100)
        String nome,

        @Size(max = 500)
        String descricao,

        @NotNull(message = "categoria e obrigatoria")
        CategoriaSabor categoria,

        @NotNull(message = "precoAdicional e obrigatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "precoAdicional deve ser >= 0")
        @Digits(integer = 8, fraction = 2)
        BigDecimal precoAdicional,

        Boolean ativo,

        @NotEmpty(message = "ingredientes nao pode ser vazio")
        Set<@NotBlank @Size(max = 60) String> ingredientes
) {}
