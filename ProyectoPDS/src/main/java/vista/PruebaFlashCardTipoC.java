package vista;

import java.time.LocalDate;
import modelado.CursoEnMarcha;
import modelado.Curso;
import modelado.Usuario;

public class PruebaFlashCardTipoC {
    
    public static void main(String[] args) {
        // Crear objetos mínimos necesarios para la prueba
        Curso curso = new Curso("Curso de Prueba", "Descripción de prueba", "Prueba");
        Usuario usuario = new Usuario("Usuario de prueba", "contraseña", "correo@um.es");
        CursoEnMarcha cursoEnMarcha = new CursoEnMarcha(curso);
        
        // Establecer vidas para el curso en marcha
        cursoEnMarcha.setVidas(3);
        
        // Crear e instanciar la FlashCardTipoC
        // Los índices 0, 0 son placeholders ya que usamos datos de prueba dentro de la flashcard
        FlashCardTipoC flashcard = new FlashCardTipoC(cursoEnMarcha, 0, 0);
        
        // Mostrar la ventana
        flashcard.setVisible(true);
    }
}