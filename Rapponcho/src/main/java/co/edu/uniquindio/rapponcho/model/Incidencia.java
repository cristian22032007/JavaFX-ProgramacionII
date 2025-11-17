package co.edu.uniquindio.rapponcho.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Incidencia {
        private String idIncidencia;
        private String descripcion;
        private LocalDateTime fecha;
        private EstadoIncidencia estado;
        private LocalDateTime fechaCierre;
        private Envio envio; // relación inversa (referencia al envío padre)

        public Incidencia(String idIncidencia, String descripcion, LocalDateTime fechaCierre,Envio envio) {
            this.idIncidencia = idIncidencia;
            this.descripcion = descripcion;
            this.fecha = LocalDateTime.now();
            this.estado = EstadoIncidencia.ABIERTA;
            this.fechaCierre = fechaCierre;
            this.envio = envio;
        }

    // Getters
    public String getIdIncidencia() { return idIncidencia; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFecha() { return fecha; }
    public EstadoIncidencia getEstado() { return estado; }
    public Envio getEnvio() { return envio; }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    // Setters
    public void setEstado(EstadoIncidencia estado) { this.estado = estado; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }

