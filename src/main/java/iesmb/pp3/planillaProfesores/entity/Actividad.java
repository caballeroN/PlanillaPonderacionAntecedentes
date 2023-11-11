package iesmb.pp3.planillaProfesores.entity;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Actividad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private int puntuacion;
    private int ptsAsignados;
    @ManyToOne
    private Categoria categoria;

    @OneToMany(mappedBy = "actividad")
    private List<PuntajeActividad> puntajesActividad;

    public Actividad() {
        puntajesActividad = new ArrayList<>();
    }

    public Actividad(String nombre, int puntuacion, int ptsAsignados) {
        this.nombre = nombre;
        this.puntuacion = puntuacion;
        this.ptsAsignados = ptsAsignados;
        puntajesActividad = new ArrayList<>();
    }

    public int getPtsAsignados() {
        return ptsAsignados;
    }

    public void setPtsAsignados(int ptsAsignados) {
        this.ptsAsignados = ptsAsignados;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<PuntajeActividad> getPuntajesActividad() {
        return puntajesActividad;
    }

    public void setPuntajesActividad(List<PuntajeActividad> puntajesActividad) {
        this.puntajesActividad = puntajesActividad;
    }
}