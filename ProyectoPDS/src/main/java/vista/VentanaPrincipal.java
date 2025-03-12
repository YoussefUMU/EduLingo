package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        // Configuración de la ventana principal
        setTitle("EduLingo - Aprende Jugando");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con imagen de fondo
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/resources/background.jpg"); // Ruta de la imagen de fondo
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BorderLayout());

        // Título
        JLabel title = new JLabel("¡Bienvenido a EduLingo!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Botones estilizados
        JButton btnStart = createStyledButton("📚 Iniciar Curso");
        JButton btnCourses = createStyledButton("📂 Mis Cursos");
        JButton btnSettings = createStyledButton("⚙️ Configuración");
        JButton btnExit = createStyledButton("🚪 Salir");

        // Añadir botones al panel
        buttonPanel.add(btnStart);
        buttonPanel.add(btnCourses);
        buttonPanel.add(btnSettings);
        buttonPanel.add(btnExit);

        // Acción de los botones
        btnStart.addActionListener(e -> JOptionPane.showMessageDialog(this, "¡Iniciando curso!"));
        btnCourses.addActionListener(e -> JOptionPane.showMessageDialog(this, "Mostrando tus cursos."));
        btnSettings.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abriendo configuración."));
        btnExit.addActionListener(e -> System.exit(0));

        // Agregar los botones en el centro del panel
        panel.add(buttonPanel, BorderLayout.CENTER);

        // Configuración final
        add(panel);
        setVisible(true);
    }

    // Método para crear botones estilizados
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 144, 255));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
