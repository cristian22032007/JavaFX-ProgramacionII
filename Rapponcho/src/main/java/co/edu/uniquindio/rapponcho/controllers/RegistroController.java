package co.edu.uniquindio.rapponcho.controllers;

import co.edu.uniquindio.rapponcho.model.Usuario;
import co.edu.uniquindio.rapponcho.services.UsuarioService;
import co.edu.uniquindio.rapponcho.utils.AlertHelper;
import co.edu.uniquindio.rapponcho.utils.Validador;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegistroController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private PasswordField txtConfirmarContrasena;
    @FXML private CheckBox chkTerminos;
    @FXML private Button btnRegistrarse;
    @FXML private Hyperlink linkLogin;
    @FXML private ProgressIndicator progressIndicator;

    private final UsuarioService usuarioService;

    public RegistroController() {
        this.usuarioService = new UsuarioService();
    }

    @FXML
    public void initialize() {
        configurarAnimaciones();
        configurarValidacionesEnTiempoReal();
        progressIndicator.setVisible(false);
    }

    /**
     * Maneja el proceso de registro
     */
    @FXML
    private void handleRegistrarse(ActionEvent event) {
        // Validar términos y condiciones
        if (!chkTerminos.isSelected()) {
            AlertHelper.mostrarAdvertencia("Términos y Condiciones",
                    "Debes aceptar los términos y condiciones para continuar.");
            return;
        }

        // Obtener valores
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText();
        String confirmacion = txtConfirmarContrasena.getText();

        // Validar formulario completo
        if (!Validador.validarFormularioRegistro(
                nombre, telefono, email, usuario, contrasena, confirmacion)) {
            return;
        }

        // Deshabilitar botón y mostrar progreso
        btnRegistrarse.setDisable(true);
        progressIndicator.setVisible(true);

        // Simular proceso de registro en hilo separado
        new Thread(() -> {
            try {
                Thread.sleep(800); // Simular delay de red

                javafx.application.Platform.runLater(() -> {
                    try {
                        Usuario nuevoUsuario = usuarioService.registrarUsuario(
                                nombre, telefono, email, usuario, contrasena
                        );

                        AlertHelper.mostrarExito("Registro Exitoso",
                                "¡Bienvenido, " + nuevoUsuario.getNombre() + "!\n\n" +
                                        "Tu cuenta ha sido creada exitosamente.\n" +
                                        "Ahora puedes iniciar sesión.");

                        // Delay para que el usuario vea el mensaje
                        new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                                javafx.application.Platform.runLater(this::volverAlLogin);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();

                    } catch (Exception e) {
                        AlertHelper.mostrarExcepcion("Error en el Registro", e);
                        btnRegistrarse.setDisable(false);
                        progressIndicator.setVisible(false);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Maneja el botón de volver al login
     */
    @FXML
    private void handleVolverLogin(ActionEvent event) {
        volverAlLogin();
    }

    /**
     * Navega de vuelta a la pantalla de login
     */
    private void volverAlLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/fx10/proyectofinals2/Login.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) btnRegistrarse.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("RapponCho - Iniciar Sesión");

            aplicarAnimacionEntrada(root);

        } catch (Exception e) {
            AlertHelper.mostrarError("Error de Navegación",
                    "No se pudo regresar a la pantalla de inicio de sesión.");
        }
    }

    /**
     * Configura animaciones de hover para botones
     */
    private void configurarAnimaciones() {
        btnRegistrarse.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btnRegistrarse);
            st.setToX(1.03);
            st.setToY(1.03);
            st.play();
        });

        btnRegistrarse.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btnRegistrarse);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    /**
     * Configura validaciones en tiempo real para mejorar UX
     */
    private void configurarValidacionesEnTiempoReal() {
        // Validación de email en tiempo real
        txtEmail.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && !txtEmail.getText().isEmpty()) {
                if (!Validador.validarEmail(txtEmail.getText())) {
                    txtEmail.setStyle("-fx-border-color: #dc2626; -fx-border-width: 2;");
                } else {
                    txtEmail.setStyle("-fx-border-color: #10b981; -fx-border-width: 2;");
                }
            }
        });

        // Validación de teléfono en tiempo real
        txtTelefono.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && !txtTelefono.getText().isEmpty()) {
                if (!Validador.validarTelefono(txtTelefono.getText())) {
                    txtTelefono.setStyle("-fx-border-color: #dc2626; -fx-border-width: 2;");
                } else {
                    txtTelefono.setStyle("-fx-border-color: #10b981; -fx-border-width: 2;");
                }
            }
        });

        // Validación de contraseñas coinciden
        txtConfirmarContrasena.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                if (!newVal.equals(txtContrasena.getText())) {
                    txtConfirmarContrasena.setStyle("-fx-border-color: #dc2626; -fx-border-width: 2;");
                } else {
                    txtConfirmarContrasena.setStyle("-fx-border-color: #10b981; -fx-border-width: 2;");
                }
            }
        });

        // Limpiar estilos al escribir
        txtEmail.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!txtEmail.isFocused()) return;
            txtEmail.setStyle("");
        });

        txtTelefono.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!txtTelefono.isFocused()) return;
            txtTelefono.setStyle("");
        });

        txtConfirmarContrasena.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!txtConfirmarContrasena.isFocused()) return;
            txtConfirmarContrasena.setStyle("");
        });

        // Enter para submit
        txtConfirmarContrasena.setOnAction(e -> {
            if (chkTerminos.isSelected()) {
                handleRegistrarse(null);
            }
        });
    }

    /**
     * Aplica animación de entrada
     */
    private void aplicarAnimacionEntrada(Parent root) {
        FadeTransition ft = new FadeTransition(Duration.millis(400), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    /**
     * Muestra los términos y condiciones
     */
    @FXML
    private void handleVerTerminos(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Términos y Condiciones");
        alert.setHeaderText("Términos y Condiciones de Uso - RapponCho");
        alert.setContentText(
                "1. Aceptación de términos\n" +
                        "Al registrarte en RapponCho, aceptas estos términos y condiciones.\n\n" +
                        "2. Uso del servicio\n" +
                        "Te comprometes a usar el servicio de manera responsable y legal.\n\n" +
                        "3. Privacidad\n" +
                        "Tus datos serán protegidos según nuestra política de privacidad.\n\n" +
                        "4. Responsabilidad\n" +
                        "Eres responsable de mantener segura tu cuenta y contraseña.\n\n" +
                        "5. Modificaciones\n" +
                        "Nos reservamos el derecho de modificar estos términos.\n\n" +
                        "Para más información, visita nuestro sitio web."
        );
        alert.getDialogPane().setPrefWidth(500);
        alert.showAndWait();
    }
}