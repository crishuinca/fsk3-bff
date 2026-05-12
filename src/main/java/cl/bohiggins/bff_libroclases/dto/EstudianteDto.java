package cl.bohiggins.bff_libroclases.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EstudianteDto(
		Long id,
		String rut,
		String nombres,
		String apellidoPaterno,
		String apellidoMaterno,
		String email,
		CursoDto curso
) {
}
