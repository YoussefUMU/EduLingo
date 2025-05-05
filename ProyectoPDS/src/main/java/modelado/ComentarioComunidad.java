package modelado;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comentarios_comunidad")
public class ComentarioComunidad {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    private String texto;
    private String etiqueta;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    // Constructor por defecto para JPA
    public ComentarioComunidad() {
    }
    
    // Constructor para crear nuevos comentarios
    public ComentarioComunidad(Usuario usuario, String texto, String etiqueta, Date fecha) {
        this.usuario = usuario;
        this.texto = texto;
        this.etiqueta = etiqueta;
        this.fecha = fecha;
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getTexto() {
        return texto;
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public String getEtiqueta() {
        return etiqueta;
    }
    
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}