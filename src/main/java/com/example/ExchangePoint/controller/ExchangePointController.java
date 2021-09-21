package com.example.ExchangePoint.controller;

import com.example.ExchangePoint.dto.CreateRequestDTO;
import com.example.ExchangePoint.dto.ResponseDTO;
import com.example.ExchangePoint.entity.Report;
import com.example.ExchangePoint.entity.Session;
import com.example.ExchangePoint.service.ExchangePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/exchange_point")
public class ExchangePointController {

    @Autowired
    private ExchangePointService service;

    @PostMapping("/login{sessionId}")
    @ResponseBody
    public Session startSession(@PathVariable Long sessionId) {
        return service.createSession(sessionId);
    }

    @PostMapping("/create_request")
    public void exchangeCurrency(@RequestBody CreateRequestDTO request) {
        service.createRequestToExchange(request);
    }

    @GetMapping("/response{clientPhoneNumber}")
    @ResponseBody
    public ResponseDTO showResponse(@PathVariable Integer clientPhoneNumber) {
        return service.viewRequestCalcAndConfirmCode(clientPhoneNumber);
    }

    @PutMapping("/confirmation{clientPhoneNumber}")
    public void confirmExchangeRequest(@PathVariable Integer clientPhoneNumber, Long confirmationCode) {
        service.confirmRequest(clientPhoneNumber, confirmationCode);
    }

    @DeleteMapping("/delete_request{clientPhoneNumber}")
    public boolean deleteUnconfirmedRequest(@PathVariable Integer clientPhoneNumber) {
        return service.deleteRequest(clientPhoneNumber);
    }

    @GetMapping("/report{sessionId}")
    @ResponseBody
    public Report getReportForDayAndCloseSession(@PathVariable Long sessionId) {
        return service.sendSalesReportAndCloseSession(sessionId);
    }

    @GetMapping("/reports")
    public List<Report> getReportBetweenDates(LocalDate first, LocalDate last) {
        return service.getReportsByDateRange(first, last);
    }


}
