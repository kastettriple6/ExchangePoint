package com.example.ExchangePoint.repositories;

import com.example.ExchangePoint.entity.Report;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ReportRepositoryDataJpaTest {

    @Autowired
    private ReportRepository instance;

    @Test
    void shouldFindByDates() {
        Report report1 = new Report();
        report1.setId(1L);
        report1.setDate(LocalDate.of(2021, 9, 20));
        instance.save(report1);

        Report report2 = new Report();
        report2.setId(2L);
        report2.setDate(LocalDate.of(2021, 9, 19));
        instance.save(report2);

        Report report3 = new Report();
        report3.setId(3L);
        report3.setDate(LocalDate.of(2021, 9, 18));
        instance.save(report3);

        List<Report> reportBetweenDates = instance.findByDateBetween(
                LocalDate.of(2021, 9, 18),
                LocalDate.of(2021, 9, 20));

        Assertions.assertFalse(reportBetweenDates.isEmpty());
    }
}
