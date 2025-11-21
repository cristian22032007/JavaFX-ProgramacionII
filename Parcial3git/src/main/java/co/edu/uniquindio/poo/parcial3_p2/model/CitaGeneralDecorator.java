package co.edu.uniquindio.poo.parcial3_p2.model;

public class CitaGeneralDecorator extends CitaDecorator {

    public CitaGeneralDecorator(ICita cita) {
        super(cita);
    }

    @Override
    public double calcularValor() {
        return super.calcularValor(); // no aumenta nada
    }
}

