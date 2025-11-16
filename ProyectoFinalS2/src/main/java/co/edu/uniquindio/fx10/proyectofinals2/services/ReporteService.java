package co.edu.uniquindio.fx10.proyectofinals2.services;


import co.edu.uniquindio.fx10.proyectofinals2.model.*;
import co.edu.uniquindio.fx10.proyectofinals2.reposytorie.Repositorio;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import java.io.File;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para generar reportes en PDF y Excel
 * RF-009, RF-039
 */
public class ReporteService {
    private final Repositorio repositorio;
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ReporteService() {
        this.repositorio = Repositorio.getInstancia();
    }

    // ========== REPORTES PARA USUARIOS ==========

    /**
     * Genera reporte PDF de envíos del usuario
     */
    public File generarReporteUsuarioPDF(String idUsuario, LocalDateTime fechaInicio,
                                         LocalDateTime fechaFin) throws IOException {
        Usuario usuario = repositorio.getUsuarios().get(idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // Filtrar envíos del usuario
        List<Envio> envios = usuario.getEnvios().stream()
                .filter(e -> {
                    if (fechaInicio != null && e.getFechaCreacion().isBefore(fechaInicio)) {
                        return false;
                    }
                    if (fechaFin != null && e.getFechaCreacion().isAfter(fechaFin)) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        // Crear archivo temporal
        File archivo = File.createTempFile("reporte_usuario_", ".pdf");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = 750;
                float margin = 50;
                float fontSize = 12;

                // Título
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("REPORTE DE ENVIOS - RAPPONCHO");
                contentStream.endText();

                yPosition -= 30;

                // Información del usuario
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Usuario: " + usuario.getNombre());
                contentStream.endText();

                yPosition -= 15;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("ID: " + usuario.getId() + " | Email: " + usuario.getCorreo());
                contentStream.endText();

                yPosition -= 20;

                // Período
                String periodo = "Todos los envíos";
                if (fechaInicio != null && fechaFin != null) {
                    periodo = "Período: " + fechaInicio.format(FORMATO_FECHA) +
                            " - " + fechaFin.format(FORMATO_FECHA);
                }

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(periodo);
                contentStream.endText();

                yPosition -= 30;

                // Línea divisoria
                contentStream.moveTo(margin, yPosition);
                contentStream.lineTo(550, yPosition);
                contentStream.stroke();

                yPosition -= 20;

                // Estadísticas
                long totalEntregados = envios.stream()
                        .filter(e -> e.getEstado() == EstadoEnvio.ENTREGADO).count();
                double totalGastado = envios.stream()
                        .filter(e -> e.getPago() != null &&
                                e.getPago().getResultado() == ResultadoPago.APROBADO)
                        .mapToDouble(e -> e.getPago().getMonto()).sum();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("RESUMEN:");
                contentStream.endText();

                yPosition -= 18;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Total de envios: " + envios.size());
                contentStream.endText();

                yPosition -= 15;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Envios entregados: " + totalEntregados);
                contentStream.endText();

                yPosition -= 15;

                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Total gastado: $" + String.format("%.2f", totalGastado));
                contentStream.endText();

                yPosition -= 30;

                // Detalle de envíos
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("DETALLE DE ENVIOS:");
                contentStream.endText();

                yPosition -= 20;

                // Listar envíos
                for (Envio envio : envios) {
                    if (yPosition < 100) {
                        contentStream.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        PDPageContentStream newStream = new PDPageContentStream(document, page);
                        yPosition = 750;

                        // Continuar con el nuevo stream
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText("ID: " + envio.getIdEnvio());
                        contentStream.endText();
                    } else {
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText("ID: " + envio.getIdEnvio());
                        contentStream.endText();
                    }

                    yPosition -= 15;

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 9);
                    contentStream.newLineAtOffset(margin + 10, yPosition);
                    contentStream.showText("Origen: " + envio.getOrigen().getMunicipio().name() +
                            " -> Destino: " + envio.getDestino().getMunicipio().name());
                    contentStream.endText();

                    yPosition -= 12;

                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin + 10, yPosition);
                    contentStream.showText("Estado: " + envio.getEstado() +
                            " | Fecha: " + envio.getFechaCreacion().format(FORMATO_FECHA));
                    contentStream.endText();

                    yPosition -= 12;

                    if (envio.getPago() != null) {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin + 10, yPosition);
                        contentStream.showText("Costo: $" + String.format("%.2f", envio.getPago().getMonto()));
                        contentStream.endText();
                    }

                    yPosition -= 20;
                }

                // Footer
                yPosition = 30;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 8);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Generado: " + LocalDateTime.now().format(FORMATO_FECHA) +
                        " | RapponCho - Envios Urbanos Same-Day");
                contentStream.endText();
            }

            document.save(archivo);
        }

        return archivo;
    }



    // ========== REPORTES PARA ADMINISTRADORES ==========

    /**
     * Genera reporte completo del sistema para admin
     */
    public File generarReporteAdminPDF() throws IOException {
        File archivo = File.createTempFile("reporte_admin_", ".pdf");

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = 750;
                float margin = 50;

                // Título
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("REPORTE ADMINISTRATIVO");
                contentStream.endText();

                yPosition -= 25;

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("RapponCho - Dashboard Ejecutivo");
                contentStream.endText();

                yPosition -= 30;

                // Línea divisoria
                contentStream.moveTo(margin, yPosition);
                contentStream.lineTo(550, yPosition);
                contentStream.stroke();

                yPosition -= 30;

                // Estadísticas generales
                Map<String, Object> stats = obtenerEstadisticasGenerales();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("ESTADISTICAS GENERALES");
                contentStream.endText();

                yPosition -= 25;

                for (Map.Entry<String, Object> entry : stats.entrySet()) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 11);
                    contentStream.newLineAtOffset(margin + 10, yPosition);
                    contentStream.showText(entry.getKey() + ": " + entry.getValue());
                    contentStream.endText();
                    yPosition -= 18;
                }

                yPosition -= 20;

                // Envíos por estado
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("ENVIOS POR ESTADO");
                contentStream.endText();

                yPosition -= 25;

                for (EstadoEnvio estado : EstadoEnvio.values()) {
                    long count = repositorio.getEnvios().values().stream()
                            .filter(e -> e.getEstado() == estado).count();

                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 11);
                    contentStream.newLineAtOffset(margin + 10, yPosition);
                    contentStream.showText(estado.name() + ": " + count);
                    contentStream.endText();
                    yPosition -= 18;
                }

                // Footer
                yPosition = 30;
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 8);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Generado: " + LocalDateTime.now().format(FORMATO_FECHA));
                contentStream.endText();
            }

            document.save(archivo);
        }

        return archivo;
    }

    /**
     * Genera reporte Excel completo del sistema
     */

    // ========== MÉTODOS AUXILIARES ==========

    private Map<String, Object> obtenerEstadisticasGenerales() {
        return Map.of(
                "Total Usuarios", repositorio.getUsuarios().size(),
                "Total Repartidores", repositorio.getRepartidores().size(),
                "Total Envíos", repositorio.getEnvios().size(),
                "Ingresos Totales", calcularIngresosTotales()
        );
    }

    private double calcularIngresosTotales() {
        return repositorio.getPagos().values().stream()
                .filter(p -> p.getResultado() == ResultadoPago.APROBADO)
                .mapToDouble(Pago::getMonto)
                .sum();
    }
    
}