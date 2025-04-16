package vista;

import controlador.ControladorPDS;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import modelado.Usuario;

public class VentanaRegistro extends JFrame {
    private JTextField textFieldNombre, textFieldApellidos, textFieldCorreo, textFieldUsuario;
    private JPasswordField passwordField1, passwordField2;
    private JSpinner spinnerCumpleaños;
    private JToggleButton btnShowPassword1, btnShowPassword2;
    private JPanel mainPanel;
    private ControladorPDS controlador;

    public VentanaRegistro() {
        controlador = ControladorPDS.getUnicaInstancia();

        // Configuración básica de la ventana
        setTitle("Registro - Edulingo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750); // Aumentado para mejor distribución
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 600, 750, 15, 15));

        // Panel principal con gradiente
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(66, 133, 244); // Azul claro
                Color color2 = new Color(15, 76, 129);  // Azul oscuro
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout(0, 20)); // Más espacio vertical entre secciones
        mainPanel.setBorder(new EmptyBorder(20, 40, 30, 40)); // Márgenes generales más amplios
        setContentPane(mainPanel);

        // Panel superior con logo y título
        JPanel panelNorte = new JPanel(new BorderLayout(0, 15)); // Más espacio entre logo y subtítulo
        panelNorte.setOpaque(false);
        
        // Logo centrado
        JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
        lblLogo.setIcon(new ImageIcon(getClass().getResource("/recursos/EdulingoRedimensionadad.png")));
        panelNorte.add(lblLogo, BorderLayout.CENTER);

        // Texto "Crea tu cuenta" con estilo
        JLabel lblTagline = new JLabel("Crea tu cuenta", SwingConstants.CENTER);
        lblTagline.setFont(new Font("Segoe UI", Font.ITALIC, 24)); // Fuente más grande
        lblTagline.setForeground(Color.WHITE);
        
        // Animación de aparición del subtítulo
        Timer fadeInTimer = new Timer(60, new ActionListener() {
            float alpha = 0f;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (alpha < 1.0f) {
                    alpha += 0.05f;
                    lblTagline.setForeground(new Color(255, 255, 255, (int) (alpha * 255)));
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        fadeInTimer.start();
        
        panelNorte.add(lblTagline, BorderLayout.SOUTH);

        
        
        
        JPanel panelSuperiorDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSuperiorDerecha.setOpaque(false);
        panelSuperiorDerecha.setBorder(new EmptyBorder(10, 0, 0, 10));

        JButton btnCerrar = new JButton("×");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 20));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorder(null);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> System.exit(0));

        panelSuperiorDerecha.add(btnCerrar);
        panelNorte.add(panelSuperiorDerecha, BorderLayout.NORTH); // Añadir el panel con el botón a la esquina derecha
        
        
        mainPanel.add(panelNorte, BorderLayout.NORTH);

        // Panel central con formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        mainPanel.add(panelFormulario, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0; // Permitir expansión horizontal

        // Estilo de fuente para etiquetas
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Color labelColor = new Color(240, 240, 255); // Blanco ligeramente azulado para mejor visibilidad
        
        // Organizar campos en dos columnas con gap entre ellos
        // Primera fila - Nombre y Apellidos
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 20); // Espacio a la derecha entre columnas
        gbc.gridwidth = 1;
        
        // Nombre - Columna izquierda
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(labelFont);
        lblNombre.setForeground(labelColor);
        panelFormulario.add(lblNombre, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 20); // Espacio inferior entre filas
        JPanel panelNombre = createFieldPanel("\uD83D\uDC64", "Ingrese su nombre");
        textFieldNombre = (JTextField) panelNombre.getComponent(1);
        panelFormulario.add(panelNombre, gbc);
        
        // Apellidos - Columna derecha
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 20, 5, 0); // Espacio a la izquierda
        JLabel lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setFont(labelFont);
        lblApellidos.setForeground(labelColor);
        panelFormulario.add(lblApellidos, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 25, 0);
        JPanel panelApellidos = createFieldPanel("\uD83D\uDC64", "Ingrese sus apellidos");
        textFieldApellidos = (JTextField) panelApellidos.getComponent(1);
        panelFormulario.add(panelApellidos, gbc);
        
        // Segunda fila - Correo y Fecha de nacimiento
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 20);
        JLabel lblCorreo = new JLabel("Correo (UM):");
        lblCorreo.setFont(labelFont);
        lblCorreo.setForeground(labelColor);
        panelFormulario.add(lblCorreo, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 25, 20);
        JPanel panelCorreo = createFieldPanel("\u2709", "ejemplo@um.es");
        textFieldCorreo = (JTextField) panelCorreo.getComponent(1);
        panelFormulario.add(panelCorreo, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 20, 5, 0);
        JLabel lblCumpleaños = new JLabel("Fecha de nacimiento:");
        lblCumpleaños.setFont(labelFont);
        lblCumpleaños.setForeground(labelColor);
        panelFormulario.add(lblCumpleaños, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 20, 25, 0);
        JPanel panelCumpleaños = createDatePanel();
        spinnerCumpleaños = (JSpinner) panelCumpleaños.getComponent(1);
        panelFormulario.add(panelCumpleaños, gbc);
        
        // Tercera fila - Usuario (centrado y ocupando ambas columnas)
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Ocupa ambas columnas
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel lblUsuario = new JLabel("Nombre de usuario:");
        lblUsuario.setFont(labelFont);
        lblUsuario.setForeground(labelColor);
        panelFormulario.add(lblUsuario, gbc);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 25, 0);
        JPanel panelUsuario = createFieldPanel("\uD83C\uDF93", "Cree un nombre de usuario único");
        textFieldUsuario = (JTextField) panelUsuario.getComponent(1);
        panelFormulario.add(panelUsuario, gbc);
        
        // Cuarta fila - Contraseñas
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 5, 20);
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(labelFont);
        lblContraseña.setForeground(labelColor);
        panelFormulario.add(lblContraseña, gbc);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 25, 20);
        JPanel panelPassword1 = createPasswordPanel();
        passwordField1 = (JPasswordField) panelPassword1.getComponent(1);
        btnShowPassword1 = (JToggleButton) panelPassword1.getComponent(2);
        panelFormulario.add(panelPassword1, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 20, 5, 0);
        JLabel lblConfirmarContraseña = new JLabel("Confirmar Contraseña:");
        lblConfirmarContraseña.setFont(labelFont);
        lblConfirmarContraseña.setForeground(labelColor);
        panelFormulario.add(lblConfirmarContraseña, gbc);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 20, 25, 0);
        JPanel panelPassword2 = createPasswordPanel();
        passwordField2 = (JPasswordField) panelPassword2.getComponent(1);
        btnShowPassword2 = (JToggleButton) panelPassword2.getComponent(2);
        panelFormulario.add(panelPassword2, gbc);

        // Panel para los botones
        JPanel panelBotonera = new JPanel();
        panelBotonera.setOpaque(false);
        panelBotonera.setLayout(new GridLayout(1, 2, 40, 0)); // 2 columnas con espacio entre ellas
        panelBotonera.setBorder(new EmptyBorder(10, 60, 0, 60)); // Margen en los lados
        mainPanel.add(panelBotonera, BorderLayout.SOUTH);

        // Botones más grandes y con colores distintos
        JButton btnRegistrarme = createAnimatedButton("Registrarme", new Color(76, 175, 80));
        JButton btnVolver = createAnimatedButton("Volver", new Color(33, 150, 243));
        
        // Dimensiones mayores para los botones
        Dimension buttonSize = new Dimension(0, 50); // Altura fija, ancho ajustable
        btnRegistrarme.setPreferredSize(buttonSize);
        btnVolver.setPreferredSize(buttonSize);

        // Acciones de los botones
        btnRegistrarme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = textFieldNombre.getText();
                String apellidos = textFieldApellidos.getText();
                String correo = textFieldCorreo.getText();
                String usuario = textFieldUsuario.getText();
                String contraseña = new String(passwordField1.getPassword());
                String confirmarContraseña = new String(passwordField2.getPassword());
                LocalDate cumpleaños = ((Date) spinnerCumpleaños.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (validarCampos(nombre, apellidos, correo, usuario, contraseña, confirmarContraseña)) {
                    boolean registrado = controlador.registrarUsuario(nombre + " " + apellidos, contraseña, correo, usuario, cumpleaños);
                    if (registrado) {
                        JOptionPane.showMessageDialog(null, "Usuario registrado con éxito.");
                        transicionAVentanaLogin();
                    } else {
                        shakefield(textFieldUsuario);
                        JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe.", 
                                "Error de Registro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnVolver.addActionListener(e -> transicionAVentanaLogin());

        panelBotonera.add(btnRegistrarme);
        panelBotonera.add(btnVolver);
        
        // Eventos para movimiento de la ventana
        enableWindowDrag();
    }
    
    private JPanel createFieldPanel(String emoji, String placeholder) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel icono = new JLabel(emoji);
        icono.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
        icono.setForeground(Color.WHITE);
        panel.add(icono, BorderLayout.WEST);
        
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16)); // Fuente más grande
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12))); // Padding más grande
        
        // Efecto focus
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(66, 133, 244), 2, true),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            }
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            }
        });
        
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createDatePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel icono = new JLabel("\uD83D\uDCC5"); // Emoji calendario
        icono.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
        icono.setForeground(Color.WHITE);
        panel.add(icono, BorderLayout.WEST);
        
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinner, "dd/MM/yyyy");
        spinner.setEditor(dateEditor);
        spinner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        // Efecto focus
        spinner.getEditor().getComponent(0).addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                spinner.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(66, 133, 244), 2, true),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            }
            @Override
            public void focusLost(FocusEvent e) {
                spinner.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            }
        });
        
        panel.add(spinner, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel icono = new JLabel("\uD83D\uDD12"); // Emoji candado
        icono.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
        icono.setForeground(Color.WHITE);
        panel.add(icono, BorderLayout.WEST);
        
        JPasswordField passwordField = new JPasswordField();
        passwordField.setEchoChar('•');
        passwordField.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16)); // Fuente más grande
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12))); // Padding más grande
                
        // Efecto focus
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(66, 133, 244), 2, true),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            }
            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                        BorderFactory.createEmptyBorder(10, 12, 10, 12)));
            }
        });
        
        panel.add(passwordField, BorderLayout.CENTER);
        
        // Botón para mostrar/ocultar contraseña
        JToggleButton btnShowPassword = new JToggleButton("\uD83D\uDC41"); // Emoji ojo
        btnShowPassword.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        btnShowPassword.setFocusPainted(false);
        btnShowPassword.setContentAreaFilled(false);
        btnShowPassword.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        btnShowPassword.setForeground(Color.WHITE);
        btnShowPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnShowPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnShowPassword.isSelected()) {
                    passwordField.setEchoChar((char) 0); // Mostrar texto
                    btnShowPassword.setText("\u274C"); // Emoji de cruz (❌)
                } else {
                    passwordField.setEchoChar('•'); // Ocultar con puntos
                    btnShowPassword.setText("\uD83D\uDC41"); // Emoji ojo
                }
            }
        });
        panel.add(btnShowPassword, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton createAnimatedButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color buttonColor = getModel().isPressed() ? baseColor.darker() : 
                                   getModel().isRollover() ? baseColor.brighter() : baseColor;
                
                g2.setColor(buttonColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Bordes más redondeados
                
                FontMetrics metrics = g2.getFontMetrics(getFont());
                int x = (getWidth() - metrics.stringWidth(getText())) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Fuente más grande
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void enableWindowDrag() {
        addMouseMotionListener(new MouseMotionAdapter() {
            private Point point = new Point();
            
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                point.x = e.getX();
                point.y = e.getY();
            }
        });
    }
    
    private void transicionAVentanaLogin() {
        // Animación de transición
        Timer timer = new Timer(10, new ActionListener() {
            float alpha = 1.0f;
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha -= 0.05f;
                if (alpha <= 0) {
                    ((Timer)e.getSource()).stop();
                    dispose(); // Cerrar la ventana de registro
                    new VentanaLogin().setVisible(true); // Volver a la ventana de login
                }
                mainPanel.setOpaque(false);
                mainPanel.setBackground(new Color(0, 0, 0, Math.max(0, Math.min(1, alpha))));
                mainPanel.repaint();
            }
        });
        timer.start();
    }
    
    private void shakefield(JComponent field) {
        final Point point = field.getLocation();
        final int delay = 50;
        final int distance = 10;
        
        Runnable r = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    try {
                        field.setLocation(point.x + distance, point.y);
                        Thread.sleep(delay);
                        field.setLocation(point.x - distance, point.y);
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                field.setLocation(point);
            }
        };
        
        Thread t = new Thread(r);
        t.start();
    }

    private boolean validarCampos(String nombre, String apellidos, String correo, String usuario, String contraseña, String confirmarContraseña) {
        if (nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || usuario.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!correo.endsWith("@um.es")) {
            shakefield(textFieldCorreo);
            JOptionPane.showMessageDialog(this, "El correo debe terminar en @um.es.", "Formato de correo incorrecto", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (contraseña.length() < 6) {
            shakefield(passwordField1);
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres.", "Contraseña débil", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!contraseña.equals(confirmarContraseña)) {
            shakefield(passwordField2);
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error de verificación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
}