package co.edu.uniquindio.fx10.proyectofinals2.model;

public class TarifaDecorator implements ITarifa {
    protected ITarifa tarifa;
    public TarifaDecorator(ITarifa tarifa) {
        this.tarifa = tarifa;
    }


    @Override
    public String getDescripcion() {
        return tarifa.getDescripcion();}

    @Override
    public double CalcularCosto(double distanciaKm, double pesoKg, double volumenM3) {
        return tarifa.CalcularCosto(distanciaKm, pesoKg, volumenM3);
    }
}
