package iesmb.pp3.planillaProfesores.entity;

public class ProfesorTotalDePuntos {
    private Profesor profesor;
    private int totalAcumulado;

    public ProfesorTotalDePuntos() {
    }

    public ProfesorTotalDePuntos(Profesor profesor, int totalAcumulado) {
        this.profesor = profesor;
        this.totalAcumulado = totalAcumulado;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public int getTotalAcumulado() {
        return totalAcumulado;
    }

    public void setTotalAcumulado(int totalAcumulado) {
        this.totalAcumulado = totalAcumulado;
    }
}
