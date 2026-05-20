package cl.bohiggins.bff_libroclases.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos del estudiante a registrar en ms-academico al crear un usuario ALUMNO")
public record EstudianteAlumnoCreateRequest(
		@NotNull(message = "El curso es obligatorio.")
		@Schema(example = "1")
		Long cursoId,

		@NotBlank(message = "El RUT es obligatorio.")
		@Schema(example = "21345678-9")
		String rut,

		@NotBlank(message = "Los nombres son obligatorios.")
		@Schema(example = "Maria")
		String nombres,

		@NotBlank(message = "El apellido paterno es obligatorio.")
		@Schema(example = "Perez")
		String apellidoPaterno,

		@NotBlank(message = "El apellido materno es obligatorio.")
		@Schema(example = "Gonzalez")
		String apellidoMaterno,

		@Schema(example = "2010-05-15")
		LocalDate fechaNacimiento,

		@Schema(example = "maria.perez@colegio.cl")
		String email
) {
}
