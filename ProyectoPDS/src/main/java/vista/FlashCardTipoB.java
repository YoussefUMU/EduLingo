package vista;

import javax.swing.*;
import javax.swing.border.*;

import controlador.ControladorPDS;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FlashCardTipoB extends JPanel {

    private JProgressBar barraTiempo;
    private JLabel preguntaLabel;
    private JLabel contadorVidas;
    private int vidas = 3; // Valor predeterminado

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
    private Runnable accionCorrecta;
    private Runnable accionIncorrecta;

    public FlashCardTipoB() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setOpaque(false);

        // Panel superior con temporizador y título
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setOpaque(false);
        
        // Panel de control superior
        JPanel panelControl = new JPanel(new BorderLayout(5, 0));
        panelControl.setOpaque(false);
        
        // Barra de progreso con estilo mejorado
        barraTiempo = new JProgressBar(0, 100);
        barraTiempo.setValue(100);
        barraTiempo.setStringPainted(true);
        barraTiempo.setBackground(new Color(230, 240, 250));
        barraTiempo.setForeground(new Color(66, 133, 244));
        barraTiempo.setBorder(BorderFactory.createEmptyBorder());
        barraTiempo.setPreferredSize(new Dimension(100, 20));
        
        // Panel para vidas y cerrar
        JPanel panelMetricas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelMetricas.setOpaque(false);
        
        // Contador de vidas
        if(!ControladorPDS.getUnicaInstancia().tieneVidasInfinitas()) {
            contadorVidas = new JLabel("❤ " + vidas);
        } else {
            contadorVidas = new JLabel("♾️❤");
        }
        contadorVidas.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        contadorVidas.setForeground(new Color(244, 67, 54));
        
        panelMetricas.add(contadorVidas);
        
        panelControl.add(barraTiempo, BorderLayout.CENTER);
        panelControl.add(panelMetricas, BorderLayout.EAST);
        
        // Panel para la pregunta
        JPanel panelPregunta = new JPanel(new BorderLayout());
        panelPregunta.setOpaque(false);
        panelPregunta.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        preguntaLabel = new JLabel("Selecciona la imagen correcta", SwingConstants.CENTER);
        preguntaLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        preguntaLabel.setForeground(new Color(60, 60, 60));
        
        panelPregunta.add(preguntaLabel, BorderLayout.CENTER);
        
        panelSuperior.add(panelControl, BorderLayout.NORTH);
        panelSuperior.add(panelPregunta, BorderLayout.CENTER);
        
        add(panelSuperior, BorderLayout.NORTH);

        // Panel de opciones con imágenes
        JPanel panelOpciones = new JPanel(new GridLayout(1, 3, 15, 0));
        panelOpciones.setOpaque(false);
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // OPCIÓN 1
        opcion1Panel = crearPanelOpcion();
        opcion1Imagen = new JLabel("", SwingConstants.CENTER);
        opcion1Imagen.setPreferredSize(new Dimension(180, 120));
        opcion1Texto = new JLabel("Opción 1", SwingConstants.CENTER);
        opcion1Texto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        opcion1Panel.add(opcion1Imagen, BorderLayout.CENTER);
        opcion1Panel.add(opcion1Texto, BorderLayout.SOUTH);

        // OPCIÓN 2
        opcion2Panel = crearPanelOpcion();
        opcion2Imagen = new JLabel("", SwingConstants.CENTER);
        opcion2Imagen.setPreferredSize(new Dimension(180, 120));
        opcion2Texto = new JLabel("Opción 2", SwingConstants.CENTER);
        opcion2Texto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        opcion2Panel.add(opcion2Imagen, BorderLayout.CENTER);
        opcion2Panel.add(opcion2Texto, BorderLayout.SOUTH);

        // OPCIÓN 3
        opcion3Panel = crearPanelOpcion();
        opcion3Imagen = new JLabel("", SwingConstants.CENTER);
        opcion3Imagen.setPreferredSize(new Dimension(180, 120));
        opcion3Texto = new JLabel("Opción 3", SwingConstants.CENTER);
        opcion3Texto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        opcion3Panel.add(opcion3Imagen, BorderLayout.CENTER);
        opcion3Panel.add(opcion3Texto, BorderLayout.SOUTH);

        // Añadir panels al contenedor de opciones
        panelOpciones.add(opcion1Panel);
        panelOpciones.add(opcion2Panel);
        panelOpciones.add(opcion3Panel);

        add(panelOpciones, BorderLayout.CENTER);
    }
    
    private JPanel crearPanelOpcion() {
        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 210, 230), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Añadir efecto hover
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(240, 245, 255));
                panel.setBorder(new CompoundBorder(
                    new LineBorder(new Color(66, 133, 244), 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(255, 255, 255));
                panel.setBorder(new CompoundBorder(
                    new LineBorder(new Color(200, 210, 230), 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                panel.setBackground(new Color(230, 240, 250));
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                panel.setBackground(new Color(240, 245, 255));
            }
        });
        
        return panel;
    }
    
    public void setVidas(int vidas) {
        this.vidas = vidas;
        actualizarContadorVidas();
    }
    
    public void actualizarContadorVidas() {
        contadorVidas.setText("❤ " + vidas);
    }
    
    public void configurarAcciones(Runnable accionCorrecta, Runnable accionIncorrecta) {
        this.accionCorrecta = accionCorrecta;
        this.accionIncorrecta = accionIncorrecta;
    }
    
    public void configurarOpcion(int indice, boolean esCorrecta) {
        JPanel panel;
        switch (indice) {
            case 0: panel = opcion1Panel; break;
            case 1: panel = opcion2Panel; break;
            case 2: panel = opcion3Panel; break;
            default: return;
        }
        
        // Eliminar MouseListeners anteriores que manejen clicks
        for (MouseListener listener : panel.getMouseListeners()) {
            if (listener instanceof MouseAdapter) {
                panel.removeMouseListener(listener);
                panel.addMouseListener(listener); // Re-añadir para mantener efectos hover
            }
        }
        
        // Añadir nuevo listener para el click
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (esCorrecta) {
                    mostrarAnimacionExito(panel);
                    
                    // Ejecutar acción de respuesta correcta tras un delay
                    Timer timer = new Timer(1200, evt -> {
                        if (accionCorrecta != null) {
                            accionCorrecta.run();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else {
                	if (!ControladorPDS.getUnicaInstancia().tieneVidasInfinitas()) {
                	    vidas--;
                	    actualizarContadorVidas();
                	}
                    mostrarAnimacionError(panel);
                    
                    // Ejecutar acción de respuesta incorrecta
                    if (!ControladorPDS.getUnicaInstancia().tieneVidasInfinitas() && vidas <= 0 && accionIncorrecta != null) {
                        Timer timer = new Timer(1000, evt -> accionIncorrecta.run());
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            }
        });
    }
    
    private void mostrarAnimacionExito(JPanel panel) {
        // Detener temporizador
        detenerTemporizador();
        
        // Cambiar estilo del panel correcto
        panel.setBackground(new Color(76, 175, 80)); // Verde
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(56, 142, 60), 2, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Buscar el JLabel dentro del panel y cambiar su color
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setForeground(Color.WHITE);
            }
        }
        
        // Desactivar las otras opciones
        for (JPanel p : new JPanel[]{opcion1Panel, opcion2Panel, opcion3Panel}) {
            if (p != panel) {
                p.setEnabled(false);
                p.setCursor(Cursor.getDefaultCursor());
                p.setBackground(new Color(240, 240, 240));
            }
        }
    }
    
    private void mostrarAnimacionError(JPanel panel) {
        panel.setBackground(new Color(244, 67, 54)); // Rojo
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(211, 47, 47), 2, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
     // Buscar el JLabel dentro del panel y cambiar su color
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setForeground(Color.WHITE);
            }
        }
        
        // Timer para restaurar el color
        Timer timer = new Timer(1000, evt -> {
            if (vidas > 0) {
                panel.setBackground(new Color(255, 255, 255));
                panel.setBorder(new CompoundBorder(
                    new LineBorder(new Color(200, 210, 230), 1, true), 
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                
                // Restaurar color de texto
                for (Component comp : panel.getComponents()) {
                    if (comp instanceof JLabel) {
                        ((JLabel) comp).setForeground(new Color(60, 60, 60));
                    }
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    // Temporizador integrado
    public void iniciarTemporizador(int duracionMilisegundos, Runnable alFinal) {
        if (temporizador != null && temporizador.isRunning()) {
            temporizador.stop();
        }

        final int totalSteps = 100;
        final int intervalo = duracionMilisegundos / totalSteps;

        temporizador = new Timer(intervalo, new ActionListener() {
            int progreso = 100;

            @Override
            public void actionPerformed(ActionEvent e) {
                progreso--;
                barraTiempo.setValue(progreso);
                
                // Cambiar color según el tiempo restante
                if (progreso <= 25) {
                    barraTiempo.setForeground(new Color(244, 67, 54)); // Rojo cuando queda poco tiempo
                } else if (progreso <= 50) {
                    barraTiempo.setForeground(new Color(255, 152, 0)); // Naranja en tiempo medio
                }
                
                if (progreso <= 0) {
                    temporizador.stop();
                    if (alFinal != null) alFinal.run();
                }
            }
        });

        barraTiempo.setValue(100);
        barraTiempo.setForeground(new Color(66, 133, 244)); // Color inicial azul
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
    
    public void setImagenOpcion(int indice, ImageIcon imagen) {
        switch (indice) {
            case 0: 
                opcion1Imagen.setIcon(imagen);
                break;
            case 1: 
                opcion2Imagen.setIcon(imagen);
                break;
            case 2:
                opcion3Imagen.setIcon(imagen);
                break;
        }
    }
    
    public void setTextoOpcion(int indice, String texto) {
        switch (indice) {
            case 0: 
                opcion1Texto.setText(texto);
                break;
            case 1: 
                opcion2Texto.setText(texto);
                break;
            case 2:
                opcion3Texto.setText(texto);
                break;
        }
    }
    
    public JProgressBar getBarraTiempo() {
        return barraTiempo;
    }
    
    public int getVidas() {
        return vidas;
    }
}