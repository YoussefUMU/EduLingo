package modelado;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstadisticaTest {

	private Estadistica stats;

	@BeforeEach
	void setUp() {
		stats = new Estadistica();
	}

	@Test
	void testConstructorInicializaACero() {
		assertEquals(0, stats.getTiempoUso());
		assertEquals(0, stats.getMejorRacha());
		assertEquals(0, stats.getPreguntasRespondidas());
		assertEquals(0, stats.getPreguntasCorrectas());
		assertEquals(0, stats.getCursosCompletados());
		assertEquals(0, stats.getLenguajesAprendidos());
		assertTrue(stats.getCursosCompletadosIds().isEmpty());
		assertTrue(stats.getLogrosDesbloqueados().isEmpty());
		// experiencia inicial < XP_NIVEL_2
		assertEquals(1, stats.getNivelActual());
		assertEquals(0, stats.getPorcentajeNivel());
	}

	@Test
	void testIncrementarTiempoUso() {
		stats.incrementarTiempoUso(30);
		assertEquals(30, stats.getTiempoUso());
		stats.incrementarTiempoUso(45);
		assertEquals(75, stats.getTiempoUso());
	}

	@Test
	void testRegistrarCursoCompletadoYLenguaje() {
		boolean primera = stats.registrarCursoCompletado("C1", "Programación Java");
		assertTrue(primera, "Debe retornar true la primera vez");
		assertTrue(stats.getCursosCompletadosIds().contains("C1"));
		assertEquals(1, stats.getCursosCompletados());
		// XP por curso completado
		assertEquals(Estadistica.XP_POR_CURSO_COMPLETADO, stats.getExperiencia());
		assertEquals(1, stats.getLenguajesAprendidos());

		// repetido
		boolean segunda = stats.registrarCursoCompletado("C1", "Programación Java");
		assertFalse(segunda, "No desbloquea dos veces el mismo curso");
		assertEquals(1, stats.getCursosCompletados());
	}

	@Test
	void testActualizarRacha() {
		// Simular primer timestamp (en segundos desde 1970)
		stats.setUltimoInicioSesion(0);
		// Un día después exacto (86400 segundos)
		stats.actualizarRacha(86400);
		assertEquals(1, stats.getMejorRacha(), "Al cabo de un día, racha debe ser 1");
		assertEquals(Estadistica.XP_POR_RACHA_DIARIA, stats.getExperiencia(),
				"Debemos recibir sólo XP_POR_RACHA_DIARIA tras el aumento de racha");

		// Más de un día (ruptura de racha)
		stats.actualizarRacha(86400 * 3);
		assertEquals(1, stats.getMejorRacha(), "Si se interrumpe la racha, se reinicia a 1");
	}

	@Test
	void testRegistrarPreguntaRespondida() {
		stats.registrarPreguntaRespondida(false);
		assertEquals(1, stats.getPreguntasRespondidas());
		assertEquals(0, stats.getPreguntasCorrectas());

		stats.registrarPreguntaRespondida(true);
		assertEquals(2, stats.getPreguntasRespondidas());
		assertEquals(1, stats.getPreguntasCorrectas());

		// XP: sólo 10 por la respuesta correcta
		assertEquals(Estadistica.XP_POR_PREGUNTA_CORRECTA, stats.getExperiencia(),
				"Debemos recibir solo XP_POR_PREGUNTA_CORRECTA tras una respuesta correcta");
	}

	@Test
	void testDesbloquearYVerificarLogro() {
		boolean ok = stats.desbloquearLogro("L1");
		assertTrue(ok);
		assertTrue(stats.esLogroDesbloqueado("L1"));
		assertEquals(1, stats.getLogrosDesbloqueados().size());

		// XP adicional por logro: 25 puntos
		assertEquals(25, stats.getExperiencia(), "Desbloqueo de logro añade 25 XP");

		// desbloquear de nuevo debe devolver false y no sumar más XP
		assertFalse(stats.desbloquearLogro("L1"));
		assertEquals(25, stats.getExperiencia(), "No debe sumar XP al intentar desbloquear un logro ya existente");
	}

	@Test
	void testNivelesYPorcentaje() {
		stats.setExperiencia(0);
		assertEquals(1, stats.getNivelActual());
		assertEquals(0, stats.getPorcentajeNivel());

		stats.setExperiencia(150);
		assertEquals(2, stats.getNivelActual());
		// (150−100)/(250−100)*100 = 50/150*100 ≈ 33%
		assertEquals(33, stats.getPorcentajeNivel());

		stats.setExperiencia(600);
		assertEquals(4, stats.getNivelActual());
		// (600−500)/(1000−500)*100 = 100/500*100 = 20%
		assertEquals(20, stats.getPorcentajeNivel());

		stats.setExperiencia(1200);
		assertEquals(5, stats.getNivelActual());
		// (1200−1000)/(5000−1000)*100 = 200/4000*100 = 5%
		assertEquals(5, stats.getPorcentajeNivel());

		stats.setExperiencia(6000);
		assertEquals(10, stats.getNivelActual());
		assertEquals(100, stats.getPorcentajeNivel());
	}

	@Test
	void testEstudioPorLaMananaYPorLaNoche() throws Exception {
		// inyectar manualmente primera y última actividad
		Field primer = Estadistica.class.getDeclaredField("primeraActividadDelDia");
		primer.setAccessible(true);
		primer.set(stats, LocalDateTime.of(LocalDate.now(), LocalTime.of(6, 0)));

		Field ultima = Estadistica.class.getDeclaredField("ultimaActividadDelDia");
		ultima.setAccessible(true);
		ultima.set(stats, LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 0)));

		assertTrue(stats.estudioPorLaMañana(), "6:00 debe considerarse mañana");
		assertTrue(stats.estudioPorLaNoche(), "23:00 debe considerarse noche");

		// fuera de rango
		primer.set(stats, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 0)));
		ultima.set(stats, LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0)));
		assertFalse(stats.estudioPorLaMañana());
		assertFalse(stats.estudioPorLaNoche());
	}
}
