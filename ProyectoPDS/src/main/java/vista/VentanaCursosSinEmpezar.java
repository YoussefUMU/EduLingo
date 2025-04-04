package vista;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import modelado.Curso;
import modelado.CursoEnMarcha;
import controlador.ControladorPDS;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaCursosSinEmpezar extends JFrame {
	private JPanel panelContenidoScroll;

	public VentanaCursosSinEmpezar() {
		initialize();
	}

	public void initialize() {
		setTitle("Edulingo - Explorar Cursos");
		setBackground(new Color(240, 240, 240));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationByPlatform(true);
		setSize(new Dimension(600, 600));
		getContentPane().setBackground(new Color(240, 240, 240));
		getContentPane().setLayout(new BorderLayout(0, 0));

		// Panel superior
		JPanel panelNorte = new JPanel(new BorderLayout(10, 10));
		panelNorte.setBackground(new Color(240, 240, 240));
		panelNorte.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		getContentPane().add(panelNorte, BorderLayout.NORTH);

		// Panel izquierdo para logo
		JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelLogo.setOpaque(false);
		
		ImageIcon iconoLogo = new ImageIcon(getClass().getResource("/Recursos/EdulingoRedimensionadad.png"));
		JLabel lblLogo = new JLabel(iconoLogo);
		panelLogo.add(lblLogo);
		
		panelNorte.add(panelLogo, BorderLayout.WEST);

		// Etiqueta de título
		JLabel lblNewLabel = new JLabel("CURSOS DISPONIBLES");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 20));
		panelNorte.add(lblNewLabel, BorderLayout.CENTER);

		// Botón de añadir curso en panel de la derecha
		JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelBoton.setBackground(new Color(240, 240, 240));
		
		JButton btnAñadirCurso = new JButton("Añadir Curso");
		btnAñadirCurso.setFont(new Font("Arial", Font.BOLD, 12));
		btnAñadirCurso.setBackground(new Color(76, 175, 80));
		btnAñadirCurso.setForeground(Color.WHITE);
		
		btnAñadirCurso.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Funcionalidad para añadir curso", 
						"Añadir Curso", JOptionPane.INFORMATION_MESSAGE);
				// Aquí se abriría la ventana para añadir un curso
			}
		});
		
		panelBoton.add(btnAñadirCurso);
		panelNorte.add(panelBoton, BorderLayout.EAST);

		// Panel intermedio para centrar el scroll
		JPanel panelCentroContenedor = new JPanel(new BorderLayout());
		panelCentroContenedor.setBackground(new Color(240, 240, 240));
		panelCentroContenedor.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		getContentPane().add(panelCentroContenedor, BorderLayout.CENTER);

		// ScrollPanel con contenido
		panelContenidoScroll = new JPanel();
		panelContenidoScroll.setLayout(new BoxLayout(panelContenidoScroll, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(panelContenidoScroll);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		
		JList<Curso> list = new JList<Curso>();
		list.setCellRenderer(new CursoCellRenderer());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Obtener los cursos disponibles
		// Esto debería ser implementado en el controlador
		List<Curso> cursosDisponibles = ControladorPDS.getUnicaInstancia().obtenerCursosLocales();
		DefaultListModel<Curso> modelo = new DefaultListModel<Curso>();
		
		if (cursosDisponibles != null) {
			for (Curso curso : cursosDisponibles) {
				modelo.addElement(curso);
			}
		} else {
			// Datos de ejemplo para visualización
			// Esto debería eliminarse cuando se tenga la función real
			modelo.addElement(new Curso("Curso de Programación Java", "Aprende Java desde cero", "Programación"));
			modelo.addElement(new Curso("Curso de Diseño UX/UI", "Diseño de interfaces de usuario", "Diseño"));
			modelo.addElement(new Curso("Curso de Matemáticas", "Matemáticas para ingeniería", "Ciencias"));
		}
		
		list.setModel(modelo);
		list.setBackground(new Color(248, 248, 248));
		list.setFont(new Font("Arial", Font.PLAIN, 14));
		
		panelContenidoScroll.add(list);
		scrollPane.setPreferredSize(new Dimension(500, 400));
		panelCentroContenedor.add(scrollPane, BorderLayout.CENTER);

		// Panel inferior con botones centrados y grandes
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		panelSur.setBackground(new Color(240, 240, 240));
		panelSur.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
		getContentPane().add(panelSur, BorderLayout.SOUTH);

		JButton btnEmpezar = new JButton("Empezar");

		btnEmpezar.setFont(new Font("Arial", Font.BOLD, 14));
		btnEmpezar.setBackground(new Color(76, 175, 80));
		btnEmpezar.setForeground(Color.WHITE);
		
		JButton btnCancelar = new JButton("Volver");
		btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
		btnCancelar.setBackground(new Color(220, 220, 220));
		btnCancelar.setForeground(Color.BLACK);

		// Tamaño más grande para los botones
		Dimension botonDimension = new Dimension(120, 40);
		btnEmpezar.setPreferredSize(botonDimension);
		btnCancelar.setPreferredSize(botonDimension);

		// Agregar acciones a los botones
		btnEmpezar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Curso cursoSeleccionado = list.getSelectedValue();
				if (cursoSeleccionado != null) {
					// Aquí se añadiría el curso a los cursos activos del usuario
					CursoEnMarcha cursoEnMarcha = ControladorPDS.getUnicaInstancia().iniciarCurso(cursoSeleccionado);
					FlashcardTipoA flash = new FlashcardTipoA(cursoEnMarcha, 0, 0);
					flash.setVisible(true);
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Por favor, seleccione un curso.", 
							"No hay selección", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
				ventanaPrincipal.setVisible(true);
			}
		});

		panelSur.add(btnEmpezar);
		panelSur.add(btnCancelar);
		
		// Centrar la ventana en la pantalla
		setLocationRelativeTo(null);
	}

	public void mostrarVentana() {
		setVisible(true);
	}
}