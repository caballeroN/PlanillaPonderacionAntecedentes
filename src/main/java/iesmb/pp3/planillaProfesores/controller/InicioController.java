package iesmb.pp3.planillaProfesores.controller;

import iesmb.pp3.planillaProfesores.entity.Actividad;
import iesmb.pp3.planillaProfesores.entity.Categoria;
import iesmb.pp3.planillaProfesores.entity.Profesor;
import iesmb.pp3.planillaProfesores.service.IActividadService;
import iesmb.pp3.planillaProfesores.service.ICategoriaService;
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

    @Autowired
    ICategoriaService categoriaService;
    @Autowired
    IActividadService actividadService;

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



        @PostMapping("/borrar/{profesorId}")
        public String borrarxdni(@PathVariable Integer profesorId, ModelMap model) {
            System.out.println("llegue al borrar profesor ");
            service.delete(profesorId);
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
        System.out.println("llegue al guardar profe su nombre es  " + nombre);
        Profesor newProfe = new Profesor();
        newProfe.setNombre(nombre);
        newProfe.setApellido(apellido);
        newProfe.setDireccion(direccion);
        newProfe.setTelefono(telefono);
        newProfe.setDocumento(dni);
        service.save(newProfe);
        return "exito";
    }


    @GetMapping("/categorias_t/{profesorId}" )
    public String listarCategorias(@PathVariable Integer profesorId, ModelMap model) {
        System.out.println(" llegue a CATEGORIAS!!!!!");
        List<Categoria> categorias = categoriaService.getAll();
        model.put("lCategorias", categorias);
        model.put("profesorId", profesorId);
        return "categorias";
    }


    @PostMapping("/categorias_t/{profesorId}")
    public String cargarNotaPorCategoria(@PathVariable Integer profesorId, @RequestParam(name = "categoriasSeleccionadas", required = false) List<String> categoriasSeleccionadas, ModelMap model) {
        System.out.println("llegue a cargarNotaPorCategoria");
        if (categoriasSeleccionadas != null && !categoriasSeleccionadas.isEmpty()) {
            String categoriaId = categoriasSeleccionadas.get(0); // Obtén el primer elemento
            Categoria categoria = categoriaService.getById(Integer.valueOf(categoriaId));
            List<Actividad> actividades = categoria.getActividades();
            model.put("profesorId", profesorId);
            model.put("categoria", categoria);
            model.put("actividades", actividades);
            model.put("categoriasSeleccionadas", categoriasSeleccionadas); // Agregar a modelo
            return "categoria";
        }
        return "exito";
    }

    @PostMapping("/categoria_t/{profesorId}")
    public String iterarCategoria(@PathVariable Integer profesorId, @RequestParam(name = "categoriasSeleccionadas", required = false) List<String> categoriasSeleccionadas,
                                  @RequestParam(name = "asignados") List<String> asignados, ModelMap model) {
        System.out.println("LLEGUE A CATEGORIA CON POST  ");

        System.out.println(asignados.size());
        int aux = 1;
        for (String asignado : asignados) {
            System.out.println(aux + "_  " + asignado);
            aux++;
        }
        if (categoriasSeleccionadas != null && categoriasSeleccionadas.size() > 1) {
            System.out.println("categorias  " + categoriasSeleccionadas.get(0));
            System.out.println("profesor id   " + profesorId);

            // Obtén la categoría actual y sus actividades
            String categoriaId = categoriasSeleccionadas.get(0);
            Categoria categoria = categoriaService.getById(Integer.valueOf(categoriaId));
            List<Actividad> actividades = categoria.getActividades();

            // Procesa las modificaciones y guarda en la base de datos (ejemplo simplificado)
            for (Actividad actividad : actividades) {
                // Realiza operaciones de actualización en la base de datos
                actividadService.save(actividad);
            }
            categoriasSeleccionadas.remove(0); // Elimina el primer elemento
            model.put("categoriasSeleccionadas", categoriasSeleccionadas); // Mantén la lista restante
            return "forward:/categorias_t"; // Redirige de nuevo al controlador anterior
        }
        return "exito"; // o la página a la que desees redirigir cuando no haya más categorías
    }
}
