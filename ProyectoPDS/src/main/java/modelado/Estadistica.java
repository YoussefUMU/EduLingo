package modelado;

public class Estadistica {
    private Usuario usuario;
    private Curso curso;
    private int tiempoUso;
    private int mejorRacha;

    public Estadistica(Usuario usuario, Curso curso) {
        this.usuario = usuario;
        this.curso = curso;
        this.tiempoUso = 0;
        this.mejorRacha = 0;
    }
}