package co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects;

public class MetodoPagoDTO {
    private String alias;
    private String tipo;
    private String numeroEnmascarado;
    private String fechaRegistro;

    public MetodoPagoDTO(String alias, String tipo,
                         String numeroEnmascarado, String fechaRegistro) {
        this.alias = alias;
        this.tipo = tipo;
        this.numeroEnmascarado = numeroEnmascarado;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters
    public String getAlias() { return alias; }
    public String getTipo() { return tipo; }
    public String getNumeroEnmascarado() { return numeroEnmascarado; }
    public String getFechaRegistro() { return fechaRegistro; }

    public String getInfoCompleta() {
        return String.format("%s - %s (%s)", alias, tipo, numeroEnmascarado);
    }
}