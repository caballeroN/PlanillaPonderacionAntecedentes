package iesmb.pp3.planillaProfesores.repository;

import iesmb.pp3.planillaProfesores.entity.Categoria;
import iesmb.pp3.planillaProfesores.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

import iesmb.pp3.planillaProfesores.entity.PuntajeActividad;

import java.util.List;


public interface PuntajeActividadRepository extends JpaRepository <PuntajeActividad, Integer>{
    List<PuntajeActividad> findByProfesorId(Integer profesorId);

    List<PuntajeActividad> findByProfesorAndActividad_Categoria(Profesor profesor, Categoria categoria);


}
