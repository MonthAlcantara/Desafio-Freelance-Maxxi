package github.io.monthalcantara.desafiomaxxi.controller;

import github.io.monthalcantara.desafiomaxxi.dto.TokenDTO;
import github.io.monthalcantara.desafiomaxxi.exception.SenhaInvalidaException;
import github.io.monthalcantara.desafiomaxxi.jwt.JwtService;
import github.io.monthalcantara.desafiomaxxi.model.Usuario;
import github.io.monthalcantara.desafiomaxxi.service.interfaces.UsuarioService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService userService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth")
    @ApiOperation("Gera um Token de Acesso")
    @Cacheable("usuarios")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Usuário não localizado"),
            @ApiResponse(code = 200, message = "Token gerado"),
            @ApiResponse(code = 201, message = "Token gerado com sucesso")})
    public TokenDTO authenticate(@RequestBody Usuario userLogin, HttpSession session) {
        try {
            Usuario user = Usuario.builder()
                    .login(userLogin.getLogin())
                    .password(userLogin.getPassword())
                    .build();
            UserDetails authenticate = userService.autenticar(user);
            String token = jwtService.createToken(user);
            return new TokenDTO(userLogin.getLogin(), token);

        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
