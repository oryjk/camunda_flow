package com.betalpha.fosun.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * created on 2018/5/10
 *
 * @author huzongpeng
 */
@Data
public class Dividend {

    private String code;
    private Double dividend;
    private LocalDate tradingDate;
    private String type;
}
