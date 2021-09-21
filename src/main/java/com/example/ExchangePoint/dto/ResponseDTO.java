package com.example.ExchangePoint.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO {

    private Double currencyAmountOut;
    private Integer clientPhoneNumber;
    private Long confirmationCode;
}
