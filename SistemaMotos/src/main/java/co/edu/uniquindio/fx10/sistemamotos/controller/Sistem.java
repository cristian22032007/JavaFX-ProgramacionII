package co.edu.uniquindio.fx10.sistemamotos.controller;

import co.edu.uniquindio.fx10.sistemamotos.model.Moto;
import co.edu.uniquindio.fx10.sistemamotos.repository.SistemMoto;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;



public class Sistem {
    @FXML
    private ScrollPane ScrollPane;
    @FXML
    private VBox ContentCreator;
    @FXML
    private TextField txtPlaca;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtModelo;
    @FXML
    private Button btnGuardar;
    @FXML
    private SistemMoto sistemMoto;

    @FXML
    public void initialize() {
        sistemMoto = SistemMoto.getInstacia();
    }
    @FXML
    private void onGuardarMoto() {
        if (!validarCampos()) {
            return;
        }

        try {
            String placa = txtPlaca.getText().trim();
            String marca = txtMarca.getText().trim();
            int modelo = Integer.parseInt(txtModelo.getText().trim());

            // Verificar si el código ya existe
            if (sistemMoto.buscarPorCodigo(placa) != null) {
                mostrarAlerta("Error", "Ya existe una moto con esa placa", Alert.AlertType.ERROR);
                return;
            }

            // Crear y guardar el producto
            Moto nuevaMoto = new Moto.Builder().placa(placa).marca(marca).modelo(modelo).build();
            sistemMoto.addMoto(nuevaMoto);

            mostrarAlerta("Éxito", "Moto creado correctamente", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El Modelo debe ser valor numérico válido", Alert.AlertType.ERROR);
        }
        txtPlaca.clear();
        txtMarca.clear();
        txtModelo.clear();
        cargarOrdenes();

    }
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    private boolean validarCampos() {
        if (txtPlaca.getText().trim().isEmpty()) {
            mostrarAlerta("Error de validación", "La Placa es obligatorio", Alert.AlertType.WARNING);
            return false;
        }
        if (txtMarca.getText().trim().isEmpty()) {
            mostrarAlerta("Error de validación", "La Marca es obligatorio", Alert.AlertType.WARNING);
            return false;
        }
        if (txtModelo.getText().trim().isEmpty()) {
            mostrarAlerta("Error de validación", "El Modelo es obligatorio", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
        public void cargarOrdenes() {

        // Obtener todas las órdenes del sistema
            List<Moto> motos = sistemMoto.getMotos();

            if (motos.isEmpty()) {
                VBox vbox = new VBox();
                Label noMotos = new Label("No hay Motos registradas");
                noMotos.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                noMotos.setStyle("-fx-text-fill: #000000");
                vbox.getChildren().add(noMotos);

                ScrollPane.setContent(vbox);
                return;
            }

        // Crear una caja por cada orden
            for (int i = 0; i < motos.size(); i++) {
                Moto moto = motos.get(i);
                VBox motoMoto = crearCajaOrden(moto, i + 1);
                ScrollPane.setContent(motoMoto);
            }
        }
    private VBox crearCajaOrden(Moto moto, int numeroOrden) {
        VBox caja = new VBox();
        caja.setPadding(new Insets(5));
        caja.setPrefWidth(150);
        caja.setMinWidth(150);
        caja.setMaxWidth(150);

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
        Label lblTitulo = new Label("Moto #" + numeroOrden);
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Separador
        Region separador1 = new Region();
        separador1.setPrefHeight(1);
        separador1.setStyle("-fx-background-color: #000000;");
        // Separador
        Region separador2 = new Region();
        separador1.setPrefHeight(1);
        separador1.setStyle("-fx-background-color: #000000;");




        Label lblMarca = new Label(moto.getMarca());
        Label lblPlaca = new Label(moto.getPlaca());
        Label lblModelo = new Label (String.valueOf(moto.getModelo()));
        lblMarca.setFont(Font.font("Arial", 13));
        lblModelo.setFont(Font.font("Arial", 13));
        lblPlaca.setFont(Font.font("Arial", 13));


        // Precio total (incluye base + decoradores)
        // El método getPrice() del decorator suma todo


        caja.getChildren().addAll(
                lblTitulo,
                separador1,
                lblPlaca,
                lblMarca,
                lblModelo,
                separador2
        );

        return caja;
    }




}
