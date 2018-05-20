package com.betalpha.fosun.service;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * created on 2018/5/11
 *
 * @author huzongpeng
 */
@Service
public class TradingDateService {

    private static List<LocalDate> tradingDates;

    @PostConstruct
    public static void init() {
        LocalDate startDate = TransactionRecordService.getStartDate();
        LocalDate endDate = TransactionRecordService.getEndDate();
        tradingDates = Lists.newArrayList();
        int i = 0;
        while(startDate.plusDays(i).isBefore(endDate)) {
            if (startDate.plusDays(i).getDayOfWeek().equals(DayOfWeek.SATURDAY) || startDate.plusDays(i).getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                i++;
                continue;
            }
            tradingDates.add(startDate.plusDays(i));
            i++;
        }
        tradingDates.add(endDate);
        Collections.sort(tradingDates);
    }

    public static List<LocalDate> getTradingDates() {
        if (tradingDates == null) {
            init();
        }
        return tradingDates;
    }

    public static LocalDate getPreTradingDate(LocalDate tradingDate) {
        if (tradingDates.indexOf(tradingDate) == 0) {
            return null;
        }
        return tradingDates.get(tradingDates.indexOf(tradingDate) - 1);
    }
}
