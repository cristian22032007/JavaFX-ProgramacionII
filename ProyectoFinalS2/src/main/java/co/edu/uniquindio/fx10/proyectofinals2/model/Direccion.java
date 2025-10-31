package co.edu.uniquindio.fx10.proyectofinals2.model;

import java.util.List;

public class Direccion {
    private String idDireccion;
    private String alias;
    private String calle;
    private String ciudad;
    private String codigoPostal;
    private String coordenadaX;
    private String coordenadaY;

    public Direccion(String idDireccion, String alias, String calle, String ciudad, String codigoPostal, String coordenadaX, String coordenadaY) {
        this.idDireccion = idDireccion;
        this.alias = alias;
        this.calle = calle;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
    }

    public String getIdDireccion() {
        return idDireccion;
    }

    public String getAlias() {
        return alias;
    }

    public String getCalle() {
        return calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public String getCoordenadaX() {
        return coordenadaX;
    }

    public String getCoordenadaY() {
        return coordenadaY;
    }
}
