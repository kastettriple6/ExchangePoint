package com.example.ExchangePoint.controller;

import com.example.ExchangePoint.dto.CreateRequestDTO;
import com.example.ExchangePoint.dto.ResponseDTO;
import com.example.ExchangePoint.entity.Report;
import com.example.ExchangePoint.entity.Session;
import com.example.ExchangePoint.service.ExchangePointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/exchange_point")
@Api
public class ExchangePointController {

    @Autowired
    private ExchangePointService service;

    @PostMapping("/login{sessionId}")
    @ResponseBody
    @ApiOperation(value = "Make log-in, then create new session")
    public Session startSession(@PathVariable Long sessionId) {
        return service.createSession(sessionId);
    }

    @PostMapping("/create_request")
    @ApiOperation(value = "Operation to create request to exchange currency, after get client info")
    public void exchangeCurrency(@RequestBody CreateRequestDTO request) {
        service.createRequestToExchange(request);
    }

    @GetMapping("/response{clientPhoneNumber}")
    @ResponseBody
    @ApiOperation(value = "Return calculation of result exchange operation and confirmation code")
    public ResponseDTO showResponse(@PathVariable Integer clientPhoneNumber) {
        return service.viewRequestCalcAndConfirmCode(clientPhoneNumber);
    }

    @PutMapping("/confirmation{clientPhoneNumber}")
    @ApiOperation(value = "Confirm operation if code is valid")
    public void confirmExchangeRequest(@PathVariable Integer clientPhoneNumber, Long confirmationCode) {
        service.confirmRequest(clientPhoneNumber, confirmationCode);
    }

    @DeleteMapping("/delete_request{clientPhoneNumber}")
    @ApiOperation(value = "Delete unconfirmed request if needed")
    public boolean deleteUnconfirmedRequest(@PathVariable Integer clientPhoneNumber) {
        return service.deleteRequest(clientPhoneNumber);
    }

    @GetMapping("/report{sessionId}")
    @ResponseBody
    @ApiOperation(value = "Make session ended, then return report for session")
    @Transactional
    public Report getReportForDayAndCloseSession(@PathVariable Long sessionId) {
        return service.sendSalesReportAndCloseSession(sessionId);
    }

    @GetMapping("/reports")
    @ApiOperation(value = "Return reports for period between two date, that gets from request")
    public List<Report> getReportBetweenDates(LocalDate first, LocalDate last) {
        return service.getReportsByDateRange(first, last);
    }


}
