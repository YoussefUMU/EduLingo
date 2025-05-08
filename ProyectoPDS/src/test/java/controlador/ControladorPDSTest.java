package controlador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import modelado.*;
import modelado.GestorLogros.LogroInfo;

class ControladorPDSTest {
	private ControladorPDS ctrl;
	private RepositorioUsuarios repoMock;
	private ManejadorCursos manejadorMock;
	private MockedStatic<RepositorioUsuarios> repoStatic;
	private MockedStatic<ManejadorCursos> manejadorStatic;
	private MockedStatic<GestorLogros> logrosStatic;

	@BeforeEach
	void setUp() throws Exception {
		// Reset singleton
		java.lang.reflect.Field f = ControladorPDS.class.getDeclaredField("unicaInstancia");
		f.setAccessible(true);
		f.set(null, null);

		// Mock repositorio estático
		repoStatic = mockStatic(RepositorioUsuarios.class);
		repoMock = mock(RepositorioUsuarios.class);
		repoStatic.when(RepositorioUsuarios::getUnicaInstancia).thenReturn(repoMock);

		// Mock manejador cursos
		manejadorMock = mock(ManejadorCursos.class);
		// usar spy vía inyección directa
		ctrl = ControladorPDS.getUnicaInstancia();
		java.lang.reflect.Field mField = ControladorPDS.class.getDeclaredField("manejador");
		mField.setAccessible(true);
		mField.set(ctrl, manejadorMock);

		// Mock GestorLogros
		logrosStatic = mockStatic(GestorLogros.class);
	}

	@AfterEach
	void tearDown() {
		repoStatic.close();
		logrosStatic.close();
	}

	@Test
    void testRegistrarYLoginYCerrarSesion() {
        // registrar ok
        when(repoMock.getUsuario("u1")).thenReturn(null);
        assertTrue(ctrl.registrarUsuario("n","p","c","u1", LocalDate.now()));
        verify(repoMock).guardarUsuario(any());

        // registro duplicado
        when(repoMock.getUsuario("u1")).thenReturn(new Usuario());
        assertFalse(ctrl.registrarUsuario("n","p","c","u1", LocalDate.now()));

        // login fail usuario no existe
        when(repoMock.getUsuario("none")).thenReturn(null);
        assertFalse(ctrl.login("none","pw"));

        // login incorrecto
        Usuario u = new Usuario("nom","pw","c","u2", LocalDate.now());
        when(repoMock.getUsuario("u2")).thenReturn(u);
        assertFalse(ctrl.login("u2","bad"));

        assertTrue(ctrl.login("u2","pw"));
        assertEquals(u, ctrl.getSesionActual());

        // cerrar sesión suma tiempo
        LocalDateTime start = ctrl.inicioSesionActual;
        // simular +2 segundos
        ctrl.inicioSesionActual = start.minusSeconds(2);
        assertTrue(ctrl.cerrarSesion());
        assertNull(ctrl.getSesionActual());
        assertNull(ctrl.inicioSesionActual);

        // cerrar sin sesión
        assertFalse(ctrl.cerrarSesion());
    }

	@Test
	void testEstadisticasSesionYNivelYRango() {
		Usuario u = new Usuario("n", "p", "c", "u3", LocalDate.now());
		when(repoMock.getUsuario("u3")).thenReturn(u);
		ctrl.login("u3", "p");
		// getEstadisticas != null
		assertNotNull(ctrl.getEstadisticas());
		// nivel y porcentaje y rango sin sesión?
		assertEquals(u.getNivel(), ctrl.getNivelUsuario());
		assertEquals(u.getPorcentajeNivel(), ctrl.getPorcentajeNivel());
		assertEquals(u.getRango(), ctrl.getRangoUsuario());
		ctrl.cerrarSesion();
		assertNull(ctrl.getEstadisticas());
		assertEquals(1, ctrl.getNivelUsuario());
		assertEquals(0, ctrl.getPorcentajeNivel());
		assertEquals("Aprendiz Entusiasta", ctrl.getRangoUsuario());
	}

	@Test
	void testIniciarYFinalizarCurso() {
		// preparar usuario y repositorio
		Usuario u = new Usuario("n", "p", "c", "u4", LocalDate.now());
		when(repoMock.getUsuario("u4")).thenReturn(u);
		// hacer login
		assertTrue(ctrl.login("u4", "p"));

		// mockear un Curso con categoría
		Curso curso = mock(Curso.class);
		when(curso.getCategoria()).thenReturn("cat");

		// iniciar curso
		CursoEnMarcha cem = ctrl.iniciarCursoE(curso, new EstrategiaSecuencial(), TipoEstrategia.SECUENCIAL);
		assertNotNull(cem, "Debe devolver un CursoEnMarcha al iniciar");
		// verificar que se llamó a repositorio.agregarCurso con el objeto correcto
		verify(repoMock).agregarCurso(eq(u.getId()), eq(cem));

		// finalizar curso
		ctrl.finalizarCursoEnMarcha(cem);
		// tras finalizar, debe actualizar el usuario completo
		verify(repoMock).actualizarUsuario(u);
	}

	@Test
	void testObtenerListasYCategorias() {
		Usuario u = new Usuario("n", "p", "c", "u5", LocalDate.now());
		when(repoMock.getUsuario("u5")).thenReturn(u);
		ctrl.login("u5", "p");
		// preparar cursos en marcha y completados
		CursoEnMarcha cem1 = mock(CursoEnMarcha.class);
		Curso c1 = mock(Curso.class), c2 = mock(Curso.class);
		when(cem1.getCurso()).thenReturn(c1);
		when(c1.getCategoria()).thenReturn("A");
		u.getCursosActivos().add(cem1);
		u.getCursosCompletados().add(c2);
		when(c2.getCategoria()).thenReturn("B");
		// num categorias
		Map<String, Integer> act = ctrl.numCursosActivos();
		assertEquals(1, act.get("A"));
		Map<String, Integer> comp = ctrl.numCursosCompletados();
		assertEquals(1, comp.get("B"));
	}

	@Test
	void testPremiumYLogros() {
		Usuario u = new Usuario("n", "p", "c", "u6", LocalDate.now());
		when(repoMock.getUsuario("u6")).thenReturn(u);
		ctrl.login("u6", "p");
		// activar premium
		doNothing().when(repoMock).activarPremium(u.getId(), "mensual");
		assertTrue(ctrl.activarPremium("mensual"));
		// esPremium depende de Usuario.esPremium()
		assertFalse(ctrl.esPremium());
		// cancelar premium sin sesión?
		ctrl.cerrarSesion();
		assertFalse(ctrl.cancelarPremium());
	}

	@Test
	void testPublicarEditarEliminarComentario() throws Exception {
		Usuario u = new Usuario("n", "p", "c", "u7", LocalDate.now());
		when(repoMock.getUsuario("u7")).thenReturn(u);
		ctrl.login("u7", "p");
		// publicar
		ComentarioComunidad com = ctrl.publicarComentario("t", "e");
		assertNotNull(com);
		verify(repoMock).guardarComentario(com);
		Long id = com.getId();
		// editar
		assertTrue(ctrl.editarComentario(id, "nuevo"));
		verify(repoMock).actualizarComentario(argThat(c -> c.getTexto().equals("nuevo")));
		// eliminar
		assertTrue(ctrl.eliminarComentario(id));
		verify(repoMock).eliminarComentario(id);
	}

	@Test
	void testObtenerTodosComentarios() {
		List<ComentarioComunidad> lista = List.of(new ComentarioComunidad());
		when(repoMock.obtenerTodosComentarios()).thenReturn(lista);
		assertEquals(lista, ctrl.obtenerTodosComentarios());
	}

	@Test
	void testRegistrarRespuestaYPersistir() {
		Usuario u = new Usuario("n", "p", "c", "u8", LocalDate.now());
		when(repoMock.getUsuario("u8")).thenReturn(u);
		ctrl.login("u8", "p");
		ctrl.registrarRespuestaPregunta(true);
		verify(repoMock).actualizarEstadisticas(u.getEstadisticas());
	}

	/*@Test
	void testGetTodosLosLogros() {
		Map<String, LogroInfo> dummy = Map.of("X", new LogroInfo("X", "t", "d"));
		logrosStatic.when(GestorLogros::getTodosLosLogros).thenReturn(dummy);
		assertEquals(dummy, ctrl.getTodosLosLogros());
	}*/
}
