package com.pizzai.pizza.dto;

import com.pizzai.pizza.Pizza;
import com.pizzai.sabor.dto.SaborResponseDTO;
import com.pizzai.shared.enums.TamanhoPizza;

import java.math.BigDecimal;
import java.util.List;

public record PizzaResponseDTO(
        Long id,
        String nome,
        String descricao,
        TamanhoPizza tamanho,
        int fatias,
        boolean disponivel,
        BigDecimal precoTotal,
        List<SaborResponseDTO> sabores
) {
    public static PizzaResponseDTO fromEntity(Pizza p) {
        List<SaborResponseDTO> sabores = p.getSabores().stream()
                .map(SaborResponseDTO::fromEntity)
                .toList();
        return new PizzaResponseDTO(
                p.getId(),
                p.getNome(),
                p.getDescricao(),
                p.getTamanho(),
                p.getTamanho().getFatias(),
                p.isDisponivel(),
                p.calcularPreco(),
                sabores
        );
    }
}
