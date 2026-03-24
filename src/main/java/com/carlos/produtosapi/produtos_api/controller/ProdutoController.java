package com.carlos.produtosapi.produtos_api.controller;

import com.carlos.produtosapi.produtos_api.dto.ProdutoDTO;
import com.carlos.produtosapi.produtos_api.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos-api")
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<Void> salvarProduto(@RequestBody ProdutoDTO produtoDTO){
        produtoService.cadastrarProduto(produtoDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodosOsProdutos(){
        return ResponseEntity.ok(produtoService.listarTodosOsProdutos());
    }
}
