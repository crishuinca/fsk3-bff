package cl.bohiggins.bff_libroclases.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import cl.bohiggins.bff_libroclases.entity.RolUsuario;
import cl.bohiggins.bff_libroclases.entity.Usuario;
import cl.bohiggins.bff_libroclases.repository.UsuarioRepository;

@Component
public class ProfesorInicialSeeder {

	private static final Logger log = LoggerFactory.getLogger(ProfesorInicialSeeder.class);

	@Value("${app.profesor-inicial.nombre-usuario}")
	private String nombreUsuario;

	@Value("${app.profesor-inicial.email}")
	private String email;

	@Value("${app.profesor-inicial.password}")
	private String password;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@EventListener(ApplicationReadyEvent.class)
	public void asegurarProfesorInicial() {
		if (existeProfesorEnBaseDeDatos()) {
			log.info("Usuario profesor ya registrado ({}). No se crea uno nuevo.", email);
			return;
		}

		Usuario newUsuario = new Usuario();
		newUsuario.setNombreUsuario(nombreUsuario.trim());
		newUsuario.setEmail(email.trim().toLowerCase());
		newUsuario.setPassword(passwordEncoder.encode(password));
		newUsuario.setRol(RolUsuario.PROFESOR);
		usuarioRepository.save(newUsuario);

		log.info("Usuario profesor creado al iniciar: usuario={}, email={}", nombreUsuario, email);
	}

	private boolean existeProfesorEnBaseDeDatos() {
		return usuarioRepository.findByEmailIgnoreCase(email).isPresent()
				|| usuarioRepository.existsByNombreUsuarioIgnoreCase(nombreUsuario);
	}
}
