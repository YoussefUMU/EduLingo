package vista;

import modelado.*;
import javax.swing.*;
import java.util.List;
import controlador.ControladorPDS;

import java.awt.*;

/**
 * Ventana principal mejorada con una interfaz más agradable y funcionalidad de cursos guardados.
 */
public class VentanaPrincipal extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Usuario usuario;
    private JComboBox<String> comboCursosDisponibles;
    private JComboBox<String> comboCursosGuardados;
    private JTextArea areaSalida;
    private JLabel saludoUsuario;

    public VentanaPrincipal() {
        usuario = ControladorPDS.getUnicaInstancia().getSesionActual();
        inicializarInterfaz();
    }

    private void inicializarInterfaz() {
        setTitle("Plataforma de Aprendizaje");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout());
        saludoUsuario = new JLabel("Bienvenido, " + usuario.getNombreUsuario());
        JButton btnCambiarUsuario = new JButton("Cambiar Usuario");
        btnCambiarUsuario.addActionListener(e -> cambiarUsuario());
        panelSuperior.add(saludoUsuario);
        panelSuperior.add(btnCambiarUsuario);
        
        JPanel panelCentral = new JPanel(new GridLayout(3, 2, 10, 10));
        comboCursosDisponibles = new JComboBox<>();
        comboCursosGuardados = new JComboBox<>();
        JButton btnIniciarCurso = new JButton("Iniciar Curso");
        JButton btnReanudarCurso = new JButton("Reanudar Curso");

        btnIniciarCurso.addActionListener(e -> iniciarCurso());
        btnReanudarCurso.addActionListener(e -> reanudarCurso());

        panelCentral.add(new JLabel("Cursos Disponibles:"));
        panelCentral.add(comboCursosDisponibles);
        panelCentral.add(new JLabel("Cursos Guardados:"));
        panelCentral.add(comboCursosGuardados);
        panelCentral.add(btnIniciarCurso);
        panelCentral.add(btnReanudarCurso);
        
        areaSalida = new JTextArea();
        areaSalida.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaSalida);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        cargarCursos();
    }

    private void cargarCursos() {
    	List<Curso> cursos = ControladorPDS.getUnicaInstancia().obtenerCursosLocales();
    	for (Curso curso: cursos) {
    		comboCursosDisponibles.addItem(curso.getNombre());
    	}
        // Simulación de carga de cursos desde JSON/YAML
        //comboCursosDisponibles.addItem("Curso de Geografía");
        //comboCursosDisponibles.addItem("Curso de Matemáticas");
        //comboCursosDisponibles.addItem("Curso de Programación");
        
        // Simulación de cursos guardados (ya empezados)
        comboCursosGuardados.addItem("Curso de Geografía - Progreso 50%");
        comboCursosGuardados.addItem("Curso de Matemáticas - Progreso 30%");
    }

    private void iniciarCurso() {
        String cursoSeleccionado = (String) comboCursosDisponibles.getSelectedItem();
        areaSalida.setText("Iniciando curso: " + cursoSeleccionado + "\n¡Buena suerte!");
    }
    
    private void reanudarCurso() {
        String cursoSeleccionado = (String) comboCursosGuardados.getSelectedItem();
        areaSalida.setText("Reanudando curso: " + cursoSeleccionado + "\n¡Sigue aprendiendo!");
    }
    
    private void cambiarUsuario() {
        String nuevoNombre = JOptionPane.showInputDialog(this, "Ingrese el nuevo nombre de usuario:", "Cambiar Usuario", JOptionPane.PLAIN_MESSAGE);
        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
            usuario = new Usuario("2", nuevoNombre, nuevoNombre.toLowerCase() + "@example.com", "userExample");
            saludoUsuario.setText("Bienvenido, " + usuario.getNombre());
            areaSalida.setText("Usuario cambiado a " + usuario.getNombre());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}