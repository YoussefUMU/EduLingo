package modelado;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class UsuarioTest {
    private Usuario usuario;
    private Curso curso1;
    private Curso curso2;
    private Estrategia estrategia1;
    private Estrategia estrategia2;

    @BeforeEach
    /*void setUp() {
        usuario = new Usuario("1", "Juan Pérez", "password123", "juan@example.com", "juanperez");
        curso1 = new Curso("1", "Matemáticas", "Curso básico de matemáticas");
        curso2 = new Curso("2", "Programación", "Introducción a la programación");
        
        estrategia1 = new EstrategiaSecuencial(); 
        estrategia2 = new EstrategiaSecuencial();
    }*/

    @Test
    void testAgregarCurso() {
        //assertTrue(usuario.agregarCurso(curso1, 3, estrategia1));
        assertEquals(1, usuario.getCursosActivos().size());
        
        // Intentar agregar el mismo curso con la misma estrategia debería fallar
       // assertFalse(usuario.agregarCurso(curso1, 3, estrategia1));
        
        // Agregar otro curso diferente debería funcionar
        //assertTrue(usuario.agregarCurso(curso2, 5, estrategia2));
        assertEquals(2, usuario.getCursosActivos().size());
    }

    @Test
    void testFinalizarCurso() {
        //usuario.agregarCurso(curso1, 3, estrategia1);
        CursoEnMarcha cursoEnMarcha = usuario.getCursosActivos().get(0);
        
        usuario.finalizarCurso(cursoEnMarcha);
        assertTrue(usuario.getCursosActivos().isEmpty());
    }

    @Test
    void testObtenerCursosActivos() {
       // usuario.agregarCurso(curso1, 3, estrategia1);
       // usuario.agregarCurso(curso2, 5, estrategia2);
        
        List<CursoEnMarcha> cursos = usuario.obtenerCursosActivos();
        assertEquals(2, cursos.size());
    }

   /* @Test
    void testObtenerCursoEnMarcha() {
        usuario.agregarCurso(curso1, 3, estrategia1);
        String cursoId = usuario.getCursosActivos().get(0).getId();
        
        Optional<CursoEnMarcha> cursoOpt = usuario.obtenerCursoEnMarcha(cursoId);
        assertTrue(cursoOpt.isPresent());
        assertEquals(cursoId, cursoOpt.get().getId());
        
        // Buscar un curso que no existe
        assertFalse(usuario.obtenerCursoEnMarcha("curso-inexistente").isPresent());
    }

    @Test
    void testIniciarCurso() {
        usuario.agregarCurso(curso1, 3, estrategia1);
        String cursoId = usuario.getCursosActivos().get(0).getId();
        
        usuario.iniciarCurso(cursoId);
        Optional<CursoEnMarcha> cursoOpt = usuario.obtenerCursoEnMarcha(cursoId);
        assertTrue(cursoOpt.isPresent());
    }

    @Test
    void testAvanzarEnCurso() {
        usuario.agregarCurso(curso1, 3, estrategia1);
        String cursoId = usuario.getCursosActivos().get(0).getId();
        usuario.avanzarEnCurso(cursoId);
        Optional<CursoEnMarcha> cursoOpt = usuario.obtenerCursoEnMarcha(cursoId);
        assertTrue(cursoOpt.isPresent());

    }


    @Test
    void testObtenerEstadisticas() {
        Estadistica estadisticas = usuario.obtenerEstadisticas();
        assertNotNull(estadisticas);
    }*/
}