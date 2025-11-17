package co.edu.uniquindio.rapponcho.model;

public class TarifaSeguro extends TarifaDecorator {
    private static final double COSTO = 1500;

    public TarifaSeguro(ITarifa tarifa) {
        super(tarifa);
    }

    @Override
    public String getDescripcion() {
        return tarifa.getDescripcion() + "+ Seguro";
    }

    @Override
    public double CalcularCosto(double distanciaKm, double pesoKg, double volumenM3) {
        return tarifa.CalcularCosto(distanciaKm, pesoKg, volumenM3) + COSTO;
    }
}

