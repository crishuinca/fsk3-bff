package cl.bohiggins.bff_libroclases.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.bohiggins.bff_libroclases.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByNombreUsuarioIgnoreCase(String nombreUsuario);

	Optional<Usuario> findByEmailIgnoreCase(String email);

	boolean existsByNombreUsuarioIgnoreCase(String nombreUsuario);

	boolean existsByEmailIgnoreCase(String email);

	Optional<Usuario> findByNombreUsuarioIgnoreCaseOrEmailIgnoreCase(String nombreUsuario, String email);
}
