module co.edu.uniquindio.fx10.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.fx10.demo to javafx.fxml;
    exports co.edu.uniquindio.fx10.demo;
}