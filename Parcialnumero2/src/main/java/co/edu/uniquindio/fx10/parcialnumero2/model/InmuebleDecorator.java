package co.edu.uniquindio.fx10.parcialnumero2.model;


/**
 * Implemetamos Decorator, para crear una descripcion personalizada la cual depende del tipo
 */
public abstract class InmuebleDecorator extends Inmueble {
    public InmuebleDecorator(Builder builder) {
        super(builder);
    }

}
