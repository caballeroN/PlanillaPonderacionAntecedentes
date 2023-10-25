package iesmb.pp3.planillaProfesores.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import iesmb.pp3.planillaProfesores.entity.Profesor;
import iesmb.pp3.planillaProfesores.service.IProfesorService;
import iesmb.pp3.planillaProfesores.util.ResponseUtil;
import jakarta.validation.ConstraintViolationException;

@RestController
@RequestMapping("/profesores")
public class ProfesorController {

	@Autowired
	IProfesorService service;

	@GetMapping("")
	public ResponseEntity<APIResponse<List<Profesor>>>getAllProfesores() {

		return service.getAll().isEmpty() ? ResponseUtil.notFound("No hay profesores creados, por favor, cree algunos a trvés del método POST.")
			      : ResponseUtil.success(service.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<APIResponse<Profesor>> getProfesorById(@PathVariable("id") Integer id){
		return service.exists(id) ? ResponseUtil.success(service.getById(id))
				 : ResponseUtil.notFound("Profesor no encontrado para ese id.");
	}

	@PostMapping("")
	public ResponseEntity<APIResponse<Profesor>> createProfesor(@RequestBody Profesor profesor) {

		return service.exists(profesor.getId()) ? ResponseUtil.badRequest("Ya hay un profesor creado con ese id. Para actualizar, utilice el verbo PUT.")
				 : ResponseUtil.created(service.save(profesor));
	}

	@PutMapping("")
	public ResponseEntity<APIResponse<Profesor>> editProfesor(@RequestBody Profesor profesor) {
		return service.exists(profesor.getId()) ? ResponseUtil.success(service.save(profesor))
				 : ResponseUtil.badRequest("No existe un profesor con ese id. Para crearlo, utilice el método POST.");
		}

	@DeleteMapping("/{id}")
	public ResponseEntity<APIResponse<Boolean>> deleteProfesor(@PathVariable("id") Integer id){
		return service.exists(id) ? ResponseUtil.success(service.delete(service.getById(id).getId()))
				 : ResponseUtil.notFound("Profesor no encontrado para ese id.");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<APIResponse<Profesor>> handleException(Exception ex) {
		return ResponseUtil.badRequest(ex.getMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<APIResponse<Profesor>> handleConstraintViolationException(ConstraintViolationException ex) {
		return ResponseUtil.handleConstraintException(ex);
	}
}
