package modelado;

import java.util.List;

public interface Estrategia {
    Bloque siguiente(List<Bloque> bloques, int actual);
}