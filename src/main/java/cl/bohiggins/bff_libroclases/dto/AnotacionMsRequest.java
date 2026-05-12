package cl.bohiggins.bff_libroclases.dto;

import java.time.LocalDate;

public record AnotacionMsRequest(
		Long cursoId,
		Long estudianteId,
		LocalDate fecha,
		String tipo,
		String descripcion,
		String registradaPor
) {
}
