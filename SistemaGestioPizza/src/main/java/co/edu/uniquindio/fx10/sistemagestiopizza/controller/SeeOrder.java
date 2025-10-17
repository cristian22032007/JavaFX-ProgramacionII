package co.edu.uniquindio.fx10.sistemagestiopizza.controller;


import co.edu.uniquindio.fx10.sistemagestiopizza.App;
import co.edu.uniquindio.fx10.sistemagestiopizza.model.Order;
import co.edu.uniquindio.fx10.sistemagestiopizza.repository.OrderSistem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import static java.awt.Color.GRAY;


public class SeeOrder {
    @FXML
    private FlowPane contentOrder;
    @FXML
    private Button btnVolver;

    private OrderSistem orderSistem;

    public void initialize() {
        // Obtener la instancia única del sistema
        orderSistem = OrderSistem.getInstance();
        configurarContenedor();
        cargarOrdenes();
    }

    private void configurarContenedor() {
        contentOrder.setHgap(15);
        contentOrder.setVgap(15);
        contentOrder.setPadding(new Insets(20));
        contentOrder.setAlignment(Pos.TOP_LEFT);
    }
    public void cargarOrdenes() {

            // Obtener todas las órdenes del sistema
            List<Order> ordenes = orderSistem.getOrders();

            if (ordenes.isEmpty()) {
                Label noOrdenes = new Label("No hay órdenes registradas");
                noOrdenes.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                noOrdenes.setStyle("-fx-text-fill: #000000");
                contentOrder.getChildren().add(noOrdenes);
                return;
            }

            // Crear una caja por cada orden
            for (int i = 0; i < ordenes.size(); i++) {
                Order orden = ordenes.get(i);
                VBox cajaOrden = crearCajaOrden(orden, i + 1, GRAY);
                contentOrder.getChildren().add(cajaOrden);
            }
        }

        private VBox crearCajaOrden(Order orden, int numeroOrden, Color color) {
            VBox caja = new VBox(10);
            caja.setPadding(new Insets(15));
            caja.setPrefWidth(280);
            caja.setMinWidth(280);
            caja.setMaxWidth(280);

            // Estilo de la caja
            caja.setStyle(
                    "-fx-background-color: #BDBDBD;" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-color: #000000;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 10;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);"
            );

            // Título de la orden
            Label lblTitulo = new Label("Orden #" + numeroOrden);
            lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            // Separador
            Region separador1 = new Region();
            separador1.setPrefHeight(1);
            separador1.setStyle("-fx-background-color: #000000;");
            // Separador
            Region separador2 = new Region();
            separador1.setPrefHeight(1);
            separador1.setStyle("-fx-background-color: #000000;");



            // El método getDescription() del decorator concatena todo
            Label lblDescripcion = new Label(orden.getDescription());
            lblDescripcion.setWrapText(true);
            lblDescripcion.setFont(Font.font("Arial", 13));
            lblDescripcion.setMaxWidth(250);
            

            // Precio total (incluye base + decoradores)
            // El método getPrice() del decorator suma todo
            HBox contenedorPrecio = new HBox();
            contenedorPrecio.setAlignment(Pos.CENTER_RIGHT);

            Label lblPrecio = new Label("Total: $" + String.format("%.2f", orden.getCost()));
            lblPrecio.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            lblPrecio.setStyle("-fx-text-fill: #2E7D32;");

            contenedorPrecio.getChildren().add(lblPrecio);

            caja.getChildren().addAll(
                    lblTitulo,
                    separador1,
                    lblDescripcion,
                    separador2,
                    contenedorPrecio
            );

            return caja;
        }
    @FXML
    private void onVolverMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/fx10/sistemagestiopizza/Home.fxml"));
            Parent home = loader.load();
            App.getPrimaryStage().getScene().setRoot(home);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo volver al menú principal", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}



