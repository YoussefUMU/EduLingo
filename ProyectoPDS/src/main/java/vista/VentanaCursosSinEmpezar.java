package vista;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import modelado.Curso;

public class VentanaCursosSinEmpezar {
	private JFrame panelIncial;
	private JPanel panelContenidoScroll;

	public VentanaCursosSinEmpezar() {
		initialize();
	}

	public void initialize() {
		panelIncial = new JFrame();
		panelIncial.setBackground(new Color(240, 240, 240));
		panelIncial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		panelIncial.setLocationByPlatform(true);
		panelIncial.setSize(new Dimension(600, 600));
		panelIncial.getContentPane().setBackground(new Color(240, 240, 240));
		panelIncial.getContentPane().setLayout(new BorderLayout(0, 0));

		// Panel superior
		JPanel panelNorte = new JPanel(new BorderLayout());
		panelNorte.setBackground(new Color(240, 240, 240));
		panelIncial.getContentPane().add(panelNorte, BorderLayout.NORTH);

		// Etiqueta de título
		JLabel lblNewLabel = new JLabel("CURSOS DISPONIBLES");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrado horizontal
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
		panelNorte.add(lblNewLabel, BorderLayout.NORTH); // Lo colocamos arriba

		// Botón a la derecha, pero en un subpanel separado
		JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelBoton.setBackground(new Color(240, 240, 240));
		JButton btnNewButton = new JButton("Añadir Curso");
		btnNewButton.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 11));
		panelBoton.add(btnNewButton);
		panelNorte.add(panelBoton, BorderLayout.SOUTH);

		// Panel intermedio para centrar el scroll
		JPanel panelCentroContenedor = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20)); // Espaciado vertical
		panelCentroContenedor.setBackground(new Color(240, 240, 240));
		panelIncial.getContentPane().add(panelCentroContenedor, BorderLayout.CENTER);

		// ScrollPanel con contenido
		panelContenidoScroll = new JPanel();
		panelContenidoScroll.setLayout(new BoxLayout(panelContenidoScroll, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(panelContenidoScroll);
		
		JList<Curso> list = new JList<Curso>();
		list.setCellRenderer(new CursoCellRenderer());
		//TO DO: Crear funcion en el controlador para obtener los cursos.
		List<Curso> CursosUser = null;
		DefaultListModel<Curso> Modelo = new DefaultListModel<Curso>();
		for (Curso curso : CursosUser) {
			Modelo.addElement(curso);
		}
		list.setModel(Modelo);
		panelContenidoScroll.add(list);
		scrollPane.setPreferredSize(new Dimension(340, 420)); // Tamaño limitado, centrado
		panelCentroContenedor.add(scrollPane);

		// Panel inferior con botones centrados y grandes
		JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Centrados, con espacio
		panelSur.setBackground(new Color(240, 240, 240));
		panelIncial.getContentPane().add(panelSur, BorderLayout.SOUTH);

		JButton btnEmpezar = new JButton("Empezar");
		btnEmpezar.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 11));
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 11));

		// Tamaño más grande para los botones
		Dimension botonDimension = new Dimension(120, 40);
		btnEmpezar.setPreferredSize(botonDimension);
		btnCancelar.setPreferredSize(botonDimension);

		panelSur.add(btnEmpezar);
		panelSur.add(btnCancelar);
	}

	public void mostrarVentana() {
		panelIncial.setVisible(true);
	}

}
