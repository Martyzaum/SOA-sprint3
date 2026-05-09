package com.pizzai.sabor;

import com.pizzai.shared.enums.CategoriaSabor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SaborRepository extends JpaRepository<Sabor, Long> {

    Optional<Sabor> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);

    Page<Sabor> findByCategoria(CategoriaSabor categoria, Pageable pageable);

    Page<Sabor> findByAtivoTrue(Pageable pageable);

    @Query("""
           SELECT DISTINCT s FROM Sabor s
           JOIN s.ingredientes i
           WHERE LOWER(i) IN :ingredientes
             AND s.ativo = TRUE
           """)
    List<Sabor> findAtivosComIngredientes(@Param("ingredientes") Set<String> ingredientes);
}
