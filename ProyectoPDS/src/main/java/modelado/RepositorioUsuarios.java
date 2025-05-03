package modelado;



import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


/**
 * Clase que representa un repositorio de usuarios en la aplicación.
 * 
 * Implementa el patrón Singleton para garantizar que solo haya una única instancia
 * del repositorio a lo largo de la ejecución del programa.
 */
public class RepositorioUsuarios {
	
	// Instancia única del repositorio (Singleton)
    private static RepositorioUsuarios unicaInstancia = new RepositorioUsuarios();
    private EntityManagerFactory emf;
    
    //Devuelve la instancia única del repositorio.
    public static RepositorioUsuarios getUnicaInstancia() {
    	if (unicaInstancia == null)
			unicaInstancia = new RepositorioUsuarios();
		return unicaInstancia;
    }
    
    private RepositorioUsuarios() {
    	emf = Persistence.createEntityManagerFactory("repositorioUsuarios");
    	//LocalDate cumple = LocalDate.of(2004, 12, 03);
    	//Usuario usuarioE = new Usuario("Ramón", "1234", "ramon@um.es", "ramonPRO",cumple);
    	//guardarUsuario(usuarioE);
    }
    
    public void guardarUsuario(Usuario usuario) {
    	EntityManager em = emf.createEntityManager();
    	try {
    		em.getTransaction().begin();
    		em.persist(usuario);
    		em.getTransaction().commit();
    	} catch (Exception e) {
    		em.getTransaction().rollback();
    		e.printStackTrace();
    	} finally {
    		em.close();
    	}
    }
   
    public Usuario getUsuario(String nombreUsuario) {
    	EntityManager em = emf.createEntityManager();

        try {
        	TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.nombreUsuario LIKE :nombreUsuario", Usuario.class)
                    .setParameter("nombreUsuario", nombreUsuario);
        	return query.getSingleResult();
        } catch (NoResultException e) {
        	return null;
        } finally {
            em.close();
        }
    }

    public void actualizarCurso(CursoEnMarcha cursoEnMarcha) {
    	EntityManager em = emf.createEntityManager();
    	 try {
         	
             em.getTransaction().begin();

             em.merge(cursoEnMarcha);
             
             em.getTransaction().commit();
         } catch (Exception e) {
             if (em.getTransaction().isActive()) {
                 em.getTransaction().rollback();
             }
             e.printStackTrace();
         } finally {
             em.close();
         }
    }
    
    public void agregarCurso(Long id, CursoEnMarcha cursoEnMarcha) {
        EntityManager em = emf.createEntityManager();
        Usuario usuario = em.find(Usuario.class, id);
        Curso curso = null;
        if (cursoEnMarcha.getCurso().getIdDB() != null) {
        	curso = em.find(Curso.class, cursoEnMarcha.getCurso().getIdDB());	//Como varios Cursos en Marcha comparten el mismo atributo Curso, es necesario manejar su persistencia por separado.
             
        }
        try {
            em.getTransaction().begin();
            
            if (curso == null) {
            	em.persist(cursoEnMarcha.getCurso());
            }
            cursoEnMarcha.setUsuario(usuario); 
            usuario.getCursosActivos().add(cursoEnMarcha);
            
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


}
