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
class ProfesorInicialSeederTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private ProfesorInicialSeeder seeder;

	@BeforeEach
	void configurarPropiedades() {
		ReflectionTestUtils.setField(seeder, "nombreUsuario", "profesor");
		ReflectionTestUtils.setField(seeder, "email", "profesor@colegiobo.cl");
		ReflectionTestUtils.setField(seeder, "password", "clave123");
	}

	@Test
	void asegurarProfesorInicial_creaUsuarioCuandoNoExiste() {
		when(usuarioRepository.findByEmailIgnoreCase("profesor@colegiobo.cl")).thenReturn(java.util.Optional.empty());
		when(usuarioRepository.existsByNombreUsuarioIgnoreCase("profesor")).thenReturn(false);
		when(passwordEncoder.encode("clave123")).thenReturn("hash");

		seeder.asegurarProfesorInicial();

		ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
		verify(usuarioRepository).save(captor.capture());
		org.junit.jupiter.api.Assertions.assertEquals(RolUsuario.PROFESOR, captor.getValue().getRol());
	}

	@Test
	void asegurarProfesorInicial_noCreaSiYaExiste() {
		when(usuarioRepository.findByEmailIgnoreCase("profesor@colegiobo.cl"))
				.thenReturn(java.util.Optional.of(new Usuario()));

		seeder.asegurarProfesorInicial();

		verify(usuarioRepository, never()).save(any());
	}
}
