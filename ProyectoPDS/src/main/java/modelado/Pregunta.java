package modelado;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "preguntas")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "tipo_usuario",
discriminatorType = DiscriminatorType.STRING)
public abstract class Pregunta {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private int numPregunta;
	private String enunciado;
	@ManyToOne
	@JoinColumn(name="usuario_id")
	private Usuario usuario;
	
	public Pregunta(int numPregunta, String enunciado) {
		this.numPregunta = numPregunta;
		this.enunciado = enunciado;
	}

	public Pregunta() {
	}

	public abstract boolean verificarRespuesta(String respuesta);

	public String getEnunciado() {
		return enunciado;
	}

	public int getNumPregunta() {
		return numPregunta;
	}

	public void setNumPregunta(int numPregunta) {
		this.numPregunta = numPregunta;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Pregunta pregunta = (Pregunta) obj;

		return numPregunta == pregunta.numPregunta;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(numPregunta);
	}

}
