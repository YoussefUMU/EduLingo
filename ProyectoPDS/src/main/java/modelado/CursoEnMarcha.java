package modelado;

import java.time.LocalDate;

public class CursoEnMarcha {
	private final Curso curso;
	private final LocalDate FechaInicio;
	private int Progreso;
	private final Estrategia EstrategiaUsada;
	
	public CursoEnMarcha(LocalDate F, Estrategia E, Curso C) {
		this.FechaInicio = F;
		this.EstrategiaUsada = E;
		this.curso = C;
		this.Progreso = 0;
	}

	public LocalDate getFechaInicio() {
		return FechaInicio;
	}

	public int getProgreso() {
		return Progreso;
	}

	public Estrategia getEstrategiaUsada() {
		return EstrategiaUsada;
	}

	public Curso getCurso() {
		return curso;
	}
	
	
	
	
}
