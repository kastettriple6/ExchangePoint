package com.example.ExchangePoint.repositories;

import com.example.ExchangePoint.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByDateBetween(LocalDate first, LocalDate last);
}
