package vista;

import javax.swing.*;

import controlador.ControladorPDS;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaLogin extends JFrame {
	private JTextField userField;
	private JPasswordField passwordField;

	// Constructor que configura la ventana de login
	public VentanaLogin() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300); // Tamaño de la ventana de login
		setLocationRelativeTo(null); // Centrar la ventana en la pantalla
		setLayout(new BorderLayout(0, 0)); // Usar BorderLayout

		// Panel superior
		JPanel panelNorte = new JPanel();
		panelNorte.setBackground(new Color(240, 240, 240));
		add(panelNorte, BorderLayout.NORTH);

		JLabel lblTitulo = new JLabel("");
		lblTitulo.setIcon(new ImageIcon(getClass().getResource("/Recursos/EdulingoRedimensionadad.png")));
		lblTitulo.setForeground(Color.BLUE);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
		panelNorte.add(lblTitulo);

		// Panel central (formulario)
		JPanel panelCentral = new JPanel();
		add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new GridBagLayout());

		// Etiqueta usuario
		JLabel lblUsuario = new JLabel("Nombre de Usuario:");
		GridBagConstraints gbcUsuario = new GridBagConstraints();
		gbcUsuario.gridx = 0;
		gbcUsuario.gridy = 0;
		gbcUsuario.insets = new Insets(10, 10, 10, 10); // Espaciado
		panelCentral.add(lblUsuario, gbcUsuario);

		// Campo usuario
		userField = new JTextField(20);
		GridBagConstraints gbcUsuarioCampo = new GridBagConstraints();
		gbcUsuarioCampo.gridx = 1;
		gbcUsuarioCampo.gridy = 0;
		gbcUsuarioCampo.insets = new Insets(10, 10, 10, 10);
		panelCentral.add(userField, gbcUsuarioCampo);

		// Etiqueta contraseña
		JLabel lblContrasena = new JLabel("Contraseña:");
		GridBagConstraints gbcContrasena = new GridBagConstraints();
		gbcContrasena.gridx = 0;
		gbcContrasena.gridy = 1;
		gbcContrasena.insets = new Insets(10, 10, 10, 10);
		panelCentral.add(lblContrasena, gbcContrasena);

		// Campo contraseña
		passwordField = new JPasswordField(20);
		GridBagConstraints gbcContrasenaCampo = new GridBagConstraints();
		gbcContrasenaCampo.gridx = 1;
		gbcContrasenaCampo.gridy = 1;
		gbcContrasenaCampo.insets = new Insets(10, 10, 10, 10);
		panelCentral.add(passwordField, gbcContrasenaCampo);

		// Botonera inferior
		JPanel botonera = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		add(botonera, BorderLayout.SOUTH);
		JButton btnRegistro = new JButton("Registro");
		JButton btnAceptar = new JButton("Aceptar");

		// Cambiar tamaño y estilo de los botones
		Dimension btnSize = new Dimension(100, 35);
		btnAceptar.setPreferredSize(btnSize);
		btnRegistro.setPreferredSize(btnSize);

		// Estilo más bonito para los botones
		btnAceptar.setFont(new Font("Arial", Font.BOLD, 14));
		btnRegistro.setFont(new Font("Arial", Font.BOLD, 14));

		// Agregar botones a la botonera
		botonera.add(btnAceptar);
		botonera.add(btnRegistro);

		// Acción para el botón "Registro"
		btnRegistro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Crear la nueva ventana registro
				VentanaRegistro ventanaRegistro = new VentanaRegistro();
				ventanaRegistro.setVisible(true);

				dispose();
			}
		});

		// Acción para el botón "Aceptar"
		btnAceptar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Crear la nueva ventana principal
				boolean login = ControladorPDS.getUnicaInstancia().login(userField.getText(), String.valueOf(passwordField.getPassword()));
				if(login) {
					VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
					ventanaPrincipal.setVisible(true);
					dispose(); // Cierra la ventana de login
				} else {
		            JOptionPane.showMessageDialog(null, "Nombre de usuario o contraseña incorrectos.", "Error de Login", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
