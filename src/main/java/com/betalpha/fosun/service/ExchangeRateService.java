package com.betalpha.fosun.service;

import com.betalpha.fosun.data.loader.ExchangeRateLoader;
import com.betalpha.fosun.model.ExchangeRate;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * created on 2018/5/11
 *
 * @author huzongpeng
 */
@Slf4j
@Service
public class ExchangeRateService {

    private static ConcurrentMap<LocalDate, ExchangeRate> exchangeRateMap = Maps.newConcurrentMap();

    @PostConstruct
    public static void init() {
        List<ExchangeRate> exchangeRateList = ExchangeRateLoader.load();
        Map<LocalDate, ExchangeRate> map = exchangeRateList.parallelStream()
                .collect(Collectors.toMap(ExchangeRate::getTradingDate, Function.identity()));
        exchangeRateMap.putAll(map);
    }

    public static ExchangeRate getExchangeRate(LocalDate tradingDate) {
        if (exchangeRateMap.isEmpty()) {
            init();
        }
        return exchangeRateMap.get(tradingDate);
    }

    public static Double getExchangeRate(LocalDate tradingDate, String currency) {
        if (exchangeRateMap.isEmpty()) {
            init();
        }
        ExchangeRate exchangeRate = exchangeRateMap.get(tradingDate);
        if (exchangeRate == null){
            exchangeRate = getPreExchangeRate(tradingDate);
        }
        if (currency.contains("HKD")){
            return exchangeRate.getHKDRate();
        }
        if (currency.contains("EUR")) {
            return exchangeRate.getEURRate();
        }
        if (currency.contains("USD")) {
            return exchangeRate.getUSDRate();
        }
        return 1D;
    }

    private static ExchangeRate getPreExchangeRate(LocalDate date) {
        LocalDate preTradingDate = TradingDateService.getPreTradingDate(date);
        if (exchangeRateMap.get(preTradingDate) == null) {
            return getPreExchangeRate(preTradingDate);
        }
        return exchangeRateMap.get(preTradingDate);
    }
}
