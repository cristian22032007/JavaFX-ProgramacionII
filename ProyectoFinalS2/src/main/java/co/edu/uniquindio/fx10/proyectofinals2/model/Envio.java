package co.edu.uniquindio.fx10.proyectofinals2.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Envio {
    private String idEnvio;
    private Usuario usuario;
    private Repartidor repartidor;
    private Direccion origen;
    private Direccion destino;
    private double costo;
    private EstadoEnvio estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEstimadaEntrega;
    private Pago pago;
    private List<ServicioAdicional> serviciosAdicionados;

    public Envio(String idEnvio, Usuario usuario, Repartidor repartidor,Direccion origen, Direccion destino, double costo, EstadoEnvio estado,
                 LocalDateTime fechaCreacion, LocalDateTime fechaEstimadaEntrega, Pago pago) {
        this.idEnvio = idEnvio;
        this.usuario = usuario;
        this.repartidor = repartidor;
        this.origen = origen;
        this.destino = destino;
        this.costo = costo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
        this.pago = pago;
        this.serviciosAdicionados = new ArrayList<>();
    }
}
