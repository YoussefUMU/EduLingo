package modelado;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class EstrategiaTest {

	static Stream<Estrategia> estrategiasProvider() {
		return Stream.of(new EstrategiaAleatoria(), new EstrategiaEspaciada(), new EstrategiaSecuencial());
	}

	@ParameterizedTest
	@MethodSource("estrategiasProvider")
	/*
	 * void testSiguienteBloque_validoOControlado(Estrategia estrategia) {
	 * List<Bloque> bloques = crearBloques(3, 3); List<Bloque> completados = new
	 * ArrayList<>();
	 * 
	 * try { Bloque siguiente = estrategia.siguienteBloque(bloques, 0, completados);
	 * 
	 * // Permitir null si es esperable en lógica (como en Aleatoria sin opciones)
	 * if (estrategia instanceof EstrategiaAleatoria && bloques.size() <= 1) {
	 * assertNull(siguiente); } else { assertNotNull(siguiente,
	 * estrategia.getClass().getSimpleName() + " devolvió null en siguienteBloque");
	 * }
	 * 
	 * } catch (Exception e) { fail(estrategia.getClass().getSimpleName() +
	 * " lanzó excepción: " + e.getMessage()); } }
	 */

	void testSiguienteBloque_noDevuelveNull(Estrategia estrategia) {
		List<Bloque> bloques = crearBloques(3, 3);
		List<Bloque> completados = new ArrayList<>();

		Bloque siguiente = estrategia.siguienteBloque(bloques, 0, completados);
		assertNotNull(siguiente, estrategia.getClass().getSimpleName() + " devolvió null en siguienteBloque");
	}

	@ParameterizedTest
	@MethodSource("estrategiasProvider")
	void testSiguientePregunta_noDevuelveNull(Estrategia estrategia) {
		List<Bloque> bloques = crearBloques(1, 5);
		Bloque bloque = bloques.get(0);
		List<Pregunta> completadas = new ArrayList<>();

		Pregunta siguiente = estrategia.siguientePregunta(bloque, 1, completadas);
		assertNotNull(siguiente, estrategia.getClass().getSimpleName() + " devolvió null en siguientePregunta");
	}

	// Helpers
	private List<Bloque> crearBloques(int numBloques, int numPreguntasPorBloque) {
		List<Bloque> bloques = new ArrayList<>();
		for (int i = 0; i < numBloques; i++) {
			List<Pregunta> preguntas = new ArrayList<>();
			for (int j = 1; j <= numPreguntasPorBloque; j++) {
				preguntas.add(
						new PreguntaTest(j, "¿Cuál es la respuesta a " + j + "?", List.of("A", "B", "C", "D"), "A"));
			}

			// Títulos únicos para evitar problemas con equals()
			Bloque b = new Bloque("Bloque " + i, preguntas);
			bloques.add(b);
		}
		return bloques;
	}

}
