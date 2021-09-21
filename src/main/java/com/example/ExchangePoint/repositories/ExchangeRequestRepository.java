package com.example.ExchangePoint.repositories;

import com.example.ExchangePoint.entity.Currency;
import com.example.ExchangePoint.entity.ExchangeRequest;
import com.example.ExchangePoint.entity.OperationType;
import com.example.ExchangePoint.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Integer> {

    List<ExchangeRequest> findByTypeAndCurrencyToExchange(OperationType type, Currency currencyToExchange);

    List<ExchangeRequest> findByRequestTime(LocalDate requestTime);

    void deleteByStatus(RequestStatus status);
}
