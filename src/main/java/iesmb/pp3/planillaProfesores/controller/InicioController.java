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

import java.util.*;

@Controller
@RequestMapping("")
public class InicioController {

    @Autowired
    IProfesorService profesorService;
    @Autowired
    ICategoriaService categoriaService;
    @Autowired
    IActividadService actividadService;
    @Autowired
    PuntajeActividadServiceImpl puntajeActividadService;

    @GetMapping("")
    public String cargarInicio(ModelMap model) {
        List<Profesor> profesores = profesorService.getAll();
        List<ProfesorTotalDePuntos> listaProfesoresTotalDePuntos = new ArrayList<>();

        for (Profesor profesor : profesores) {
            ProfesorTotalDePuntos profesorTotalDePuntos = new ProfesorTotalDePuntos();
            profesorTotalDePuntos.setTotalAcumulado(puntajeActividadService.obtenerTotalPuntosPorProfesor(profesor));
            profesorTotalDePuntos.setProfesor(profesor);

            listaProfesoresTotalDePuntos.add(profesorTotalDePuntos);
        }

        model.addAttribute("profesores", listaProfesoresTotalDePuntos);
        return "index";
    }


    @GetMapping("/xdni")
    public String buscarXdni(@RequestParam Integer id, ModelMap model) {
        Profesor profe = profesorService.getById(id);
        int total = puntajeActividadService.obtenerTotalPuntosPorProfesor(profe);
        model.addAttribute("total_puntos", total);
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

    @PostMapping("/buscarxdni")
    public String buscarProfeXDNI(@RequestParam String dni, ModelMap model) {
        Profesor profe = profesorService.getByDni(dni);
        System.out.println("profesor " +profe);
        if(profe != null){
            model.addAttribute("id", profe.getId());
            return buscarXdni(profe.getId(), model);
        }else {
            profe = new Profesor();
            model.addAttribute("profesor", profe);
            return "datos_personales";
        }
    }
    @PostMapping("/buscarxid")
    public String buscarProfeXId(@RequestParam Integer id, ModelMap model) {
        Profesor profe = profesorService.getById(id);
        if(profe != null){
            model.addAttribute("id", profe.getId());
            return buscarXdni(profe.getId(), model);
        }else {
            profe = new Profesor();
            model.addAttribute("profesor", profe);
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
        List<CategoriaConTotal> categoriaConTotal = new ArrayList<>();
        Profesor profesor = profesorService.getById(profesorId);
        List<Categoria> categorias = categoriaService.getAll();

        for (Categoria categoria : categorias) {
            int totalPorCategoria = 0;
            List<PuntajeActividad> puntajes = puntajeActividadService.obtenerPuntajesPorProfesorYCategoria(profesor, categoria);
            for (PuntajeActividad puntaje : puntajes) {
                totalPorCategoria += puntaje.getPuntaje();
            }
            CategoriaConTotal categoriaConTotalItem = new CategoriaConTotal();
            categoriaConTotalItem.setCategoria(categoria);
            categoriaConTotalItem.setTotalPorCategoria(totalPorCategoria);
            categoriaConTotal.add(categoriaConTotalItem);
        }
        int total = puntajeActividadService.obtenerTotalPuntosPorProfesor(profesor);
        model.addAttribute("total_puntos", total);
        model.addAttribute("profesor", profesor);
        model.addAttribute("categoriaConTotal", categoriaConTotal);
        model.addAttribute("lCategorias", categoriaConTotal);
        model.addAttribute("profesorId", profesorId);
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
            model.addAttribute("profesor", profesor);
            model.addAttribute("categoriaId", categoriaId);
            model.addAttribute("categoria", categoria);
            model.addAttribute("nameCategoria", strNameCategoria);
            model.addAttribute("profesorId", profesorId);
            model.addAttribute("profesor_nombre", profesor_nombre);
            model.addAttribute("actividadesConPuntajes", actividadesConPuntajes);
            model.addAttribute("strCategoriasSeleccionadas", strCategoriasSeleccionadas);
            model.addAttribute("categoriasSeleccionadas", categoriasSeleccionadas); // Agregar a modelo
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
        Categoria categoria = categoriaService.getById(idCategoria);

        // Obtener las actividades de la categoría seleccionada
        List<Actividad> actividades = categoria.getActividades();

        // Obtener o inicializar los puntajesActividad del profesor para esta categoría
        List<PuntajeActividad> puntajesActividad = puntajeActividadService.obtenerPuntajesActividad(profesor, actividades);

        // Iterar sobre los puntajes y asignar valores
        for (int i = 0; i < asignados.size(); i++) {
            PuntajeActividad puntajeActividad = puntajeActividadService.obtenerPuntajeActividad(puntajesActividad, actividades.get(i));
            puntajeActividad.setPuntaje((asignados.get(i)).isEmpty() ? 0 : Integer.parseInt(asignados.get(i).trim()));
            puntajeActividad.setProfesor(profesor);
            puntajeActividad.setActividad(actividades.get(i));
            puntajeActividadService.save(puntajeActividad);
        }
        String respaldo = "";
        if (strCategoriasSeleccionadas.split(",").length > 1) {
            for (int i = 1; i < strCategoriasSeleccionadas.split(",").length; i++) {
                respaldo += strCategoriasSeleccionadas.split(",")[i] + ", ";
            }
            strCategoriasSeleccionadas = respaldo;
        } else {

            model.addAttribute("id", profesor.getId());
            return buscarXdni(profesor.getId(), model);
        }
        model.addAttribute("model", model);
        model.addAttribute("profesorId", profesorId);
        model.addAttribute("strCategoriasSeleccionadas", strCategoriasSeleccionadas);
        return "continuar";
    }
}
