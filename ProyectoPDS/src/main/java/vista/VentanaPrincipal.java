package vista;

import modelado.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import controlador.ControladorPDS;

/**
 * Ventana principal mejorada con navegación a diferentes secciones,
 * visualización de estadísticas de usuario y asistente virtual.
 */
public class VentanaPrincipal extends JFrame {
	private static final long serialVersionUID = 1L;
	private Usuario usuario;
	private JPanel panelPrincipal, panelCentral, panelContenido;
	private JLabel saludoUsuario;
	private JButton btnCursosActivos, btnCursosDisponibles, btnMiPerfil, btnAsistente;
	private JTextArea areaBienvenida;
	private CardLayout gestorPaneles;
	private JLabel lblAsistente;
	private JTextPane textoAsistente;
	private JPanel panelAsistente;
	private Timer timerAsistente;
	private String[] consejosDia = { "¡Recuerda practicar al menos 15 minutos cada día para mantener tu racha!",
			"Los estudios muestran que estudiar antes de dormir mejora la retención",
			"Prueba a usar la técnica Pomodoro: 25 minutos de estudio, 5 de descanso",
			"Repasar el mismo contenido en diferentes momentos del día ayuda a memorizarlo",
			"¡Hoy es un buen día para empezar un nuevo curso!",
			"Compartir lo que aprendes con otros mejora tu comprensión",
			"El aprendizaje espaciado es más efectivo que el intensivo en maratones",
			"Los errores son parte fundamental del aprendizaje, ¡no temas cometerlos!" };

	public VentanaPrincipal() {
		usuario = ControladorPDS.getUnicaInstancia().getSesionActual();
		inicializarInterfaz();
	}

	private void inicializarInterfaz() {
		setTitle("Edulingo - Plataforma de Aprendizaje");
		setSize(1200, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true); // Mover esta línea arriba
		setShape(new RoundRectangle2D.Double(0, 0, 1200, 800, 15, 15));


		// Panel principal con gradiente
		panelPrincipal = new JPanel(new BorderLayout()) {
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
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		setContentPane(panelPrincipal);

		// Crear los diferentes paneles
		JPanel panelSuperior = crearPanelSuperior();
		panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

		panelCentral = new JPanel(new BorderLayout(10, 10));
		panelCentral.setOpaque(false);
		panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

		// Panel para el contenido central
		gestorPaneles = new CardLayout();
		panelContenido = new JPanel(gestorPaneles);
		panelContenido.setOpaque(false);

		// Crear los diferentes paneles de contenido
		JPanel panelBienvenida = crearPanelBienvenida();
		panelContenido.add(panelBienvenida, "bienvenida");

		// Panel de estadísticas
		JPanel panelEstadisticas = crearPanelEstadisticas();
		panelContenido.add(panelEstadisticas, "estadisticas");

		// Panel de asistente
		panelAsistente = crearPanelAsistente();
		panelAsistente.setVisible(false);

		// Panel de layout de contenido principal
		JPanel panelLayout = new JPanel(new BorderLayout());
		panelLayout.setOpaque(false);
		panelLayout.add(panelContenido, BorderLayout.CENTER);
		panelLayout.add(panelAsistente, BorderLayout.EAST);

		panelCentral.add(panelLayout, BorderLayout.CENTER);
		panelPrincipal.add(panelCentral, BorderLayout.CENTER);

		// Panel inferior para información adicional
		JPanel panelInferior = crearPanelInferior();
		panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

		// Mostrar el panel de bienvenida por defecto
		gestorPaneles.show(panelContenido, "bienvenida");

		// Hacer la ventana arrastrable
		habilitarArrastreVentana();
	}

	private JPanel crearPanelSuperior() {
		JPanel panel = new JPanel(new BorderLayout(10, 0));
		panel.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 0, new Color(200, 200, 200, 100)),
				BorderFactory.createEmptyBorder(10, 15, 10, 15)));
		panel.setOpaque(false);

		// Logo y nombre de app
		JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		panelIzquierdo.setOpaque(false);

		// Logo (importado o texto como alternativa)
		JLabel logoLabel = new JLabel("", SwingConstants.CENTER);
		try {
			ImageIcon logoIcon = new ImageIcon(getClass().getResource("/recursos/EdulingoRedimensionadad.png"));
			if (logoIcon.getIconWidth() == -1) {
				logoLabel = new JLabel("Edulingo", SwingConstants.CENTER);
				logoLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
				logoLabel.setForeground(new Color(66, 133, 244));
			} else {
				logoLabel.setIcon(new ImageIcon(logoIcon.getImage().getScaledInstance(130, 45, Image.SCALE_SMOOTH)));
			}
		} catch (Exception e) {
			logoLabel = new JLabel("Edulingo", SwingConstants.CENTER);
			logoLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
			logoLabel.setForeground(new Color(66, 133, 244));
		}
		panelIzquierdo.add(logoLabel);

		// Barra de menú principal
		JPanel panelCentral = new JPanel();
		panelCentral.setOpaque(false);
		panelCentral.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

		// Botones de navegación con efecto hover
		btnCursosActivos = crearBotonNavegacion("Mis Cursos", "/recursos/cursos_activos.png", "\uD83D\uDCDA");
		btnCursosDisponibles = crearBotonNavegacion("Explorar Cursos", "/recursos/cursos_disponibles.png",
				"\uD83D\uDD0E");
		btnAsistente = crearBotonNavegacion("Asistente", "/recursos/asistente.png", "\uD83E\uDD16");

		panelCentral.add(btnCursosActivos);
		panelCentral.add(btnCursosDisponibles);
		panelCentral.add(btnAsistente);

		// Panel perfil usuario
		JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		panelDerecho.setOpaque(false);

		// Saludo al usuario
		saludoUsuario = new JLabel("¡Hola, " + usuario.getNombreUsuario() + "!");
		saludoUsuario.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));

		// Avatar y menú de perfil
		btnMiPerfil = new JButton();
		btnMiPerfil.setBorderPainted(false);
		btnMiPerfil.setContentAreaFilled(false);
		btnMiPerfil.setFocusPainted(false);
		btnMiPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));

		try {
			ImageIcon avatarIcon = new ImageIcon(getClass().getResource("/recursos/avatar_usuario.png"));
			if (avatarIcon.getIconWidth() == -1) {
				btnMiPerfil.setText("\uD83D\uDC64"); // Emoji si no hay icono
				btnMiPerfil.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
			} else {
				Image img = avatarIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
				btnMiPerfil.setIcon(new ImageIcon(img));
			}
		} catch (Exception e) {
			btnMiPerfil.setText("\uD83D\uDC64"); // Emoji si no hay icono
			btnMiPerfil.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
		}

		// Menú desplegable para perfil
		JPopupMenu menuPerfil = new JPopupMenu();
		menuPerfil.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));

		JMenuItem itemEstadisticas = new JMenuItem("Mis Estadísticas");
		JMenuItem itemPremium = new JMenuItem("Edulingo Premium");
		JMenuItem itemRangos = new JMenuItem("Mis Rangos y Logros");
		JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");

		// Efecto hover en items del menú
		for (Component c : new Component[] { itemEstadisticas, itemPremium, itemRangos, itemCerrarSesion }) {
			if (c instanceof JMenuItem) {
				JMenuItem item = (JMenuItem) c;
				item.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
				item.setPreferredSize(new Dimension(180, 35));

				item.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						item.setBackground(new Color(240, 240, 250));
					}

					@Override
					public void mouseExited(MouseEvent e) {
						item.setBackground(UIManager.getColor("MenuItem.background"));
					}
				});
			}
		}

		// Acciones de los items
		itemEstadisticas.addActionListener(e -> {
			gestorPaneles.show(panelContenido, "estadisticas");
		});

		itemPremium.addActionListener(e -> {
				abrirPremium();
		});

		itemRangos.addActionListener(e -> {
			mostrarRangosYLogros();
		});

		itemCerrarSesion.addActionListener(e -> cambiarUsuario());

		menuPerfil.add(itemEstadisticas);
		menuPerfil.add(itemPremium);
		menuPerfil.add(itemRangos);
		menuPerfil.addSeparator();
		menuPerfil.add(itemCerrarSesion);

		btnMiPerfil.addActionListener(e -> {
			menuPerfil.show(btnMiPerfil, 0, btnMiPerfil.getHeight());
		});

		panelDerecho.add(saludoUsuario);
		panelDerecho.add(btnMiPerfil);

		// Botón de cerrar en la esquina superior derecha
		JButton btnCerrar = new JButton("×");
		btnCerrar.setFont(new Font("Arial", Font.BOLD, 20));
		btnCerrar.setForeground(new Color(120, 120, 120));
		btnCerrar.setBorderPainted(false);
		btnCerrar.setContentAreaFilled(false);
		btnCerrar.setFocusPainted(false);
		btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCerrar.addActionListener(e -> System.exit(0));

		JPanel panelCierreMinimizar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		panelCierreMinimizar.setOpaque(false);

		// Botón de minimizar
		JButton btnMinimizar = new JButton("_");
		btnMinimizar.setFont(new Font("Arial", Font.BOLD, 20));
		btnMinimizar.setForeground(new Color(120, 120, 120));
		btnMinimizar.setBorderPainted(false);
		btnMinimizar.setContentAreaFilled(false);
		btnMinimizar.setFocusPainted(false);
		btnMinimizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnMinimizar.addActionListener(e -> setState(JFrame.ICONIFIED));

		panelCierreMinimizar.add(btnMinimizar);
		panelCierreMinimizar.add(btnCerrar);

		// Añadir todos los paneles a sus posiciones
		panel.add(panelIzquierdo, BorderLayout.WEST);
		panel.add(panelCentral, BorderLayout.CENTER);
		panel.add(panelDerecho, BorderLayout.EAST);
		panel.add(panelCierreMinimizar, BorderLayout.NORTH);

		// Configurar acciones para los botones
		btnCursosActivos.addActionListener(e -> abrirVentanaCursosActivos());
		btnCursosDisponibles.addActionListener(e -> abrirVentanaCursosDisponibles());
		btnAsistente.addActionListener(e -> toggleAsistente());

		return panel;
	}

	private JButton crearBotonNavegacion(String texto, String rutaIcono, String iconoTexto) {
		JButton boton = new JButton(texto);
		boton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
		boton.setForeground(new Color(60, 60, 60));
		boton.setBorderPainted(false);
		boton.setContentAreaFilled(false);
		boton.setFocusPainted(false);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

		// Efecto hover
		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				boton.setForeground(new Color(66, 133, 244));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boton.setForeground(new Color(60, 60, 60));
			}
		});

		try {
			ImageIcon icono = new ImageIcon(getClass().getResource(rutaIcono));
			if (icono.getIconWidth() == -1) {
				boton.setText(iconoTexto + " " + texto);
				boton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
			} else {
				Image img = icono.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
				boton.setIcon(new ImageIcon(img));
				boton.setHorizontalTextPosition(SwingConstants.RIGHT);
				boton.setIconTextGap(10);
			}
		} catch (Exception e) {
			boton.setText(iconoTexto + " " + texto);
		}

		return boton;
	}

	private JPanel crearPanelBienvenida() {
		JPanel panel = new JPanel(new BorderLayout(20, 20));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Panel superior con imagen destacada
		JPanel panelImagen = new JPanel(new BorderLayout());
		panelImagen.setOpaque(false);
		panelImagen.setPreferredSize(new Dimension(0, 220)); // Alto fijo para imagen
		panelImagen.setBorder(new LineBorder(new Color(200, 200, 220), 1, true));

		// Panel con gradiente y texto superpuesto
		JPanel panelDestacado = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				int w = getWidth();
				int h = getHeight();

				// Fondo degradado
				GradientPaint gp = new GradientPaint(0, 0, new Color(66, 133, 244, 220), w, h,
						new Color(15, 76, 129, 220));
				g2d.setPaint(gp);
				g2d.fillRect(0, 0, w, h);

				// Círculos decorativos
				g2d.setColor(new Color(255, 255, 255, 40));
				g2d.fillOval(-50, -50, 200, 200);
				g2d.fillOval(w - 100, h - 100, 200, 200);
			}
		};

		// Mensaje de bienvenida personalizado
		JLabel lblBienvenida = new JLabel(
				"<html><div style='text-align: center;'>¡Bienvenido a tu viaje de aprendizaje, "
						+ usuario.getNombreUsuario() + "!</div></html>");
		lblBienvenida.setFont(new Font("Segoe UI Emoji", Font.BOLD, 28));
		lblBienvenida.setForeground(Color.WHITE);
		lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenida.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));

		JLabel lblSubtitulo = new JLabel(
				"<html><div style='text-align: center;'>Aprender es más divertido con Edulingo. ¡Continúa tu aventura!</div></html>");
		lblSubtitulo.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 16));
		lblSubtitulo.setForeground(new Color(240, 240, 255));
		lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel panelTexto = new JPanel(new BorderLayout());
		panelTexto.setOpaque(false);
		panelTexto.add(lblBienvenida, BorderLayout.NORTH);
		panelTexto.add(lblSubtitulo, BorderLayout.CENTER);

		// Botón para comenzar
		JButton btnComenzar = crearBotonDestacado("¡Comenzar ahora!", new Color(76, 175, 80));
		btnComenzar.addActionListener(e -> abrirVentanaCursosActivos());

		JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelBoton.setOpaque(false);
		panelBoton.add(btnComenzar);
		panelTexto.add(panelBoton, BorderLayout.SOUTH);

		panelDestacado.add(panelTexto, BorderLayout.CENTER);
		panelImagen.add(panelDestacado, BorderLayout.CENTER);
		panel.add(panelImagen, BorderLayout.NORTH);

		// Panel central con contenido
		JPanel panelCentroContenido = new JPanel(new GridLayout(1, 2, 20, 0));
		panelCentroContenido.setOpaque(false);

		// Panel de resumen de actividad
		JPanel panelResumen = crearPanelCartas("Mi actividad reciente",
				new String[] { "Has completado 3 lecciones de Programación Java en la última semana",
						"Tienes una racha actual de " + usuario.getEstadisticas().getMejorRacha() + " días",
						"Tu nivel actual es: Aprendiz entusiasta",
						"Próxima meta: 30 minutos de estudio diario durante 7 días" },
				new Color(33, 150, 243, 50));

		// Panel de cursos recomendados
		JPanel panelRecomendados = crearPanelCartas("Recomendados para ti",
				new String[] { "Curso avanzado de Programación Orientada a Objetos", "Fundamentos de Bases de Datos",
						"Patrones de Diseño en Java", "Desarrollo de Interfaces Gráficas con Swing" },
				new Color(76, 175, 80, 50));

		panelCentroContenido.add(panelResumen);
		panelCentroContenido.add(panelRecomendados);
		panel.add(panelCentroContenido, BorderLayout.CENTER);

		// Panel inferior con accesos rápidos
		JPanel panelAccesos = new JPanel(new GridLayout(1, 3, 15, 0));
		panelAccesos.setOpaque(false);
		panelAccesos.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

		panelAccesos.add(crearBotonAccesoRapido("Mis Cursos", "\uD83D\uDCDA", "Continúa donde lo dejaste",
				e -> abrirVentanaCursosActivos()));
		panelAccesos.add(crearBotonAccesoRapido("Explorar", "\uD83D\uDD0E", "Descubre nuevos cursos",
				e -> abrirVentanaCursosDisponibles()));
		panelAccesos.add(crearBotonAccesoRapido("Comunidad", "\uD83D\uDC65", "Conéctate con otros estudiantes",
			    e -> abrirVentanaComunidad()));

		panel.add(panelAccesos, BorderLayout.SOUTH);

		return panel;
	}

	private JPanel crearPanelCartas(String titulo, String[] items, Color colorFondo) {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Título del panel
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
		panel.add(lblTitulo, BorderLayout.NORTH);

		// Panel de contenido con items
		JPanel panelItems = new JPanel();
		panelItems.setLayout(new BoxLayout(panelItems, BoxLayout.Y_AXIS));
		panelItems.setOpaque(false);

		for (String item : items) {
			JPanel carta = new JPanel(new BorderLayout(10, 0)) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.setColor(colorFondo);
					g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
				}
			};
			carta.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
			carta.setOpaque(false);

			JLabel lblItem = new JLabel(item);
			lblItem.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
			carta.add(lblItem, BorderLayout.CENTER);

			// Espaciado entre cartas
			panelItems.add(Box.createVerticalStrut(10));
			panelItems.add(carta);
		}

		JScrollPane scrollPane = new JScrollPane(panelItems);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);

		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private JPanel crearPanelAsistente() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setPreferredSize(new Dimension(280, 0));
		panel.setBorder(new CompoundBorder(new MatteBorder(0, 1, 0, 0, new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(10, 15, 10, 15)));
		panel.setBackground(new Color(245, 248, 250));

		// Título del panel
		JPanel panelTitulo = new JPanel(new BorderLayout());
		panelTitulo.setOpaque(false);
		panelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		JLabel lblTituloAsistente = new JLabel("Tu Asistente Edulingo");
		lblTituloAsistente.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));

		JButton btnCerrarAsistente = new JButton("×");
		btnCerrarAsistente.setFont(new Font("Arial", Font.BOLD, 18));
		btnCerrarAsistente.setForeground(new Color(120, 120, 120));
		btnCerrarAsistente.setBorderPainted(false);
		btnCerrarAsistente.setContentAreaFilled(false);
		btnCerrarAsistente.setFocusPainted(false);
		btnCerrarAsistente.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnCerrarAsistente.addActionListener(e -> toggleAsistente());

		panelTitulo.add(lblTituloAsistente, BorderLayout.WEST);
		panelTitulo.add(btnCerrarAsistente, BorderLayout.EAST);

		// Avatar del asistente
		lblAsistente = new JLabel();
		lblAsistente.setHorizontalAlignment(SwingConstants.CENTER);

		try {
			ImageIcon avatarAsistente = new ImageIcon(getClass().getResource("/recursos/asistente_avatar.png"));
			if (avatarAsistente.getIconWidth() == -1) {
				lblAsistente.setText("\uD83E\uDD16"); // Emoji robot
				lblAsistente.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
			} else {
				Image img = avatarAsistente.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
				lblAsistente.setIcon(new ImageIcon(img));
			}
		} catch (Exception e) {
			lblAsistente.setText("\uD83E\uDD16"); // Emoji robot
			lblAsistente.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
		}

		// Texto del asistente
		textoAsistente = new JTextPane();
		textoAsistente.setContentType("text/html");
		textoAsistente.setEditable(false);
		textoAsistente.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		textoAsistente.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));

		// Mensaje inicial
		actualizarMensajeAsistente("¡Hola " + usuario.getNombreUsuario()
				+ "! Soy tu asistente personal de Edulingo. ¿En qué puedo ayudarte hoy?");

		// Botones de acción rápida
		JPanel panelAcciones = new JPanel();
		panelAcciones.setLayout(new BoxLayout(panelAcciones, BoxLayout.Y_AXIS));
		panelAcciones.setOpaque(false);

		JButton btnConsejo = crearBotonAsistente("Dame un consejo");
		JButton btnCurso = crearBotonAsistente("Sugerir un curso");
		JButton btnRacha = crearBotonAsistente("¿Cómo va mi racha?");

		btnConsejo.addActionListener(e -> {
			int indice = (int) (Math.random() * consejosDia.length);
			actualizarMensajeAsistente(consejosDia[indice]);
		});

		btnCurso.addActionListener(e -> {
			actualizarMensajeAsistente(
					"Basado en tu progreso, te recomendaría explorar el curso de Patrones de Diseño en Java. ¡Es un excelente complemento a tu formación actual!");
		});

		btnRacha.addActionListener(e -> {
			Estadistica stats = usuario.getEstadisticas();
			actualizarMensajeAsistente("¡Llevas " + stats.getMejorRacha()
					+ " días de racha! Sigue así para alcanzar nuevos niveles y desbloquear recompensas.");
		});

		panelAcciones.add(btnConsejo);
		panelAcciones.add(Box.createVerticalStrut(8));
		panelAcciones.add(btnCurso);
		panelAcciones.add(Box.createVerticalStrut(8));
		panelAcciones.add(btnRacha);

		JPanel panelAvatar = new JPanel(new BorderLayout(0, 10));
		panelAvatar.setOpaque(false);
		panelAvatar.add(lblAsistente, BorderLayout.CENTER);

		JPanel panelContenidoAsistente = new JPanel(new BorderLayout(0, 15));
		panelContenidoAsistente.setOpaque(false);
		panelContenidoAsistente.add(textoAsistente, BorderLayout.CENTER);
		panelContenidoAsistente.add(panelAcciones, BorderLayout.SOUTH);

		panel.add(panelTitulo, BorderLayout.NORTH);
		panel.add(panelAvatar, BorderLayout.NORTH);
		panel.add(panelContenidoAsistente, BorderLayout.CENTER);

		// Timer para cambiar consejos automáticamente
		timerAsistente = new Timer(300000, e -> { // 5 minutos
			int indice = (int) (Math.random() * consejosDia.length);
			actualizarMensajeAsistente(consejosDia[indice]);
		});
		timerAsistente.start();

		return panel;
	}

	private void actualizarMensajeAsistente(String mensaje) {
		String estiloHTML = "<html><body style='font-family:Segoe UI Emoji; font-size:14px;'>"
				+ mensaje.replace("\n", "<br/>") + "</body></html>";
		textoAsistente.setText(estiloHTML);
	}

	private JPanel crearPanelEstadisticas() {
		JPanel panel = new JPanel(new BorderLayout(20, 20));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Panel superior con título y descripción
		JPanel panelTitulo = new JPanel(new BorderLayout());
		panelTitulo.setOpaque(false);

		JLabel lblTitulo = new JLabel("Mis Estadísticas");
		lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));

		JLabel lblDescripcion = new JLabel("Visualiza tu progreso y crecimiento en Edulingo");
		lblDescripcion.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 14));
		lblDescripcion.setForeground(new Color(100, 100, 100));

		JButton btnVolver = new JButton("Volver al inicio");
		btnVolver.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
		btnVolver.setFocusPainted(false);
		btnVolver.addActionListener(e -> gestorPaneles.show(panelContenido, "bienvenida"));

		panelTitulo.add(lblTitulo, BorderLayout.NORTH);
		panelTitulo.add(lblDescripcion, BorderLayout.CENTER);
		panelTitulo.add(btnVolver, BorderLayout.EAST);

		// Panel central con tarjetas de estadísticas
		JPanel panelTarjetas = new JPanel(new GridLayout(2, 2, 15, 15));
		panelTarjetas.setOpaque(false);

		// Obtener estadísticas del usuario
		Estadistica stats = usuario.getEstadisticas();

		// Tarjeta de tiempo de uso
		JPanel tarjetaTiempo = crearTarjetaEstadistica("Tiempo total dedicado", formatearTiempo(stats.getTiempoUso()),
				new Color(66, 133, 244, 50), "\u23F1");

		// Tarjeta de racha
		JPanel tarjetaRacha = crearTarjetaEstadistica("Mejor racha", stats.getMejorRacha() + " días",
				new Color(219, 68, 55, 50), "\uD83D\uDD25");

		// Tarjeta de cursos
		JPanel tarjetaCursos = crearTarjetaEstadistica("Cursos activos", usuario.getCursosActivos().size() + " cursos",
				new Color(15, 157, 88, 50), "\uD83D\uDCDA");

		// Fecha de registro formateada
		LocalDate fechaRegistro = usuario.getFechaRegistro();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String fechaFormateada = fechaRegistro.format(formatter);

		// Días desde registro
		LocalDate hoy = LocalDate.now();
		long diasMiembro = java.time.temporal.ChronoUnit.DAYS.between(fechaRegistro, hoy);

		// Tarjeta de miembro desde
		JPanel tarjetaMiembro = crearTarjetaEstadistica("Miembro desde",
				fechaFormateada + " (" + diasMiembro + " días)", new Color(244, 160, 0, 50), "\uD83C\uDF1F");

		panelTarjetas.add(tarjetaTiempo);
		panelTarjetas.add(tarjetaRacha);
		panelTarjetas.add(tarjetaCursos);
		panelTarjetas.add(tarjetaMiembro);

		// Panel inferior con gráfico o datos adicionales
		JPanel panelInferior = new JPanel(new BorderLayout());
		panelInferior.setOpaque(false);
		panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

		JLabel lblProgresoTitulo = new JLabel("Tu nivel de progreso");
		lblProgresoTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));

		// Simulación de un gráfico de progreso
		JPanel panelProgreso = new JPanel(new BorderLayout(10, 10)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				int width = getWidth();
				int height = getHeight();

				// Fondo del gráfico
				g2d.setColor(new Color(240, 240, 240));
				g2d.fillRoundRect(0, 0, width, height, 15, 15);

				// Barras de progreso
				int margen = 40;
				int espaciado = 50;
				int altoBarras = 30;
				int anchoDisponible = width - 2 * margen;

				String[] categorias = { "Java", "POO", "Patrones", "Interfaces" };
				int[] valores = { 85, 70, 50, 60 }; // Porcentajes de progreso
				Color[] colores = { new Color(66, 133, 244), new Color(219, 68, 55), new Color(244, 160, 0),
						new Color(15, 157, 88) };

				for (int i = 0; i < categorias.length; i++) {
					int y = margen + i * espaciado;

					// Etiqueta
					g2d.setColor(Color.BLACK);
					g2d.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
					g2d.drawString(categorias[i], 10, y + altoBarras / 2 + 5);

					// Barra de fondo
					g2d.setColor(new Color(200, 200, 200));
					g2d.fillRoundRect(100, y, anchoDisponible - 100, altoBarras, 10, 10);

					// Barra de progreso
					g2d.setColor(colores[i]);
					int anchoBarra = (int) ((anchoDisponible - 100) * valores[i] / 100.0);
					g2d.fillRoundRect(100, y, anchoBarra, altoBarras, 10, 10);

					// Porcentaje
					g2d.setColor(Color.WHITE);
					g2d.drawString(valores[i] + "%", 110, y + altoBarras / 2 + 5);
				}
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(600, 250);
			}
		};

		panelInferior.add(lblProgresoTitulo, BorderLayout.NORTH);
		panelInferior.add(panelProgreso, BorderLayout.CENTER);

		panel.add(panelTitulo, BorderLayout.NORTH);
		panel.add(panelTarjetas, BorderLayout.CENTER);
		panel.add(panelInferior, BorderLayout.SOUTH);

		return panel;
	}

	private JPanel crearTarjetaEstadistica(String titulo, String valor, Color colorFondo, String emoji) {
		JPanel panel = new JPanel(new BorderLayout(5, 10)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(colorFondo);
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

				// Pequeños círculos decorativos
				g2d.setColor(new Color(255, 255, 255, 50));
				g2d.fillOval(-20, -20, 70, 70);
				g2d.fillOval(getWidth() - 30, getHeight() - 30, 60, 60);
			}
		};
		panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		panel.setOpaque(false);

		JLabel lblEmoji = new JLabel(emoji);
		lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
		lblEmoji.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblValor = new JLabel(valor);
		lblValor.setFont(new Font("Segoe UI Emoji", Font.BOLD, 17));
		lblValor.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
		panelCentro.setOpaque(false);
		panelCentro.add(lblTitulo, BorderLayout.NORTH);
		panelCentro.add(lblValor, BorderLayout.CENTER);

		panel.add(lblEmoji, BorderLayout.NORTH);
		panel.add(panelCentro, BorderLayout.CENTER);

		return panel;
	}

	private JPanel crearPanelInferior() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
		panel.setOpaque(false);

		// Panel izquierdo con información y fecha
		JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
		panelIzquierdo.setOpaque(false);

		// Información de racha y tiempo de uso
		Estadistica stats = usuario.getEstadisticas();
		JLabel lblFecha = new JLabel("Hoy: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		lblFecha.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 12));

		JLabel lblRacha = new JLabel("Racha actual: " + stats.getMejorRacha() + " días 🔥");
		lblRacha.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));

		panelIzquierdo.add(lblFecha);
		panelIzquierdo.add(lblRacha);

		// Panel central vacío para mantener equilibrio
		JPanel panelCentro = new JPanel();
		panelCentro.setOpaque(false);

		// Panel derecho con enlaces rápidos
		JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
		panelDerecho.setOpaque(false);

		// Enlaces de ayuda y contacto
		JLabel lblAyuda = new JLabel("Ayuda");
		lblAyuda.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
		lblAyuda.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblAyuda.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(VentanaPrincipal.this,
						"Para obtener ayuda contacta con m.davidsona@um.es", "Ayuda Edulingo",
						JOptionPane.INFORMATION_MESSAGE);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblAyuda.setForeground(new Color(66, 133, 244));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblAyuda.setForeground(Color.BLACK);
			}
		});

		JLabel lblContacto = new JLabel("Contacto");
		lblContacto.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
		lblContacto.setCursor(new Cursor(Cursor.HAND_CURSOR));
		lblContacto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(VentanaPrincipal.this, "Puedes contactarnos en info@edulingo.com",
						"Contacto Edulingo", JOptionPane.INFORMATION_MESSAGE);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblContacto.setForeground(new Color(66, 133, 244));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				lblContacto.setForeground(Color.BLACK);
			}
		});

		JLabel lblVersion = new JLabel("v1.2.0");
		lblVersion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
		lblVersion.setForeground(new Color(150, 150, 150));

		panelDerecho.add(lblAyuda);
		panelDerecho.add(lblContacto);
		panelDerecho.add(lblVersion);

		panel.add(panelIzquierdo, BorderLayout.WEST);
		panel.add(panelCentro, BorderLayout.CENTER);
		panel.add(panelDerecho, BorderLayout.EAST);

		return panel;
	}

	private JButton crearBotonDestacado(String texto, Color colorBase) {
		JButton boton = new JButton(texto);
		boton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
		boton.setForeground(Color.WHITE);
		boton.setBackground(colorBase);
		boton.setFocusPainted(false);
		boton.setBorderPainted(false);
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));

		// Efectos al pasar el ratón
		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// Oscurecer el color al pasar el ratón
				Color colorMasOscuro = colorBase.darker();
				boton.setBackground(colorMasOscuro);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boton.setBackground(colorBase);
			}
		});

		return boton;
	}

	private JPanel crearBotonAccesoRapido(String titulo, String emoji, String subtitulo, ActionListener accion) {
		JPanel panel = new JPanel(new BorderLayout(10, 5)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(new Color(240, 240, 250));
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
			}
		};
		panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

		JLabel lblEmoji = new JLabel(emoji);
		lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
		lblEmoji.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblSubtitulo = new JLabel(subtitulo);
		lblSubtitulo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
		lblSubtitulo.setForeground(new Color(100, 100, 100));
		lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel panelTexto = new JPanel(new BorderLayout(5, 2));
		panelTexto.setOpaque(false);
		panelTexto.add(lblTitulo, BorderLayout.NORTH);
		panelTexto.add(lblSubtitulo, BorderLayout.CENTER);

		panel.add(lblEmoji, BorderLayout.NORTH);
		panel.add(panelTexto, BorderLayout.CENTER);

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (accion != null) {
					accion.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "clicked"));
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				panel.setBackground(new Color(230, 230, 250));
				panel.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				panel.setBackground(UIManager.getColor("Panel.background"));
				panel.repaint();
			}
		});

		return panel;
	}

	private JButton crearBotonAsistente(String texto) {
		JButton boton = new JButton(texto);
		boton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
		boton.setFocusPainted(false);
		boton.setContentAreaFilled(false);
		boton.setBorderPainted(true);
		boton.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
		boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		boton.setPreferredSize(new Dimension(200, 35));

		boton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				boton.setBackground(new Color(245, 245, 255));
				boton.setBorder(new LineBorder(new Color(66, 133, 244), 1, true));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				boton.setBackground(UIManager.getColor("Button.background"));
				boton.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
			}
		});

		return boton;
	}

	private void toggleAsistente() {
		panelAsistente.setVisible(!panelAsistente.isVisible());
		revalidate();
		repaint();
	}

	private void mostrarRangosYLogros() {
		JDialog dialogoRangos = new JDialog(this, "Mis Rangos y Logros", true);
		dialogoRangos.setSize(600, 500);
		dialogoRangos.setLocationRelativeTo(this);
		dialogoRangos.setLayout(new BorderLayout());

		JPanel panelContenido = new JPanel(new BorderLayout(20, 20));
		panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panelContenido.setBackground(new Color(250, 250, 255));

		// Título
		JLabel lblTitulo = new JLabel("Tus Rangos y Logros");
		lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		// Panel de usuario y nivel
		JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panelUsuario.setOpaque(false);

		// Avatar
		JLabel lblAvatar = new JLabel();
		try {
			ImageIcon avatarIcon = new ImageIcon(getClass().getResource("/recursos/avatar_usuario.png"));
			if (avatarIcon.getIconWidth() == -1) {
				lblAvatar.setText("\uD83D\uDC64"); // Emoji si no hay icono
				lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
			} else {
				Image img = avatarIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
				lblAvatar.setIcon(new ImageIcon(img));
			}
		} catch (Exception e) {
			lblAvatar.setText("\uD83D\uDC64"); // Emoji si no hay icono
			lblAvatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
		}

		// Información de nivel
		JPanel panelNivel = new JPanel();
		panelNivel.setLayout(new BoxLayout(panelNivel, BoxLayout.Y_AXIS));
		panelNivel.setOpaque(false);

		JLabel lblNombre = new JLabel(usuario.getNombreUsuario());
		lblNombre.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
		lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblRango = new JLabel("Aprendiz Entusiasta");
		lblRango.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 16));
		lblRango.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Barra de progreso
		JProgressBar barraProgreso = new JProgressBar(0, 100);
		barraProgreso.setValue(75);
		barraProgreso.setStringPainted(true);
		barraProgreso.setString("Nivel 3 - 75%");
		barraProgreso.setPreferredSize(new Dimension(200, 20));
		barraProgreso.setMaximumSize(new Dimension(200, 20));
		barraProgreso.setAlignmentX(Component.CENTER_ALIGNMENT);

		panelNivel.add(lblNombre);
		panelNivel.add(Box.createVerticalStrut(5));
		panelNivel.add(lblRango);
		panelNivel.add(Box.createVerticalStrut(10));
		panelNivel.add(barraProgreso);

		panelUsuario.add(lblAvatar);
		panelUsuario.add(panelNivel);

		// Panel de logros
		JPanel panelLogros = new JPanel(new GridLayout(0, 3, 10, 10));
		panelLogros.setOpaque(false);
		panelLogros.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)),
				"Logros Desbloqueados", TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI Emoji", Font.BOLD, 16)));

		// Añadir logros
		panelLogros.add(crearTarjetaLogro("Primer Día", "Completaste tu primera lección", true));
		panelLogros.add(crearTarjetaLogro("Racha x7", "Mantuviste una racha de 7 días", true));
		panelLogros.add(crearTarjetaLogro("Explorador", "Probaste 3 cursos diferentes", true));
		panelLogros.add(crearTarjetaLogro("Madrugador", "Estudiaste antes de las 8:00 AM", false));
		panelLogros.add(crearTarjetaLogro("Nocturno", "Estudiaste después de las 22:00", true));
		panelLogros.add(crearTarjetaLogro("Racha x30", "Mantén una racha de 30 días", false));
		panelLogros.add(crearTarjetaLogro("Maestro Java", "Completa el curso de Java", false));
		panelLogros.add(crearTarjetaLogro("Políglota", "Aprende 3 lenguajes", false));
		panelLogros.add(crearTarjetaLogro("Colaborador", "Crea tu primer curso", false));

		JScrollPane scrollLogros = new JScrollPane(panelLogros);
		scrollLogros.setBorder(BorderFactory.createEmptyBorder());
		scrollLogros.getVerticalScrollBar().setUnitIncrement(16);

		// Panel de próximos rangos
		JPanel panelRangos = new JPanel();
		panelRangos.setLayout(new BoxLayout(panelRangos, BoxLayout.Y_AXIS));
		panelRangos.setOpaque(false);
		panelRangos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)),
				"Próximos Rangos", TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI Emoji", Font.BOLD, 16)));

		panelRangos.add(crearItemRango("Aprendiz Entusiasta", "Nivel actual"));
		panelRangos.add(Box.createVerticalStrut(5));
		panelRangos.add(crearItemRango("Estudiante Constante", "Alcanza el Nivel 5"));
		panelRangos.add(Box.createVerticalStrut(5));
		panelRangos.add(crearItemRango("Experto Junior", "Completa 5 cursos"));
		panelRangos.add(Box.createVerticalStrut(5));
		panelRangos.add(crearItemRango("Maestro del Conocimiento", "Alcanza el Nivel 10"));

		// Botón de cerrar
		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
		btnCerrar.addActionListener(e -> dialogoRangos.dispose());

		JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelBoton.setOpaque(false);
		panelBoton.add(btnCerrar);

		// Añadir componentes al panel principal
		panelContenido.add(lblTitulo, BorderLayout.NORTH);
		panelContenido.add(panelUsuario, BorderLayout.NORTH);

		JPanel panelCentro = new JPanel(new BorderLayout(0, 15));
		panelCentro.setOpaque(false);
		panelCentro.add(scrollLogros, BorderLayout.CENTER);
		panelCentro.add(panelRangos, BorderLayout.SOUTH);

		panelContenido.add(panelCentro, BorderLayout.CENTER);
		panelContenido.add(panelBoton, BorderLayout.SOUTH);

		dialogoRangos.add(panelContenido);
		dialogoRangos.setVisible(true);
	}

	private JPanel crearTarjetaLogro(String titulo, String descripcion, boolean desbloqueado) {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		panel.setBackground(desbloqueado ? new Color(240, 248, 255) : new Color(245, 245, 245));

		JLabel lblIcono = new JLabel(desbloqueado ? "🏆" : "🔒");
		lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
		lblIcono.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));

		JLabel lblDescripcion = new JLabel("<html><body style='width: 120px'>" + descripcion + "</body></html>");
		lblDescripcion.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
		lblDescripcion.setForeground(desbloqueado ? new Color(60, 60, 60) : new Color(150, 150, 150));

		JPanel panelTexto = new JPanel(new BorderLayout(2, 2));
		panelTexto.setOpaque(false);
		panelTexto.add(lblTitulo, BorderLayout.NORTH);
		panelTexto.add(lblDescripcion, BorderLayout.CENTER);

		panel.add(lblIcono, BorderLayout.WEST);
		panel.add(panelTexto, BorderLayout.CENTER);

		return panel;
	}

	private JPanel crearItemRango(String rango, String requisito) {
		JPanel panel = new JPanel(new BorderLayout(10, 0));
		panel.setOpaque(false);
		panel.setMaximumSize(new Dimension(400, 40));

		JLabel lblIcono = new JLabel("🏅");
		lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

		JLabel lblRango = new JLabel(rango);
		lblRango.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));

		JLabel lblRequisito = new JLabel(requisito);
		lblRequisito.setFont(new Font("Segoe UI Emoji", Font.ITALIC, 12));
		lblRequisito.setForeground(new Color(100, 100, 100));

		JPanel panelTexto = new JPanel(new BorderLayout());
		panelTexto.setOpaque(false);
		panelTexto.add(lblRango, BorderLayout.NORTH);
		panelTexto.add(lblRequisito, BorderLayout.CENTER);

		panel.add(lblIcono, BorderLayout.WEST);
		panel.add(panelTexto, BorderLayout.CENTER);

		return panel;
	}

	private void abrirVentanaCursosActivos() {
		// Guardar referencia a esta ventana para volver
		JFrame ventanaAnterior = this;

		// Crear una instancia de VentanaCursosActivos y mostrarla
		try {
			dispose(); // Cerrar ventana actual
			VentanaCursosEnMarcha ventana = new VentanaCursosEnMarcha();
			ventana.setVisible(true);
		} catch (Exception e) {
			// Si hay error, mostramos mensaje y volvemos a mostrar esta ventana
			JOptionPane.showMessageDialog(null, "Error al abrir la ventana de cursos activos: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			ventanaAnterior.setVisible(true);
		}
	}

	private void abrirVentanaCursosDisponibles() {
		// Guardar referencia a esta ventana para volver
		JFrame ventanaAnterior = this;

		// Crear una instancia de VentanaCursosDisponibles y mostrarla
		try {
			dispose(); // Cerrar ventana actual
			VentanaCursosSinEmpezar ventana = new VentanaCursosSinEmpezar();
			ventana.setVisible(true);
		} catch (Exception e) {
			// Si hay error, mostramos mensaje y volvemos a mostrar esta ventana
			JOptionPane.showMessageDialog(null, "Error al abrir la ventana de cursos disponibles: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			ventanaAnterior.setVisible(true);
		}
	}

	private String formatearTiempo(int minutos) {
		int horas = minutos / 60;
		int minutosRestantes = minutos % 60;

		if (horas > 0) {
			return horas + " h " + (minutosRestantes > 0 ? minutosRestantes + " min" : "");
		} else {
			return minutosRestantes + " minutos";
		}
	}

	private void cambiarUsuario() {
		int respuesta = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas cerrar sesión?",
				"Confirmar cierre de sesión", JOptionPane.YES_NO_OPTION);

		if (respuesta == JOptionPane.YES_OPTION) {
			dispose();
			ControladorPDS.getUnicaInstancia().cerrarSesion();
			new VentanaLogin().setVisible(true);
		}
	}

	private void habilitarArrastreVentana() {
		// Variables para tracking del movimiento
		final Point[] draggedAt = { new Point(0, 0) };

		MouseAdapter adaptador = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				draggedAt[0] = e.getPoint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				Point currentPosition = e.getLocationOnScreen();
				setLocation(currentPosition.x - draggedAt[0].x, currentPosition.y - draggedAt[0].y);
			}
		};

		// Aplicar el adaptador a los componentes superiores para permitir arrastrar la
		// ventana
		getContentPane().addMouseListener(adaptador);
		getContentPane().addMouseMotionListener(adaptador);
	}
	private void abrirVentanaComunidad() {
	    // Si estamos usando el gestor de paneles para cambiar entre contenidos
	    PanelComunidad panelComunidad = new PanelComunidad(usuario);
	    
	    // Si no existe ya un panel de comunidad en el gestor de paneles, lo añadimos
	    if (!existePanelEnGestor("comunidad")) {
	        panelContenido.add(panelComunidad, "comunidad");
	    }
	    
	    // Mostrar el panel de comunidad
	    gestorPaneles.show(panelContenido, "comunidad");
	    
	    // Opcional: Si usas un panel de asistente, puedes ocultarlo o mostrarlo según necesites
	    if (panelAsistente != null) {
	        panelAsistente.setVisible(false);
	    }
	}
	
	private void abrirPremium() {
	    // Si estamos usando el gestor de paneles para cambiar entre contenidos
	    PanelPremium panelPremium = new PanelPremium();
	    
	    // Si no existe ya un panel de comunidad en el gestor de paneles, lo añadimos
	    if (!existePanelEnGestor("premium")) {
	        panelContenido.add(panelPremium, "premium");
	    }
	    
	    // Mostrar el panel de comunidad
	    gestorPaneles.show(panelContenido, "premium");
	    
	    // Opcional: Si usas un panel de asistente, puedes ocultarlo o mostrarlo según necesites
	    if (panelAsistente != null) {
	        panelAsistente.setVisible(false);
	    }
	}

	// Método auxiliar para verificar si un panel ya existe en el gestor
	private boolean existePanelEnGestor(String nombre) {
	    for (Component c : panelContenido.getComponents()) {
	        if (c.getName() != null && c.getName().equals(nombre)) {
	            return true;
	        }
	    }
	    return false;
	}
	/**
	 * Actualiza la interfaz según el estado premium del usuario
	 */
	public void actualizarEstadoPremium() {
	    boolean esPremium = ControladorPDS.getUnicaInstancia().esPremium();
	    
	    // Actualizar saludo con corona para usuarios premium
	    if (esPremium) {
	        saludoUsuario.setText("¡Hola, " + usuario.getNombreUsuario() + "! 👑");
	    } else {
	        saludoUsuario.setText("¡Hola, " + usuario.getNombreUsuario() + "!");
	    }

	    
	    // Si hay un panel premium abierto, actualizar su información
	    if (existePanelEnGestor("premium")) {
	        Component[] components = panelContenido.getComponents();
	        for (Component c : components) {
	            if (c instanceof PanelPremium) {
	                ((PanelPremium) c).actualizarInfoSuscripcion();
	                break;
	            }
	        }
	    }
	   
	}
}