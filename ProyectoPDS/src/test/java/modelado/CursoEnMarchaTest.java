package modelado;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

class CursoEnMarchaTest {
   /* private Curso curso;
    private CursoEnMarcha cursoEnMarcha;
    private Bloque bloque1, bloque2;
    private PreguntaSimple pregunta1, pregunta2, pregunta3;*/

   /* @BeforeEach
    void setUp() {
        // Configuración inicial para las pruebas usando las clases actualizadas
        pregunta1 = new PreguntaSimple("Pregunta 1", "respuesta1");
        pregunta2 = new PreguntaSimple("Pregunta 2", "respuesta2");
        pregunta3 = new PreguntaSimple("Pregunta 3", "respuesta3");
        
        bloque1 = new Bloque("Bloque 1", Arrays.asList(pregunta1, pregunta2));
        bloque2 = new Bloque("Bloque 2", Arrays.asList(pregunta3));
        
        curso = new Curso("1", "Curso de prueba", "Autor", "Descripción", 
                         Arrays.asList(bloque1, bloque2), "imagen.jpg");
        
        cursoEnMarcha = new CursoEnMarcha(curso, 3, new EstrategiaSecuencial());
    }*/

    // Clase auxiliar para testing
   /* class PreguntaSimple extends Pregunta {
        private String respuestaCorrecta;
        
        public PreguntaSimple(String enunciado, String respuestaCorrecta) {
            super(enunciado);
            this.respuestaCorrecta = respuestaCorrecta;
        }

        @Override
        public boolean verificarRespuesta(String respuesta) {
            return respuestaCorrecta.equals(respuesta);
        }
    }

    @Test
    void testConstructor() {
        assertEquals(0, cursoEnMarcha.getBloqueActualIndex());
        assertEquals(0, cursoEnMarcha.getPreguntaActualIndex());
        assertEquals(3, cursoEnMarcha.getVidas());
        assertNotNull(cursoEnMarcha.getEstrategia());
    }

    @Test
    void testAvanzarPregunta_DentroDelMismoBloque() {
        // Primera pregunta
        Pregunta preguntaActual = cursoEnMarcha.getPreguntaActual();
        assertEquals("Pregunta 1", preguntaActual.getEnunciado());
        
        // Avanzar a segunda pregunta
        cursoEnMarcha.avanzarPregunta();
        assertEquals(0, cursoEnMarcha.getBloqueActualIndex());
        assertEquals(1, cursoEnMarcha.getPreguntaActualIndex());
        assertEquals("Pregunta 2", cursoEnMarcha.getPreguntaActual().getEnunciado());
    }

    @Test
    void testAvanzarPregunta_CambioDeBloque() {
        // Posicionar en última pregunta del primer bloque
        cursoEnMarcha.setPreguntaActual(1);
        
        // Avanzar debería cambiar al siguiente bloque
        cursoEnMarcha.avanzarPregunta();
        assertEquals(1, cursoEnMarcha.getBloqueActualIndex());
        assertEquals(0, cursoEnMarcha.getPreguntaActualIndex());
        assertEquals("Pregunta 3", cursoEnMarcha.getPreguntaActual().getEnunciado());
    }

    @Test
    void testAvanzarPregunta_FinDelCurso() {
        // Posicionar en última pregunta del último bloque
        cursoEnMarcha.setBloqueActual(1);
        cursoEnMarcha.setPreguntaActual(0);
        
        // Avanzar debería finalizar el curso
        cursoEnMarcha.avanzarPregunta();
        // Verificar que se llamó al método finalizar (podrías usar un mock para verificar esto)
    }

    @Test
    void testReiniciarCurso() {
        // Avanzar algunas preguntas
        cursoEnMarcha.avanzarPregunta();
        cursoEnMarcha.avanzarPregunta();
        
        // Reiniciar
        cursoEnMarcha.reiniciarCurso();
        assertEquals(0, cursoEnMarcha.getBloqueActualIndex());
        assertEquals(0, cursoEnMarcha.getPreguntaActualIndex());
    }


    @Test
    void testSetVidas() {
        cursoEnMarcha.setVidas(5);
        assertEquals(5, cursoEnMarcha.getVidas());
    }

    @Test
    void testObtenerPreguntaDesdeBloque() {
        Bloque bloque = cursoEnMarcha.getBloqueActual();
        assertNotNull(bloque);
        
        Pregunta pregunta = bloque.obtenerPregunta(0);
        assertNotNull(pregunta);
        assertEquals("Pregunta 1", pregunta.getEnunciado());
    }*/
}