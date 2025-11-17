package co.edu.uniquindio.rapponcho.dataTransferObjects;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PagoSimpleDTO {
    private String idPago;
    private double monto;
    private String fecha;
    private String metodoPago;
    private String resultado;

    public PagoSimpleDTO(String idPago, double monto, LocalDateTime fecha,
                   String metodoPago, String resultado) {
        this.idPago = idPago;
        this.monto = monto;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.fecha = fecha.format(formatter);
        this.metodoPago = metodoPago;
        this.resultado = resultado;
    }

    // Getters
    public String getIdPago() { return idPago; }
    public double getMonto() { return monto; }
    public String getFecha() { return fecha; }
    public String getMetodoPago() { return metodoPago; }
    public String getResultado() { return resultado; }

}


