package com.example.ExchangePoint.service;

import com.example.ExchangePoint.dto.CreateRequestDTO;
import com.example.ExchangePoint.dto.ResponseDTO;
import com.example.ExchangePoint.entity.*;
import com.example.ExchangePoint.repositories.ExchangeRequestRepository;
import com.example.ExchangePoint.repositories.ReportRepository;
import com.example.ExchangePoint.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExchangePointService {

    private static final Logger log = LoggerFactory.getLogger(ExchangePointService.class);

    private final ExchangeRequestRepository exchangeRepository;

    private final SessionRepository sessionRepository;

    private final PBCurrencyRateService currencyRateService;

    private final ReportRepository reportRepository;

    public Session createSession(Long sessionId) {
        Session session = new Session();
        session.setId(sessionId);
        session.setSStatus(SessionStatus.STARTED);
        session.setDate(LocalDate.now());
        session.setRates(currencyRateService.getAllRates());
        sessionRepository.save(session);
        return session;
    }

    public void createRequestToExchange(CreateRequestDTO request) {
        Session session = sessionRepository.findByDate(LocalDate.now()).orElseThrow();
        ExchangeRequest requestEntity = new ExchangeRequest();
        requestEntity.setType(request.getType());
        requestEntity.setBaseCurrency(request.getFirstCurrency());
        requestEntity.setCurrencyToExchange((request.getSecondCurrency()));
        requestEntity.setCurrencyAmountIn(request.getCurrencyAmount());
        switch (request.getType()) {
            case SALE:
                requestEntity.setCurrencyAmountOut(request.getCurrencyAmount() * Double.parseDouble(session.getRates().get(String.valueOf(request.getFirstCurrency()))));
                break;
            case BUY:
                requestEntity.setCurrencyAmountOut(request.getCurrencyAmount() / Double.parseDouble(session.getRates().get(String.valueOf(request.getSecondCurrency()))));
                break;
        }
        requestEntity.setRequestTime(LocalDate.now());
        requestEntity.setClientPhoneNumber(request.getClientPhoneNumber());
        requestEntity.setStatus(RequestStatus.NEW);
        requestEntity.setConfirmationCode((long) (Math.random() * (9999L - 1000L + 1L) + 1000L));
        requestEntity.setSession(session);
        exchangeRepository.save(requestEntity);
    }

    public ResponseDTO viewRequestCalcAndConfirmCode(Integer clientPhoneNumber) {
        ResponseDTO response = new ResponseDTO();
        ExchangeRequest request = exchangeRepository.findById(clientPhoneNumber).orElseThrow(IllegalArgumentException::new);
        response.setCurrencyAmountOut(request.getCurrencyAmountOut());
        response.setClientPhoneNumber(request.getClientPhoneNumber());
        response.setConfirmationCode(request.getConfirmationCode());
        return response;
    }

    public void confirmRequest(Integer clientPhoneNumber, Long confirmationCode) {
        ExchangeRequest request = exchangeRepository.findById(clientPhoneNumber).orElseThrow(IllegalArgumentException::new);
        if (request.getConfirmationCode().equals(confirmationCode)) {
            request.setStatus(RequestStatus.COMPLETED);
            log.info("request confirmed");
        } else {
            request.setStatus(RequestStatus.CANCELED);
            log.error("confirmation code is incorrect, request canceled");
        }
        exchangeRepository.save(request);
    }

    public boolean deleteRequest(Integer clientPhoneNumber) {
        ExchangeRequest requestEntity = exchangeRepository.findById(clientPhoneNumber).orElseThrow(IllegalArgumentException::new);
        boolean result = false;
        if (requestEntity.getStatus().equals(RequestStatus.NEW)) {
                exchangeRepository.deleteById(clientPhoneNumber);
                result = true;
        } else {
            log.info("can not delete successful request");
        }
        return result;
    }

    public Report sendSalesReportAndCloseSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(IllegalArgumentException::new);
        session.setSStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
        Report report = new Report();
        exchangeRepository.deleteByStatus(RequestStatus.CANCELED);
        exchangeRepository.deleteByStatus(RequestStatus.NEW);
        report.setCountExchanges(exchangeRepository.findByRequestTime(session.getDate()).size());
        Map<String, String> salesReport = new HashMap<String, String>();
        Currency[] curr = Currency.values();
        OperationType[] type = OperationType.values();
        for (OperationType operationType : type) {
            for (Currency currency : curr) {
                List<ExchangeRequest> singleResult = exchangeRepository.findByTypeAndCurrencyToExchange(operationType, currency);
                List<ExchangeRequest> requestsPerSession = singleResult.stream().filter(exchangeRequest -> Objects.equals(exchangeRequest.getRequestTime(), session.getDate())).collect(Collectors.toList());
                String amounts = String.valueOf(requestsPerSession.stream().map(ExchangeRequest::getCurrencyAmountIn).reduce(0.0, Double::sum));
                String operationAndCcy = String.valueOf(currency).concat("_").concat(String.valueOf(operationType));
                salesReport.put(operationAndCcy, amounts);
            }
        }
        report.setId(sessionId);
        report.setSalesReport(salesReport);
        report.setDate(session.getDate());
        reportRepository.save(report);
        return report;
    }

    public List<Report> getReportsByDateRange(LocalDate first, LocalDate last) {
        return reportRepository.findByDateBetween(first, last);
    }
}
