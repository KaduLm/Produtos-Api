package com.carlos.produtosapi.produtos_api.dto;

public record ProdutoRequestDTO(
        String nome,
        Double preco,
        String descricao,
        String categoria
) {
}
