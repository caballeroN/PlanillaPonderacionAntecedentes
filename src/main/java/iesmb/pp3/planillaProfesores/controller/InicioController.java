package iesmb.pp3.planillaProfesores.controller;

import iesmb.pp3.planillaProfesores.entity.Actividad;
import iesmb.pp3.planillaProfesores.entity.Categoria;
import iesmb.pp3.planillaProfesores.entity.Profesor;
import iesmb.pp3.planillaProfesores.entity.PuntajeActividad;
import iesmb.pp3.planillaProfesores.service.IActividadService;
import iesmb.pp3.planillaProfesores.service.ICategoriaService;
import iesmb.pp3.planillaProfesores.service.IProfesorService;
import iesmb.pp3.planillaProfesores.service.jpa.PuntajeActividadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        model.put("lCategorias", categorias);
        model.put("profesorId", profesorId);
        return "categorias";
    }

    @PostMapping("/categorias_t/{profesorId}")
    public String cargarNotaPorCategoria(@PathVariable Integer profesorId,
                                         @RequestParam(name = "categoriasSeleccionadas", required = false) List<String> categoriasSeleccionadas,
                                         ModelMap model) {
        String strCategoriasSeleccionadas = "";
        if (categoriasSeleccionadas.size() > 0) {
            for (int i = 0; i < categoriasSeleccionadas.size(); i++) {
                strCategoriasSeleccionadas += categoriasSeleccionadas.get(i) + ", ";
            }
        }
        List<List<String>> globalTabla = new ArrayList<>();

        if (categoriasSeleccionadas != null && !categoriasSeleccionadas.isEmpty()) {
            String categoriaId = categoriasSeleccionadas.get(0); // Obtén el primer elemento
            Categoria categoria = categoriaService.getById(Integer.valueOf(categoriaId));
            List<Actividad> actividades = categoria.getActividades();
            List<PuntajeActividad> puntuacionXactividad = puntajeActividadService.getPuntajesActividadPorProfesor(profesorId);
            System.out.println("Tamaño de actividades: " + actividades.size());
            System.out.println("Tamaño de puntuacionXactividad: " + puntuacionXactividad.size());


            for (int i = 0; i < actividades.size(); i++) {
                List<String> fila = new ArrayList<>();
                fila.add(String.valueOf(actividades.get(i).getId()));
                fila.add(actividades.get(i).getNombre());
                fila.add(String.valueOf(actividades.get(i).getPuntuacion()));
                // Asegúrate de que haya un puntaje disponible para esta actividad
                if (i < puntuacionXactividad.size()) {
                    fila.add(String.valueOf(puntuacionXactividad.get(i).getPuntaje()));
                } else {
                    // Si no hay puntaje disponible, agrega cero
                    fila.add("0");
                }
                globalTabla.add(fila);
            }
            System.out.println("###$$$$##### globalTabla.get(0)    "+ globalTabla.get(0));
            System.out.println("###$$#  globalTabla.size()  "+ globalTabla.size());
            model.put("profesorId", profesorId);
            model.put("strCategoriasSeleccionadas", strCategoriasSeleccionadas);
            model.put("categoria", categoria);
            model.put("globalTabla", globalTabla);
            model.put("categoriasSeleccionadas", categoriasSeleccionadas);
            return "categoria";
        }
        return "exito";
    }

    @PostMapping("/categoria_t/{profesorId}")
    public String iterarCategoria(@PathVariable Integer profesorId,
                                  @RequestParam(name = "strCategoriasSeleccionadas") String strCategoriasSeleccionadas,
                                  @RequestParam(name = "globalTabla") List<List<String>> globalTabla, ModelMap model) {

        int idCategoria = Integer.parseInt(strCategoriasSeleccionadas.split(",")[0].trim());
        int resultadoParcial = 0;
        int contador =0;
        Profesor profesor = profesorService.getById(profesorId);
        List<PuntajeActividad> puntajesActividad = profesor.getPuntajesActividad();
        Categoria categoria = categoriaService.getById((idCategoria));
        List<Actividad> actividades = categoria.getActividades();
        while (puntajesActividad.size() < globalTabla.size()) {
            puntajesActividad.add(new PuntajeActividad());
        }
        int minSize = Math.min(globalTabla.size(), puntajesActividad.size());
        for (int i = 0; i < minSize; i++) {
            PuntajeActividad puntajeActividad = puntajesActividad.get(i);
            resultadoParcial += puntajeActividad.getPuntaje();
        //    puntajeActividad.setPuntaje(((globalTabla.get(i)).isEmpty()) ? 0 : Integer.parseInt(globalTabla.get(i)));
            // Asocia el puntaje con el profesor
            puntajeActividad.setProfesor(profesor);
            // Asocia el puntaje con la actividad
            Actividad actividad = actividadService.getById(actividades.get(i).getId());
            puntajeActividad.setActividad(actividad);
            // Guardar el puntaje en la base de datos
            puntajeActividadService.save(puntajeActividad);
            contador = i + 1 ;
        }
        categoria.setTotalPuntosXCategoria(resultadoParcial);
        categoriaService.save(categoria);
        System.out.println("ID + nombre profesor "+profesor.getId() + profesor.getNombre());
        System.out.println(" total       = nomber Categoria \n"+ categoria.getTotalPuntosXCategoria()+categoria.getNombre());
        System.out.println(" cantidad de categorias para esta categoria es = "+contador);
        System.out.println("\nNombre de la primer actividad \n"+ actividades.get(0).getNombre());
        System.out.println("\nNombre de la ultima actividad \n"+ actividades.get(actividades.size()-1).getNombre());
        return "exito";
    }
}
