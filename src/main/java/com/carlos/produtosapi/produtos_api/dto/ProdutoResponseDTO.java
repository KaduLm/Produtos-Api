package com.carlos.produtosapi.produtos_api.dto;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        Double preco,
        String descricao,
        String categoria
) {
}
