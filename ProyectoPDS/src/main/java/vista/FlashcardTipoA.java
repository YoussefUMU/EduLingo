package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FlashcardTipoA extends JPanel {

    private JProgressBar barraTiempo;
    private JLabel preguntaLabel;
    private JLabel imagenLabel;
    private JButton opcion1Button;
    private JButton opcion2Button;
    private JButton opcion3Button;
    private JButton opcion4Button;

    private Timer temporizador;

    public FlashcardTipoA() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Barra de tiempo y pregunta en la parte superior
        barraTiempo = new JProgressBar(0, 100);
        barraTiempo.setValue(100);
        barraTiempo.setStringPainted(true);
        barraTiempo.setPreferredSize(new Dimension(100, 25));

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.add(barraTiempo, BorderLayout.NORTH);

        preguntaLabel = new JLabel("Aquí va la pregunta", SwingConstants.CENTER);
        preguntaLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(preguntaLabel, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // Imagen centrada
        imagenLabel = new JLabel("", SwingConstants.CENTER);
        imagenLabel.setPreferredSize(new Dimension(250, 150));
        imagenLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel imagenPanel = new JPanel(new GridBagLayout());
        imagenPanel.add(imagenLabel);
        add(imagenPanel, BorderLayout.CENTER);

        // Botones de opciones
        opcion1Button = new JButton("Opción 1");
        opcion2Button = new JButton("Opción 2");
        opcion3Button = new JButton("Opción 3");
        opcion4Button = new JButton("Opción 4");

        Dimension botonGrande = new Dimension(200, 45);
        opcion1Button.setPreferredSize(botonGrande);
        opcion2Button.setPreferredSize(botonGrande);
        opcion3Button.setPreferredSize(botonGrande);
        opcion4Button.setPreferredSize(botonGrande);

        JPanel botonesPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        botonesPanel.add(opcion1Button);
        botonesPanel.add(opcion2Button);
        botonesPanel.add(opcion3Button);
        botonesPanel.add(opcion4Button);
        botonesPanel.setPreferredSize(new Dimension(450, 120));

        JPanel botonesWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botonesWrapper.add(botonesPanel);

        add(botonesWrapper, BorderLayout.SOUTH);
    }

    // Temporizador integrado
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

    // Getters y setters
    public void setPregunta(String texto) {
        preguntaLabel.setText(texto);
    }

    public void setImagen(ImageIcon imagen) {
        imagenLabel.setIcon(imagen);
    }

    public JButton getOpcion1Button() {
        return opcion1Button;
    }

    public JButton getOpcion2Button() {
        return opcion2Button;
    }

    public JButton getOpcion3Button() {
        return opcion3Button;
    }

    public JButton getOpcion4Button() {
        return opcion4Button;
    }

    public JLabel getPreguntaLabel() {
        return preguntaLabel;
    }

    public JLabel getImagenLabel() {
        return imagenLabel;
    }

    public JProgressBar getBarraTiempo() {
        return barraTiempo;
    }
}
