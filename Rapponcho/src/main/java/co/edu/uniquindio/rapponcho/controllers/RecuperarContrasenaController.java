package co.edu.uniquindio.rapponcho.controllers;


import co.edu.uniquindio.rapponcho.model.Usuario;
import co.edu.uniquindio.rapponcho.reposytorie.Repositorio;
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
import co.edu.uniquindio.rapponcho.utils.AlertHelper;

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
    private final Repositorio repositorio;

    public RecuperarContrasenaController() {
        this.repositorio = Repositorio.getInstancia();
    }

    @FXML
    private void generarContrasena(ActionEvent event) {
        vboxResultado.setVisible(false);
        vboxResultado.setManaged(false);

        String correo = txtCorreo.getText().trim();

        // Validar correo
        if (correo.isEmpty()) {
            AlertHelper.mostrarAlerta(Alert.AlertType.ERROR,
                    "Error",
                    "Por favor ingresa un correo electrónico",
                    "");
            return;
        }

        if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            AlertHelper.mostrarAlerta(Alert.AlertType.ERROR,
                    "Error",
                    "Por favor ingresa un correo válido",
                    "");
            return;
        }

        // SIMULACIÓN: Verificar si el correo existe
        Usuario usuario = buscarUsuarioPorCorreo(correo);
        if (usuario == null) {
            AlertHelper.mostrarAlerta(Alert.AlertType.ERROR,
                    "Error",
                    "No existe ninguna cuenta registrada con ese correo",
                    "");
            return;
        }

        // Generar contraseña temporal
        contrasenaTemporal = generarContrasenaAleatoria();

        // SIMULACIÓN: Actualizar contraseña en el sistema
        actualizarContrasenaEnSistema(usuario, contrasenaTemporal);

        // Mostrar resultado
        lblContrasenaTemp.setText(contrasenaTemporal);
        vboxResultado.setVisible(true);
        vboxResultado.setManaged(true);

        btnGenerar.setText("Generar Otra Contraseña");

        AlertHelper.mostrarAlerta(Alert.AlertType.INFORMATION,
                "Éxito",
                "Se ha generado una nueva contraseña temporal para tu cuenta",
                "");
    }

    @FXML
    private void copiarContrasena(ActionEvent event) {
        if (contrasenaTemporal != null) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(contrasenaTemporal);
            clipboard.setContent(content);

            AlertHelper.mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Contraseña copiada al portapapeles",
                    "");
        }
    }

    @FXML
    private void volverLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/fx10/proyectofinals2/Login.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("RapponCho - Iniciar Sesión");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertHelper.mostrarAlerta(Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo cargar la ventana de login",
                    "");
        }
    }

    // ========== MÉTODOS DE SIMULACIÓN ==========

    /**
     * Busca un usuario por correo en el repositorio
     */
    private Usuario buscarUsuarioPorCorreo(String correo) {
        return repositorio.getUsuarios().values().stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
                .findFirst()
                .orElse(null);
    }

    /**
     * Actualiza la contraseña del usuario en el sistema
     */
    private void actualizarContrasenaEnSistema(Usuario usuario, String nuevaContrasena) {
        // Actualizar la contraseña del usuario
        usuario.setContrasena(nuevaContrasena);

        System.out.println("═══════════════════════════════════════");
        System.out.println("📧 RECUPERACIÓN DE CONTRASEÑA");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Usuario: " + usuario.getNombre());
        System.out.println("Correo: " + usuario.getCorreo());
        System.out.println("Nueva contraseña: " + nuevaContrasena);
        System.out.println("═══════════════════════════════════════");
    }

    /**
     * Genera una contraseña aleatoria de 8 caracteres
     */
    private String generarContrasenaAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }

        return sb.toString();
    }
}