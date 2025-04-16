package modelado;



import java.time.LocalDate;
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
    	LocalDate cumple = LocalDate.of(2004, 12, 03);
    	Usuario usuarioE = new Usuario("Ramón", "1234", "ramon@um.es", "ramonPRO",cumple);
    	usuarios.put(usuarioE.getNombreUsuario(), usuarioE);
    }
    
    public void addUsuario(Usuario usuario) {
    	usuarios.put(usuario.getNombreUsuario(), usuario);
    }
    
    public Usuario getUsuario(String userName) {
    	return usuarios.get(userName);
    }
}
