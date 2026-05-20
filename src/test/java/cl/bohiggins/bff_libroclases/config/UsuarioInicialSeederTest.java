package cl.bohiggins.bff_libroclases.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import cl.bohiggins.bff_libroclases.entity.RolUsuario;
import cl.bohiggins.bff_libroclases.entity.Usuario;
import cl.bohiggins.bff_libroclases.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioInicialSeederTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UsuarioInicialSeeder seeder;

	@BeforeEach
	void configurarPropiedades() {
		ReflectionTestUtils.setField(seeder, "nombreUsuario", "inspector");
		ReflectionTestUtils.setField(seeder, "email", "inspector@colegiobo.cl");
		ReflectionTestUtils.setField(seeder, "password", "clave123");
		ReflectionTestUtils.setField(seeder, "rol", RolUsuario.INSPECTOR);
	}

	@Test
	void asegurarInspectorInicial_creaUsuarioCuandoNoExisteEnBaseDeDatos() {
		when(usuarioRepository.findByEmailIgnoreCase("inspector@colegiobo.cl")).thenReturn(java.util.Optional.empty());
		when(usuarioRepository.existsByNombreUsuarioIgnoreCase("inspector")).thenReturn(false);
		when(passwordEncoder.encode("clave123")).thenReturn("hash");

		seeder.asegurarInspectorInicial();

		ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
		verify(usuarioRepository).save(captor.capture());
		Usuario newUsuario = captor.getValue();
		org.junit.jupiter.api.Assertions.assertEquals("inspector", newUsuario.getNombreUsuario());
		org.junit.jupiter.api.Assertions.assertEquals("inspector@colegiobo.cl", newUsuario.getEmail());
		org.junit.jupiter.api.Assertions.assertEquals(RolUsuario.INSPECTOR, newUsuario.getRol());
	}

	@Test
	void asegurarInspectorInicial_noCreaSiYaExistePorCorreo() {
		when(usuarioRepository.findByEmailIgnoreCase("inspector@colegiobo.cl"))
				.thenReturn(java.util.Optional.of(new Usuario()));

		seeder.asegurarInspectorInicial();

		verify(usuarioRepository, never()).save(any());
	}

	@Test
	void asegurarInspectorInicial_noCreaSiYaExistePorNombreUsuario() {
		when(usuarioRepository.findByEmailIgnoreCase("inspector@colegiobo.cl")).thenReturn(java.util.Optional.empty());
		when(usuarioRepository.existsByNombreUsuarioIgnoreCase("inspector")).thenReturn(true);

		seeder.asegurarInspectorInicial();

		verify(usuarioRepository, never()).save(any());
	}
}
