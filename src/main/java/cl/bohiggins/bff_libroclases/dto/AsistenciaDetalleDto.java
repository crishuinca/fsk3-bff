package cl.bohiggins.bff_libroclases.dto;

public record AsistenciaDetalleDto(
		AsistenciaDto asistencia,
		EstudianteDto estudiante,
		CursoDto curso
) {
}
