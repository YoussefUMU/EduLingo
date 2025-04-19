package vista;

import javax.swing.*;
import javax.swing.border.*;

import controlador.ControladorPDS;
import modelado.CursoEnMarcha;
import modelado.TestPregunta;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FlashCardTipoA extends JFrame {

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
	private JLabel contadorVidas;
	private CursoEnMarcha cursoEnMarcha;

	public FlashCardTipoA(CursoEnMarcha curso, int indBloque, int indPregunta) {
		this.cursoEnMarcha = curso; // Añadir esta línea
		pregunta = (TestPregunta) curso.getPreguntaActual();
		vidas = curso.getVidas();
		pregunta = (TestPregunta) curso.getPreguntaActual();
		vidas = curso.getVidas();

		setSize(new Dimension(700, 650));
		setLocationRelativeTo(null);
		setUndecorated(true); // Eliminar bordes de ventana

		// Panel principal con gradiente
		JPanel panelPrincipal = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				int w = getWidth();
				int h = getHeight();
				Color color1 = new Color(240, 248, 255); // Azul muy claro en la parte superior
				Color color2 = new Color(230, 240, 250); // Azul más oscuro abajo
				GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
				g2d.setPaint(gp);
				g2d.fillRect(0, 0, w, h);
			}
		};
		panelPrincipal.setBorder(new CompoundBorder(new LineBorder(new Color(200, 200, 200), 1),
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		setContentPane(panelPrincipal);

		// Panel superior con temporizador y vidas
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
		if (!ControladorPDS.getUnicaInstancia().tieneVidasInfinitas()) {
			contadorVidas = new JLabel("❤ " + vidas);
		} else {
			contadorVidas = new JLabel("♾️❤");
		}
		contadorVidas.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
		contadorVidas.setForeground(new Color(244, 67, 54));

		// Botón cerrar
		JButton btnCerrar = new JButton("×");
		btnCerrar.setFont(new Font("Arial", Font.BOLD, 20));
		btnCerrar.setForeground(new Color(120, 120, 120));
		btnCerrar.setBorderPainted(false);
		btnCerrar.setContentAreaFilled(false);
		btnCerrar.setFocusPainted(false);
		btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCerrar.addActionListener(e -> {
			int respuesta = JOptionPane.showConfirmDialog(this,
					"¿Seguro que deseas salir? Perderás el progreso de esta pregunta.", "Confirmar salida",
					JOptionPane.YES_NO_OPTION);
			if (respuesta == JOptionPane.YES_OPTION) {
				dispose();
				new VentanaPrincipal().setVisible(true);
			}
		});

		panelMetricas.add(contadorVidas);
		panelMetricas.add(Box.createHorizontalStrut(15));
		panelMetricas.add(btnCerrar);

		panelControl.add(barraTiempo, BorderLayout.CENTER);
		panelControl.add(panelMetricas, BorderLayout.EAST);

		// Panel para la pregunta
		JPanel panelPregunta = new JPanel(new BorderLayout());
		panelPregunta.setOpaque(false);
		panelPregunta.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

		preguntaLabel = new JLabel("B"+curso.getBloqueActualIndex()+"."+curso.getPreguntaActualIndex()+"."+ pregunta.getEnunciado(), SwingConstants.CENTER);
		preguntaLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
		preguntaLabel.setForeground(new Color(60, 60, 60));

		panelPregunta.add(preguntaLabel, BorderLayout.CENTER);

		panelSuperior.add(panelControl, BorderLayout.NORTH);
		panelSuperior.add(panelPregunta, BorderLayout.CENTER);

		panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

		// Panel central con la imagen
		JPanel panelImagen = new JPanel(new GridBagLayout());
		panelImagen.setOpaque(false);

		imagenLabel = new JLabel("", SwingConstants.CENTER);
		imagenLabel.setPreferredSize(new Dimension(300, 180));

		// Intentar cargar la imagen
		try {
			ImageIcon imagenIcon = new ImageIcon(getClass().getResource("/recursos/pregunta_img.png"));
			if (imagenIcon.getIconWidth() != -1) {
				Image img = imagenIcon.getImage().getScaledInstance(300, 180, Image.SCALE_SMOOTH);
				imagenLabel.setIcon(new ImageIcon(img));
			}
		} catch (Exception e) {
			// Si no hay imagen, crear un panel decorativo
			imagenLabel.setBorder(new CompoundBorder(new LineBorder(new Color(200, 220, 240), 1),
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			imagenLabel.setText("Ejercicio: " + (indPregunta + 1) + " de "
					+ curso.getCurso().getBloques().get(indBloque).getPreguntas().size());
			imagenLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
			imagenLabel.setForeground(new Color(100, 130, 180));
		}

		panelImagen.add(imagenLabel);
		panelPrincipal.add(panelImagen, BorderLayout.CENTER);

		// Panel para las opciones
		JPanel panelOpciones = new JPanel(new GridLayout(2, 2, 15, 15));
		panelOpciones.setOpaque(false);
		panelOpciones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Crear botones con estilo mejorado
		opcion1Button = crearBotonOpcion(pregunta.getOpciones().get(0), 0);
		opcion2Button = crearBotonOpcion(pregunta.getOpciones().get(1), 1);
		opcion3Button = crearBotonOpcion(pregunta.getOpciones().get(2), 2);
		opcion4Button = crearBotonOpcion(pregunta.getOpciones().get(3), 3);

		panelOpciones.add(opcion1Button);
		panelOpciones.add(opcion2Button);
		panelOpciones.add(opcion3Button);
		panelOpciones.add(opcion4Button);

		panelPrincipal.add(panelOpciones, BorderLayout.SOUTH);

		// Permitir arrastrar la ventana
		habilitarArrastreVentana();

		// Iniciar temporizador (20 segundos)
		iniciarTemporizador(20000, () -> {
			JOptionPane.showMessageDialog(this, "¡Se acabó el tiempo!", "Tiempo agotado",
					JOptionPane.INFORMATION_MESSAGE);
			vidas--;
			actualizarContadorVidas();
			evaluar("");
		});
	}

	private JButton crearBotonOpcion(String texto, int indice) {
		JButton boton = new JButton(texto);
		boton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		boton.setBackground(new Color(255, 255, 255));
		boton.setForeground(new Color(60, 60, 60));
		boton.setBorder(new CompoundBorder(new LineBorder(new Color(200, 210, 230), 1, true),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		boton.setFocusPainted(false);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Efecto hover
		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				boton.setBackground(new Color(240, 245, 255));
				boton.setBorder(new CompoundBorder(new LineBorder(new Color(66, 133, 244), 1, true),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boton.setBackground(new Color(255, 255, 255));
				boton.setBorder(new CompoundBorder(new LineBorder(new Color(200, 210, 230), 1, true),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				boton.setBackground(new Color(230, 240, 250));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				boton.setBackground(new Color(240, 245, 255));
			}
		});

		boton.addActionListener(e -> evaluar(boton.getText()));

		return boton;
	}

	private void habilitarArrastreVentana() {
		AtomicInteger posX = new AtomicInteger(0);
		AtomicInteger posY = new AtomicInteger(0);

		getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				posX.set(e.getX());
				posY.set(e.getY());
			}
		});

		getContentPane().addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				setLocation(getLocation().x + e.getX() - posX.get(), getLocation().y + e.getY() - posY.get());
			}
		});
	}

	protected void evaluar(String opcion) {
	    if (!ControladorPDS.getUnicaInstancia().tieneVidasInfinitas() && vidas <= 0) {
	        JOptionPane.showMessageDialog(this, 
	            "Se te han acabado las vidas.", 
	            "Fin del ejercicio", 
	            JOptionPane.INFORMATION_MESSAGE);
	        dispose();
	        new VentanaPrincipal().setVisible(true);
	    }
	    else if (opcion.equals(pregunta.getRespuestaCorrecta())) {
	        // Detener temporizador
	        detenerTemporizador();
	        
	        // Mostrar animación de éxito
	        mostrarAnimacionExito();
	        
	        // Respuesta correcta - ELIMINAMOS EL MENSAJE DE DIÁLOGO
	        Timer timer = new Timer(800, e -> {
	            // Avanzar a la siguiente pregunta sin mostrar mensaje interruptor
	            NavegadorPreguntas.avanzarSiguientePregunta(cursoEnMarcha, this);
	        });
	        timer.setRepeats(false);
	        timer.start();
	    }
	    else if (!opcion.isEmpty()) {
	        // Restar vida
	        if (!ControladorPDS.getUnicaInstancia().tieneVidasInfinitas()) {
	            vidas--;
	            actualizarContadorVidas();
	        }
	        
	        // Mostrar animación de error
	        mostrarAnimacionError(opcion);
	        
	        // Respuesta incorrecta
	        if (vidas > 0) {
	            // Reducimos la interrupción - solo un breve mensaje o incluso sin mensaje
	            JOptionPane.showMessageDialog(this, 
	                "Respuesta incorrecta. ¡Inténtalo de nuevo!",
	                "Incorrecto", 
	                JOptionPane.ERROR_MESSAGE);
	        } else {
	            JOptionPane.showMessageDialog(this, 
	                "Respuesta incorrecta. Se te han acabado las vidas.",
	                "Fin del ejercicio", 
	                JOptionPane.ERROR_MESSAGE);
	            dispose();
	            new VentanaPrincipal().setVisible(true);
	        }
	    }
	}


	private void actualizarContadorVidas() {
		contadorVidas.setText("❤ " + vidas);
	}

	private void mostrarAnimacionExito() {
		for (JButton boton : new JButton[] { opcion1Button, opcion2Button, opcion3Button, opcion4Button }) {
			if (boton.getText().equals(pregunta.getRespuestaCorrecta())) {
				boton.setBackground(new Color(76, 175, 80));
				boton.setForeground(Color.WHITE);
				boton.setBorder(new CompoundBorder(new LineBorder(new Color(56, 142, 60), 2, true),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));
			} else {
				boton.setEnabled(false);
				boton.setBackground(new Color(240, 240, 240));
			}
		}
	}

	private void mostrarAnimacionError(String opcion) {
		for (JButton boton : new JButton[] { opcion1Button, opcion2Button, opcion3Button, opcion4Button }) {
			if (boton.getText().equals(opcion)) {
				boton.setBackground(new Color(244, 67, 54));
				boton.setForeground(Color.WHITE);
				boton.setBorder(new CompoundBorder(new LineBorder(new Color(211, 47, 47), 2, true),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));

				// Timer para restaurar el color
				Timer timer = new Timer(1000, e -> {
					boton.setBackground(new Color(255, 255, 255));
					boton.setForeground(new Color(60, 60, 60));
					boton.setBorder(new CompoundBorder(new LineBorder(new Color(200, 210, 230), 1, true),
							BorderFactory.createEmptyBorder(10, 10, 10, 10)));
				});
				timer.setRepeats(false);
				timer.start();
			}
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

				// Cambiar color según el tiempo restante
				if (progreso <= 25) {
					barraTiempo.setForeground(new Color(244, 67, 54)); // Rojo cuando queda poco tiempo
				} else if (progreso <= 50) {
					barraTiempo.setForeground(new Color(255, 152, 0)); // Naranja en tiempo medio
				}

				if (progreso <= 0) {
					temporizador.stop();
					if (alFinal != null)
						alFinal.run();
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