package cl.bohiggins.bff_libroclases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import cl.bohiggins.bff_libroclases.dto.EstudianteDto;
import cl.bohiggins.bff_libroclases.dto.LoginRequest;
import cl.bohiggins.bff_libroclases.dto.LoginResponse;
import cl.bohiggins.bff_libroclases.dto.UsuarioCreateRequest;
import cl.bohiggins.bff_libroclases.dto.UsuarioResponse;
import cl.bohiggins.bff_libroclases.entity.RolUsuario;
import cl.bohiggins.bff_libroclases.entity.Usuario;
import cl.bohiggins.bff_libroclases.repository.UsuarioRepository;

@Service
public class AuthService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AcademicoClient academicoClient;

	public LoginResponse iniciarSesion(LoginRequest request) {
		Usuario usuario = usuarioRepository
				.findByNombreUsuarioIgnoreCaseOrEmailIgnoreCase(request.identificador(), request.identificador())
				.orElseThrow(() -> new IllegalArgumentException("Usuario o contrasena incorrectos."));

		if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
			throw new IllegalArgumentException("Usuario o contrasena incorrectos.");
		}

		String token = jwtService.generarToken(usuario.getNombreUsuario(), usuario.getRol());
		return new LoginResponse(token, "Bearer", mapearUsuario(usuario));
	}

	public UsuarioResponse crearUsuario(UsuarioCreateRequest request) {
		if (usuarioRepository.existsByNombreUsuarioIgnoreCase(request.nombreUsuario())) {
			throw new IllegalArgumentException("El nombre de usuario ya existe.");
		}
		if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
			throw new IllegalArgumentException("El correo ya esta registrado.");
		}

		Usuario newUsuario = new Usuario();
		newUsuario.setNombreUsuario(request.nombreUsuario().trim());
		newUsuario.setEmail(request.email().trim().toLowerCase());
		newUsuario.setPassword(passwordEncoder.encode(request.password()));
		newUsuario.setRol(request.rol());
		newUsuario.setEstudianteId(resolverEstudianteId(request));

		return mapearUsuario(usuarioRepository.save(newUsuario));
	}

	public List<UsuarioResponse> listarUsuarios() {
		return usuarioRepository.findAll().stream()
				.sorted(Comparator.comparing(Usuario::getNombreUsuario, String.CASE_INSENSITIVE_ORDER))
				.map(this::mapearUsuario)
				.toList();
	}

	public UsuarioResponse vincularEstudianteAlumno(Long usuarioId, Long estudianteId) {
		Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new IllegalArgumentException("No existe el usuario indicado."));

		if (usuario.getRol() != RolUsuario.ALUMNO) {
			throw new IllegalArgumentException("Solo se puede vincular ID de estudiante a usuarios con rol ALUMNO.");
		}

		if (estudianteId == null || estudianteId <= 0) {
			throw new IllegalArgumentException("Debe indicar un ID de estudiante valido.");
		}

		validarEstudianteExistente(estudianteId);
		usuario.setEstudianteId(estudianteId);
		return mapearUsuario(usuarioRepository.save(usuario));
	}

	public UsuarioResponse obtenerUsuarioActual(String nombreUsuario) {
		Usuario usuario = usuarioRepository.findByNombreUsuarioIgnoreCase(nombreUsuario)
				.orElseThrow(() -> new IllegalArgumentException("No existe el usuario autenticado."));
		return mapearUsuario(usuario);
	}

	public Long obtenerProximoEstudianteId() {
		return academicoClient.obtenerProximoEstudianteId();
	}

	private Long resolverEstudianteId(UsuarioCreateRequest request) {
		if (request.rol() != RolUsuario.ALUMNO) {
			return null;
		}

		if (request.datosEstudiante() != null) {
			EstudianteDto creado = academicoClient.crearEstudiante(request.datosEstudiante());
			if (creado == null || creado.id() == null) {
				throw new IllegalArgumentException("No se pudo registrar el estudiante en el sistema academico.");
			}
			return creado.id();
		}

		if (request.estudianteId() != null && request.estudianteId() > 0) {
			validarEstudianteExistente(request.estudianteId());
			return request.estudianteId();
		}

		throw new IllegalArgumentException(
				"Para usuarios ALUMNO debe completar los datos del estudiante (RUT, nombres y curso).");
	}

	private void validarEstudianteExistente(Long estudianteId) {
		EstudianteDto estudiante = academicoClient.consultarEstudianteExistente(estudianteId);
		if (estudiante == null || estudiante.id() == null) {
			throw new IllegalArgumentException(
					"No existe un estudiante registrado con el ID " + estudianteId + ".");
		}
	}

	private UsuarioResponse mapearUsuario(Usuario usuario) {
		return new UsuarioResponse(
				usuario.getId(),
				usuario.getNombreUsuario(),
				usuario.getEmail(),
				usuario.getRol(),
				usuario.getEstudianteId());
	}
}
