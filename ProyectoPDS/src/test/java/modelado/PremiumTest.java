package modelado;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PremiumTest {

	private Premium premiumMensual;
	private Premium premiumAnual;

	@BeforeEach
	void setUp() {
		// Crea con constructor por defecto (mensual)
		premiumMensual = new Premium();
		// Crea con constructor anual
		premiumAnual = new Premium("anual");
	}

	@Test
	void testConstructorPorDefecto() {
		// fechaInicio hoy, fechaFin > fechaInicio + 0 meses
		assertNotNull(premiumMensual.getFechaInicio());
		assertNotNull(premiumMensual.getFechaFin());
		assertTrue(premiumMensual.getFechaFin().isAfter(premiumMensual.getFechaInicio()));

		assertTrue(premiumMensual.isActivo(), "Debería estar activo tras creación");
		assertEquals("mensual", premiumMensual.getTipoPlan(), "Plan por defecto debe ser 'mensual'");
		assertTrue(premiumMensual.isVidasInfinitas(), "Vidas infinitas deben estar activas por defecto");
	}

	@Test
	void testConstructorAnual() {
		assertNotNull(premiumAnual.getFechaInicio());
		assertNotNull(premiumAnual.getFechaFin());
		// fechaFin debe ser al menos un año tras fechaInicio
		assertTrue(premiumAnual.getFechaFin().isAfter(premiumAnual.getFechaInicio().plusMonths(11)),
				"Con plan anual, fechaFin debe estar ~1 año después");

		assertTrue(premiumAnual.isActivo());
		assertEquals("anual", premiumAnual.getTipoPlan(), "Tipo de plan debe ser 'anual'");
		assertTrue(premiumAnual.isVidasInfinitas());
	}

	@Test
	void testEstaActivoConFechaExpirada() {
		// Simula expiración: fija fechaFin a ayer
		premiumMensual.setFechaFin(LocalDate.now().minusDays(1));
		premiumMensual.setActivo(true);
		assertFalse(premiumMensual.estaActivo(), "No debe estar activo si la fechaFin ya pasó");

		// Simula cancelación: fechaFin en futuro pero activo=false
		premiumMensual.setFechaFin(LocalDate.now().plusDays(10));
		premiumMensual.setActivo(false);
		assertFalse(premiumMensual.estaActivo(), "No debe estar activo si el flag 'activo' es false");
	}

	@Test
	void testRenovarAntesDeExpiracion() {
		// Con fechaFin en el futuro, renovar extiende fechaFin
		LocalDate originalFin = premiumMensual.getFechaFin();
		premiumMensual.renovar(2); // +2 meses
		assertTrue(premiumMensual.getFechaFin().isAfter(originalFin.plusMonths(1)),
				"Renovar 2 meses debe extender más allá de +1 mes original");
		assertTrue(premiumMensual.isActivo(), "Tras renovar debe estar activo");
		assertEquals("mensual", premiumMensual.getTipoPlan(), "Renovar <12 meses mantiene 'mensual'");
	}

	@Test
	void testRenovarDespuesDeExpiracion() {
		// Simula expiración: fechaFin cinco días antes de hoy
		premiumMensual.setFechaFin(LocalDate.now().minusDays(5));

		// Hacemos la renovación de 3 meses
		premiumMensual.renovar(3);

		// Tras renovación, fechaInicio debe ser hoy
		assertEquals(LocalDate.now(), premiumMensual.getFechaInicio(),
				"Si ya expiró, fechaInicio debe reiniciarse a hoy");

		// Y fechaFin debe ser fechaInicio + 3 meses
		assertEquals(premiumMensual.getFechaInicio().plusMonths(3), premiumMensual.getFechaFin(),
				"FechaFin debe ser today + meses renovados");

		assertTrue(premiumMensual.isActivo(), "Tras renovar, activo debe ser true");
		assertEquals("mensual", premiumMensual.getTipoPlan(), "Renovar <12 meses mantiene 'mensual'");
	}

	@Test
	void testRenovarConPlanAnual() {
		// Expirado o no, si meses>=12 cambia a anual
		LocalDate originalFin = premiumMensual.getFechaFin();
		premiumMensual.renovar(12);
		assertTrue(premiumMensual.getFechaFin().isAfter(originalFin));
		assertTrue(premiumMensual.isActivo());
		assertEquals("anual", premiumMensual.getTipoPlan(), "Renovar 12 meses debe convertir en 'anual'");
	}

	@Test
	void testCancelar() {
		premiumMensual.cancelar();
		assertFalse(premiumMensual.isActivo(), "Flag interno 'activo' debe ser false tras cancelar");
		// Incluso con fechaFin en el futuro, estaActivo() debe ser false
		premiumMensual.setFechaFin(LocalDate.now().plusDays(10));
		assertFalse(premiumMensual.estaActivo());
	}

	@Test
	void testVidasInfinitasSetterGetter() {
		premiumMensual.setVidasInfinitas(false);
		assertFalse(premiumMensual.isVidasInfinitas(), "Setter de vidasInfinitas debe funcionar");
		premiumMensual.setVidasInfinitas(true);
		assertTrue(premiumMensual.isVidasInfinitas());
	}
}
