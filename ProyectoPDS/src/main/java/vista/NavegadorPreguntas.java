package vista;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import modelado.CursoEnMarcha;

/**
 * Utilidad para navegar entre preguntas de un curso
 */
public class NavegadorPreguntas {
    
    /**
     * Avanza a la siguiente pregunta o finaliza el curso si es la última
     * @param cursoEnMarcha El curso actual
     * @param ventanaActual La ventana de flashcard actual para cerrarla
     */
    public static void avanzarSiguientePregunta(CursoEnMarcha cursoEnMarcha, JFrame ventanaActual) {
        // Guardamos la referencia del bloque y pregunta actuales
        int bloqueActual = cursoEnMarcha.getBloqueActualIndex();
        int preguntaActual = cursoEnMarcha.getPreguntaActualIndex();
        
        // Avanzamos a la siguiente pregunta en el curso
        cursoEnMarcha.avanzarPregunta();
        
        // Verificar si hemos llegado al final del curso
        boolean esFinal = false;
        
        // Un curso finaliza cuando:
        // 1. Estamos en el último bloque Y
        // 2. Estamos en la última pregunta de ese bloque
        int ultimoBloqueIndex = cursoEnMarcha.getCurso().getBloques().size() - 1;
        
        if (bloqueActual == ultimoBloqueIndex) {
            int ultimaPreguntaIndex = cursoEnMarcha.getCurso().getBloques().get(ultimoBloqueIndex).getPreguntas().size() - 1;
            if (preguntaActual == ultimaPreguntaIndex) {
                esFinal = true;
            }
        }
        if (esFinal) {
            // Hemos terminado el curso
            JOptionPane.showMessageDialog(ventanaActual, 
                "¡Felicidades! Has completado este curso con éxito.", 
                "Curso completado", JOptionPane.INFORMATION_MESSAGE);
            
            // Finalizar el curso en el modelo
            cursoEnMarcha.finalizar();
            
            ventanaActual.dispose();
            new VentanaPrincipal().setVisible(true);
        } else {
            // No es el final, continuamos con la siguiente pregunta
            ventanaActual.dispose();
            
            // Crear y mostrar la siguiente flashcard
            JFrame siguienteFlashcard = AdaptadorPreguntas.crearFlashCard(
                cursoEnMarcha, 
                cursoEnMarcha.getBloqueActualIndex(), 
                cursoEnMarcha.getPreguntaActualIndex()
            );
            
            if (siguienteFlashcard != null) {
                siguienteFlashcard.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Error al cargar la siguiente pregunta.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                new VentanaPrincipal().setVisible(true);
            }
        }
    }
}