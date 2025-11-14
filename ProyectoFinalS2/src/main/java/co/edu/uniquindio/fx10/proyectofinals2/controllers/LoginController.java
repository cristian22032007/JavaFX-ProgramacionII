package co.edu.uniquindio.fx10.proyectofinals2.controllers;


import co.edu.uniquindio.fx10.proyectofinals2.model.Administrador;
import co.edu.uniquindio.fx10.proyectofinals2.model.Usuario;
import co.edu.uniquindio.fx10.proyectofinals2.reposytorie.Repositorio;
import co.edu.uniquindio.fx10.proyectofinals2.services.AdministradorService;
import co.edu.uniquindio.fx10.proyectofinals2.services.UsuarioService;
import co.edu.uniquindio.fx10.proyectofinals2.utils.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private RadioButton rbUsuario;
    @FXML private RadioButton rbAdministrador;
    @FXML private CheckBox chkRecordarme;
    @FXML private Button btnIniciarSesion;
    @FXML private Hyperlink linkRegistro;

    private final UsuarioService usuarioService;
    private final AdministradorService administradorService;

    public LoginController() {
        this.usuarioService = new UsuarioService();
        this.administradorService = new AdministradorService();
        Repositorio.getInstancia().inicializarSistemaPorDefecto();
    }

    @FXML
    public void initialize() {
        // Enter para submit
        txtContrasena.setOnAction(e -> handleIniciarSesion(null));
    }

    @FXML
    private void handleIniciarSesion(ActionEvent event) {
        // Validar campos vacíos
        if (txtUsuario.getText().trim().isEmpty()) {
            AlertHelper.mostrarError("Error", "Por favor, ingresa tu nombre de usuario");
            txtUsuario.requestFocus();
            return;
        }

        if (txtContrasena.getText().isEmpty()) {
            AlertHelper.mostrarError("Error", "Por favor, ingresa tu contraseña");
            txtContrasena.requestFocus();
            return;
        }

        String usuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText();
        boolean esUsuario = rbUsuario.isSelected();

        btnIniciarSesion.setDisable(true);

        try {
            if (esUsuario) {
                Usuario usuarioAutenticado = usuarioService.iniciarSesion(usuario, contrasena);
                AlertHelper.mostrarExito("Éxito", "¡Bienvenido, " + usuarioAutenticado.getNombre() + "!");
                navegarAPantallaUsuario(usuarioAutenticado);
            } else {
                Administrador adminAutenticado = administradorService.iniciarSesion(usuario, contrasena);
                AlertHelper.mostrarExito("Éxito", "¡Bienvenido, Administrador!");
                navegarAPantallaAdmin(adminAutenticado);
            }
        } catch (Exception e) {
            AlertHelper.mostrarError("Error de Autenticación", e.getMessage());
            btnIniciarSesion.setDisable(false);
        }
    }

    private void navegarAPantallaUsuario(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/fx10/proyectofinals2/UsuarioMain.fxml")
            );
            Parent root = loader.load();

            // Pasar el usuario al controlador si existe
            // UsuarioMainController controller = loader.getController();
            // controller.setUsuario(usuario);

            Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("RapponCho - Panel de Usuario");

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudo cargar la pantalla principal: " + e.getMessage());
            btnIniciarSesion.setDisable(false);
        }
    }

    private void navegarAPantallaAdmin(Administrador admin) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/fx10/proyectofinals2/AdminMain.fxml")
            );
            Parent root = loader.load();

            // Pasar el admin al controlador si existe
            // AdminMainController controller = loader.getController();
            // controller.setAdministrador(admin);

            Stage stage = (Stage) btnIniciarSesion.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("RapponCho - Panel de Administración");

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudo cargar la pantalla de administración: " + e.getMessage());
            btnIniciarSesion.setDisable(false);
        }
    }

    @FXML
    private void handleIrARegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/fx10/proyectofinals2/Registro.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) linkRegistro.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("RapponCho - Registro");

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudo cargar la pantalla de registro: " + e.getMessage());
        }
    }

    @FXML
    private void handleRecuperarContrasena(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/fx10/proyectofinals2/RecuperarContrasena.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("RapponCho - Recuperar Contraseña");

        } catch (IOException e) {
            AlertHelper.mostrarError("Error", "No se pudo cargar la ventana de recuperación: " + e.getMessage());
        }
    }
}