module co.edu.uniquindio.fx10.sistemamotos {
    requires javafx.controls;
    requires javafx.fxml;

    // Permite que JavaFX pueda acceder a tu clase principal (HelloApplication)
    exports co.edu.uniquindio.fx10.sistemamotos;

    // Permite que FXMLLoader acceda a los controladores con @FXML
    opens co.edu.uniquindio.fx10.sistemamotos.controller to javafx.fxml;}