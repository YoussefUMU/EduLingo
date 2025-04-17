package modelado;

public abstract class Pregunta {
	private int numPregunta;
    private String enunciado;

    public Pregunta(int numPregunta,String enunciado) {
    	this.numPregunta = numPregunta;
        this.enunciado = enunciado;
    }

    public Pregunta() {}
    
    public abstract boolean verificarRespuesta(String respuesta);

	public String getEnunciado() {
		return enunciado;
	}
	
	public int getNumPregunta() {
		return this.numPregunta;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}
}
