package com.pizzai.cliente;

import com.pizzai.shared.vo.Endereco;
import com.pizzai.shared.vo.Telefone;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Embedded
    private Telefone telefone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "logradouro", column = @Column(name = "endereco_logradouro", length = 150)),
            @AttributeOverride(name = "numero", column = @Column(name = "endereco_numero", length = 10)),
            @AttributeOverride(name = "complemento", column = @Column(name = "endereco_complemento", length = 100)),
            @AttributeOverride(name = "bairro", column = @Column(name = "endereco_bairro", length = 80)),
            @AttributeOverride(name = "cidade", column = @Column(name = "endereco_cidade", length = 80)),
            @AttributeOverride(name = "uf", column = @Column(name = "endereco_uf", length = 2)),
            @AttributeOverride(name = "cep", column = @Column(name = "endereco_cep", length = 9))
    })
    private Endereco endereco;

    @CreationTimestamp
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;
}
