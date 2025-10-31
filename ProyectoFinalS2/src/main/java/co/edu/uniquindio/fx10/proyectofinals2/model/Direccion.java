package co.edu.uniquindio.fx10.proyectofinals2.model;

public class Direccion {
    private String idDireccion;
    private String alias;
    private String calle;
    private ZonaCobertura municipio;
    private String codigoPostal;
    private double coordenadaX;
    private double coordenadaY;

    public Direccion(String idDireccion, String alias, String calle, ZonaCobertura municipio, String codigoPostal, double coordenadaX, double coordenadaY) {
        this.idDireccion = idDireccion;
        this.alias = alias;
        this.calle = calle;
        this.municipio = municipio;
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

    public ZonaCobertura getMunicipio() {
        return municipio;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public double getCoordenadaX() {
        return coordenadaX;
    }

    public double getCoordenadaY() {
        return coordenadaY;
    }
}
