package com.pizzai.pedido.dto;

import com.pizzai.pedido.ItemPedido;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        Long id,
        Long pizzaId,
        String pizzaNome,
        int quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
    public static ItemPedidoResponseDTO fromEntity(ItemPedido item) {
        return new ItemPedidoResponseDTO(
                item.getId(),
                item.getPizza().getId(),
                item.getPizza().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
        );
    }
}
