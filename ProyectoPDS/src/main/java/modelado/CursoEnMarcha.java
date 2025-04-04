package modelado;

public class CursoEnMarcha extends Curso {

	public static final int VIDAS_PREDETERMINADAS = 5;
	public static final Estrategia ESTRATEGIA_PREDETERMINADA = new EstrategiaSecuencial();

	private int bloqueActual;
	private int preguntaActual;
	private int vidas;
	private Estrategia estrategia;
 
	public CursoEnMarcha(Curso curso) {
		this(curso, VIDAS_PREDETERMINADAS, ESTRATEGIA_PREDETERMINADA);
	}

	public CursoEnMarcha(Curso curso, int vidas, Estrategia estrategia) {
		// Llamar al constructor apropiado de la clase padre
		super(curso.getId(), curso.getNombre(), curso.getAutor(), curso.getDescripcion(), curso.getBloques(),
				curso.getImagenCurso());
		this.bloqueActual = 0; // Empezar en el primer bloque (índice 0)
		this.preguntaActual = 0; // Empezar en la primera pregunta (índice 0)
		this.vidas = vidas;
		this.estrategia = estrategia;
	}

	public void avanzarPregunta() {
		Bloque bloqueActualObj = getBloqueActual();
		if (bloqueActualObj == null) {
			return; // Protección contra NullPointerException
		}

		if (preguntaActual < bloqueActualObj.getPreguntas().size() - 1) {
			preguntaActual++;
		} else {
			preguntaActual = 0;
			Bloque siguienteBloque = obtenerSiguienteBloque(bloqueActual);
			if (siguienteBloque != null) {
				bloqueActual = super.getBloques().indexOf(siguienteBloque);
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
		if (bloqueActual < 0 || bloqueActual >= super.getBloques().size()) {
			return null;
		}

		Bloque bloque = super.getBloques().get(bloqueActual);
		if (preguntaActual < 0 || preguntaActual >= bloque.getPreguntas().size()) {
			return null;
		}

		return bloque.getPreguntas().get(preguntaActual);
	}

	public Bloque getBloqueActual() {
		if (bloqueActual < 0 || bloqueActual >= super.getBloques().size()) {
			return null;
		}
		return super.getBloques().get(bloqueActual);
	}

	public Bloque obtenerSiguienteBloque(int actual) {
		return estrategia.siguiente(super.getBloques(), actual);
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

	public void setBloqueActual(int bloqueActual) {
		if (bloqueActual >= 0 && bloqueActual < super.getBloques().size()) {
			this.bloqueActual = bloqueActual;
			this.preguntaActual = 0; // Reiniciar la pregunta al cambiar de bloque
		}
	}

	public void setPreguntaActual(int preguntaActual) {
		Bloque bloque = getBloqueActual();
		if (bloque != null && preguntaActual >= 0 && preguntaActual < bloque.getPreguntas().size()) {
			this.preguntaActual = preguntaActual;
		}
	}
}