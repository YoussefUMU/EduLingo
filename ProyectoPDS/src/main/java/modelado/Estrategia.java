package modelado;

import java.util.List;

public interface Estrategia {
    Bloque siguienteBloque(List<Bloque> bloques, int actual, List<Bloque> bloquesCompletos);
    
    Pregunta siguientePregunta(Bloque Bactual, int Pactual, List<Pregunta> PreguntasCompletas );
}