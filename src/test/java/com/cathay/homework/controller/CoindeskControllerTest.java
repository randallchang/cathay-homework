package com.cathay.homework.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cathay.homework.model.coindesk.request.CoindeskRequest;
import com.cathay.homework.model.coindesk.response.CoindeskResponse;
import com.cathay.homework.service.CoindeskService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext
public class CoindeskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoindeskService coindeskService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private static final String[] DEFAULT_DATA_STRING = new String[] {
        "{\"id\":1,\"chartName\":\"Bitcoin\",\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"apiUpdateTime\":\"2023/02/18 05:01:00\",\"bpis\":[{\"currencyCode\":\"USD\",\"currencyName\":\"美元\",\"rate\":\"24,650.1320\",\"description\":\"United States Dollar\"},{\"currencyCode\":\"GBP\",\"currencyName\":\"英鎊\",\"rate\":\"20,597.4531\",\"description\":\"British Pound Sterling\"},{\"currencyCode\":\"EUR\",\"currencyName\":\"歐元\",\"rate\":\"24,012.8275\",\"description\":\"Euro\"}]}",
        "{\"id\":2,\"chartName\":\"USDT\",\"disclaimer\":\"\",\"apiUpdateTime\":\"2023/02/18 05:01:00\",\"bpis\":[{\"currencyCode\":\"USD\",\"currencyName\":\"美元\",\"rate\":\"1\",\"description\":\"UnitedStatesDollar\"},{\"currencyCode\":\"GBP\",\"currencyName\":\"英鎊\",\"rate\":\"0.83\",\"description\":\"BritishPoundSterling\"},{\"currencyCode\":\"EUR\",\"currencyName\":\"歐元\",\"rate\":\"0.93\",\"description\":\"Euro\"}]}"
    };

    //1. 測試呼叫查詢幣別對應表資料API，並顯示其內容。
    @Test
    public void findCoindeskList_shouldSuccess() throws Exception {

        // Arrange
        List<CoindeskResponse> expectCoindeskResponseList =
            Stream.of(DEFAULT_DATA_STRING)
                .map(src -> {
                    try {
                        return OBJECT_MAPPER.readValue(src, CoindeskResponse.class);
                    } catch (Exception ex) {
                        return new CoindeskResponse();
                    }
                }).collect(Collectors.toList());

        BDDMockito.given(
            coindeskService.findAll())
                .willReturn(expectCoindeskResponseList);

        // Act
        ResultActions resultActions =
            mockMvc.perform(
                MockMvcRequestBuilders.get("/coindesk/list"));

        // Assert
        MvcResult result =
            resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<CoindeskResponse> actualCoindeskResponseList =
            OBJECT_MAPPER.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<CoindeskResponse>>(){});

        Assertions.assertThat(actualCoindeskResponseList)
            .usingRecursiveComparison()
            .isEqualTo(expectCoindeskResponseList);
    }

    //2. 測試呼叫新增幣別對應表資料API。
    @Test
    public void createCoindesk_shouldSuccess() throws Exception {

        // Arrange
        String coindeskDataString = "{\"id\":3,\"chartName\":\"ETH\",\"disclaimer\":\"\",\"apiUpdateTime\":\"2023/02/18 05:01:00\",\"bpis\":[{\"currencyCode\":\"USD\",\"currencyName\":\"美元\",\"rate\":\"1,698.78\",\"description\":\"UnitedStatesDollar\"},{\"currencyCode\":\"GBP\",\"currencyName\":\"英鎊\",\"rate\":\"1,410.48\",\"description\":\"BritishPoundSterling\"},{\"currencyCode\":\"EUR\",\"currencyName\":\"歐元\",\"rate\":\"1,584.62\",\"description\":\"Euro\"}]}";

        CoindeskRequest coindeskRequest =
            OBJECT_MAPPER.readValue(
                coindeskDataString,
                CoindeskRequest.class);

        CoindeskResponse expectCoindeskResponse =
            OBJECT_MAPPER.readValue(
                coindeskDataString,
                CoindeskResponse.class);

        BDDMockito.given(
            coindeskService.createCoindesk(coindeskRequest))
                .willReturn(expectCoindeskResponse);

        // Act
        ResultActions resultActions =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/coindesk")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OBJECT_MAPPER.writeValueAsString(coindeskRequest)));

        MvcResult result =
            resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Assert
        CoindeskResponse actualCoindeskResponse =
            OBJECT_MAPPER.readValue(
                result.getResponse().getContentAsString(),
                CoindeskResponse.class);

        Assertions.assertThat(actualCoindeskResponse)
            .usingRecursiveComparison()
            .isEqualTo(expectCoindeskResponse);
    }

    //3. 測試呼叫更新幣別對應表資料API，並顯示其內容。
    @Test
    public void updateCoindeskById_shouldSuccess() throws Exception {

        // Arrange
        String coindeskDataString =  "{\"id\":2,\"chartName\":\"USDT\",\"disclaimer\":\"\",\"apiUpdateTime\":\"2023/02/18 10:01:00\",\"bpis\":[{\"currencyCode\":\"USD\",\"currencyName\":\"美元\",\"rate\":\"1\",\"description\":\"UnitedStatesDollar\"},{\"currencyCode\":\"GBP\",\"currencyName\":\"英鎊\",\"rate\":\"0.88\",\"description\":\"BritishPoundSterling\"},{\"currencyCode\":\"EUR\",\"currencyName\":\"歐元\",\"rate\":\"0.99\",\"description\":\"Euro\"}]}";

        Long id = 2L;
        CoindeskRequest coindeskRequest =
            OBJECT_MAPPER.readValue(
                coindeskDataString,
                CoindeskRequest.class);

        CoindeskResponse expectCoindeskResponse =
            OBJECT_MAPPER.readValue(
                coindeskDataString,
                CoindeskResponse.class);

        BDDMockito.given(
            coindeskService.updateCoindeskById(id, coindeskRequest))
                .willReturn(expectCoindeskResponse);

        // Act
        ResultActions resultActions =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .put("/coindesk/" + id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OBJECT_MAPPER.writeValueAsString(coindeskRequest)));

        MvcResult result =
            resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Assert
        CoindeskResponse actualCoindeskResponse =
            OBJECT_MAPPER.readValue(
                result.getResponse().getContentAsString(),
                CoindeskResponse.class);

        Assertions.assertThat(actualCoindeskResponse)
            .usingRecursiveComparison()
            .isEqualTo(expectCoindeskResponse);
    }

    //4. 測試呼叫刪除幣別對應表資料API。
    @Test
    public void deleteCoindeskById_shouldSuccess() throws Exception {

        // Arrange
        Long id = 2L;

        // Act
        ResultActions resultActions =
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/coindesk/" + id));

        // Assert
        resultActions
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
    }

    @Test
    public void deleteCoindeskById_shouldReturnError() throws Exception {

        // Arrange
        Long id = 3L;

        BDDMockito
            .doThrow(new RuntimeException("Coindesk not exist."))
            .when(coindeskService)
            .deleteCoindeskById(id);

        // Act
        ResultActions resultActions =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .delete("/coindesk/" + id));

        // Assert
        resultActions
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isInternalServerError())
            .andReturn();
    }

    //6. 測試呼叫資料轉換的API，並顯示其內容。
    @Test
    public void syncDataFromApi_shouldSuccess() throws Exception {

        // Arrange
        String coindeskDataString = "{\"id\":1,\"chartName\":\"Bitcoin\",\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"apiUpdateTime\":\"2023/02/18 05:01:00\",\"bpis\":[{\"currencyCode\":\"USD\",\"currencyName\":\"美元\",\"rate\":\"24,650.1320\",\"description\":\"United States Dollar\"},{\"currencyCode\":\"GBP\",\"currencyName\":\"英鎊\",\"rate\":\"20,597.4531\",\"description\":\"British Pound Sterling\"},{\"currencyCode\":\"EUR\",\"currencyName\":\"歐元\",\"rate\":\"24,012.8275\",\"description\":\"Euro\"}]}";

        CoindeskResponse expectCoindeskResponse =
            OBJECT_MAPPER.readValue(
                coindeskDataString,
                CoindeskResponse.class);

        BDDMockito.given(
            coindeskService.syncCoindeskFromApi())
                .willReturn(expectCoindeskResponse);

        // Act
        ResultActions resultActions =
            mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/coindesk/sync-data"));

        MvcResult result =
            resultActions
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // Assert
        CoindeskResponse actualCoindeskResponse =
            OBJECT_MAPPER.readValue(
                result.getResponse().getContentAsString(),
                CoindeskResponse.class);

        Assertions.assertThat(actualCoindeskResponse)
            .usingRecursiveComparison()
            .isEqualTo(expectCoindeskResponse);
    }
}
