package com.carlos.produtosapi.produtos_api.controller;

import com.carlos.produtosapi.produtos_api.dto.response.ProdutoResponseDTO;
import com.carlos.produtosapi.produtos_api.dto.request.ProdutoRequestDTO;
import com.carlos.produtosapi.produtos_api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos-api")
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
@SecurityRequirement(name = "bearerAuth")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Operation(
            summary = "Cadastrar produto",
            description = "Cria um novo produto no sistema. Requer role ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer role ADMIN", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/cadastrar")
    public ResponseEntity<Void> salvarProduto(@RequestBody @Valid ProdutoRequestDTO produtoDTO){
        produtoService.cadastrarProduto(produtoDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Listar todos os produtos",
            description = "Retorna uma página de produtos cadastrados. Suporta paginação e ordenação via parâmetros do Pageable. Requer role USER."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de produtos retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
            ),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer role USER", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido", content = @Content)
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> listarTodosOsProdutos(Pageable pageable){
        return ResponseEntity.ok(produtoService.listarTodosOsProdutos(pageable));
    }

    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna os dados de um produto específico pelo seu ID. Requer role USER."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto encontrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer role USER", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido", content = @Content)
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> listarProdutosPorId(@PathVariable Long id){
        return ResponseEntity.ok(produtoService.listarProdutosPorId(id));
    }

    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza os dados de um produto existente pelo seu ID. Requer role ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer role ADMIN", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("atualizar/{id}")
    public ResponseEntity<Void> atualizarProduto(@RequestBody @Valid ProdutoRequestDTO produtoDTO, @PathVariable Long id){
        produtoService.atualizarProduto(id, produtoDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Deletar produto",
            description = "Remove permanentemente um produto do sistema pelo seu ID. Requer role ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado — requer role ADMIN", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente ou inválido", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id){
        produtoService.deletarProduto(id);
        return ResponseEntity.ok().build();
    }
}
