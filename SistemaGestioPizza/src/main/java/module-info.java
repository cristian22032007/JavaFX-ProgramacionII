module co.edu.uniquindio.fx10.sistemagestiopizza {
        requires javafx.controls;
        requires javafx.fxml;
    requires java.desktop;

    // Permite que FXMLLoader acceda a las clases del paquete controller
        opens co.edu.uniquindio.fx10.sistemagestiopizza.controller to javafx.fxml;

        // Exporta el paquete base para que otras partes del programa puedan usarlo
        exports co.edu.uniquindio.fx10.sistemagestiopizza;
        }