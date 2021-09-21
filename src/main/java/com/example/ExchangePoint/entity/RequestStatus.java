package com.example.ExchangePoint.entity;

public enum RequestStatus {

    NEW("New"),
    COMPLETED("Completed"),
    CANCELED("Canceled");

    public String status;

    RequestStatus(String status){
        this.status = status;
    }
}
