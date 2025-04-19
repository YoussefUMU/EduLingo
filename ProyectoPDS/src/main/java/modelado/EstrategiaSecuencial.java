package modelado;

import java.util.List;

public class EstrategiaSecuencial implements Estrategia {
    @Override
    public Bloque siguienteBloque(List<Bloque> bloques, int actual, List<Bloque> bloquesCompletos) {
        return actual < bloques.size() - 1 ? bloques.get(actual + 1) : bloques.get(actual);
    }

	@Override
	public Pregunta siguientePregunta(Bloque Bactual, int Pactual, List<Pregunta> preguntasCompletas) {
		List<Pregunta> preguntas = Bactual.getPreguntas();
		if(Pactual <=(preguntas.size())) {
			for(Pregunta P: preguntas) {
				if(P.getNumPregunta() == (Pactual+1)) {
					//System.out.println(P.getEnunciado());
					return P;
				}
			}
		}
		return null;
	}
}