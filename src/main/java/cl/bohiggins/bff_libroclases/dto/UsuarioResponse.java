package cl.bohiggins.bff_libroclases.dto;

import cl.bohiggins.bff_libroclases.entity.RolUsuario;

public record UsuarioResponse(
		Long id,
		String nombreUsuario,
		String email,
		RolUsuario rol,
		Long estudianteId
) {
}
