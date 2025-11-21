package co.edu.uniquindio.poo.parcial3_p2.model;

public class CitaPediatriaDecorator extends CitaDecorator {

    public CitaPediatriaDecorator(ICita cita) {
        super(cita);
    }

    @Override
    public double calcularValor() {
        return super.calcularValor() + 30000;
    }
}

