package com.app.cargaDeHoras.controller;


import com.app.cargaDeHoras.entity.Registro;
import com.app.cargaDeHoras.service.CargaHorasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cargaHoras")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class CargaHorasController {

    private final CargaHorasService cargaHorasService;

    @PostMapping
    public ResponseEntity<Registro> createTimeEntry(@RequestBody Registro timeEntry) {
        Registro saved = cargaHorasService.save(timeEntry);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/day/{year}/{month}/{day}")
    public ResponseEntity<List<Registro>> getMonthEntries(
            @PathVariable int year,
            @PathVariable int month,
            @PathVariable int day) {
        List<Registro> entries = cargaHorasService.getRegistrosPorDia(year,month,day);
        return ResponseEntity.ok(entries);
    }


    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<List<Registro>> getMonthEntries(
            @PathVariable int year,
            @PathVariable int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        List<Registro> entries = cargaHorasService.getRegistrosDelMes(year,month);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/is-last-working-day")
    public ResponseEntity<Boolean> isLastWorkingDay() {
        return ResponseEntity.ok(cargaHorasService.isLastWorkingDay());
    }

    @GetMapping("/print")
    public ResponseEntity<byte[]> generateReport() {
        byte[] pdfContent = cargaHorasService.generateMonthlyReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(
                "filename",
                "reporte-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getYear() + ".pdf"
        );

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdfContent);
    }
}
