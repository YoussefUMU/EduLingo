package vista;

import modelado.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import controlador.ControladorPDS;

/**
 * Ventana principal mejorada con navegación a diferentes secciones
 * y visualización de estadísticas de usuario.
 */
public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    private Usuario usuario;
    private JPanel panelCentral;
    private JLabel saludoUsuario;
    private JButton btnCursosActivos;
    private JButton btnCursosDisponibles;
    private JButton btnEstadisticas;
    private JTextArea areaBienvenida;

    public VentanaPrincipal() {
        usuario = ControladorPDS.getUnicaInstancia().getSesionActual();
        inicializarInterfaz();
    }

    private void inicializarInterfaz() {
        setTitle("Edulingo - Plataforma de Aprendizaje");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior con logo y botones de navegación
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);
        
        // Panel central con contenido dinámico
        panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mostrarPanelBienvenida();
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior para información adicional
        JPanel panelInferior = crearPanelInferior();
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        panel.setBackground(new Color(240, 240, 240));
        
        // Panel izquierdo para logo y saludo
        JPanel panelLogo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLogo.setOpaque(false);
        
        saludoUsuario = new JLabel("¡Bienvenido, " + usuario.getNombreUsuario() + "!");
        saludoUsuario.setFont(new Font("Arial", Font.BOLD, 16));
        saludoUsuario.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        panelLogo.add(saludoUsuario);
        
        panel.add(panelLogo, BorderLayout.WEST);
        
        // Panel derecho para botones de navegación y estadísticas
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setOpaque(false);
        
        btnCursosActivos = crearBotonNavegacion("Mis Cursos", "/recursos/cursos_activos.png");
        btnCursosDisponibles = crearBotonNavegacion("Explorar Cursos", "/recursos/cursos_disponibles.png");
        
        // Botón de estadísticas con icono
        btnEstadisticas = new JButton();
        btnEstadisticas.setToolTipText("Estadísticas");
        try {
            ImageIcon iconoEstadisticas = new ImageIcon(getClass().getResource("/recursos/estadisticas.png"));
            // Si no encuentra el recurso, usamos un texto alternativo
            if (iconoEstadisticas.getIconWidth() == -1) {
                btnEstadisticas = new JButton("📊");
                btnEstadisticas.setFont(new Font("Dialog", Font.PLAIN, 20));
            } else {
                btnEstadisticas.setIcon(iconoEstadisticas);
            }
        } catch (Exception e) {
            btnEstadisticas = new JButton("📊");
            btnEstadisticas.setFont(new Font("Dialog", Font.PLAIN, 20));
        }
        
        btnEstadisticas.setPreferredSize(new Dimension(40, 40));
        btnEstadisticas.setFocusPainted(false);
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());
        
        panelBotones.add(btnCursosActivos);
        panelBotones.add(btnCursosDisponibles);
        panelBotones.add(btnEstadisticas);
        
        panel.add(panelBotones, BorderLayout.EAST);
        
        // Configurar acciones para los botones
        btnCursosActivos.addActionListener(e -> abrirVentanaCursosActivos());
        btnCursosDisponibles.addActionListener(e -> abrirVentanaCursosDisponibles());
        
        return panel;
    }
    
    private JButton crearBotonNavegacion(String texto, String rutaIcono) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setMargin(new Insets(8, 15, 8, 15));
        
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(rutaIcono));
            // Si encuentra el icono, lo usamos
            if (icono.getIconWidth() != -1) {
                boton.setIcon(icono);
                boton.setHorizontalTextPosition(SwingConstants.RIGHT);
                boton.setIconTextGap(10);
            }
        } catch (Exception e) {
            // Si no encuentra el icono, solo mostramos el texto
        }
        
        return boton;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        
        // Información de racha y tiempo de uso
        Estadistica stats = usuario.getEstadisticas();
        String infoEstadisticas = "Mejor racha: " + stats.getMejorRacha() + " días | " +
                                  "Tiempo total: " + formatearTiempo(stats.getTiempoUso());
        
        JLabel lblEstadisticas = new JLabel(infoEstadisticas);
        lblEstadisticas.setFont(new Font("Arial", Font.ITALIC, 12));
        panel.add(lblEstadisticas, BorderLayout.WEST);
        
        // Botón para cambiar usuario
        JButton btnCambiarUsuario = new JButton("Cerrar Sesión");
        btnCambiarUsuario.addActionListener(e -> cambiarUsuario());
        panel.add(btnCambiarUsuario, BorderLayout.EAST);
        
        return panel;
    }
    
    private void mostrarPanelBienvenida() {
        panelCentral.removeAll();
        
        JPanel panelBienvenida = new JPanel(new BorderLayout(20, 20));
        panelBienvenida.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Título de bienvenida
        JLabel lblBienvenida = new JLabel("¡Aprende a tu ritmo con Edulingo!");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 24));
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        panelBienvenida.add(lblBienvenida, BorderLayout.NORTH);
        
        // Área de texto con información de la aplicación
        areaBienvenida = new JTextArea();
        areaBienvenida.setEditable(false);
        areaBienvenida.setWrapStyleWord(true);
        areaBienvenida.setLineWrap(true);
        areaBienvenida.setFont(new Font("Arial", Font.PLAIN, 16));
        areaBienvenida.setText("Bienvenido a Edulingo, tu plataforma de aprendizaje personalizada.\n\n" +
                              "En Edulingo puedes:\n" +
                              "• Seguir cursos de diferentes dominios\n" +
                              "• Elegir tu estrategia de aprendizaje (secuencial, repetición espaciada o aleatoria)\n" +
                              "• Guardar tu progreso y reanudar cuando quieras\n" +
                              "• Crear y compartir tus propios cursos\n" +
                              "• Seguir tus estadísticas de aprendizaje\n\n" +
                              "Para comenzar, puedes explorar los cursos disponibles o continuar con tus cursos activos.");
        
        JScrollPane scrollPane = new JScrollPane(areaBienvenida);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panelBienvenida.add(scrollPane, BorderLayout.CENTER);
        
        // Botones de acceso rápido
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        JButton btnComenzar = new JButton("Comenzar a Aprender");
        btnComenzar.setFont(new Font("Arial", Font.BOLD, 16));
        btnComenzar.setBackground(new Color(76, 175, 80));
        btnComenzar.setForeground(Color.WHITE);
        btnComenzar.addActionListener(e -> abrirVentanaCursosDisponibles());
        
        JButton btnContinuar = new JButton("Continuar Aprendizaje");
        btnContinuar.setFont(new Font("Arial", Font.BOLD, 16));
        btnContinuar.setBackground(new Color(33, 150, 243));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.addActionListener(e -> abrirVentanaCursosActivos());
        
        panelBotones.add(btnComenzar);
        panelBotones.add(btnContinuar);
        panelBienvenida.add(panelBotones, BorderLayout.SOUTH);
        
        panelCentral.add(panelBienvenida, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();
    }
    
    private void abrirVentanaCursosActivos() {
        // Esta función abriría la ventana de cursos en marcha
        VentanaCursosEnMarcha ventanaCursosActivos = new VentanaCursosEnMarcha();
        ventanaCursosActivos.mostrarVentana();
        this.setVisible(false);
    }
    
    private void abrirVentanaCursosDisponibles() {
        // Esta función abriría la ventana de cursos disponibles
        VentanaCursosSinEmpezar ventanaCursosDisponibles = new VentanaCursosSinEmpezar();
        ventanaCursosDisponibles.mostrarVentana();
        this.setVisible(false);
    }
    
    private void mostrarEstadisticas() {
        // Crear un diálogo para mostrar las estadísticas
        JDialog dialogoEstadisticas = new JDialog(this, "Mis Estadísticas", true);
        dialogoEstadisticas.setSize(400, 300);
        dialogoEstadisticas.setLocationRelativeTo(this);
        dialogoEstadisticas.setLayout(new BorderLayout(10, 10));
        
        // Panel de contenido
        JPanel panelEstadisticas = new JPanel();
        panelEstadisticas.setLayout(new BoxLayout(panelEstadisticas, BoxLayout.Y_AXIS));
        panelEstadisticas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Obtener estadísticas del usuario
        Estadistica stats = usuario.getEstadisticas();
        
        // Título
        JLabel lblTitulo = new JLabel("Estadísticas de " + usuario.getNombreUsuario());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEstadisticas.add(lblTitulo);
        panelEstadisticas.add(Box.createVerticalStrut(20));
        
        // Información de estadísticas
        agregarEstadistica(panelEstadisticas, "Fecha de registro:", usuario.getFechaRegistro().toString());
        agregarEstadistica(panelEstadisticas, "Tiempo total de uso:", formatearTiempo(stats.getTiempoUso()));
        agregarEstadistica(panelEstadisticas, "Mejor racha:", stats.getMejorRacha() + " días");
        agregarEstadistica(panelEstadisticas, "Cursos activos:", String.valueOf(usuario.getCursosActivos().size()));
        
        // Agregar más estadísticas aquí según lo que tenga la clase Estadistica
        
        JScrollPane scrollPane = new JScrollPane(panelEstadisticas);
        dialogoEstadisticas.add(scrollPane, BorderLayout.CENTER);
        
        // Botón para cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialogoEstadisticas.dispose());
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(btnCerrar);
        dialogoEstadisticas.add(panelBoton, BorderLayout.SOUTH);
        
        dialogoEstadisticas.setVisible(true);
    }
    
    private void agregarEstadistica(JPanel panel, String etiqueta, String valor) {
        JPanel lineaEstadistica = new JPanel(new BorderLayout(10, 0));
        lineaEstadistica.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.PLAIN, 14));
        
        lineaEstadistica.add(lblEtiqueta, BorderLayout.WEST);
        lineaEstadistica.add(lblValor, BorderLayout.EAST);
        
        panel.add(lineaEstadistica);
        panel.add(Box.createVerticalStrut(10));
    }
    
    private String formatearTiempo(int segundos) {
        int horas = segundos / 3600;
        int minutos = (segundos % 3600) / 60;
        int segs = segundos % 60;
        
        return String.format("%d h %d min %d s", horas, minutos, segs);
    }
    
    private void cambiarUsuario() {
        int opcion = JOptionPane.showConfirmDialog(this, 
                "¿Desea cerrar la sesión actual?", 
                "Cerrar Sesión", 
                JOptionPane.YES_NO_OPTION);
                
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            VentanaLogin ventanaLogin = new VentanaLogin();
            ventanaLogin.setVisible(true);
        }
    }
   
}