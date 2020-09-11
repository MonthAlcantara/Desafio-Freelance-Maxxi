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
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UsuarioService userService;
    @Autowired
    private JwtService jwtService;

    int i = 0;

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

    @GetMapping("/contador")
    public String contaRefresh(HttpServletRequest request) {
        Integer pageViews = 1;
        if (request.getSession().getAttribute("pageViews") != null) {
            pageViews += (Integer) request.getSession().getAttribute("pageViews");
        }
        request.getSession().setAttribute("pageViews", pageViews);
        String expira = String.valueOf(request.getSession().getMaxInactiveInterval());
        String page = String.valueOf(pageViews);
        return "Numero de refreshs: " + pageViews + " Expira em: " + expira + " segundos";
    }


//    @GetMapping("/zeracontador")
//    public void zeraContador(HttpServletRequest session) {
//
//        System.out.println(redisTemplate.getExpire("spring:session:sessions:" + session.getSession().getId()));
//    //    session.getSession().invalidate();
//
//        redisTemplate.delete("spring:session:sessions:" + session.getSession().getId());
//        redisTemplate.delete("spring:session:sessions:expires:" + session.getSession().getId());
//
//        System.out.println("spring:session:sessions:" + session.getSession().getId());
//        System.out.println("spring:session:sessions:expires:" + session.getSession().getId());
//
//
//    }
}
