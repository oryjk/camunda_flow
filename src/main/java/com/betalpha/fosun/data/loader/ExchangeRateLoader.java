package com.betalpha.fosun.data.loader;

import com.betalpha.fosun.model.ExchangeRate;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * created on 2018/5/10
 *
 * @author huzongpeng
 */
@Slf4j
public class ExchangeRateLoader extends AbstractLoader{

    public static List<ExchangeRate> load() {
        log.info("start to process");
        String path = "/data/";
        String fileName = "exchangeRate.csv";
        String pathStr = path + fileName;
        List<Map<String, String>> lines = readCsv(pathStr);
        List<ExchangeRate> exchangeRates = lines.stream().map(map -> {
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setEURRate(convertToDouble(map.get("EURRate")));
            exchangeRate.setHKDRate(convertToDouble(map.get("HKDRate")));
            exchangeRate.setTradingDate(LocalDate.parse(map.get("tradingDate")));
            exchangeRate.setUSDRate(convertToDouble(map.get("USDRate")));
            return exchangeRate;
        }).collect(Collectors.toList());
        log.info("finish processing ");
        return exchangeRates;
    }

}
