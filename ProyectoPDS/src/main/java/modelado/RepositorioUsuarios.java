package modelado;



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
    public void actualizarUsuario(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario);
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
        try {
            em.getTransaction().begin();
            
            em.persist(cursoEnMarcha.getCurso());
            
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

    public void activarPremium(Long id, String tipoPlan) {
    	EntityManager em = emf.createEntityManager();
        Usuario usuario = em.find(Usuario.class, id);
        try {
            em.getTransaction().begin();
            
            usuario.activarPremium(tipoPlan);
            
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

    public void actualizarEstadisticas(Estadistica estadisticas) {
    	EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            
            em.merge(estadisticas);
            
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
    
    public void cancelarPremium(Long id) {
        EntityManager em = emf.createEntityManager();
        Usuario usuario = em.find(Usuario.class, id);
        try {
            em.getTransaction().begin();
            
            // Marcamos premium como no renovable, pero mantenemos hasta fecha fin
            Premium premium = usuario.getPremium();
            premium.setActivo(false);
            em.merge(premium);
            
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
    /**
     * Guarda un nuevo comentario en la base de datos
     * @param comentario El comentario a guardar
     */
    public void guardarComentario(ComentarioComunidad comentario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(comentario);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza un comentario existente
     * @param comentario El comentario a actualizar
     */
    public void actualizarComentario(ComentarioComunidad comentario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(comentario);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Elimina un comentario
     * @param comentarioId ID del comentario a eliminar
     */
    public void eliminarComentario(Long comentarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            ComentarioComunidad comentario = em.find(ComentarioComunidad.class, comentarioId);
            if (comentario != null) {
                em.remove(comentario);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todos los comentarios de la comunidad
     * @return Lista de todos los comentarios
     */
    public List<ComentarioComunidad> obtenerTodosComentarios() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ComentarioComunidad> query = em.createQuery(
                "SELECT c FROM ComentarioComunidad c ORDER BY c.fecha DESC", 
                ComentarioComunidad.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
