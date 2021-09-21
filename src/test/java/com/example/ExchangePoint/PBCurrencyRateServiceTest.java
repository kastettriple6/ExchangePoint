package com.example.ExchangePoint;

import com.example.ExchangePoint.service.PBCurrencyRateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
class PBCurrencyRateServiceTest {

    private static final String CURRENCY_KEY_USD = "USD";

    @Autowired
    private PBCurrencyRateService instance;

    @Test
    void shouldReturnCurrencies() {
        Map<String, String> allRates = instance.getAllRates();
        Assertions.assertFalse(allRates.isEmpty());
        Assertions.assertNotNull(allRates.get(CURRENCY_KEY_USD));
    }
}
