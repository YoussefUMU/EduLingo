package modelado;

import java.time.LocalDate;

/**
 * Clase que representa una suscripción premium de un usuario.
 * Incluye la fecha de inicio, la fecha de fin y los beneficios disponibles.
 */
public class Premium {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activo;
    private String tipoPlan; // "mensual", "anual", etc.
    
    // Beneficios específicos que puede tener un usuario premium
    private boolean vidasInfinitas;
    private boolean cursosAdicionales;
    private boolean sinAnuncios;
    
    /**
     * Constructor por defecto
     */
    public Premium() {
        this.fechaInicio = LocalDate.now();
        this.fechaFin = LocalDate.now().plusMonths(1); // Por defecto, suscripción mensual
        this.activo = true;
        this.tipoPlan = "mensual";
        
        // Activar todos los beneficios por defecto
        this.vidasInfinitas = true;
        this.cursosAdicionales = true;
        this.sinAnuncios = true;
    }
    
    /**
     * Constructor que permite especificar el tipo de plan
     * @param tipoPlan Tipo de plan ("mensual", "anual", etc.)
     */
    public Premium(String tipoPlan) {
        this.fechaInicio = LocalDate.now();
        this.activo = true;
        this.tipoPlan = tipoPlan;
        
        // Establecer la fecha de fin según el tipo de plan
        if ("anual".equalsIgnoreCase(tipoPlan)) {
            this.fechaFin = LocalDate.now().plusYears(1);
        } else {
            // Por defecto, mensual
            this.fechaFin = LocalDate.now().plusMonths(1);
        }
        
        // Activar todos los beneficios por defecto
        this.vidasInfinitas = true;
        this.cursosAdicionales = true;
        this.sinAnuncios = true;
    }

    /**
     * Verifica si la suscripción premium está activa (no ha expirado)
     * @return true si la suscripción sigue vigente, false en caso contrario
     */
    public boolean estaActivo() {
        if (!activo) return false;
        
        LocalDate hoy = LocalDate.now();
        return !hoy.isAfter(fechaFin);
    }
    
    /**
     * Renueva la suscripción premium
     * @param meses Número de meses a renovar
     */
    public void renovar(int meses) {
        if (LocalDate.now().isAfter(fechaFin)) {
            // Si ya expiró, comenzar desde hoy
            this.fechaInicio = LocalDate.now();
        }
        
        this.fechaFin = this.fechaFin.plusMonths(meses);
        this.activo = true;
        
        if (meses >= 12) {
            this.tipoPlan = "anual";
        } else {
            this.tipoPlan = "mensual";
        }
    }
    
    /**
     * Cancela la suscripción premium (se mantendrá activa hasta la fecha de fin)
     */
    public void cancelar() {
        this.activo = false;
    }

    // Getters y Setters
    
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getTipoPlan() {
        return tipoPlan;
    }

    public void setTipoPlan(String tipoPlan) {
        this.tipoPlan = tipoPlan;
    }

    public boolean isVidasInfinitas() {
        return vidasInfinitas;
    }

    public void setVidasInfinitas(boolean vidasInfinitas) {
        this.vidasInfinitas = vidasInfinitas;
    }

    public boolean isCursosAdicionales() {
        return cursosAdicionales;
    }

    public void setCursosAdicionales(boolean cursosAdicionales) {
        this.cursosAdicionales = cursosAdicionales;
    }

    public boolean isSinAnuncios() {
        return sinAnuncios;
    }

    public void setSinAnuncios(boolean sinAnuncios) {
        this.sinAnuncios = sinAnuncios;
    }
}