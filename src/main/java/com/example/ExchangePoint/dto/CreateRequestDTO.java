package com.example.ExchangePoint.dto;

import com.example.ExchangePoint.entity.Currency;
import com.example.ExchangePoint.entity.OperationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRequestDTO {

    private OperationType type;
    private Currency firstCurrency;
    private Currency secondCurrency;
    private Double currencyAmount;
    private Integer clientPhoneNumber;
}
