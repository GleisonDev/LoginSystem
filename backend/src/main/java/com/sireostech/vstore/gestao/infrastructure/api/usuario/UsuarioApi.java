package com.sireostech.vstore.gestao.infrastructure.api.usuario;

import com.sireostech.vstore.gestao.infrastructure.api.usuario.controller.post.request.CadastroUsuarioRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.usuario.controller.post.response.CadastroUsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(
        name = "Usuários",
        description = "Endpoints responsáveis pela gestão de usuários, incluindo cadastro, consulta e atualização."
)
@RequestMapping("/usuarios")
public interface UsuarioApi {

    @PostMapping
    @Operation(
            summary = "Cadastrar um usuário",
            description = "Registra um novo usuário no sistema com as informações fornecidas.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida - dados incorretos"),
                    @ApiResponse(responseCode = "409", description = "Conflito - Usuário já existe"),
                    @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
            }
    )

    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    ResponseEntity<CadastroUsuarioResponseDTO> cadastrarUsuario(@RequestBody @Valid CadastroUsuarioRequestDTO request);

}