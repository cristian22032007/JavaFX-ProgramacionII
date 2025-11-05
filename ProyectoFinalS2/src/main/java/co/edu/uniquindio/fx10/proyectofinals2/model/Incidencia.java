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

        public Envio getEnvio() {
            return envio;
        }
    }

