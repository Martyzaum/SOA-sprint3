package com.pizzai.sabor;

import com.pizzai.shared.enums.CategoriaSabor;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "sabores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sabor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true, length = 100)
    private String nome;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 30)
    private CategoriaSabor categoria;

    @Column(name = "preco_adicional", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoAdicional;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "sabor_ingredientes",
            joinColumns = @JoinColumn(name = "sabor_id")
    )
    @Column(name = "ingrediente", length = 60)
    @Builder.Default
    private Set<String> ingredientes = new HashSet<>();
}
