package iesmb.pp3.planillaProfesores.repository;

import iesmb.pp3.planillaProfesores.entity.Categoria;
import iesmb.pp3.planillaProfesores.entity.Profesor;
import iesmb.pp3.planillaProfesores.entity.PuntajeXCategoriaValidado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuntajeXCategoriaValidadoRepository extends JpaRepository<PuntajeXCategoriaValidado, Integer> {
    PuntajeXCategoriaValidado findByProfesorAndCategoria(Profesor profesor, Categoria categoria);
}
