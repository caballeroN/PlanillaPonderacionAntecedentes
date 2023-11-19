package iesmb.pp3.planillaProfesores.entity;

public class CategoriaConTotal {
    private Categoria  categoria;
    private int totalPorCategoria;

    public CategoriaConTotal() {
    }

    public CategoriaConTotal(Categoria categoria, int totalPorCategoria) {
        this.categoria = categoria;
        this.totalPorCategoria = totalPorCategoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getTotalPorCategoria() {
        return totalPorCategoria;
    }

    public void setTotalPorCategoria(int totalPorCategoria) {
        this.totalPorCategoria = totalPorCategoria;
    }
}
