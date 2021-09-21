package com.example.ExchangePoint.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@Entity
public class Report {

    @Id
    private Long id;
    @Column(name = "count")
    private Integer countExchanges;
    @Column(name = "sales")
    @ElementCollection(targetClass = String.class)
    private Map<String, String> salesReport;
    @Column(name = "date")
    private LocalDate date;

    @OneToOne(mappedBy = "report")
    private Session session;

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", countExchanges=" + countExchanges +
                ", salesReport=" + salesReport +
                ", date=" + date +
                ", session=" + session +
                '}';
    }
}
