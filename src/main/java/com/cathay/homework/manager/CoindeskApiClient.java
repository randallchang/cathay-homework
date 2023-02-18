package com.cathay.homework.manager;

import com.cathay.homework.constant.Constants;
import com.cathay.homework.model.coindesk.response.CoindeskApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoindeskApiClient {

    private final RestTemplate restTemplate;

    public CoindeskApiResponse getCoindesk() {

        try {
            return new ObjectMapper().readValue(
                restTemplate.exchange(
                    Constants.COINDESK_API_URL,
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    String.class)
                        .getBody(),
                CoindeskApiResponse.class);
        } catch(Exception ex) {

            log.error("Parse coindesk response error.", ex);

            return null;
        }
    }
}
