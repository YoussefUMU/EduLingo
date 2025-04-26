package vista;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import modelado.CursoEnMarcha;
import modelado.Pregunta;
import modelado.PreguntaArrastrar;
import modelado.PreguntaImagen;
import modelado.TestPregunta;

/**
 * Esta clase se encarga de crear la flashcard apropiada según el tipo de pregunta
 */
public class AdaptadorPreguntas {
    
    /**
     * Crea y muestra la flashcard apropiada según el tipo de pregunta actual
     * @param cursoEnMarcha El curso en marcha actual
     * @param indBloque Índice del bloque actual
     * @param indPregunta Índice de la pregunta actual
     * @return La ventana de flashcard creada
     */
    public static JFrame crearFlashCard(CursoEnMarcha cursoEnMarcha, int indBloque, int indPregunta) {
        Pregunta preguntaActual = cursoEnMarcha.getPreguntaActual();
        
        if (preguntaActual instanceof TestPregunta) {
            // Crear FlashCard Tipo A para preguntas de opción múltiple
            FlashCardTipoA flashCard = new FlashCardTipoA(cursoEnMarcha, indBloque, indPregunta);
            return flashCard;
        } 
        else if (preguntaActual instanceof PreguntaImagen) {
            // Crear FlashCard Tipo B para preguntas con imágenes
            return crearFlashCardTipoB(cursoEnMarcha, indBloque, indPregunta);
        } 
        else if (preguntaActual instanceof PreguntaArrastrar) {
            // Crear FlashCard Tipo C para preguntas de arrastrar
            FlashCardTipoC flashCard = new FlashCardTipoC(cursoEnMarcha, indBloque, indPregunta);
            return flashCard;
        } 
        else {
            // Si el tipo no se reconoce, mostrar un mensaje de error
            JOptionPane.showMessageDialog(null, 
                    "Tipo de pregunta no soportado: " + preguntaActual.getClass().getName(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    /**
     * Crea un JFrame con FlashCardTipoB basado en una PreguntaImagen
     */
    private static JFrame crearFlashCardTipoB(CursoEnMarcha cursoEnMarcha, int indBloque, int indPregunta) {
        // Obtener la pregunta del tipo imagen
        PreguntaImagen pregunta = (PreguntaImagen) cursoEnMarcha.getPreguntaActual();
        
        // Crear el JFrame de tipo B
        FlashCardTipoB frame = new FlashCardTipoB(cursoEnMarcha, indBloque, indPregunta);
        
        // Configurar la pregunta
        frame.setPregunta("B"+cursoEnMarcha.getBloqueActualIndex()+"."+cursoEnMarcha.getPreguntaActualIndex()+"."+pregunta.getEnunciado());
        frame.setVidas(cursoEnMarcha.getVidas());
        
        // Configurar imágenes y textos
        List<String> imagenes = pregunta.getImagenes();
        List<String> textos = pregunta.getTextos();
        
        // Asegurarse de que tenemos al menos 3 opciones o completar con opciones vacías
        while (imagenes.size() < 3) imagenes.add("");
        while (textos.size() < 3) textos.add("Opción " + (textos.size() + 1));
        
        // Establecer las imágenes (máximo 3 en el tipo B)
        for (int i = 0; i < Math.min(3, imagenes.size()); i++) {
            try {
                String imagenURL = imagenes.get(i);
                // Intentar cargar la imagen desde URL
                ImageIcon imagen = null;
                
                if (imagenURL != null && !imagenURL.isEmpty()) {
                    // Primero intentar como URL absoluta
                    imagen = new ImageIcon(imagenURL);
                    
                    // Si falla, intentar como recurso
                    if (imagen.getIconWidth() <= 0) {
                        imagen = new ImageIcon(frame.getClass().getResource(imagenURL));
                    }
                    
                    // Si sigue fallando, usar una imagen por defecto
                    if (imagen.getIconWidth() <= 0) {
                        imagen = new ImageIcon(frame.getClass().getResource("/recursos/pregunta_img.png"));
                        // Si no hay recurso predeterminado, crear una imagen con texto
                        if (imagen.getIconWidth() <= 0) {
                            BufferedImage bufferedImage = new BufferedImage(
                                200, 150, BufferedImage.TYPE_INT_ARGB);
                            Graphics2D g2d = bufferedImage.createGraphics();
                            g2d.setColor(Color.LIGHT_GRAY);
                            g2d.fillRect(0, 0, 200, 150);
                            g2d.setColor(Color.BLACK);
                            g2d.drawString("Imagen " + (i+1), 70, 75);
                            g2d.dispose();
                            imagen = new ImageIcon(bufferedImage);
                        }
                    }
                    
                    // Redimensionar la imagen para que se ajuste mejor
                    Image img = imagen.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                    frame.setImagenOpcion(i, new ImageIcon(img));
                } else {
                    // URL vacía, crear imagen con texto
                    BufferedImage bufferedImage = new BufferedImage(
                        200, 150, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = bufferedImage.createGraphics();
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.fillRect(0, 0, 200, 150);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("Imagen " + (i+1), 70, 75);
                    g2d.dispose();
                    frame.setImagenOpcion(i, new ImageIcon(bufferedImage));
                }
                
                frame.setTextoOpcion(i, textos.get(i));
            } catch (Exception e) {
                // En caso de error, crear una imagen con texto
                BufferedImage bufferedImage = new BufferedImage(
                    200, 150, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillRect(0, 0, 200, 150);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Error: " + e.getMessage(), 20, 75);
                g2d.dispose();
                frame.setImagenOpcion(i, new ImageIcon(bufferedImage));
                frame.setTextoOpcion(i, textos.get(i));
            }
        }
        
        // Identificar cuál es la opción correcta
        int imagenCorrecta = pregunta.getImagenCorrecta();
        
        // Configurar acciones para cada opción
        for (int i = 0; i < 3; i++) {
            final int index = i;
            frame.configurarOpcion(i, i == imagenCorrecta);
        }
        
        // Configurar acciones para respuestas correctas e incorrectas
        frame.configurarAcciones(
            // Acción para respuesta correcta - Eliminamos el mensaje de diálogo
            () -> {
                // Actualizar vidas en el curso en marcha
                cursoEnMarcha.setVidas(frame.getVidas());
                
                // Avanzar a la siguiente pregunta sin mensaje interruptor
                NavegadorPreguntas.avanzarSiguientePregunta(cursoEnMarcha, frame);
            },
            // Acción para respuesta incorrecta cuando se acaban las vidas
            () -> {
                JOptionPane.showMessageDialog(frame, 
                    "Se te han acabado las vidas. La respuesta correcta era la opción " + (imagenCorrecta + 1),
                    "Fin del ejercicio", 
                    JOptionPane.ERROR_MESSAGE);
                frame.dispose();
                new VentanaPrincipal().setVisible(true);
            }
        );
        
        // Iniciar temporizador
        frame.iniciarTemporizador(20000, () -> {
            JOptionPane.showMessageDialog(frame, 
                "¡Se acabó el tiempo!", 
                "Tiempo agotado", 
                JOptionPane.INFORMATION_MESSAGE);
            cursoEnMarcha.setVidas(frame.getVidas() - 1);
            if (cursoEnMarcha.getVidas() <= 0) {
                JOptionPane.showMessageDialog(frame, 
                    "Se te han acabado las vidas.", 
                    "Fin del ejercicio", 
                    JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                new VentanaPrincipal().setVisible(true);
            }
        });
        
        return frame;
    }
}