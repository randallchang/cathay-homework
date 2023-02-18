package com.cathay.homework.service;

import com.cathay.homework.model.coindesk.response.CoindeskApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class CoindeskServiceTest {

    @Autowired
    private CoindeskService coindeskService;

    //5. 測試呼叫coindesk API，並顯示其內容。
    @Test
    public void getCoindeskFromApi_shouldSuccess() throws JsonProcessingException {

        // Arrange
        // Act
        CoindeskApiResponse coindeskApiResponse =
            coindeskService.getCoindeskFromApi();

        // Assert
        Assertions.assertNotNull(coindeskApiResponse);

        log.info(new ObjectMapper().writeValueAsString(coindeskApiResponse));
    }
}
