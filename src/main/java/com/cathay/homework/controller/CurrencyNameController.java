package com.cathay.homework.controller;

import java.util.List;

import com.cathay.homework.model.currency_name.request.CurrencyNameRequest;
import com.cathay.homework.model.currency_name.response.CurrencyNameResponse;
import com.cathay.homework.service.CurrencyNameService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency-name")
@RequiredArgsConstructor
public class CurrencyNameController {

    private final CurrencyNameService currencyNameService;

    @GetMapping("/list")
    public ResponseEntity<List<CurrencyNameResponse>> findCurrencyNameList() {
        return ResponseEntity.ok(currencyNameService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyNameResponse> findCurrencyNameById(
            @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(currencyNameService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CurrencyNameResponse> createCurrencyName(
            @RequestBody CurrencyNameRequest currencyNameRequest) {
        return ResponseEntity.ok(
            currencyNameService.createCurrencyName(currencyNameRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyNameResponse> updateCurrencyNameById(
            @PathVariable(name = "id") Long id,
            @RequestBody CurrencyNameRequest currencyNameRequest) {
        return ResponseEntity.ok(
            currencyNameService.updateCurrencyNameById(id, currencyNameRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrencyNameById(
            @PathVariable(name = "id") Long id) {

        currencyNameService.deleteCurrencyNameById(id);

        return ResponseEntity.ok().build();
    }
}
