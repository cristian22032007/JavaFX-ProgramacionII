module co.edu.uniquindio.fx10.proyectofinals2 {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    // PDFBox y POI - modo no-modular (compatibilidad)
    requires org.apache.pdfbox;
    requires org.apache.fontbox;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.xmlbeans;
    requires org.apache.commons.collections4;
    requires org.apache.commons.compress;

    // Opens para reflexión JavaFX
    opens co.edu.uniquindio.fx10.proyectofinals2 to javafx.fxml;
    opens co.edu.uniquindio.fx10.proyectofinals2.controllers to javafx.fxml;
    opens co.edu.uniquindio.fx10.proyectofinals2.model to javafx.fxml;
    opens co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects to javafx.fxml;

    // Exports
    exports co.edu.uniquindio.fx10.proyectofinals2;
    exports co.edu.uniquindio.fx10.proyectofinals2.controllers;
    exports co.edu.uniquindio.fx10.proyectofinals2.model;
    exports co.edu.uniquindio.fx10.proyectofinals2.services;
    exports co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects;
    exports co.edu.uniquindio.fx10.proyectofinals2.utils;
    exports co.edu.uniquindio.fx10.proyectofinals2.reposytorie;
}