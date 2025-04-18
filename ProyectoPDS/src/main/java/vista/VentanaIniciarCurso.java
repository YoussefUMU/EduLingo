package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import modelado.Curso;
import modelado.CursoEnMarcha;

public class VentanaIniciarCurso extends JFrame {
    private Point dragPoint;
    private JPanel mainPanel;
    private ButtonGroup estrategiaGroup;
    private JCheckBox chkSecuencial;
    private JCheckBox chkRepeticion;
    private JCheckBox chkAleatoria;
    private Curso cursoSeleccionado;
    
    public VentanaIniciarCurso(Curso curso) {
        this.cursoSeleccionado = curso;
        initialize();
    }
    
    public void initialize() {
        setTitle("Edulingo - Configurar Curso");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(650, 600));
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 650, 600, 15, 15));
        
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
        setContentPane(mainPanel);
        
        // Configurar el movimiento de ventana
        setupWindowMovement();
        
        // Panel superior con título y botón de cerrar
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setOpaque(false);
        panelNorte.setBorder(new EmptyBorder(15, 20, 10, 20));
        
        // Logo y título
        JPanel logoTitulo = new JPanel(new BorderLayout(10, 0));
        logoTitulo.setOpaque(false);
        
        // Opcionalmente añadir logo
        /*
        ImageIcon iconoLogo = new ImageIcon(getClass().getResource("/Recursos/EdulingoRedimensionadad.png"));
        JLabel lblLogo = new JLabel(iconoLogo);
        logoTitulo.add(lblLogo, BorderLayout.WEST);
        */
        
        JLabel lblTitulo = new JLabel("ÚLTIMO PASO ANTES DE EMPEZAR");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        logoTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        // Animación para el título
        lblTitulo.setForeground(new Color(255, 255, 255, 0)); // Inicialmente transparente
        Timer fadeInTimer = new Timer(40, new ActionListener() {
            float alpha = 0f;
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += 0.05f;
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    ((Timer)e.getSource()).stop();
                }
                lblTitulo.setForeground(new Color(255, 255, 255, (int)(alpha * 255)));
                lblTitulo.repaint();
            }
        });
        fadeInTimer.start();
        
        // Panel para botón de cerrar
        JPanel panelCerrar = new JPanel(new BorderLayout());
        panelCerrar.setOpaque(false);
        
        JButton btnCerrar = new JButton("×");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 20));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorder(null);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());
        
        panelCerrar.add(btnCerrar, BorderLayout.EAST);
        
        // Añadir componentes al panel norte
        panelNorte.add(logoTitulo, BorderLayout.CENTER);
        panelNorte.add(panelCerrar, BorderLayout.NORTH);
        
        // Si se ha seleccionado un curso, mostrar su nombre
        if (cursoSeleccionado != null) {
            JLabel lblCursoNombre = new JLabel("Curso: " + cursoSeleccionado.getNombre());
            lblCursoNombre.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblCursoNombre.setForeground(new Color(255, 255, 200));
            lblCursoNombre.setHorizontalAlignment(SwingConstants.CENTER);
            panelNorte.add(lblCursoNombre, BorderLayout.SOUTH);
        }
        
        add(panelNorte, BorderLayout.NORTH);
        
        // Panel central con las opciones
        JPanel panelCentral = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Panel semi-transparente
                g2d.setColor(new Color(255, 255, 255, 70));
                g2d.fillRoundRect(20, 20, getWidth() - 40, getHeight() - 40, 20, 20);
                
                // Borde suave
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.drawRoundRect(20, 20, getWidth() - 40, getHeight() - 40, 20, 20);
            }
        };
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new GridBagLayout());
        add(panelCentral, BorderLayout.CENTER);
        
        // Configurar GridBagLayout para panel central
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 5, 20);
        
        // Etiqueta de selección de estrategia
        JLabel lblSeleccionEstrategia = new JLabel("Seleccione su estrategia de aprendizaje:");
        lblSeleccionEstrategia.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSeleccionEstrategia.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panelCentral.add(lblSeleccionEstrategia, gbc);
        
        // Espacio
        gbc.gridy++;
        panelCentral.add(Box.createVerticalStrut(10), gbc);
        
        // Grupo de botones para las estrategias
        estrategiaGroup = new ButtonGroup();
        
        // 1. Opción Secuencial
        gbc.gridy++;
        chkSecuencial = createStyledCheckbox("Secuencial", Color.WHITE);
        chkSecuencial.setFont(new Font("Segoe UI", Font.BOLD, 15));
        gbc.anchor = GridBagConstraints.WEST;
        panelCentral.add(chkSecuencial, gbc);
        estrategiaGroup.add(chkSecuencial);
        
        // Descripción secuencial
        gbc.gridy++;
        JTextPane txtSecuencial = createStyledTextPane(
            "Con la estrategia secuencial, estudiarás en el orden en que las lecciones están dispuestas.", 
            new Color(240, 240, 240));
        panelCentral.add(txtSecuencial, gbc);
        
        // Espacio
        gbc.gridy++;
        panelCentral.add(Box.createVerticalStrut(10), gbc);
        
        // 2. Opción Repetición espaciada
        gbc.gridy++;
        chkRepeticion = createStyledCheckbox("Repetición espaciada", Color.WHITE);
        chkRepeticion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panelCentral.add(chkRepeticion, gbc);
        estrategiaGroup.add(chkRepeticion);
        
        // Descripción repetición
        gbc.gridy++;
        JTextPane txtRepeticion = createStyledTextPane(
            "Con esta técnica, en lugar de estudiar todo de una vez, repasarías la misma información en intervalos de tiempo, aumentando el espacio entre cada repaso. Esto ayuda a consolidar la memoria a largo plazo.", 
            new Color(240, 240, 240));
        panelCentral.add(txtRepeticion, gbc);
        
        // Espacio
        gbc.gridy++;
        panelCentral.add(Box.createVerticalStrut(10), gbc);
        
        // 3. Opción Aleatoria
        gbc.gridy++;
        chkAleatoria = createStyledCheckbox("Aleatoria", Color.WHITE);
        chkAleatoria.setFont(new Font("Segoe UI", Font.BOLD, 15));
        panelCentral.add(chkAleatoria, gbc);
        estrategiaGroup.add(chkAleatoria);
        
        // Descripción aleatoria
        gbc.gridy++;
        JTextPane txtAleatoria = createStyledTextPane(
            "Con esta estrategia, el orden en que estudias los elementos no sigue una secuencia fija. En lugar de estudiar un tema de principio a fin, podrías mezclar las lecciones o incluso cambiar entre diferentes temas en el mismo día. Esto puede ayudarte a desarrollar flexibilidad mental.", 
            new Color(240, 240, 240));
        panelCentral.add(txtAleatoria, gbc);
        
        // Seleccionar por defecto la primera opción
        chkSecuencial.setSelected(true);
        
        // Panel inferior con botones
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Botones
        JButton btnIniciar = createAnimatedButton("Iniciar Curso", new Color(76, 175, 80));
        JButton btnCancelar = createAnimatedButton("Cancelar", new Color(220, 50, 50));

        Dimension buttonSize = new Dimension(160, 45);
        btnIniciar.setPreferredSize(buttonSize);
        btnCancelar.setPreferredSize(buttonSize);

        // Contenedor centrado
        JPanel contenedorBotones = new JPanel(new GridBagLayout());
        contenedorBotones.setOpaque(false);
        contenedorBotones.add(btnIniciar);
        contenedorBotones.add(Box.createHorizontalStrut(20));
        contenedorBotones.add(btnCancelar);

        panelSur.add(contenedorBotones, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        // Acción Iniciar Curso
        btnIniciar.addActionListener(e -> {
            String estrategia = "Secuencial";
            if (chkRepeticion.isSelected()) estrategia = "Repetición espaciada";
            else if (chkAleatoria.isSelected()) estrategia = "Aleatoria";

            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de comenzar el curso con estrategia: " + estrategia + "?", 
                "Confirmar inicio", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                setVisible(false);
                dispose();

                CursoEnMarcha cursoEnMarcha = controlador.ControladorPDS.getUnicaInstancia().iniciarCurso(cursoSeleccionado);
                FlashCardTipoA flashcard = new FlashCardTipoA(cursoEnMarcha, 0, 0); // Parámetros ejemplo
                flashcard.setVisible(true);
            }
        });

        // Acción Cancelar
        btnCancelar.addActionListener(e -> {
            dispose();
        });

        // Mostrar con animación suave
        setOpacity(0.0f);
        setLocationRelativeTo(null); // Centrar
        setVisible(true);

        Timer fadeIn = new Timer(10, new ActionListener() {
            float opacity = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    ((Timer) e.getSource()).stop();
                }
                setOpacity(Math.min(opacity, 1.0f));
            }
        });
        fadeIn.start();
    }
    private JCheckBox createStyledCheckbox(String text, Color fgColor) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setOpaque(false);
        checkBox.setForeground(fgColor);
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        checkBox.setFocusPainted(false);
        checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return checkBox;
    }

    private JTextPane createStyledTextPane(String text, Color color) {
        JTextPane textPane = new JTextPane();
        textPane.setText(text);
        textPane.setEditable(false);
        textPane.setOpaque(false);
        textPane.setFocusable(false);
        textPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textPane.setForeground(color);
        textPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return textPane;
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
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupWindowMovement() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragPoint = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point current = getLocation();
                setLocation(current.x + e.getX() - dragPoint.x,
                            current.y + e.getY() - dragPoint.y);
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }
}