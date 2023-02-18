package com.cathay.homework.repository;

import java.util.Optional;

import com.cathay.homework.entity.Coindesk;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoindeskRepository extends JpaRepository<Coindesk, Long> {

    Optional<Coindesk> findByChartName(String chartName);
}