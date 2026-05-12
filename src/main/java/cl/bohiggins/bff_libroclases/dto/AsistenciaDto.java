package cl.bohiggins.bff_libroclases.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AsistenciaDto(
		Long id,
		LocalDate fecha,
		Long cursoId,
		Long estudianteId,
		String estado,
		String observacion,
		String registradaPor
) {
}
