package com.pizzai.pedido.dto;

import com.pizzai.cliente.dto.EnderecoDTO;
import com.pizzai.shared.enums.FormaPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record PedidoRequestDTO(
        @NotNull(message = "clienteId e obrigatorio")
        Long clienteId,

        @NotEmpty(message = "o pedido precisa ter pelo menos 1 item")
        @Size(max = 30, message = "maximo de 30 itens por pedido")
        @Valid
        List<ItemPedidoRequestDTO> itens,

        @NotNull(message = "formaPagamento e obrigatoria")
        FormaPagamento formaPagamento,

        @NotNull(message = "valorEntrega e obrigatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "valorEntrega deve ser >= 0")
        @Digits(integer = 8, fraction = 2)
        BigDecimal valorEntrega,

        @Valid
        EnderecoDTO enderecoEntrega,

        @Size(max = 500)
        String observacoes
) {}
