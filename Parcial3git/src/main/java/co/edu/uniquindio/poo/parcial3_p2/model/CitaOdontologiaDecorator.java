package co.edu.uniquindio.poo.parcial3_p2.model;

public class CitaOdontologiaDecorator extends CitaDecorator {

    public CitaOdontologiaDecorator(ICita cita) {
        super(cita);
    }

    @Override
    public double calcularValor() {
        return super.calcularValor() + 25000;
    }
}

