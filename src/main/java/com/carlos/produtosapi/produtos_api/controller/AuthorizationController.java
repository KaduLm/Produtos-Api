package com.carlos.produtosapi.produtos_api.controller;

import com.carlos.produtosapi.produtos_api.dto.request.AuthenticationRequestDTO;
import com.carlos.produtosapi.produtos_api.dto.response.LoginResponseDTO;
import com.carlos.produtosapi.produtos_api.dto.request.RegisterRequestDTO;
import com.carlos.produtosapi.produtos_api.service.AuthorizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
public class AuthorizationController {
    private final AuthorizationService authorizationService;


    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza o login do usuário com username e password, retornando um token JWT válido."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Credenciais inválidas",
                    content = @Content
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@RequestBody @Valid AuthenticationRequestDTO authenticationRequestDTO){
        return ResponseEntity.ok(authorizationService.getLoginResponseDTO(authenticationRequestDTO));
    }


    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um novo usuário no sistema. Retorna 400 se o username já estiver em uso."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário registrado com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Username já cadastrado",
                    content = @Content
            )
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        authorizationService.registrarUsuario(registerRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/atualizar-role-usuario/{id}")
    @Operation(
            summary = "Atualizar role do usuário para ADMIN",
            description = "Somente usuários com role ADMIN podem promover outros usuários."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role atualizada com sucesso", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    public ResponseEntity<Void> atualizarRoleUsuario(@PathVariable Long id) {
        authorizationService.atualizarRoleUsuario(id);
        return ResponseEntity.ok().build();
    }

}
