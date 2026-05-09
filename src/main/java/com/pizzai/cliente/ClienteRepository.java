package com.pizzai.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCpf(String cpf);

    Optional<Cliente> findByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Page<Cliente> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
