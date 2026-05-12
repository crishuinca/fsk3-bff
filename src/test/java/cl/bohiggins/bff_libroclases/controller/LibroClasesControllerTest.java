package cl.bohiggins.bff_libroclases.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.bohiggins.bff_libroclases.dto.AnotacionCreateRequest;
import cl.bohiggins.bff_libroclases.dto.AnotacionDetalleDto;
import cl.bohiggins.bff_libroclases.dto.AnotacionDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaCreateRequest;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDetalleDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDto;
import cl.bohiggins.bff_libroclases.dto.CursoDto;
import cl.bohiggins.bff_libroclases.dto.EstudianteDto;
import cl.bohiggins.bff_libroclases.dto.PerfilEstudianteDto;
import cl.bohiggins.bff_libroclases.service.LibroClasesService;

@ExtendWith(MockitoExtension.class)
class LibroClasesControllerTest {

	@Mock
	private LibroClasesService servicio;

	@InjectMocks
	private LibroClasesController controller;

	@Test
	void controller_delegaOperacionesAlService() {
		CursoDto curso = new CursoDto(3L, "2 Medio", "A", 2026, "12345678-9");
		EstudianteDto estudiante = new EstudianteDto(2L, "21827564-8", "Cristobal", "Huinca", "Aravena", "", curso);
		AnotacionDto anotacion = new AnotacionDto(1L, LocalDate.of(2026, 5, 8), 3L, 2L, "POSITIVA", "Participa", "12345678-9");
		AsistenciaDto asistencia = new AsistenciaDto(1L, LocalDate.of(2026, 5, 8), 3L, 2L, "PRESENTE", "Sin observacion", "12345678-9");
		AnotacionDetalleDto anotacionDetalle = new AnotacionDetalleDto(anotacion, estudiante, curso);
		AsistenciaDetalleDto asistenciaDetalle = new AsistenciaDetalleDto(asistencia, estudiante, curso);
		PerfilEstudianteDto perfil = new PerfilEstudianteDto(estudiante, curso, List.of(anotacion), List.of(asistencia));
		AnotacionCreateRequest anotacionRequest = new AnotacionCreateRequest(2L, LocalDate.of(2026, 5, 8), "POSITIVA", "Participa", "12345678-9");
		AsistenciaCreateRequest asistenciaRequest = new AsistenciaCreateRequest(2L, LocalDate.of(2026, 5, 8), "PRESENTE", "Sin observacion", "12345678-9");

		when(servicio.obtenerAnotacionDetalle(1L)).thenReturn(anotacionDetalle);
		when(servicio.obtenerAsistenciaDetalle(1L)).thenReturn(asistenciaDetalle);
		when(servicio.obtenerPerfilEstudiante(2L)).thenReturn(perfil);
		when(servicio.obtenerPerfilEstudiantePorRut("21827564-8")).thenReturn(perfil);
		when(servicio.crearAnotacion(anotacionRequest)).thenReturn(anotacion);
		when(servicio.crearAsistencia(asistenciaRequest)).thenReturn(asistencia);

		assertSame(anotacionDetalle, controller.c_obtenerAnotacionDetalle(1L));
		assertSame(asistenciaDetalle, controller.c_obtenerAsistenciaDetalle(1L));
		assertSame(perfil, controller.c_obtenerPerfilEstudiante(2L));
		assertSame(perfil, controller.c_obtenerPerfilEstudianteRut("21827564-8"));
		assertSame(anotacion, controller.c_crearAnotacion(anotacionRequest));
		assertSame(asistencia, controller.c_crearAsistencia(asistenciaRequest));
	}
}
