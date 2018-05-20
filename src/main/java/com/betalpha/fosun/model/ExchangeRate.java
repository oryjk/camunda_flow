package com.betalpha.fosun.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * created on 2018/5/10
 *
 * @author huzongpeng
 */
@Data
public class ExchangeRate {

    private LocalDate tradingDate;
    private Double USDRate;
    private Double HKDRate;
    private Double EURRate;
}
