package cl.bohiggins.bff_libroclases.dto;

public record LoginResponse(
		String token,
		String tipo,
		UsuarioResponse usuario
) {
}
