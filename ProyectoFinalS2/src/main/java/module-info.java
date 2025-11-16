module co.edu.uniquindio.fx10.proyectofinals2 {
    // Módulos JavaFX requeridos
    requires javafx.controls;
    requires javafx.fxml;

    // ========== MÓDULOS PARA REPORTES PDF Y EXCEL ==========

    // Apache PDFBox para generación de PDFs
    requires org.apache.pdfbox;

    // Apache POI para generación de archivos Excel
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;

    // Dependencias adicionales de POI
    requires org.apache.commons.collections4;
    requires org.apache.commons.compress;
    requires org.apache.fontbox;
    // Para abrir archivos automáticamente
    requires java.desktop;

    // Abrir paquetes para JavaFX FXML
    opens co.edu.uniquindio.fx10.proyectofinals2 to javafx.fxml;
    opens co.edu.uniquindio.fx10.proyectofinals2.controllers to javafx.fxml;
    opens co.edu.uniquindio.fx10.proyectofinals2.model to javafx.fxml;
    opens co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects to javafx.fxml;

    // Exportar paquetes necesarios
    exports co.edu.uniquindio.fx10.proyectofinals2;
    exports co.edu.uniquindio.fx10.proyectofinals2.controllers;
    exports co.edu.uniquindio.fx10.proyectofinals2.model;
    exports co.edu.uniquindio.fx10.proyectofinals2.services;
    exports co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects;
    exports co.edu.uniquindio.fx10.proyectofinals2.utils;
    exports co.edu.uniquindio.fx10.proyectofinals2.reposytorie;

}