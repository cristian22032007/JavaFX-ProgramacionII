package co.edu.uniquindio.rapponcho.dataTransferObjects;


/**
 * DTO para la información de tarifas
 * Incluye desglose de costos
 */
public class TarifaDTO {
    private String idTarifa;
    private double tarifaBase;
    private double costoPorKm;
    private double costoPorKg;
    private double costoPorM3;
    private double costoTotal;
    private String descripcion;

    private double distanciaKm;
    private double pesoKg;
    private double volumenM3;

    public TarifaDTO(String idTarifa, double tarifaBase, double costoPorKm,
                     double costoPorKg, double costoPorM3, double costoTotal,
                     String descripcion, double distanciaKm, double pesoKg,
                     double volumenM3) {
        this.idTarifa = idTarifa;
        this.tarifaBase = tarifaBase;
        this.costoPorKm = costoPorKm;
        this.costoPorKg = costoPorKg;
        this.costoPorM3 = costoPorM3;
        this.costoTotal = costoTotal;
        this.descripcion = descripcion;
        this.distanciaKm = distanciaKm;
        this.pesoKg = pesoKg;
        this.volumenM3 = volumenM3;
    }

    public String getIdTarifa() { return idTarifa; }
    public double getTarifaBase() { return tarifaBase; }
    public double getCostoPorKm() { return costoPorKm; }
    public double getCostoPorKg() { return costoPorKg; }
    public double getCostoPorM3() { return costoPorM3; }
    public double getCostoTotal() { return costoTotal; }
    public String getDescripcion() { return descripcion; }
    public double getDistanciaKm() { return distanciaKm; }
    public double getPesoKg() { return pesoKg; }
    public double getVolumenM3() { return volumenM3; }

    public double getCostoDistancia() {
        return distanciaKm * costoPorKm;
    }

    public double getCostoPeso() {
        return pesoKg * costoPorKg;
    }

    public double getCostoVolumen() {
        return volumenM3 * costoPorM3;
    }


    public String getDesglose() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Tarifa base: $%.2f\n", tarifaBase));
        sb.append(String.format("Distancia (%.2f km × $%.2f): $%.2f\n",
                distanciaKm, costoPorKm, getCostoDistancia()));
        sb.append(String.format("Peso (%.2f kg × $%.2f): $%.2f\n",
                pesoKg, costoPorKg, getCostoPeso()));
        sb.append(String.format("Volumen (%.4f m³ × $%.2f): $%.2f\n",
                volumenM3, costoPorM3, getCostoVolumen()));
        sb.append(String.format("\nTOTAL: $%.2f", costoTotal));
        return sb.toString();
    }
}