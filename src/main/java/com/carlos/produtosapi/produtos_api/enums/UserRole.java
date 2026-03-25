package com.carlos.produtosapi.produtos_api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum UserRole {

    ADMIN(List.of("admin", "user")),
    USER(List.of("user"));

    private List<String> authorities;


}

