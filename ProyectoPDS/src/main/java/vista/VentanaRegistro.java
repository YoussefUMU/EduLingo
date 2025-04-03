package vista;

import controlador.ControladorPDS;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import modelado.Usuario;

public class VentanaRegistro extends JFrame {
    private JTextField textFieldNombre, textFieldApellidos, textFieldCorreo, textFieldUsuario;
    private JPasswordField passwordField1, passwordField2;
    private JSpinner spinnerCumpleaños;
    private ControladorPDS controlador;

    public VentanaRegistro() {
        controlador = ControladorPDS.getUnicaInstancia();

        // Ventana de registro
        setTitle("Registro de Usuario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600); // Tamaño mayor para mejorar la distribución
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20)); // Espaciado entre los componentes

        // Panel para el formulario
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridLayout(8, 2, 15, 15)); // Mayor espacio entre los componentes
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen adicional
        add(panelFormulario, BorderLayout.CENTER);

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14)); // Estilo de letra
        textFieldNombre = new JTextField();
        panelFormulario.add(lblNombre);
        panelFormulario.add(textFieldNombre);

        // Apellidos
        JLabel lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setFont(new Font("Arial", Font.BOLD, 14));
        textFieldApellidos = new JTextField();
        panelFormulario.add(lblApellidos);
        panelFormulario.add(textFieldApellidos);

        // Correo
        JLabel lblCorreo = new JLabel("Correo (UM):");
        lblCorreo.setFont(new Font("Arial", Font.BOLD, 14));
        textFieldCorreo = new JTextField();
        panelFormulario.add(lblCorreo);
        panelFormulario.add(textFieldCorreo);

        // Cumpleaños
        JLabel lblCumpleaños = new JLabel("Cumpleaños:");
        lblCumpleaños.setFont(new Font("Arial", Font.BOLD, 14));
        spinnerCumpleaños = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerCumpleaños, "dd/MM/yyyy");
        spinnerCumpleaños.setEditor(dateEditor);
        panelFormulario.add(lblCumpleaños);
        panelFormulario.add(spinnerCumpleaños);

        // Nombre de usuario
        JLabel lblUsuario = new JLabel("Nombre de usuario:");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        textFieldUsuario = new JTextField();
        panelFormulario.add(lblUsuario);
        panelFormulario.add(textFieldUsuario);

        // Contraseña
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(new Font("Arial", Font.BOLD, 14));
        passwordField1 = new JPasswordField();
        panelFormulario.add(lblContraseña);
        panelFormulario.add(passwordField1);

        // Confirmar contraseña
        JLabel lblConfirmarContraseña = new JLabel("Confirmar Contraseña:");
        lblConfirmarContraseña.setFont(new Font("Arial", Font.BOLD, 14));
        passwordField2 = new JPasswordField();
        panelFormulario.add(lblConfirmarContraseña);
        panelFormulario.add(passwordField2);

        // Panel para los botones
        JPanel panelBotonera = new JPanel();
        panelBotonera.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10)); // Mejor alineación de los botones
        add(panelBotonera, BorderLayout.SOUTH);

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setFont(new Font("Arial", Font.BOLD, 14));
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));

        // Estilo de los botones
        btnAceptar.setBackground(new Color(34, 139, 34)); // Verde
        btnAceptar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(220, 20, 60)); // Rojo
        btnCancelar.setForeground(Color.WHITE);

        // Acción de los botones
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textFieldNombre.getText();
                String apellidos = textFieldApellidos.getText();
                String correo = textFieldCorreo.getText();
                String usuario = textFieldUsuario.getText();
                String contraseña = new String(passwordField1.getPassword());
                String confirmarContraseña = new String(passwordField2.getPassword());
                Date cumpleaños = (Date) spinnerCumpleaños.getValue();

                if (validarCampos(nombre, apellidos, correo, usuario, contraseña, confirmarContraseña)) {
                    boolean registrado = controlador.registrarUsuario(usuario, contraseña, correo, usuario);
                    if (registrado) {
                        JOptionPane.showMessageDialog(null, "Usuario registrado con éxito.");
                        dispose(); // Cerrar la ventana de registro
                        new VentanaLogin().setVisible(true); // Volver a la ventana de login
                    } else {
                        JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe.");
                    }
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Cerrar la ventana de registro
                new VentanaLogin().setVisible(true); // Volver a la ventana de login
            }
        });

        panelBotonera.add(btnAceptar);
        panelBotonera.add(btnCancelar);
    }

    private boolean validarCampos(String nombre, String apellidos, String correo, String usuario, String contraseña, String confirmarContraseña) {
        if (nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || usuario.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return false;
        }

        if (!correo.endsWith("@um.es")) {
            JOptionPane.showMessageDialog(this, "El correo debe terminar en @um.es.");
            return false;
        }

        if (!contraseña.equals(confirmarContraseña)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
            return false;
        }

        return true;
    }

}
