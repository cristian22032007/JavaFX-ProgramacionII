package co.edu.uniquindio.fx10.proyectofinals2;

import co.edu.uniquindio.fx10.proyectofinals2.reposytorie.Repositorio;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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

            // Cargar el archivo FXML del Login
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/fx10/proyectofinals2/Login.fxml")
            );
            Parent root = loader.load();

            // Crear la escena
            Scene scene = new Scene(root);

            // Configurar el Stage (ventana principal)
            primaryStage.setTitle("RapponCho - Envíos Urbanos Same-Day");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setMinWidth(900);
            primaryStage.setMinHeight(650);

            // Opcional: Agregar ícono a la aplicación
            try {
                primaryStage.getIcons().add(
                        new Image(getClass().getResourceAsStream("/co/edu/uniquindio/fx10/proyectofinals2/images/logo.png"))
                );
            } catch (Exception e) {
                System.out.println("No se pudo cargar el ícono de la aplicación");
            }

            // Centrar la ventana en la pantalla
            primaryStage.centerOnScreen();

            // Mostrar la ventana
            primaryStage.show();

            System.out.println("✅ Aplicación RapponCho iniciada correctamente");
            System.out.println("📦 Pantalla de Login cargada");

        } catch (Exception e) {
            System.err.println("❌ Error al iniciar la aplicación:");
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
        System.out.println("🛑 Aplicación RapponCho cerrada");
    }

    public static void main(String[] args) {
        launch(args);
    }
}