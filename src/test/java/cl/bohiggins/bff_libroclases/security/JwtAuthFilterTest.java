package cl.bohiggins.bff_libroclases.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import cl.bohiggins.bff_libroclases.entity.RolUsuario;
import cl.bohiggins.bff_libroclases.service.JwtService;
import jakarta.servlet.FilterChain;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

	@Mock
	private JwtService jwtService;

	@Mock
	private FilterChain filterChain;

	@InjectMocks
	private JwtAuthFilter filter;

	@BeforeEach
	void limpiarContexto() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void sinAuthorization_continuaSinAutenticar() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		filter.doFilterInternal(request, response, filterChain);

		assertNull(SecurityContextHolder.getContext().getAuthentication());
		verify(filterChain).doFilter(request, response);
		verify(jwtService, never()).extraerUsuario(any());
	}

	@Test
	void tokenValido_estableceAutenticacion() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer token-valido");
		MockHttpServletResponse response = new MockHttpServletResponse();

		when(jwtService.extraerUsuario("token-valido")).thenReturn("profesor");
		when(jwtService.esTokenValido("token-valido", "profesor")).thenReturn(true);
		when(jwtService.extraerRol("token-valido")).thenReturn(RolUsuario.PROFESOR);

		filter.doFilterInternal(request, response, filterChain);

		assertNotNull(SecurityContextHolder.getContext().getAuthentication());
		assertEquals("profesor", SecurityContextHolder.getContext().getAuthentication().getName());
		verify(filterChain).doFilter(request, response);
	}
}
