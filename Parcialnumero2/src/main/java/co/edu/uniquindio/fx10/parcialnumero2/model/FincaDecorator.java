package co.edu.uniquindio.fx10.parcialnumero2.model;

public class FincaDecorator extends InmuebleDecorator {
    public FincaDecorator(Builder builder) {
        super(builder);
        builder.tipo("Finca");
    }

}

