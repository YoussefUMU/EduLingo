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
    private String contraseña;
    private String nombreUsuario;
    private LocalDate fechaRegistro;
    private List<CursoEnMarcha> cursosActivos;
    private Estadistica estadisticas;

    public Usuario(String id, String nombre, String contraseña, String correo, String nombreUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.correo = correo;
        this.nombreUsuario = nombreUsuario;
        this.fechaRegistro = LocalDate.now();
        this.cursosActivos = new ArrayList<>();

        this.estadisticas = new Estadistica();
    }

    public Usuario(String nombre, String contraseña, String correo, String nombreUsuario) {
    	this.id = "";
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.correo = correo;
        this.nombreUsuario = nombreUsuario;
        this.fechaRegistro = LocalDate.now();
        this.cursosActivos = new ArrayList<>();
        this.estadisticas = new Estadistica();
    }
    
    public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public List<CursoEnMarcha> getCursosActivos() {
		return cursosActivos;
	}

	public void setCursosActivos(List<CursoEnMarcha> cursosActivos) {
		this.cursosActivos = cursosActivos;
	}

	public Estadistica getEstadisticas() {
		return estadisticas;
	}

	public void setEstadisticas(Estadistica estadisticas) {
		this.estadisticas = estadisticas;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public CursoEnMarcha agregarCurso(Curso curso, int vidas,Estrategia estrategia) {
		CursoEnMarcha cursoEnMarcha;
        cursosActivos.add(cursoEnMarcha = new CursoEnMarcha(curso, vidas, estrategia ));
        return cursoEnMarcha;
    }

    public void finalizarCurso(CursoEnMarcha cursoEnMarcha) {
        cursoEnMarcha.finalizar();
    }

    public List<CursoEnMarcha> obtenerCursosActivos() {
        return cursosActivos.stream()
                .collect(Collectors.toList());
    }
    
    public Optional<CursoEnMarcha> obtenerCursoEnMarcha(String cursoId) {
        return cursosActivos.stream().filter(c -> c.getId().equals(cursoId)).findFirst();
    }
    
    public void iniciarCurso(String cursoId) {
        obtenerCursoEnMarcha(cursoId).ifPresent(CursoEnMarcha::reiniciarCurso);
    }
    
    public void avanzarEnCurso(String cursoId) {
        obtenerCursoEnMarcha(cursoId).ifPresent(CursoEnMarcha::avanzarPregunta);
    }
    
    public Estadistica obtenerEstadisticas() {
        return estadisticas;
    }
    
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContraseña() {
        return contraseña;
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