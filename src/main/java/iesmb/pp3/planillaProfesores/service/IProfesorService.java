package iesmb.pp3.planillaProfesores.service;

import java.util.List;

import iesmb.pp3.planillaProfesores.entity.Profesor;

public interface IProfesorService {

	public List<Profesor> getAll();

	public Profesor getById (Integer id);

	public Profesor save (Profesor profesor);

	public boolean delete (Integer id);
	
	boolean exists(Integer id);
}
