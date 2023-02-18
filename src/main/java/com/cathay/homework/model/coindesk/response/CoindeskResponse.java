package com.cathay.homework.model.coindesk.response;

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
public class CoindeskResponse {

    private Long id;
    private String chartName;
    private String disclaimer;
    private String apiUpdateTime;
    private List<BpiData> bpis;
}
