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

	public Pregunta obtenerPregunta(int numPregunta) {
		for(Pregunta P: preguntas) {
			if(P.getNumPregunta()==numPregunta) {
				return P;
			}
		}return null;
	}

	public List<Pregunta> getPreguntas() {
		return preguntas;
	}
	
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;

	    Bloque bloque = (Bloque) obj;

	    return titulo != null ? titulo.equals(bloque.titulo) : bloque.titulo == null;
	}

	@Override
	public int hashCode() {
	    return titulo != null ? titulo.hashCode() : 0;
	}

}