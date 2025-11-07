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
    private Tarifa tarifa;
    private EstadoEnvio estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEstimadaEntrega;
    private Pago pago;
    private List<Paquete> paquetes;
    private List<ServicioAdicional> serviciosAdicionados;
    private List<Incidencia> incidencias;

    public Envio(String idEnvio, Usuario usuario, Repartidor repartidor, Direccion origen, Direccion destino, Tarifa tarifa,
                 EstadoEnvio estado, LocalDateTime fechaCreacion, LocalDateTime fechaEstimadaEntrega, Pago pago) {
        this.idEnvio = idEnvio;
        this.usuario = usuario;
        this.repartidor = repartidor;
        this.origen = origen;
        this.destino = destino;
        this.tarifa = tarifa;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
        this.pago = pago;
        this.paquetes = new ArrayList<>();
        this.serviciosAdicionados = new ArrayList<>();
        this.incidencias = new ArrayList<>();
    }

    private double calcularPesoTotal() {
        return paquetes.stream()
                .mapToDouble(Paquete::getPesokg)
                .sum();
    }

    private double calcularVolumenTotal() {
        return paquetes.stream()
                .mapToDouble(p -> p.getAncho() * p.getAlto() * p.getLargo())
                .sum();
    }
}
