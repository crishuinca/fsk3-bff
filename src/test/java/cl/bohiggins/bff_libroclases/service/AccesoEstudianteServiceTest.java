package cl.bohiggins.bff_libroclases.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import cl.bohiggins.bff_libroclases.entity.RolUsuario;
import cl.bohiggins.bff_libroclases.entity.Usuario;
import cl.bohiggins.bff_libroclases.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class AccesoEstudianteServiceTest {

	@Mock
	private UsuarioRepository usuarioRepository;

	@InjectMocks
	private AccesoEstudianteService accesoEstudianteService;

	@Test
	void alumnoSoloAccedeASuEstudianteId() {
		Usuario alumno = new Usuario();
		alumno.setNombreUsuario("alumno1");
		alumno.setRol(RolUsuario.ALUMNO);
		alumno.setEstudianteId(1L);
		when(usuarioRepository.findByNombreUsuarioIgnoreCase("alumno1")).thenReturn(Optional.of(alumno));

		assertDoesNotThrow(() -> accesoEstudianteService.validarAccesoPorEstudianteId("alumno1", 1L));
		assertThrows(AccessDeniedException.class,
				() -> accesoEstudianteService.validarAccesoPorEstudianteId("alumno1", 2L));
	}

	@Test
	void profesorPuedeConsultarCualquierEstudiante() {
		Usuario profesor = new Usuario();
		profesor.setNombreUsuario("profesor1");
		profesor.setRol(RolUsuario.PROFESOR);
		when(usuarioRepository.findByNombreUsuarioIgnoreCase("profesor1")).thenReturn(Optional.of(profesor));

		assertDoesNotThrow(() -> accesoEstudianteService.validarAccesoPorEstudianteId("profesor1", 99L));
	}
}
