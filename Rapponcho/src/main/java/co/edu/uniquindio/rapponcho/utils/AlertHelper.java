package co.edu.uniquindio.rapponcho.utils;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * Utilidad centralizada para mostrar alertas y diálogos
 * Proporciona métodos consistentes para feedback al usuario
 */
public class AlertHelper {

    /**
     * Muestra una alerta de éxito
     */
    public static void mostrarExito(String titulo, String mensaje) {
        mostrarAlerta(Alert.AlertType.INFORMATION, titulo, mensaje, "✓ Éxito");
    }

    /**
     * Muestra una alerta de error
     */
    public static void mostrarError(String titulo, String mensaje) {
        mostrarAlerta(Alert.AlertType.ERROR, titulo, mensaje, "❌ Error");
    }

    /**
     * Muestra una alerta de advertencia
     */
    public static void mostrarAdvertencia(String titulo, String mensaje) {
        mostrarAlerta(Alert.AlertType.WARNING, titulo, mensaje, "⚠ Advertencia");
    }

    /**
     * Muestra una alerta de información
     */
    public static void mostrarInfo(String titulo, String mensaje) {
        mostrarAlerta(Alert.AlertType.INFORMATION, titulo, mensaje, "ℹ Información");
    }

    /**
     * Muestra un diálogo de confirmación
     * @return true si el usuario confirma, false si cancela
     */
    public static boolean mostrarConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.initModality(Modality.APPLICATION_MODAL);

        // Personalizar botones
        ButtonType btnSi = new ButtonType("Sí");
        ButtonType btnNo = new ButtonType("No");
        alert.getButtonTypes().setAll(btnSi, btnNo);

        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == btnSi;
    }

    /**
     * Muestra un diálogo de entrada de texto
     * @return El texto ingresado por el usuario, o null si cancela
     */
    public static Optional<String> mostrarEntradaTexto(String titulo, String mensaje, String valorPorDefecto) {
        TextInputDialog dialog = new TextInputDialog(valorPorDefecto);
        dialog.setTitle(titulo);
        dialog.setHeaderText(null);
        dialog.setContentText(mensaje);
        dialog.initModality(Modality.APPLICATION_MODAL);

        return dialog.showAndWait();
    }

    /**
     * Muestra una alerta de validación con lista de errores
     */
    public static void mostrarErrorValidacion(String mensaje) {
        mostrarError("Datos Inválidos", mensaje);
    }

    /**
     * Muestra una alerta de operación exitosa
     */
    public static void mostrarOperacionExitosa(String operacion) {
        mostrarExito("Operación Exitosa", operacion + " se realizó correctamente.");
    }

    /**
     * Muestra una alerta de operación fallida
     */
    public static void mostrarOperacionFallida(String operacion, String razon) {
        mostrarError("Operación Fallida",
                "No se pudo " + operacion + ".\n\nRazón: " + razon);
    }

    /**
     * Método base para crear alertas consistentes
     */
    public static void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje, String header) {
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
    }

    /**
     * Muestra error desde Exception
     */
    public static void mostrarExcepcion(String titulo, Exception e) {
        mostrarError(titulo, e.getMessage());
    }

    /**
     * Muestra alerta de sesión expirada
     */
    public static void mostrarSesionExpirada() {
        mostrarAdvertencia("Sesión Expirada",
                "Tu sesión ha expirado. Por favor, inicia sesión nuevamente.");
    }

    /**
     * Muestra alerta de no autorizado
     */
    public static void mostrarNoAutorizado() {
        mostrarError("Acceso Denegado",
                "No tienes permisos para realizar esta acción.");
    }
}