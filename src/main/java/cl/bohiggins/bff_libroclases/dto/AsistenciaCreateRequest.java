package cl.bohiggins.bff_libroclases.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AsistenciaCreateRequest(
		@NotNull Long cursoId,
		@NotNull Long estudianteId,
		@NotNull LocalDate fecha,
		@NotBlank String estado,
		String observacion,
		@NotBlank String registradaPor
) {
}
