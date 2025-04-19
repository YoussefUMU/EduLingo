package modelado;

import java.util.ArrayList;
import java.util.List;

public class CursoEnMarcha {

	public static final int VIDAS_PREDETERMINADAS = 5;
	public static final Estrategia ESTRATEGIA_PREDETERMINADA = new EstrategiaSecuencial();

	private Curso curso;
	private int bloqueActual;
	private int preguntaActual;
	private int vidas;
	private Estrategia estrategia;
	private List<Bloque> BloquesCompletos;
	private List<Pregunta> PreguntasCompletas;

	public CursoEnMarcha(Curso curso) {
		this(curso, VIDAS_PREDETERMINADAS, ESTRATEGIA_PREDETERMINADA);
	}

	public CursoEnMarcha(Curso curso, int vidas, Estrategia estrategia) {
		this.bloqueActual = 0; // Empezar en el primer bloque (índice 0)
		this.preguntaActual = 0; // Empezar en la primera pregunta (índice 0)
		this.vidas = vidas;
		this.estrategia = estrategia;
		this.curso = curso;
		this.BloquesCompletos = new ArrayList<Bloque>();
		this.PreguntasCompletas = new ArrayList<Pregunta>();
	}

	public void avanzarPregunta() {
		Bloque bloqueActualObj = getBloqueActual();
		if (bloqueActualObj == null) {
			return; // Protección contra NullPointerException
		}

		Pregunta siguientePregunta = this.obtenerSiguientePregunta(preguntaActual);
		if (siguientePregunta != null) {
			// añadimos la pregunta vieja a preguntas completas
			Pregunta actual = this.getPreguntaActual();
			if (!PreguntasCompletas.contains(actual)) {
				PreguntasCompletas.add(actual);
			}

			// actualizamos pregunta actual
			this.preguntaActual = siguientePregunta.getNumPregunta();
		} else {
			preguntaActual = 0;
			// Reseteamos la lista de preguntas completas
			this.PreguntasCompletas = new ArrayList<Pregunta>();

			Bloque siguienteBloque = obtenerSiguienteBloque(bloqueActual);
			if (siguienteBloque != null) {
				this.BloquesCompletos.add(bloqueActualObj);
				bloqueActual = this.curso.getBloques().indexOf(siguienteBloque);
			} else {
				finalizar();
			}
		}
	}

	public void reiniciarCurso() {
		this.bloqueActual = 0;
		this.preguntaActual = 0;
	}

	public Pregunta getPreguntaActual() {
		if (bloqueActual < 0 || bloqueActual >= this.curso.getBloques().size()) {
			return null;
		}

		Bloque bloque = this.getBloqueActual();
		if (preguntaActual < 0 || preguntaActual >= bloque.getPreguntas().size()) {
			return null;
		}

		return bloque.obtenerPregunta(preguntaActual);
	}

	public Bloque getBloqueActual() {
		if (bloqueActual < 0 || bloqueActual >= this.curso.getBloques().size()) {
			return null;
		}
		return this.curso.getBloqueEspecifico(bloqueActual);
	}

	public Bloque obtenerSiguienteBloque(int actual) {
		return estrategia.siguienteBloque(this.curso.getBloques(), actual, this.BloquesCompletos);

	}

	public Pregunta obtenerSiguientePregunta(int actual) {
		return estrategia.siguientePregunta(this.getBloqueActual(), actual, PreguntasCompletas);
	}

	public void finalizar() {
		// Implementar lógica para finalizar el curso
		System.out.println("¡Curso completado!");
	}

	public Estrategia getEstrategia() {
		return this.estrategia;
	}

	public int getVidas() {
		return vidas;
	}

	public void setVidas(int vidas) {
		this.vidas = vidas;
	}

	public int getBloqueActualIndex() {
		return bloqueActual;
	}

	public int getPreguntaActualIndex() {
		return preguntaActual;
	}

	public Curso getCurso() {
		return this.curso;
	}

	/*
	 * public void setBloqueActual(int bloqueActual) { if (bloqueActual >= 0 &&
	 * bloqueActual < this.curso.getBloques().size()) { this.bloqueActual =
	 * bloqueActual; this.preguntaActual = 0; // Reiniciar la pregunta al cambiar de
	 * bloque } }
	 */

	/*
	 * public void setPreguntaActual(int preguntaActual) { Bloque bloque =
	 * getBloqueActual(); if (bloque != null && preguntaActual >= 0 &&
	 * preguntaActual < bloque.getPreguntas().size()) { this.preguntaActual =
	 * preguntaActual; } }
	 */
}