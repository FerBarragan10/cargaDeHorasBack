package com.app.cargaDeHoras.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "registro")
public class Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dia;

    @Column(name = "horas_trabajadas", nullable = false)
    private Double horasTrabajadas;

    @Column(name = "nombre_tarea", nullable = false)
    private String nombreTarea;

    @Column(nullable = false)
    private String descripcion;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }
}
