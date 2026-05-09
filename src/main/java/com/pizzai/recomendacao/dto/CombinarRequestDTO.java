package com.pizzai.recomendacao.dto;

import com.pizzai.shared.enums.CategoriaSabor;
import com.pizzai.shared.enums.TamanhoPizza;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Set;

public record CombinarRequestDTO(
        @NotNull(message = "tamanho e obrigatorio")
        TamanhoPizza tamanho,

        @NotNull(message = "orcamento e obrigatorio")
        @DecimalMin(value = "10.0", message = "orcamento minimo e 10.00")
        BigDecimal orcamento,

        @Min(value = 1, message = "minimo 1 sabor")
        Integer minSabores,

        @Min(value = 1, message = "maximo 4 sabores")
        Integer maxSabores,

        Set<CategoriaSabor> categoriasPreferidas,

        Set<String> ingredientesPreferidos,

        Set<String> ingredientesProibidos
) {
    public int minSaboresOrDefault() {
        return minSabores == null ? 1 : minSabores;
    }
    public int maxSaboresOrDefault() {
        return maxSabores == null ? 2 : Math.min(maxSabores, 4);
    }
}
