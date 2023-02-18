package com.cathay.homework.repository;

import java.util.List;

import com.cathay.homework.entity.Bpi;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BpiRepository extends JpaRepository<Bpi, Long> {

    List<Bpi> findByCoindeskId(Long coindeskId);

    long deleteByCoindeskId(Long coindeskId);

    List<Bpi> findByCoindeskIdIn(List<Long> coindeskIdList);
}