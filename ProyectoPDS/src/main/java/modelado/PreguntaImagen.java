package modelado;

import java.util.List;

public class PreguntaImagen extends Pregunta {
    private List<String> imagenes;
    private List<String> textos;
    private int imagenCorrecta;

    public PreguntaImagen() {
        super();
    }
    
    public PreguntaImagen(String enunciado, List<String> imagenes, List<String> textos, int imagenCorrecta) {
        super(enunciado);
        this.imagenes = imagenes;
        this.textos = textos;
        this.imagenCorrecta = imagenCorrecta;
    }

    @Override
    public boolean verificarRespuesta(String respuesta) {
        try {
            int seleccion = Integer.parseInt(respuesta);
            return seleccion == imagenCorrecta;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public List<String> getTextos() {
        return textos;
    }

    public void setTextos(List<String> textos) {
        this.textos = textos;
    }

    public int getImagenCorrecta() {
        return imagenCorrecta;
    }

    public void setImagenCorrecta(int imagenCorrecta) {
        this.imagenCorrecta = imagenCorrecta;
    }
}