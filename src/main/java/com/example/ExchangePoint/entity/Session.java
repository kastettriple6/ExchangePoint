package com.example.ExchangePoint.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
public class Session {

    @Id
    private Long id;
    @Column(name = "status")
    private SessionStatus sStatus;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "rates")
    @ElementCollection(targetClass = String.class)
    private Map<String, String> rates;



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "session")
    private List<ExchangeRequest> exchangeRequests;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    private Report report;

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", sStatus=" + sStatus +
                ", date=" + date +
                ", rates=" + rates +
                ", exchangeRequests=" + exchangeRequests +
                ", report=" + report +
                '}';
    }
}
