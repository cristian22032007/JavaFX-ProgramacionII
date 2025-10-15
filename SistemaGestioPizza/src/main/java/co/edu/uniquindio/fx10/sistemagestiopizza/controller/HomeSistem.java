package co.edu.uniquindio.fx10.sistemagestiopizza.controller;

import co.edu.uniquindio.fx10.sistemagestiopizza.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomeSistem {
    @FXML
    private Button BtnCreateOrder;
    @FXML
    private Button BtnVerOrder;

    @FXML
    private void onBtnCreateOrder() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/fx10/sistemagestiopizza/CreateOrder.fxml"));
            Parent form = loader.load();
            CreateOrder controller = loader.getController();
            controller.setHomeSistem(this);
            App.getPrimaryStage().getScene().setRoot(form);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBtnSeeOrder() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/fx10/sistemagestiopizza/SeeOrders.fxml"));
            Parent dashboard = loader.load();
            App.getPrimaryStage().getScene().setRoot(dashboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


