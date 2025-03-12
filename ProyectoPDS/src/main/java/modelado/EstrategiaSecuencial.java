package modelado;

import java.util.List;

public class EstrategiaSecuencial implements Estrategia {
    @Override
    public Bloque siguiente(List<Bloque> bloques, int actual) {
        return actual < bloques.size() - 1 ? bloques.get(actual + 1) : bloques.get(actual);
    }
}