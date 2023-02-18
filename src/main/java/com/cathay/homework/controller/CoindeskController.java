package com.cathay.homework.controller;

import java.util.List;

import com.cathay.homework.model.coindesk.request.CoindeskRequest;
import com.cathay.homework.model.coindesk.response.CoindeskApiResponse;
import com.cathay.homework.model.coindesk.response.CoindeskResponse;
import com.cathay.homework.service.CoindeskService;

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
@RequestMapping("/coindesk")
@RequiredArgsConstructor
public class CoindeskController {

    private final CoindeskService coindeskService;

    @GetMapping("/list")
    public ResponseEntity<List<CoindeskResponse>> findCoindeskList() {
        return ResponseEntity.ok(coindeskService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoindeskResponse> findCoindeskById(
            @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(coindeskService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CoindeskResponse> createCoindesk(
            @RequestBody CoindeskRequest coindeskRequest) {
        return ResponseEntity.ok(
            coindeskService.createCoindesk(coindeskRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoindeskResponse> updateCoindeskById(
            @PathVariable(name = "id") Long id,
            @RequestBody CoindeskRequest coindeskRequest) {
        return ResponseEntity.ok(
            coindeskService.updateCoindeskById(id, coindeskRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoindeskById(
            @PathVariable(name = "id") Long id) {

        coindeskService.deleteCoindeskById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/raw-data")
    public ResponseEntity<CoindeskApiResponse> getDataFromApi() {
        return ResponseEntity.ok(coindeskService.getCoindeskFromApi());
    }

    @PostMapping("/sync-data")
    public ResponseEntity<CoindeskResponse> syncDataFromApi() {
        return ResponseEntity.ok(coindeskService.syncCoindeskFromApi());
    }
}
