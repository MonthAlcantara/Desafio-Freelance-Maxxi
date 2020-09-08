package github.io.monthalcantara.desafiomaxxi.repository;

import github.io.monthalcantara.desafiomaxxi.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);
}
