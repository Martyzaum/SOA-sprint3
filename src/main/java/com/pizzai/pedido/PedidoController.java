package com.pizzai.pedido;

import com.pizzai.pedido.dto.AtualizarStatusRequestDTO;
import com.pizzai.pedido.dto.PedidoRequestDTO;
import com.pizzai.pedido.dto.PedidoResponseDTO;
import com.pizzai.shared.enums.StatusPedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Cria, consulta e altera status de pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cria novo pedido (calcula totais e gera numero unico)")
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody @Valid PedidoRequestDTO dto,
                                                   UriComponentsBuilder uriBuilder) {
        PedidoResponseDTO criado = service.criar(dto);
        URI location = uriBuilder.path("/api/pedidos/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca pedido por id")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Busca pedido por numero (PZ-AAAAMMDD-XXXXXX)")
    public ResponseEntity<PedidoResponseDTO> buscarPorNumero(@PathVariable String numero) {
        return ResponseEntity.ok(service.buscarPorNumero(numero));
    }

    @GetMapping
    @Operation(summary = "Lista pedidos paginados (filtros opcionais)")
    public ResponseEntity<Page<PedidoResponseDTO>> listar(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) StatusPedido status,
            @PageableDefault(size = 20, sort = "dataCriacao", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listar(clienteId, status, pageable));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza status do pedido respeitando transicoes validas")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id,
                                                             @RequestBody @Valid AtualizarStatusRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarStatus(id, dto.status()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancela pedido (transicao para CANCELADO)")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.cancelar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
