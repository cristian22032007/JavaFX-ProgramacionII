package co.edu.uniquindio.rapponcho;

import co.edu.uniquindio.rapponcho.reposytorie.Repositorio;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Cristian Fernando Delgado Cruz

/**
 * Clase principal de la aplicación RapponCho
 * Punto de entrada del sistema de envíos urbanos
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Inicializar el repositorio con datos por defecto
            Repositorio.getInstancia().inicializarSistemaPorDefecto();


            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/rapponcho/Login.fxml")
            );
            Parent root = loader.load();

            // Crear la escena
            Scene scene = new Scene(root);

            // Configurar el Stage (ventana principal)
            primaryStage.setTitle("RapponCho - Envíos Urbanos Same-Day");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(700);

            // Centrar la ventana en la pantalla
            primaryStage.centerOnScreen();

            // Mostrar la ventana
            primaryStage.show();

            System.out.println("Aplicación RapponCho iniciada correctamente");
            System.out.println("Pantalla de Login cargada");

        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación:");
            e.printStackTrace();

            // Mostrar alerta de error
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR
            );
            alert.setTitle("Error Fatal");
            alert.setHeaderText("No se pudo iniciar la aplicación");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();

            System.exit(1);
        }
    }

    @Override
    public void stop() {
        System.out.println("Aplicación RapponCho cerrada");
    }

    public static void main(String[] args) {
        launch(args);
    }
}