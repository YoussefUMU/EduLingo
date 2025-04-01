package modelado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Clase que representa a un usuario de la plataforma de aprendizaje.
 */
public class Usuario {
    private final String id;
    private String nombre;
    private String correo;
    private LocalDate fechaRegistro;
    private List<CursoEnMarcha> cursosActivos;
    private List<Estadistica> estadisticas;

    public Usuario(String id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.fechaRegistro = LocalDate.now();
        this.cursosActivos = new ArrayList<>();
        this.estadisticas = new ArrayList<>();
    }

    public void agregarCurso(Curso curso) {
        cursosActivos.add(new CursoEnMarcha(curso));
    }

    public void finalizarCurso(CursoEnMarcha cursoEnMarcha) {
        cursoEnMarcha.finalizar();
        estadisticas.add(new Estadistica(this, cursoEnMarcha.getCurso()));
    }

    public List<CursoEnMarcha> obtenerCursosActivos() {
        return cursosActivos.stream()
                .filter(CursoEnMarcha::estaEnProgreso)
                .collect(Collectors.toList());
    }
    
    public Optional<CursoEnMarcha> obtenerCursoEnMarcha(String cursoId) {
        return cursosActivos.stream().filter(c -> c.getCurso().getId().equals(cursoId)).findFirst();
    }
    
    public void iniciarCurso(String cursoId) {
        obtenerCursoEnMarcha(cursoId).ifPresent(CursoEnMarcha::reiniciarCurso);
    }
    
    public void avanzarEnCurso(String cursoId) {
        obtenerCursoEnMarcha(cursoId).ifPresent(CursoEnMarcha::avanzarPregunta);
    }
    
    public List<Estadistica> obtenerEstadisticas() {
        return estadisticas;
    }
    
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
}