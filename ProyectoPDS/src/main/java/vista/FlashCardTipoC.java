package vista;

import javax.swing.*;
import javax.swing.border.*;

import controlador.ControladorPDS;
import modelado.CursoEnMarcha;
import modelado.PreguntaArrastrar;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FlashCardTipoC extends JFrame {

	private JProgressBar barraTiempo;
	private JLabel preguntaLabel;
	private JPanel zonaOpcionesPanel;
	private JPanel zonaRespuestaPanel;
	private JLabel contadorVidas;
	private int vidas = 3; // Valor predeterminado

	private List<String> opciones;
	private String textoCompleto;
	private String[] huecos;
	private List<DraggableLabel> etiquetasOpciones;
	private List<DropTargetPanel> zonasRespuesta;

	private Timer temporizador;
	private Color colorAzul = new Color(66, 133, 244);
	private Color colorVerde = new Color(76, 175, 80);
	private Color colorRojo = new Color(244, 67, 54);

	private Point draggedAt = null;
	private DraggableLabel labelArrastrado = null;

	// Referencia al curso en marcha
	private CursoEnMarcha cursoEnMarcha;

	public FlashCardTipoC(CursoEnMarcha curso, int indBloque, int indPregunta) {
		// Guardar referencia al curso
		this.cursoEnMarcha = curso;

		// Inicializar la lista de opciones antes de todo
		this.opciones = new ArrayList<>();

		// Obtener información desde la PreguntaArrastrar
		try {
			PreguntaArrastrar preguntaArrastrar = (PreguntaArrastrar) curso.getPreguntaActual();

			// Usar los datos de la pregunta
			this.textoCompleto = preguntaArrastrar.getTextoCompleto();

			// Convertir la lista a array
			List<String> huecosLista = preguntaArrastrar.getHuecos();
			this.huecos = huecosLista.toArray(new String[0]);

			// Crear opciones basadas en los huecos
			this.opciones.addAll(huecosLista);

			// Si no hay suficientes opciones, usar datos de prueba adicionales
			if (this.opciones.size() < 4) {
				this.opciones.add("Opción adicional 1");
				this.opciones.add("Opción adicional 2");
				this.opciones.add("Opción adicional 3");
			}

			// Mezclar las opciones
			Collections.shuffle(this.opciones);

		} catch (Exception e) {
		}


		vidas = curso.getVidas();

		setSize(new Dimension(700, 650));
		setLocationRelativeTo(null);
		setUndecorated(true);

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
		barraTiempo.setForeground(colorAzul);
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
		contadorVidas.setForeground(colorRojo);

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

		// Panel para la instrucción
		JPanel panelPregunta = new JPanel(new BorderLayout());
		panelPregunta.setOpaque(false);
		panelPregunta.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

		preguntaLabel = new JLabel("B"+curso.getBloqueActualIndex()+"."+curso.getPreguntaActualIndex()+"."+curso.getPreguntaActual().getEnunciado(), SwingConstants.CENTER);
		preguntaLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
		preguntaLabel.setForeground(new Color(60, 60, 60));

		panelPregunta.add(preguntaLabel, BorderLayout.CENTER);

		panelSuperior.add(panelControl, BorderLayout.NORTH);
		panelSuperior.add(panelPregunta, BorderLayout.CENTER);

		panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

		// Panel central con la frase y espacios para arrastrar
		JPanel panelCentral = new JPanel(new BorderLayout(0, 20));
		panelCentral.setOpaque(false);
		panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Panel para la zona de respuesta
		zonaRespuestaPanel = new JPanel();
		zonaRespuestaPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		zonaRespuestaPanel.setOpaque(false);

		// Crear el panel con los huecos
		crearPanelDeHuecos();

		JScrollPane scrollZonaRespuesta = new JScrollPane(zonaRespuestaPanel);
		scrollZonaRespuesta.setOpaque(false);
		scrollZonaRespuesta.getViewport().setOpaque(false);
		scrollZonaRespuesta.setBorder(BorderFactory.createEmptyBorder());

		// Panel inferior con las opciones para arrastrar
		zonaOpcionesPanel = new JPanel();
		zonaOpcionesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		zonaOpcionesPanel.setOpaque(false);
		zonaOpcionesPanel.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
				BorderFactory.createEmptyBorder(20, 10, 10, 10)));

		// Crear las etiquetas arrastrables
		crearEtiquetasArrastrables();

		JScrollPane scrollZonaOpciones = new JScrollPane(zonaOpcionesPanel);
		scrollZonaOpciones.setOpaque(false);
		scrollZonaOpciones.getViewport().setOpaque(false);
		scrollZonaOpciones.setBorder(BorderFactory.createEmptyBorder());

		panelCentral.add(scrollZonaRespuesta, BorderLayout.CENTER);

		panelPrincipal.add(panelCentral, BorderLayout.CENTER);
		panelPrincipal.add(scrollZonaOpciones, BorderLayout.SOUTH);

		// Panel de botón de verificar
		JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelBoton.setOpaque(false);

		JButton btnVerificar = new JButton("Verificar");
		btnVerificar.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnVerificar.setBackground(colorAzul);
		btnVerificar.setForeground(Color.WHITE);
		btnVerificar.setFocusPainted(false);
		btnVerificar.setBorderPainted(false);
		btnVerificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnVerificar.setPreferredSize(new Dimension(150, 40));

		// Efecto hover
		btnVerificar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				btnVerificar.setBackground(colorAzul.darker());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				btnVerificar.setBackground(colorAzul);
			}
		});

		btnVerificar.addActionListener(e -> verificarRespuestas());

		panelBoton.add(btnVerificar);
		panelCentral.add(panelBoton, BorderLayout.SOUTH);

		// Permitir arrastrar la ventana
		habilitarArrastreVentana();

		// Iniciar temporizador (30 segundos)
		iniciarTemporizador(45000, () -> {
			vidas--;
			actualizarContadorVidas();
			verificarRespuestas();
		});
	}

	private void crearPanelDeHuecos() {
		// Dividir el texto en partes según los huecos
		List<String> partes = new ArrayList<>();
		String textoRestante = textoCompleto;

		zonasRespuesta = new ArrayList<>();

		for (String hueco : huecos) {
			int pos = textoRestante.indexOf(hueco);
			if (pos >= 0) {
				if (pos > 0) {
					partes.add(textoRestante.substring(0, pos));
				}
				partes.add("[HUECO]"); // Marcador para el hueco
				textoRestante = textoRestante.substring(pos + hueco.length());
			}
		}

		if (!textoRestante.isEmpty()) {
			partes.add(textoRestante);
		}

		// Crear el panel que contiene el texto con huecos
		JPanel panelTextoHuecos = new JPanel();
		panelTextoHuecos.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
		panelTextoHuecos.setOpaque(false);

		for (String parte : partes) {
			if (parte.equals("[HUECO]")) {
				// Crear un panel para soltar la respuesta
				DropTargetPanel dropPanel = new DropTargetPanel();
				dropPanel.setPreferredSize(new Dimension(100, 40));
				dropPanel.setOpaque(true);
				dropPanel.setBackground(new Color(240, 240, 240));
				dropPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));

				zonasRespuesta.add(dropPanel);
				panelTextoHuecos.add(dropPanel);
			} else {
				// Añadir el texto normal
				JLabel textLabel = new JLabel(parte);
				textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
				textLabel.setForeground(new Color(60, 60, 60));
				panelTextoHuecos.add(textLabel);
			}
		}

		zonaRespuestaPanel.add(panelTextoHuecos);
	}

	private void crearEtiquetasArrastrables() {
		etiquetasOpciones = new ArrayList<>();

		for (String opcion : opciones) {
			DraggableLabel label = new DraggableLabel(opcion);
			label.setFont(new Font("Segoe UI", Font.BOLD, 14));
			label.setForeground(new Color(60, 60, 60));
			label.setBackground(new Color(255, 255, 255));
			label.setOpaque(true);
			label.setBorder(new CompoundBorder(new LineBorder(colorAzul, 1, true),
					BorderFactory.createEmptyBorder(8, 12, 8, 12)));

			// Configurar eventos de arrastre
			label.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (!label.isEnabled())
						return;

					labelArrastrado = label;
					draggedAt = e.getPoint();

					// Si estaba en una zona de respuesta, liberarla
					liberarZonaRespuesta(label);

					// Mover la etiqueta al frente
					zonaOpcionesPanel.setComponentZOrder(label, 0);
					zonaOpcionesPanel.repaint();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (labelArrastrado == null)
						return;

					// Verificar si está sobre una zona de respuesta
					Point screenPoint = e.getLocationOnScreen();
					boolean colocado = false;

					for (DropTargetPanel dropPanel : zonasRespuesta) {
						if (dropPanel.getOcupada())
							continue;

						Rectangle bounds = dropPanel.getBounds();
						Point panelScreen = dropPanel.getLocationOnScreen();
						bounds.setLocation(panelScreen);

						if (bounds.contains(screenPoint)) {
							// Colocar en la zona de respuesta
							dropPanel.setRespuesta(labelArrastrado);
							labelArrastrado.setEnabled(false);
							colocado = true;
							break;
						}
					}

					if (!colocado) {
						// Devolverlo a la zona de opciones
						label.setLocation(label.getOriginalX(), label.getOriginalY());
						label.setEnabled(true);
					}

					labelArrastrado = null;
					draggedAt = null;

					zonaOpcionesPanel.revalidate();
					zonaOpcionesPanel.repaint();
				}
			});

			label.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					if (labelArrastrado == null || draggedAt == null)
						return;

					Point currentPoint = e.getLocationOnScreen();
					Point panelScreen = zonaOpcionesPanel.getLocationOnScreen();

					int newX = currentPoint.x - panelScreen.x - draggedAt.x;
					int newY = currentPoint.y - panelScreen.y - draggedAt.y;

					labelArrastrado.setLocation(newX, newY);
					zonaOpcionesPanel.revalidate();
					zonaOpcionesPanel.repaint();
				}
			});

			zonaOpcionesPanel.add(label);
			etiquetasOpciones.add(label);
		}
	}

	private void liberarZonaRespuesta(DraggableLabel label) {
		for (DropTargetPanel dropPanel : zonasRespuesta) {
			if (dropPanel.getOcupada() && dropPanel.getRespuesta() == label) {
				dropPanel.limpiar();
				label.setEnabled(true);
				break;
			}
		}
	}

	private void verificarRespuestas() {
		detenerTemporizador();

		// Verificar si todas las zonas están ocupadas
		boolean todasOcupadas = true;
		for (DropTargetPanel dropPanel : zonasRespuesta) {
			if (!dropPanel.getOcupada()) {
				todasOcupadas = false;
				break;
			}
		}

		if (!todasOcupadas) {
			JOptionPane.showMessageDialog(this, "Por favor, completa todos los huecos antes de verificar.",
					"Respuesta incompleta", JOptionPane.WARNING_MESSAGE);

			// Reiniciar el temporizador
			iniciarTemporizador(30000, () -> {
				//vidas--;
				actualizarContadorVidas();
				verificarRespuestas();
			});

			return;
		}

		// Verificar si las respuestas son correctas
		boolean todasCorrectas = true;
		List<DropTargetPanel> incorrectas = new ArrayList<>();

		for (int i = 0; i < zonasRespuesta.size(); i++) {
			DropTargetPanel dropPanel = zonasRespuesta.get(i);
			DraggableLabel respuesta = dropPanel.getRespuesta();

			if (respuesta != null) {
				if (!respuesta.getText().equals(huecos[i])) {
					todasCorrectas = false;
					incorrectas.add(dropPanel);
				}
			} else {
				todasCorrectas = false;
			}
		}

		if (todasCorrectas) {
			// Mostrar animación de éxito
			mostrarAnimacionExito();

			// Actualizar vidas en el curso
			cursoEnMarcha.setVidas(vidas);

			// Respuesta correcta - ELIMINAMOS EL MENSAJE DE DIÁLOGO
			Timer timer = new Timer(800, e -> {
				// Avanzar a la siguiente pregunta sin mensaje interruptor
				NavegadorPreguntas.avanzarSiguientePregunta(cursoEnMarcha, this);
			});
			timer.setRepeats(false);
			timer.start();

		} else {
			// Restar vida
			if (!ControladorPDS.getUnicaInstancia().tieneVidasInfinitas()) {
				vidas--;
				actualizarContadorVidas();
			}

			// Mostrar animación de error
			mostrarAnimacionError(incorrectas);

			// Respuesta incorrecta
			if (ControladorPDS.getUnicaInstancia().tieneVidasInfinitas() || vidas > 0) {
				Timer timer = new Timer(1200, e -> {
					// Limpiar las zonas incorrectas y devolver las etiquetas a su posición original
					for (DropTargetPanel panel : incorrectas) {
						DraggableLabel label = panel.getRespuesta();
						panel.limpiar();
						if (label != null) {
							// Devolver la etiqueta a la zona de opciones
							label.setEnabled(true);
							zonaOpcionesPanel.add(label);
							// Restablecer posición original
							label.setLocation(label.getOriginalX(), label.getOriginalY());
							// Restablecer estilo original
							label.setForeground(new Color(60, 60, 60));
							label.setBackground(new Color(255, 255, 255));
							label.setBorder(new CompoundBorder(new LineBorder(colorAzul, 1, true),
									BorderFactory.createEmptyBorder(8, 12, 8, 12)));
						}
					}

					// Refrescar el panel de opciones
					zonaOpcionesPanel.revalidate();
					zonaOpcionesPanel.repaint();

					JOptionPane.showMessageDialog(this,
							"Hay " + incorrectas.size() + " respuestas incorrectas. ¡Inténtalo de nuevo!", "Incorrecto",
							JOptionPane.ERROR_MESSAGE);

					// Reiniciar el temporizador
					iniciarTemporizador(30000, () -> {
						vidas--;
						actualizarContadorVidas();
						verificarRespuestas();
					});
				});
				timer.setRepeats(false);
				timer.start();
			} else if (!ControladorPDS.getUnicaInstancia().tieneVidasInfinitas()) {
				JOptionPane.showMessageDialog(this,
						"Se te han acabado las vidas. La respuesta correcta era:\n" + textoCompleto,
						"Fin del ejercicio", JOptionPane.ERROR_MESSAGE);
				dispose();
				new VentanaPrincipal().setVisible(true);
			}
		}
	}

	private void mostrarAnimacionExito() {
		for (DropTargetPanel dropPanel : zonasRespuesta) {
			dropPanel.setBackground(colorVerde);
			if (dropPanel.getRespuesta() != null) {
				dropPanel.getRespuesta().setForeground(Color.WHITE);
				dropPanel.getRespuesta().setBorder(new CompoundBorder(new LineBorder(colorVerde.darker(), 1, true),
						BorderFactory.createEmptyBorder(8, 12, 8, 12)));
			}
		}
	}

	private void mostrarAnimacionError(List<DropTargetPanel> incorrectas) {
		// Marcar las incorrectas en rojo
		for (DropTargetPanel dropPanel : incorrectas) {
			dropPanel.setBackground(colorRojo);
			if (dropPanel.getRespuesta() != null) {
				dropPanel.getRespuesta().setForeground(Color.WHITE);
				dropPanel.getRespuesta().setBorder(new CompoundBorder(new LineBorder(colorRojo.darker(), 1, true),
						BorderFactory.createEmptyBorder(8, 12, 8, 12)));
			}
		}

		// Marcar las correctas en verde
		for (DropTargetPanel dropPanel : zonasRespuesta) {
			if (!incorrectas.contains(dropPanel)) {
				dropPanel.setBackground(colorVerde);
				if (dropPanel.getRespuesta() != null) {
					dropPanel.getRespuesta().setForeground(Color.WHITE);
					dropPanel.getRespuesta().setBorder(new CompoundBorder(new LineBorder(colorVerde.darker(), 1, true),
							BorderFactory.createEmptyBorder(8, 12, 8, 12)));
				}
			}
		}
	}

	private void habilitarArrastreVentana() {
		AtomicInteger posX = new AtomicInteger(0);
		AtomicInteger posY = new AtomicInteger(0);

		getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (labelArrastrado == null) {
					posX.set(e.getX());
					posY.set(e.getY());
				}
			}
		});

		getContentPane().addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (labelArrastrado == null) {
					setLocation(getLocation().x + e.getX() - posX.get(), getLocation().y + e.getY() - posY.get());
				}
			}
		});
	}

	private void actualizarContadorVidas() {
		contadorVidas.setText("❤ " + vidas);
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
					barraTiempo.setForeground(colorRojo); // Rojo cuando queda poco tiempo
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
		barraTiempo.setForeground(colorAzul); // Color inicial azul
		temporizador.start();
	}

	public void detenerTemporizador() {
		if (temporizador != null) {
			temporizador.stop();
		}
	}

	// Clases internas

	// Panel para soltar respuestas
	class DropTargetPanel extends JPanel {
		private DraggableLabel respuesta;

		public DropTargetPanel() {
			setLayout(new BorderLayout());
		}

		public boolean getOcupada() {
			return respuesta != null;
		}

		public DraggableLabel getRespuesta() {
			return respuesta;
		}

		public void setRespuesta(DraggableLabel respuesta) {
			limpiar();
			this.respuesta = respuesta;

			JLabel label = new JLabel(respuesta.getText(), SwingConstants.CENTER);
			label.setFont(respuesta.getFont());
			label.setForeground(respuesta.getForeground());

			add(label, BorderLayout.CENTER);
			revalidate();
			repaint();
		}

		public void limpiar() {
			respuesta = null;
			removeAll();
			revalidate();
			repaint();
		}
	}

	// Etiqueta arrastrable
	class DraggableLabel extends JLabel {
		private int originalX;
		private int originalY;

		public DraggableLabel(String text) {
			super(text, SwingConstants.CENTER);
			setHorizontalAlignment(SwingConstants.CENTER);
			setPreferredSize(new Dimension(100, 40));
		}

		@Override
		public void setBounds(int x, int y, int width, int height) {
			super.setBounds(x, y, width, height);
			if (originalX == 0 && originalY == 0) {
				originalX = x;
				originalY = y;
			}
		}

		public int getOriginalX() {
			return originalX;
		}

		public int getOriginalY() {
			return originalY;
		}
	}
}