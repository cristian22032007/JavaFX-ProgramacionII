package co.edu.uniquindio.rapponcho.services;

import co.edu.uniquindio.rapponcho.model.*;
import co.edu.uniquindio.rapponcho.utils.GeneradorID;

/**
 * Servicio para calcular tarifas
 * RF-003, RF-031, RF-032
 */
public class TarifaService {

    /**
     * Cotizar Tarifa
     * @param distancia
     * @param peso
     * @param volumen
     * @param conSeguro
     * @param conPrioridad
     * @param esFragil
     * @param requiereFirma
     * @return
     */
    public ITarifa cotizarTarifa(double distancia, double peso, double volumen,
                                 boolean conSeguro, boolean conPrioridad,
                                 boolean esFragil, boolean requiereFirma) {

        // Crear tarifa base
        String idTarifa = GeneradorID.generarIDTarifa();
        ITarifa tarifa = new Tarifa.Builder()
                .idTarifa(idTarifa)
                .costoPorKm(2000)
                .costoPorKg(1500)
                .costoPorM3(5000)
                .build();

        // Aplicar decoradores según servicios adicionales
        if (conSeguro) {
            tarifa = new TarifaSeguro(tarifa);
        }
        if (conPrioridad) {
            tarifa = new TarifaPrioridad(tarifa);
        }
        if (esFragil) {
            tarifa = new TarifaFragil(tarifa);
        }
        if (requiereFirma) {
            tarifa = new TarifaFirmaRequerida(tarifa);
        }

        return tarifa;
    }

    /**
     * Deglosar Tarifa
     * @param tarifa
     * @param distancia
     * @param peso
     * @param volumen
     * @return
     */
    public String desglosarTarifa(ITarifa tarifa, double distancia,
                                  double peso, double volumen) {
        double costoTotal = tarifa.CalcularCosto(distancia, peso, volumen);

        StringBuilder desglose = new StringBuilder();
        desglose.append("Descripción: ").append(tarifa.getDescripcion()).append("\n");
        desglose.append("Distancia: ").append(String.format("%.2f km", distancia)).append("\n");
        desglose.append("Peso: ").append(String.format("%.2f kg", peso)).append("\n");
        desglose.append("Volumen: ").append(String.format("%.4f m³", volumen)).append("\n");
        desglose.append("Costo total: $").append(String.format("%.2f", costoTotal));

        return desglose.toString();
    }
}