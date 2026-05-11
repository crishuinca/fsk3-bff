package cl.bohiggins.bff_libroclases.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.bohiggins.bff_libroclases.dto.AnotacionCreateRequest;
import cl.bohiggins.bff_libroclases.dto.AnotacionDetalleDto;
import cl.bohiggins.bff_libroclases.dto.AnotacionDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaCreateRequest;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDetalleDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDto;
import cl.bohiggins.bff_libroclases.dto.PerfilEstudianteDto;
import cl.bohiggins.bff_libroclases.service.LibroClasesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
	public PerfilEstudianteDto c_obtenerPerfilEstudiante(@PathVariable Long estudianteId) {
		return servicio.obtenerPerfilEstudiante(estudianteId);
	}

	@Operation(summary = "Perfil completo buscando estudiante por RUT")
	@GetMapping("/perfilEstudianteRut/{rut}")
	public PerfilEstudianteDto c_obtenerPerfilEstudianteRut(@PathVariable String rut) {
		return servicio.obtenerPerfilEstudiantePorRut(rut);
	}

	@Operation(summary = "Registrar una anotacion desde el frontend")
	@PostMapping("/anotaciones")
	public AnotacionDto c_crearAnotacion(@Valid @RequestBody AnotacionCreateRequest request) {
		return servicio.crearAnotacion(request);
	}

	@Operation(summary = "Registrar una asistencia desde el frontend")
	@PostMapping("/asistencias")
	public AsistenciaDto c_crearAsistencia(@Valid @RequestBody AsistenciaCreateRequest request) {
		return servicio.crearAsistencia(request);
	}
}
