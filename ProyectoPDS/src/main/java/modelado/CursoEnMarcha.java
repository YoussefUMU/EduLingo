package modelado;


public class CursoEnMarcha extends Curso {
	
    public static final int VIDAS_PREDETERMINADAS = 5;
	public static final Estrategia ESTRATEGIA_PREDETERMINADA = new EstrategiaSecuencial();
	
	public int bloqueActual;
    public int preguntaActual;
    private int vidas;
    private Estrategia estrategia;
    
    

    public CursoEnMarcha(Curso curso,int vidas, Estrategia estrategia) {
		super(curso.getId(), curso.getNombre(), curso.getDescripcion(), curso.getBloques());
		this.bloqueActual = 1;
		this.preguntaActual = 1;
		this.vidas = vidas;
		this.estrategia = estrategia;
	}

	public void avanzarPregunta() {
        Bloque bloque =obtenerSiguienteBloque(bloqueActual);
        if (preguntaActual < bloque.getPreguntas().size() - 1) {
            preguntaActual++;
        } else {
            preguntaActual = 0;
            if (bloqueActual < super.getBloques().size() - 1) {
                bloqueActual++;
            } else {
                finalizar();
            }
        }
    }

    public void reiniciarCurso() {
        this.bloqueActual = 0;
        this.preguntaActual = 0;
    }
    
    public Pregunta getPreguntaActual() {
        return super.getBloques().get(bloqueActual).getPreguntas().get(preguntaActual);
    }
    
    public Bloque getBloqueActual() {
        return super.getBloques().get(bloqueActual);
    }

    public Bloque obtenerSiguienteBloque(int actual) {
        return estrategia.siguiente(super.getBloques(), actual);
    }
    public void finalizar() {
       
    }


	public int getVidas() {
		return vidas;
	}

	public void setVidas(int vidas) {
		this.vidas = vidas;
	}

}

