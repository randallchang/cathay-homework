package com.cathay.homework.model.coindesk.response;

import java.util.Map;

import com.cathay.homework.model.coindesk.bpi.coin.Currency;
import com.cathay.homework.model.coindesk.coindesk_time.CoindeskTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoindeskApiResponse {

    private CoindeskTime time;
    private String disclaimer;
    private String chartName;
    private Map<String, Currency> bpi;
}
