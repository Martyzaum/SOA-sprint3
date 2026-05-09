package com.pizzai.pizza.dto;

import com.pizzai.shared.enums.TamanhoPizza;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record PizzaRequestDTO(
        @NotBlank(message = "nome e obrigatorio")
        @Size(min = 3, max = 120)
        String nome,

        @Size(max = 500)
        String descricao,

        @NotNull(message = "tamanho e obrigatorio")
        TamanhoPizza tamanho,

        @NotEmpty(message = "saborIds nao pode ser vazio")
        @Size(min = 1, max = 4, message = "uma pizza pode ter de 1 a 4 sabores")
        Set<Long> saborIds,

        Boolean disponivel
) {}
