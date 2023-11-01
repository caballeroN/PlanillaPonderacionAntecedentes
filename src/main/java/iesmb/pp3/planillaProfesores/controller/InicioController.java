package iesmb.pp3.planillaProfesores.controller;

import iesmb.pp3.planillaProfesores.entity.Profesor;
import iesmb.pp3.planillaProfesores.service.IProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/inicio")
public class InicioController {

    @Autowired
    IProfesorService service;

    @GetMapping("/profesores")
    public String metodoPrueba(ModelMap model) {
        List<Profesor> profe = service.getAll();
        model.addAttribute("profesores", profe);
        return "profesores";
    }

    @GetMapping("/xdni")
    public String buscarXdni(@RequestParam Integer id, ModelMap model) {
        System.out.println("llegue al profesor ");
        Profesor  profe = service.getById(id);
        model.addAttribute("profesor", profe);
        return "profesor";
    }

    @GetMapping("/borrar")
    public String borrarxdni(@RequestParam Integer id, ModelMap model) {
        System.out.println("llegue al borrar profesor ");
        service.delete(id);
        model.addAttribute("profesor", "profe borrado");
        return "profesordelete";
    }

    @GetMapping("/new_profe")
    public String nuevo_profe(ModelMap model) {
        System.out.println("llegue al cargar un nuevo profesor ");
        model.addAttribute("profesor", "profe borrado");
        return "cargar_profesor";
    }
    @PostMapping("/buscarxid")
    public String buscarProfeXId(@RequestParam Integer id, ModelMap model) {
        System.out.println("llegue al buscar x id");
        Profesor  profe = service.getById(id);
        if(profe != null){
            model.addAttribute("id", profe.getId());
            return buscarXdni(profe.getId(), model);
        }else {
            return "datos_personales";
        }
    }
    @PostMapping("/guardar_profe")
    public String guardar_profe(@RequestParam String nombre,
                                @RequestParam String apellido,
                                @RequestParam String direccion,
                                @RequestParam String telefono,
                                @RequestParam String dni,
                                ModelMap model) {
        System.out.println("llegue al guardar profe su nombre es  "+ nombre);
        Profesor newProfe = new Profesor() ;
        newProfe.setNombre(nombre);
        newProfe.setApellido(apellido);
        newProfe.setDireccion(direccion);
        newProfe.setTelefono(telefono);
        newProfe.setDocumento(dni);
        service.save(newProfe);
        return "exito";
    }
}
