package modelado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import controlador.ControladorPDS;
import modelado.Curso;
import modelado.CursoEnMarcha;
import modelado.Estrategia;
import modelado.TipoEstrategia;
import modelado.Usuario;

class UsuarioTest {
	private Usuario usuario;
	private Curso curso;
	private Estrategia estrategia;
	private TipoEstrategia tipoEstrategia = TipoEstrategia.SECUENCIAL;
	private MockedStatic<ControladorPDS> controladorMock;
	private ControladorPDS controlador;

	@BeforeEach
	void setUp() {
		usuario = new Usuario("Juan", "pass", "juan@mail.com", "juan23", LocalDate.of(2000, 1, 1));

		// Mock Curso
		curso = mock(Curso.class);
		// verdaderamente debería devolver un long, por eso algunos test no se pueden
		// hacer
		when(curso.getId()).thenReturn("1L");
		when(curso.getNombre()).thenReturn("Curso A");
		when(curso.getDescripcion()).thenReturn("Desc A");

		// Mock ControladorPDS estático
		controladorMock = mockStatic(ControladorPDS.class);
		controlador = mock(ControladorPDS.class);
		controladorMock.when(() -> ControladorPDS.getUnicaInstancia()).thenReturn(controlador);
		when(controlador.getNombreCursoEnMarcha(any(CursoEnMarcha.class))).thenReturn("Curso A");
		when(controlador.getDescripcionCursoEnMarcha(any(CursoEnMarcha.class))).thenReturn("Desc A");
	}

	@AfterEach
	void tearDown() {
		controladorMock.close();
	}

	@Test
	void testAgregarCursoEvitaDuplicadosMismoEstrategia() {
		// Añade con EstrategiaAleatoria
		Estrategia aleatoria = new EstrategiaAleatoria();
		assertTrue(usuario.agregarCurso(curso, 3, aleatoria, TipoEstrategia.ALEATORIA));
		// No permite duplicar con misma clase de estrategia
		boolean addedAgain = usuario.agregarCurso(curso, 5, aleatoria, TipoEstrategia.ALEATORIA);
		assertFalse(addedAgain);
		assertEquals(1, usuario.getCursosActivos().size());
	}

	@Test
	void testAgregarCursoDistintasEstrategiasPermitido() {
		// Añade con EstrategiaAleatoria
		Estrategia aleatoria = new EstrategiaAleatoria();
		assertTrue(usuario.agregarCurso(curso, 3, aleatoria, TipoEstrategia.ALEATORIA));
		// Añade con EstrategiaSecuencial (distinta clase)
		Estrategia secuencial = new EstrategiaSecuencial();
		assertTrue(usuario.agregarCurso(curso, 4, secuencial, TipoEstrategia.SECUENCIAL));
		// Añade con EstrategiaEspaciada
		Estrategia espaciada = new EstrategiaEspaciada();
		assertTrue(usuario.agregarCurso(curso, 2, espaciada, TipoEstrategia.ESPACIADA));
		assertEquals(3, usuario.getCursosActivos().size());
	}

	@Test
	void testObtenerCursoEnMarchaPorEstrategia() {
		Estrategia espaciada = new EstrategiaEspaciada();
		usuario.agregarCurso(curso, 1, espaciada, TipoEstrategia.ESPACIADA);

		Optional<CursoEnMarcha> found = usuario.obtenerCursoEnMarcha(curso, espaciada);
		assertTrue(found.isPresent());
		assertEquals(curso, found.get().getCurso());
		assertEquals(espaciada.getClass(), found.get().getEstrategia().getClass());
	}

	@Test
	void testObtenerCursosActivosInmutable() {
		Estrategia secuencial = new EstrategiaSecuencial();
		usuario.agregarCurso(curso, 1, secuencial, TipoEstrategia.SECUENCIAL);
		List<CursoEnMarcha> lista = usuario.obtenerCursosActivos();
		// Modificar lista externa no afecta interior
		lista.clear();
		assertFalse(usuario.getCursosActivos().isEmpty());
	}

	@Test
	void testPremiumLifecycle() {
		assertFalse(usuario.esPremium());
		usuario.activarPremium("mensual");
		assertTrue(usuario.esPremium());
		assertTrue(usuario.tieneVidasInfinitas());
		usuario.cancelarPremium();
		assertFalse(usuario.esPremium());
	}

	@Test
	void testGetRangoSegunNivel() {
		Estadistica stats = mock(Estadistica.class);
		usuario.setEstadisticas(stats);
		when(stats.getNivelActual()).thenReturn(2);
		assertEquals("Aprendiz Entusiasta", usuario.getRango());
		when(stats.getNivelActual()).thenReturn(4);
		assertEquals("Estudiante Constante", usuario.getRango());
		when(stats.getNivelActual()).thenReturn(6);
		assertEquals("Experto Junior", usuario.getRango());
		when(stats.getNivelActual()).thenReturn(10);
		assertEquals("Maestro del Conocimiento", usuario.getRango());
	}

	// No se puede por tema id
	/*
	 * @Test void testComentariosCrud() { ComentarioComunidad c1 =
	 * usuario.añadirComentario("Texto1", "tag1"); assertNotNull(c1);
	 * assertEquals(1, usuario.getComentarios().size());
	 * 
	 * boolean edited = usuario.editarComentario(c1.getId(), "TextoMod");
	 * assertTrue(edited); assertEquals("TextoMod",
	 * usuario.getComentarios().get(0).getTexto());
	 * 
	 * boolean removed = usuario.eliminarComentario(c1.getId());
	 * assertTrue(removed); assertTrue(usuario.getComentarios().isEmpty()); }
	 */

	@Test
	void testLogrosYEstadisticas() {
		Estadistica stats = mock(Estadistica.class);
		when(stats.getLogrosDesbloqueados()).thenReturn(List.of("L1", "L2"));
		when(stats.esLogroDesbloqueado("L1")).thenReturn(true);
		usuario.setEstadisticas(stats);

		List<String> logros = usuario.getLogrosDesbloqueados();
		assertEquals(2, logros.size());
		assertTrue(usuario.tieneLogroDesbloqueado("L1"));
	}
}
