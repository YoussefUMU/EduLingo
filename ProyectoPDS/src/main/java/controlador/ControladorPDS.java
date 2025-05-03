package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import modelado.Curso;
import modelado.CursoEnMarcha;
import modelado.Estadistica;
import modelado.Estrategia;
import modelado.GestorLogros;
import modelado.ManejadorCursos;
import modelado.RepositorioUsuarios;
import modelado.TipoEstrategia;
import modelado.Usuario;

public class ControladorPDS {
	

	// Instancia única
	private static ControladorPDS unicaInstancia;
	
	// Usuario conectado
	private Usuario sesionActual;
	
	// Tiempo de inicio de sesión
    private LocalDateTime inicioSesionActual;

	// Repositorio donde se almacenan los usuarios registrados en la aplicación
	private RepositorioUsuarios repositorioUsuarios;
	
	
	 private ManejadorCursos manejador = new ManejadorCursos();
	
	//Constructor
	private ControladorPDS() {
		sesionActual = null;
        inicioSesionActual = null;
        repositorioUsuarios = RepositorioUsuarios.getUnicaInstancia(); 
	}
	
	// Implementación del patrón Singleton: devuelve la única instancia de la clase.
	public static ControladorPDS getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new ControladorPDS();
		return unicaInstancia;
	}
	
	/*
	 * Crear usuario con la información introducida y comprobar si dicho usuario ya
	 * existe.
	 * 
	 */
	public boolean registrarUsuario(String nombre, String contraseña, String correo, String nombreUsuario, LocalDate fechaNacimiento) {
		Usuario usuario = new Usuario(nombre, contraseña, correo, nombreUsuario, fechaNacimiento);
		if (repositorioUsuarios.getUsuario(usuario.getNombreUsuario()) != null) {
			return false;
		}
		repositorioUsuarios.guardarUsuario(usuario);
		return true;
	}
	
	
	// Accede al repositorio de Usuarios utilizando el nombre del usuario introducido.
	// Si existe un Usuario vinculado y su contraseña corresponde a la introducida,
	// se inicia sesion correctamente.
	public boolean login (String nombreUsuario, String contraseña) {
		Usuario usuario = repositorioUsuarios.getUsuario(nombreUsuario);
		if (usuario == null) {
			return false;
		}
		//Escrito de esta manera por si nos interesa diferenciar entre un caso y otro en el futuro.
		if (!usuario.getContraseña().equals(contraseña)) {
			return false;
		}
		sesionActual = usuario;
		
        inicioSesionActual = LocalDateTime.now();
		return true;
	}
	
    public boolean cerrarSesion() {
        if(sesionActual != null) {
            // Calcular el tiempo de esta sesión
            if(inicioSesionActual != null) {
                LocalDateTime finSesion = LocalDateTime.now();
                long segundosTranscurridos = java.time.Duration.between(inicioSesionActual, finSesion).getSeconds();
                // Actualizar estadísticas del usuario
                sesionActual.getEstadisticas().incrementarTiempoUso((int)segundosTranscurridos);
            }
            sesionActual = null;
            inicioSesionActual = null;
            return true;
        }
        return false;
    }
	
	// Método para devolver el usuario conectado
	public Usuario getSesionActual() {
		return sesionActual;
	}
	
    /**
     * Obtiene el objeto Estadistica del usuario de la sesión actual
     * @return Objeto Estadistica del usuario o null si no hay sesión activa
     */
    public Estadistica getEstadisticas() {
        if (sesionActual == null) {
            return null;
        }
        return sesionActual.getEstadisticas();
    }
	
	// Nuevo método para iniciar curso con una estrategia específica
	public CursoEnMarcha iniciarCursoE(Curso curso, Estrategia estrategia, TipoEstrategia tipoEstrategia) {
		if (sesionActual.agregarCurso(curso, CursoEnMarcha.VIDAS_PREDETERMINADAS, estrategia, tipoEstrategia)) {
			repositorioUsuarios.agregarCurso(sesionActual.getId(), sesionActual.obtenerCursoEnMarcha(curso, estrategia).get());
			return sesionActual.obtenerCursoEnMarcha(curso, estrategia).get();
		}
		return null;
	}

	
	//Nuevo metodo para finalizar un curso
	public void finalizarCursoEnMarcha(CursoEnMarcha curso) {
		this.sesionActual.finalizarCurso(curso);
	}
	
	public List<Curso> obtenerCursosLocales(){
		return manejador.obtenerCursosLocales();
	}
	
	public String getNombreCursoEnMarcha(CursoEnMarcha curso) {
		return curso.getCurso().getNombre();
	}
	
	public String getDescripcionCursoEnMarcha(CursoEnMarcha curso) {
		return curso.getCurso().getDescripcion();
	}
	
	public List<CursoEnMarcha> getCursosActivosSesionActual() {
		return this.sesionActual.getCursosActivos();
	}
	

	/**
	 * Activa la suscripción premium para el usuario que tiene la sesión activa
	 * @param tipoPlan Tipo de plan (mensual, anual)
	 * @return true si se activó correctamente, false en caso contrario
	 */
	public boolean activarPremium(String tipoPlan) {
	    if (sesionActual == null) {
	        return false;
	    }
	    
	    try {
	        sesionActual.activarPremium(tipoPlan);
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/**
	 * Cancela la suscripción premium del usuario que tiene la sesión activa
	 * @return true si se canceló correctamente, false en caso contrario
	 */
	public boolean cancelarPremium() {
	    if (sesionActual == null) {
	        return false;
	    }
	    
	    try {
	        sesionActual.cancelarPremium();
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/***
	 * Método para obtener un mapa asociando la categoría del curso con el número de cursos que tiene activos un usuario
	 * @return mapa categoría curso - número de cursoa
	 */
	
	public Map<String, Integer> numCursosActivos() {
	    List<CursoEnMarcha> cursosActivos = getCursosActivosSesionActual();
	    
	    Map<String, Integer> resultado = cursosActivos.stream()
	        .collect(Collectors.groupingBy(
	            curso -> curso.getCurso().getCategoria(),
	            Collectors.summingInt(curso -> 1)
	        ));
	    return resultado;
	}
	/**
	 * Verifica si el usuario de la sesión actual tiene suscripción premium activa
	 * @return true si el usuario tiene premium activo, false en caso contrario
	 */
	public boolean esPremium() {
	    return sesionActual != null && sesionActual.esPremium();
	}

	/**
	 * Verifica si el usuario tiene el beneficio de vidas infinitas
	 * @return true si el usuario tiene vidas infinitas, false en caso contrario
	 */
	public boolean tieneVidasInfinitas() {
	    return sesionActual != null && sesionActual.tieneVidasInfinitas();
	}
	/**
     * Registra una respuesta a una pregunta
     * @param correcta Si la respuesta fue correcta
     */
    public void registrarRespuestaPregunta(boolean correcta) {
        if (sesionActual != null) {
            sesionActual.registrarRespuestaPregunta(correcta);
        }
    }
    
    /**
     * Obtiene el nivel actual del usuario
     */
    public int getNivelUsuario() {
        if (sesionActual == null) return 1;
        return sesionActual.getNivel();
    }
    
    /**
     * Obtiene el porcentaje de progreso en el nivel actual
     */
    public int getPorcentajeNivel() {
        if (sesionActual == null) return 0;
        return sesionActual.getPorcentajeNivel();
    }
    
    /**
     * Obtiene el rango actual del usuario
     */
    public String getRangoUsuario() {
        if (sesionActual == null) return "Aprendiz Entusiasta";
        return sesionActual.getRango();
    }
    
    /**
     * Obtiene la información de todos los logros
     */
    public Map<String, GestorLogros.LogroInfo> getTodosLosLogros() {
        return GestorLogros.getTodosLosLogros();
    }
    
    /**
     * Verifica si el usuario tiene un logro desbloqueado
     */
    public boolean tieneLogroDesbloqueado(String idLogro) {
        if (sesionActual == null) return false;
        return sesionActual.tieneLogroDesbloqueado(idLogro);
    }
    
    /**
     * Obtiene la lista de IDs de logros desbloqueados
     */
    public List<String> getLogrosDesbloqueados() {
        if (sesionActual == null) return new ArrayList<>();
        return sesionActual.getLogrosDesbloqueados();
    }
	/**
	 * Verifica si el usuario tiene el beneficio de acceso a cursos adicionales
	 * @return true si el usuario tiene acceso a cursos adicionales, false en caso contrario
	 */
	public boolean tieneCursosAdicionales() {
	    return sesionActual != null && sesionActual.tieneCursosAdicionales();
	}
	

	/**
	 * Actualiza un curso en marcha en la base de datos
	 */
	
	public void actualizarCursoEnMarcha(CursoEnMarcha cursoEnMarcha) {
		repositorioUsuarios.actualizarCurso(cursoEnMarcha);
	}
    public void actualizarEstadisticasTiempo() {
        if(sesionActual != null && inicioSesionActual != null) {
            LocalDateTime ahora = LocalDateTime.now();
            long segundosTranscurridos = java.time.Duration.between(inicioSesionActual, ahora).getSeconds();
            // Actualizar estadísticas del usuario
            sesionActual.getEstadisticas().incrementarTiempoUso((int)segundosTranscurridos);
            // Reiniciar el contador
            inicioSesionActual = ahora;
        }
    }
}