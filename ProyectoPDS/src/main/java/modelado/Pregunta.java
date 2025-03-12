package modelado;

public abstract class Pregunta {
    protected final String id;
    protected String enunciado;

    public Pregunta(String id, String enunciado) {
        this.id = id;
        this.enunciado = enunciado;
    }

    public abstract boolean verificarRespuesta(String respuesta);

	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public String getId() {
		return id;
	}
}
