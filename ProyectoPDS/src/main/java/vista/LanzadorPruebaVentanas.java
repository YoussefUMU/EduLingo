package vista;

import javax.swing.*;
import java.awt.*;

public class LanzadorPruebaVentanas {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Flashcard Tipo B");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 400);
            frame.setLocationRelativeTo(null); // Centrar la ventana

            FlashcardTipoB flashcard = new FlashcardTipoB();
            frame.add(flashcard);

            // Configurar la pregunta
            flashcard.setPregunta("¿Cuál de estos animales es un mamífero?");

            // Cargar imágenes (usa tus propias rutas si lo deseas)
            flashcard.opcion1Imagen.setIcon(new ImageIcon("imagenes/pez.png"));
            flashcard.opcion1Texto.setText("Pez");

            flashcard.opcion2Imagen.setIcon(new ImageIcon("imagenes/perro.png"));
            flashcard.opcion2Texto.setText("Perro");

            flashcard.opcion3Imagen.setIcon(new ImageIcon("imagenes/pajaro.png"));
            flashcard.opcion3Texto.setText("Pájaro");

            // Iniciar temporizador con acción al finalizar
            flashcard.iniciarTemporizador(10000, () -> {
                JOptionPane.showMessageDialog(frame, "¡Tiempo agotado!");
            });

            frame.setVisible(true);
        });
    }
}
