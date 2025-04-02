package modelado;



import java.util.HashMap;
import java.util.Map;


/**
 * Clase que representa un repositorio de usuarios en la aplicación.
 * 
 * Implementa el patrón Singleton para garantizar que solo haya una única instancia
 * del repositorio a lo largo de la ejecución del programa.
 */
public class RepositorioUsuarios {
	
	// Instancia única del repositorio (Singleton)
    private static RepositorioUsuarios unicaInstancia = new RepositorioUsuarios();
    
    //Repositorio temporal. String = id, aunque por simplicidad uso el nombre por ahora.
    private Map<String, Usuario> usuarios;
    
    //Devuelve la instancia única del repositorio.
    public static RepositorioUsuarios getUnicaInstancia() {
    	if (unicaInstancia == null)
			unicaInstancia = new RepositorioUsuarios();
		return unicaInstancia;
    }
    
    private RepositorioUsuarios() {
    	usuarios = new HashMap<String, Usuario>();
    }
    
    public void addUsuario(Usuario usuario) {
    	usuarios.put(usuario.getNombre(), usuario);
    }
    
    public Usuario getUsuario(String usuario) {
    	return usuarios.get(usuario);
    }
}
