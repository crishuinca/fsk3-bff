package cl.bohiggins.bff_libroclases.dto;

import cl.bohiggins.bff_libroclases.entity.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioCreateRequest(
		@Schema(example = "profesor1")
		@NotBlank(message = "El nombre de usuario es obligatorio.")
		@Size(max = 50, message = "El nombre de usuario no puede superar 50 caracteres.")
		String nombreUsuario,

		@Schema(example = "profesor1@colegio.cl")
		@NotBlank(message = "El correo es obligatorio.")
		@Email(message = "El correo no tiene un formato valido.")
		String email,

		@Schema(example = "clave123")
		@NotBlank(message = "La contrasena es obligatoria.")
		@Size(min = 6, max = 100, message = "La contrasena debe tener entre 6 y 100 caracteres.")
		String password,

		@Schema(example = "PROFESOR")
		@NotNull(message = "El rol es obligatorio.")
		RolUsuario rol,

		@Schema(description = "ID existente en ms-academico (solo si el estudiante ya fue creado)", example = "1")
		Long estudianteId,

		@Schema(description = "Datos para crear el estudiante automaticamente (rol ALUMNO)")
		EstudianteAlumnoCreateRequest datosEstudiante
) {
}
