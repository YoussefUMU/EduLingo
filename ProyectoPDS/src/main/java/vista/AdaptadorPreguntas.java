package vista;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
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
            PreguntaTipoA flashCard = new PreguntaTipoA(cursoEnMarcha, indBloque, indPregunta);
            return flashCard;
        } 
        else if (preguntaActual instanceof PreguntaImagen) {
            // Crear FlashCard Tipo B para preguntas con imágenes
            return crearFlashCardTipoB(cursoEnMarcha, indBloque, indPregunta);
        } 
        else if (preguntaActual instanceof PreguntaArrastrar) {
            // Crear FlashCard Tipo C para preguntas de arrastrar
            PreguntaTipoC flashCard = new PreguntaTipoC(cursoEnMarcha, indBloque, indPregunta);
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
    private static JFrame crearFlashCardTipoB(CursoEnMarcha cursoEnMarcha, int indBloque, int indPregunta) {
        // Obtener la pregunta del tipo imagen
        PreguntaImagen pregunta = (PreguntaImagen) cursoEnMarcha.getPreguntaActual();
        
        // Crear el JFrame de tipo B
        PreguntaTipoB frame = new PreguntaTipoB(cursoEnMarcha, indBloque, indPregunta);
        
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
                // Intentar cargar la imagen
                ImageIcon imagen = null;
                
                if (imagenURL != null && !imagenURL.isEmpty()) {
                    try {
                        // Verificar si la URL es válida y cargarla directamente
                        URL url = new URL(imagenURL);
                        Image img = ImageIO.read(url);
                        
                        if (img != null) {
                            imagen = new ImageIcon(img);
                        }
                    } catch (Exception e) {
                        // Si no es una URL válida, intentar como recurso local
                        try {
                            imagen = new ImageIcon(frame.getClass().getResource(imagenURL));
                        } catch (Exception e2) {
                            System.out.println("Error cargando imagen como recurso: " + e2.getMessage());
                        }
                    }
                    
                    // Si sigue fallando, usar una imagen por defecto
                    if (imagen == null || imagen.getIconWidth() <= 0) {
                        try {
                            imagen = new ImageIcon(frame.getClass().getResource("/recursos/pregunta_img.png"));
                        } catch (Exception e3) {
                            // Si no hay recurso predeterminado, crear una imagen con texto
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
            // Acción para respuesta correcta
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