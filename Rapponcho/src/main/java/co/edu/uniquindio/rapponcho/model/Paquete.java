package co.edu.uniquindio.rapponcho.model;

public class Paquete {
    private String idpaquete;
    private double ancho;
    private double alto;
    private double largo;
    private double pesokg;
    private double volumenM3;
    private String dimensionesFormateadas;

    public Paquete(String idpaquete, double ancho, double alto, double largo, double pesokg) {
        this.idpaquete = idpaquete;
        this.ancho = ancho;
        this.alto = alto;
        this.largo = largo;
        this.pesokg = pesokg;
        this.volumenM3 = calcularVolumen();
        this.dimensionesFormateadas = formatearDimensiones();
    }

    private double calcularVolumen() {
        // Convertir de cm³ a m³
        return (ancho * alto * largo) / 1_000_000.0;
    }

    private String formatearDimensiones() {
        return String.format("%.1f x %.1f x %.1f cm", ancho, alto, largo);
    }

    public String getIdpaquete() {
        return idpaquete;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }

    public double getLargo() {
        return largo;
    }

    public double getPesokg() {
        return pesokg;
    }

    public double getVolumenM3() {
        return volumenM3;
    }

    public String getDimensionesFormateadas() {
        return dimensionesFormateadas;
    }
}
