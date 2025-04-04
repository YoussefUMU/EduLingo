package modelado;

/**
 * Clase que almacena las estadísticas de uso de un usuario.
 */
public class Estadistica {
    private int tiempoUso; // Tiempo en segundos
    private int mejorRacha; // Cantidad de días consecutivos usando la aplicación
    private int preguntasRespondidas;
    private int preguntasCorrectas;
    private int cursosCompletados;
    private int sesionesEstudio;
    private int ultimoInicioSesion; // Timestamp para cálculo de rachas

    /**
     * Constructor por defecto que inicializa todas las estadísticas a cero.
     */
    public Estadistica() {
        this.tiempoUso = 0;
        this.mejorRacha = 0;
        this.preguntasRespondidas = 0;
        this.preguntasCorrectas = 0;
        this.cursosCompletados = 0;
        this.sesionesEstudio = 0;
        this.ultimoInicioSesion = 0;
    }

    /**
     * Incrementa el tiempo de uso en la cantidad especificada de segundos.
     * @param segundos Cantidad de segundos a añadir al tiempo de uso.
     */
    public void incrementarTiempoUso(int segundos) {
        this.tiempoUso += segundos;
    }

    /**
     * Actualiza la racha de días si corresponde.
     * @param timestampActual Timestamp actual en formato Unix (segundos desde 1970).
     */
    public void actualizarRacha(int timestampActual) {
        // Lógica para actualizar rachas basada en diferencia de días
        // Esta es una implementación simple, se puede mejorar según necesidades
        int segundosEnUnDia = 86400;
        int diasDesdeUltimoUso = (timestampActual - ultimoInicioSesion) / segundosEnUnDia;
        
        if (diasDesdeUltimoUso == 1) {
            // El usuario ha vuelto un día después, incrementamos racha
            mejorRacha++;
        } else if (diasDesdeUltimoUso > 1) {
            // El usuario ha interrumpido su racha, reiniciamos a 1
            mejorRacha = 1;
        }
        
        ultimoInicioSesion = timestampActual;
    }
    
    /**
     * Registra una nueva pregunta respondida.
     * @param correcta true si la respuesta fue correcta, false en caso contrario.
     */
    public void registrarPreguntaRespondida(boolean correcta) {
        preguntasRespondidas++;
        if (correcta) {
            preguntasCorrectas++;
        }
    }
    
    /**
     * Registra la finalización de un curso.
     */
    public void registrarCursoCompletado() {
        cursosCompletados++;
    }
    
    /**
     * Registra una nueva sesión de estudio.
     */
    public void registrarSesionEstudio() {
        sesionesEstudio++;
    }

    // Getters y setters
    
    public int getTiempoUso() {
        return tiempoUso;
    }

    public void setTiempoUso(int tiempoUso) {
        this.tiempoUso = tiempoUso;
    }

    public int getMejorRacha() {
        return mejorRacha;
    }

    public void setMejorRacha(int mejorRacha) {
        this.mejorRacha = mejorRacha;
    }
    
    public int getPreguntasRespondidas() {
        return preguntasRespondidas;
    }
    
    public int getPreguntasCorrectas() {
        return preguntasCorrectas;
    }
    
    public double getPorcentajeAciertos() {
        if (preguntasRespondidas == 0) {
            return 0.0;
        }
        return (double) preguntasCorrectas / preguntasRespondidas * 100.0;
    }
    
    public int getCursosCompletados() {
        return cursosCompletados;
    }
    
    public int getSesionesEstudio() {
        return sesionesEstudio;
    }
    
    public int getUltimoInicioSesion() {
        return ultimoInicioSesion;
    }
    
    public void setUltimoInicioSesion(int ultimoInicioSesion) {
        this.ultimoInicioSesion = ultimoInicioSesion;
    }
}