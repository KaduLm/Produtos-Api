package com.carlos.produtosapi.produtos_api.dto.request;

import com.carlos.produtosapi.produtos_api.enums.UserRole;

public record RegisterRequestDTO(String username, String password, UserRole role) {
}
