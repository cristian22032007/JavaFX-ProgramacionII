package co.edu.uniquindio.fx10.proyectofinals2.model;

import java.time.LocalDateTime;

public class Incidencia {
        private int idIncidencia;
        private String descripcion;
        private LocalDateTime fecha;
        private EstadoIncidencia estado;
        private Envio envio; // relación inversa (referencia al envío padre)

        public Incidencia(int idIncidencia, String descripcion, Envio envio) {
            this.idIncidencia = idIncidencia;
            this.descripcion = descripcion;
            this.fecha = LocalDateTime.now();
            this.estado = EstadoIncidencia.ABIERTA;
            this.envio = envio;
        }

    // Getters
    public int getIdIncidencia() { return idIncidencia; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFecha() { return fecha; }
    public EstadoIncidencia getEstado() { return estado; }
    public Envio getEnvio() { return envio; }

    // Setters
    public void setEstado(EstadoIncidencia estado) { this.estado = estado; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    }

