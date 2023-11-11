package iesmb.pp3.planillaProfesores.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import iesmb.pp3.planillaProfesores.entity.PuntajeActividad;

import java.util.List;


public interface PuntajeActividadRepository extends JpaRepository <PuntajeActividad, Integer>{
    List<PuntajeActividad> findByProfesorId(Integer profesorId);

}
