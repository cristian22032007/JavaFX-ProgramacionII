package co.edu.uniquindio.fx10.parcialnumero2.model;

public class ApartDecorator extends InmuebleDecorator {
    public ApartDecorator(InmuebleIT inmueble) {
        super(inmueble);
    }

    @Override
    public String descripcion() {
        return "Apartamento" + inmueble.descripcion();
    }
}
