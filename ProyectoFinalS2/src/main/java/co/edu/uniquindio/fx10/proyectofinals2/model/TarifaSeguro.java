package co.edu.uniquindio.fx10.proyectofinals2.model;

public class TarifaSeguro extends TarifaDecorator {
    private final double tarifaSeguro = 2000;
    public TarifaSeguro(Builder builder) {
        super(builder);
        double v = super.getTarifaBase() + tarifaSeguro;
    }
    @Override
    public String getDescripcion() {
        return "Seguro";
    }
}
