package modelado;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
@Entity
public class Curso {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id_db;
	private String id;
    private String nombre;
    private String autor;
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "curso_id")
    private List<Bloque> bloques;
    private String imagenCurso;
    private String categoria;
    public Curso() {
        this.bloques = new ArrayList<>();
    }
    
    // Constructor simple para la vista de cursos sin empezar
    public Curso(String nombre, String descripcion, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.bloques = new ArrayList<>();
        this.imagenCurso = "https://cdn-icons-png.flaticon.com/512/2000/2000860.png";
        
        // Autor por defecto
        this.autor = "Profesor";
    }
  
    public void setId(String id) {
        this.id = id;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public void setBloques(List<Bloque> bloques) {
        this.bloques = bloques;
    }
    
    // Constructor completo con URL para imagen
    public Curso(String id, String nombre, String autor, String descripcion, List<Bloque> bloques, String imagenCurso) {
        this.id = id;
        this.nombre = nombre;
        this.autor = autor;
        this.descripcion = descripcion; 
        this.bloques = bloques != null ? bloques : new ArrayList<>();
        this.imagenCurso = "/recursos/EdulingoRedimensionadad.png";
    }
    
    // Constructor básico sin imagen
    public Curso(String id, String nombre, String autor, String descripcion, List<Bloque> bloques) {
        this.id = id;
        this.nombre = nombre;
        this.autor = autor;
        this.descripcion = descripcion;
        this.bloques = bloques != null ? bloques : new ArrayList<>();
    }


	public String getId() {
		return id;
	}

	public Long getIdDB() {
		return id_db;
	}
	
	public String getNombre() {
		return nombre;
	}

	public String getAutor() {
		return autor;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public List<Bloque> getBloques() {
		return bloques;
	}
	
	public Bloque getBloqueEspecifico(int index) {
		return this.bloques.get(index);
	}

	public String getImagenCurso() {
		return imagenCurso;
	}

	public void setImagenCurso(String imagenCurso) {
		this.imagenCurso = imagenCurso;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return nombre;
	}

	public void setCursoEnMarcha(CursoEnMarcha cursoEnMarcha) {
		//this.cursoEnMarcha = cursoEnMarcha;
		
	}
}