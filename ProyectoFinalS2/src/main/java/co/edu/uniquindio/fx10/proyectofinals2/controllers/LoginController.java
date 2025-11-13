package co.edu.uniquindio.fx10.proyectofinals2.controllers;


import co.edu.uniquindio.fx10.proyectofinals2.model.Administrador;
import co.edu.uniquindio.fx10.proyectofinals2.model.Usuario;
import co.edu.uniquindio.fx10.proyectofinals2.reposytorie.Repositorio;
import co.edu.uniquindio.fx10.proyectofinals2.services.AdministradorService;
import co.edu.uniquindio.fx10.proyectofinals2.services.UsuarioService;
import co.edu.uniquindio.fx10.proyectofinals2.utils.Validador;
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

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private RadioButton rbUsuario;
    @FXML private RadioButton rbAdministrador;
    @FXML private CheckBox chkRecordarme;
    @FXML private Label lblError;
    @FXML private Button btnIniciarSesion;
    @FXML private Hyperlink linkRegistro;

    private final UsuarioService usuarioService;
    private final AdministradorService administradorService;

    public LoginController() {
        this.usuarioService = new UsuarioService();
        this.administradorService = new AdministradorService();

        // Inicializar sistema con admin por defecto
        Repositorio.getInstancia().inicializarSistemaPorDefecto();
    }

    @FXML
    public void initialize() {
        configurarAnimaciones();
        configurarValidaciones();
    }

    /**
     * Maneja el inicio de sesión
     */
    @FXML
    private void handleIniciarSesion(ActionEvent event) {
        limpiarError();

        // Validar campos vacíos
        if (!validarCampos()) {
            return;
        }

        String usuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText();
        boolean esUsuario = rbUsuario.isSelected();

        // Deshabilitar botón durante el proceso
        btnIniciarSesion.setDisable(true);

        try {
            if (esUsuario) {
                iniciarSesionUsuario(usuario, contrasena);
            } else {
                iniciarSesionAdministrador(usuario, contrasena);
            }
        } catch (Exception e) {
            mostrarError(e.getMessage());
            btnIniciarSesion.setDisable(false);
        }
    }

    /**
     * Inicia sesión como usuario
     */
    private void iniciarSesionUsuario(String usuario, String contrasena) throws Exception {
        Usuario usuarioAutenticado = usuarioService.iniciarSesion(usuario, contrasena);

        mostrarExito("¡Bienvenido, " + usuarioAutenticado.getNombre() + "!");

        // Pequeño delay para que el usuario vea el mensaje
        new Thread(() -> {
            try {
                Thread.sleep(800);
                javafx.application.Platform.runLater(() ->
                        navegarAPantallaUsuario(usuarioAutenticado)
                );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Inicia sesión como administrador
     */
    private void iniciarSesionAdministrador(String usuario, String contrasena) throws Exception {
        Administrador adminAutenticado = administradorService.iniciarSesion(usuario, contrasena);

        mostrarExito("¡Bienvenido, Administrador!");

        new Thread(() -> {
            try {
                Thread.sleep(800);
                javafx.application.Platform.runLater(() ->
                        navegarAPantallaAdmin(adminAutenticado)
                );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Navega a la pantalla principal del usuario
     */
    private void navegarAPantallaUsuario(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/fx10/proyectofinals2/UsuarioMain.fxml")
            );
            Parent root = loader.load();

            // Pasar el usuario al controlador de la siguiente pantalla
            // UsuarioMainController controller = loader.getController();
            // controller.setUsuario(usuario);

            Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("RapponCho - Panel de Usuario");

            // Animación de entrada
            aplicarAnimacionEntrada(root);

        } catch (Exception e) {
            mostrarError("Error al cargar la pantalla principal: " + e.getMessage());
            btnIniciarSesion.setDisable(false);
        }
    }

    /**
     * Navega a la pantalla principal del administrador
     */
    private void navegarAPantallaAdmin(Administrador admin) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/fx10/proyectofinals2/AdminMain.fxml")
            );
            Parent root = loader.load();

            // Pasar el admin al controlador de la siguiente pantalla
            // AdminMainController controller = loader.getController();
            // controller.setAdministrador(admin);

            Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("RapponCho - Panel de Administración");

            aplicarAnimacionEntrada(root);

        } catch (Exception e) {
            mostrarError("Error al cargar la pantalla de administración: " + e.getMessage());
            btnIniciarSesion.setDisable(false);
        }
    }

    /**
     * Maneja la navegación a la pantalla de registro
     */
    @FXML
    private void handleIrARegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/fx10/proyectofinals2/Registro.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) linkRegistro.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("RapponCho - Registro");

            aplicarAnimacionEntrada(root);

        } catch (Exception e) {
            mostrarError("Error al cargar la pantalla de registro: " + e.getMessage());
        }
    }

    /**
     * Valida que los campos no estén vacíos
     */
    private boolean validarCampos() {
        if (txtUsuario.getText().trim().isEmpty()) {
            mostrarError("Por favor, ingresa tu nombre de usuario");
            txtUsuario.requestFocus();
            return false;
        }

        if (txtContrasena.getText().isEmpty()) {
            mostrarError("Por favor, ingresa tu contraseña");
            txtContrasena.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Muestra un mensaje de error
     */
    private void mostrarError(String mensaje) {
        lblError.setText("❌ " + mensaje);
        lblError.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #dc2626; -fx-padding: 12 16; -fx-background-radius: 6;");
        lblError.setVisible(true);

        // Animación de shake
        aplicarAnimacionShake(lblError);
    }

    /**
     * Muestra un mensaje de éxito
     */
    private void mostrarExito(String mensaje) {
        lblError.setText("✓ " + mensaje);
        lblError.setStyle("-fx-background-color: #d1fae5; -fx-text-fill: #059669; -fx-padding: 12 16; -fx-background-radius: 6;");
        lblError.setVisible(true);
    }

    /**
     * Limpia el mensaje de error
     */
    private void limpiarError() {
        lblError.setVisible(false);
    }

    /**
     * Configura animaciones de hover para botones
     */
    private void configurarAnimaciones() {
        // Animación hover para el botón
        btnIniciarSesion.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btnIniciarSesion);
            st.setToX(1.03);
            st.setToY(1.03);
            st.play();
        });

        btnIniciarSesion.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btnIniciarSesion);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    /**
     * Configura validaciones en tiempo real
     */
    private void configurarValidaciones() {
        // Limpiar error cuando el usuario empiece a escribir
        txtUsuario.textProperty().addListener((obs, oldVal, newVal) -> {
            if (lblError.isVisible() && !newVal.trim().isEmpty()) {
                limpiarError();
            }
        });

        txtContrasena.textProperty().addListener((obs, oldVal, newVal) -> {
            if (lblError.isVisible() && !newVal.isEmpty()) {
                limpiarError();
            }
        });

        // Enter para submit
        txtContrasena.setOnAction(e -> handleIniciarSesion(null));
    }

    /**
     * Aplica animación de shake a un nodo
     */
    private void aplicarAnimacionShake(javafx.scene.Node nodo) {
        javafx.animation.TranslateTransition tt = new javafx.animation.TranslateTransition(Duration.millis(50), nodo);
        tt.setFromX(0);
        tt.setByX(10);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.play();
    }

    /**
     * Aplica animación de entrada a una escena
     */
    private void aplicarAnimacionEntrada(Parent root) {
        FadeTransition ft = new FadeTransition(Duration.millis(400), root);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }
}