module co.edu.uniquindio.rapponcho {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.rapponcho to javafx.fxml;
    exports co.edu.uniquindio.rapponcho;
}