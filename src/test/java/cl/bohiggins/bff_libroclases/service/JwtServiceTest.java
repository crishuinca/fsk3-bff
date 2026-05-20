package cl.bohiggins.bff_libroclases.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cl.bohiggins.bff_libroclases.entity.RolUsuario;

class JwtServiceTest {

	private JwtService jwtService;

	@BeforeEach
	void setUp() {
		jwtService = new JwtService(
				"LibroClasesBffSecretKeyParaEvaluacionFullstack2026MuyLarga",
				3_600_000L);
	}

	@Test
	void generarYValidarToken_incluyeUsuarioYRol() {
		String token = jwtService.generarToken("inspector", RolUsuario.INSPECTOR);

		assertEquals("inspector", jwtService.extraerUsuario(token));
		assertEquals(RolUsuario.INSPECTOR, jwtService.extraerRol(token));
		assertTrue(jwtService.esTokenValido(token, "inspector"));
		assertFalse(jwtService.esTokenValido(token, "otro"));
	}

	@Test
	void extraerRol_deTokenProfesor() {
		String token = jwtService.generarToken("profesor", RolUsuario.PROFESOR);

		assertEquals(RolUsuario.PROFESOR, jwtService.extraerRol(token));
	}

}
