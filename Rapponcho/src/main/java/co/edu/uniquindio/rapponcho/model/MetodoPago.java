package co.edu.uniquindio.rapponcho.model;

import java.time.LocalDate;

public class MetodoPago {
    private TipoMetodo tipo;
    private String alias;
    private String numeroSimulado;
    private LocalDate fechaRegistro;

    public MetodoPago(String alias, TipoMetodo tipo, String numeroSimulado) {
        this.alias = alias;
        this.tipo = tipo;
        this.numeroSimulado = numeroSimulado;
        this.fechaRegistro = LocalDate.now();
        }

    public TipoMetodo getTipo() {
        return tipo;
    }

    public String getAlias() {
        return alias;
    }

    public String getNumeroSimulado() {
        return numeroSimulado;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
}
