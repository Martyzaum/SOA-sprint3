package com.pizzai.pizza;

import com.pizzai.shared.enums.TamanhoPizza;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    Page<Pizza> findByDisponivelTrue(Pageable pageable);

    Page<Pizza> findByTamanho(TamanhoPizza tamanho, Pageable pageable);

    Page<Pizza> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
