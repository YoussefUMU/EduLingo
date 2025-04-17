package modelado;

import java.util.List;

public class EstrategiaAleatoria implements Estrategia {

	@Override
	public Bloque siguienteBloque(List<Bloque> bloques, int actual, List<Bloque> bloquesCompletos) {
		// Lista de bloques disponibles que cumplen las condiciones
		List<Bloque> bloquesDisponibles = bloques.stream()
				.filter(bloque -> !bloquesCompletos.contains(bloque) && !bloque.equals(bloques.get(actual))).toList();

		// Si no hay bloques disponibles, devuelve null
		if (bloquesDisponibles.isEmpty()) {
			return null;
		}

		// Selecciona un bloque aleatoriamente
		int indiceAleatorio = (int) (Math.random() * bloquesDisponibles.size());
		return bloquesDisponibles.get(indiceAleatorio);
	}

	@Override
	public Pregunta siguientePregunta(Bloque Bactual, int Pactual, List<Pregunta> PreguntasCompletas) {
		// Filtrar las preguntas disponibles en el bloque actual
		List<Pregunta> preguntasDisponibles = Bactual.getPreguntas().stream()
				.filter(pregunta -> !PreguntasCompletas.contains(pregunta) && pregunta.getNumPregunta() != Pactual)
				.toList();

		// Si no hay preguntas disponibles, devolver null
		if (preguntasDisponibles.isEmpty()) {
			return null;
		}

		// Seleccionar una pregunta aleatoriamente
		int indiceAleatorio = (int) (Math.random() * preguntasDisponibles.size());
		return preguntasDisponibles.get(indiceAleatorio);
	}

}
