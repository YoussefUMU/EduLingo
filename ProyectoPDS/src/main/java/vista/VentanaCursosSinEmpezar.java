package vista;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import modelado.Curso;
import modelado.CursoEnMarcha;
import modelado.Estrategia;
import modelado.EstrategiaAleatoria;
import modelado.EstrategiaEspaciada;
import modelado.EstrategiaSecuencial;
import modelado.ManejadorCursos;
import controlador.ControladorPDS;

public class VentanaCursosSinEmpezar extends JFrame {
    private JPanel panelContenidoScroll;
    private JPanel mainPanel;
    private Point dragPoint;
    private JComboBox<String> comboEstrategias;
    private JList<Curso> listaCursos; // Añadido para mantener referencia a la lista
    private DefaultListModel<Curso> modeloCursos; // Añadido para mantener referencia al modelo

    public VentanaCursosSinEmpezar() {
        initialize();
    }

    public void initialize() {
        setTitle("Edulingo - Explorar Cursos");
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
                Color color1 = new Color(66, 133, 244);
                Color color2 = new Color(15, 76, 129);
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
        JLabel lblNewLabel = new JLabel("CURSOS DISPONIBLES");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setForeground(new Color(255, 255, 255, 0));
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

        // Panel de botones superior
        JPanel panelBotonesSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelBotonesSuperior.setOpaque(false);
        
        // Botón para importar cursos YAML
        JButton btnImportarYAML = createAnimatedButton("Añadir Curso", new Color(255, 152, 0));
        btnImportarYAML.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importarCursosYAML();
            }
        });
        
        panelBotonesSuperior.add(btnImportarYAML);
        panelNorte.add(panelBotonesSuperior, BorderLayout.SOUTH);

        // Panel intermedio para centrar el scroll
        JPanel panelCentroContenedor = new JPanel(new BorderLayout());
        panelCentroContenedor.setOpaque(false);
        panelCentroContenedor.setBorder(new EmptyBorder(15, 25, 15, 25));
        getContentPane().add(panelCentroContenedor, BorderLayout.CENTER);

        // ScrollPanel con contenido
        panelContenidoScroll = new JPanel();
        panelContenidoScroll.setLayout(new BoxLayout(panelContenidoScroll, BoxLayout.Y_AXIS));
        panelContenidoScroll.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(panelContenidoScroll);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        
        // Lista de cursos con un renderer personalizado
        listaCursos = new JList<Curso>(); // Usar la variable de instancia
        listaCursos.setCellRenderer(new ModernCursoCellRenderer());
        listaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaCursos.setOpaque(false);
        
        // Obtener los cursos disponibles
        List<Curso> cursosDisponibles = ControladorPDS.getUnicaInstancia().obtenerCursosLocales();
        modeloCursos = new DefaultListModel<Curso>(); // Usar la variable de instancia
        
        if (cursosDisponibles != null && !cursosDisponibles.isEmpty()) {
            for (Curso curso : cursosDisponibles) {
                modeloCursos.addElement(curso);
            }
        }
        
        listaCursos.setModel(modeloCursos);
        listaCursos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        panelContenidoScroll.add(listaCursos);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        panelCentroContenedor.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones centrados y grandes
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.setBorder(new EmptyBorder(10, 15, 20, 15));
        getContentPane().add(panelSur, BorderLayout.SOUTH);

        // Panel para la selección de estrategia
        JPanel panelEstrategia = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelEstrategia.setOpaque(false);
        
        JLabel lblEstrategia = new JLabel("Estrategia de aprendizaje:");
        lblEstrategia.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEstrategia.setForeground(Color.WHITE);
        
        comboEstrategias = new JComboBox<>(new String[] {"Secuencial", "Aleatoria", "Espaciada"});
        comboEstrategias.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboEstrategias.setPreferredSize(new Dimension(150, 30));
        
        panelEstrategia.add(lblEstrategia);
        panelEstrategia.add(comboEstrategias);
        
        panelSur.add(panelEstrategia, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);

        JButton btnEmpezar = createAnimatedButton("Empezar", new Color(76, 175, 80));
        JButton btnCancelar = createAnimatedButton("Volver", new Color(220, 50, 50));

        // Tamaño más grande para los botones
        Dimension botonDimension = new Dimension(150, 45);
        btnEmpezar.setPreferredSize(botonDimension);
        btnCancelar.setPreferredSize(botonDimension);

        // Agregar acciones a los botones
        btnEmpezar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Curso cursoSeleccionado = listaCursos.getSelectedValue(); // Usar la variable de instancia
                if (cursoSeleccionado != null) {
                    // Iniciar el curso con la estrategia seleccionada
                    Estrategia estrategiaSeleccionada = obtenerEstrategiaSeleccionada();
                    CursoEnMarcha cursoEnMarcha = ControladorPDS.getUnicaInstancia()
                            .iniciarCursoE(cursoSeleccionado, estrategiaSeleccionada);

                    if (cursoEnMarcha == null) {
                        JOptionPane.showMessageDialog(null, 
                                "No puede empezar un curso dos veces con la misma estrategia.", 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        final Timer timer = new Timer(10, new ActionListener() {
                            float alpha = 1.0f;
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                alpha -= 0.05f;
                                if (alpha <= 0) {
                                    ((Timer)e.getSource()).stop();
                                    dispose();
                                    
                                    JFrame flashCard = AdaptadorPreguntas.crearFlashCard(cursoEnMarcha, 0, 0);
                                    if (flashCard != null) {
                                        flashCard.setVisible(true);
                                    } else {
                                        new VentanaPrincipal().setVisible(true);
                                    }
                                }
                                setOpacity(Math.max(0, alpha));
                            }
                        });
                        timer.start();
                    }
                } else {
                    mostrarMensajeError("Por favor, seleccione un curso.");
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final Timer timer = new Timer(10, new ActionListener() {
                    float alpha = 1.0f;
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        alpha -= 0.05f;
                        if (alpha <= 0) {
                            ((Timer)e.getSource()).stop();
                            dispose();
                            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
                            ventanaPrincipal.setVisible(true);
                        }
                        setOpacity(Math.max(0, alpha));
                    }
                });
                timer.start();
            }
        });

        panelBotones.add(btnEmpezar);
        panelBotones.add(btnCancelar);
        
        panelSur.add(panelBotones, BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
    }

    // Método modificado para importar cursos y actualizar la lista
    private void importarCursosYAML() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivos YAML");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        
        // Filtro para archivos YAML
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".yaml") || 
                       f.getName().toLowerCase().endsWith(".yml");
            }

            @Override
            public String getDescription() {
                return "Archivos YAML (*.yaml, *.yml)";
            }
        });
        
        // Configurar directorio inicial
        File libreriaFolder = new File(System.getProperty("user.dir"), "libreria");
        if (libreriaFolder.exists() && libreriaFolder.isDirectory()) {
            fileChooser.setCurrentDirectory(libreriaFolder);
        }
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles();
            
            if (selectedFiles != null && selectedFiles.length > 0) {
                int cursosAñadidos = 0;
                ManejadorCursos manejador = new ManejadorCursos();
                
                for (File file : selectedFiles) {
                    try {
                        // Parsear el archivo YAML
                        Curso nuevoCurso = manejador.parseYAML(file.toPath());
                        
                        // Verificar si el curso ya existe en la lista
                        boolean cursoExiste = false;
                        for (int i = 0; i < modeloCursos.getSize(); i++) {
                            Curso cursoExistente = modeloCursos.getElementAt(i);
                            if (cursoExistente.getId().equals(nuevoCurso.getId())) {
                                cursoExiste = true;
                                break;
                            }
                        }
                        
                        // Si no existe, añadirlo
                        if (!cursoExiste) {
                            modeloCursos.addElement(nuevoCurso);
                            cursosAñadidos++;
                        }
                    } catch (Exception e) {
                        System.err.println("Error al procesar archivo: " + file.getName());
                        e.printStackTrace();
                    }
                }
                
                // Actualizar la lista
                listaCursos.setModel(modeloCursos);
                
                // Mostrar resultado
                if (cursosAñadidos > 0) {
                    mostrarDialogoImportacion(cursosAñadidos);
                } else {
                    mostrarMensajeError("No se añadieron nuevos cursos. Es posible que ya existieran en la lista.");
                }
            } else {
                mostrarMensajeError("No se seleccionaron archivos.");
            }
        }
    }
    
    private void mostrarDialogoImportacion(int numCursos) {
        JDialog dialog = new JDialog(this, "Importación Exitosa", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);
        dialog.setShape(new RoundRectangle2D.Double(0, 0, 300, 200, 15, 15));
        
        JPanel panelDialog = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(76, 175, 80);
                Color color2 = new Color(27, 94, 32);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        panelDialog.setLayout(new BorderLayout());
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" +
                "¡Importación exitosa!<br><br>" +
                "Se han añadido " + numCursos + " cursos nuevos.</div></html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        messageLabel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JButton okButton = createAnimatedButton("Aceptar", new Color(255, 255, 255, 100));
        okButton.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);
        
        panelDialog.add(messageLabel, BorderLayout.CENTER);
        panelDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setContentPane(panelDialog);
        
        okButton.addActionListener(e -> dialog.dispose());
        
        // Mostrar con animación
        dialog.setOpacity(0.0f);
        dialog.setVisible(true);
        
        final Timer timer = new Timer(20, new ActionListener() {
            float opacity = 0.0f;
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.1f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    ((Timer)e.getSource()).stop();
                }
                dialog.setOpacity(opacity);
            }
        });
        timer.start();
    }
    
    private void mostrarMensajeError(String mensaje) {
        // Efecto de sacudida para la ventana
        Point originalLocation = getLocation();
        final int offset = 20;
        final int time = 50;
        
        new Thread(() -> {
            try {
                for (int i = 0; i < 4; i++) {
                    setLocation(originalLocation.x + offset, originalLocation.y);
                    Thread.sleep(time);
                    setLocation(originalLocation.x - offset, originalLocation.y);
                    Thread.sleep(time);
                }
                setLocation(originalLocation);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
        
        // Mostrar mensaje de error
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private Estrategia obtenerEstrategiaSeleccionada() {
        String estrategiaSeleccionada = (String) comboEstrategias.getSelectedItem();
        switch (estrategiaSeleccionada) {
            case "Aleatoria":
                return new EstrategiaAleatoria();
            case "Espaciada":
                return new EstrategiaEspaciada();
            case "Secuencial":
            default:
                return new EstrategiaSecuencial();
        }
    }
    
    private void setupWindowMovement() {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragPoint = e.getPoint();
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentLocation = getLocation();
                setLocation(currentLocation.x + e.getX() - dragPoint.x,
                          currentLocation.y + e.getY() - dragPoint.y);
            }
        };
        
        addMouseListener(ma);
        addMouseMotionListener(ma);
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
    
    class ModernCursoCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Curso curso = (Curso) value;

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

            JLabel lblTitle = new JLabel(curso.getNombre());
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblTitle.setForeground(isSelected ? Color.WHITE : new Color(33, 33, 33));
            lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblDescription = new JLabel("<html><div style='width:350px'>" + curso.getDescripcion() + "</div></html>");
            lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblDescription.setForeground(isSelected ? new Color(240, 240, 240) : new Color(100, 100, 100));
            lblDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblCategory = new JLabel(curso.getCategoria() != null ? curso.getCategoria() : "General");
            lblCategory.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblCategory.setForeground(isSelected ? new Color(255, 255, 200) : new Color(100, 100, 100));
            lblCategory.setAlignmentX(Component.LEFT_ALIGNMENT);

            panel.add(lblTitle);
            panel.add(Box.createVerticalStrut(4));
            panel.add(lblCategory);
            panel.add(Box.createVerticalStrut(4));
            panel.add(lblDescription);

            panel.setPreferredSize(new Dimension(400, 110));
            panel.setMinimumSize(new Dimension(400, 110));

            return panel;
        }
    }

    public void mostrarVentana() {
        setOpacity(0.0f);
        setVisible(true);
        
        final Timer timer = new Timer(10, new ActionListener() {
            float opacity = 0.0f;
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    ((Timer)e.getSource()).stop();
                }
                setOpacity(Math.min(opacity, 1.0f));
            }
        });
        timer.start();
    }
}