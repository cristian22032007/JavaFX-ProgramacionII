package co.edu.uniquindio.poo.parcial3_p2;

import co.edu.uniquindio.poo.parcial3_p2.model.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class PacienteController {

    @FXML
    private Label lblBienvenida;

    @FXML
    private TableView<Cita> tablePacientes;

    @FXML
    private TableColumn<Cita, String> FechaId;

    @FXML
    private TableColumn<Cita, Integer> HoraId;

    @FXML
    private TableColumn<Cita, Double> PrecioId;

    @FXML
    private TableColumn<Cita, String> EspecializacionId;

    private Clinica clinica = Clinica.getInstance();
    private ObservableList<Cita> listaCitas;

    @FXML
    public void initialize() {
        configurarTabla();
        cargarCitas();
    }


    @FXML
    private void ActualizarCitas() {
        actualizarTablaPacientes();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizado", "Lista de repartidores actualizada", "ℹ Información");
    }

    private void actualizarTablaPacientes() {
        try {
            tablePacientes.setItems(FXCollections.observableArrayList(clinica.getListCitas()));
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los repartidores: " + e.getMessage(), "Error");
        }
    }


    @FXML
    private void CerrarSesion() {
        boolean confirmar = mostrarConfirmacion(
                "Cerrar Sesión",
                "¿Estás seguro de que deseas cerrar sesión?"
        );

        if (confirmar) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/co/edu/uniquindio/parcial3/Dashboard.fxml")
                );
                Parent root = loader.load();

                Stage stage = (Stage) lblBienvenida.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Eps Sanitas Lobby");
                stage.centerOnScreen();


            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cerrar sesión: " + e.getMessage(), "Error");
            }
        }
    }


    private static Alert mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje, String header) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(mensaje);
        alert.initModality(Modality.APPLICATION_MODAL);

        // Aplicar estilos personalizados
        alert.getDialogPane().setStyle(
                "-fx-background-color: white;" +
                        "-fx-font-family: 'System';" +
                        "-fx-font-size: 14px;"
        );

        alert.showAndWait();
        return alert;
    }

    private static boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = mostrarAlerta(Alert.AlertType.CONFIRMATION, "Confirmación", titulo, mensaje);

        // Personalizar botones
        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alert.getButtonTypes().setAll(btnSi, btnNo);

        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == btnSi;
    }

    private void configurarTabla() {
        FechaId.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getFecha() != null
                                ? cellData.getValue().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                : "N/A"
                )
        );

        HoraId.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getHora()).asObject()
        );

        PrecioId.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().calcularValor()).asObject()
        );

        EspecializacionId.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getMedico() != null
                                ? cellData.getValue().getMedico().getEspecializacion().toString()
                                : "N/A"
                )
        );
    }

    @FXML
    void AgregrCita(ActionEvent event) {
        Dialog<Cita> dialog = new Dialog<>();
        dialog.setTitle("Nueva Cita");
        dialog.setHeaderText("Agendar una nueva cita médica");

        ButtonType agregarButtonType = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(agregarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        DatePicker fechaPicker = new DatePicker(LocalDate.now());
        TextField horaField = new TextField();
        horaField.setPromptText("Hora (0-23)");

        ComboBox<Especializacion> especializacionCombo = new ComboBox<>();
        especializacionCombo.getItems().addAll(Especializacion.values());
        especializacionCombo.setPromptText("Seleccione especialización");

        TextField pacienteIdField = new TextField();
        pacienteIdField.setPromptText("ID Paciente");

        TextField pacienteNombreField = new TextField();
        pacienteNombreField.setPromptText("Nombre Paciente");

        grid.add(new Label("Fecha:"), 0, 0);
        grid.add(fechaPicker, 1, 0);
        grid.add(new Label("Hora:"), 0, 1);
        grid.add(horaField, 1, 1);
        grid.add(new Label("Especialización:"), 0, 2);
        grid.add(especializacionCombo, 1, 2);
        grid.add(new Label("ID Paciente:"), 0, 3);
        grid.add(pacienteIdField, 1, 3);
        grid.add(new Label("Nombre Paciente:"), 0, 4);
        grid.add(pacienteNombreField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == agregarButtonType) {
                try {
                    int hora = Integer.parseInt(horaField.getText());
                    LocalDate fecha = fechaPicker.getValue();
                    Especializacion especializacion = especializacionCombo.getValue();

                    if (hora < 0 || hora > 23) {
                        mostrarAlerta("Error", "La hora debe estar entre 0 y 23");
                        return null;
                    }

                    if (especializacion == null) {
                        mostrarAlerta("Error", "Debe seleccionar una especialización");
                        return null;
                    }

                    // Buscar o crear paciente
                    Paciente paciente = buscarOCrearPaciente(
                            pacienteIdField.getText(),
                            pacienteNombreField.getText()
                    );

                    // Asignar médico según especialización
                    Medico medico = clinica.asignarMedicoPorEspecializacion(especializacion);

                    if (medico == null) {
                        mostrarAlerta("Error", "No hay médicos disponibles para " + especializacion);
                        return null;
                    }

                    // Crear cita base
                    ICita cita = new Cita(hora, fecha, medico, paciente);

                    // Aplicar decorador según especialización
                    cita = aplicarDecoradorEspecializacion(cita, especializacion);

                    // Agregar a la lista de citas
                    clinica.getListCitas().add((Cita) cita);

                    return (Cita) cita;

                } catch (NumberFormatException e) {
                    mostrarAlerta("Error", "La hora debe ser un número válido");
                    return null;
                }
            }
            return null;
        });

        Optional<Cita> result = dialog.showAndWait();

        result.ifPresent(cita -> {
            cargarCitas();
            mostrarAlerta("Éxito", "Cita agendada correctamente. Valor: $" + cita.calcularValor());
        });
    }

    private Paciente buscarOCrearPaciente(String id, String nombre) {
        // Buscar paciente existente
        for (Paciente p : clinica.getListPacientes()) {
            if (p.getId().equals(id)) {
                return p;
            }
        }

        // Crear nuevo paciente si no existe
        Paciente nuevoPaciente = PacienteFactory.crearPaciente(id, nombre);
        clinica.getListPacientes().add(nuevoPaciente);
        return nuevoPaciente;
    }

    private ICita aplicarDecoradorEspecializacion(ICita cita, Especializacion especializacion) {
        switch (especializacion) {
            case CARDIOLOGIA:
                return new CitaCardiologiaDecorator(cita);
            case PEDIATRIA:
                return new CitaPediatriaDecorator(cita);
            case ODONTOLOGIA:
                return new CitaOdontologiaDecorator(cita);
            case GENERAL:
                return new CitaGeneralDecorator(cita);
            default:
                return cita;
        }
    }
}
