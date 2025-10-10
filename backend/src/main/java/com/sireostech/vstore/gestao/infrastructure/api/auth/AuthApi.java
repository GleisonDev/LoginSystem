package com.sireostech.vstore.gestao.infrastructure.api.auth;

import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request.LoginRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.request.RefreshTokenRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.LoginResponseDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.LogoutResponseDTO;
import com.sireostech.vstore.gestao.infrastructure.api.auth.controller.post.response.RefreshTokenResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(
        name = "Autenticação",
        description = "Endpoints responsáveis pelo login, geração e renovação de tokens JWT."
)
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

    @PostMapping("/logout")
    @Operation(
            summary = "Realizar Logoff",
            description = "Adiciona o token JWT atual à lista negra, invalidando-o imediatamente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logoff bem-sucedido"),
                    @ApiResponse(responseCode = "401", description = "Não autorizado")
            }
    )
    ResponseEntity<LogoutResponseDTO> logout(HttpServletRequest request);

    @PostMapping("/refresh")
    @Operation(
            summary = "Renovar Access Token usando Refresh Token",
            description = "Recebe o refresh token válido e retorna um novo JWT de acesso.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token renovado com sucesso"),
                    @ApiResponse(responseCode = "401", description = "Refresh token inválido ou expirado")
            }
    )
    ResponseEntity<RefreshTokenResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO request);
}