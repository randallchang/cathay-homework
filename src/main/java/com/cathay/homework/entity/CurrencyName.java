package com.cathay.homework.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "currency_name")
public class CurrencyName extends BaseEntity {

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "name")
    private String name;

    @Column(name = "language")
    private String language;
}
