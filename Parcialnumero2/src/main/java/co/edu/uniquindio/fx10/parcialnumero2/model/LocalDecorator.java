package co.edu.uniquindio.fx10.parcialnumero2.model;

public class LocalDecorator extends InmuebleDecorator {
    public LocalDecorator(InmuebleIT inmueble) {
        super(inmueble);
    }

    @Override
    public String descripcion() {
        return "Local" + inmueble.descripcion();
    }
}
