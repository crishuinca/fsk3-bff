package cl.bohiggins.bff_libroclases.dto;

import java.time.LocalDate;

public record AsistenciaMsRequest(
		Long cursoId,
		Long estudianteId,
		LocalDate fecha,
		String estado,
		String observacion,
		String registradaPor
) {
}
