package com.carlos.produtosapi.produtos_api.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record AuthenticationRequestDTO(

        @NotBlank(message = "Username é obrigatório")
        @Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres")
        String username,

        @NotBlank(message = "Password é obrigatório")
        @Size(min = 6, max = 100, message = "Password deve ter entre 6 e 100 caracteres")
        String password

) {}