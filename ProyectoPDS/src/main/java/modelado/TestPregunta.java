package modelado;

import java.util.List;

public class TestPregunta extends Pregunta {
    private List<String> opciones;
    private String respuestaCorrecta;
    
	 public TestPregunta() {
	    }
	
	

 public TestPregunta(String enunciado, List<String> opciones, String respuestaCorrecta) {
     super(enunciado);
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