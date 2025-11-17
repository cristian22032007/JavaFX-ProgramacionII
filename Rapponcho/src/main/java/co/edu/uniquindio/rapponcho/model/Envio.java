package co.edu.uniquindio.rapponcho.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Envio {
    private String idEnvio;
    private Usuario usuario;
    private Repartidor repartidor;
    private Direccion origen;
    private Direccion destino;
    private ITarifa tarifa;
    private EstadoEnvio estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEstimadaEntrega;
    private Pago pago;
    private List<Paquete> paquetes;
    private List<ServicioAdicional> serviciosAdicionados;
    private List<Incidencia> incidencias;
    private  double costoTotal;

    public Envio(Builder builder) {
        this.idEnvio = builder.idEnvio;
        this.usuario = builder.usuario;
        this.repartidor = builder.repartidor;
        this.origen = builder.origen;
        this.destino = builder.destino;
        this.tarifa = builder.tarifa;
        this.estado = builder.estado;
        this.fechaCreacion = builder.fechaCreacion;
        this.fechaEstimadaEntrega = builder.fechaEstimadaEntrega;
        this.pago = builder.pago;
        this.paquetes = new ArrayList<>();
        this.serviciosAdicionados = new ArrayList<>();
        this.incidencias = new ArrayList<>();
        double distancia = calcularDistancia();
        double pesokg = calcularPesoTotal();
        double volumenM3 = calcularVolumenTotal();
        this.costoTotal = tarifa.CalcularCosto(distancia, pesokg, volumenM3);
    }

    public static class Builder {
        private String idEnvio;
        private Usuario usuario;
        private Repartidor repartidor;
        private Direccion origen;
        private Direccion destino;
        private ITarifa tarifa;
        private EstadoEnvio estado;
        private LocalDateTime fechaCreacion;
        private LocalDateTime fechaEstimadaEntrega;
        private Pago pago;

        public Builder IdEnvio(String idEnvio) {
            this.idEnvio = idEnvio;
            return this;
        }

        public Builder Usuario(Usuario usuario) {
            this.usuario = usuario;
            return this;
        }

        public Builder Repartidor(Repartidor repartidor) {
            this.repartidor = repartidor;
            return this;
        }

        public Builder Origen(Direccion origen) {
            this.origen = origen;
            return this;
        }

        public Builder Destino(Direccion destino) {
            this.destino = destino;
            return this;
        }

        public Builder Tarifa(ITarifa tarifa) {
            this.tarifa = tarifa;
            return this;
        }

        public Builder EstadoEnvio(EstadoEnvio estado) {
            this.estado = estado;
            return this;
        }

        public Builder FechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public Builder FechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) {
            this.fechaEstimadaEntrega = fechaEstimadaEntrega;
            return this;
        }

        public Builder Pago(Pago pago) {
            this.pago = pago;
            return this;
        }

        public Envio build() {
            return new Envio(this);
        }


    }

    public void setIdEnvio(String idEnvio) {
        this.idEnvio = idEnvio;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setOrigen(Direccion origen) {
        this.origen = origen;
    }

    public void setDestino(Direccion destino) {
        this.destino = destino;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // Getters
    public String getIdEnvio() {
        return idEnvio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public Direccion getOrigen() {
        return origen;
    }

    public Direccion getDestino() {
        return destino;
    }

    public ITarifa getTarifa() {
        return tarifa;
    }

    public EstadoEnvio getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaEstimadaEntrega() {
        return fechaEstimadaEntrega;
    }

    public Pago getPago() {
        return pago;
    }

    public List<Paquete> getPaquetes() {
        return paquetes;
    }

    public List<ServicioAdicional> getServiciosAdicionados() {
        return serviciosAdicionados;
    }

    public List<Incidencia> getIncidencias() {
        return incidencias;
    }

    // Setters importantes
    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    public void setEstado(EstadoEnvio estado) {
        this.estado = estado;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public void setFechaEstimadaEntrega(LocalDateTime fecha) {
        this.fechaEstimadaEntrega = fecha;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }
    // Métodos de ayuda

    public boolean tieneIncidencias() {
        return !incidencias.isEmpty();
    }

    public boolean estaPagado() {
        return pago != null && pago.getResultado().equals("APROBADO");
    }


    public int getCantidadPaquetes() {
        return paquetes.size();
    }


    public void agregarPaquete(Paquete paquete) {
        this.paquetes.add(paquete);
        recalcularCosto(); // ← Recalcula automáticamente
    }

    public void agregarServicioAdicional(ServicioAdicional servicio) {
        this.serviciosAdicionados.add(servicio);
    }

    public void agregarIncidencia(Incidencia incidencia) {
        this.incidencias.add(incidencia);
    }
    public double calcularDistancia() {
            double dx = destino.getMunicipio().getCoordenadaX() - origen.getMunicipio().getCoordenadaX();
            double dy = destino.getMunicipio().getCoordenadaY() - origen.getMunicipio().getCoordenadaY();
            return Math.sqrt(dx * dx + dy * dy);
    }
    private double calcularPesoTotal() {
            return paquetes.stream()
                .mapToDouble(Paquete::getPesokg)
                .sum();
    }
    private double calcularVolumenTotal() {
            return paquetes.stream()
                .mapToDouble(p -> p.getAncho() * p.getAlto() * p.getLargo() / 1000000.0)
                .sum();
    }
    private void recalcularCosto() {
        double distancia = calcularDistancia();
        double peso = calcularPesoTotal();
        double volumen = calcularVolumenTotal();
        this.costoTotal = tarifa.CalcularCosto(distancia, peso, volumen);
    }
    }
