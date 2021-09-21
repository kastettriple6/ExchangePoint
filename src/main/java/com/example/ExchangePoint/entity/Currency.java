package com.example.ExchangePoint.entity;

public enum Currency {

    USD("USD"),
    EUR("EUR"),
    UAH("UAH");

    public String currency;
    Currency(String currency) {
        this.currency = currency;
    }
}
