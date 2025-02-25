package com.app.cargaDeHoras.service;
import com.app.cargaDeHoras.entity.Registro;
import com.app.cargaDeHoras.repository.CargaHorasRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CargaHorasService {

    private final CargaHorasRepository cargaHorasRepository;

    public Registro save(Registro timeEntry) {
        return cargaHorasRepository.save(timeEntry);
    }

    public List<Registro> getEntriesForMonth(LocalDate date) {
        LocalDate startOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        return cargaHorasRepository.findByDateBetween(startOfMonth, endOfMonth);
    }
    public List<Registro> getRegistrosDelMes(int year, int month) {
        return cargaHorasRepository.findByYearAndMonth(year, month);
    }

    public List<Registro> getRegistrosPorDia(int year, int month,int day) {
        return cargaHorasRepository.findByDay(year, month,day);
    }

    public boolean isLastWorkingDay() {
        LocalDate today = LocalDate.now();
        LocalDate lastDayOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        // Si es el último día del mes
        if (today.equals(lastDayOfMonth)) {
            return !isWeekend(today);
        }

        // Si el último día es fin de semana, buscar el último día hábil
        while (isWeekend(lastDayOfMonth)) {
            lastDayOfMonth = lastDayOfMonth.minusDays(1);
        }

        return today.equals(lastDayOfMonth);
    }

    private boolean isWeekend(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case SATURDAY:
            case SUNDAY:
                return true;
            default:
                return false;
        }
    }

    public byte[] generateMonthlyReport() {
        try {
            LocalDate now = LocalDate.now();
            List<Registro> entries = getEntriesForMonth(now);

            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            document.open();

            // Título
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Reporte Mensual de Horas", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Tabla
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            // Encabezados
            Stream.of("Fecha", "Horas", "Tarea", "Descripción")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });

            // Datos
            entries.forEach(entry -> {
                table.addCell(entry.getDia().toString());
                table.addCell(entry.getHorasTrabajadas().toString());
                table.addCell(entry.getNombreTarea());
                table.addCell(entry.getDescripcion());
            });

            document.add(table);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando el PDF", e);
        }
    }
}
