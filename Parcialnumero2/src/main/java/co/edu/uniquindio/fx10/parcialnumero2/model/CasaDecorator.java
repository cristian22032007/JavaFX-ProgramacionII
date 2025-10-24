package co.edu.uniquindio.fx10.parcialnumero2.model;

public class CasaDecorator extends InmuebleDecorator {
    public CasaDecorator(Builder builder) {
        super(builder);
        builder.tipo("Casa");
    }
}
