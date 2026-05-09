package com.pizzai.pizza;

import com.pizzai.pizza.dto.PizzaRequestDTO;
import com.pizzai.pizza.dto.PizzaResponseDTO;
import com.pizzai.shared.enums.TamanhoPizza;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/pizzas")
@Tag(name = "Pizzas", description = "CRUD de pizzas (combinacao de sabores + tamanho)")
public class PizzaController {

    private final PizzaService service;

    public PizzaController(PizzaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cria nova pizza")
    public ResponseEntity<PizzaResponseDTO> criar(@RequestBody @Valid PizzaRequestDTO dto,
                                                  UriComponentsBuilder uriBuilder) {
        PizzaResponseDTO criada = service.criar(dto);
        URI location = uriBuilder.path("/api/pizzas/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca pizza por id (ja com preco calculado)")
    public ResponseEntity<PizzaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Lista pizzas paginadas com filtros opcionais")
    public ResponseEntity<Page<PizzaResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) TamanhoPizza tamanho,
            @RequestParam(required = false) Boolean apenasDisponiveis,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(service.listar(nome, tamanho, apenasDisponiveis, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza pizza existente")
    public ResponseEntity<PizzaResponseDTO> atualizar(@PathVariable Long id,
                                                      @RequestBody @Valid PizzaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove pizza")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
