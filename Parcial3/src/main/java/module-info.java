module co.edu.uniquindio.parcial3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.parcial3 to javafx.fxml;
    exports co.edu.uniquindio.parcial3;
}