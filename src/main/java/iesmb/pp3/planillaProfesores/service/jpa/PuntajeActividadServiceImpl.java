package iesmb.pp3.planillaProfesores.service.jpa;

import java.util.List;
import java.util.Optional;

import iesmb.pp3.planillaProfesores.entity.Categoria;
import iesmb.pp3.planillaProfesores.entity.Profesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import iesmb.pp3.planillaProfesores.entity.PuntajeActividad;
import iesmb.pp3.planillaProfesores.repository.PuntajeActividadRepository;
import iesmb.pp3.planillaProfesores.service.IPuntajeActividadService;

@Service
public class PuntajeActividadServiceImpl implements IPuntajeActividadService{

	@Autowired
	PuntajeActividadRepository puntajeActividadRepository;

	@Override
	public List<PuntajeActividad>getAll() {

		List<PuntajeActividad> puntajeActividad = puntajeActividadRepository.findAll();
		return puntajeActividad;
	}

	@Override
	public PuntajeActividad getById(Integer id){

		Optional<PuntajeActividad> optional = puntajeActividadRepository.findById(id);
		return optional.isPresent() ? optional.get() : null;
	}

	@Override
	public PuntajeActividad save(PuntajeActividad puntajeActividad){

		return puntajeActividadRepository.save(puntajeActividad);
	}

	@Override
	public boolean delete(Integer id){

		if(puntajeActividadRepository.existsById(id)) {
			puntajeActividadRepository.deleteById(id);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean exists(Integer id) {

		return (id == null) ? false: puntajeActividadRepository.existsById(id);
	}
	public List<PuntajeActividad> obtenerPuntajesPorProfesorYCategoria(Profesor profesor, Categoria categoria) {
		return puntajeActividadRepository.findByProfesorAndActividad_Categoria(profesor, categoria);
	}

}
