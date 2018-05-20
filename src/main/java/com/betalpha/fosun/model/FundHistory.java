package com.betalpha.fosun.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * created on 2018/5/11
 *
 * @author huzongpeng
 */
@Data
public class FundHistory {

    private Double nav;
    private LocalDate tradingDate;
    private Double marketValue;
    private List<FundHoldingHistory> fundHoldingHistoryList;
    private Double dailyReturn;

    public String getTradingDate() {
        return tradingDate.toString();
    }
}
