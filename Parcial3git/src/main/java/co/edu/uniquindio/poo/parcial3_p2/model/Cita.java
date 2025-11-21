package co.edu.uniquindio.poo.parcial3_p2.model;

import java.time.LocalDate;

public class Cita implements ICita {

    private int hora;
    private LocalDate fecha;
    private Medico medico;
    private Paciente paciente;

    public Cita(int hora, LocalDate fecha, Medico medico, Paciente paciente) {
        this.hora = hora;
        this.fecha = fecha;
        this.medico = medico;
        this.paciente = paciente;
    }

    @Override
    public double calcularValor() {
        return 50000; // Valor base
    }

    public int getHora() {
        return hora;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Medico getMedico() {
        return medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
