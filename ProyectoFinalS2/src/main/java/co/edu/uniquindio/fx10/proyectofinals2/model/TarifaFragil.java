package co.edu.uniquindio.fx10.proyectofinals2.model;

public class TarifaFragil  extends TarifaDecorator {
    private static final double COSTO = 1400;

    public TarifaFragil(ITarifa tarifa) {
        super(tarifa);
    }

    @Override
    public String getDescripcion() {
        return tarifa.getDescripcion() + "+ Fragil";
    }

    @Override
    public double CalcularCosto(double distanciaKm, double pesoKg, double volumenM3) {
        return tarifa.CalcularCosto(distanciaKm, pesoKg, volumenM3) + COSTO;
    }
}

