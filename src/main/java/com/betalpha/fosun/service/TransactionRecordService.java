package com.betalpha.fosun.service;

import com.betalpha.fosun.data.loader.BondTransactionRecordLoader;
import com.betalpha.fosun.data.loader.StockTransactionRecordLoader;
import com.betalpha.fosun.model.BondTransactionRecord;
import com.betalpha.fosun.model.StockTransactionRecord;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * created on 2018/5/11
 *
 * @author huzongpeng
 */
@Slf4j
@Service
public class TransactionRecordService {

    public static ConcurrentMap<LocalDate, List<StockTransactionRecord>> stockTransactionRecordMap = Maps.newConcurrentMap();
    public static ConcurrentMap<LocalDate, List<BondTransactionRecord>> bondTransactionRecordMap = Maps.newConcurrentMap();
    private static Map<String, String> codeCurrencyMap = Maps.newHashMap();
    
    @PostConstruct
    public static void init() {
        List<StockTransactionRecord> stockTransactionRecordList = StockTransactionRecordLoader.load();
        Map<LocalDate, List<StockTransactionRecord>> recordMap = stockTransactionRecordList.parallelStream()
                .collect(Collectors.groupingBy(StockTransactionRecord::getTradingDate, Collectors.toList()));
        stockTransactionRecordMap.putAll(recordMap);

        List<BondTransactionRecord> bondTransactionRecordList = BondTransactionRecordLoader.load();
        Map<LocalDate, List<BondTransactionRecord>> map = bondTransactionRecordList.parallelStream().collect(Collectors.groupingBy(BondTransactionRecord::getTradingDate, Collectors.toList()));
        bondTransactionRecordMap.putAll(map);

        stockTransactionRecordList.forEach(record -> {
            if (!codeCurrencyMap.containsKey(record.getCode())) {
                codeCurrencyMap.put(record.getCode(), record.getCurrency());
            }
        });
        bondTransactionRecordList.forEach(record -> {
            if (!codeCurrencyMap.containsKey(record.getCode())) {
                codeCurrencyMap.put(record.getCode(), record.getCurrency());
            }
        });
    }

    public static LocalDate getStartDate() {
        if (stockTransactionRecordMap.isEmpty()) {
            init();
        }
        List<LocalDate> tradingDateList = Lists.newArrayList();
        tradingDateList.addAll(stockTransactionRecordMap.keySet());
        tradingDateList.addAll(bondTransactionRecordMap.keySet());
        Collections.sort(tradingDateList);
        return tradingDateList.get(0);
    }

    public static LocalDate getEndDate() {
        if (stockTransactionRecordMap.isEmpty()) {
            init();
        }
        List<LocalDate> tradingDateList = Lists.newArrayList();
        tradingDateList.addAll(stockTransactionRecordMap.keySet());
        tradingDateList.addAll(bondTransactionRecordMap.keySet());
        Collections.sort(tradingDateList);
        return tradingDateList.get(tradingDateList.size() - 1);
    }

    public static String getCurrency(String code) {
        return codeCurrencyMap.get(code);
    }
}
