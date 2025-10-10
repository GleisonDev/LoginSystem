package com.sireostech.vstore.gestao.infrastructure.api.usuario.controller;

import com.sireostech.vstore.gestao.application.contracts.UsuarioContracts;
import com.sireostech.vstore.gestao.domain.model.Usuario;
import com.sireostech.vstore.gestao.infrastructure.api.usuario.UsuarioApi;
import com.sireostech.vstore.gestao.infrastructure.api.usuario.controller.post.request.CadastroUsuarioRequestDTO;
import com.sireostech.vstore.gestao.infrastructure.api.usuario.controller.post.response.CadastroUsuarioResponseDTO;
import com.sireostech.vstore.gestao.infrastructure.api.usuario.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class UsuarioController implements UsuarioApi {

    private final UsuarioContracts contracts;
    private final UsuarioMapper usuarioMapper;

    @Override
    public ResponseEntity<CadastroUsuarioResponseDTO> cadastrarUsuario(CadastroUsuarioRequestDTO request) {
        Usuario command = usuarioMapper.toEntity(request);

        Usuario usuarioSalvo = contracts.cadastrarNovoUsuario(command);

        CadastroUsuarioResponseDTO response = usuarioMapper.toResponseDto(usuarioSalvo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}