package vista;

import javax.swing.*;

import modelado.CursoEnMarcha;
import modelado.TestPregunta;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FlashcardTipoA extends JFrame {

    private JProgressBar barraTiempo;
    private JLabel preguntaLabel;
    private JLabel imagenLabel;
    private JButton opcion1Button;
    private JButton opcion2Button;
    private JButton opcion3Button;
    private JButton opcion4Button;
    
    private TestPregunta pregunta;
    private int vidas;
    
    private Timer temporizador;

    public FlashcardTipoA(CursoEnMarcha curso, int indBloque, int indPregunta) {
    	//curso.getBloqueActual();
    	pregunta = (TestPregunta) curso.getCurso().getBloques().get(indBloque).getPreguntas().get(indPregunta);
    	vidas = curso.getVidas();
    	
    	setSize(new Dimension(600, 600));
        getContentPane().setLayout(new BorderLayout(10, 10));
        //setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
       
        // Barra de tiempo y pregunta en la parte superior
        barraTiempo = new JProgressBar(0, 100);
        barraTiempo.setValue(100);
        barraTiempo.setStringPainted(true);
        barraTiempo.setPreferredSize(new Dimension(100, 25));

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.add(barraTiempo, BorderLayout.NORTH);

        preguntaLabel = new JLabel(pregunta.getEnunciado(), SwingConstants.CENTER);
        preguntaLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panelSuperior.add(preguntaLabel, BorderLayout.SOUTH);

        getContentPane().add(panelSuperior, BorderLayout.NORTH);

        // Imagen centrada
        imagenLabel = new JLabel("", SwingConstants.CENTER);
        imagenLabel.setPreferredSize(new Dimension(250, 150));
        imagenLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel imagenPanel = new JPanel(new GridBagLayout());
        imagenPanel.add(imagenLabel);
        getContentPane().add(imagenPanel, BorderLayout.CENTER);

        // Botones de opciones
        opcion1Button = new JButton(pregunta.getOpciones().get(0));
        opcion1Button.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		evaluar(opcion1Button.getText());
        	}
        });
        opcion2Button = new JButton(pregunta.getOpciones().get(1));
        opcion2Button.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		evaluar(opcion2Button.getText());
        	}
        });
        opcion3Button = new JButton(pregunta.getOpciones().get(2));
        opcion3Button.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		evaluar(opcion3Button.getText());
        	}
        });
        opcion4Button = new JButton(pregunta.getOpciones().get(3));
        opcion4Button.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		evaluar(opcion4Button.getText());
        	}
        });

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

        getContentPane().add(botonesWrapper, BorderLayout.SOUTH);
    }

    protected void evaluar(String opcion) {
    	if (vidas<=0) {
    		JOptionPane.showMessageDialog(null, "Se te han acabado las vidas.");
    		VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
    		ventanaPrincipal.setVisible(true);
    		dispose();
    	}
    	else if (opcion.equals(pregunta.getRespuestaCorrecta())){
    		JOptionPane.showMessageDialog(null, "Bien Hecho!");
    		VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
    		ventanaPrincipal.setVisible(true);
    		dispose();
    	}
    	else {
    		JOptionPane.showMessageDialog(null, "Inténtalo de nuevo!");
    		vidas--;
    	}
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
