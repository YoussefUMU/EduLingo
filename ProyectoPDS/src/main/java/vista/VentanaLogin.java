package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JPanel {
    private JTextField textField;
    private JPasswordField passwordField;

    public VentanaLogin() {
        setLayout(new BorderLayout(0, 0));

        // Panel superior
        JPanel panelNorte = new JPanel();
        panelNorte.setBackground(new Color(240, 240, 240));
        add(panelNorte, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel("");
        lblTitulo.setIcon(new ImageIcon(VentanaLogin.class.getResource("/Recursos/EdulingoRedimensionadad.png")));
        lblTitulo.setForeground(Color.BLUE);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        panelNorte.add(lblTitulo);

        // Botonera inferior
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        add(botonera, BorderLayout.SOUTH);

        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnRegistro = new JButton("Registro");

        Dimension btnSize = new Dimension(100, 40);
        btnAceptar.setPreferredSize(btnSize);
        btnCancelar.setPreferredSize(btnSize);
        btnRegistro.setPreferredSize(btnSize);

        botonera.add(btnAceptar);
        botonera.add(btnCancelar);
        botonera.add(btnRegistro);

        // Panel central (formulario)
        JPanel panelCentral = new JPanel();
        add(panelCentral, BorderLayout.CENTER);
        GridBagLayout gbl_panelCentral = new GridBagLayout();
        gbl_panelCentral.columnWidths = new int[]{150, 250, 0};
        gbl_panelCentral.rowHeights = new int[]{30, 30, 30, 30};
        gbl_panelCentral.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panelCentral.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        panelCentral.setLayout(gbl_panelCentral);

        // Etiqueta usuario
        JLabel lblUsuario = new JLabel("Nombre de Usuario:");
        lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
        GridBagConstraints gbc_lblUsuario = new GridBagConstraints();
        gbc_lblUsuario.anchor = GridBagConstraints.EAST;
        gbc_lblUsuario.insets = new Insets(10, 10, 10, 10);
        gbc_lblUsuario.gridx = 0;
        gbc_lblUsuario.gridy = 0;
        panelCentral.add(lblUsuario, gbc_lblUsuario);

        // Campo usuario
        textField = new JTextField();
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.insets = new Insets(10, 0, 10, 10);
        gbc_textField.gridx = 1;
        gbc_textField.gridy = 0;
        panelCentral.add(textField, gbc_textField);
        textField.setColumns(20);

        // Etiqueta contraseña
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Tahoma", Font.PLAIN, 14));
        GridBagConstraints gbc_lblContrasena = new GridBagConstraints();
        gbc_lblContrasena.anchor = GridBagConstraints.EAST;
        gbc_lblContrasena.insets = new Insets(10, 10, 10, 10);
        gbc_lblContrasena.gridx = 0;
        gbc_lblContrasena.gridy = 1;
        panelCentral.add(lblContrasena, gbc_lblContrasena);

        // Campo contraseña
        passwordField = new JPasswordField();
        GridBagConstraints gbc_passwordField = new GridBagConstraints();
        gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
        gbc_passwordField.insets = new Insets(10, 0, 10, 10);
        gbc_passwordField.gridx = 1;
        gbc_passwordField.gridy = 1;
        panelCentral.add(passwordField, gbc_passwordField);
        passwordField.setColumns(20);
    }

    // Getters si los necesitas
    public JTextField getTextField() {
        return textField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }
}
