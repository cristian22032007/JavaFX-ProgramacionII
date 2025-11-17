package co.edu.uniquindio.rapponcho.model;

public class TarifaFirmaRequerida  extends TarifaDecorator {
    private static final double COSTO = 800;

    public TarifaFirmaRequerida(ITarifa tarifa) {
        super(tarifa);
    }

    @Override
    public String getDescripcion() {
        return tarifa.getDescripcion() + "+ Firma Requerida";
    }

    @Override
    public double CalcularCosto(double distanciaKm, double pesoKg, double volumenM3) {
        return tarifa.CalcularCosto(distanciaKm, pesoKg, volumenM3) + COSTO;
    }
}
