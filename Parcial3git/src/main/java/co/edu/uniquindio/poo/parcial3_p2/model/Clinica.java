package co.edu.uniquindio.poo.parcial3_p2.model;

import java.util.ArrayList;
import java.util.List;

public class Clinica {

    private static Clinica clinica;
    private List<Medico> listMedicos;
    private List<Paciente> listPacientes;
    private List<Cita> listCitas;

    private Clinica(){
        this.listCitas = new ArrayList<>();
        this.listMedicos = new ArrayList<>();
        this.listPacientes = new ArrayList<>();

        Medico medico1 = new Medico.Builder()
                .nombre("Dr. Juan Cardona")
                .id("M001")
                .especializacion(Especializacion.CARDIOLOGIA)
                .build();

        listMedicos.add(medico1);

        Paciente paciente1 = new PacienteFactory().crearPaciente("P001", "Samuel");
        Paciente paciente2 = new PacienteFactory().crearPaciente("P002", "Andrea");
        Paciente paciente3 = new PacienteFactory().crearPaciente("P003", "Camilo");
        Paciente paciente4 = new PacienteFactory().crearPaciente("P004", "Valentina");

        listPacientes.add(paciente1);
        listPacientes.add(paciente2);
        listPacientes.add(paciente3);
        listPacientes.add(paciente4);
    }

    public static Clinica getInstance(){
        if(clinica == null){
            clinica = new Clinica();
        }
        return clinica;
    }

    public List<Medico> getListMedicos() {
        return listMedicos;
    }

    public List<Paciente> getListPacientes() {
        return listPacientes;
    }

    public List<Cita> getListCitas() {
        return listCitas;
    }

    public Medico asignarMedicoPorEspecializacion(Especializacion especializacion) {
        for (Medico medico : listMedicos) {
            if (medico.getEspecializacion() == especializacion) {
                return medico; // Asigna el primero disponible
            }
        }
        return null;
    }
}
