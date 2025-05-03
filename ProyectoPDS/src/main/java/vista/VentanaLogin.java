package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controlador.ControladorPDS;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class VentanaLogin extends JFrame {
    private JTextField userField;
    private JPasswordField passwordField;
    private JToggleButton btnShowPassword;
    private JPanel mainPanel;

    // Constructor que configura la ventana de login
    public VentanaLogin() {
        setTitle("Login - Edulingo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 500); // Tamaño aumentado para un diseño más espacioso
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setUndecorated(true); // Quitamos bordes de ventana para un diseño más moderno
        setShape(new RoundRectangle2D.Double(0, 0, 450, 500, 15, 15)); // Bordes redondeados

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
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(0, 0, 0, 50));  // Fondo oscuro con transparencia

        setContentPane(mainPanel);

        // Panel superior con logo y título
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setOpaque(false);
        panelNorte.setBorder(new EmptyBorder(25, 25, 10, 25));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("");
        lblTitulo.setIcon(new ImageIcon(getClass().getResource("/recursos/EdulingoRedimensionadad.png")));
        logoPanel.add(lblTitulo);
        panelNorte.add(logoPanel, BorderLayout.CENTER);

        JLabel lblTagline = new JLabel("Aprendizaje a tu ritmo");
        lblTagline.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        lblTagline.setForeground(Color.WHITE);
        lblTagline.setHorizontalAlignment(SwingConstants.CENTER);
        
        Timer fadeInTimer = new Timer(60, new ActionListener() {
            float alpha = 0f;  // Comienza con opacidad 0

            @Override
            public void actionPerformed(ActionEvent e) {
                if (alpha < 1.0f) {
                    alpha += 0.05f;  // Aumenta la opacidad en cada intervalo
                    lblTagline.setForeground(new Color(255, 255, 255, (int) (alpha * 255))); // Modifica la opacidad
                } else {
                    ((Timer) e.getSource()).stop();  // Detiene el timer cuando la animación finaliza
                }
            }
        });
        fadeInTimer.start();

        panelNorte.add(lblTagline, BorderLayout.SOUTH);

        add(panelNorte, BorderLayout.NORTH);

        // Panel central (formulario)
        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new GridBagLayout());
        add(panelCentral, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 15, 7, 15);

        // Panel para el campo de usuario con icono
        JPanel panelUsuario = new JPanel(new BorderLayout(10, 0));
        panelUsuario.setOpaque(false);
        JLabel iconoUsuario = new JLabel("\uD83D\uDC64"); // Emoji usuario
        iconoUsuario.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
        iconoUsuario.setForeground(Color.WHITE);
        panelUsuario.add(iconoUsuario, BorderLayout.WEST);

        userField = new JTextField(15);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        panelUsuario.add(userField, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        Font nuevaFuente = new Font("Segoe UI", Font.ITALIC, 18);
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(nuevaFuente);
        lblUsuario.setForeground(Color.WHITE); // Asegúrate de que el texto sea visible sobre el fondo oscuro
        panelCentral.add(lblUsuario, gbc);
        
        gbc.gridy = 1;
        panelCentral.add(panelUsuario, gbc);

        // Panel para el campo de contraseña con icono y botón mostrar/ocultar
        JPanel panelPassword = new JPanel(new BorderLayout(10, 0));
        panelPassword.setOpaque(false);
        JLabel iconoPassword = new JLabel("\uD83D\uDD12"); // Emoji candado
        iconoPassword.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
        iconoPassword.setForeground(Color.WHITE);
        panelPassword.add(iconoPassword, BorderLayout.WEST);

        passwordField = new JPasswordField(15);
        passwordField.setEchoChar('•');
        passwordField.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        panelPassword.add(passwordField, BorderLayout.CENTER);

        // Botón para mostrar/ocultar contraseña
        btnShowPassword = new JToggleButton("\uD83D\uDC41"); // Emoji ojo
        btnShowPassword.setFont(new Font("Arial Unicode MS", Font.PLAIN, 14));
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
                    btnShowPassword.setText("\u274C"); // Emoji de cruz 
                } else {
                    passwordField.setEchoChar('•'); // Ocultar con puntos
                    btnShowPassword.setText("\uD83D\uDC41"); // Emoji ojo
                }
            }
        });
        panelPassword.add(btnShowPassword, BorderLayout.EAST);

        gbc.gridy = 2;
        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setFont(nuevaFuente);
        lblContraseña.setForeground(Color.WHITE);
        panelCentral.add(lblContraseña, gbc);
        
        gbc.gridy = 3;
        panelCentral.add(panelPassword, gbc);

        // Botones animados
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(30, 15, 7, 7);
        
        JButton btnAceptar = createAnimatedButton("Iniciar Sesión", new Color(76, 175, 80));
        panelCentral.add(btnAceptar, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(30, 7, 7, 15);
        JButton btnRegistro = createAnimatedButton("Registro", new Color(33, 150, 243));
        panelCentral.add(btnRegistro, gbc);

        // Acción para el botón "Registro"
        btnRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Animación de transición
                Timer timer = new Timer(10, new ActionListener() {
                    float alpha = 1.0f;
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        alpha -= 0.05f;
                        if (alpha <= 0) {
                            ((Timer)e.getSource()).stop();
                            // Crear la nueva ventana registro
                            VentanaRegistro ventanaRegistro = new VentanaRegistro();
                            ventanaRegistro.setVisible(true);
                            dispose();
                        }
                        mainPanel.setOpaque(false);
                        mainPanel.setBackground(new Color(0, 0, 0, Math.max(0, Math.min(1, alpha))));
                        mainPanel.repaint();
                    }
                });
                timer.start();
            }
        });

        // Acción para el botón "Aceptar"
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Validación
                if (userField.getText().isEmpty() || String.valueOf(passwordField.getPassword()).isEmpty()) {
                    JOptionPane.showMessageDialog(null, 
                            "Por favor, complete todos los campos", 
                            "Campos vacíos", 
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                boolean login = ControladorPDS.getUnicaInstancia().login(
                        userField.getText(), 
                        String.valueOf(passwordField.getPassword()));
                
                if(login) {
                    // Animación de transición
                    Timer timer = new Timer(10, new ActionListener() {
                        float alpha = 1.0f;
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            alpha -= 0.05f;
                            if (alpha <= 0) {
                                ((Timer)e.getSource()).stop();
                                VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
                                ventanaPrincipal.setVisible(true);
                                dispose(); // Cierra la ventana de login
                            }
                            mainPanel.setOpaque(false);
                            mainPanel.setBackground(new Color(0, 0, 0, Math.max(0, Math.min(1, alpha))));
                            mainPanel.repaint();
                        }
                    });
                    timer.start();
                } else {
                    // Animación de error
                    shakefield(passwordField);
                    JOptionPane.showMessageDialog(null, 
                            "Nombre de usuario o contraseña incorrectos.", 
                            "Error de Login", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

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
        panelNorte.add(panelSuperiorDerecha, BorderLayout.EAST); // Añadir el panel con el botón a la esquina derecha

        // Eventos para movimiento de la ventana
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
        
        // Eventos para campos de texto
        userField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                userField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(66, 133, 244), 2, true),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)));
            }
            @Override
            public void focusLost(FocusEvent e) {
                userField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)));
            }
        });
        
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(66, 133, 244), 2, true),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)));
            }
            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)));
            }
        });
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
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                FontMetrics metrics = g2.getFontMetrics(getFont());
                int x = (getWidth() - metrics.stringWidth(getText())) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    // Método para animar campos con error
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
}