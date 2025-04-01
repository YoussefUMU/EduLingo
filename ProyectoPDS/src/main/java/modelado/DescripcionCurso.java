package modelado;

public class DescripcionCurso {
	private final Usuario autor;
	private final String textoDescripcion;
	
	public DescripcionCurso(Usuario autor, String textoDescripcion) {
		super();
		this.autor = autor;
		this.textoDescripcion = textoDescripcion;
	}

	public Usuario getAutor() {
		return autor;
	}

	public String getTextoDescripcion() {
		return textoDescripcion;
	}
	
	
	
}
