package co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EnvioSimpleDTO {
    private String idEnvio;
    private String nombreUsuario;
    private String nombreRepartidor;
    private String origenResumen;
    private String destinoResumen;
    private double costo;
    private String estado;
    private String fechaCreacion;
    private String fechaEstimadaEntrega;
    private String estadoPago;

    public EnvioSimpleDTO(String idEnvio, String nombreUsuario, String nombreRepartidor,
                          String origenResumen, String destinoResumen, double costo,
                          String estado, LocalDateTime fechaCreacion,
                          LocalDateTime fechaEstimadaEntrega, String estadoPago) {
        this.idEnvio = idEnvio;
        this.nombreUsuario = nombreUsuario;
        this.nombreRepartidor = nombreRepartidor;
        this.origenResumen = origenResumen;
        this.destinoResumen = destinoResumen;
        this.costo = costo;
        this.estado = estado;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.fechaCreacion = fechaCreacion.format(formatter);
        this.fechaEstimadaEntrega = fechaEstimadaEntrega != null ?
                fechaEstimadaEntrega.format(formatter) : "No estimada";
        this.estadoPago = estadoPago;
    }

    // Getters
    public String getIdEnvio() { return idEnvio; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getNombreRepartidor() { return nombreRepartidor; }
    public String getOrigenResumen() { return origenResumen; }
    public String getDestinoResumen() { return destinoResumen; }
    public double getCosto() { return costo; }
    public String getEstado() { return estado; }
    public String getFechaCreacion() { return fechaCreacion; }
    public String getFechaEstimadaEntrega() { return fechaEstimadaEntrega; }
    public String getEstadoPago() { return estadoPago; }
}

