package com.betalpha.fosun.service;

import com.betalpha.fosun.data.loader.SplitLoader;
import com.betalpha.fosun.model.Split;
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
public class SplitService {

    private static ConcurrentMap<LocalDate, Map<String, Split>> splitMap = Maps.newConcurrentMap();

    @PostConstruct
    public static void init() {
        List<Split> splitList = SplitLoader.load();
        Map<LocalDate, Map<String, Split>> map = splitList.parallelStream()
                .collect(Collectors.groupingBy(Split::getTradingDate, Collectors.toMap(Split::getCode, Function.identity())));
        splitMap.putAll(map);
    }

    public static Double getSplit(LocalDate tradingDate, String code) {
        if (splitMap.isEmpty()) {
            init();
        }
        if (splitMap.get(tradingDate) == null) {
            return 1D;
        }
        Split split = splitMap.get(tradingDate).get(code);
        if (split == null) {
            return 1D;
        }
        return split.getSplit();
    }

    public static String getType(LocalDate tradingDate, String code) {
        return splitMap.get(tradingDate).get(code).getType();
    }
}
