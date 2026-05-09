package com.pizzai.pedido.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoRequestDTO(
        @NotNull(message = "pizzaId e obrigatorio")
        Long pizzaId,

        @NotNull(message = "quantidade e obrigatoria")
        @Min(value = 1, message = "quantidade minima e 1")
        @Max(value = 50, message = "quantidade maxima por item e 50")
        Integer quantidade
) {}
