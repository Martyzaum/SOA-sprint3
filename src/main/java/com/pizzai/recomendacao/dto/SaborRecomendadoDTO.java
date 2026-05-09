package com.pizzai.recomendacao.dto;

import com.pizzai.shared.enums.CategoriaSabor;

import java.math.BigDecimal;
import java.util.Set;

public record SaborRecomendadoDTO(
        Long saborId,
        String nome,
        CategoriaSabor categoria,
        BigDecimal precoAdicional,
        int ingredientesAcertados,
        Set<String> ingredientesEmComum,
        double scoreCompatibilidade
) {}
