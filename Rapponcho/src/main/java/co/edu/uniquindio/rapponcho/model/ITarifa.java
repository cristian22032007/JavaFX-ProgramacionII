package co.edu.uniquindio.rapponcho.model;

public interface ITarifa {

    public String getDescripcion();
    public double CalcularCosto(double distanciaKm, double pesoKg, double volumenM3);
}
