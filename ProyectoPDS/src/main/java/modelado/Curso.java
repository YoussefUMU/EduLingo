package modelado;

import java.util.LinkedList;

public class Curso {
	private final String Titulo;
	private final String Descripcion;
	private LinkedList<Pregunta> PreguntasCurso;
	private final Usuario Creador;
	private int NumeroInscritos;
	
	public Curso(String T, String D, LinkedList<Pregunta> P, Usuario U) {
		this.Titulo = T;
		this.Descripcion = D;
		this.PreguntasCurso = P;
		this.Creador = U;
		this.NumeroInscritos = 0;
	}

	public String getTitulo() {
		return Titulo;
	}

	public String getDescripcion() {
		return Descripcion;
	}

	public LinkedList<Pregunta> getPreguntasCurso() {
		return PreguntasCurso;
	}

	public Usuario getCreador() {
		return Creador;
	}

	public int getNumeroInscritos() {
		return NumeroInscritos;
	}
	
	public void nuevoInscrito() {
		this.NumeroInscritos++;
	}
}
