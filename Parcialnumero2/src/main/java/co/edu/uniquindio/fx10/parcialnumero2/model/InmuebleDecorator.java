package co.edu.uniquindio.fx10.parcialnumero2.model;

/**
 * Implemetamos Decorator, para crear una descripcion personalizada la cual depende del tipo
 */
public abstract class InmuebleDecorator implements InmuebleIT {
    protected InmuebleIT inmueble;
    public InmuebleDecorator(InmuebleIT inmueble) {
        this.inmueble = inmueble;
    }

    @Override
    public abstract String descripcion();

}
