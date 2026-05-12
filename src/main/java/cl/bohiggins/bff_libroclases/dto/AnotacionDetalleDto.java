package cl.bohiggins.bff_libroclases.dto;

public record AnotacionDetalleDto(
		AnotacionDto anotacion,
		EstudianteDto estudiante,
		CursoDto curso
) {
}
