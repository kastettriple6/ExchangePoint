package com.example.ExchangePoint.repositories;

import com.example.ExchangePoint.entity.Currency;
import com.example.ExchangePoint.entity.ExchangeRequest;
import com.example.ExchangePoint.entity.OperationType;
import com.example.ExchangePoint.entity.RequestStatus;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ExchangeRepositoryDataJpaTest {

    @Autowired
    private ExchangeRequestRepository instance;

    @Test
    void shouldReturnListByCurrAndType() {
        ExchangeRequest request1 = new ExchangeRequest();
        request1.setClientPhoneNumber(9379994);
        request1.setType(OperationType.BUY);
        request1.setCurrencyToExchange(Currency.USD);
        instance.save(request1);

        ExchangeRequest request2 = new ExchangeRequest();
        request2.setClientPhoneNumber(9379996);
        request2.setType(OperationType.BUY);
        request2.setCurrencyToExchange(Currency.USD);
        instance.save(request2);
        List<ExchangeRequest> requests = instance.findByTypeAndCurrencyToExchange(OperationType.BUY, Currency.USD);

        Assertions.assertNotNull(requests);
    }

    @Test
    void shouldReturnListByDate() {
        ExchangeRequest request1 = new ExchangeRequest();
        request1.setClientPhoneNumber(9379998);
        request1.setRequestTime(LocalDate.of(2021,9,21));
        instance.save(request1);

        ExchangeRequest request2 = new ExchangeRequest();
        request2.setClientPhoneNumber(9379999);
        request2.setRequestTime(LocalDate.now());
        instance.save(request2);

        List<ExchangeRequest> requests = instance.findByRequestTime(LocalDate.now());

        Assertions.assertNotNull(requests);
    }

    @Test
    void shouldDeleteByStatus() {
        ExchangeRequest request = new ExchangeRequest();
        request.setClientPhoneNumber(9379995);
        request.setStatus(RequestStatus.NEW);
        instance.save(request);

        instance.deleteByStatus(RequestStatus.NEW);

        Assertions.assertTrue(instance.findAll().isEmpty());
    }
}
