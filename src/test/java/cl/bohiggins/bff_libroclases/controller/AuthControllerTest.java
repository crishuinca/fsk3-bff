package cl.bohiggins.bff_libroclases.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.bohiggins.bff_libroclases.dto.LoginRequest;
import cl.bohiggins.bff_libroclases.dto.LoginResponse;
import cl.bohiggins.bff_libroclases.dto.UsuarioCreateRequest;
import cl.bohiggins.bff_libroclases.dto.UsuarioResponse;
import cl.bohiggins.bff_libroclases.entity.RolUsuario;
import cl.bohiggins.bff_libroclases.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AuthService authService;

	@InjectMocks
	private AuthController controller;

	@Test
	void login_delegaAlService() {
		LoginRequest request = new LoginRequest("profesor", "clave123");
		LoginResponse response = new LoginResponse("token", "Bearer",
				new UsuarioResponse(1L, "profesor", "profesor@colegiobo.cl", RolUsuario.PROFESOR, null));
		when(authService.iniciarSesion(request)).thenReturn(response);

		assertSame(response, controller.c_iniciarSesion(request));
	}

	@Test
	void crearUsuario_delegaAlService() {
		UsuarioCreateRequest request = new UsuarioCreateRequest("nuevo", "nuevo@colegio.cl", "clave123", RolUsuario.PROFESOR, null, null);
		UsuarioResponse response = new UsuarioResponse(2L, "nuevo", "nuevo@colegio.cl", RolUsuario.PROFESOR, null);
		when(authService.crearUsuario(request)).thenReturn(response);

		assertSame(response, controller.c_crearUsuario(request));
	}

	@Test
	void me_delegaAlService() {
		UsuarioResponse response = new UsuarioResponse(1L, "profesor", "profesor@colegiobo.cl", RolUsuario.PROFESOR, null);
		when(authService.obtenerUsuarioActual("profesor")).thenReturn(response);

		assertSame(response, controller.c_obtenerUsuarioActual("profesor"));
		verify(authService).obtenerUsuarioActual("profesor");
	}
}
