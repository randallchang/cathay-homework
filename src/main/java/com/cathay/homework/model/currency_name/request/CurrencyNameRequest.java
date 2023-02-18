package com.cathay.homework.model.currency_name.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyNameRequest {

    private String currencyCode;
    private String name;
    private String language;
}
