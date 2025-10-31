package co.edu.uniquindio.fx10.proyectofinals2.model;

import java.time.LocalDateTime;
import java.util.List;

public class Envio {
    private String idEnvio;
    private Direccion origen;
    private Direccion destino;
    private double costo;
    private EstadoEnvio estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEstimadaEntrega;
    private List<ServicioAdicional> serviciosAdicionados;

    public Envio(String idEnvio, Direccion origen, Direccion destino, double costo, EstadoEnvio estado,
                 LocalDateTime fechaCreacion, LocalDateTime fechaEstimadaEntrega, List<ServicioAdicional>
                         serviciosAdicionados) {
        this.idEnvio = idEnvio;
        this.origen = origen;
        this.destino = destino;
        this.costo = costo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
        this.serviciosAdicionados = serviciosAdicionados;
    }
}
