package cl.bohiggins.bff_libroclases.service;

import cl.bohiggins.bff_libroclases.dto.AnotacionDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Service
public class AsistenciaClient {

	@Autowired
	@Qualifier("asistenciaRestClient")
	private RestClient cliente;

	@CircuitBreaker(name = "asistencia", fallbackMethod = "anotacionFallback")
	public AnotacionDto obtenerAnotacion(Long id) {
		return cliente.get()
				.uri("/anotacionByID/{id}", id)
				.retrieve()
				.body(AnotacionDto.class);
	}

	@CircuitBreaker(name = "asistencia", fallbackMethod = "asistenciaFallback")
	public AsistenciaDto obtenerAsistencia(Long id) {
		return cliente.get()
				.uri("/asistenciaByID/{id}", id)
				.retrieve()
				.body(AsistenciaDto.class);
	}

	@CircuitBreaker(name = "asistencia", fallbackMethod = "listaAnotacionesFallback")
	public List<AnotacionDto> obtenerAnotacionesPorEstudiante(Long estudianteId) {
		return cliente.get()
				.uri("/anotacionesPorEstudiante/{id}", estudianteId)
				.retrieve()
				.body(new ParameterizedTypeReference<List<AnotacionDto>>() {});
	}

	@CircuitBreaker(name = "asistencia", fallbackMethod = "listaAsistenciasFallback")
	public List<AsistenciaDto> obtenerAsistenciasPorEstudiante(Long estudianteId) {
		return cliente.get()
				.uri("/asistenciasPorEstudiante/{id}", estudianteId)
				.retrieve()
				.body(new ParameterizedTypeReference<List<AsistenciaDto>>() {});
	}

	public AnotacionDto anotacionFallback(Long id, Throwable t) {
		return new AnotacionDto(id, null, null, null, "DESCONOCIDA", "(servicio no disponible)", "");
	}

	public AsistenciaDto asistenciaFallback(Long id, Throwable t) {
		return new AsistenciaDto(id, null, null, null, "DESCONOCIDO", "(servicio no disponible)", "");
	}

	public List<AnotacionDto> listaAnotacionesFallback(Long estudianteId, Throwable t) {
		return Collections.emptyList();
	}

	public List<AsistenciaDto> listaAsistenciasFallback(Long estudianteId, Throwable t) {
		return Collections.emptyList();
	}
}
