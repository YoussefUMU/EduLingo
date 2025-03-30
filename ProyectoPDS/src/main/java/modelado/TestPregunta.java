package modelado;

import java.util.List;

public class TestPregunta extends Pregunta {
    private List<String> opciones;
    public List<String> getOpciones() {
		return opciones;
	}

	public void setOpciones(List<String> opciones) {
		this.opciones = opciones;
	}

	private String respuestaCorrecta;

    public TestPregunta(String id, String enunciado, List<String> opciones, String respuestaCorrecta) {
        super(id, enunciado);
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    @Override
    public boolean verificarRespuesta(String respuesta) {
        return respuestaCorrecta.equalsIgnoreCase(respuesta);
    }
}