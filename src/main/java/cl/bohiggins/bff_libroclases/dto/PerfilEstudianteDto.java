package cl.bohiggins.bff_libroclases.dto;

import java.util.List;

public record PerfilEstudianteDto(
		EstudianteDto estudiante,
		CursoDto curso,
		List<AnotacionDto> anotaciones,
		List<AsistenciaDto> asistencias
) {
}
