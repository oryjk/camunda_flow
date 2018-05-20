package com.betalpha.fosun.service;

import com.betalpha.fosun.data.loader.DividendLoader;
import com.betalpha.fosun.model.Dividend;
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
public class DividendService {

    private static ConcurrentMap<LocalDate, Map<String, Dividend>> dividendMap = Maps.newConcurrentMap();

    @PostConstruct
    public static void init() {
        List<Dividend> dividendList = DividendLoader.load();
        Map<LocalDate, Map<String, Dividend>> map = dividendList.parallelStream()
                .collect(Collectors.groupingBy(Dividend::getTradingDate, Collectors.toMap(Dividend::getCode, Function.identity())));
        dividendMap.putAll(map);
    }

    public static Double getDividend(LocalDate tradingDate, String code) {
        if (dividendMap.isEmpty()) {
            init();
        }
        if (dividendMap.get(tradingDate) == null) {
            return 0D;
        }
        Dividend dividend = dividendMap.get(tradingDate).get(code);
        if (dividend == null) {
            return 0D;
        }
        return dividend.getDividend();
    }
}
