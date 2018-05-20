package com.betalpha.fosun.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * created on 2018/5/11
 *
 * @author huzongpeng
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FundHoldingHistory {

    private String code;
    private String name;
    private String tradingDate;
    private Double weight;
    private Double amount;
    private Double marketValue;
    private Double dailyReturn;
    private Double nav;
}
