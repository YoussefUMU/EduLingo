package modelado;

import java.net.URL;
import java.util.List;

public class Curso {
    private final String id;
    private String nombre;
    private DescripcionCurso descripcion;
    private List<Bloque> bloques;
    private URL imagenCurso;
    
    public DescripcionCurso getDescripcion() {
		return this.descripcion;
	}

	public Curso(String id, String nombre, DescripcionCurso descripcion, List<Bloque> bloques) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.bloques = bloques;
    }


    public String getId() {
        return id;
    }
    
    public String getNombre() {
        return nombre;
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
}
