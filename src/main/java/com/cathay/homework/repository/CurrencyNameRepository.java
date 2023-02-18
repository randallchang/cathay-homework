package com.cathay.homework.repository;

import java.util.Optional;

import com.cathay.homework.entity.CurrencyName;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyNameRepository extends JpaRepository<CurrencyName, Long> {

    Optional<CurrencyName> findByCurrencyCodeAndLanguage(String currencyCode, String language);
}