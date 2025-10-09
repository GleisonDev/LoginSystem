package com.sireostech.vstore.gestao.infrastructure.api.auth;

import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request.LoginRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(
        name = "Autenticação",
        description = "Endpoints responsáveis pelo login, geração e renovação de tokens JWT."
)
// Definimos a rota base /api/v1/public/auth aqui
@RequestMapping("/login")
public interface AuthApi {

    @PostMapping
    @Operation(
            summary = "Autenticar usuário",
            description = "Recebe nome de usuário e senha e retorna um token de acesso JWT.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida - dados incorretos"),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou não autorizadas"),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
            }
    )
    ResponseEntity<LoginResponseDTO> authenticate(@RequestBody @Valid LoginRequestDTO request);

    // Futuramente, outros endpoints como @PostMapping("/refresh") podem ser adicionados aqui.
}