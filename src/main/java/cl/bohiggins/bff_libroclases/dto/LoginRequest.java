package cl.bohiggins.bff_libroclases.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		@Schema(description = "Nombre de usuario o correo electronico", example = "profesor1")
		@NotBlank(message = "El usuario o correo es obligatorio.")
		String identificador,

		@Schema(description = "Contrasena", example = "clave123")
		@NotBlank(message = "La contrasena es obligatoria.")
		String password
) {
}
