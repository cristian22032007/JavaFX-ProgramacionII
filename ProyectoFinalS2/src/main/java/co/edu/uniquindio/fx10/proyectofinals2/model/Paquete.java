package co.edu.uniquindio.fx10.proyectofinals2.model;

public class Paquete {
    private String idpaquete;
    private double ancho;
    private double alto;
    private double largo;
    private double pesokg;

    public Paquete(String idpaquete, double ancho, double alto, double largo, double pesokg) {
        this.idpaquete = idpaquete;
        this.ancho = ancho;
        this.alto = alto;
        this.largo = largo;
        this.pesokg = pesokg;
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
}
