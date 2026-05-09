package cl.bohiggins.bff_libroclases.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.bohiggins.bff_libroclases.dto.AnotacionDetalleDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDetalleDto;
import cl.bohiggins.bff_libroclases.dto.PerfilEstudianteDto;
import cl.bohiggins.bff_libroclases.service.LibroClasesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "BFF Libro de Clases", description = "Endpoints compuestos que orquestan ms-academico y ms-asistencia con Circuit Breaker")
public class LibroClasesController {

	@Autowired
	private LibroClasesService servicio;

	@Operation(summary = "Detalle de la anotacion con datos del estudiante y del curso")
	@GetMapping("/anotacionDetalle/{id}")
	public AnotacionDetalleDto c_obtenerAnotacionDetalle(@PathVariable Long id) {
		return servicio.obtenerAnotacionDetalle(id);
	}

	@Operation(summary = "Detalle de la asistencia con datos del estudiante y del curso")
	@GetMapping("/asistenciaDetalle/{id}")
	public AsistenciaDetalleDto c_obtenerAsistenciaDetalle(@PathVariable Long id) {
		return servicio.obtenerAsistenciaDetalle(id);
	}

	@Operation(summary = "Perfil completo: estudiante + curso + anotaciones + asistencias")
	@GetMapping("/perfilEstudiante/{estudianteId}")
	public PerfilEstudianteDto c_obtenerPerfilEstudiante(
			@PathVariable Long estudianteId,
			@RequestParam Long cursoId) {
		return servicio.obtenerPerfilEstudiante(estudianteId, cursoId);
	}
}
