package co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects;

public class DireccionSimpleDTO {
    private String idDireccion;
    private String alias;
    private String calle;
    private String municipio;
    private String codigoPostal;

    public DireccionSimpleDTO(String idDireccion, String alias, String calle,
                        String municipio, String codigoPostal) {
        this.idDireccion = idDireccion;
        this.alias = alias;
        this.calle = calle;
        this.municipio = municipio;
        this.codigoPostal = codigoPostal;
    }

    // Getters
    public String getIdDireccion() { return idDireccion; }
    public String getAlias() { return alias; }
    public String getCalle() { return calle; }
    public String getMunicipio() { return municipio; }
    public String getCodigoPostal() { return codigoPostal; }
}


