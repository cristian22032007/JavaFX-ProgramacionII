package co.edu.uniquindio.fx10.parcialnumero2.model;

public class ApartDecorator extends InmuebleDecorator {
    public ApartDecorator(Builder builder) {
        super(builder);
        builder.tipo("Apartamento");
    }
}
