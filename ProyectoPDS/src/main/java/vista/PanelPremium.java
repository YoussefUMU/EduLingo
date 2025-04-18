package vista;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.*;

import controlador.ControladorPDS;
import modelado.Premium;
import modelado.Usuario;

public class PanelPremium extends JPanel {
	private JLabel estadoSuscripcionLabel;
	private JLabel fechaVencimientoLabel;
	private JRadioButton radioMensual;
	private JRadioButton radioAnual;
	private JButton btnActivar;
	private JButton btnCancelar;
	private JPanel panelPremiumPromo;

	// Colores para la interfaz
	private Color colorOro = new Color(255, 215, 0);
	private Color colorAzul = new Color(66, 133, 244);
	private Color colorVerde = new Color(76, 175, 80);
	private Color colorRojo = new Color(244, 67, 54);
	private Color colorFondoPanel = new Color(245, 248, 255);

	public PanelPremium() {
		setLayout(new BorderLayout(10, 10));
		setOpaque(true);
		setBackground(colorFondoPanel);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Panel superior con título y estado
		JPanel panelSuperior = crearPanelSuperior();
		add(panelSuperior, BorderLayout.NORTH);

		// Panel central dividido en dos partes
		JPanel panelCentral = new JPanel(new BorderLayout(20, 20));
		panelCentral.setOpaque(false);

		// Panel izquierdo: Descripción + Panel de botón Activar
		JPanel panelIzquierdo = new JPanel(new BorderLayout(0, 20));
		panelIzquierdo.setOpaque(false);
		panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

		// Texto descriptivo
		JPanel panelDescripcion = crearPanelDescripcion();
		panelIzquierdo.add(panelDescripcion, BorderLayout.CENTER);

		// Botón de activar
		JPanel panelBoton = crearPanelBoton();
		panelIzquierdo.add(panelBoton, BorderLayout.SOUTH);

		// Panel derecho: Planes y Beneficios
		JPanel panelDerecho = new JPanel(new BorderLayout(0, 15));
		panelDerecho.setOpaque(false);

		// Panel de planes disponibles
		JPanel panelPlanes = crearPanelPlanes();

		// Panel de beneficios
		JPanel panelBeneficios = crearPanelBeneficios();

		// Añadir ambos paneles en un panel vertical
		JPanel panelDerechoContenido = new JPanel();
		panelDerechoContenido.setLayout(new BoxLayout(panelDerechoContenido, BoxLayout.Y_AXIS));
		panelDerechoContenido.setOpaque(false);
		panelDerechoContenido.add(panelPlanes);
		panelDerechoContenido.add(Box.createVerticalStrut(15));
		panelDerechoContenido.add(panelBeneficios);

		panelDerecho.add(panelDerechoContenido, BorderLayout.CENTER);

		// Añadir paneles al panel central
		panelCentral.add(panelIzquierdo, BorderLayout.WEST);
		panelCentral.add(panelDerecho, BorderLayout.CENTER);

		add(panelCentral, BorderLayout.CENTER);

		// Actualizar la información de suscripción
		actualizarInfoSuscripcion();
	}

	private JPanel crearPanelSuperior() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setOpaque(false);

		// Título con icono
		JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelTitulo.setOpaque(false);

		JLabel iconoLabel = new JLabel("👑");
		iconoLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
		iconoLabel.setForeground(colorOro);

		JLabel tituloLabel = new JLabel("Edulingo Premium");
		tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		tituloLabel.setForeground(colorAzul);

		panelTitulo.add(iconoLabel);
		panelTitulo.add(tituloLabel);

		// Panel para estado de suscripción
		JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelEstado.setOpaque(false);

		estadoSuscripcionLabel = new JLabel("Estado: No tienes suscripción premium");
		estadoSuscripcionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

		fechaVencimientoLabel = new JLabel("");
		fechaVencimientoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		fechaVencimientoLabel.setForeground(new Color(100, 100, 100));

		panelEstado.add(new JLabel("Estado: "));
		panelEstado.add(estadoSuscripcionLabel);

		panel.add(panelTitulo, BorderLayout.WEST);
		panel.add(panelEstado, BorderLayout.EAST);

		// Separador con estilo
		JSeparator separador = new JSeparator();
		separador.setForeground(new Color(220, 220, 230));

		JPanel panelCompleto = new JPanel(new BorderLayout());
		panelCompleto.setOpaque(false);
		panelCompleto.add(panel, BorderLayout.CENTER);
		panelCompleto.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
		panelCompleto.add(separador, BorderLayout.SOUTH);

		return panelCompleto;
	}

	private JPanel crearPanelDescripcion() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		JLabel lblTitulo = new JLabel("¡Potencia tu experiencia con Edulingo Premium!");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

		JTextPane textoDesc = new JTextPane();
		textoDesc.setContentType("text/html");
		textoDesc.setText("<html><body style='width: 300px; font-family: Segoe UI; font-size: 14px;'>"
				+ "<p>Con Premium disfrutarás de una experiencia de aprendizaje sin límites, pudiendo practicar "
				+ "todas las veces que necesites y accediendo a contenido exclusivo.</p>"
				+ "<p>Elige el plan que mejor se adapte a ti y comienza a disfrutar de todas las ventajas.</p>"
				+ "</body></html>");
		textoDesc.setEditable(false);
		textoDesc.setOpaque(false);
		textoDesc.setBorder(null);
		textoDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.add(lblTitulo);
		panel.add(Box.createVerticalStrut(10));
		panel.add(textoDesc);

		return panel;
	}

	private JPanel crearPanelPlanes() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 210, 230), 1, true),
						"Planes Disponibles", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
						new Font("Segoe UI", Font.BOLD, 14)));
		panel.setBackground(new Color(248, 250, 255));
		panel.setPreferredSize(new Dimension(400, 120));

		ButtonGroup grupoPlanes = new ButtonGroup();

		// Panel para opciones
		JPanel opcionesPanel = new JPanel(new GridLayout(2, 1, 0, 10));
		opcionesPanel.setOpaque(false);
		opcionesPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		// Plan mensual
		JPanel mensualPanel = new JPanel(new BorderLayout(10, 0));
		mensualPanel.setOpaque(false);

		radioMensual = new JRadioButton("Plan Mensual");
		radioMensual.setFont(new Font("Segoe UI", Font.BOLD, 14));
		radioMensual.setOpaque(false);
		radioMensual.setSelected(true);

		JLabel precioMensual = new JLabel("9,99€/mes");
		precioMensual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		precioMensual.setForeground(new Color(100, 100, 100));

		JPanel mensualInfoPanel = new JPanel();
		mensualInfoPanel.setLayout(new BoxLayout(mensualInfoPanel, BoxLayout.Y_AXIS));
		mensualInfoPanel.setOpaque(false);
		mensualInfoPanel.add(radioMensual);
		mensualInfoPanel.add(precioMensual);

		JLabel iconoMensual = new JLabel("📅");
		iconoMensual.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

		mensualPanel.add(iconoMensual, BorderLayout.WEST);
		mensualPanel.add(mensualInfoPanel, BorderLayout.CENTER);

		// Plan anual
		JPanel anualPanel = new JPanel(new BorderLayout(10, 0));
		anualPanel.setOpaque(false);

		radioAnual = new JRadioButton("Plan Anual");
		radioAnual.setFont(new Font("Segoe UI", Font.BOLD, 14));
		radioAnual.setOpaque(false);

		JLabel precioAnual = new JLabel("89,99€/año (25% de ahorro)");
		precioAnual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		precioAnual.setForeground(new Color(100, 100, 100));

		JPanel anualInfoPanel = new JPanel();
		anualInfoPanel.setLayout(new BoxLayout(anualInfoPanel, BoxLayout.Y_AXIS));
		anualInfoPanel.setOpaque(false);
		anualInfoPanel.add(radioAnual);
		anualInfoPanel.add(precioAnual);

		JLabel iconoAnual = new JLabel("🗓️");
		iconoAnual.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

		JLabel mejorValor = new JLabel("MEJOR VALOR");
		mejorValor.setFont(new Font("Segoe UI", Font.BOLD, 10));
		mejorValor.setForeground(Color.WHITE);
		mejorValor.setBackground(colorOro);
		mejorValor.setOpaque(true);
		mejorValor.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));

		JPanel mejorValorPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		mejorValorPanel.setOpaque(false);
		mejorValorPanel.add(mejorValor);

		anualPanel.add(iconoAnual, BorderLayout.WEST);
		anualPanel.add(anualInfoPanel, BorderLayout.CENTER);
		anualPanel.add(mejorValorPanel, BorderLayout.EAST);

		grupoPlanes.add(radioMensual);
		grupoPlanes.add(radioAnual);

		opcionesPanel.add(mensualPanel);
		opcionesPanel.add(anualPanel);

		panel.add(opcionesPanel, BorderLayout.CENTER);

		return panel;
	}

	private JPanel crearPanelBeneficios() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 210, 230), 1, true),
						"Beneficios Premium", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
						new Font("Segoe UI", Font.BOLD, 14)));
		panel.setBackground(new Color(248, 250, 255));
		panel.setPreferredSize(new Dimension(400, 200));

		// Panel para beneficios
		JPanel contenidoPanel = new JPanel();
		contenidoPanel.setLayout(new BoxLayout(contenidoPanel, BoxLayout.Y_AXIS));
		contenidoPanel.setOpaque(false);
		contenidoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

		// Vidas infinitas
		JPanel vidasPanel = new JPanel(new BorderLayout(10, 0));
		vidasPanel.setOpaque(false);
		vidasPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		JLabel iconoVidas = new JLabel("❤️");
		iconoVidas.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

		JPanel textoVidasPanel = new JPanel();
		textoVidasPanel.setLayout(new BoxLayout(textoVidasPanel, BoxLayout.Y_AXIS));
		textoVidasPanel.setOpaque(false);

		JLabel tituloVidas = new JLabel("Vidas Infinitas");
		tituloVidas.setFont(new Font("Segoe UI", Font.BOLD, 14));
		tituloVidas.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel descVidas = new JLabel("Nunca más te quedarás sin vidas. Practica tanto como necesites.");
		descVidas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		descVidas.setForeground(new Color(80, 80, 80));
		descVidas.setAlignmentX(Component.LEFT_ALIGNMENT);

		textoVidasPanel.add(tituloVidas);
		textoVidasPanel.add(descVidas);

		vidasPanel.add(iconoVidas, BorderLayout.WEST);
		vidasPanel.add(textoVidasPanel, BorderLayout.CENTER);

		// Cursos premium
		JPanel cursosPanel = new JPanel(new BorderLayout(10, 0));
		cursosPanel.setOpaque(false);
		cursosPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		JLabel iconoCursos = new JLabel("📚");
		iconoCursos.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

		JPanel textoCursosPanel = new JPanel();
		textoCursosPanel.setLayout(new BoxLayout(textoCursosPanel, BoxLayout.Y_AXIS));
		textoCursosPanel.setOpaque(false);

		JLabel tituloCursos = new JLabel("Cursos Premium");
		tituloCursos.setFont(new Font("Segoe UI", Font.BOLD, 14));
		tituloCursos.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel descCursos = new JLabel("Accede a cursos exclusivos y contenido avanzado.");
		descCursos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		descCursos.setForeground(new Color(80, 80, 80));
		descCursos.setAlignmentX(Component.LEFT_ALIGNMENT);

		textoCursosPanel.add(tituloCursos);
		textoCursosPanel.add(descCursos);

		cursosPanel.add(iconoCursos, BorderLayout.WEST);
		cursosPanel.add(textoCursosPanel, BorderLayout.CENTER);

		// Soporte prioritario
		JPanel soportePanel = new JPanel(new BorderLayout(10, 0));
		soportePanel.setOpaque(false);

		JLabel iconoSoporte = new JLabel("⭐");
		iconoSoporte.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

		JPanel textoSoportePanel = new JPanel();
		textoSoportePanel.setLayout(new BoxLayout(textoSoportePanel, BoxLayout.Y_AXIS));
		textoSoportePanel.setOpaque(false);

		JLabel tituloSoporte = new JLabel("Soporte Prioritario");
		tituloSoporte.setFont(new Font("Segoe UI", Font.BOLD, 14));
		tituloSoporte.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel descSoporte = new JLabel("Atención preferente a tus consultas y sugerencias.");
		descSoporte.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		descSoporte.setForeground(new Color(80, 80, 80));
		descSoporte.setAlignmentX(Component.LEFT_ALIGNMENT);

		textoSoportePanel.add(tituloSoporte);
		textoSoportePanel.add(descSoporte);

		soportePanel.add(iconoSoporte, BorderLayout.WEST);
		soportePanel.add(textoSoportePanel, BorderLayout.CENTER);

		contenidoPanel.add(vidasPanel);
		contenidoPanel.add(cursosPanel);
		contenidoPanel.add(soportePanel);

		panel.add(contenidoPanel, BorderLayout.CENTER);

		return panel;
	}

	private JPanel crearPanelBoton() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		btnActivar = crearBotonEstilizado("Activar Premium", colorVerde, Color.WHITE);
		btnActivar.setPreferredSize(new Dimension(200, 45));

		btnCancelar = crearBotonEstilizado("Cancelar Suscripción", colorRojo, Color.WHITE);
		btnCancelar.setPreferredSize(new Dimension(200, 45));
		btnCancelar.setVisible(false); // Inicialmente oculto

		// Acciones para los botones
		btnActivar.addActionListener(e -> activarPremium());
		btnCancelar.addActionListener(e -> cancelarPremium());

		JPanel panelBotones = new JPanel(new GridLayout(2, 1, 0, 10));
		panelBotones.setOpaque(false);

		panelBotones.add(btnActivar);
		panelBotones.add(btnCancelar);

		panel.add(panelBotones);

		return panel;
	}

	private JButton crearBotonEstilizado(String texto, Color colorFondo, Color colorTexto) {
		JButton boton = new JButton(texto) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				if (getModel().isPressed()) {
					g2.setColor(colorFondo.darker());
				} else if (getModel().isRollover()) {
					g2.setColor(colorFondo.brighter());
				} else {
					g2.setColor(colorFondo);
				}

				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

				g2.setColor(colorTexto);
				g2.setFont(getFont());
				FontMetrics fm = g2.getFontMetrics();
				int stringWidth = fm.stringWidth(getText());
				int stringHeight = fm.getHeight();
				int x = (getWidth() - stringWidth) / 2;
				int y = (getHeight() - stringHeight) / 2 + fm.getAscent();
				g2.drawString(getText(), x, y);
			}
		};

		boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		boton.setForeground(colorTexto);
		boton.setBorderPainted(false);
		boton.setContentAreaFilled(false);
		boton.setFocusPainted(false);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		return boton;
	}

	public void actualizarInfoSuscripcion() {
		Usuario usuario = ControladorPDS.getUnicaInstancia().getSesionActual();

		if (usuario != null && usuario.esPremium()) {
			Premium premium = usuario.getPremium();

			// Siempre mostramos como activo si es premium, independientemente de si está
			// cancelado
			estadoSuscripcionLabel.setText("Premium activo (" + premium.getTipoPlan() + ")");
			estadoSuscripcionLabel.setForeground(colorVerde);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			String fechaFin = premium.getFechaFin().format(formatter);

			// Si está cancelado, indicamos que no se renovará
			if (!premium.isActivo()) {
				fechaVencimientoLabel.setText("Finaliza el: " + fechaFin + " (no se renovará)");
				fechaVencimientoLabel.setForeground(new Color(200, 100, 100));
				btnCancelar.setVisible(false);
			} else {
				fechaVencimientoLabel.setText("Vence el: " + fechaFin);
				fechaVencimientoLabel.setForeground(new Color(100, 100, 100));
				btnCancelar.setVisible(true);
			}

			btnActivar.setText("Renovar Premium");

			// Seleccionar el plan actual
			if ("anual".equalsIgnoreCase(premium.getTipoPlan())) {
				radioAnual.setSelected(true);
			} else {
				radioMensual.setSelected(true);
			}
		} else {
			estadoSuscripcionLabel.setText("No tienes suscripción premium");
			estadoSuscripcionLabel.setForeground(new Color(100, 100, 100));
			fechaVencimientoLabel.setText("");
			btnActivar.setText("Activar Premium");
			btnCancelar.setVisible(false);
		}
	}

	private void activarPremium() {
		String tipoPlan = radioAnual.isSelected() ? "anual" : "mensual";

		// Crear panel de confirmación
		JPanel panelConfirmacion = new JPanel(new BorderLayout(20, 10));
		panelConfirmacion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel iconoConfirmacion = new JLabel(radioAnual.isSelected() ? "🗓️" : "📅");
		iconoConfirmacion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
		iconoConfirmacion.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel panelTextoConfirmacion = new JPanel();
		panelTextoConfirmacion.setLayout(new BoxLayout(panelTextoConfirmacion, BoxLayout.Y_AXIS));

		JLabel lblTituloConfirmacion = new JLabel("Confirmar suscripción Premium");
		lblTituloConfirmacion.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTituloConfirmacion.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblPlanSeleccionado = new JLabel(
				"Plan seleccionado: " + (radioAnual.isSelected() ? "Anual" : "Mensual"));
		lblPlanSeleccionado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblPlanSeleccionado.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblPrecio = new JLabel("Precio: " + (radioAnual.isSelected() ? "89,99€/año" : "9,99€/mes"));
		lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Solo mostrar ahorro en plan anual
		if (radioAnual.isSelected()) {
			JLabel lblAhorro = new JLabel("Ahorras: 29,89€ (25%)");
			lblAhorro.setFont(new Font("Segoe UI", Font.BOLD, 14));
			lblAhorro.setForeground(colorVerde);
			lblAhorro.setAlignmentX(Component.LEFT_ALIGNMENT);

			panelTextoConfirmacion.add(lblTituloConfirmacion);
			panelTextoConfirmacion.add(Box.createVerticalStrut(10));
			panelTextoConfirmacion.add(lblPlanSeleccionado);
			panelTextoConfirmacion.add(Box.createVerticalStrut(5));
			panelTextoConfirmacion.add(lblPrecio);
			panelTextoConfirmacion.add(Box.createVerticalStrut(5));
			panelTextoConfirmacion.add(lblAhorro);
		} else {
			panelTextoConfirmacion.add(lblTituloConfirmacion);
			panelTextoConfirmacion.add(Box.createVerticalStrut(10));
			panelTextoConfirmacion.add(lblPlanSeleccionado);
			panelTextoConfirmacion.add(Box.createVerticalStrut(5));
			panelTextoConfirmacion.add(lblPrecio);
		}

		panelConfirmacion.add(iconoConfirmacion, BorderLayout.WEST);
		panelConfirmacion.add(panelTextoConfirmacion, BorderLayout.CENTER);

		int confirmacion = JOptionPane.showConfirmDialog(this, panelConfirmacion, "Confirmar Pago",
				JOptionPane.YES_NO_OPTION);

		if (confirmacion == JOptionPane.YES_OPTION) {
			// Simulamos la pantalla de procesamiento de pago con un diseño mejorado
			JDialog dialogoProcesando = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
					"Procesando pago...", true);
			dialogoProcesando.setSize(350, 150);
			dialogoProcesando.setLocationRelativeTo(this);
			dialogoProcesando.setLayout(new BorderLayout());

			JPanel panelProcesando = new JPanel(new BorderLayout(20, 10));
			panelProcesando.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

			JLabel iconoProcesando = new JLabel("💳");
			iconoProcesando.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));

			JLabel msgProcesando = new JLabel("Procesando su pago, por favor espere...");
			msgProcesando.setFont(new Font("Segoe UI", Font.PLAIN, 14));

			// Agregar un indicador de progreso
			JProgressBar progressBar = new JProgressBar();
			progressBar.setIndeterminate(true);
			progressBar.setPreferredSize(new Dimension(200, 5));

			panelProcesando.add(iconoProcesando, BorderLayout.WEST);
			panelProcesando.add(msgProcesando, BorderLayout.CENTER);
			panelProcesando.add(progressBar, BorderLayout.SOUTH);

			dialogoProcesando.add(panelProcesando);
			dialogoProcesando.setUndecorated(true);

			// Mostrar diálogo en otro hilo para que aparezca la animación
			new Thread(() -> {
				dialogoProcesando.setVisible(true);
			}).start();

			// Simular tiempo de procesamiento
			new Thread(() -> {
				try {
					Thread.sleep(2000); // Simular 2 segundos de procesamiento

					// Activar premium
					boolean resultado = ControladorPDS.getUnicaInstancia().activarPremium(tipoPlan);

					// Cerrar diálogo
					SwingUtilities.invokeLater(() -> {
						dialogoProcesando.dispose();

						if (resultado) {
							// Panel de éxito con diseño mejorado
							JPanel panelExito = new JPanel(new BorderLayout(20, 10));
							panelExito.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

							JLabel iconoExito = new JLabel("✅");
							iconoExito.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));

							JLabel msgExito = new JLabel("<html><body>" + "<h3>¡Felicidades!</h3>"
									+ "<p>Tu suscripción premium ha sido activada correctamente.</p>"
									+ "<p>Disfruta de todos los beneficios desde ahora mismo.</p>" + "</body></html>");

							panelExito.add(iconoExito, BorderLayout.WEST);
							panelExito.add(msgExito, BorderLayout.CENTER);

							JOptionPane.showMessageDialog(this, panelExito, "Pago exitoso", JOptionPane.PLAIN_MESSAGE);

							actualizarInfoSuscripcion();

							// Actualizar también la ventana principal si es posible
							if (SwingUtilities.getWindowAncestor(this) instanceof VentanaPrincipal) {
								((VentanaPrincipal) SwingUtilities.getWindowAncestor(this)).actualizarEstadoPremium();
							}
						} else {
							JOptionPane.showMessageDialog(this,
									"Lo sentimos, hubo un problema al procesar tu pago. Inténtalo de nuevo.",
									"Error en el pago", JOptionPane.ERROR_MESSAGE);
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}

	private void cancelarPremium() {
		// Panel de confirmación estilizado
		JPanel panelConfirmacion = new JPanel(new BorderLayout(20, 10));
		panelConfirmacion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel iconoConfirmacion = new JLabel("⚠️");
		iconoConfirmacion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));

		JTextPane textoConfirmacion = new JTextPane();
		textoConfirmacion.setContentType("text/html");
		textoConfirmacion.setText("<html><body style='font-family: Segoe UI; width: 300px;'>"
				+ "<h3>¿Estás seguro de que deseas cancelar?</h3>"
				+ "<p>Tu suscripción Premium seguirá activa hasta la fecha de finalización, "
				+ "pero no se renovará automáticamente.</p>" + "<p><b>Fecha de finalización:</b> "
				+ ControladorPDS.getUnicaInstancia().getSesionActual().getPremium().getFechaFin()
						.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
				+ "</p>" + "</body></html>");
		textoConfirmacion.setEditable(false);
		textoConfirmacion.setOpaque(false);
		textoConfirmacion.setBorder(null);

		panelConfirmacion.add(iconoConfirmacion, BorderLayout.WEST);
		panelConfirmacion.add(textoConfirmacion, BorderLayout.CENTER);

		int confirmacion = JOptionPane.showConfirmDialog(this, panelConfirmacion, "Confirmar cancelación",
				JOptionPane.YES_NO_OPTION);

		if (confirmacion == JOptionPane.YES_OPTION) {
			boolean resultado = ControladorPDS.getUnicaInstancia().cancelarPremium();

			if (resultado) {
				// Panel de confirmación de cancelación
				JPanel panelCancelado = new JPanel(new BorderLayout(20, 10));
				panelCancelado.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

				JLabel iconoCancelado = new JLabel("ℹ️");
				iconoCancelado.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));

				Premium premium = ControladorPDS.getUnicaInstancia().getSesionActual().getPremium();

				JTextPane textoCancelado = new JTextPane();
				textoCancelado.setContentType("text/html");
				textoCancelado.setText(
						"<html><body style='font-family: Segoe UI; width: 300px;'>" + "<h3>Suscripción cancelada</h3>"
								+ "<p>Tu suscripción Premium ha sido cancelada. Seguirá activa hasta:</p>" + "<p><b>"
								+ premium.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</b></p>"
								+ "<p>Después de esta fecha perderás todos los beneficios Premium.</p>"
								+ "<p>Puedes reactivar tu suscripción en cualquier momento.</p>" + "</body></html>");
				textoCancelado.setEditable(false);
				textoCancelado.setOpaque(false);
				textoCancelado.setBorder(null);

				panelCancelado.add(iconoCancelado, BorderLayout.WEST);
				panelCancelado.add(textoCancelado, BorderLayout.CENTER);

				JOptionPane.showMessageDialog(this, panelCancelado, "Suscripción cancelada",
						JOptionPane.INFORMATION_MESSAGE);

				actualizarInfoSuscripcion();

				// Actualizar también la ventana principal si es posible
				if (SwingUtilities.getWindowAncestor(this) instanceof VentanaPrincipal) {
					((VentanaPrincipal) SwingUtilities.getWindowAncestor(this)).actualizarEstadoPremium();
				}
			} else {
				JOptionPane.showMessageDialog(this, "Hubo un problema al cancelar tu suscripción. Inténtalo de nuevo.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}