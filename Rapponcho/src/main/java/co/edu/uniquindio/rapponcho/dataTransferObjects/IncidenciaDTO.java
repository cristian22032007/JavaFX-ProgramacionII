package co.edu.uniquindio.rapponcho.dataTransferObjects;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class IncidenciaDTO {
    private String idIncidencia;
    private String descripcion;
    private String fecha;
    private String estado;
    private String idEnvio;
    private String fechaCierre;

    public IncidenciaDTO(String idIncidencia, String descripcion,
                         LocalDateTime fecha, String estado, String idEnvio,
                         LocalDateTime fechaCierre) {
        this.idIncidencia = idIncidencia;
        this.descripcion = descripcion;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.fecha = fecha.format(formatter);
        this.estado = estado;
        this.idEnvio = idEnvio;
        this.fechaCierre = fechaCierre != null ? fechaCierre.format(formatter) : null;

    }

    // Getters
    public String getIdIncidencia() {
        return idIncidencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }

    public String getIdEnvio() {
        return idEnvio;
    }

    public String getFechaCierre() {
        return fechaCierre;
    }

    public boolean estaCerrada() {
        return "CERRADA".equals(estado);
    }

    public String getResumen() {
        return String.format("[#%d] %s - Estado: %s (%s)",
                idIncidencia,
                descripcion.length() > 40 ? descripcion.substring(0, 37) + "..." : descripcion,
                estado,
                fecha);
    }

}


