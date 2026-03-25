package com.carlos.produtosapi.produtos_api.dto.response;

public record ProdutoResponseDTO(
        Long id,
        String nome,
        Double preco,
        String descricao,
        String categoria
) {
}
