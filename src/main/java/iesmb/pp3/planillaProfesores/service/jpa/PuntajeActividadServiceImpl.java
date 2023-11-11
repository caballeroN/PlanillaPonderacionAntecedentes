package iesmb.pp3.planillaProfesores.service.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import iesmb.pp3.planillaProfesores.entity.PuntajeActividad;
import iesmb.pp3.planillaProfesores.repository.PuntajeActividadRepository;
import iesmb.pp3.planillaProfesores.service.IPuntajeActividadService;

@Service
public class PuntajeActividadServiceImpl implements IPuntajeActividadService{

	@Autowired
	PuntajeActividadRepository repo;



	@Override
	public List<PuntajeActividad>getAll() {

		List<PuntajeActividad> puntajeActividad = repo.findAll();
		return puntajeActividad;
	}

	@Override
	public PuntajeActividad getById(Integer id){

		Optional<PuntajeActividad> optional = repo.findById(id);
		return optional.isPresent() ? optional.get() : null;
	}

	@Override
	public PuntajeActividad save(PuntajeActividad puntajeActividad){

		return repo.save(puntajeActividad);
	}

	@Override
	public boolean delete(Integer id){

		if(repo.existsById(id)) {
			repo.deleteById(id);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean exists(Integer id) {

		return (id == null) ? false: repo.existsById(id);
	}


	@Override
	public List<PuntajeActividad> getPuntajesActividadPorProfesor(Integer profesorId) {
		return repo.findByProfesorId(profesorId);
	}
}
