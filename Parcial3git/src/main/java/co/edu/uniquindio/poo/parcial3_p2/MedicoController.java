package co.edu.uniquindio.poo.parcial3_p2;

import co.edu.uniquindio.poo.parcial3_p2.model.Cita;
import co.edu.uniquindio.poo.parcial3_p2.model.Clinica;
import co.edu.uniquindio.poo.parcial3_p2.model.Especializacion;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class MedicoController {

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

    private void configurarTabla() {
        // Configurar las columnas para mostrar los datos de las citas
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

    private void cargarCitas() {
        listaCitas = FXCollections.observableArrayList(clinica.getListCitas());
        tablePacientes.setItems(listaCitas);
    }

    @FXML
    void ActualizarCitas(ActionEvent event) {
        cargarCitas();
        System.out.println("Tabla de citas actualizada");
    }

    @FXML
    void CerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dashboard - EPs Sanitas");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar Dashboard.fxml: " + e.getMessage());
        }
    }
}
