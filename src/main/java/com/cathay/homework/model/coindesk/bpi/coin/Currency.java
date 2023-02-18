package com.cathay.homework.model.coindesk.bpi.coin;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Currency {

    private String code;

    private String symbol;

    private String rate;

    private String description;

    @JsonProperty("rate_float")
    private BigDecimal rateFloat;
}
