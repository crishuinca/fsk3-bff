package cl.bohiggins.bff_libroclases.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String ERROR = "error";

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> negocio(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().body(Map.of(ERROR, ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> validacion(MethodArgumentNotValidException ex) {
		StringBuilder msg = new StringBuilder();
		for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
			if (!msg.isEmpty()) {
				msg.append(" | ");
			}
			msg.append(fe.getField()).append(": ").append(fe.getDefaultMessage());
		}
		return ResponseEntity.badRequest().body(Map.of(ERROR, msg.toString()));
	}

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<Map<String, String>> errorClienteAguasArriba(HttpClientErrorException ex) {
		return ResponseEntity.status(ex.getStatusCode()).body(Map.of(ERROR, "Error en microservicio: " + ex.getMessage()));
	}

	@ExceptionHandler(HttpServerErrorException.class)
	public ResponseEntity<Map<String, String>> errorServidorAguasArriba(HttpServerErrorException ex) {
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(ERROR, "Falla del microservicio: " + ex.getMessage()));
	}

	@ExceptionHandler(ResourceAccessException.class)
	public ResponseEntity<Map<String, String>> errorRed(ResourceAccessException ex) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(ERROR, "Microservicio no disponible."));
	}
}
