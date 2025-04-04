package vista;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import modelado.Curso;
import modelado.CursoEnMarcha;
import controlador.ControladorPDS;

public class VentanaCursosEnMarcha extends JFrame {
	private JPanel panelContenidoScroll;

	public VentanaCursosEnMarcha() {
		initialize();
	}

	public void initialize() {
		setTitle("Edulingo - Mis Cursos");
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
		JLabel lblNewLabel = new JLabel("CONTINUE CON SUS CURSOS");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 20));
		panelNorte.add(lblNewLabel, BorderLayout.CENTER);

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
		
		// Obtener los cursos del usuario actual
		List<CursoEnMarcha> cursosUser = ControladorPDS.getUnicaInstancia().getSesionActual().getCursosActivos();
		DefaultListModel<Curso> modelo = new DefaultListModel<Curso>();
		
		if (cursosUser != null) {
			for (Curso curso : cursosUser) {
				modelo.addElement(curso);
			}
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

		JButton btnContinuar = new JButton("Continuar");
		btnContinuar.setFont(new Font("Arial", Font.BOLD, 14));
		btnContinuar.setBackground(new Color(33, 150, 243));
		btnContinuar.setForeground(Color.WHITE);
		
		JButton btnCancelar = new JButton("Volver");
		btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
		btnCancelar.setBackground(new Color(220, 220, 220));
		btnCancelar.setForeground(Color.BLACK);

		// Tamaño más grande para los botones
		Dimension botonDimension = new Dimension(120, 40);
		btnContinuar.setPreferredSize(botonDimension);
		btnCancelar.setPreferredSize(botonDimension);

		// Agregar acciones a los botones
		btnContinuar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Curso cursoSeleccionado = list.getSelectedValue();
				if (cursoSeleccionado != null) {
					// Aquí se abriría la ventana del curso seleccionado
					JOptionPane.showMessageDialog(null, "Abriendo curso: " + cursoSeleccionado.getNombre(), 
							"Curso Seleccionado", JOptionPane.INFORMATION_MESSAGE);
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

		panelSur.add(btnContinuar);
		panelSur.add(btnCancelar);
		
		// Centrar la ventana en la pantalla
		setLocationRelativeTo(null);
	}

	public void mostrarVentana() {
		setVisible(true);
	}
}