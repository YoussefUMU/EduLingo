package modelado;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import controlador.ControladorPDS;

class CursoEnMarchaTest {
	private CursoEnMarcha cem;
	private Curso curso;
	private MockedStatic<ControladorPDS> ctrlMock;
	private ControladorPDS ctrl;

	// Bloques de prueba
	private Bloque bloque1, bloque2;
	private PreguntaTest p1, p2, p3;

	@BeforeEach
	void setUp() {
		// 1) Mockear controlador estático
		ctrlMock = mockStatic(ControladorPDS.class);
		ctrl = mock(ControladorPDS.class);
		ctrlMock.when(ControladorPDS::getUnicaInstancia).thenReturn(ctrl);

		// 2) Crear preguntas y bloques
		p1 = new PreguntaTest(1, "P1", List.of("A", "B"), "A");
		p2 = new PreguntaTest(2, "P2", List.of("A", "B"), "A");
		p3 = new PreguntaTest(1, "P3", List.of("A", "B"), "A");

		bloque1 = new Bloque("B1", List.of(p1, p2));
		bloque2 = new Bloque("B2", List.of(p3));

		// 3) Mockear Curso para devolver estos bloques
		curso = mock(Curso.class);
		when(curso.getBloques()).thenReturn(List.of(bloque1, bloque2));
		when(curso.getBloqueEspecifico(0)).thenReturn(bloque1);
		when(curso.getBloqueEspecifico(1)).thenReturn(bloque2);

		// 4) Instanciar CursoEnMarcha con 3 vidas y estrategia secuencial
		cem = new CursoEnMarcha(curso, 3, new EstrategiaSecuencial(), TipoEstrategia.SECUENCIAL);
	}

	@AfterEach
	void tearDown() {
		ctrlMock.close();
	}

	@Test
	void testConstructorInicializaCorrectamente() {
		assertEquals(curso, cem.getCurso());
		assertEquals(3, cem.getVidas());
		assertEquals(0, cem.getBloqueActualIndex());
		assertEquals(1, cem.getPreguntaActualIndex());
		assertTrue(cem.getEstrategia() instanceof EstrategiaSecuencial);
		assertEquals(TipoEstrategia.SECUENCIAL, cem.getTipoEstrategia());
	}

	@Test
	void testRegistrarRespuesta_invocaControlador() {
		cem.registrarRespuesta(true);
		verify(ctrl).registrarRespuestaPregunta(true);
	}

	@Test
	void testReiniciarCurso() {
		// Avanzamos para modificar índices
		cem.reiniciarCurso();
		assertEquals(0, cem.getBloqueActualIndex());
		assertEquals(0, cem.getPreguntaActualIndex());
	}

	@Test
	void testGetPreguntaYBloqueActual() {
		// Inicialmente bloque 0, pregunta 1 → getPreguntaActual → p1
		assertEquals(bloque1, cem.getBloqueActual());
		assertEquals(p1, cem.getPreguntaActual());
	}

	@Test
	void testAvanzarPreguntaDentroDelBloque() {
		// Al avanzar desde pregunta 1 en bloque1, debe pasar a la 2
		cem.avanzarPregunta();
		assertEquals(0, cem.getBloqueActualIndex());
		assertEquals(2, cem.getPreguntaActualIndex());
		// La pregunta 1 debe quedar registrada como completada
		// (internamente en la lista de PreguntasCompletas)
		// No hay acceso público directo, pero al volver a avanzar:
		cem.avanzarPregunta(); // ya no hay más en bloque1 → salta de bloque
		// Comprueba que resetea el índice de pregunta y avanza bloque
		assertEquals(1, cem.getBloqueActualIndex());
		assertEquals(1, cem.getPreguntaActualIndex());
	}

	@Test
	void testAvanzarPreguntaAlFinalDeBloque() {
		// Inicialmente: bloqueActualIndex = 0, preguntaActualIndex = 1
		// Avanzamos dos veces:
		cem.avanzarPregunta(); // de pregunta 1 → 2 en el mismo bloque
		cem.avanzarPregunta(); // fin de bloque1 → pasa a bloque2, pregunta = 1

		assertEquals(1, cem.getBloqueActualIndex(), "Debería haber avanzado al bloque 2 (índice 1)");
		assertEquals(1, cem.getPreguntaActualIndex(), "La pregunta debería reiniciarse a la 1 en el nuevo bloque");
	}

	@Test
	void testObtenerSiguienteBloqueYPregunta() {
		// Con estrategia secuencial:
		assertEquals(bloque2, cem.obtenerSiguienteBloque(0));
		assertEquals(p2, cem.obtenerSiguientePregunta(1));
	}

	@Test
    void testFinalizarLlamaRegistrarActividad() {
        // Stubear el final de curso: situar en último bloque y última pregunta
        // Forzamos que nextBlock sea null:
        when(ctrl.getEstadisticas()).thenReturn(mock(Estadistica.class));
        // Llamamos a finalizar
        cem.finalizar();
        // Debe invocar registro de actividad
        verify(ctrl.getEstadisticas()).registrarActividad();
    }
}
