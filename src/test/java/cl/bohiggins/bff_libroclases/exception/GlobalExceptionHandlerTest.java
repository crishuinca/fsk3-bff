package cl.bohiggins.bff_libroclases.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;

class GlobalExceptionHandlerTest {

	private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

	@Test
	void negocio_retornaBadRequestConMensaje() {
		ResponseEntity<Map<String, String>> respuesta = handler.negocio(new IllegalArgumentException("Dato invalido."));

		assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
		assertNotNull(respuesta.getBody());
		assertEquals("Dato invalido.", respuesta.getBody().get("error"));
	}

	@Test
	void errorRed_retornaServiceUnavailable() {
		ResponseEntity<Map<String, String>> respuesta = handler.errorRed(new ResourceAccessException("sin conexion"));

		assertEquals(HttpStatus.SERVICE_UNAVAILABLE, respuesta.getStatusCode());
		assertNotNull(respuesta.getBody());
		assertEquals("Microservicio no disponible.", respuesta.getBody().get("error"));
	}
}
