package modelado;

public class CursoEnMarcha {
    private Curso curso;
    public int bloqueActual;
    public int preguntaActual;
    private boolean finalizado;
    private int vidas;

    public CursoEnMarcha(Curso curso) {
        this.curso = curso;
        this.bloqueActual = 0;
        this.preguntaActual = 0;
        this.finalizado = false;
        this.setVidas(5);
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

	public int getVidas() {
		return vidas;
	}

	public void setVidas(int vidas) {
		this.vidas = vidas;
	}

}

