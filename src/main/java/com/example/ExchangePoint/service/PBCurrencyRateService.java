package com.example.ExchangePoint.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PBCurrencyRateService {

    private static final Logger log = LoggerFactory.getLogger(PBCurrencyRateService.class);

    private static final String CURRENCY_KEY = "ccy";
    private static final String BUY_KEY = "buy";

    @Value("${pb.api.url}")
    private String apiUrl;

    @Autowired
    private ObjectMapper mapper;

    public Map<String, String> getAllRates() {
        try {
            List<Map<String, String>> response = mapper.readValue(new URL(apiUrl), new TypeReference<>() {});
            return response.stream()
                    .collect(Collectors.toMap(
                            res -> res.get(CURRENCY_KEY),
                            res -> res.get(BUY_KEY)
                    ));
        } catch (IOException e) {
            log.error("unable to get rates", e);
            throw new IllegalStateException(e);
        }
    }
}
