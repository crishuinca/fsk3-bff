package cl.bohiggins.bff_libroclases.integration;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void login_retornaToken() throws Exception {
		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(APPLICATION_JSON)
				.content("{\"identificador\":\"profesor\",\"password\":\"clave123\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").isNotEmpty())
				.andExpect(jsonPath("$.usuario.nombreUsuario").value("profesor"));
	}

	@Test
	void login_passwordIncorrecto() throws Exception {
		mockMvc.perform(post("/api/v1/auth/login")
				.contentType(APPLICATION_JSON)
				.content("{\"identificador\":\"profesor\",\"password\":\"mal\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void me_sinToken() throws Exception {
		mockMvc.perform(get("/api/v1/auth/me"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void me_conToken() throws Exception {
		String loginJson = mockMvc.perform(post("/api/v1/auth/login")
				.contentType(APPLICATION_JSON)
				.content("{\"identificador\":\"profesor\",\"password\":\"clave123\"}"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		String token = new ObjectMapper().readTree(loginJson).get("token").asText();

		mockMvc.perform(get("/api/v1/auth/me")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nombreUsuario").value("profesor"));
	}
}
