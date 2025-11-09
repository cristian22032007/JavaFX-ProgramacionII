package co.edu.uniquindio.fx10.proyectofinals2.model;

public interface ITarifa {

    public String getDescripcion();
    public double CalcularCosto(double distanciaKm, double pesoKg, double volumenM3);
}
