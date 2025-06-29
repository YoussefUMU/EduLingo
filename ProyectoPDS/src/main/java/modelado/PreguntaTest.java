package modelado;

import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "preguntas_test")
@DiscriminatorValue("TEST")
public class PreguntaTest extends Pregunta {
	private List<String> opciones;
	private String respuestaCorrecta;

	public PreguntaTest() {
	}

	public PreguntaTest(int numPregunta, String enunciado, List<String> opciones, String respuestaCorrecta) {
		super(numPregunta, enunciado);
		this.opciones = opciones;
		this.respuestaCorrecta = respuestaCorrecta;
	}

	public List<String> getOpciones() {
		return opciones;
	}

	public void setOpciones(List<String> opciones) {
		this.opciones = opciones;
	}

	public void setRespuestaCorrecta(String respuestaCorrecta) {
		this.respuestaCorrecta = respuestaCorrecta;
	}

	public String getRespuestaCorrecta() {
		return respuestaCorrecta;
	}

	@Override
	public boolean verificarRespuesta(String respuesta) {
		return respuestaCorrecta.equalsIgnoreCase(respuesta);
	}
}