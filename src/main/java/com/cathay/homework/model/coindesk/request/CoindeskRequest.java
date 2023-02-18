package com.cathay.homework.model.coindesk.request;

import java.util.List;

import com.cathay.homework.model.coindesk.bpi.BpiData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoindeskRequest {

    private String chartName;
    private String disclaimer;
    private List<BpiData> bpis;
}
