package com.betalpha.fosun.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * created on 2018/5/10
 *
 * @author huzongpeng
 */
@Data
public class Price {

    private String code;
    private LocalDate tradingDate;
    private Double close;
}
