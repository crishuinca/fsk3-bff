package cl.bohiggins.bff_libroclases.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import cl.bohiggins.bff_libroclases.dto.EstudianteAlumnoCreateRequest;
import cl.bohiggins.bff_libroclases.dto.EstudianteDto;
import cl.bohiggins.bff_libroclases.dto.LoginRequest;
import cl.bohiggins.bff_libroclases.dto.UsuarioCreateRequest;
import cl.bohiggins.bff_libroclases.entity.RolUsuario;
import cl.bohiggins.bff_libroclases.entity.Usuario;
import cl.bohiggins.bff_libroclases.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtService jwtService;

	@Mock
	private AcademicoClient academicoClient;

	@InjectMocks
	private AuthService authService;

	@Test
	void iniciarSesion_retornaTokenCuandoCredencialesSonValidas() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNombreUsuario("admin");
		usuario.setEmail("admin@colegio.cl");
		usuario.setPassword("hash");
		usuario.setRol(RolUsuario.ADMIN);

		when(usuarioRepository.findByNombreUsuarioIgnoreCaseOrEmailIgnoreCase("admin", "admin"))
				.thenReturn(Optional.of(usuario));
		when(passwordEncoder.matches("clave123", "hash")).thenReturn(true);
		when(jwtService.generarToken("admin", RolUsuario.ADMIN)).thenReturn("jwt-token");

		var response = authService.iniciarSesion(new LoginRequest("admin", "clave123"));

		assertEquals("jwt-token", response.token());
		assertEquals("admin", response.usuario().nombreUsuario());
	}

	@Test
	void listarUsuarios_retornaTodosSinContrasena() {
		Usuario inspector = new Usuario();
		inspector.setId(1L);
		inspector.setNombreUsuario("inspector");
		inspector.setEmail("inspector@colegiobo.cl");
		inspector.setPassword("hash");
		inspector.setRol(RolUsuario.INSPECTOR);

		Usuario profesor = new Usuario();
		profesor.setId(2L);
		profesor.setNombreUsuario("profesor1");
		profesor.setEmail("profesor1@colegio.cl");
		profesor.setPassword("hash");
		profesor.setRol(RolUsuario.PROFESOR);

		when(usuarioRepository.findAll()).thenReturn(List.of(inspector, profesor));

		var usuarios = authService.listarUsuarios();

		assertEquals(2, usuarios.size());
		assertEquals("inspector", usuarios.get(0).nombreUsuario());
		assertEquals("profesor1", usuarios.get(1).nombreUsuario());
	}

	@Test
	void crearUsuario_guardaDatosConRolSolicitado() {
		when(usuarioRepository.existsByNombreUsuarioIgnoreCase("profesor1")).thenReturn(false);
		when(usuarioRepository.existsByEmailIgnoreCase("profesor1@colegio.cl")).thenReturn(false);
		when(passwordEncoder.encode("clave123")).thenReturn("hash");
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
			Usuario guardado = invocation.getArgument(0);
			guardado.setId(2L);
			return guardado;
		});

		var response = authService.crearUsuario(
				new UsuarioCreateRequest("profesor1", "profesor1@colegio.cl", "clave123", RolUsuario.PROFESOR, null, null));

		ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
		verify(usuarioRepository).save(captor.capture());
		Usuario newUsuario = captor.getValue();
		assertEquals(RolUsuario.PROFESOR, newUsuario.getRol());
		assertEquals("profesor1", response.nombreUsuario());
	}

	@Test
	void crearUsuario_alumnoRequiereDatosEstudiante() {
		assertThrows(IllegalArgumentException.class,
				() -> authService.crearUsuario(
						new UsuarioCreateRequest("alumno1", "alumno1@colegio.cl", "clave123", RolUsuario.ALUMNO, null, null)));
	}

	@Test
	void crearUsuario_alumnoConDatosEstudiante_guardaIdAsignado() {
		when(usuarioRepository.existsByNombreUsuarioIgnoreCase("alumno1")).thenReturn(false);
		when(usuarioRepository.existsByEmailIgnoreCase("alumno1@colegio.cl")).thenReturn(false);
		when(passwordEncoder.encode("clave123")).thenReturn("hash");
		when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(academicoClient.crearEstudiante(any(EstudianteAlumnoCreateRequest.class)))
				.thenReturn(new EstudianteDto(2L, "21345678-9", "Maria", "Perez", "Gonzalez", "", null));

		authService.crearUsuario(new UsuarioCreateRequest(
				"alumno1",
				"alumno1@colegio.cl",
				"clave123",
				RolUsuario.ALUMNO,
				null,
				new EstudianteAlumnoCreateRequest(1L, "21345678-9", "Maria", "Perez", "Gonzalez", null, null)));

		ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
		verify(usuarioRepository).save(captor.capture());
		assertEquals(2L, captor.getValue().getEstudianteId());
	}
}
