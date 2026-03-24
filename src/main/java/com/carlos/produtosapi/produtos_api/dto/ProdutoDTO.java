package com.carlos.produtosapi.produtos_api.dto;

public record ProdutoDTO(
         String nome,
         Double preco,
         String descricao,
         String categoria
) {
}
