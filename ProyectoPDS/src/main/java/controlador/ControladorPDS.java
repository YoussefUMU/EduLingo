package controlador;

import java.time.LocalDate;
import java.util.List;

import modelado.Curso;
import modelado.CursoEnMarcha;
import modelado.Estrategia;
import modelado.EstrategiaAleatoria;
import modelado.EstrategiaEspaciada;
import modelado.EstrategiaSecuencial;
import modelado.ManejadorCursos;
import modelado.RepositorioUsuarios;
import modelado.Usuario;

public class ControladorPDS {
	

	// Instancia única
	private static ControladorPDS unicaInstancia;
	
	// Usuario conectado
	private Usuario sesionActual;
	
	// Repositorio donde se almacenan los usuarios registrados en la aplicación
	private RepositorioUsuarios repositorioUsuarios;
	
	
	 private ManejadorCursos manejador = new ManejadorCursos();
	
	//Constructor
	private ControladorPDS() {
		sesionActual = null;
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
	public boolean registrarUsuario(String nombre, String contraseña, String correo, String nombreUsuario, LocalDate FechaNacimiento) {
		Usuario usuario = new Usuario(nombre, contraseña, correo, nombreUsuario, FechaNacimiento);
		if (repositorioUsuarios.getUsuario(usuario.getNombre()) != null) {
			return false;
		}
		repositorioUsuarios.addUsuario(usuario);
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
		return true;
	}
	
	public boolean cerrarSesion () {
		if(sesionActual != null) {
			sesionActual = null;
			return true;
		}
		return false;
	}
	
	// Método para devolver el usuario conectado
	public Usuario getSesionActual() {
		return sesionActual;
	}
	
	// Nuevo método para iniciar curso con una estrategia específica
	public CursoEnMarcha iniciarCursoE(Curso curso, Estrategia estrategia) {
		sesionActual.agregarCurso(curso, CursoEnMarcha.VIDAS_PREDETERMINADAS, estrategia);

		return sesionActual.obtenerCursoEnMarcha(curso.getId()).get();
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
	
	// Añadir estos métodos al ControladorPDS.java

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
	 * Verifica si el usuario tiene el beneficio de acceso a cursos adicionales
	 * @return true si el usuario tiene acceso a cursos adicionales, false en caso contrario
	 */
	public boolean tieneCursosAdicionales() {
	    return sesionActual != null && sesionActual.tieneCursosAdicionales();
	}
}