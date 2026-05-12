package cl.bohiggins.bff_libroclases.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cl.bohiggins.bff_libroclases.dto.AnotacionDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDto;
import cl.bohiggins.bff_libroclases.dto.CursoDto;
import cl.bohiggins.bff_libroclases.dto.EstudianteDto;

class ClientFallbackTest {

	private final AcademicoClient academicoClient = new AcademicoClient();
	private final AsistenciaClient asistenciaClient = new AsistenciaClient();

	@Test
	void academicoFallbacks_retornaDatosPlaceholder() {
		EstudianteDto estudiante = academicoClient.estudianteFallback(1L, new RuntimeException("error"));
		EstudianteDto estudianteRut = academicoClient.estudiantePorRutFallback("21827564-8", new RuntimeException("error"));
		CursoDto curso = academicoClient.cursoFallback(2L, new RuntimeException("error"));

		assertEquals(1L, estudiante.id());
		assertEquals("(no disponible)", estudiante.rut());
		assertNull(estudianteRut.id());
		assertEquals("21827564-8", estudianteRut.rut());
		assertEquals(2L, curso.id());
		assertEquals("Desconocido", curso.nivel());
	}

	@Test
	void asistenciaFallbacks_retornaDatosPlaceholderYListasVacias() {
		AnotacionDto anotacion = asistenciaClient.anotacionFallback(1L, new RuntimeException("error"));
		AsistenciaDto asistencia = asistenciaClient.asistenciaFallback(2L, new RuntimeException("error"));

		assertEquals(1L, anotacion.id());
		assertEquals("DESCONOCIDA", anotacion.tipo());
		assertEquals(2L, asistencia.id());
		assertEquals("DESCONOCIDO", asistencia.estado());
		assertTrue(asistenciaClient.listaAnotacionesFallback(1L, new RuntimeException("error")).isEmpty());
		assertTrue(asistenciaClient.listaAsistenciasFallback(1L, new RuntimeException("error")).isEmpty());
	}
}
