package modelado;

import java.util.List;
import java.util.Objects;

public class PreguntaArrastrar extends Pregunta {
    private String textoCompleto;
    private List<String> huecos;
    private List<Integer> ordenCorrecto;

    public PreguntaArrastrar() {
        super();
    }
    
    public PreguntaArrastrar(int numPregunta,String enunciado, String textoCompleto, List<String> huecos) {
        super(numPregunta,enunciado);
        this.textoCompleto = textoCompleto;
        this.huecos = huecos;
    }

    @Override
    public boolean verificarRespuesta(String respuesta) {
        if (ordenCorrecto == null) return false;
        
        try {
            String[] respuestas = respuesta.split(",");
            if (respuestas.length != ordenCorrecto.size()) return false;
            
            for (int i = 0; i < respuestas.length; i++) {
                int indice = Integer.parseInt(respuestas[i]);
                if (indice != ordenCorrecto.get(i)) return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getTextoCompleto() {
        return textoCompleto;
    }

    public void setTextoCompleto(String textoCompleto) {
        this.textoCompleto = textoCompleto;
    }

    public List<String> getHuecos() {
        return huecos;
    }

    public void setHuecos(List<String> huecos) {
        this.huecos = huecos;
    }

    public List<Integer> getOrdenCorrecto() {
        return ordenCorrecto;
    }

    public void setOrdenCorrecto(List<Integer> ordenCorrecto) {
        this.ordenCorrecto = ordenCorrecto;
    }
}