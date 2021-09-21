package com.example.ExchangePoint.service;

import com.example.ExchangePoint.dto.CreateRequestDTO;
import com.example.ExchangePoint.entity.*;
import com.example.ExchangePoint.repositories.ExchangeRequestRepository;
import com.example.ExchangePoint.repositories.ReportRepository;
import com.example.ExchangePoint.repositories.SessionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.verify;

class ExchangePointServiceTest {

    private static final Integer CLIENT_PHONE_NUMBER = 9379992;
    private static final Long CONFIRMATION_CODE = 1345L;
    private static final Long SESSION_ID = 1L;

    private ExchangePointService instance;

    private ExchangeRequestRepository exchangeRepository;

    private SessionRepository sessionRepository;

    private PBCurrencyRateService currencyRateService;

    private ReportRepository reportRepository;

    @BeforeEach
    void init() {
        exchangeRepository = Mockito.mock(ExchangeRequestRepository.class);
        sessionRepository = Mockito.mock(SessionRepository.class);
        currencyRateService = Mockito.mock(PBCurrencyRateService.class);
        reportRepository = Mockito.mock(ReportRepository.class);
        instance = new ExchangePointService(exchangeRepository, sessionRepository, currencyRateService, reportRepository);
    }

    @Test
    void shouldStartSessionAndGetRates() {
        Session session = instance.createSession(SESSION_ID);

        Mockito.when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(session));

        Assertions.assertEquals(SessionStatus.STARTED, session.getSStatus());
        Assertions.assertNotNull(session.getRates());
        verify(sessionRepository).save(session);
    }


    @Test
    void shouldConfirmRequest() {
        ExchangeRequest request = new ExchangeRequest();
        request.setConfirmationCode(CONFIRMATION_CODE);
        Mockito.when(exchangeRepository.findById(CLIENT_PHONE_NUMBER)).thenReturn(Optional.of(request));

        instance.confirmRequest(CLIENT_PHONE_NUMBER, CONFIRMATION_CODE);

        Assertions.assertEquals(RequestStatus.COMPLETED, request.getStatus());
        verify(exchangeRepository).save(request);
    }

    @Test
    void shouldCalcAmountOutAndReturnConfCode() {
        ExchangeRequest request = new ExchangeRequest();
        CreateRequestDTO createRequestDTO = new CreateRequestDTO();
        Session session = sessionRepository.findById(SESSION_ID).orElseThrow(IllegalArgumentException::new);
        createRequestDTO.setClientPhoneNumber(CLIENT_PHONE_NUMBER);
        createRequestDTO.setType(OperationType.BUY);
        createRequestDTO.setSecondCurrency(Currency.USD);
        createRequestDTO.setCurrencyAmount(1488.0);
        Mockito.when(exchangeRepository.findById(CLIENT_PHONE_NUMBER)).thenReturn(Optional.of(request));

        instance.createRequestToExchange(createRequestDTO);

        Assertions.assertNotNull(request.getCurrencyAmountOut());
        Assertions.assertNotNull(request.getConfirmationCode());
    }

    @Test
    void shouldCancelRequest() {
        ExchangeRequest request = new ExchangeRequest();
        request.setConfirmationCode(1000L);
        Mockito.when(exchangeRepository.findById(CLIENT_PHONE_NUMBER)).thenReturn(Optional.of(request));

        instance.confirmRequest(CLIENT_PHONE_NUMBER, CONFIRMATION_CODE);

        Assertions.assertEquals(RequestStatus.CANCELED, request.getStatus());
        verify(exchangeRepository).save(request);


    }

    @Test
    void shouldDeleteRequest() {
        ExchangeRequest request = new ExchangeRequest();
        request.setStatus(RequestStatus.NEW);
        Mockito.when(exchangeRepository.findById(CLIENT_PHONE_NUMBER)).thenReturn(Optional.of(request));

        boolean result = instance.deleteRequest(CLIENT_PHONE_NUMBER);

        Mockito.verify(exchangeRepository)
                .delete(request);
        Assertions.assertTrue(result);
    }

    @Test
    void shouldReturnReport() {
        Session session = new Session();
        Mockito.when(sessionRepository.findById(SESSION_ID)).thenReturn(Optional.of(session));

        Report report = instance.sendSalesReportAndCloseSession(session.getId());

        Assertions.assertNotNull(report);
        verify(reportRepository).findById(SESSION_ID);
    }
}