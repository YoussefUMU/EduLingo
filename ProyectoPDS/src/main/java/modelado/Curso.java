package modelado;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Curso {
    private String id;
    private String nombre;
    private String autor;
    private String descripcion;
    private List<Bloque> bloques;
    private URL imagenCurso;
    
    public Curso() {}

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

	public Curso(String id, String nombre, String autor, String descripcion, List<Bloque> bloques) {
        this.id = id;
        this.nombre = nombre;
        this.autor = autor;
        this.descripcion = descripcion;
        this.bloques = bloques;
    }


    public String getId() {
        return id;
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

	public URL getImagenCurso() {
		return imagenCurso;
	}

	public void setImagenCurso(URL imagenCurso) {
		this.imagenCurso = imagenCurso;
	}
	
	public void setImagenCurso(String imagenCurso) {
		try {
			this.imagenCurso = new URL(imagenCurso);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
