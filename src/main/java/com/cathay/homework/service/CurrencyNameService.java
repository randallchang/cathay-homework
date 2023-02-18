package com.cathay.homework.service;

import java.util.List;
import java.util.Optional;

import com.cathay.homework.entity.CurrencyName;
import com.cathay.homework.model.currency_name.request.CurrencyNameRequest;
import com.cathay.homework.model.currency_name.response.CurrencyNameResponse;
import com.cathay.homework.repository.CurrencyNameRepository;
import com.cathay.homework.util.ConvertUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyNameService {

    private final CurrencyNameRepository currencyNameRepository;

    public List<CurrencyNameResponse> findAll() {
        return ConvertUtil.convertList(
                currencyNameRepository.findAll(),
                CurrencyNameResponse.class);
    }

    public CurrencyNameResponse findById(Long id) {
        return ConvertUtil.convert(
                currencyNameRepository.findById(id),
                CurrencyNameResponse.class);
    }

    public CurrencyNameResponse createCurrencyName(CurrencyNameRequest currencyNameRequest) {

        Optional<CurrencyName> currencyNameOptional =
            currencyNameRepository.findByCurrencyCodeAndLanguage(
                currencyNameRequest.getCurrencyCode(),
                currencyNameRequest.getLanguage());

        CurrencyName entity = null;

        if (currencyNameOptional.isPresent()) {
            entity = currencyNameOptional.get();
        } else {
            entity = currencyNameRepository.save(
                ConvertUtil.convert(
                    currencyNameRequest,
                    CurrencyName.class));
        }

        return ConvertUtil.convert(
            entity,
            CurrencyNameResponse.class);
    }

    public CurrencyNameResponse updateCurrencyNameById(
            Long id, CurrencyNameRequest currencyNameRequest) {

        Optional<CurrencyName> currencyNameOptional =
            currencyNameRepository.findById(id);

        CurrencyName entity = null;

        if (currencyNameOptional.isPresent()) {
            entity = currencyNameOptional.get();
            ConvertUtil.copyNotNullProperties(currencyNameRequest, entity);
            entity = currencyNameRepository.save(entity);
        } else {
            throw new RuntimeException("CurrencyName not exist.");
        }

        return ConvertUtil.convert(entity, CurrencyNameResponse.class);
    }

    public void deleteCurrencyNameById(Long id) {
        currencyNameRepository.findById(id)
            .ifPresent(entity -> currencyNameRepository.deleteById(id));
    }
}
