package com.pizzai.recomendacao;

import com.pizzai.recomendacao.dto.CombinacaoSugeridaDTO;
import com.pizzai.recomendacao.dto.CombinarRequestDTO;
import com.pizzai.recomendacao.dto.SaborRecomendadoDTO;
import com.pizzai.shared.enums.CategoriaSabor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recomendacoes")
@Tag(name = "Recomendacoes", description = "PizzAI - sugestoes de sabores e combinacoes")
public class RecomendacaoController {

    private final RecomendacaoService service;

    public RecomendacaoController(RecomendacaoService service) {
        this.service = service;
    }

    @GetMapping("/sabores")
    @Operation(summary = "Recomenda sabores ativos baseado em ingredientes desejados")
    public ResponseEntity<List<SaborRecomendadoDTO>> recomendarSabores(
            @RequestParam List<String> ingredientes,
            @RequestParam(required = false) CategoriaSabor categoria) {
        return ResponseEntity.ok(service.recomendarSaboresPorIngredientes(ingredientes, categoria));
    }

    @PostMapping("/combinar")
    @Operation(summary = "Sugere combinacoes de sabores dentro de um orcamento e preferencias")
    public ResponseEntity<List<CombinacaoSugeridaDTO>> combinar(
            @RequestBody @Valid CombinarRequestDTO request) {
        return ResponseEntity.ok(service.sugerirCombinacoes(request));
    }
}
