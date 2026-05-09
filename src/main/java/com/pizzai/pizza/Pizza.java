package com.pizzai.pizza;

import com.pizzai.sabor.Sabor;
import com.pizzai.shared.enums.TamanhoPizza;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pizzas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tamanho", nullable = false, length = 20)
    private TamanhoPizza tamanho;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "pizza_sabores",
            joinColumns = @JoinColumn(name = "pizza_id"),
            inverseJoinColumns = @JoinColumn(name = "sabor_id")
    )
    @Builder.Default
    private Set<Sabor> sabores = new HashSet<>();

    @Column(name = "disponivel", nullable = false)
    private boolean disponivel;

    public BigDecimal calcularPreco() {
        BigDecimal somaSabores = sabores.stream()
                .map(Sabor::getPrecoAdicional)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return tamanho.getPrecoBase().add(somaSabores);
    }
}
