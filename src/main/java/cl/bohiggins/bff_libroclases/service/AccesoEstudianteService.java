package cl.bohiggins.bff_libroclases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import cl.bohiggins.bff_libroclases.entity.RolUsuario;
import cl.bohiggins.bff_libroclases.entity.Usuario;
import cl.bohiggins.bff_libroclases.repository.UsuarioRepository;

@Service
public class AccesoEstudianteService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario obtenerUsuarioAutenticado(String nombreUsuario) {
		return usuarioRepository.findByNombreUsuarioIgnoreCase(nombreUsuario)
				.orElseThrow(() -> new IllegalArgumentException("No existe el usuario autenticado."));
	}

	public void validarAccesoPorEstudianteId(String nombreUsuario, Long estudianteIdSolicitado) {
		Usuario usuario = obtenerUsuarioAutenticado(nombreUsuario);
		if (usuario.getRol() != RolUsuario.ALUMNO) {
			return;
		}

		if (usuario.getEstudianteId() == null) {
			throw new AccessDeniedException("Su usuario alumno no tiene un ID de estudiante vinculado.");
		}

		if (estudianteIdSolicitado == null || !usuario.getEstudianteId().equals(estudianteIdSolicitado)) {
			throw new AccessDeniedException("Solo puede consultar sus propios datos de estudiante.");
		}
	}

	public void validarBusquedaPorRut(String nombreUsuario) {
		Usuario usuario = obtenerUsuarioAutenticado(nombreUsuario);
		if (usuario.getRol() == RolUsuario.ALUMNO) {
			throw new AccessDeniedException("Los alumnos solo pueden consultar su perfil vinculado por ID.");
		}
	}
}
