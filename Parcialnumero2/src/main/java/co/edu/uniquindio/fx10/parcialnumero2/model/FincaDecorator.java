package co.edu.uniquindio.fx10.parcialnumero2.model;

public class FincaDecorator extends InmuebleDecorator {
    public FincaDecorator(InmuebleIT inmueble) {
        super(inmueble);
    }

    @Override
    public String descripcion() {
        return "Finca" + inmueble.descripcion();
    }
}
