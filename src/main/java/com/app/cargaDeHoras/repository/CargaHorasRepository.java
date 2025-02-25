package com.app.cargaDeHoras.repository;
import com.app.cargaDeHoras.entity.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
public interface CargaHorasRepository  extends JpaRepository<Registro, Long> {


    @Query("SELECT r FROM Registro r WHERE r.dia BETWEEN :startDate AND :endDate")
    List<Registro> findByDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    @Query("SELECT r FROM Registro r WHERE YEAR(r.dia) = :year AND MONTH(r.dia) = :month")
    List<Registro> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT r FROM Registro r WHERE YEAR(r.dia) = :year AND MONTH(r.dia) = :month AND DAY(r.dia) = :day")
    List<Registro> findByDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    @Query("SELECT SUM(r.horasTrabajadas) FROM Registro r WHERE r.dia BETWEEN :startDate AND :endDate")
    Double sumHoursByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
