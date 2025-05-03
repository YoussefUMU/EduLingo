package vista;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import modelado.Curso;
import modelado.CursoEnMarcha;
import modelado.EstrategiaAleatoria;
import modelado.EstrategiaSecuencial;
import controlador.ControladorPDS;

public class VentanaCursosEnMarcha extends JFrame {
    private JPanel panelContenidoScroll;
    private JPanel mainPanel;
    private Point dragPoint;

    public VentanaCursosEnMarcha() {
        initialize();
    }

    public void initialize() {
        setTitle("Edulingo - Mis Cursos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(650, 600));
        setUndecorated(true); // Quitamos bordes de ventana para un diseño más moderno
        setShape(new RoundRectangle2D.Double(0, 0, 650, 600, 15, 15)); // Bordes redondeados
        
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
        mainPanel.setLayout(new BorderLayout(0, 0));
        setContentPane(mainPanel);

        // Configurar el movimiento de ventana
        setupWindowMovement();

        // Panel superior
        JPanel panelNorte = new JPanel(new BorderLayout(10, 10));
        panelNorte.setOpaque(false);
        panelNorte.setBorder(new EmptyBorder(15, 15, 10, 15));
        getContentPane().add(panelNorte, BorderLayout.NORTH);
        
        // Panel superior derecha para botón cerrar
        JPanel panelBotonCerrar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonCerrar.setOpaque(false);
        
        JButton btnCerrar = new JButton("×");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 20));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBorder(null);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotonCerrar.add(btnCerrar);
        panelNorte.add(panelBotonCerrar, BorderLayout.EAST);

        // Panel izquierdo para logo
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLogo.setOpaque(false);
        
        ImageIcon iconoLogo = new ImageIcon(getClass().getResource("/Recursos/EdulingoRedimensionadad.png"));
        JLabel lblLogo = new JLabel(iconoLogo);
        panelLogo.add(lblLogo);
        
        panelNorte.add(panelLogo, BorderLayout.WEST);

        // Etiqueta de título con animación de aparición gradual
        JLabel lblNewLabel = new JLabel("CONTINUE CON SUS CURSOS");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setForeground(new Color(255, 255, 255, 0)); // Inicialmente transparente
        lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelNorte.add(lblNewLabel, BorderLayout.CENTER);
        
        // Animación para el título
        Timer fadeInTimer = new Timer(40, new ActionListener() {
            float alpha = 0f;
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += 0.05f;
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    ((Timer)e.getSource()).stop();
                }
                lblNewLabel.setForeground(new Color(255, 255, 255, (int)(alpha * 255)));
                lblNewLabel.repaint();
            }
        });
        fadeInTimer.start();

        // Panel intermedio para centrar el scroll
        JPanel panelCentroContenedor = new JPanel(new BorderLayout());
        panelCentroContenedor.setOpaque(false);
        panelCentroContenedor.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        getContentPane().add(panelCentroContenedor, BorderLayout.CENTER);

        // ScrollPanel con contenido
        panelContenidoScroll = new JPanel();
        panelContenidoScroll.setLayout(new BoxLayout(panelContenidoScroll, BoxLayout.Y_AXIS));
        panelContenidoScroll.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(panelContenidoScroll);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200, 80)));
        
        JList<CursoEnMarcha> list = new JList<CursoEnMarcha>();
        list.setCellRenderer(new ModernCursoCellRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBackground(new Color(248, 248, 248, 180));
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Obtener los cursos del usuario actual
        List<CursoEnMarcha> cursosUser = ControladorPDS.getUnicaInstancia().getCursosActivosSesionActual();
        DefaultListModel<CursoEnMarcha> modelo = new DefaultListModel<CursoEnMarcha>();
        
        if (cursosUser != null) {
            for (CursoEnMarcha curso : cursosUser) {
                modelo.addElement(curso);
            }
        }
        
        list.setModel(modelo);
        
        panelContenidoScroll.add(list);
        scrollPane.setPreferredSize(new Dimension(550, 400));
        panelCentroContenedor.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones centrados y con estilo moderno
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        getContentPane().add(panelSur, BorderLayout.SOUTH);

        JButton btnContinuar = createStyledButton("Continuar", new Color(33, 150, 243));
        JButton btnCancelar = createStyledButton("Volver", new Color(220, 220, 220));

        // Agregar acciones a los botones
        btnContinuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CursoEnMarcha cursoSeleccionado = list.getSelectedValue();
                if (cursoSeleccionado != null) {
                    // Usamos un timer para aplicar la animación de desvanecimiento
                    final Timer timer = new Timer(10, new ActionListener() {
                        float alpha = 1.0f;
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            alpha -= 0.05f;
                            if (alpha <= 0) {
                                // Para la animación y cerramos la ventana actual
                                ((Timer)e.getSource()).stop();
                                dispose();

                                // Creamos la flashcard usando el adaptador con los índices actuales
                                JFrame flashCard = AdaptadorPreguntas.crearFlashCard(
                                        cursoSeleccionado,
                                        cursoSeleccionado.getBloqueActualIndex(),
                                        cursoSeleccionado.getPreguntaActualIndex()
                                );

                                if (flashCard != null) {
                                    flashCard.setVisible(true);
                                } else {
                                    // En caso de error, se muestra la ventana principal
                                    new VentanaPrincipal().setVisible(true);
                                }
                            }
                            setOpacity(Math.max(0, alpha));
                        }
                    });
                    timer.start();
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

    private void setupWindowMovement() {
        mainPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dragPoint = e.getPoint();
            }
        });
        
        mainPanel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getLocationOnScreen();
                setLocation(currentPoint.x - dragPoint.x, currentPoint.y - dragPoint.y);
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(bgColor.equals(new Color(220, 220, 220)) ? Color.BLACK : Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 40));
        
        return button;
    }

    public void mostrarVentana() { 
        setVisible(true);
    }
}

class ModernCursoCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        CursoEnMarcha curso = (CursoEnMarcha) value;

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);

                Color startColor = isSelected ? new Color(100, 181, 246) : new Color(255, 255, 255);
                Color endColor = isSelected ? new Color(41, 121, 255) : new Color(240, 240, 240);
                GradientPaint gp = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
                g2d.setPaint(gp);

                g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);

                g2d.setColor(isSelected ? new Color(30, 136, 229) : new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel(curso.getCurso().getNombre());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(isSelected ? Color.WHITE : new Color(33, 33, 33));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDescription = new JLabel("<html><div style='width:350px'>" + curso.getCurso().getDescripcion() + "</div></html>");
        lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescription.setForeground(isSelected ? new Color(240, 240, 240) : new Color(100, 100, 100));
        lblDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblCategory = new JLabel(curso.getCurso().getCategoria() != null ? curso.getCurso().getCategoria() : "General");
        lblCategory.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblCategory.setForeground(isSelected ? new Color(255, 255, 200) : new Color(100, 100, 100));
        lblCategory.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String estrategia = null;
        if(curso.getEstrategia() instanceof EstrategiaSecuencial) {
        	estrategia = "Estrategia - Secuencial";
        } else if (curso.getEstrategia() instanceof EstrategiaAleatoria) {
        	estrategia = "Estrategia - Aleatoria";
        } else {
        	estrategia = "Estrategia - Espaciada";
        }
        
        JLabel lblEstrategia = new JLabel(estrategia);
        lblEstrategia.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblEstrategia.setForeground(isSelected ? new Color(255, 255, 200) : new Color(100, 100, 100));
        lblEstrategia.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblCategory);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblEstrategia);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblDescription);

        // Aumentar el tamaño del panel
        panel.setPreferredSize(new Dimension(400, 110)); // Mayor altura y ancho
        panel.setMinimumSize(new Dimension(400, 110));

        return panel;
    }
}