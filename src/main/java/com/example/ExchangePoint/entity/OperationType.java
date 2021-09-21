package com.example.ExchangePoint.entity;

public enum OperationType {
    BUY ("buy"),
    SALE ("sale");

    public String label;

    OperationType(String label){
        this.label = label;
    }
}
