package co.edu.uniquindio.fx10.proyectofinals2.model;

public class Direccion {
    private String idDireccion;
    private String alias;
    private String calle;
    private ZonaDireccion municipio;

    public Direccion(String idDireccion, String alias, String calle, ZonaDireccion municipio) {
        this.idDireccion = idDireccion;
        this.alias = alias;
        this.calle = calle;
        this.municipio = municipio;

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

    public ZonaDireccion getMunicipio() {
        return municipio;
    }


}
