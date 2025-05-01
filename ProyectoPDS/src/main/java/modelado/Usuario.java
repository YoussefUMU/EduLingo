package modelado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import controlador.ControladorPDS;

/**
 * Clase que representa a un usuario de la plataforma de aprendizaje.
 */
public class Usuario {
	private final String id;
	private String nombre;
	private String correo;
	private String contraseña;
	private String nombreUsuario;
	private LocalDate fechaRegistro;  
	private LocalDate fechaNacimiento;
	private List<CursoEnMarcha> cursosActivos;
	private Estadistica estadisticas;
	private Premium premium;

	
	public Usuario(String id, String nombre, String contraseña, String correo, String nombreUsuario, LocalDate fechaNacimiento) {
		this.id = id;
		this.nombre = nombre;
		this.contraseña = contraseña;
		this.correo = correo;
		this.nombreUsuario = nombreUsuario;
		this.fechaRegistro = LocalDate.of(2025, 04, 25);
		this.cursosActivos = new ArrayList<>();
		this.estadisticas = new Estadistica();
		this.fechaNacimiento = fechaNacimiento;
	}

	public Usuario(String nombre, String contraseña, String correo, String nombreUsuario, LocalDate fechaNacimiento) {
		this.id = "";
		this.nombre = nombre;
		this.contraseña = contraseña;
		this.correo = correo;
		this.nombreUsuario = nombreUsuario;
		this.fechaRegistro = LocalDate.of(2025, 04, 25);
		this.cursosActivos = new ArrayList<>();
		this.estadisticas = new Estadistica();
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public List<CursoEnMarcha> getCursosActivos() {
		return cursosActivos;
	}

	public void setCursosActivos(List<CursoEnMarcha> cursosActivos) {
		this.cursosActivos = cursosActivos;
	}

	public Estadistica getEstadisticas() {
		return estadisticas;
	}

	public void setEstadisticas(Estadistica estadisticas) {
		this.estadisticas = estadisticas;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public boolean agregarCurso(Curso curso, int vidas, Estrategia estrategia) {
		boolean coincidencia = cursosActivos.stream()
				.anyMatch(c -> c.getEstrategia().getClass() == estrategia.getClass() // Comparar clases
						&& ControladorPDS.getUnicaInstancia().getNombreCursoEnMarcha(c).equals(curso.getNombre())
						&& ControladorPDS.getUnicaInstancia().getDescripcionCursoEnMarcha(c).equals(curso.getDescripcion()));

		if (!coincidencia) {
			cursosActivos.add(new CursoEnMarcha(curso, vidas, estrategia));
			return true;
		}
		return false;
	}

	public List<CursoEnMarcha> obtenerCursosActivos() {
		return cursosActivos.stream().collect(Collectors.toList());
	}

	public Optional<CursoEnMarcha> obtenerCursoEnMarcha(String cursoId) {
		return cursosActivos.stream().filter(c -> c.getCurso().getId().equals(cursoId)).findFirst();
	}

	public Optional<CursoEnMarcha> obtenerCursoEnMarcha(Curso curso, Estrategia estrategia) {
		return cursosActivos.stream().filter(c -> c.getCurso().getId().equals(curso.getId()) && 
				c.getEstrategia().getClass().equals(estrategia.getClass())).findFirst();
	}
	
	public void iniciarCurso(String cursoId) {
		obtenerCursoEnMarcha(cursoId).ifPresent(CursoEnMarcha::reiniciarCurso);
	}

	public void avanzarEnCurso(String cursoId) {
		obtenerCursoEnMarcha(cursoId).ifPresent(CursoEnMarcha::avanzarPregunta);
	}

	public Estadistica obtenerEstadisticas() {
		return estadisticas;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}

	public boolean esPremium() {
	    return premium != null && premium.estaActivo();
	}

	public void activarPremium(String tipoPlan) {
	    if (premium == null) {
	        premium = new Premium(tipoPlan);
	    } else {
	        // Si ya tenía premium pero estaba inactivo, renovarlo
	        if (!premium.estaActivo()) {
	            if ("anual".equalsIgnoreCase(tipoPlan)) {
	                premium.renovar(12);
	            } else {
	                premium.renovar(1);
	            }
	        }
	    }
	}

	public void cancelarPremium() {
	    if (premium != null) {
	        premium.cancelar();
	    }
	}

	public Premium getPremium() {
	    return premium;
	}

	public void setPremium(Premium premium) {
	    this.premium = premium;
	}

	// Estos métodos ayudan a determinar si el usuario tiene ciertos beneficios premium
	public boolean tieneVidasInfinitas() {
	    return esPremium() && premium.isVidasInfinitas();
	}

	public boolean tieneCursosAdicionales() {
	    return esPremium() && premium.isCursosAdicionales();
	}

	public boolean sinAnuncios() {
	    return esPremium() && premium.isSinAnuncios();
	}
	public void finalizarCurso(CursoEnMarcha cursoEnMarcha) {
        Curso curso = cursoEnMarcha.getCurso();
        
        // Registrar curso completado en estadísticas
        if (estadisticas.registrarCursoCompletado(curso.getId(), curso.getCategoria())) {
            // Comprobar si se han desbloqueado nuevos logros
            String[] nuevosLogros = GestorLogros.comprobarLogros(this);
            
            // Si se han desbloqueado logros, mostrar notificación
            if (nuevosLogros.length > 0) {
                mostrarNotificacionLogros(nuevosLogros);
            }
        }
        
        // Eliminar de cursos activos
        this.cursosActivos.remove(cursoEnMarcha);
        cursoEnMarcha.finalizar();
    }
    
    /**
     * Muestra una notificación al usuario sobre los logros desbloqueados
     */
    private void mostrarNotificacionLogros(String[] logrosIds) {
        StringBuilder mensaje = new StringBuilder("¡Has desbloqueado ");
        
        if (logrosIds.length == 1) {
            mensaje.append("un nuevo logro: ")
                   .append(GestorLogros.getInfoLogro(logrosIds[0]).getTitulo());
        } else {
            mensaje.append("nuevos logros: ");
            for (int i = 0; i < logrosIds.length; i++) {
                if (i > 0) {
                    mensaje.append(i == logrosIds.length - 1 ? " y " : ", ");
                }
                mensaje.append(GestorLogros.getInfoLogro(logrosIds[i]).getTitulo());
            }
        }
        mensaje.append("!");
        
        // Mostrar mensaje
        javax.swing.JOptionPane.showMessageDialog(null, 
                mensaje.toString(), 
                "¡Logros Desbloqueados!", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Obtiene el nivel actual del usuario
     */
    public int getNivel() {
        return estadisticas.getNivelActual();
    }
    
    /**
     * Obtiene el porcentaje de progreso en el nivel actual
     */
    public int getPorcentajeNivel() {
        return estadisticas.getPorcentajeNivel();
    }
    
    /**
     * Obtiene el rango actual del usuario basado en su nivel
     */
    public String getRango() {
        int nivel = getNivel();
        
        if (nivel >= 10) return "Maestro del Conocimiento";
        if (nivel >= 5) return "Experto Junior";
        if (nivel >= 3) return "Estudiante Constante";
        return "Aprendiz Entusiasta";
    }
    
    /**
     * Registra una respuesta a una pregunta y actualiza estadísticas
     */
    public void registrarRespuestaPregunta(boolean correcta) {
        estadisticas.registrarPreguntaRespondida(correcta);
        
        // Comprobar nuevos logros
        String[] nuevosLogros = GestorLogros.comprobarLogros(this);
        if (nuevosLogros.length > 0) {
            mostrarNotificacionLogros(nuevosLogros);
        }
    }
    
    /**
     * Obtiene la lista de IDs de logros desbloqueados
     */
    public List<String> getLogrosDesbloqueados() {
        return estadisticas.getLogrosDesbloqueados();
    }
    
    /**
     * Verifica si un logro está desbloqueado
     */
    public boolean tieneLogroDesbloqueado(String idLogro) {
        return estadisticas.esLogroDesbloqueado(idLogro);
    }
}