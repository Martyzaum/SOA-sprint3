package com.pizzai.sabor.dto;

import com.pizzai.sabor.Sabor;
import com.pizzai.shared.enums.CategoriaSabor;

import java.math.BigDecimal;
import java.util.Set;

public record SaborResponseDTO(
        Long id,
        String nome,
        String descricao,
        CategoriaSabor categoria,
        BigDecimal precoAdicional,
        boolean ativo,
        Set<String> ingredientes
) {
    public static SaborResponseDTO fromEntity(Sabor s) {
        return new SaborResponseDTO(
                s.getId(),
                s.getNome(),
                s.getDescricao(),
                s.getCategoria(),
                s.getPrecoAdicional(),
                s.isAtivo(),
                Set.copyOf(s.getIngredientes())
        );
    }
}
