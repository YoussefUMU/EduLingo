package modelado;

import java.time.LocalDate;
import java.util.LinkedList;

public class Usuario {
	
	private final String nombre;
	private LinkedList<Curso> CursosCompartidos;
	private LinkedList<Curso> CursosCreados;
	private LinkedList<CursoEnMarcha> CursosEnRealizacion;
	
	public Usuario(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public LinkedList<Curso> getCursosCompartidos() {
		return CursosCompartidos;
	}

	public LinkedList<Curso> getCursosCreados() {
		return CursosCreados;
	}

	public LinkedList<CursoEnMarcha> getCursosEnRealizacion() {
		return CursosEnRealizacion;
	}
	
	public void CompartirCurso(Curso C) {
		this.CursosCompartidos.add(C);
	}
	
	public void CrearCurso(Curso C) {
		this.CursosCreados.add(C);
	}
	
	public void EmpezarCurso(Curso C, Estrategia E) {
		this.CursosEnRealizacion.add(new CursoEnMarcha(LocalDate.now(), E, C));
	}
	
}
