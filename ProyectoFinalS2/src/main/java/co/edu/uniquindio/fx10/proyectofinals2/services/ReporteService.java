package co.edu.uniquindio.fx10.proyectofinals2.services;


import co.edu.uniquindio.fx10.proyectofinals2.model.*;
import co.edu.uniquindio.fx10.proyectofinals2.reposytorie.Repositorio;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
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

    /**
     * Genera reporte Excel de envíos del usuario
     */
    public File generarReporteUsuarioExcel(String idUsuario, LocalDateTime fechaInicio,
                                           LocalDateTime fechaFin) throws IOException {
        Usuario usuario = repositorio.getUsuarios().get(idUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

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

        File archivo = File.createTempFile("reporte_usuario_", ".xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Mis Envíos");

            // Estilos
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Encabezados
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID Envío", "Origen", "Destino", "Estado",
                    "Fecha", "Costo", "Método Pago"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowNum = 1;
            for (Envio envio : envios) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(envio.getIdEnvio());
                row.createCell(1).setCellValue(envio.getOrigen().getMunicipio().name());
                row.createCell(2).setCellValue(envio.getDestino().getMunicipio().name());
                row.createCell(3).setCellValue(envio.getEstado().name());
                row.createCell(4).setCellValue(envio.getFechaCreacion().format(FORMATO_FECHA));

                if (envio.getPago() != null) {
                    row.createCell(5).setCellValue(envio.getPago().getMonto());
                    row.createCell(6).setCellValue(envio.getPago().getMetodoPago().getTipo().name());
                }
            }

            // Ajustar anchos
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream outputStream = new FileOutputStream(archivo)) {
                workbook.write(outputStream);
            }
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
    public File generarReporteAdminExcel() throws IOException {
        File archivo = File.createTempFile("reporte_admin_", ".xlsx");

        try (Workbook workbook = new XSSFWorkbook()) {
            // Hoja 1: Resumen
            Sheet resumenSheet = workbook.createSheet("Resumen");
            crearHojaResumen(workbook, resumenSheet);

            // Hoja 2: Envíos
            Sheet enviosSheet = workbook.createSheet("Todos los Envíos");
            crearHojaEnvios(workbook, enviosSheet);

            // Hoja 3: Usuarios
            Sheet usuariosSheet = workbook.createSheet("Usuarios");
            crearHojaUsuarios(workbook, usuariosSheet);

            try (FileOutputStream outputStream = new FileOutputStream(archivo)) {
                workbook.write(outputStream);
            }
        }

        return archivo;
    }

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

    private void crearHojaResumen(Workbook workbook, Sheet sheet) {
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Métrica");
        row.createCell(1).setCellValue("Valor");

        Map<String, Object> stats = obtenerEstadisticasGenerales();
        int rowNum = 1;
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            Row dataRow = sheet.createRow(rowNum++);
            dataRow.createCell(0).setCellValue(entry.getKey());
            dataRow.createCell(1).setCellValue(entry.getValue().toString());
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private void crearHojaEnvios(Workbook workbook, Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Usuario", "Repartidor", "Origen", "Destino",
                "Estado", "Fecha", "Costo"};

        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Envio envio : repositorio.getEnvios().values()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(envio.getIdEnvio());
            row.createCell(1).setCellValue(envio.getUsuario().getNombre());
            row.createCell(2).setCellValue(envio.getRepartidor() != null ?
                    envio.getRepartidor().getNombre() : "Sin asignar");
            row.createCell(3).setCellValue(envio.getOrigen().getMunicipio().name());
            row.createCell(4).setCellValue(envio.getDestino().getMunicipio().name());
            row.createCell(5).setCellValue(envio.getEstado().name());
            row.createCell(6).setCellValue(envio.getFechaCreacion().format(FORMATO_FECHA));

            if (envio.getPago() != null) {
                row.createCell(7).setCellValue(envio.getPago().getMonto());
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void crearHojaUsuarios(Workbook workbook, Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nombre", "Email", "Teléfono", "Total Envíos"};

        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Usuario usuario : repositorio.getUsuarios().values()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(usuario.getId());
            row.createCell(1).setCellValue(usuario.getNombre());
            row.createCell(2).setCellValue(usuario.getCorreo());
            row.createCell(3).setCellValue(usuario.getTelefono());
            row.createCell(4).setCellValue(usuario.getEnvios().size());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}