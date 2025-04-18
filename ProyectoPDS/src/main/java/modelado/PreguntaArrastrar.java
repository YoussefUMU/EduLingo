package modelado;

import java.util.List;

public class PreguntaArrastrar extends Pregunta{

    private String enunciado;
    private String[] fragmentos;
    private List<String> opciones;
    private List<Integer> ordenCorrecto;

    // Constructor
    public PreguntaArrastrar() {
        // Initialize with some default values if necessary
    }

    // Getters and Setters
    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String[] getFragmentos() {
        return fragmentos;
    }

    public void setFragmentos(String[] fragmentos) {
        this.fragmentos = fragmentos;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }

    public List<Integer> getOrdenCorrecto() {
        return ordenCorrecto;
    }

    public void setOrdenCorrecto(List<Integer> ordenCorrecto) {
        this.ordenCorrecto = ordenCorrecto;
    }

	@Override
	public boolean verificarRespuesta(String respuesta) {
		// TODO Auto-generated method stub
		return false;
	}
}
