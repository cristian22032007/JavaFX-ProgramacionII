package co.edu.uniquindio.parcial3;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class PacienteController {

    @FXML
    private TableColumn<?, ?> EspecializacionId;

    @FXML
    private TableColumn<?, ?> FechaId;

    @FXML
    private TableColumn<?, ?> HoraId;

    @FXML
    private TableColumn<?, ?> PrecioId;

    @FXML
    private Label lblBienvenida;

    @FXML
    private TableView<?> tablePacientes;

    @FXML
    private void ActualizarCitas() {
        actualizarTablaPacientes();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Actualizado", "Lista de repartidores actualizada", "ℹ Información");
    }

    private void actualizarTablaPacientes() {
        try {
            tablePacientes.setItems(FXCollections.observableArrayList());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los repartidores: " + e.getMessage(), "Error");
        }
    }


    @FXML
    void AgregrCita() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nueva Cita");
        dialog.setHeaderText("Registrar nueva cita en el sistema");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));

        TextField txtFecha = new TextField();
        txtFecha.setPromptText("Fecha");

        TextField txtHora = new TextField();
        txtHora.setPromptText("Hora");

        ComboBox cbmEspecializacion = new ComboBox();
        cbmEspecializacion.setPromptText("Especializacion");


        grid.add(new Label("Fecha:"), 0, 0);
        grid.add(tx, 1, 0);
        grid.add(new Label("Hora:"), 0, 1);
        grid.add(txtDocumento, 1, 1);
        grid.add(new Label("Especializacion:"), 0, 2);
        grid.add(, 2);


        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String nombre = txtNombre.getText().trim();
                String documento = txtDocumento.getText().trim();
                String telefono = txtTelefono.getText().trim();
                String correo = txtCorreo.getText().trim();
                String usuario = txtUsuario.getText().trim();
                String contrasena = txtContrasena.getText();

                repartidorService.crearRepartidor(nombre, documento, telefono, correo, usuario, contrasena);
                AlertHelper.mostrarExito("Repartidor Creado", "El repartidor se registró exitosamente");
                actualizarTablaRepartidores();
                actualizarMetricas();

            } catch (Exception e) {
                AlertHelper.mostrarError("Error", e.getMessage());
            }
        }
    }


    @FXML
    void CerrarSesion() {
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


    public static boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = mostrarAlerta(Alert.AlertType.CONFIRMATION, "Confirmación", titulo, mensaje);

        // Personalizar botones
        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alert.getButtonTypes().setAll(btnSi, btnNo);

        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == btnSi;
    }
}
