package cl.bohiggins.bff_libroclases.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cl.bohiggins.bff_libroclases.entity.RolUsuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final SecretKey secretKey;
	private final long expirationMs;

	public JwtService(
			@Value("${app.jwt.secret}") String secret,
			@Value("${app.jwt.expiration-ms}") long expirationMs) {
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationMs = expirationMs;
	}

	public String generarToken(String nombreUsuario, RolUsuario rol) {
		Date ahora = new Date();
		Date expiracion = new Date(ahora.getTime() + expirationMs);

		return Jwts.builder()
				.subject(nombreUsuario)
				.claim("rol", rol.name())
				.issuedAt(ahora)
				.expiration(expiracion)
				.signWith(secretKey)
				.compact();
	}

	public String extraerUsuario(String token) {
		return extraerClaims(token).getSubject();
	}

	public RolUsuario extraerRol(String token) {
		String rol = extraerClaims(token).get("rol", String.class);
		return RolUsuario.valueOf(rol);
	}

	public boolean esTokenValido(String token, String nombreUsuario) {
		String usuarioToken = extraerUsuario(token);
		return usuarioToken.equals(nombreUsuario) && !estaExpirado(token);
	}

	private boolean estaExpirado(String token) {
		return extraerClaims(token).getExpiration().before(new Date());
	}

	private Claims extraerClaims(String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}
