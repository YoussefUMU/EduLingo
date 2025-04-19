package modelado;

public abstract class Pregunta {
	private int numPregunta;
	private String enunciado;

	public Pregunta(int numPregunta, String enunciado) {
		this.numPregunta = numPregunta;
		this.enunciado = enunciado;
	}

	public Pregunta() {
	}

	public abstract boolean verificarRespuesta(String respuesta);

	public String getEnunciado() {
		return enunciado;
	}

	public int getNumPregunta() {
		return numPregunta;
	}

	public void setNumPregunta(int numPregunta) {
		this.numPregunta = numPregunta;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Pregunta pregunta = (Pregunta) obj;

		return numPregunta == pregunta.numPregunta;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(numPregunta);
	}

}
