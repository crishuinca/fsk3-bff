package cl.bohiggins.bff_libroclases.integration;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.bohiggins.bff_libroclases.dto.CursoDto;
import cl.bohiggins.bff_libroclases.dto.EstudianteDto;
import cl.bohiggins.bff_libroclases.dto.PerfilEstudianteDto;
import cl.bohiggins.bff_libroclases.service.LibroClasesService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LibroClasesIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LibroClasesService servicio;

	private String token;

	@BeforeEach
	void login() throws Exception {
		String json = mockMvc.perform(post("/api/v1/auth/login")
				.contentType(APPLICATION_JSON)
				.content("{\"identificador\":\"profesor\",\"password\":\"clave123\"}"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		token = new ObjectMapper().readTree(json).get("token").asText();
	}

	@Test
	void perfil_sinToken() throws Exception {
		mockMvc.perform(get("/api/v1/perfilEstudiante/1"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void perfil_ok() throws Exception {
		CursoDto curso = new CursoDto(1L, "2 Medio", "A", 2026, "12345678-9");
		EstudianteDto estudiante = new EstudianteDto(1L, "21827564-8", "Cristobal", "Huinca", "Aravena", "", curso);
		when(servicio.obtenerPerfilEstudiante(1L)).thenReturn(new PerfilEstudianteDto(estudiante, curso, List.of(), List.of()));

		mockMvc.perform(get("/api/v1/perfilEstudiante/1")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.estudiante.nombres").value("Cristobal"));
	}
}
