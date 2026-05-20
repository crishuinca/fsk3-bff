package cl.bohiggins.bff_libroclases.service;

import cl.bohiggins.bff_libroclases.dto.AnotacionDetalleDto;
import cl.bohiggins.bff_libroclases.dto.AnotacionCreateRequest;
import cl.bohiggins.bff_libroclases.dto.AnotacionDto;
import cl.bohiggins.bff_libroclases.dto.AnotacionMsRequest;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDetalleDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaCreateRequest;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaMsRequest;
import cl.bohiggins.bff_libroclases.dto.CursoDto;
import cl.bohiggins.bff_libroclases.dto.EstudianteDto;
import cl.bohiggins.bff_libroclases.dto.PerfilEstudianteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LibroClasesService {

	@Autowired
	private AcademicoClient academicoClient;

	@Autowired
	private AsistenciaClient asistenciaClient;

	public AnotacionDetalleDto obtenerAnotacionDetalle(Long anotacionId) {
		AnotacionDto anotacion = asistenciaClient.obtenerAnotacion(anotacionId);
		EstudianteDto estudiante = academicoClient.obtenerEstudiante(anotacion.estudianteId());
		CursoDto curso = academicoClient.obtenerCurso(anotacion.cursoId());
		return new AnotacionDetalleDto(anotacion, estudiante, curso);
	}

	public AsistenciaDetalleDto obtenerAsistenciaDetalle(Long asistenciaId) {
		AsistenciaDto asistencia = asistenciaClient.obtenerAsistencia(asistenciaId);
		EstudianteDto estudiante = academicoClient.obtenerEstudiante(asistencia.estudianteId());
		CursoDto curso = academicoClient.obtenerCurso(asistencia.cursoId());
		return new AsistenciaDetalleDto(asistencia, estudiante, curso);
	}

	public PerfilEstudianteDto obtenerPerfilEstudiante(Long estudianteId) {
		EstudianteDto estudiante = academicoClient.obtenerEstudiante(estudianteId);
		CursoDto curso = obtenerCursoDesdeEstudiante(estudiante);
		List<AnotacionDto> anotaciones = asistenciaClient.obtenerAnotacionesPorEstudiante(estudianteId);
		List<AsistenciaDto> asistencias = asistenciaClient.obtenerAsistenciasPorEstudiante(estudianteId);
		return new PerfilEstudianteDto(estudiante, curso, anotaciones, asistencias);
	}

	public PerfilEstudianteDto obtenerPerfilEstudiantePorRut(String rut) {
		EstudianteDto estudiante = academicoClient.obtenerEstudiantePorRut(rut);
		CursoDto curso = obtenerCursoDesdeEstudiante(estudiante);
		if (estudiante.id() == null) {
			return new PerfilEstudianteDto(estudiante, curso, Collections.emptyList(), Collections.emptyList());
		}
		List<AnotacionDto> anotaciones = asistenciaClient.obtenerAnotacionesPorEstudiante(estudiante.id());
		List<AsistenciaDto> asistencias = asistenciaClient.obtenerAsistenciasPorEstudiante(estudiante.id());
		return new PerfilEstudianteDto(estudiante, curso, anotaciones, asistencias);
	}

	private CursoDto obtenerCursoDesdeEstudiante(EstudianteDto estudiante) {
		if (estudiante.curso() != null) {
			return estudiante.curso();
		}
		return new CursoDto(null, "Desconocido", "?", 0, "");
	}

	public AnotacionDto crearAnotacion(AnotacionCreateRequest request) {
		EstudianteDto estudiante = validarEstudianteRegistrado(request.estudianteId());
		Long cursoId = obtenerCursoIdDesdeEstudiante(estudiante);
		AnotacionMsRequest requestMs = new AnotacionMsRequest(
				cursoId,
				request.estudianteId(),
				request.fecha(),
				request.tipo(),
				request.descripcion(),
				request.registradaPor()
		);
		return asistenciaClient.crearAnotacion(requestMs);
	}

	public AsistenciaDto crearAsistencia(AsistenciaCreateRequest request) {
		EstudianteDto estudiante = validarEstudianteRegistrado(request.estudianteId());
		Long cursoId = obtenerCursoIdDesdeEstudiante(estudiante);
		AsistenciaMsRequest requestMs = new AsistenciaMsRequest(
				cursoId,
				request.estudianteId(),
				request.fecha(),
				request.estado(),
				request.observacion(),
				request.registradaPor()
		);
		return asistenciaClient.crearAsistencia(requestMs);
	}

	private EstudianteDto validarEstudianteRegistrado(Long estudianteId) {
		if (estudianteId == null || estudianteId <= 0) {
			throw new IllegalArgumentException("Debe indicar un ID de estudiante valido.");
		}

		EstudianteDto estudiante = academicoClient.consultarEstudianteExistente(estudianteId);
		if (estudiante == null || estudiante.id() == null) {
			throw new IllegalArgumentException(
					"No existe un estudiante registrado con el ID " + estudianteId + ".");
		}

		return estudiante;
	}

	private Long obtenerCursoIdDesdeEstudiante(EstudianteDto estudiante) {
		CursoDto curso = obtenerCursoDesdeEstudiante(estudiante);
		if (curso.id() == null) {
			throw new IllegalArgumentException("El estudiante no tiene curso asociado.");
		}
		return curso.id();
	}
}
