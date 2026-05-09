package cl.bohiggins.bff_libroclases.service;

import cl.bohiggins.bff_libroclases.dto.AnotacionDetalleDto;
import cl.bohiggins.bff_libroclases.dto.AnotacionDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDetalleDto;
import cl.bohiggins.bff_libroclases.dto.AsistenciaDto;
import cl.bohiggins.bff_libroclases.dto.CursoDto;
import cl.bohiggins.bff_libroclases.dto.EstudianteDto;
import cl.bohiggins.bff_libroclases.dto.PerfilEstudianteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public PerfilEstudianteDto obtenerPerfilEstudiante(Long estudianteId, Long cursoId) {
		EstudianteDto estudiante = academicoClient.obtenerEstudiante(estudianteId);
		CursoDto curso = academicoClient.obtenerCurso(cursoId);
		List<AnotacionDto> anotaciones = asistenciaClient.obtenerAnotacionesPorEstudiante(estudianteId);
		List<AsistenciaDto> asistencias = asistenciaClient.obtenerAsistenciasPorEstudiante(estudianteId);
		return new PerfilEstudianteDto(estudiante, curso, anotaciones, asistencias);
	}
}
