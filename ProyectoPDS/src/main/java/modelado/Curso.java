package modelado;

import java.util.List;

public class Curso {
    private final String id;
    private String nombre;
    private String descripcion;
    private List<Bloque> bloques;
    private Estrategia estrategia;

    public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Estrategia getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(Estrategia estrategia) {
		this.estrategia = estrategia;
	}

	public Curso(String id, String nombre, String descripcion, List<Bloque> bloques, Estrategia estrategia) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.bloques = bloques;
        this.estrategia = estrategia;
    }

    public Bloque obtenerSiguienteBloque(int actual) {
        return estrategia.siguiente(bloques, actual);
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
}
