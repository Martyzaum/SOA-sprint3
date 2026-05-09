package com.pizzai.sabor;

import com.pizzai.sabor.dto.SaborRequestDTO;
import com.pizzai.sabor.dto.SaborResponseDTO;
import com.pizzai.shared.enums.CategoriaSabor;
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
@RequestMapping("/api/sabores")
@Tag(name = "Sabores", description = "CRUD de sabores de pizza com ingredientes")
public class SaborController {

    private final SaborService service;

    public SaborController(SaborService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cria novo sabor")
    public ResponseEntity<SaborResponseDTO> criar(@RequestBody @Valid SaborRequestDTO dto,
                                                  UriComponentsBuilder uriBuilder) {
        SaborResponseDTO criado = service.criar(dto);
        URI location = uriBuilder.path("/api/sabores/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca sabor por id")
    public ResponseEntity<SaborResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    @Operation(summary = "Lista sabores paginados (filtros opcionais)")
    public ResponseEntity<Page<SaborResponseDTO>> listar(
            @RequestParam(required = false) CategoriaSabor categoria,
            @RequestParam(required = false) Boolean apenasAtivos,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(service.listar(categoria, apenasAtivos, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza sabor existente")
    public ResponseEntity<SaborResponseDTO> atualizar(@PathVariable Long id,
                                                      @RequestBody @Valid SaborRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove sabor")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
