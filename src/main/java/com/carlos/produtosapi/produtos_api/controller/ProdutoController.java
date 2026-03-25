package com.carlos.produtosapi.produtos_api.controller;

import com.carlos.produtosapi.produtos_api.dto.ProdutoResponseDTO;
import com.carlos.produtosapi.produtos_api.dto.ProdutoRequestDTO;
import com.carlos.produtosapi.produtos_api.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos-api")
public class ProdutoController {

    private final ProdutoService produtoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cadastrar")
    public ResponseEntity<Void> salvarProduto(@RequestBody ProdutoRequestDTO produtoDTO){
        produtoService.cadastrarProduto(produtoDTO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> listarTodosOsProdutos(Pageable pageable){
        return ResponseEntity.ok(produtoService.listarTodosOsProdutos(pageable));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> listarProdutosPorId(@PathVariable Long id){
        return ResponseEntity.ok(produtoService.listarProdutosPorId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("atualizar/{id}")
    public ResponseEntity<Void> atualizarProduto(@RequestBody ProdutoRequestDTO produtoDTO, @PathVariable Long id){
        produtoService.atualizarProduto(id, produtoDTO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id){
        produtoService.deletarProduto(id);
        return ResponseEntity.ok().build();
    }
}
