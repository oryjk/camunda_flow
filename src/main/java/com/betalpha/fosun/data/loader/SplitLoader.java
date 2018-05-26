package com.betalpha.fosun.data.loader;

import com.betalpha.fosun.model.Split;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created on 2018/5/10
 *
 * @author huzongpeng
 */
@Slf4j
public class SplitLoader extends AbstractLoader{

    public static List<Split> load() {
        log.info("start to process");
        String path = "/data/";
        String fileName = "股票分红.csv";
        List<String> splitType = Lists.newArrayList("Stock Split", "Bonus");
        String pathStr = path + fileName;
        List<Map<String, String>> lines = readCsv(pathStr);

        List<Split> dividendList = lines.stream().filter(map -> splitType.contains(map.get("Type"))).map(map -> {
            Split split = new Split();
            split.setCode(map.get("Symbol"));
            split.setTradingDate(convertDate(map.get("Date")));
            split.setType(map.get("Type"));
            split.setSplit(calculateSplit(map.get("Amount"), split.getType()));
            return split;
        }).collect(Collectors.toList());
        log.info("finish processing ");
        return dividendList;
    }

    private static Double calculateSplit(String split, String type) {
        if (StringUtils.isEmpty(split)) {
            return Double.NaN;
        }
        if ("Stock Split".equals(type)) {
            return Double.valueOf(split);
        }
        return Double.valueOf(split) + 1;
    }
}
