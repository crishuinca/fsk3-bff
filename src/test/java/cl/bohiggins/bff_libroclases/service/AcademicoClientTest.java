package cl.bohiggins.bff_libroclases.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import cl.bohiggins.bff_libroclases.dto.CursoDto;
import cl.bohiggins.bff_libroclases.dto.EstudianteDto;

@ExtendWith(MockitoExtension.class)
class AcademicoClientTest {

	@Mock
	private RestClient cliente;

	@Mock
	@SuppressWarnings("rawtypes")
	private RestClient.RequestHeadersUriSpec headersUriSpec;

	@Mock
	@SuppressWarnings("rawtypes")
	private RestClient.RequestHeadersSpec headersSpec;

	@Mock
	private RestClient.ResponseSpec responseSpec;

	private AcademicoClient academicoClient;

	@BeforeEach
	void setUp() {
		academicoClient = new AcademicoClient();
		ReflectionTestUtils.setField(academicoClient, "cliente", cliente);
	}

	@Test
	@SuppressWarnings("unchecked")
	void obtenerEstudiante_llamaRestClient() {
		EstudianteDto estudiante = new EstudianteDto(1L, "21827564-8", "Cristobal", "Huinca", "Aravena", "", null);
		when(cliente.get()).thenReturn(headersUriSpec);
		when(headersUriSpec.uri("/estudianteByID/{id}", 1L)).thenReturn(headersSpec);
		when(headersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.body(EstudianteDto.class)).thenReturn(estudiante);

		EstudianteDto resultado = academicoClient.obtenerEstudiante(1L);

		assertSame(estudiante, resultado);
	}

	@Test
	@SuppressWarnings("unchecked")
	void obtenerEstudiantePorRut_llamaRestClient() {
		EstudianteDto estudiante = new EstudianteDto(1L, "21827564-8", "Cristobal", "Huinca", "Aravena", "", null);
		when(cliente.get()).thenReturn(headersUriSpec);
		when(headersUriSpec.uri("/estudianteByRut/{rut}", "21827564-8")).thenReturn(headersSpec);
		when(headersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.body(EstudianteDto.class)).thenReturn(estudiante);

		EstudianteDto resultado = academicoClient.obtenerEstudiantePorRut("21827564-8");

		assertSame(estudiante, resultado);
	}

	@Test
	@SuppressWarnings("unchecked")
	void obtenerCurso_llamaRestClient() {
		CursoDto curso = new CursoDto(1L, "2 Medio", "A", 2026, "12345678-9");
		when(cliente.get()).thenReturn(headersUriSpec);
		when(headersUriSpec.uri("/cursoByID/{id}", 1L)).thenReturn(headersSpec);
		when(headersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.body(CursoDto.class)).thenReturn(curso);

		CursoDto resultado = academicoClient.obtenerCurso(1L);

		assertSame(curso, resultado);
	}
}
