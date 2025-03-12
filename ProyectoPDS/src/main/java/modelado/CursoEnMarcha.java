package modelado;

public class CursoEnMarcha {
    private Usuario usuario;
    private Curso curso;
    public int bloqueActual;
    public int preguntaActual;
    private boolean finalizado;

    public CursoEnMarcha(Usuario usuario, Curso curso) {
        this.usuario = usuario;
        this.curso = curso;
        this.bloqueActual = 0;
        this.preguntaActual = 0;
        this.finalizado = false;
    }

    public void avanzarPregunta() {
        Bloque bloque = curso.obtenerSiguienteBloque(bloqueActual);
        if (preguntaActual < bloque.getPreguntas().size() - 1) {
            preguntaActual++;
        } else {
            preguntaActual = 0;
            if (bloqueActual < curso.getBloques().size() - 1) {
                bloqueActual++;
            } else {
                finalizar();
            }
        }
    }

    public void reiniciarCurso() {
        this.bloqueActual = 0;
        this.preguntaActual = 0;
        this.finalizado = false;
    }
    
    public Pregunta getPreguntaActual() {
        return curso.getBloques().get(bloqueActual).getPreguntas().get(preguntaActual);
    }
    
    public Bloque getBloqueActual() {
        return curso.getBloques().get(bloqueActual);
    }

    public void finalizar() {
        finalizado = true;
    }

    public boolean estaEnProgreso() {
        return !finalizado;
    }

    public Curso getCurso() {
        return curso;
    }

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}

