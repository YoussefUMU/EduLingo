package modelado;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase encargada de gestionar los logros del usuario
 */
public class GestorLogros {
    
    // Constantes para identificar logros
    public static final String LOGRO_PRIMER_DIA = "primer_dia";
    public static final String LOGRO_RACHA_7 = "racha_x7";
    public static final String LOGRO_RACHA_30 = "racha_x30";
    public static final String LOGRO_EXPLORADOR = "explorador";
    public static final String LOGRO_MADRUGADOR = "madrugador";
    public static final String LOGRO_NOCTURNO = "nocturno";
    public static final String LOGRO_MAESTRO_JAVA = "maestro_java";
    public static final String LOGRO_POLIGLOTA = "poliglota";
    public static final String LOGRO_COLABORADOR = "colaborador";
    
    // Información de los logros (título, descripción, etc.)
    private static final Map<String, LogroInfo> infoLogros = new HashMap<>();
    
    static {
        // Inicializar información de logros
        infoLogros.put(LOGRO_PRIMER_DIA, 
                new LogroInfo("Primer Día", "Completaste tu primera lección"));
        
        infoLogros.put(LOGRO_RACHA_7, 
                new LogroInfo("Racha x7", "Mantuviste una racha de 7 días"));
        
        infoLogros.put(LOGRO_RACHA_30, 
                new LogroInfo("Racha x30", "Mantén una racha de 30 días"));
        
        infoLogros.put(LOGRO_EXPLORADOR, 
                new LogroInfo("Explorador", "Probaste 3 cursos diferentes"));
        
        infoLogros.put(LOGRO_MADRUGADOR, 
                new LogroInfo("Madrugador", "Estudiaste antes de las 8:00 AM"));
        
        infoLogros.put(LOGRO_NOCTURNO, 
                new LogroInfo("Nocturno", "Estudiaste después de las 22:00"));
        
        infoLogros.put(LOGRO_MAESTRO_JAVA, 
                new LogroInfo("Maestro Java", "Completa el curso de Java"));
        
        infoLogros.put(LOGRO_POLIGLOTA, 
                new LogroInfo("Políglota", "Aprende 3 lenguajes"));
        
        infoLogros.put(LOGRO_COLABORADOR, 
                new LogroInfo("Colaborador", "Crea tu primer curso"));
    }
    
    /**
     * Comprueba y actualiza los logros basándose en las estadísticas del usuario
     * @param usuario Usuario a verificar
     * @return Array de IDs de los nuevos logros desbloqueados (si hay alguno)
     */
    public static String[] comprobarLogros(Usuario usuario) {
        Estadistica stats = usuario.getEstadisticas();
        ArrayList<String> nuevosLogros = new ArrayList<>();
        
        // Comprobar logro: Primer día
        if (stats.getPreguntasRespondidas() > 0) {
            if (stats.desbloquearLogro(LOGRO_PRIMER_DIA)) {
                nuevosLogros.add(LOGRO_PRIMER_DIA);
            }
        }
        
        // Comprobar logro: Racha x7
        if (stats.getMejorRacha() >= 7) {
            if (stats.desbloquearLogro(LOGRO_RACHA_7)) {
                nuevosLogros.add(LOGRO_RACHA_7);
            }
        }
        
        // Comprobar logro: Racha x30
        if (stats.getMejorRacha() >= 30) {
            if (stats.desbloquearLogro(LOGRO_RACHA_30)) {
                nuevosLogros.add(LOGRO_RACHA_30);
            }
        }
        
        // Comprobar logro: Explorador
        if (stats.getCursosCompletadosIds().size() >= 3) {
            if (stats.desbloquearLogro(LOGRO_EXPLORADOR)) {
                nuevosLogros.add(LOGRO_EXPLORADOR);
            }
        }
        
        // Comprobar logro: Madrugador
        if (stats.estudioPorLaMañana()) {
            if (stats.desbloquearLogro(LOGRO_MADRUGADOR)) {
                nuevosLogros.add(LOGRO_MADRUGADOR);
            }
        }
        
        // Comprobar logro: Nocturno
        if (stats.estudioPorLaNoche()) {
            if (stats.desbloquearLogro(LOGRO_NOCTURNO)) {
                nuevosLogros.add(LOGRO_NOCTURNO);
            }
        }
        
        // Comprobar logro: Maestro Java
        for (String cursoId : stats.getCursosCompletadosIds()) {
            // Verificar si alguno de los cursos completados es de Java
            if (cursoId.toLowerCase().contains("java")) {
                if (stats.desbloquearLogro(LOGRO_MAESTRO_JAVA)) {
                    nuevosLogros.add(LOGRO_MAESTRO_JAVA);
                }
                break;
            }
        }
        
        // Comprobar logro: Políglota
        if (stats.getLenguajesAprendidos() >= 3) {
            if (stats.desbloquearLogro(LOGRO_POLIGLOTA)) {
                nuevosLogros.add(LOGRO_POLIGLOTA);
            }
        }
        
        // El logro 'Colaborador' se verificará cuando se implemente la creación de cursos
        
        return nuevosLogros.toArray(new String[0]);
    }
    
    /**
     * Obtiene la información asociada a un logro
     * @param idLogro Identificador del logro
     * @return Información del logro o null si no existe
     */
    public static LogroInfo getInfoLogro(String idLogro) {
        return infoLogros.get(idLogro);
    }
    
    /**
     * Obtiene todos los IDs de logros disponibles
     * @return Array con todos los IDs de logros
     */
    public static String[] getTodosLosLogrosIds() {
        return infoLogros.keySet().toArray(new String[0]);
    }
    
    /**
     * Obtiene un mapa con la información de todos los logros
     */
    public static Map<String, LogroInfo> getTodosLosLogros() {
        return new HashMap<>(infoLogros);
    }
    
    /**
     * Clase interna para almacenar información del logro
     */
    public static class LogroInfo {
        private String titulo;
        private String descripcion;
        
        public LogroInfo(String titulo, String descripcion) {
            this.titulo = titulo;
            this.descripcion = descripcion;
        }
        
        public String getTitulo() {
            return titulo;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
}