package modelado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import controlador.ControladorPDS;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Clase que representa a un usuario de la plataforma de aprendizaje.
 */

@Entity
@Table(name = "usuarios")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String nombre;
	private String correo;
	private String contraseña;
	private String nombreUsuario;
	private LocalDate fechaRegistro;  
	private LocalDate fechaNacimiento;
	@OneToMany(mappedBy="usuario", cascade={ CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	private List<CursoEnMarcha> cursosActivos;
	@OneToOne(cascade={ CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(unique=true)
	private Estadistica estadisticas;
	@OneToOne(cascade={ CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(unique=true)
	private Premium premium;
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ComentarioComunidad> comentarios = new ArrayList<>();
	
	public Usuario() {
		
	}
	
	public Usuario(String nombre, String contraseña, String correo, String nombreUsuario, LocalDate fechaNacimiento) {
		this.nombre = nombre;
		this.contraseña = contraseña;
		this.correo = correo;
		this.nombreUsuario = nombreUsuario;
		this.fechaRegistro = LocalDate.now();
		this.cursosActivos = new ArrayList<>();
		this.estadisticas = new Estadistica();
		this.fechaNacimiento = fechaNacimiento;
	}

	public Usuario(String nombre, String correo, String nombreUsuario) {
		this.nombre = nombre;
		this.contraseña = "1234";
		this.correo = correo;
		this.nombreUsuario = nombreUsuario;
		this.fechaRegistro = LocalDate.now();
		this.cursosActivos = new ArrayList<>();
		this.estadisticas = new Estadistica();
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

	public boolean agregarCurso(Curso curso, int vidas, Estrategia estrategia, TipoEstrategia tipoEstrategia) {
		boolean coincidencia = cursosActivos.stream()
				.anyMatch(c -> c.getEstrategia().getClass() == estrategia.getClass() // Comparar clases
						&& ControladorPDS.getUnicaInstancia().getNombreCursoEnMarcha(c).equals(curso.getNombre())
						&& ControladorPDS.getUnicaInstancia().getDescripcionCursoEnMarcha(c).equals(curso.getDescripcion()));

		if (!coincidencia) {
			cursosActivos.add(new CursoEnMarcha(curso, vidas, estrategia, tipoEstrategia));
			return true;
		}
		return false;
	}

	public List<CursoEnMarcha> obtenerCursosActivos() {
		return cursosActivos.stream().collect(Collectors.toList());
	}
	
	public Optional<CursoEnMarcha> obtenerCursoEnMarcha(Curso curso, Estrategia estrategia) {
		return cursosActivos.stream().filter(c -> c.getCurso().getId().equals(curso.getId()) && 
				c.getEstrategia().getClass().equals(estrategia.getClass())).findFirst();
	}

	public Estadistica obtenerEstadisticas() {
		return new Estadistica();
	}

	public Long getId() {
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
	        premium.setUsuario(this); // Asegurarse de que la relación bidireccional está configurada
	        premium.setActivo(true);
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
    /**
     * Añade un comentario a la lista de comentarios del usuario
     * @param texto Texto del comentario
     * @param etiqueta Etiqueta del comentario
     * @return El comentario creado
     */
    public ComentarioComunidad añadirComentario(String texto, String etiqueta) {
        ComentarioComunidad comentario = new ComentarioComunidad(this, texto, etiqueta, new Date());
        comentarios.add(comentario);
        return comentario;
    }

    /**
     * Edita un comentario existente
     * @param comentarioId ID del comentario a editar
     * @param nuevoTexto Nuevo texto para el comentario
     * @return true si se editó correctamente, false si no se encontró
     */
    public boolean editarComentario(Long comentarioId, String nuevoTexto) {
        for (ComentarioComunidad comentario : comentarios) {
            if (comentario.getId().equals(comentarioId)) {
                comentario.setTexto(nuevoTexto);
                comentario.setFecha(new Date()); // Actualizar fecha
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un comentario
     * @param comentarioId ID del comentario a eliminar
     * @return true si se eliminó correctamente, false si no se encontró
     */
    public boolean eliminarComentario(Long comentarioId) {
        return comentarios.removeIf(c -> c.getId().equals(comentarioId));
    }

    /**
     * Obtiene todos los comentarios del usuario
     * @return Lista de comentarios
     */
    public List<ComentarioComunidad> getComentarios() {
        return new ArrayList<>(comentarios);
    }
}