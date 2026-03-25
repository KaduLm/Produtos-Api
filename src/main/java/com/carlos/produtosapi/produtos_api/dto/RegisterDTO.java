package com.carlos.produtosapi.produtos_api.dto;

import com.carlos.produtosapi.produtos_api.enums.UserRole;

public record RegisterDTO(String username, String password, UserRole role) {
}
