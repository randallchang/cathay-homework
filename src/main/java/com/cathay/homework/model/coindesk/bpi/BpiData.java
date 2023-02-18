package com.cathay.homework.model.coindesk.bpi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BpiData {

    private String currencyCode;
    private String currencyName;
    private String rate;
    private String description;
}
