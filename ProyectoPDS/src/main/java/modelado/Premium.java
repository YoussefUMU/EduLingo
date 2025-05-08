package modelado;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/**
 * Clase que representa una suscripción premium de un usuario. Incluye la fecha
 * de inicio, la fecha de fin y los beneficios disponibles.
 */
@Entity
public class Premium {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activo;
    private String tipoPlan; // "mensual", "anual", etc.

    @OneToOne(mappedBy = "premium")
    private Usuario usuario;

    // Beneficios específicos que puede tener un usuario premium
    private boolean vidasInfinitas;

    /** Constructor por defecto (mensual). */
    public Premium() {
        this.fechaInicio      = LocalDate.now();
        this.fechaFin         = LocalDate.now().plusMonths(1);
        this.activo           = true;
        this.tipoPlan         = "mensual";
        this.vidasInfinitas   = true;
    }

    /** Constructor que permite especificar el tipo de plan. */
    public Premium(String tipoPlan) {
        this.fechaInicio = LocalDate.now();
        this.activo      = true;
        this.tipoPlan    = tipoPlan;
        if ("anual".equalsIgnoreCase(tipoPlan)) {
            this.fechaFin = LocalDate.now().plusYears(1);
        } else {
            this.fechaFin = LocalDate.now().plusMonths(1);
        }
        this.vidasInfinitas = true;
    }

    /** Verifica si la suscripción premium está activa (flag y fecha). */
    public boolean estaActivo() {
        LocalDate hoy = LocalDate.now();
        return activo && !hoy.isAfter(fechaFin);
    }

    /** Renueva la suscripción premium. */
    public void renovar(int meses) {
        if (LocalDate.now().isAfter(fechaFin)) {
            this.fechaInicio = LocalDate.now();
        }
        this.fechaFin = this.fechaFin.plusMonths(meses);
        this.activo  = true;
        this.tipoPlan = meses >= 12 ? "anual" : "mensual";
    }

    /** Cancela la suscripción premium (flag a false). */
    public void cancelar() {
        this.activo = false;
    }

    // Getters y setters...
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    /** Devuelve el flag interno; no confundir con estaActivo(). */
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getTipoPlan() { return tipoPlan; }
    public void setTipoPlan(String tipoPlan) { this.tipoPlan = tipoPlan; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public boolean isVidasInfinitas() { return vidasInfinitas; }
    public void setVidasInfinitas(boolean vidasInfinitas) { this.vidasInfinitas = vidasInfinitas; }
}
