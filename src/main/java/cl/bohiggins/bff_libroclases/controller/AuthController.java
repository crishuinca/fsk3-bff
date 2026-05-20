package cl.bohiggins.bff_libroclases.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.bohiggins.bff_libroclases.dto.LoginRequest;
import cl.bohiggins.bff_libroclases.dto.LoginResponse;
import cl.bohiggins.bff_libroclases.dto.UsuarioCreateRequest;
import cl.bohiggins.bff_libroclases.dto.UsuarioResponse;
import cl.bohiggins.bff_libroclases.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Autenticacion", description = "Login JWT y gestion de usuarios con minimo privilegio")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Operation(summary = "Iniciar sesion con usuario/correo y contrasena")
	@SecurityRequirements
	@PostMapping("/login")
	public LoginResponse c_iniciarSesion(@Valid @RequestBody LoginRequest request) {
		return authService.iniciarSesion(request);
	}

	@Operation(summary = "Listar todos los usuarios del sistema (solo INSPECTOR)")
	@GetMapping("/usuarios")
	@PreAuthorize("hasRole('INSPECTOR')")
	public List<UsuarioResponse> c_listarUsuarios() {
		return authService.listarUsuarios();
	}

	@Operation(summary = "Proximo ID estimado para un nuevo estudiante (solo INSPECTOR)")
	@GetMapping("/proximo-estudiante-id")
	@PreAuthorize("hasRole('INSPECTOR')")
	public Long c_obtenerProximoEstudianteId() {
		return authService.obtenerProximoEstudianteId();
	}

	@Operation(summary = "Crear usuario (solo INSPECTOR)")
	@PostMapping("/crearUsuario")
	@PreAuthorize("hasRole('INSPECTOR')")
	public UsuarioResponse c_crearUsuario(@Valid @RequestBody UsuarioCreateRequest request) {
		return authService.crearUsuario(request);
	}

	@Operation(summary = "Vincular ID de estudiante a un usuario alumno (solo INSPECTOR)")
	@PutMapping("/usuarios/{usuarioId}/vinculo-estudiante/{estudianteId}")
	@PreAuthorize("hasRole('INSPECTOR')")
	public UsuarioResponse c_vincularEstudianteAlumno(
			@PathVariable Long usuarioId,
			@PathVariable Long estudianteId) {
		return authService.vincularEstudianteAlumno(usuarioId, estudianteId);
	}

	@Operation(summary = "Obtener datos del usuario autenticado")
	@GetMapping("/me")
	public UsuarioResponse c_obtenerUsuarioActual(@AuthenticationPrincipal String nombreUsuario) {
		return authService.obtenerUsuarioActual(nombreUsuario);
	}
}
