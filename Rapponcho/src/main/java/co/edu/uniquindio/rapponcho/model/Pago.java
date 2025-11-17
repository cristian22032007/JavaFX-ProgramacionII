package co.edu.uniquindio.rapponcho.model;

import java.time.LocalDateTime;

public class Pago {
    private String idPago;
    private double monto;
    private LocalDateTime fecha;
    private MetodoPago metodoPago;
    private ResultadoPago resultado;

    public Pago(String idPago, double monto, LocalDateTime fecha, MetodoPago metodoPago, ResultadoPago resultado) {
        this.idPago = idPago;
        this.monto = monto;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.resultado = resultado;
    }

    public String getIdPago() {
        return idPago;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public ResultadoPago getResultado() {
        return resultado;
    }

}