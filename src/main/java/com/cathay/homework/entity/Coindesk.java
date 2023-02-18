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
@Table(name = "coindesk")
public class Coindesk extends BaseEntity {

    @Column(name = "chart_name")
    private String chartName;

    @Column(name = "disclaimer")
    private String disclaimer;

    @Column(name = "api_update_time")
    private String apiUpdateTime;
}
