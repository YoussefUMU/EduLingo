package modelado;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "cursos_en_marcha")
public class CursoEnMarcha {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	public static final int VIDAS_PREDETERMINADAS = 5;
	public static final Estrategia ESTRATEGIA_PREDETERMINADA = new EstrategiaSecuencial();
	public static final TipoEstrategia TIPO_ESTRATEGIA_PREDETERMINADA = TipoEstrategia.SECUENCIAL;
	
	@OneToOne
	private Curso curso;
	private int bloqueActual;
	private int preguntaActual;
	private int vidas;
	@Transient
	private Estrategia estrategia;		//No implementa bien la persistencia debido a que es una interfaz.
	
	private TipoEstrategia tipoEstrategia; //Utilizamos este enumerado para la persistencia. Estrategia no es persistente, por lo que el getter mirará si existe y si no, crea una nueva a partir del enumerado.
	
	@OneToMany(cascade={ CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name="curso_en_marcha_id")
	private List<Bloque> BloquesCompletos;
	@OneToMany(cascade={ CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name="curso_en_marcha_id")
	private List<Pregunta> PreguntasCompletas;
	@ManyToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;

	public CursoEnMarcha() {
		
	}
	
	public CursoEnMarcha(Curso curso) {
		this(curso, VIDAS_PREDETERMINADAS, ESTRATEGIA_PREDETERMINADA, TIPO_ESTRATEGIA_PREDETERMINADA);
		this.curso.setCursoEnMarcha(this);
	}

	public CursoEnMarcha(Curso curso, int vidas, Estrategia estrategia, TipoEstrategia tipoEstrategia) {
		this.bloqueActual = 0; // Empezar en el primer bloque (índice 0)
		this.preguntaActual = 1; // Empezar en la primera pregunta (índice 0)
		this.vidas = vidas;
		this.estrategia = estrategia;
		this.tipoEstrategia = tipoEstrategia;
		this.curso = curso;
		this.curso.setCursoEnMarcha(this);
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
			this.preguntaActual = 1;
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
		/*if (bloqueActual < 0 || bloqueActual >= this.curso.getBloques().size()) {
			return null;
		}

		if (preguntaActual < 0 || preguntaActual >= bloque.getPreguntas().size()) {
			return null;
		}*/
		Bloque bloque = this.getBloqueActual();
		return bloque.obtenerPregunta(preguntaActual);
	}

	public Bloque getBloqueActual() {
		if (bloqueActual < 0 || bloqueActual >= this.curso.getBloques().size()) {
			return null;
		}
		return this.curso.getBloqueEspecifico(bloqueActual);
	}

	public Bloque obtenerSiguienteBloque(int actual) {
		return getEstrategia().siguienteBloque(this.curso.getBloques(), actual, this.BloquesCompletos);

	}

	public Pregunta obtenerSiguientePregunta(int actual) {
		return getEstrategia().siguientePregunta(this.getBloqueActual(), actual, PreguntasCompletas);
	}

	public void finalizar() {
		// Implementar lógica para finalizar el curso
		System.out.println("¡Curso completado!");
	}

	public Estrategia getEstrategia() {
		if (estrategia == null)
			estrategia = generarEstrategia();
		return this.estrategia;
	}

	private Estrategia generarEstrategia() {
		switch (tipoEstrategia) {
			case ALEATORIA:
				return new EstrategiaAleatoria();
			case ESPACIADA:
				return new EstrategiaEspaciada();
			case SECUENCIAL:
			default:
				return new EstrategiaSecuencial();
		}
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

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	/*
	 * public void setBloqueActual(int bloqueActual) { if (bloqueActual >= 0 &&
	 * bloqueActual < this.curso.getBloques().size()) { this.bloqueActual =
	 * bloqueActual; this.preguntaActual = 0; // Reiniciar la pregunta al cambiar de
	 * bloque } }
	 */

	public TipoEstrategia getTipoEstrategia() {
		return tipoEstrategia;
	}

	/*
	 * public void setPreguntaActual(int preguntaActual) { Bloque bloque =
	 * getBloqueActual(); if (bloque != null && preguntaActual >= 0 &&
	 * preguntaActual < bloque.getPreguntas().size()) { this.preguntaActual =
	 * preguntaActual; } }
	 */
}