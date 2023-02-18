package com.cathay.homework.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cathay.homework.constant.Constants;
import com.cathay.homework.entity.Bpi;
import com.cathay.homework.entity.Coindesk;
import com.cathay.homework.entity.CurrencyName;
import com.cathay.homework.manager.CoindeskApiClient;
import com.cathay.homework.model.coindesk.bpi.BpiData;
import com.cathay.homework.model.coindesk.request.CoindeskRequest;
import com.cathay.homework.model.coindesk.response.CoindeskApiResponse;
import com.cathay.homework.model.coindesk.response.CoindeskResponse;
import com.cathay.homework.repository.BpiRepository;
import com.cathay.homework.repository.CoindeskRepository;
import com.cathay.homework.repository.CurrencyNameRepository;
import com.cathay.homework.util.ConvertUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class CoindeskService {

    private final CoindeskApiClient coindeskApiClient;
    private final CoindeskRepository coindeskRepository;
    private final BpiRepository bpiRepository;
    private final CurrencyNameRepository currencyNameRepository;

    private static final DateTimeFormatter SOURCE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    private static final DateTimeFormatter SYSTEM_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public CoindeskApiResponse getCoindeskFromApi() {
        return coindeskApiClient.getCoindesk();
    }

    @Transactional
    public CoindeskResponse syncCoindeskFromApi() {

        CoindeskApiResponse coindeskResponse =
            coindeskApiClient.getCoindesk();
        if (coindeskResponse == null) {
            return null;
        }

        String apiUpdateTime =
            LocalDateTime.parse(
                    coindeskResponse.getTime().getUpdatedISO(),
                    SOURCE_FORMATTER)
                .format(SYSTEM_FORMATTER);

        purgeCoindeskData(coindeskResponse.getChartName());

        Coindesk coindesk =
            Coindesk.builder()
                .chartName(coindeskResponse.getChartName())
                .disclaimer(coindeskResponse.getDisclaimer())
                .apiUpdateTime(apiUpdateTime)
                .build();

        coindeskRepository.save(coindesk);

        Long coindeskId = coindesk.getId();

        List<Bpi> bpiList =
            coindeskResponse.getBpi()
                .entrySet()
                .stream()
                .map(
                    entry ->
                        Bpi.builder()
                            .coindeskId(coindeskId)
                            .currencyCode(entry.getValue().getCode())
                            .rate(entry.getValue().getRate())
                            .description(entry.getValue().getDescription())
                            .build())
                .collect(Collectors.toList());

        bpiRepository.saveAll(bpiList);

        return CoindeskResponse.builder()
                .id(coindesk.getId())
                .chartName(coindesk.getChartName())
                .disclaimer(coindesk.getDisclaimer())
                .apiUpdateTime(coindesk.getApiUpdateTime())
                .bpis(
                    bpiList.stream()
                        .map(entity ->
                            BpiData.builder()
                                .currencyCode(entity.getCurrencyCode())
                                .currencyName(
                                    currencyNameRepository.findByCurrencyCodeAndLanguage(
                                            entity.getCurrencyCode(),
                                            Constants.LANGUAGE_ZH_TW)
                                        .orElse(new CurrencyName())
                                        .getName())
                                .rate(entity.getRate())
                                .description(entity.getDescription())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public List<CoindeskResponse> findAll() {

        List<Coindesk> coindeskList = coindeskRepository.findAll();

        Map<Long, List<Bpi>> bpiListByCoindeskId =
            bpiRepository.findByCoindeskIdIn(
                coindeskList
                    .stream()
                    .map(Coindesk::getId)
                    .collect(Collectors.toList()))
                        .stream()
                        .collect(Collectors.groupingBy(Bpi::getCoindeskId));

        List<CoindeskResponse> coindeskResponseList =
            ConvertUtil.convertList(coindeskList, CoindeskResponse.class);

        coindeskResponseList.forEach(coindeskResponse -> {
            List<Bpi> bpiList =
                bpiListByCoindeskId.get(coindeskResponse.getId());
            if (!CollectionUtils.isEmpty(bpiList)) {
                prepareBpiLiist(bpiList, coindeskResponse);
            }
        });

        return coindeskResponseList;
    }

    public CoindeskResponse findById(Long id) {

        Coindesk coindesk =
            coindeskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coindesk not exist."));

        CoindeskResponse coindeskResponse =
            ConvertUtil.convert(coindesk, CoindeskResponse.class);

        prepareBpiLiist(
            bpiRepository.findByCoindeskId(coindesk.getId()),
            coindeskResponse);

        return coindeskResponse;
    }

    private void prepareBpiLiist(List<Bpi> bpiList, CoindeskResponse coindeskResponse) {

        List<BpiData> bpiDataList =
            ConvertUtil.convertList(bpiList, BpiData.class);
        bpiDataList.forEach(
            bpiData -> bpiData.setCurrencyName(
                currencyNameRepository.findByCurrencyCodeAndLanguage(
                        bpiData.getCurrencyCode(),
                        Constants.LANGUAGE_ZH_TW)
                    .orElse(new CurrencyName())
                    .getName()));

        coindeskResponse.setBpis(bpiDataList);
    }

    @Transactional
    public CoindeskResponse createCoindesk(CoindeskRequest coindeskRequest) {

        Coindesk coindesk =
            Coindesk.builder()
                .chartName(coindeskRequest.getChartName())
                .disclaimer(coindeskRequest.getDisclaimer())
                .build();

        coindeskRepository.save(coindesk);

        Long coindeskId = coindesk.getId();
        List<Bpi> bpiList =
            coindeskRequest.getBpis()
                .stream()
                .map(
                    bpiData ->
                        Bpi.builder()
                            .coindeskId(coindeskId)
                            .currencyCode(bpiData.getCurrencyCode())
                            .rate(bpiData.getRate())
                            .description(bpiData.getDescription())
                            .build())
                .collect(Collectors.toList());
        bpiRepository.saveAll(bpiList);

        return prepareCoindeskResponse(coindesk, bpiList);
    }

    @Transactional
    public CoindeskResponse updateCoindeskById(Long id, CoindeskRequest coindeskRequest) {

        Coindesk coindesk =
            coindeskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coindesk not exist."));

        bpiRepository.deleteByCoindeskId(id);

        ConvertUtil.copyNotNullProperties(coindeskRequest, coindesk);

        coindeskRepository.save(coindesk);

        List<Bpi> bpiList =
            coindeskRequest.getBpis()
                .stream()
                .map(
                    bpiData ->
                        Bpi.builder()
                            .coindeskId(id)
                            .currencyCode(bpiData.getCurrencyCode())
                            .rate(bpiData.getRate())
                            .description(bpiData.getDescription())
                            .build())
                .collect(Collectors.toList());
        bpiRepository.saveAll(bpiList);

        return prepareCoindeskResponse(coindesk, bpiList);
    }

    @Transactional
    public void deleteCoindeskById(Long id) {
        coindeskRepository.deleteById(id);
        bpiRepository.deleteByCoindeskId(id);
    }

    private void purgeCoindeskData(String chartName) {

        coindeskRepository.findByChartName(chartName)
            .ifPresent(coindesk -> {
                Long coindeskId = coindesk.getId();
                coindeskRepository.deleteById(coindeskId);
                bpiRepository.deleteByCoindeskId(coindeskId);
            });
    }

    private CoindeskResponse prepareCoindeskResponse(
            Coindesk coindesk, List<Bpi> bpiList) {

        CoindeskResponse coindeskResponse =
            ConvertUtil.convert(coindesk, CoindeskResponse.class);

        coindeskResponse.setBpis(
            bpiList.stream()
                .map(entity ->
                    BpiData.builder()
                        .currencyCode(entity.getCurrencyCode())
                        .currencyName(
                            currencyNameRepository.findByCurrencyCodeAndLanguage(
                                    entity.getCurrencyCode(),
                                    Constants.LANGUAGE_ZH_TW)
                                .orElse(new CurrencyName())
                                .getName())
                        .rate(entity.getRate())
                        .description(entity.getDescription())
                        .build())
                .collect(Collectors.toList()));

        return coindeskResponse;
    }
}
