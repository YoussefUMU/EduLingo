package modelado;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "bloques")
public class Bloque {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String titulo;
	@OneToMany(cascade={ CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	@JoinColumn(name="bloque_id")
	private List<Pregunta> preguntas;
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas = preguntas;
	}

	public Bloque() {
	}

	public Bloque(String titulo, List<Pregunta> preguntas) {
		this.titulo = titulo;
		this.preguntas = preguntas;
	}

	public Pregunta obtenerPregunta(int numPregunta) {
		for(Pregunta P: preguntas) {
			if(P.getNumPregunta()==numPregunta) {
				System.out.println(P.getEnunciado());
				return P;
			}
		}return null;
	}

	public List<Pregunta> getPreguntas() {
		return preguntas;
	}
	
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;

	    Bloque bloque = (Bloque) obj;

	    return titulo != null ? titulo.equals(bloque.titulo) : bloque.titulo == null;
	}

	@Override
	public int hashCode() {
	    return titulo != null ? titulo.hashCode() : 0;
	}

}