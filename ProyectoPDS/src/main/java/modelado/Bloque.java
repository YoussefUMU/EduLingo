package modelado;

import java.util.List;

public class Bloque {
	private String titulo;
	private List<Pregunta> preguntas;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas = preguntas;
	}

	public Bloque() {
	}

	public Bloque(String titulo, List<Pregunta> preguntas) {
		this.titulo = titulo;
		this.preguntas = preguntas;
	}

	public Pregunta obtenerPregunta(int indice) {
		return preguntas.get(indice);
	}

	public List<Pregunta> getPreguntas() {
		return preguntas;
	}
}