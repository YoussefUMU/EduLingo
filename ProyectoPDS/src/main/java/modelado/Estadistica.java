package modelado;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Clase que almacena las estadísticas de uso de un usuario.
 */
@Entity
public class Estadistica {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private int tiempoUso; // Tiempo en segundos
	private int mejorRacha; // Cantidad de días consecutivos usando la aplicación
	private int preguntasRespondidas;
	private int preguntasCorrectas;
	private int cursosCompletados;
	private int sesionesEstudio;
	private LocalDate ultimoInicioSesion; // Timestamp para cálculo de rachas
	private int experiencia; // Nueva variable para almacenar la experiencia
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> cursosCompletadosIds; // Set para almacenar IDs de cursos completados
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> logrosDesbloqueados; // Lista para almacenar logros desbloqueados
	private LocalDateTime primeraActividadDelDia; // Primera actividad registrada en el día
	private LocalDateTime ultimaActividadDelDia; // Última actividad registrada en el día
	private int cursosProgramacionCompletados; // Contador de lenguajes de programación aprendidos

	// Constantes para cálculo de experiencia
	public static final int XP_POR_PREGUNTA_CORRECTA = 10;
	public static final int XP_POR_CURSO_COMPLETADO = 100;
	public static final int XP_POR_RACHA_DIARIA = 5;

	// Constantes para niveles
	public static final int XP_NIVEL_2 = 100;
	public static final int XP_NIVEL_3 = 250;
	public static final int XP_NIVEL_4 = 500;
	public static final int XP_NIVEL_5 = 1000;
	public static final int XP_NIVEL_10 = 5000;

	@OneToOne(mappedBy = "estadisticas")
	private Usuario usuario;

	/**
	 * Constructor por defecto que inicializa todas las estadísticas a cero.
	 */
	public Estadistica() {
		id = (long) 50;
		this.tiempoUso = 0;
		this.mejorRacha = 0;
		this.preguntasRespondidas = 0;
		this.preguntasCorrectas = 0;
		this.cursosCompletados = 0;
		this.sesionesEstudio = 0;
		this.ultimoInicioSesion = LocalDate.now();
		this.experiencia = 0;
		this.cursosCompletadosIds = new HashSet<>();
		this.logrosDesbloqueados = new ArrayList<>();
		this.primeraActividadDelDia = null;
		this.ultimaActividadDelDia = null;
		this.cursosProgramacionCompletados = 0;
	}

	/**
	 * Incrementa el tiempo de uso en la cantidad especificada de segundos.
	 * 
	 * @param segundos Cantidad de segundos a añadir al tiempo de uso.
	 */
	public void incrementarTiempoUso(int segundos) {
		this.tiempoUso += segundos;
	}

	/**
	 * Registra una nueva actividad y actualiza hora de primera/última actividad
	 */
	public void registrarActividad() {
		LocalDateTime ahora = LocalDateTime.now();

		// Si es la primera actividad del día o un día nuevo
		if (primeraActividadDelDia == null || !primeraActividadDelDia.toLocalDate().equals(ahora.toLocalDate())) {
			primeraActividadDelDia = ahora;
		}

		// Actualizar última actividad
		ultimaActividadDelDia = ahora;
	}

	/**
	 * Registra la finalización de un curso y otorga experiencia.
	 * 
	 * @param cursoId ID del curso completado
	 * @return true si es la primera vez que se completa este curso, false en caso
	 *         contrario
	 */
	public boolean registrarCursoCompletado(String cursoId, String categoria) {
		if (!cursosCompletadosIds.contains(cursoId)) {
			cursosCompletadosIds.add(cursoId);
			cursosCompletados++;

			// Aumentar experiencia
			experiencia += XP_POR_CURSO_COMPLETADO;

			// Si es un curso de programacion, incrementar contador
			if (categoria != null && categoria.toLowerCase().contains("programación")) {
				cursosProgramacionCompletados++;
			}

			return true;
		}
		return false;
	}

	/**
	 * Actualiza la racha de días si corresponde.
	 * 
	 * @param timestampActual Timestamp actual en formato Unix (segundos desde
	 *                        1970).
	 */
	public void actualizarRacha(LocalDate actual) {
		// Lógica para actualizar rachas basada en diferencia de días
		// Esta es una implementación simple, se puede mejorar según necesidades
		long diasDesdeUltimoUso = ChronoUnit.DAYS.between(ultimoInicioSesion, actual);
		if (diasDesdeUltimoUso == 1) {
			// El usuario ha vuelto un día después, incrementamos racha
			mejorRacha++;
			// Otorgar experiencia por mantener racha
			experiencia += XP_POR_RACHA_DIARIA;
		} else if (diasDesdeUltimoUso > 1) {
			// El usuario ha interrumpido su racha, reiniciamos a 1
			mejorRacha = 1;
		}
		ultimoInicioSesion = actual;
	}

	/**
	 * Registra una nueva pregunta respondida.
	 * 
	 * @param correcta true si la respuesta fue correcta, false en caso contrario.
	 */
	public void registrarPreguntaRespondida(boolean correcta) {
		registrarActividad();
		preguntasRespondidas++;
		if (correcta) {
			preguntasCorrectas++;
			// Otorgar experiencia por respuesta correcta
			experiencia += XP_POR_PREGUNTA_CORRECTA;
		}
	}

	/**
	 * Registra un logro desbloqueado si aún no lo estaba.
	 * 
	 * @param idLogro Identificador del logro
	 * @return true si se ha desbloqueado por primera vez, false si ya estaba
	 *         desbloqueado
	 */
	public boolean desbloquearLogro(String idLogro) {
		if (!logrosDesbloqueados.contains(idLogro)) {
			logrosDesbloqueados.add(idLogro);
			// Bonificación de experiencia por logro
			experiencia += 25;
			return true;
		}
		return false;
	}

	/**
	 * Verifica si un logro está desbloqueado.
	 * 
	 * @param idLogro Identificador del logro
	 * @return true si el logro está desbloqueado, false en caso contrario
	 */
	public boolean esLogroDesbloqueado(String idLogro) {
		return logrosDesbloqueados.contains(idLogro);
	}

	/**
	 * Obtiene el nivel actual del usuario basado en su experiencia.
	 * 
	 * @return Nivel actual (1-10)
	 */
	public int getNivelActual() {
		if (experiencia >= XP_NIVEL_10)
			return 10;
		if (experiencia >= XP_NIVEL_5)
			return 5 + (experiencia - XP_NIVEL_5) / ((XP_NIVEL_10 - XP_NIVEL_5) / 5);
		if (experiencia >= XP_NIVEL_4)
			return 4;
		if (experiencia >= XP_NIVEL_3)
			return 3;
		if (experiencia >= XP_NIVEL_2)
			return 2;
		return 1;
	}

	/**
	 * Obtiene el porcentaje de progreso en el nivel actual.
	 * 
	 * @return Porcentaje de progreso (0-100)
	 */
	public int getPorcentajeNivel() {
		int nivel = getNivelActual();
		int xpNivelActual = 0;
		int xpSiguienteNivel = XP_NIVEL_2;

		switch (nivel) {
		case 1:
			xpNivelActual = 0;
			xpSiguienteNivel = XP_NIVEL_2;
			break;
		case 2:
			xpNivelActual = XP_NIVEL_2;
			xpSiguienteNivel = XP_NIVEL_3;
			break;
		case 3:
			xpNivelActual = XP_NIVEL_3;
			xpSiguienteNivel = XP_NIVEL_4;
			break;
		case 4:
			xpNivelActual = XP_NIVEL_4;
			xpSiguienteNivel = XP_NIVEL_5;
			break;
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			xpNivelActual = XP_NIVEL_5;
			xpSiguienteNivel = XP_NIVEL_10;
			break;
		case 10:
			return 100; // Nivel máximo
		}

		int xpEnNivel = experiencia - xpNivelActual;
		int xpParaNivel = xpSiguienteNivel - xpNivelActual;
		return (int) ((xpEnNivel * 100.0) / xpParaNivel);
	}

	/**
	 * Verifica si el usuario estudió temprano (antes de las 8:00, después de las
	 * 5:00).
	 */
	public boolean estudioPorLaMañana() {
		if (primeraActividadDelDia == null)
			return false;
		return primeraActividadDelDia.toLocalTime().isBefore(LocalTime.of(8, 0))
				&& primeraActividadDelDia.toLocalTime().isAfter(LocalTime.of(5, 0));
	}

	/**
	 * Verifica si el usuario estudió tarde (después de las 22:00, antes de las
	 * 5:00).
	 */
	public boolean estudioPorLaNoche() {
		if (ultimaActividadDelDia == null)
			return false;
		LocalTime hora = ultimaActividadDelDia.toLocalTime();
		// Noche: hora > 22:00 O hora < 5:00
		return hora.isAfter(LocalTime.of(22, 0)) || hora.isBefore(LocalTime.of(5, 0));
	}

	public int getExperiencia() {
		return experiencia;
	}

	public void setExperiencia(int experiencia) {
		this.experiencia = experiencia;
	}

	public List<String> getLogrosDesbloqueados() {
		return new ArrayList<>(logrosDesbloqueados);
	}

	public Set<String> getCursosCompletadosIds() {
		return new HashSet<>(cursosCompletadosIds);
	}

	public int getCursosProgramacionCompletados() {
		return cursosProgramacionCompletados;
	}

	// Getters existentes
	public int getTiempoUso() {
		return tiempoUso;
	}

	public void setTiempoUso(int tiempoUso) {
		this.tiempoUso = tiempoUso;
	}

	public int getMejorRacha() {
		return mejorRacha;
	}

	public void setMejorRacha(int mejorRacha) {
		this.mejorRacha = mejorRacha;
	}

	public int getPreguntasRespondidas() {
		return preguntasRespondidas;
	}

	public int getPreguntasCorrectas() {
		return preguntasCorrectas;
	}

	public double getPorcentajeAciertos() {
		if (preguntasRespondidas == 0) {
			return 0.0;
		}
		return (double) preguntasCorrectas / preguntasRespondidas * 100.0;
	}

	public int getCursosCompletados() {
		return cursosCompletados;
	}

	public int getSesionesEstudio() {
		return sesionesEstudio;
	}

	public LocalDate getUltimoInicioSesion() {
		return ultimoInicioSesion;
	}

	public void setUltimoInicioSesion(LocalDate ultimoInicioSesion) {
		this.ultimoInicioSesion = ultimoInicioSesion;
	}

	public void setUsuario(Usuario usuario2) {
		usuario = usuario2;

	}
}