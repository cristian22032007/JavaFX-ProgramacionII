package co.edu.uniquindio.rapponcho.model;

public class TarifaPrioridad  extends TarifaDecorator {
    private static final double COSTO = 2000;

    public TarifaPrioridad(ITarifa tarifa) {
        super(tarifa);
    }

    @Override
    public String getDescripcion() {
        return tarifa.getDescripcion() + "+ Prioridad";
    }

    @Override
    public double CalcularCosto(double distanciaKm, double pesoKg, double volumenM3) {
        return tarifa.CalcularCosto(distanciaKm, pesoKg, volumenM3) + COSTO;
    }
}

