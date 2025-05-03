package modelado;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EstrategiaSecuencial implements Estrategia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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