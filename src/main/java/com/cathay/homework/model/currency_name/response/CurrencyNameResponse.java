package com.cathay.homework.model.currency_name.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyNameResponse {

    private Long id;
    private LocalDateTime createdTime;
    private LocalDateTime uptimedTime;
    private String currencyCode;
    private String name;
    private String language;

}
