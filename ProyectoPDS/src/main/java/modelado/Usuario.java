package modelado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import controlador.ControladorPDS;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Clase que representa a un usuario de la plataforma de aprendizaje.
 */

@Entity
@Table(name = "usuarios")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String nombre;
	private String correo;
	private String contraseña;
	private String nombreUsuario;
	private LocalDate fechaRegistro;  
	private LocalDate fechaNacimiento;
	@OneToMany(mappedBy="usuario", cascade={ CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
	private List<CursoEnMarcha> cursosActivos;
	@OneToOne(cascade={ CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(unique=true)
	private Estadistica estadisticas;			//ya lo meteré
	@OneToOne(cascade={ CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(unique=true)
	private Premium premium;
	

	public Usuario() {}
	
	public Usuario(String nombre, String contraseña, String correo, String nombreUsuario, LocalDate FechaNacimiento) {
		this.nombre = nombre;
		this.contraseña = contraseña;
		this.correo = correo;
		this.nombreUsuario = nombreUsuario;
		this.fechaRegistro = LocalDate.now();
		this.cursosActivos = new ArrayList<>();
		//this.estadisticas = new Estadistica();
		this.fechaNacimiento = fechaNacimiento;
	}

	public Usuario(String nombre, String correo, String nombreUsuario) {
		this.nombre = nombre;
		this.contraseña = "1234";
		this.correo = correo;
		this.nombreUsuario = nombreUsuario;
		this.fechaRegistro = LocalDate.now();
		this.cursosActivos = new ArrayList<>();
		//this.estadisticas = new Estadistica();
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
		return new Estadistica(); //estadisticas;
	}

	public void setEstadisticas(Estadistica estadisticas) {
		//this.estadisticas = estadisticas;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public boolean agregarCurso(Curso curso, int vidas, Estrategia estrategia, TipoEstrategia tipoEstrategia) {
		boolean coincidencia = cursosActivos.stream()
				.anyMatch(c -> c.getEstrategia().getClass() == estrategia.getClass() // Comparar clases
						&& ControladorPDS.getUnicaInstancia().getNombreCursoEnMarcha(c).equals(curso.getNombre())
						&& ControladorPDS.getUnicaInstancia().getDescripcionCursoEnMarcha(c).equals(curso.getDescripcion()));

		if (!coincidencia) {
			cursosActivos.add(new CursoEnMarcha(curso, vidas, estrategia, tipoEstrategia));
			return true;
		}
		return false;
	}


	public void finalizarCurso(CursoEnMarcha cursoEnMarcha) {
		this.cursosActivos.remove(cursoEnMarcha);
		cursoEnMarcha.finalizar();
	}

	public List<CursoEnMarcha> obtenerCursosActivos() {
		return cursosActivos.stream().collect(Collectors.toList());
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

	public Estadistica obtenerEstadisticas() {
		return new Estadistica();//estadisticas;
	}

	public Long getId() {
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

	public boolean esPremium() {
	    return premium != null && premium.estaActivo();
	}

	public void activarPremium(String tipoPlan) {
	    if (premium == null) {
	        premium = new Premium(tipoPlan);
	    } else {
	        // Si ya tenía premium pero estaba inactivo, renovarlo
	        if (!premium.estaActivo()) {
	            if ("anual".equalsIgnoreCase(tipoPlan)) {
	                premium.renovar(12);
	            } else {
	                premium.renovar(1);
	            }
	        }
	    }
	}

	public void cancelarPremium() {
	    if (premium != null) {
	        premium.cancelar();
	    }
	}

	public Premium getPremium() {
	    return premium;
	}

	public void setPremium(Premium premium) {
	    this.premium = premium;
	}

	// Estos métodos ayudan a determinar si el usuario tiene ciertos beneficios premium
	public boolean tieneVidasInfinitas() {
	    return esPremium() && premium.isVidasInfinitas();
	}

	public boolean tieneCursosAdicionales() {
	    return esPremium() && premium.isCursosAdicionales();
	}

	public boolean sinAnuncios() {
	    return esPremium() && premium.isSinAnuncios();
	}
}