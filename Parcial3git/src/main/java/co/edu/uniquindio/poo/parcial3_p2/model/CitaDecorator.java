package co.edu.uniquindio.poo.parcial3_p2.model;

public abstract class CitaDecorator implements ICita {

    protected ICita cita;

    public CitaDecorator(ICita cita) {
        this.cita = cita;
    }

    @Override
    public double calcularValor() {
        return cita.calcularValor();
    }
}

