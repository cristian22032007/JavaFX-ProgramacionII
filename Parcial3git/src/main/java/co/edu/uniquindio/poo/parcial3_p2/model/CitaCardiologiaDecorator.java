package co.edu.uniquindio.poo.parcial3_p2.model;

public class CitaCardiologiaDecorator extends CitaDecorator {

    public CitaCardiologiaDecorator(ICita cita) {
        super(cita);
    }

    @Override
    public double calcularValor() {
        return super.calcularValor() + 40000; // recargo extra
    }
}

