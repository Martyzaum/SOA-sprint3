package com.pizzai.pedido.dto;

import com.pizzai.cliente.dto.EnderecoDTO;
import com.pizzai.pedido.Pedido;
import com.pizzai.shared.enums.FormaPagamento;
import com.pizzai.shared.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        String numero,
        Long clienteId,
        String clienteNome,
        StatusPedido status,
        FormaPagamento formaPagamento,
        BigDecimal valorItens,
        BigDecimal valorEntrega,
        BigDecimal valorTotal,
        EnderecoDTO enderecoEntrega,
        String observacoes,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        List<ItemPedidoResponseDTO> itens
) {
    public static PedidoResponseDTO fromEntity(Pedido p) {
        List<ItemPedidoResponseDTO> itens = p.getItens().stream()
                .map(ItemPedidoResponseDTO::fromEntity)
                .toList();
        return new PedidoResponseDTO(
                p.getId(),
                p.getNumero(),
                p.getCliente().getId(),
                p.getCliente().getNome(),
                p.getStatus(),
                p.getFormaPagamento(),
                p.getValorItens(),
                p.getValorEntrega(),
                p.getValorTotal(),
                EnderecoDTO.fromEntity(p.getEnderecoEntrega()),
                p.getObservacoes(),
                p.getDataCriacao(),
                p.getDataAtualizacao(),
                itens
        );
    }
}
