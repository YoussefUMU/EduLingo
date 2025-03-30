package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlashcardTipoB extends JPanel {

    private JProgressBar barraTiempo;
    private JLabel preguntaLabel;

    public JPanel opcion1Panel;
    public JPanel opcion2Panel;
    public JPanel opcion3Panel;

    public JLabel opcion1Imagen;
    public JLabel opcion2Imagen;
    public JLabel opcion3Imagen;

    public JLabel opcion1Texto;
    public JLabel opcion2Texto;
    public JLabel opcion3Texto;

    private Timer temporizador;

    public FlashcardTipoB() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Temporizador y pregunta
        barraTiempo = new JProgressBar(0, 100);
        barraTiempo.setValue(100);
        barraTiempo.setStringPainted(true);
        barraTiempo.setPreferredSize(new Dimension(100, 25));

        preguntaLabel = new JLabel("Aquí va la pregunta", SwingConstants.CENTER);
        preguntaLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.add(barraTiempo, BorderLayout.NORTH);
        panelSuperior.add(preguntaLabel, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel de opciones
        JPanel panelOpciones = new JPanel(new GridLayout(1, 3, 15, 0));

        // OPCIÓN 1
        opcion1Panel = new JPanel(new BorderLayout(5, 5));
        opcion1Panel.setBackground(Color.WHITE);
        opcion1Panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        opcion1Panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        opcion1Imagen = new JLabel("", SwingConstants.CENTER);
        opcion1Imagen.setPreferredSize(new Dimension(150, 100));
        opcion1Texto = new JLabel("Opción 1", SwingConstants.CENTER);

        opcion1Panel.add(opcion1Imagen, BorderLayout.CENTER);
        opcion1Panel.add(opcion1Texto, BorderLayout.SOUTH);

        opcion1Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Clic en opción 1");
                // Aquí puedes manejar la lógica personalizada
            }
        });

        // OPCIÓN 2
        opcion2Panel = new JPanel(new BorderLayout(5, 5));
        opcion2Panel.setBackground(Color.WHITE);
        opcion2Panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        opcion2Panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        opcion2Imagen = new JLabel("", SwingConstants.CENTER);
        opcion2Imagen.setPreferredSize(new Dimension(150, 100));
        opcion2Texto = new JLabel("Opción 2", SwingConstants.CENTER);

        opcion2Panel.add(opcion2Imagen, BorderLayout.CENTER);
        opcion2Panel.add(opcion2Texto, BorderLayout.SOUTH);

        opcion2Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Clic en opción 2");
            }
        });

        // OPCIÓN 3
        opcion3Panel = new JPanel(new BorderLayout(5, 5));
        opcion3Panel.setBackground(Color.WHITE);
        opcion3Panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        opcion3Panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        opcion3Imagen = new JLabel("", SwingConstants.CENTER);
        opcion3Imagen.setPreferredSize(new Dimension(150, 100));
        opcion3Texto = new JLabel("Opción 3", SwingConstants.CENTER);

        opcion3Panel.add(opcion3Imagen, BorderLayout.CENTER);
        opcion3Panel.add(opcion3Texto, BorderLayout.SOUTH);

        opcion3Panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Clic en opción 3");
            }
        });

        // Agregar al contenedor de opciones
        panelOpciones.add(opcion1Panel);
        panelOpciones.add(opcion2Panel);
        panelOpciones.add(opcion3Panel);

        add(panelOpciones, BorderLayout.CENTER);
    }

    // Temporizador
    public void iniciarTemporizador(int duracionMilisegundos, Runnable alFinal) {
        if (temporizador != null && temporizador.isRunning()) {
            temporizador.stop();
        }

        final int totalSteps = 100;
        final int intervalo = duracionMilisegundos / totalSteps;

        temporizador = new Timer(intervalo, new AbstractAction() {
            int progreso = 100;

            @Override
            public void actionPerformed(ActionEvent e) {
                progreso--;
                barraTiempo.setValue(progreso);
                if (progreso <= 0) {
                    temporizador.stop();
                    if (alFinal != null) alFinal.run();
                }
            }
        });

        barraTiempo.setValue(100);
        temporizador.start();
    }

    public void detenerTemporizador() {
        if (temporizador != null) {
            temporizador.stop();
        }
    }

    public void setPregunta(String texto) {
        preguntaLabel.setText(texto);
    }

    public JProgressBar getBarraTiempo() {
        return barraTiempo;
    }
}
