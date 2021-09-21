package com.example.ExchangePoint.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class ExchangeRequest {

    @Id
    @Column(name = "client_identifier")
    private Integer clientPhoneNumber;
    @Column(name = "operation")
    private OperationType type;
    @Column(name = "ccy_first")
    private Currency baseCurrency;
    @Column(name = "ccy_second")
    private Currency currencyToExchange;
    @Column(name = "ccy_amount")
    private Double currencyAmountIn;
    @Column(name = "ccy_amount_out")
    private Double currencyAmountOut;
    @Column(name = "time")
    private LocalDate requestTime;
    @Column(name = "status")
    private RequestStatus status;
    private Long confirmationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @Override
    public String toString() {
        return "ExchangeRequest{" +
                "clientPhoneNumber=" + clientPhoneNumber +
                ", type=" + type +
                ", baseCurrency=" + baseCurrency +
                ", currencyToExchange=" + currencyToExchange +
                ", currencyAmountIn=" + currencyAmountIn +
                ", currencyAmountOut=" + currencyAmountOut +
                ", requestTime=" + requestTime +
                ", status=" + status +
                ", confirmationCode=" + confirmationCode +
                ", session=" + session +
                '}';
    }
}
