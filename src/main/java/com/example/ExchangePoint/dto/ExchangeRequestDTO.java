package com.example.ExchangePoint.dto;

import com.example.ExchangePoint.entity.Currency;
import com.example.ExchangePoint.entity.OperationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeRequestDTO {

    private OperationType type;
    private Currency currencyToExchange;
    private Double currencyAmountIn;
}
