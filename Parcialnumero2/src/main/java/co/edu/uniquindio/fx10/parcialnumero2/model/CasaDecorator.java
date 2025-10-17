package co.edu.uniquindio.fx10.parcialnumero2.model;

public class CasaDecorator extends InmuebleDecorator {
    public CasaDecorator(InmuebleIT inmueble) {
        super(inmueble);
    }

    @Override
    public String descripcion() {
        return "Casa" + inmueble.descripcion();
    }

}
