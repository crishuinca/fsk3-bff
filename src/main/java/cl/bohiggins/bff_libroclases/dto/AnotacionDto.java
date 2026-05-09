package cl.bohiggins.bff_libroclases.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AnotacionDto(
		Long id,
		LocalDate fecha,
		Long cursoId,
		Long estudianteId,
		String tipo,
		String descripcion,
		String registradaPor
) {
}
