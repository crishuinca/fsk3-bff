package cl.bohiggins.bff_libroclases.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CursoDto(
		Long id,
		String nivel,
		String letra,
		Integer anio,
		String profesorJefeRut
) {
}
