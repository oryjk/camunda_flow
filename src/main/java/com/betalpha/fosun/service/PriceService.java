package com.betalpha.fosun.service;

import com.betalpha.fosun.data.loader.PriceLoader;
import com.betalpha.fosun.model.Price;
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
public class PriceService {

    private static ConcurrentMap<LocalDate, Map<String, Price>> priceMap = Maps.newConcurrentMap();

    @PostConstruct
    public static void init() {
        List<Price> priceList = PriceLoader.load();
        Map<LocalDate, Map<String, Price>> map = priceList.parallelStream()
                .collect(Collectors.groupingBy(Price::getTradingDate, Collectors.toMap(Price::getCode, Function.identity())));
        priceMap.putAll(map);
    }

    public static Price getPrice(LocalDate tradingDate, String code) {
        if (priceMap.isEmpty()) {
            init();
        }
        if (priceMap.get(tradingDate) == null) {
            return null;
        }
        return priceMap.get(tradingDate).get(code);
    }
}
