package com.betalpha.fosun.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * created on 2018/5/10
 *
 * @author huzongpeng
 */
@Data
public class BondTransactionRecord {

    private String account;
    private LocalDate tradingDate;
    private String code;
    private String name;
    private Double coupon;
    private Double amount;
    private String currency;
    private Double netPrice;
    private Double price;
    private Double netTransactionCost;
    private Double transactionCost;
}
