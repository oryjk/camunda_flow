package com.betalpha.fosun.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * created on 2018/5/10
 *
 * @author huzongpeng
 */
@Data
public class StockTransactionRecord {

    private String account;
    private LocalDate tradingDate;
    private String code;
    private String name;
    private Double amount;
    private String currency;
    private Double price;
    private Double totalValue;
    private Double netValue;
}
