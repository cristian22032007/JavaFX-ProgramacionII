package co.edu.uniquindio.fx10.proyectofinals2.controllers;

import co.edu.uniquindio.fx10.proyectofinals2.model.Usuario;
import co.edu.uniquindio.fx10.proyectofinals2.reposytorie.Repositorio;
import co.edu.uniquindio.fx10.proyectofinals2.utils.AlertHelper;
import co.edu.uniquindio.fx10.proyectofinals2.utils.Validador;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class RecuperarContrasenaController {

    @FXML private TextField txtEmailOUsuario;
    @FXML private TextField txtCodigoVerificacion;
    @FXML private PasswordField txtNuevaContrasena;
    @FXML private PasswordField txtConfirmarContrasena;
    @FXML private Button btnEnviarCodigo;
    @FXML private Button btnVerificarCodigo;
    @FXML private Button btnCambiarContrasena;
    @FXML private Hyperlink linkVolverLogin;
    @FXML private VBox panelVerificacion;
    @FXML private VBox panelNuevaContrasena;
    @FXML private Label lblInstrucciones;
    @FXML private ProgressIndicator progressIndicator;

    private final Repositorio repositorio;
    private String codigoGenerado;
    private Usuario usuarioEncontrado;

    public RecuperarContrasenaController() {
        this.repositorio = Repositorio.getInstancia();
    }

    @FXML
    public void initialize() {
        // Inicialmente solo mostrar el primer paso
        panelVerificacion.setVisible(false);
        panelVerificacion.setManaged(false);
        panelNuevaContrasena.setVisible(false);
        panelNuevaContrasena.setManaged(false);
        progressIndicator.setVisible(false);
    }

    /**
     * Paso 1: Buscar usuario y enviar código
     */
    @FXML
    private void handleEnviarCodigo(ActionEvent event) {
        String emailOUsuario = txtEmailOUsuario.getText().trim();

        if (!Validador.validarTextoNoVacioConAlerta(emailOUsuario, "Email o Usuario")) {
            return;
        }

        // Buscar usuario
        usuarioEncontrado = buscarUsuario(emailOUsuario);

        if (usuarioEncontrado == null) {
            AlertHelper.mostrarError("Usuario no encontrado",
                    "No existe ningún usuario con ese email o nombre de usuario.");
            return;
        }

        // Generar código de verificación (simulado)
        codigoGenerado = generarCodigoVerificacion();

        // Mostrar código en consola (en producción se enviaría por email)
        System.out.println("═══════════════════════════════════════");
        System.out.println("📧 CÓDIGO DE VERIFICACIÓN GENERADO");
        System.out.println("═══════════════════════════════════════");
        System.out.println("Usuario: " + usuarioEncontrado.getUsuario());
        System.out.println("Email: " + usuarioEncontrado.getCorreo());
        System.out.println("Código: " + codigoGenerado);
        System.out.println("═══════════════════════════════════════");

        // Simular envío
        btnEnviarCodigo.setDisable(true);
        progressIndicator.setVisible(true);

        new Thread(() -> {
            try {
                Thread.sleep(1500); // Simular envío

                javafx.application.Platform.runLater(() -> {
                    progressIndicator.setVisible(false);

                    AlertHelper.mostrarInfo("Código Enviado",
                            "Se ha enviado un código de verificación a:\n\n" +
                                    "📧 " + enmascararEmail(usuarioEncontrado.getCorreo()) + "\n\n" +
                                    "Por favor, revisa tu bandeja de entrada.\n" +
                                    "(En esta demo, el código se muestra en la consola)");

                    // Mostrar panel de verificación
                    panelVerificacion.setVisible(true);
                    panelVerificacion.setManaged(true);
                    lblInstrucciones.setText(
                            "Hemos enviado un código de 6 dígitos a tu correo electrónico."
                    );
                    txtCodigoVerificacion.requestFocus();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Paso 2: Verificar código
     */
    @FXML
    private void handleVerificarCodigo(ActionEvent event) {
        String codigoIngresado = txtCodigoVerificacion.getText().trim();

        if (codigoIngresado.isEmpty()) {
            AlertHelper.mostrarAdvertencia("Código Requerido",
                    "Por favor, ingresa el código que recibiste por email.");
            return;
        }

        if (!codigoIngresado.equals(codigoGenerado)) {
            AlertHelper.mostrarError("Código Incorrecto",
                    "El código ingresado no es válido. Por favor, verifica e intenta nuevamente.");
            txtCodigoVerificacion.clear();
            txtCodigoVerificacion.requestFocus();
            return;
        }

        // Código correcto - mostrar panel para nueva contraseña
        AlertHelper.mostrarExito("Código Verificado",
                "El código es correcto. Ahora puedes establecer una nueva contraseña.");

        panelVerificacion.setVisible(false);
        panelVerificacion.setManaged(false);
        panelNuevaContrasena.setVisible(true);
        panelNuevaContrasena.setManaged(true);
        lblInstrucciones.setText("Establece tu nueva contraseña");
        txtNuevaContrasena.requestFocus();
    }

    /**
     * Paso 3: Cambiar contraseña
     */
    @FXML
    private void handleCambiarContrasena(ActionEvent event) {
        String nuevaContrasena = txtNuevaContrasena.getText();
        String confirmacion = txtConfirmarContrasena.getText();

        // Validar contraseña
        if (!Validador.validarContrasenaConAlerta(nuevaContrasena)) {
            return;
        }

        // Validar que coincidan
        if (!Validador.validarContrasenasCoinciden(nuevaContrasena, confirmacion)) {
            return;
        }

        // Cambiar contraseña
        usuarioEncontrado.setContrasena(nuevaContrasena);

        AlertHelper.mostrarExito("Contraseña Actualizada",
                "Tu contraseña ha sido cambiada exitosamente.\n\n" +
                        "Ya puedes iniciar sesión con tu nueva contraseña.");

        // Volver al login después de un delay
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                javafx.application.Platform.runLater(this::volverAlLogin);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Reenviar código
     */
    @FXML
    private void handleReenviarCodigo(ActionEvent event) {
        if (usuarioEncontrado == null) {
            AlertHelper.mostrarAdvertencia("Error",
                    "Por favor, primero ingresa tu email o usuario.");
            return;
        }

        // Generar nuevo código
        codigoGenerado = generarCodigoVerificacion();

        System.out.println("═══════════════════════════════════════");
        System.out.println("🔄 CÓDIGO REENVIADO");
        System.out.println("Nuevo código: " + codigoGenerado);
        System.out.println("═══════════════════════════════════════");

        AlertHelper.mostrarInfo("Código Reenviado",
                "Se ha enviado un nuevo código de verificación a tu correo electrónico.");

        txtCodigoVerificacion.clear();
        txtCodigoVerificacion.requestFocus();
    }

    /**
     * Volver al login
     */
    @FXML
    private void handleVolverLogin(ActionEvent event) {
        volverAlLogin();
    }

    /**
     * Busca un usuario por email o nombre de usuario
     */
    private Usuario buscarUsuario(String emailOUsuario) {
        return repositorio.getUsuarios().values().stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(emailOUsuario) ||
                        u.getUsuario().equalsIgnoreCase(emailOUsuario))
                .findFirst()
                .orElse(null);
    }

    /**
     * Genera un código de verificación de 6 dígitos
     */
    private String generarCodigoVerificacion() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000);
        return String.valueOf(codigo);
    }

    /**
     * Enmascara el email para mostrar solo parte
     */
    private String enmascararEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }

        String[] partes = email.split("@");
        String usuario = partes[0];
        String dominio = partes[1];

        if (usuario.length() <= 2) {
            return "**@" + dominio;
        }

        String usuarioEnmascarado = usuario.charAt(0) + "***" +
                usuario.charAt(usuario.length() - 1);
        return usuarioEnmascarado + "@" + dominio;
    }

    /**
     * Navega de vuelta al login
     */
    private void volverAlLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/fx10/proyectofinals2/Login.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) linkVolverLogin.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("RapponCho - Iniciar Sesión");

            // Animación
            FadeTransition ft = new FadeTransition(Duration.millis(400), root);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();

        } catch (Exception e) {
            AlertHelper.mostrarError("Error de Navegación",
                    "No se pudo regresar a la pantalla de inicio de sesión.");
        }
    }
}