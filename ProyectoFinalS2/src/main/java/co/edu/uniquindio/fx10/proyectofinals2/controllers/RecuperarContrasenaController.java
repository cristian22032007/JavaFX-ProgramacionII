package co.edu.uniquindio.fx10.proyectofinals2.controllers;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import co.edu.uniquindio.fx10.proyectofinals2.utils.AlertHelper;
import java.io.IOException;
import java.util.Random;

public class RecuperarContrasenaController {

    @FXML
    private TextField txtCorreo;

    @FXML
    private Label lblContrasenaTemp;

    @FXML
    private VBox vboxResultado;

    @FXML
    private Button btnGenerar;

    private String contrasenaTemporal;

    @FXML
    private void generarContrasena(ActionEvent event) {
        vboxResultado.setVisible(false);
        vboxResultado.setManaged(false);

        String correo = txtCorreo.getText().trim();

        // Validar correo
        if (correo.isEmpty()) {
            AlertHelper.mostrarAlerta(Alert.AlertType.ERROR,"Error", "Por favor ingresa un correo electrónico", "");
            return;
        }

        if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            AlertHelper.mostrarAlerta(Alert.AlertType.ERROR, "Error", "Por favor ingresa un correo válido", "");
            return;
        }

        // SIMULACIÓN: Verificar si el correo existe
        if (!existeUsuarioConCorreo(correo)) {
            AlertHelper.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No existe ninguna cuenta registrada con ese correo", "");
            return;
        }

        // Generar contraseña temporal
        contrasenaTemporal = generarContraseñaAleatoria();

        // SIMULACIÓN: Actualizar contraseña
        actualizarContrasenaEnSistema(correo, contrasenaTemporal);

        // Mostrar resultado
        lblContrasenaTemp.setText(contrasenaTemporal);
        vboxResultado.setVisible(true);
        vboxResultado.setManaged(true);

        btnGenerar.setText("Generar Otra Contraseña");
    }

    @FXML
    private void copiarContrasena(ActionEvent event) {
        if (contrasenaTemporal != null) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(contrasenaTemporal);
            clipboard.setContent(content);

            AlertHelper.mostrarAlerta(Alert.AlertType.INFORMATION,"Éxito", "Contraseña copiada al portapapeles","" );
        }
    }

    @FXML
    private void volverLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Iniciar Sesión");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.mostrarAlerta( Alert.AlertType.ERROR,"Error", "No se pudo cargar la ventana de login","");
        }
    }

    // ========== MÉTODOS DE SIMULACIÓN ==========

    private boolean existeUsuarioConCorreo(String correo) {
        // Aquí usarías tu Singleton o gestor de usuarios
        return correo.contains("@"); // Simulación simple
    }

    private void actualizarContrasenaEnSistema(String correo, String nuevaContrasena) {
        // Actualizar en tu sistema de gestión de usuarios
        System.out.println("Contraseña actualizada para: " + correo);
        System.out.println("Nueva contraseña: " + nuevaContrasena);
    }

    private String generarContraseñaAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return sb.toString();
    }
}