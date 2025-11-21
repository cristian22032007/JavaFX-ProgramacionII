package co.edu.uniquindio.poo.parcial3_p2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Button BtnMedico;

    @FXML
    private Button BtnPaciente;

    @FXML
    void onMedico(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Medico.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Panel Médico");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar Medico.fxml: " + e.getMessage());
        }
    }

    @FXML
    void onPaciente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Paciente.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Panel Paciente");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar Paciente.fxml: " + e.getMessage());
        }
    }
}