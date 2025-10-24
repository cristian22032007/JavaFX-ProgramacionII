package co.edu.uniquindio.fx10.parcialnumero2.controller;

import co.edu.uniquindio.fx10.parcialnumero2.App;
import co.edu.uniquindio.fx10.parcialnumero2.model.Inmueble;
import co.edu.uniquindio.fx10.parcialnumero2.repository.Inmobiliaria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class VerInmuebles   {
    @FXML
    private TableView<Inmueble> tablaInmuebles;

    @FXML
    private TableColumn<Inmueble, String> colTipo;

    @FXML
    private TableColumn<Inmueble, String> colCiudad;

    @FXML
    private TableColumn<Inmueble, Integer> colNumeroHabitantes;

    @FXML
    private TableColumn<Inmueble, Integer> colNumeroPisos;

    @FXML
    private TableColumn<Inmueble, Double> colPrecio;


    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnVolver;


    private Inmobiliaria Sistema;
    private ObservableList<Inmueble> listaInmuebles;

    public void initialize() {
        Sistema = Inmobiliaria.getIsntancia();

        // Configurar las columnas de la tabla
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colNumeroHabitantes.setCellValueFactory(new PropertyValueFactory<>("numeroHabitantes"));
        colNumeroPisos.setCellValueFactory(new PropertyValueFactory<>("numeroPisos"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));


        // Formatear la columna de precio
        colPrecio.setCellFactory(column -> new TableCell<Inmueble, Double>() {
            @Override
            protected void updateItem(Double precio, boolean empty) {
                super.updateItem(precio, empty);
                if (empty || precio == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", precio));
                }
            }
        });

        // Cargar los productos
        cargarProductos();
    }

    /**
     * Carga los productos en la tabla
     */
    public void cargarProductos() {
        listaInmuebles = FXCollections.observableArrayList(Sistema.getInmuebles());
        tablaInmuebles.setItems(listaInmuebles);

}

    @FXML
    private void onVolverMenu() throws IOException{
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/fx10/parcialnumero2/Home.fxml"));
            Parent home = loader.load();
            App.getPrimaryStage().getScene().setRoot(home);

    }

    /**
     * Maneja el evento de click en el botón "Eliminar"
     */
    @FXML
    private void onEliminarProducto() {
        Inmueble inmuebleSeleccionado = tablaInmuebles.getSelectionModel().getSelectedItem();

        if (inmuebleSeleccionado == null) {
            mostrarAlerta("Advertencia", "Por favor seleccione un inmueble para eliminar", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar el inmueble?");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Sistema.removeInmueble(inmuebleSeleccionado);
                cargarProductos();
                mostrarAlerta("Éxito", "Eliminado correctamente", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}

