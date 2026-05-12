package cl.bohiggins.bff_libroclases.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import cl.bohiggins.bff_libroclases.dto.AnotacionDto;
import cl.bohiggins.bff_libroclases.dto.AnotacionMsRequest;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaMsRequest;

@ExtendWith(MockitoExtension.class)
class AsistenciaClientTest {

	@Mock
	private RestClient cliente;

	@Mock
	@SuppressWarnings("rawtypes")
	private RestClient.RequestHeadersUriSpec headersUriSpec;

	@Mock
	@SuppressWarnings("rawtypes")
	private RestClient.RequestHeadersSpec headersSpec;

	@Mock
	private RestClient.RequestBodyUriSpec bodyUriSpec;

	@Mock
	private RestClient.RequestBodySpec bodySpec;

	@Mock
	private RestClient.ResponseSpec responseSpec;

	private AsistenciaClient asistenciaClient;

	@BeforeEach
	void setUp() {
		asistenciaClient = new AsistenciaClient();
		ReflectionTestUtils.setField(asistenciaClient, "cliente", cliente);
	}

	@Test
	@SuppressWarnings("unchecked")
	void obtenerAnotacion_llamaRestClient() {
		AnotacionDto anotacion = crearAnotacion();
		when(cliente.get()).thenReturn(headersUriSpec);
		when(headersUriSpec.uri("/anotacionByID/{id}", 1L)).thenReturn(headersSpec);
		when(headersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.body(AnotacionDto.class)).thenReturn(anotacion);

		AnotacionDto resultado = asistenciaClient.obtenerAnotacion(1L);

		assertSame(anotacion, resultado);
	}

	@Test
	@SuppressWarnings("unchecked")
	void obtenerAsistencia_llamaRestClient() {
		AsistenciaDto asistencia = crearAsistencia();
		when(cliente.get()).thenReturn(headersUriSpec);
		when(headersUriSpec.uri("/asistenciaByID/{id}", 1L)).thenReturn(headersSpec);
		when(headersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.body(AsistenciaDto.class)).thenReturn(asistencia);

		AsistenciaDto resultado = asistenciaClient.obtenerAsistencia(1L);

		assertSame(asistencia, resultado);
	}

	@Test
	@SuppressWarnings("unchecked")
	void obtenerAnotacionesPorEstudiante_llamaRestClient() {
		List<AnotacionDto> anotaciones = List.of(crearAnotacion());
		when(cliente.get()).thenReturn(headersUriSpec);
		when(headersUriSpec.uri("/anotacionesPorEstudiante/{id}", 2L)).thenReturn(headersSpec);
		when(headersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(anotaciones);

		List<AnotacionDto> resultado = asistenciaClient.obtenerAnotacionesPorEstudiante(2L);

		assertSame(anotaciones, resultado);
	}

	@Test
	@SuppressWarnings("unchecked")
	void obtenerAsistenciasPorEstudiante_llamaRestClient() {
		List<AsistenciaDto> asistencias = List.of(crearAsistencia());
		when(cliente.get()).thenReturn(headersUriSpec);
		when(headersUriSpec.uri("/asistenciasPorEstudiante/{id}", 2L)).thenReturn(headersSpec);
		when(headersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(asistencias);

		List<AsistenciaDto> resultado = asistenciaClient.obtenerAsistenciasPorEstudiante(2L);

		assertSame(asistencias, resultado);
	}

	@Test
	@SuppressWarnings("unchecked")
	void crearAnotacion_llamaRestClient() {
		AnotacionMsRequest request = new AnotacionMsRequest(3L, 2L, LocalDate.of(2026, 5, 8), "POSITIVA", "Participa", "12345678-9");
		AnotacionDto anotacion = crearAnotacion();
		when(cliente.post()).thenReturn(bodyUriSpec);
		when(bodyUriSpec.uri("/addAnotacion")).thenReturn(bodySpec);
		when(bodySpec.body(request)).thenReturn(bodySpec);
		when(bodySpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.body(AnotacionDto.class)).thenReturn(anotacion);

		AnotacionDto resultado = asistenciaClient.crearAnotacion(request);

		assertSame(anotacion, resultado);
	}

	@Test
	@SuppressWarnings("unchecked")
	void crearAsistencia_llamaRestClient() {
		AsistenciaMsRequest request = new AsistenciaMsRequest(3L, 2L, LocalDate.of(2026, 5, 8), "PRESENTE", "Sin observacion", "12345678-9");
		AsistenciaDto asistencia = crearAsistencia();
		when(cliente.post()).thenReturn(bodyUriSpec);
		when(bodyUriSpec.uri("/addAsistencia")).thenReturn(bodySpec);
		when(bodySpec.body(request)).thenReturn(bodySpec);
		when(bodySpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.body(AsistenciaDto.class)).thenReturn(asistencia);

		AsistenciaDto resultado = asistenciaClient.crearAsistencia(request);

		assertSame(asistencia, resultado);
	}

	private AnotacionDto crearAnotacion() {
		return new AnotacionDto(1L, LocalDate.of(2026, 5, 8), 3L, 2L, "POSITIVA", "Participa", "12345678-9");
	}

	private AsistenciaDto crearAsistencia() {
		return new AsistenciaDto(1L, LocalDate.of(2026, 5, 8), 3L, 2L, "PRESENTE", "Sin observacion", "12345678-9");
	}
}
