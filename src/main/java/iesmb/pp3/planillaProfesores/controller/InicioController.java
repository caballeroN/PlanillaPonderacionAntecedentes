package iesmb.pp3.planillaProfesores.controller;

import iesmb.pp3.planillaProfesores.entity.Profesor;
import iesmb.pp3.planillaProfesores.service.IProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}
