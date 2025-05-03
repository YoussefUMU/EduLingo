package vista;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import modelado.Usuario;

public class PanelComunidad extends JPanel {
    private JPanel panelComentarios;
    private JTextField campoComentario;
    private JComboBox<String> comboEtiquetas;
    private JButton btnPublicar;
    private JTextField campoBusqueda;
    private JComboBox<String> filtroEtiquetas;
    private List<Comentario> listaComentarios;
    private JPanel panelScrollable;
    private Usuario usuarioActual;
    private JLabel contadorCaracteres;
    private final int MAX_CARACTERES = 280;

    public PanelComunidad(Usuario usuario) {
        this.usuarioActual = usuario;
        this.listaComentarios = new ArrayList<>();
        
        setLayout(new BorderLayout(10, 10));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Crear componentes
        JPanel panelSuperior = crearPanelSuperior();
        JPanel panelPublicacion = crearPanelPublicacion();
        panelComentarios = crearPanelComentarios();
        
        // Añadir componentes al panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(panelComentarios, BorderLayout.CENTER);
        add(panelPublicacion, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        
        // Título con icono
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTitulo.setOpaque(false);
        
        JLabel iconoLabel = new JLabel("\uD83D\uDC65");
        iconoLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        
        JLabel tituloLabel = new JLabel("Comunidad Edulingo");
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tituloLabel.setForeground(new Color(66, 133, 244));
        
        panelTitulo.add(iconoLabel);
        panelTitulo.add(tituloLabel);
        
        // Panel de filtros y búsqueda
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFiltros.setOpaque(false);
        
        campoBusqueda = new JTextField(15);
        campoBusqueda.putClientProperty("JTextField.placeholderText", "Buscar en comentarios...");
        
        filtroEtiquetas = new JComboBox<>(new String[] {"Todas las etiquetas", "Sugerencia", "Problema", "Crítica"});
        filtroEtiquetas.setPreferredSize(new Dimension(150, 30));
        
        JButton btnBuscar = new JButton("Buscar");
        estilizarBoton(btnBuscar, new Color(66, 133, 244), Color.WHITE);
        
        btnBuscar.addActionListener(e -> aplicarFiltros());
        campoBusqueda.addActionListener(e -> aplicarFiltros());
        filtroEtiquetas.addActionListener(e -> aplicarFiltros());
        
        panelFiltros.add(new JLabel("Filtrar: "));
        panelFiltros.add(filtroEtiquetas);
        panelFiltros.add(campoBusqueda);
        panelFiltros.add(btnBuscar);
        
        // Añadir componentes al panel superior
        panel.add(panelTitulo, BorderLayout.WEST);
        panel.add(panelFiltros, BorderLayout.EAST);
        
        // Separador
        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(200, 200, 200));
        
        JPanel panelCompleto = new JPanel(new BorderLayout());
        panelCompleto.setOpaque(false);
        panelCompleto.add(panel, BorderLayout.CENTER);
        panelCompleto.add(separador, BorderLayout.SOUTH);
        
        return panelCompleto;
    }
    
    private JPanel crearPanelPublicacion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 5, 5, 5)));
        
        // Panel para el campo de texto y contador
        JPanel panelTexto = new JPanel(new BorderLayout(5, 0));
        panelTexto.setOpaque(false);
        
        campoComentario = new JTextField();
        campoComentario.putClientProperty("JTextField.placeholderText", "Escribe tu comentario para la comunidad...");
        
        contadorCaracteres = new JLabel(MAX_CARACTERES + " caracteres restantes");
        contadorCaracteres.setForeground(Color.GRAY);
        contadorCaracteres.setHorizontalAlignment(SwingConstants.RIGHT);
        
        campoComentario.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarContador();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarContador();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarContador();
            }
            
            private void actualizarContador() {
                int caracteresUsados = campoComentario.getText().length();
                int caracteresRestantes = MAX_CARACTERES - caracteresUsados;
                contadorCaracteres.setText(caracteresRestantes + " caracteres restantes");
                
                if (caracteresRestantes < 0) {
                    contadorCaracteres.setForeground(Color.RED);
                    btnPublicar.setEnabled(false);
                } else {
                    contadorCaracteres.setForeground(Color.GRAY);
                    btnPublicar.setEnabled(true);
                }
            }
        });
        
        panelTexto.add(campoComentario, BorderLayout.CENTER);
        panelTexto.add(contadorCaracteres, BorderLayout.SOUTH);
        
        // Panel para los controles
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelControles.setOpaque(false);
        
        comboEtiquetas = new JComboBox<>(new String[] {"Sugerencia", "Problema", "Crítica"});
        comboEtiquetas.setPreferredSize(new Dimension(120, 30));
        
        btnPublicar = new JButton("Publicar");
        estilizarBoton(btnPublicar, new Color(76, 175, 80), Color.WHITE);
        
        btnPublicar.addActionListener(e -> publicarComentario());
        
        panelControles.add(new JLabel("Etiqueta: "));
        panelControles.add(comboEtiquetas);
        panelControles.add(btnPublicar);
        
        // Añadir componentes al panel de publicación
        panel.add(panelTexto, BorderLayout.CENTER);
        panel.add(panelControles, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel crearPanelComentarios() {
        // Panel con scroll que contendrá todos los comentarios
        panelScrollable = new JPanel();
        panelScrollable.setLayout(new BoxLayout(panelScrollable, BoxLayout.Y_AXIS));
        panelScrollable.setOpaque(false);
        
        // Añadir los comentarios al panel
        actualizarListaComentarios();
        
        JScrollPane scrollPane = new JScrollPane(panelScrollable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void actualizarListaComentarios() {
        panelScrollable.removeAll();
        
        // Ordenar comentarios por fecha (más recientes primero)
        List<Comentario> comentariosFiltrados = new ArrayList<>(listaComentarios);
        Collections.sort(comentariosFiltrados, (c1, c2) -> c2.getFecha().compareTo(c1.getFecha()));
        
        // Aplicar filtros si es necesario
        String busqueda = campoBusqueda.getText().toLowerCase();
        String etiquetaSeleccionada = (String) filtroEtiquetas.getSelectedItem();
        
        for (Comentario comentario : comentariosFiltrados) {
            boolean cumpleFiltro = true;
            
            // Filtrar por texto de búsqueda
            if (!busqueda.isEmpty()) {
                cumpleFiltro = comentario.getTexto().toLowerCase().contains(busqueda) || 
                               comentario.getUsuario().getNombreUsuario().toLowerCase().contains(busqueda);
            }
            
            // Filtrar por etiqueta
            if (!etiquetaSeleccionada.equals("Todas las etiquetas") && cumpleFiltro) {
                cumpleFiltro = comentario.getEtiqueta().equals(etiquetaSeleccionada);
            }
            
            if (cumpleFiltro) {
                panelScrollable.add(crearPanelComentario(comentario));
                panelScrollable.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        panelScrollable.revalidate();
        panelScrollable.repaint();
    }
    
    private JPanel crearPanelComentario(Comentario comentario) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setOpaque(true);
        panel.setBackground(new Color(250, 250, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 230), 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Panel superior con información del usuario y fecha
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setOpaque(false);
        
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelUsuario.setOpaque(false);
        
        // Avatar del usuario (emoji o iniciales)
        JLabel avatarLabel = new JLabel(obtenerAvatar(comentario.getUsuario()));
        avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        avatarLabel.setForeground(new Color(66, 133, 244));
        avatarLabel.setBackground(new Color(240, 245, 255));
        avatarLabel.setOpaque(true);
        avatarLabel.setBorder(new LineBorder(new Color(200, 220, 255), 1, true));
        avatarLabel.setPreferredSize(new Dimension(30, 30));
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Nombre de usuario con formato
        JLabel nombreLabel = new JLabel(comentario.getUsuario().getNombreUsuario());
        nombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Etiqueta con fondo de color según el tipo
        JLabel etiquetaLabel = new JLabel(comentario.getEtiqueta());
        etiquetaLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        etiquetaLabel.setOpaque(true);
        etiquetaLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        etiquetaLabel.setForeground(Color.WHITE);
        
        // Definir color según etiqueta
        Color colorEtiqueta;
        switch (comentario.getEtiqueta()) {
            case "Sugerencia":
                colorEtiqueta = new Color(76, 175, 80); // Verde
                break;
            case "Problema":
                colorEtiqueta = new Color(244, 67, 54); // Rojo
                break;
            case "Crítica":
                colorEtiqueta = new Color(255, 152, 0); // Naranja
                break;
            default:
                colorEtiqueta = new Color(66, 133, 244); // Azul
        }
        etiquetaLabel.setBackground(colorEtiqueta);
        
        panelUsuario.add(avatarLabel);
        panelUsuario.add(nombreLabel);
        panelUsuario.add(Box.createRigidArea(new Dimension(10, 0)));
        panelUsuario.add(etiquetaLabel);
        
        // Fecha formateada
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        JLabel fechaLabel = new JLabel(sdf.format(comentario.getFecha()));
        fechaLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        fechaLabel.setForeground(Color.GRAY);
        
        panelInfo.add(panelUsuario, BorderLayout.WEST);
        panelInfo.add(fechaLabel, BorderLayout.EAST);
        
        // Texto del comentario
        JLabel textoLabel = new JLabel("<html><p style='width:380px'>" + comentario.getTexto() + "</p></html>");
        textoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textoLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        
        // Panel inferior con botones de acción
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelAcciones.setOpaque(false);
        
        JButton btnLike = new JButton("👍 Me gusta");
        estilizarBotonAccion(btnLike);
        
        JButton btnResponder = new JButton("↩️ Responder");
        estilizarBotonAccion(btnResponder);
        
        panelAcciones.add(btnLike);
        panelAcciones.add(btnResponder);
        
        // Añadir componentes al panel del comentario
        panel.add(panelInfo, BorderLayout.NORTH);
        panel.add(textoLabel, BorderLayout.CENTER);
        panel.add(panelAcciones, BorderLayout.SOUTH);
        
        // Si el comentario es del usuario actual, añadir botones adicionales
        if (comentario.getUsuario().equals(usuarioActual)) {
            JButton btnEditar = new JButton("✏️ Editar");
            estilizarBotonAccion(btnEditar);
            
            JButton btnEliminar = new JButton("🗑️ Eliminar");
            estilizarBotonAccion(btnEliminar);
            
            btnEliminar.addActionListener(e -> eliminarComentario(comentario));
            btnEditar.addActionListener(e -> editarComentario(comentario));
            
            panelAcciones.add(btnEditar);
            panelAcciones.add(btnEliminar);
        }
        
        return panel;
    }
    
    private String obtenerAvatar(Usuario usuario) {
        // Usar iniciales como avatar simple
        String nombre = usuario.getNombreUsuario();
        if (nombre.length() > 0) {
            return String.valueOf(nombre.charAt(0)).toUpperCase();
        } else {
            return "U"; // Usuario por defecto
        }
    }
    
    private void aplicarFiltros() {
        actualizarListaComentarios();
    }
    
    private void publicarComentario() {
        String texto = campoComentario.getText().trim();
        if (texto.isEmpty() || texto.length() > MAX_CARACTERES) {
            JOptionPane.showMessageDialog(this, 
                "El comentario debe tener entre 1 y " + MAX_CARACTERES + " caracteres.",
                "Error de publicación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String etiqueta = (String) comboEtiquetas.getSelectedItem();
        Comentario nuevoComentario = new Comentario(usuarioActual, texto, etiqueta, new Date());
        
        listaComentarios.add(0, nuevoComentario);
        actualizarListaComentarios();
        
        campoComentario.setText("");
        JOptionPane.showMessageDialog(this, 
            "¡Comentario publicado con éxito!",
            "Publicación exitosa", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void eliminarComentario(Comentario comentario) {
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que deseas eliminar este comentario?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);
            
        if (respuesta == JOptionPane.YES_OPTION) {
            listaComentarios.remove(comentario);
            actualizarListaComentarios();
            JOptionPane.showMessageDialog(this, 
                "El comentario ha sido eliminado.",
                "Eliminación exitosa", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void editarComentario(Comentario comentario) {
        String nuevoTexto = JOptionPane.showInputDialog(this,
            "Editar comentario:",
            comentario.getTexto());
            
        if (nuevoTexto != null && !nuevoTexto.trim().isEmpty() && nuevoTexto.length() <= MAX_CARACTERES) {
            comentario.setTexto(nuevoTexto.trim());
            comentario.setFecha(new Date()); // Actualizar fecha
            actualizarListaComentarios();
        } else if (nuevoTexto != null) {
            JOptionPane.showMessageDialog(this, 
                "El comentario debe tener entre 1 y " + MAX_CARACTERES + " caracteres.",
                "Error de edición", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void estilizarBoton(JButton boton, Color fondo, Color texto) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setBackground(fondo);
        boton.setForeground(texto);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(100, 30));
        
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(fondo.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(fondo);
            }
        });
    }
    
    private void estilizarBotonAccion(JButton boton) {
        boton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setForeground(new Color(100, 100, 100));
        
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setForeground(new Color(66, 133, 244));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setForeground(new Color(100, 100, 100));
            }
        });
    }
    
    // Clase para representar un comentario
    public static class Comentario {
        private Usuario usuario;
        private String texto;
        private String etiqueta;
        private Date fecha;
        private int likes;
        
        public Comentario(Usuario usuario, String texto, String etiqueta, Date fecha) {
            this.usuario = usuario;
            this.texto = texto;
            this.etiqueta = etiqueta;
            this.fecha = fecha;
            this.likes = 0;
        }
        
        public Usuario getUsuario() {
            return usuario;
        }
        
        public String getTexto() {
            return texto;
        }
        
        public void setTexto(String texto) {
            this.texto = texto;
        }
        
        public String getEtiqueta() {
            return etiqueta;
        }
        
        public Date getFecha() {
            return fecha;
        }
        
        public void setFecha(Date fecha) {
            this.fecha = fecha;
        }
        
        public int getLikes() {
            return likes;
        }
        
        public void incrementarLikes() {
            this.likes++;
        }
    }
}