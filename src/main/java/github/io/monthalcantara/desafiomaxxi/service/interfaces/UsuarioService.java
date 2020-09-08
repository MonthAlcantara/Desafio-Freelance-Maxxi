package github.io.monthalcantara.desafiomaxxi.service.interfaces;

import github.io.monthalcantara.desafiomaxxi.model.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioService extends UserDetailsService {


    UserDetails autenticar(Usuario usuario);

}
