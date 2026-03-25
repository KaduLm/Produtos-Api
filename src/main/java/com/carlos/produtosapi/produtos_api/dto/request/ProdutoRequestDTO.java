package com.carlos.produtosapi.produtos_api.dto.request;

import javax.validation.constraints.*;

public record ProdutoRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,

        @NotNull(message = "Preço é obrigatório")
        @Positive(message = "Preço deve ser maior que zero")
        @Digits(integer = 10, fraction = 2, message = "Preço deve ter no máximo 2 casas decimais")
        Double preco,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(min = 10, max = 500, message = "Descrição deve ter entre 5 e 500 caracteres")
        String descricao,

        @NotBlank(message = "Categoria é obrigatória")
        @Size(min = 2, max = 50, message = "Categoria deve ter entre 2 e 50 caracteres")
        String categoria

) {}