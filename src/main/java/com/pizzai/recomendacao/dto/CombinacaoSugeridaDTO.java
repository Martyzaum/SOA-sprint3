package com.pizzai.recomendacao.dto;

import com.pizzai.shared.enums.TamanhoPizza;

import java.math.BigDecimal;
import java.util.List;

public record CombinacaoSugeridaDTO(
        TamanhoPizza tamanho,
        int fatias,
        List<SaborSimplificadoDTO> sabores,
        BigDecimal precoTotal,
        double scoreCompatibilidade,
        String justificativa
) {
    public record SaborSimplificadoDTO(
            Long id,
            String nome,
            BigDecimal precoAdicional
    ) {}
}
