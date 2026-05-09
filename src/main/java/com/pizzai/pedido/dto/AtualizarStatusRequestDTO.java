package com.pizzai.pedido.dto;

import com.pizzai.shared.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequestDTO(
        @NotNull(message = "status e obrigatorio")
        StatusPedido status
) {}
