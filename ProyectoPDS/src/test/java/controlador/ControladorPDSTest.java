package controlador;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import modelado.*;

class ControladorPDSTest {

    @Mock
    private RepositorioUsuarios mockRepo;

    @Mock
    private Usuario mockUsuario;

    @Mock
    private ManejadorCursos mockManejador;

    private ControladorPDS controlador;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        // Reiniciar la instancia Singleton de ControladorPDS para cada test
        Field instancia = ControladorPDS.class.getDeclaredField("unicaInstancia");
        instancia.setAccessible(true);
        instancia.set(null, null);

        // Crear nueva instancia e inyectar repositorio y manejador mockeados
        controlador = ControladorPDS.getUnicaInstancia();
        Field repoField = ControladorPDS.class.getDeclaredField("repositorioUsuarios");
        repoField.setAccessible(true);
        repoField.set(controlador, mockRepo);
        Field manejadorField = ControladorPDS.class.getDeclaredField("manejador");
        manejadorField.setAccessible(true);
        manejadorField.set(controlador, mockManejador);
    }

    // Test de registro de usuario: verifica que devuelve true y guarda el usuario
    @Test
    void testRegistrarUsuario_Success() {
        when(mockRepo.getUsuario("juanP")).thenReturn(null);

        boolean result = controlador.registrarUsuario(
            "Juan", "pass123", "juan@example.com", "juanP", LocalDate.of(2000,1,1));

        assertTrue(result, "Debe retornar true al registrar un usuario inexistente");
        verify(mockRepo).guardarUsuario(any(Usuario.class));
    }

    // Test de registro de usuario duplicado: no guarda y retorna false
    @Test
    void testRegistrarUsuario_AlreadyExists() {
        when(mockRepo.getUsuario("juanP")).thenReturn(mockUsuario);

        boolean result = controlador.registrarUsuario(
            "Juan", "pass123", "juan@example.com", "juanP", LocalDate.of(2000,1,1));

        assertFalse(result, "Debe retornar false si el nombre de usuario ya existe");
        verify(mockRepo, never()).guardarUsuario(any());
    }

    // Test de login exitoso: sesión activa y estadísticas accesibles
    @Test
    void testLogin_Success() {
        when(mockRepo.getUsuario("jose")).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("secret");
        when(mockUsuario.getEstadisticas()).thenReturn(new Estadistica());

        boolean ok = controlador.login("jose", "secret");

        assertTrue(ok, "El login debe ser exitoso con credenciales correctas");
        assertEquals(mockUsuario, controlador.getSesionActual(), "La sesión actual debe ser el usuario logueado");
        assertNotNull(controlador.getEstadisticas(), "Las estadísticas no deben ser null tras el login");
    }

    // Test de login con contraseña incorrecta: no inicia sesión
    @Test
    void testLogin_WrongPassword() {
        when(mockRepo.getUsuario("jose")).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("secret");

        boolean ok = controlador.login("jose", "wrong");

        assertFalse(ok, "El login debe fallar con contraseña incorrecta");
        assertNull(controlador.getSesionActual(), "La sesión debe permanecer null si falla el login");
    }

    // Test de login de usuario inexistente: no inicia sesión
    @Test
    void testLogin_NoSuchUser() {
        when(mockRepo.getUsuario("noexiste")).thenReturn(null);

        boolean ok = controlador.login("noexiste", "pass");

        assertFalse(ok, "El login debe fallar si el usuario no existe");
        assertNull(controlador.getSesionActual(), "La sesión debe permanecer null si no hay usuario");
    }

    // Test de cierre de sesión sin sesión previa: retorna false
    @Test
    void testCerrarSesion_NoSession() {
        boolean closed = controlador.cerrarSesion();
        assertFalse(closed, "Cerrar sesión sin sesión activa debe retornar false");
    }

    // Test de cierre de sesión con sesión activa:
    // - Simula 10s de uso, verifica incrementarTiempoUso y limpieza de sesión
    @Test
    void testCerrarSesion_WithSession() throws Exception {
        when(mockRepo.getUsuario("ana")).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("pwd");
        Estadistica stats = new Estadistica();
        when(mockUsuario.getEstadisticas()).thenReturn(stats);

        // Iniciar sesión
        controlador.login("ana", "pwd");
        // Retroceder inicioSesionActual 10 segundos
        Field startField = ControladorPDS.class.getDeclaredField("inicioSesionActual");
        startField.setAccessible(true);
        startField.set(controlador, LocalDateTime.now().minusSeconds(10));

        // Ejecutar cierre de sesión
        boolean closed = controlador.cerrarSesion();

        assertTrue(closed, "Cerrar sesión con sesión activa debe retornar true");
        verify(mockUsuario).getEstadisticas();
        assertEquals(10, stats.getTiempoUso(), "El tiempo de uso debe incrementarse en 10 segundos");
        assertNull(controlador.getSesionActual(), "La sesión actual debe quedar en null after cierre");
    }

    // Test del método numCursosActivos: agrupa por categoría correctamente
    @Test
    void testNumCursosActivos_ComputedCorrectly() {
        when(mockRepo.getUsuario(anyString())).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("pwd");
        when(mockUsuario.getEstadisticas()).thenReturn(new Estadistica());
        controlador.login("u", "pwd");

        // Crear mocks de cursos activos con categorías 'catA' y 'catB'
        Curso cursoA = mock(Curso.class);
        when(cursoA.getCategoria()).thenReturn("catA");
        Curso cursoB = mock(Curso.class);
        when(cursoB.getCategoria()).thenReturn("catB");
        CursoEnMarcha em1 = mock(CursoEnMarcha.class);
        when(em1.getCurso()).thenReturn(cursoA);
        CursoEnMarcha em2 = mock(CursoEnMarcha.class);
        when(em2.getCurso()).thenReturn(cursoB);
        CursoEnMarcha em3 = mock(CursoEnMarcha.class);
        when(em3.getCurso()).thenReturn(cursoA);
        when(mockUsuario.getCursosActivos()).thenReturn(Arrays.asList(em1, em2, em3));

        Map<String,Integer> counts = controlador.numCursosActivos();

        assertEquals(2, counts.size(), "Deben existir dos categorías agrupadas");
        assertEquals(2, counts.get("catA"), "Categoria 'catA' debe contar 2 cursos");
        assertEquals(1, counts.get("catB"), "Categoria 'catB' debe contar 1 curso");
    }

    // Test de flags premium y vidas infinitas tras login
    @Test
    void testEsPremium_And_VidasInfinitas() {
        when(mockRepo.getUsuario(anyString())).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("pwd");
        when(mockUsuario.getEstadisticas()).thenReturn(new Estadistica());
        when(mockUsuario.esPremium()).thenReturn(true);
        when(mockUsuario.tieneVidasInfinitas()).thenReturn(false);

        controlador.login("user", "pwd");

        assertTrue(controlador.esPremium(), "Usuario debe ser premium");
        assertFalse(controlador.tieneVidasInfinitas(), "Usuario no debe tener vidas infinitas");
    }

    // Test de registrarRespuestaPregunta: verifica llamada a usuario y repositorio
    @Test
    void testRegistrarRespuestaPregunta_PersisteEstadistica() {
        when(mockRepo.getUsuario(anyString())).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("1234");
        // Usamos spy para stats para comprobar incremento
        Estadistica stats = spy(new Estadistica());
        when(mockUsuario.getEstadisticas()).thenReturn(stats);
        controlador.login("u", "1234");

        // Registrar respuesta correcta
        controlador.registrarRespuestaPregunta(true);

        // Verificaciones
        verify(mockUsuario).registrarRespuestaPregunta(true);
        verify(mockRepo).actualizarEstadisticas(stats);
    }

        // Test de iniciar curso con estrategia: crea CursoEnMarcha y persiste en repositorio
    @Test
    void testIniciarCursoE_Success() {
        // Preparar login
        when(mockRepo.getUsuario(anyString())).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("pwd");
        when(mockUsuario.getEstadisticas()).thenReturn(new Estadistica());
        controlador.login("u", "pwd");

        // Crear curso con ID como String (setter existente)
        Curso curso = new Curso();
        curso.setId("curso1");
        curso.setNombre("Curso1");
        curso.setDescripcion("Desc");
        Estrategia estrategia = new EstrategiaSecuencial();
        TipoEstrategia tipo = TipoEstrategia.SECUENCIAL;
        CursoEnMarcha esperado = new CursoEnMarcha(curso, CursoEnMarcha.VIDAS_PREDETERMINADAS, estrategia, tipo);

        // Stub de usuario: agregarCurso y obtenerCursoEnMarcha
        when(mockUsuario.agregarCurso(eq(curso), anyInt(), eq(estrategia), eq(tipo))).thenReturn(true);
        when(mockUsuario.obtenerCursoEnMarcha(eq(curso), eq(estrategia))).thenReturn(Optional.of(esperado));

        // Ejecutar método
        CursoEnMarcha resultado = controlador.iniciarCursoE(curso, estrategia, tipo);

        // Verificaciones
        assertNotNull(resultado, "Debe devolver instancia de CursoEnMarcha al iniciar curso");
        assertEquals(esperado, resultado, "El retorno debe coincidir con el curso en marcha esperado");
        verify(mockRepo).agregarCurso(anyLong(), eq(esperado));
    }

    // Test de finalizarCursoEnMarcha: invoca finalización y actualiza usuario
    @Test
    void testFinalizarCursoEnMarcha_PersistenciaUsuario() {
        when(mockRepo.getUsuario(anyString())).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("pwd");
        controlador.login("u", "pwd");

        CursoEnMarcha cem = mock(CursoEnMarcha.class);
        controlador.finalizarCursoEnMarcha(cem);

        verify(mockUsuario).finalizarCurso(cem);
        verify(mockRepo).actualizarUsuario(mockUsuario);
    }

    // Tests para gestión de comentarios de comunidad
    @Test
    void testPublicarComentario_PersisteCorrectamente() {
        when(mockRepo.getUsuario(anyString())).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("pwd");
        controlador.login("u", "pwd");

        ComentarioComunidad comentario = new ComentarioComunidad();
        when(mockUsuario.añadirComentario("texto", "etiqueta")).thenReturn(comentario);

        ComentarioComunidad res = controlador.publicarComentario("texto", "etiqueta");
        assertEquals(comentario, res, "Debe devolver el comentario creado");
        verify(mockRepo).guardarComentario(comentario);
    }

    @Test
    void testEditarComentario_ActualizaComentario() {
        when(mockRepo.getUsuario(anyString())).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("pwd");
        controlador.login("u", "pwd");

        ComentarioComunidad c = mock(ComentarioComunidad.class);
        when(c.getId()).thenReturn(10L);
        when(mockUsuario.editarComentario(10L, "nuevo")).thenReturn(true);
        when(mockUsuario.getComentarios()).thenReturn(Arrays.asList(c));

        boolean ok = controlador.editarComentario(10L, "nuevo");
        assertTrue(ok, "editarComentario debe retornar true si se edita correctamente");
        verify(mockRepo).actualizarComentario(c);
    }

    @Test
    void testEliminarComentario_LimpiaCorrectamente() {
        when(mockRepo.getUsuario(anyString())).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("pwd");
        controlador.login("u", "pwd");

        when(mockUsuario.eliminarComentario(5L)).thenReturn(true);

        boolean ok = controlador.eliminarComentario(5L);
        assertTrue(ok, "eliminarComentario debe retornar true al eliminar");
        verify(mockRepo).eliminarComentario(5L);
    }

    @Test
    void testObtenerTodosComentarios_DevuelveListaDeRepo() {
        List<ComentarioComunidad> lista = Arrays.asList(new ComentarioComunidad(), new ComentarioComunidad());
        when(mockRepo.obtenerTodosComentarios()).thenReturn(lista);

        List<ComentarioComunidad> res = controlador.obtenerTodosComentarios();
        assertEquals(lista, res, "Debe devolver la lista obtenida del repositorio");
    }

    // Tests de suscripción premium: activar y cancelar
    @Test
    void testActivarPremium_PersisteBien() {
        when(mockRepo.getUsuario(anyString())).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("pwd");
        controlador.login("u", "pwd");

        boolean ok = controlador.activarPremium("mensual");
        assertTrue(ok, "activarPremium debe retornar true sin errores");
        verify(mockRepo).activarPremium(anyLong(), eq("mensual"));
    }

    @Test
    void testCancelarPremium_PersisteBien() {
        when(mockRepo.getUsuario(anyString())).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("pwd");
        controlador.login("u", "pwd");

        boolean ok = controlador.cancelarPremium();
        assertTrue(ok, "cancelarPremium debe retornar true sin errores");
        verify(mockRepo).cancelarPremium(anyLong());
    }

    // Test de numCursosCompletados: agrupa correctamente cursos completados por categoría
    @Test
    void testNumCursosCompletados_ComputedCorrectly() {
        when(mockRepo.getUsuario(anyString())).thenReturn(mockUsuario);
        when(mockUsuario.getContraseña()).thenReturn("pwd");
        when(mockUsuario.getEstadisticas()).thenReturn(new Estadistica());
        controlador.login("u", "pwd");

        Curso c1 = mock(Curso.class); when(c1.getCategoria()).thenReturn("catX");
        Curso c2 = mock(Curso.class); when(c2.getCategoria()).thenReturn("catY");
        when(mockUsuario.getCursosCompletados()).thenReturn(Arrays.asList(c1, c2, c1));

        Map<String,Integer> counts = controlador.numCursosCompletados();
        assertEquals(2, counts.get("catX"), "catX debe contar 2 cursos completados");
        assertEquals(1, counts.get("catY"), "catY debe contar 1 curso completado");
    }

    // Test de obtenerCursosLocales: delega al ManejadorCursos
    @Test
    void testObtenerCursosLocales_DelegacionCorrecta() {
        // Preparar lista de cursos
        Curso cursoA = new Curso("idA", "Curso A", "Autor", "Desc A", Collections.emptyList(), null);
        Curso cursoB = new Curso("idB", "Curso B", "Autor", "Desc B", Collections.emptyList(), null);
        List<Curso> locales = Arrays.asList(cursoA, cursoB);
        when(mockManejador.obtenerCursosLocales()).thenReturn(locales);

        List<Curso> resultado = controlador.obtenerCursosLocales();
        assertEquals(locales, resultado, "Debe devolver la lista provista por el manejador de cursos");
    }

    // Tests de nombre y descripción de CursoEnMarcha: usa el curso interno
    @Test
    void testGetNombreYDescripcionCursoEnMarcha() {
        Curso curso = new Curso("idC", "Nombre Curso", "Autor", "Descripción Detallada", Collections.emptyList(), null);
        CursoEnMarcha cem = new CursoEnMarcha(curso, CursoEnMarcha.VIDAS_PREDETERMINADAS,
                                              CursoEnMarcha.ESTRATEGIA_PREDETERMINADA,
                                              CursoEnMarcha.TIPO_ESTRATEGIA_PREDETERMINADA);

        String nombre = controlador.getNombreCursoEnMarcha(cem);
        String descripcion = controlador.getDescripcionCursoEnMarcha(cem);

        assertEquals("Nombre Curso", nombre, "getNombreCursoEnMarcha debe devolver el nombre del curso");
        assertEquals("Descripción Detallada", descripcion, "getDescripcionCursoEnMarcha debe devolver la descripción del curso");
    }
}
