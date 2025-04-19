package modelado;

import java.util.List;

public class EstrategiaEspaciada implements Estrategia {

	@Override
	public Bloque siguienteBloque(List<Bloque> bloques, int actual, List<Bloque> bloquesCompletos) {
		return actual < bloques.size() - 1 ? bloques.get(actual + 1) : bloques.get(actual);
	}

	@Override
	public Pregunta siguientePregunta(Bloque Bactual, int Pactual, List<Pregunta> PreguntasCompletas) {
		List<Pregunta> todasLasPreguntas = Bactual.getPreguntas();

		// Comprobamos si venimos de una repetida
		boolean esRepetida = PreguntasCompletas.stream().anyMatch(p -> p.getNumPregunta() == Pactual);

		// Si toca una repetida (multiplo de 3), y NO venimos de una repetida
		if (Pactual % 3 == 0 && !esRepetida && !PreguntasCompletas.isEmpty()) {
			int indiceAleatorio = (int) (Math.random() * PreguntasCompletas.size());
			return PreguntasCompletas.get(indiceAleatorio);
		}

		// Cálculo del índice siguiente correcto
		int siguienteIndice;
		if (esRepetida && !PreguntasCompletas.isEmpty()) {
			Pregunta ultimaBuena = PreguntasCompletas.get(PreguntasCompletas.size() - 1);
			siguienteIndice = ultimaBuena.getNumPregunta() + 1;
		} else {
			siguienteIndice = Pactual + 1;
		}

		// Retornar la siguiente pregunta si existe
		if (siguienteIndice < todasLasPreguntas.size()) {
			for(Pregunta P : todasLasPreguntas) {
				if(P.getNumPregunta()==siguienteIndice) {
					return P;
				}
			}
		}

		return null; // Fin del bloque
	}

}
