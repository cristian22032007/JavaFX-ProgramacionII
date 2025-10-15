module co.edu.uniquindio.fx10.sistemagestiopizza {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens co.edu.uniquindio.fx10.sistemagestiopizza to javafx.fxml;
    exports co.edu.uniquindio.fx10.sistemagestiopizza;
}