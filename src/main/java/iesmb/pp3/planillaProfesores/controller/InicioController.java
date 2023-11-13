package iesmb.pp3.planillaProfesores.controller;

import iesmb.pp3.planillaProfesores.entity.*;
import iesmb.pp3.planillaProfesores.service.IActividadService;
import iesmb.pp3.planillaProfesores.service.ICategoriaService;
import iesmb.pp3.planillaProfesores.service.IProfesorService;
import iesmb.pp3.planillaProfesores.service.jpa.PuntajeActividadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/inicio")
public class InicioController {

    @Autowired
    IProfesorService profesorService;
    @Autowired
    ICategoriaService categoriaService;
    @Autowired
    IActividadService actividadService;
    @Autowired
    PuntajeActividadServiceImpl puntajeActividadService;

    @GetMapping("/profesores")
    public String metodoPrueba(ModelMap model) {
        List<Profesor> profe = profesorService.getAll();
        model.addAttribute("profesores", profe);
        return "profesores";
    }

    @GetMapping("/xdni")
    public String buscarXdni(@RequestParam Integer id, ModelMap model) {
        Profesor profe = profesorService.getById(id);
        model.addAttribute("profesor", profe);
        return "profesor";
    }

    @PostMapping("/borrar/{profesorId}")
    public String borrarxdni(@PathVariable Integer profesorId, ModelMap model) {
        profesorService.delete(profesorId);
        model.addAttribute("profesor", "profe borrado");
        return "profesordelete";
    }

    @GetMapping("/new_profe")
    public String nuevo_profe(ModelMap model) {
        model.addAttribute("profesor", "profe borrado");
        return "cargar_profesor";
    }

    @PostMapping("/buscarxid")
    public String buscarProfeXId(@RequestParam Integer id, ModelMap model) {
        Profesor profe = profesorService.getById(id);
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
        Profesor newProfe = new Profesor();
        newProfe.setNombre(nombre);
        newProfe.setApellido(apellido);
        newProfe.setDireccion(direccion);
        newProfe.setTelefono(telefono);
        newProfe.setDocumento(dni);
        profesorService.save(newProfe);
        return "exito";
    }

    @GetMapping("/categorias_t/{profesorId}" )
    public String listarCategorias(@PathVariable Integer profesorId, ModelMap model) {
        List<Categoria> categorias = categoriaService.getAll();
        System.out.println(""+ categorias.get(0).getId());
        model.put("lCategorias", categorias);
        model.put("profesorId", profesorId);
        return "categorias";
    }
    @PostMapping("/categorias_p/{profesorId}")
    public String cargarNotaPorCategoria(@PathVariable Integer profesorId,
                                         @RequestParam(name = "categoriasSeleccionadas", required = false) List<String> categoriasSeleccionadas,
                                         ModelMap model) {
        Profesor profesor = profesorService.getById(profesorId);
        String profesor_nombre = profesor.getNombre();
        String strNameCategoria;

        String strCategoriasSeleccionadas = "";
        System.out.println("antes del if!!!!!!!!!!!!!!!!");
        System.out.println("categoria seleccionada "+categoriasSeleccionadas  );
        if (categoriasSeleccionadas != null && !categoriasSeleccionadas.isEmpty()) {
            System.out.println("if 1 ");
            String categoriaId = categoriasSeleccionadas.get(0); // Obtén el primer elemento
            Categoria categoria = categoriaService.getById(Integer.valueOf(categoriaId));
            strNameCategoria= categoria.getNombre();
            // Obtener actividades y puntajes
            List<Actividad> actividades = categoria.getActividades();
            List<PuntajeActividad> puntajes = puntajeActividadService.obtenerPuntajesPorProfesorYCategoria(profesor, categoria);

            // Combinar las listas
            List<ActividadConPuntaje> actividadesConPuntajes = new ArrayList<>();

            for (int i = 0; i < actividades.size(); i++) {
                ActividadConPuntaje actividadConPuntaje = new ActividadConPuntaje();
                actividadConPuntaje.setActividad(actividades.get(i));
                // Asegúrate de manejar correctamente los índices para evitar IndexOutOfBoundsException
                if (i < puntajes.size()) {
                    actividadConPuntaje.setPuntajeActividad(puntajes.get(i));
                }else{
                    PuntajeActividad puntajeDefault = new PuntajeActividad();
                    puntajeDefault.setPuntaje(0);
                    actividadConPuntaje.setPuntajeActividad(puntajeDefault);
                }
                actividadesConPuntajes.add(actividadConPuntaje);
            }

            strCategoriasSeleccionadas = String.join(", ", categoriasSeleccionadas);
            System.out.println(" 133 strCategoriasSeleccionadas = "+strCategoriasSeleccionadas);

            model.put("nameCategoria", strNameCategoria);
            model.put("profesorId", profesorId);
            model.put("profesor_nombre", profesor_nombre);
            model.put("actividadesConPuntajes", actividadesConPuntajes);
            model.put("strCategoriasSeleccionadas", strCategoriasSeleccionadas);
            model.put("categoriasSeleccionadas", categoriasSeleccionadas); // Agregar a modelo
            return "categoria";
        }

        // Manejar el caso en que no hay categorías seleccionadas
        model.put("profesorId", profesorId);
        model.put("profesor_nombre", profesor_nombre);
        model.put("strCategoriasSeleccionadas", strCategoriasSeleccionadas);
        model.put("categoriasSeleccionadas", categoriasSeleccionadas); // Agregar a modelo
        return "exito";
    }
// REVISION


//del categoria.html con el metodo post
    @PostMapping("/categoria_t/{profesorId}")
    public String iterarCategoria(@PathVariable Integer profesorId,
                                  @RequestParam(name = "categoriasSeleccionadas", required = false) List<String> categoriasSeleccionadas,

                                  @RequestParam(name = "strCategoriasSeleccionadas") String strCategoriasSeleccionadas,
                                  @RequestParam(name = "asignados") List<String> asignados, ModelMap model) {

        System.out.println(("$#$$$#$$$# 162  categoriasSeleccionadas  "+categoriasSeleccionadas));
        int idCategoria = Integer.parseInt(strCategoriasSeleccionadas.split(",")[0].trim());
        Profesor profesor = profesorService.getById(profesorId);
        List<PuntajeActividad> puntajesActividad = profesor.getPuntajesActividad();
        Categoria categoria = categoriaService.getById((idCategoria));
        List<Actividad> actividades = categoria.getActividades();
        while (puntajesActividad.size() < asignados.size()) {
            puntajesActividad.add(new PuntajeActividad());
        }
        int minSize = Math.min(asignados.size(), puntajesActividad.size());
        for (int i = 0; i < minSize; i++) {
            PuntajeActividad puntajeActividad = puntajesActividad.get(i);
            puntajeActividad.setPuntaje(((asignados.get(i)).isEmpty()) ? 0 : Integer.parseInt(asignados.get(i)));
            // Asocia el puntaje con el profesor
            puntajeActividad.setProfesor(profesor);
            // Asocia el puntaje con la actividad
            Actividad actividad = actividadService.getById(actividades.get(i).getId());
            puntajeActividad.setActividad(actividad);
            // Guardar el puntaje en la base de datos
            puntajeActividadService.save(puntajeActividad);
        }
        System.out.println("179 strCategoriasSeleccionadas = "+strCategoriasSeleccionadas);
        System.out.println("SALIENDO!!!!");

//        model.put("nameCategoria", strNameCategoria);
        model.put("profesorId", profesorId);
//        model.put("profesor_nombre", profesor_nombre);
//        model.put("actividadesConPuntajes", actividadesConPuntajes);
        model.put("strCategoriasSeleccionadas", strCategoriasSeleccionadas);
        model.put("categoriasSeleccionadas", categoriasSeleccionadas); // Agregar a modelo
        return "exito";
    }
}
