package cl.bohiggins.bff_libroclases.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnotacionCreateRequest(
		@NotNull Long cursoId,
		@NotNull Long estudianteId,
		@NotNull LocalDate fecha,
		@NotBlank String tipo,
		@NotBlank String descripcion,
		@NotBlank String registradaPor
) {
}
