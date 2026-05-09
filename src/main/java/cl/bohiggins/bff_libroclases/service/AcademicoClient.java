package cl.bohiggins.bff_libroclases.service;

import cl.bohiggins.bff_libroclases.dto.CursoDto;
import cl.bohiggins.bff_libroclases.dto.EstudianteDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AcademicoClient {

	@Autowired
	@Qualifier("academicoRestClient")
	private RestClient cliente;

	@CircuitBreaker(name = "academico", fallbackMethod = "estudianteFallback")
	public EstudianteDto obtenerEstudiante(Long id) {
		return cliente.get()
				.uri("/estudianteByID/{id}", id)
				.retrieve()
				.body(EstudianteDto.class);
	}

	@CircuitBreaker(name = "academico", fallbackMethod = "cursoFallback")
	public CursoDto obtenerCurso(Long id) {
		return cliente.get()
				.uri("/cursoByID/{id}", id)
				.retrieve()
				.body(CursoDto.class);
	}

	public EstudianteDto estudianteFallback(Long id, Throwable t) {
		return new EstudianteDto(id, "(no disponible)", "Desconocido", "Desconocido", "Desconocido", "");
	}

	public CursoDto cursoFallback(Long id, Throwable t) {
		return new CursoDto(id, "Desconocido", "?", 0, "");
	}
}
