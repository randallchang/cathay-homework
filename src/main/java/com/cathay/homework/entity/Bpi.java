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
@Table(name = "bpi")
public class Bpi extends BaseEntity {

    @Column(name = "coindesk_id")
    private Long coindeskId;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "rate")
    private String rate;

    @Column(name = "description")
    private String description;
}
