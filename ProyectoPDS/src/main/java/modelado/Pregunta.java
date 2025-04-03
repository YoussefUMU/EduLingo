package modelado;

public abstract class Pregunta {
    protected String enunciado;

    public Pregunta(String enunciado) {
        this.enunciado = enunciado;
    }

    public Pregunta() {}
    
    public abstract boolean verificarRespuesta(String respuesta);

	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}
}
