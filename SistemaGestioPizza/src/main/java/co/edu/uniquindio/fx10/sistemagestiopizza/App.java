package co.edu.uniquindio.fx10.sistemagestiopizza;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    private static Stage primaryStage;
    @Override
    public void start(Stage stage) throws IOException {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/fx10/sistemagestiopizza/Home.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 900, 600);
            primaryStage = stage;
            primaryStage.setTitle("Sistema de Pedidos");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        public static Stage getPrimaryStage () {
            return primaryStage;
        }
        public static void main (String[]args){
            launch();
        }
    }
