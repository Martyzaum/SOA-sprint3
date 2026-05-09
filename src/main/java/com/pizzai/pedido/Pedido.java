package com.pizzai.pedido;

import com.pizzai.cliente.Cliente;
import com.pizzai.shared.enums.FormaPagamento;
import com.pizzai.shared.enums.StatusPedido;
import com.pizzai.shared.vo.Endereco;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, unique = true, length = 20)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<ItemPedido> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusPedido status;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false, length = 30)
    private FormaPagamento formaPagamento;

    @Column(name = "valor_itens", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorItens;

    @Column(name = "valor_entrega", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorEntrega;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "logradouro", column = @Column(name = "entrega_logradouro", length = 150)),
            @AttributeOverride(name = "numero", column = @Column(name = "entrega_numero", length = 10)),
            @AttributeOverride(name = "complemento", column = @Column(name = "entrega_complemento", length = 100)),
            @AttributeOverride(name = "bairro", column = @Column(name = "entrega_bairro", length = 80)),
            @AttributeOverride(name = "cidade", column = @Column(name = "entrega_cidade", length = 80)),
            @AttributeOverride(name = "uf", column = @Column(name = "entrega_uf", length = 2)),
            @AttributeOverride(name = "cep", column = @Column(name = "entrega_cep", length = 9))
    })
    private Endereco enderecoEntrega;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        this.itens.add(item);
    }

    public void recalcularTotais() {
        this.valorItens = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal entrega = valorEntrega != null ? valorEntrega : BigDecimal.ZERO;
        this.valorTotal = this.valorItens.add(entrega);
    }
}
