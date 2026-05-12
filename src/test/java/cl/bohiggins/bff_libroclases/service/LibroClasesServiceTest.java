package cl.bohiggins.bff_libroclases.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.bohiggins.bff_libroclases.dto.AnotacionCreateRequest;
import cl.bohiggins.bff_libroclases.dto.AnotacionDetalleDto;
import cl.bohiggins.bff_libroclases.dto.AnotacionDto;
import cl.bohiggins.bff_libroclases.dto.AnotacionMsRequest;
import cl.bohiggins.bff_libroclases.dto.AsistenciaCreateRequest;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDetalleDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaMsRequest;
import cl.bohiggins.bff_libroclases.dto.CursoDto;
import cl.bohiggins.bff_libroclases.dto.EstudianteDto;
import cl.bohiggins.bff_libroclases.dto.PerfilEstudianteDto;

@ExtendWith(MockitoExtension.class)
class LibroClasesServiceTest {

	@Mock
	private AcademicoClient academicoClient;

	@Mock
	private AsistenciaClient asistenciaClient;

	@InjectMocks
	private LibroClasesService servicio;

	@Test
	void obtenerAnotacionDetalle_componeDatosDeMicroservicios() {
		AnotacionDto anotacion = crearAnotacion();
		EstudianteDto estudiante = crearEstudiante();
		CursoDto curso = crearCurso();
		when(asistenciaClient.obtenerAnotacion(1L)).thenReturn(anotacion);
		when(academicoClient.obtenerEstudiante(2L)).thenReturn(estudiante);
		when(academicoClient.obtenerCurso(3L)).thenReturn(curso);

		AnotacionDetalleDto resultado = servicio.obtenerAnotacionDetalle(1L);

		assertSame(anotacion, resultado.anotacion());
		assertSame(estudiante, resultado.estudiante());
		assertSame(curso, resultado.curso());
	}

	@Test
	void obtenerAsistenciaDetalle_componeDatosDeMicroservicios() {
		AsistenciaDto asistencia = crearAsistencia();
		EstudianteDto estudiante = crearEstudiante();
		CursoDto curso = crearCurso();
		when(asistenciaClient.obtenerAsistencia(1L)).thenReturn(asistencia);
		when(academicoClient.obtenerEstudiante(2L)).thenReturn(estudiante);
		when(academicoClient.obtenerCurso(3L)).thenReturn(curso);

		AsistenciaDetalleDto resultado = servicio.obtenerAsistenciaDetalle(1L);

		assertSame(asistencia, resultado.asistencia());
		assertSame(estudiante, resultado.estudiante());
		assertSame(curso, resultado.curso());
	}

	@Test
	void obtenerPerfilEstudiante_usaCursoDelEstudianteYListasDeAsistencia() {
		EstudianteDto estudiante = crearEstudiante();
		List<AnotacionDto> anotaciones = List.of(crearAnotacion());
		List<AsistenciaDto> asistencias = List.of(crearAsistencia());
		when(academicoClient.obtenerEstudiante(2L)).thenReturn(estudiante);
		when(asistenciaClient.obtenerAnotacionesPorEstudiante(2L)).thenReturn(anotaciones);
		when(asistenciaClient.obtenerAsistenciasPorEstudiante(2L)).thenReturn(asistencias);

		PerfilEstudianteDto resultado = servicio.obtenerPerfilEstudiante(2L);

		assertSame(estudiante, resultado.estudiante());
		assertSame(estudiante.curso(), resultado.curso());
		assertSame(anotaciones, resultado.anotaciones());
		assertSame(asistencias, resultado.asistencias());
	}

	@Test
	void obtenerPerfilEstudiantePorRut_sinId_retornaListasVacias() {
		EstudianteDto estudiante = new EstudianteDto(null, "21827564-8", "Desconocido", "Desconocido", "Desconocido", "", null);
		when(academicoClient.obtenerEstudiantePorRut("21827564-8")).thenReturn(estudiante);

		PerfilEstudianteDto resultado = servicio.obtenerPerfilEstudiantePorRut("21827564-8");

		assertSame(estudiante, resultado.estudiante());
		assertEquals(0, resultado.anotaciones().size());
		assertEquals(0, resultado.asistencias().size());
		assertEquals("Desconocido", resultado.curso().nivel());
	}

	@Test
	void crearAnotacion_completaCursoAntesDeEnviarAMicroservicio() {
		EstudianteDto estudiante = crearEstudiante();
		AnotacionCreateRequest request = new AnotacionCreateRequest(
				2L,
				LocalDate.of(2026, 5, 8),
				"POSITIVA",
				"Participa activamente",
				"12345678-9"
		);
		AnotacionDto creada = crearAnotacion();
		when(academicoClient.obtenerEstudiante(2L)).thenReturn(estudiante);
		when(asistenciaClient.crearAnotacion(any(AnotacionMsRequest.class))).thenReturn(creada);

		AnotacionDto resultado = servicio.crearAnotacion(request);

		ArgumentCaptor<AnotacionMsRequest> captor = ArgumentCaptor.forClass(AnotacionMsRequest.class);
		verify(asistenciaClient).crearAnotacion(captor.capture());
		assertSame(creada, resultado);
		assertEquals(3L, captor.getValue().cursoId());
		assertEquals(2L, captor.getValue().estudianteId());
		assertEquals("POSITIVA", captor.getValue().tipo());
	}

	@Test
	void crearAsistencia_sinCursoEnEstudiante_lanzaError() {
		EstudianteDto estudiante = new EstudianteDto(2L, "21827564-8", "Cristobal", "Huinca", "Aravena", "", null);
		AsistenciaCreateRequest request = new AsistenciaCreateRequest(
				2L,
				LocalDate.of(2026, 5, 8),
				"PRESENTE",
				"Sin observacion",
				"12345678-9"
		);
		when(academicoClient.obtenerEstudiante(2L)).thenReturn(estudiante);

		IllegalArgumentException error = assertThrows(
				IllegalArgumentException.class,
				() -> servicio.crearAsistencia(request)
		);

		assertEquals("El estudiante no tiene curso asociado.", error.getMessage());
	}

	private EstudianteDto crearEstudiante() {
		return new EstudianteDto(2L, "21827564-8", "Cristobal", "Huinca", "Aravena", "cristobal@colegio.cl", crearCurso());
	}

	private CursoDto crearCurso() {
		return new CursoDto(3L, "2 Medio", "A", 2026, "12345678-9");
	}

	private AnotacionDto crearAnotacion() {
		return new AnotacionDto(1L, LocalDate.of(2026, 5, 8), 3L, 2L, "POSITIVA", "Participa activamente", "12345678-9");
	}

	private AsistenciaDto crearAsistencia() {
		return new AsistenciaDto(1L, LocalDate.of(2026, 5, 8), 3L, 2L, "PRESENTE", "Sin observacion", "12345678-9");
	}
}
