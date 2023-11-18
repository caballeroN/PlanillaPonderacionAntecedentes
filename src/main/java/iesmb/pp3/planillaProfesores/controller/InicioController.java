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


    @GetMapping("/new_profe/{profesorId}")
    public String nuevo_profe(@PathVariable Integer profesorId, ModelMap model) {
        Profesor profe = profesorService.getById(profesorId);
        model.addAttribute("profesor", profe);
        return "datos_personales";
    }

    @PostMapping("/buscarxid")
    public String buscarProfeXId(@RequestParam Integer id, ModelMap model) {
        Profesor profe = profesorService.getById(id);
        if(profe != null){
            model.addAttribute("id", profe.getId());
            return buscarXdni(profe.getId(), model);
        }else {
            profe = new Profesor();
            model.put("profesor", profe);
            return "datos_personales";
        }
    }


    @PostMapping("/guardar_profe")
    public String guardar_profe(@RequestParam String nombre,
                                @RequestParam String apellido,
                                @RequestParam String direccion,
                                @RequestParam String telefono,
                                @RequestParam String documento,
                                @RequestParam(name = "id", required = false)  Integer id,
                                ModelMap model) {
        Profesor newProfe;
        if (id == null || id == 0){
            newProfe = new Profesor();
        }else{
            newProfe= profesorService.getById(id);
        }

        newProfe.setNombre(nombre);
        newProfe.setApellido(apellido);
        newProfe.setDireccion(direccion);
        newProfe.setTelefono(telefono);
        newProfe.setDocumento(documento);
        profesorService.save(newProfe);
        return "exito";
    }

    @GetMapping("/categorias_t/{profesorId}" )
    public String listarCategorias(@PathVariable Integer profesorId, ModelMap model) {
        List<Categoria> categorias = categoriaService.getAll();
        model.put("lCategorias", categorias);
        model.put("profesorId", profesorId);
        return "categorias";
    }
    @PostMapping("/categorias_p/{profesorId}")
    public String cargarNotaPorCategoria(@PathVariable Integer profesorId,
                                         @RequestParam(name = "categoriasSeleccionadas", required = false) List<String> categoriasSeleccionadas,
                                         @RequestParam(name = "strCategoriasSeleccionadas", required = false) String strCategoriasSeleccionadas,
                                         ModelMap model) {
        Profesor profesor = profesorService.getById(profesorId);
        String profesor_nombre = profesor.getNombre();
        String strNameCategoria = "";
        int cantidadCategoriasSeleccionadas = 0;

        if (categoriasSeleccionadas == null || categoriasSeleccionadas.isEmpty()) {
            categoriasSeleccionadas = Arrays.asList(strCategoriasSeleccionadas.split(",\\s*"));
        }
        if (categoriasSeleccionadas != null && !categoriasSeleccionadas.isEmpty()) {
            String categoriaId = categoriasSeleccionadas.get(0).trim(); // Obtén el primer elemento
            Categoria categoria = categoriaService.getById(Integer.valueOf(categoriaId));
            strNameCategoria = categoria.getNombre();
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

            model.put("nameCategoria", strNameCategoria);
            model.put("profesorId", profesorId);
            model.put("profesor_nombre", profesor_nombre);
            model.put("actividadesConPuntajes", actividadesConPuntajes);
            model.put("strCategoriasSeleccionadas", strCategoriasSeleccionadas);
            model.put("categoriasSeleccionadas", categoriasSeleccionadas); // Agregar a modelo
            return "categoria";
        }


        return "error";
    }
    @PostMapping("/categoria_t/{profesorId}")
    public String iterarCategoria(@PathVariable Integer profesorId,
                                  @RequestParam(name = "strCategoriasSeleccionadas") String strCategoriasSeleccionadas,
                                  @RequestParam(name = "asignados") List<String> asignados, ModelMap model) {

        int idCategoria = Integer.parseInt(strCategoriasSeleccionadas.split(",")[0].trim());
        Profesor profesor = profesorService.getById(profesorId);
        List<PuntajeActividad> puntajesActividad = profesor.getPuntajesActividad();
        Categoria categoria = categoriaService.getById((idCategoria));
        List<Actividad> actividades = categoria.getActividades();
        while (puntajesActividad.size() < asignados.size()) {
            puntajesActividad.add(new PuntajeActividad());
        }
        int minSize = Math.min(asignados.size(), puntajesActividad.size());
        String strNewListaCategSelec = "";
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
        String respaldo = "";
        if (strCategoriasSeleccionadas.split(",").length > 1) {
            for (int i = 1; i < strCategoriasSeleccionadas.split(",").length; i++) {
                respaldo += strCategoriasSeleccionadas.split(",")[i] + ", ";
            }
            strCategoriasSeleccionadas = respaldo;
        } else {
            return "exito";
        }
        model.put("profesorId", profesorId);
        model.put("strCategoriasSeleccionadas", strCategoriasSeleccionadas);
        return "continuar";
    }
}
