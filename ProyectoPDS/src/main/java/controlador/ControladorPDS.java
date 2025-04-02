package controlador;

import modelado.Curso;
import modelado.CursoEnMarcha;
import modelado.Estrategia;
import modelado.RepositorioUsuarios;
import modelado.Usuario;

public class ControladorPDS {
	

	// Instancia única
	private static ControladorPDS unicaInstancia;
	
	// Usuario conectado
	private Usuario sesionActual;
	
	// Repositorio donde se almacenan los usuarios registrados en la aplicación
	private RepositorioUsuarios repositorioUsuarios;
	
	//Constructor
	private ControladorPDS() {
		sesionActual = null;
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
	public boolean registrarUsuario(String nombre, String contraseña, String correo) {
		Usuario usuario = new Usuario(nombre, contraseña, correo);
		if (repositorioUsuarios.getUsuario(usuario.getNombre()) != null) {
			return false;
		}
		repositorioUsuarios.addUsuario(usuario);
		return true;
	}
	
	// Accede al repositorio de Usuarios utilizando el nombre del usuario introducido.
	// Si existe un Usuario vinculado y su contraseña corresponde a la introducida,
	// se inicia sesion correctamente.
	public boolean login (String nombre, String contraseña) {
		Usuario usuario = repositorioUsuarios.getUsuario(nombre);
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
	
	// Método para devolver el usuario conectado
	public Usuario getSesionActual() {
		return sesionActual;
	}
	
	//Tras seleccionar un curso, lo convierte en un curso en marcha
	public void iniciarCurso(Curso curso, Estrategia estrategia) {
		sesionActual.agregarCurso(curso, CursoEnMarcha.VIDAS_PREDETERMINADAS, estrategia);
	}
	
	//En teoría el usuario debe seleccionar una estrategia. Esta función se ha creado para mostrar la funcionalidad básica del programa.
	public void iniciarCurso(Curso curso) {
		sesionActual.agregarCurso(curso, CursoEnMarcha.VIDAS_PREDETERMINADAS, CursoEnMarcha.ESTRATEGIA_PREDETERMINADA);
	}
}
