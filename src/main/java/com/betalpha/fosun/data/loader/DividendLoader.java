package com.betalpha.fosun.data.loader;

import com.betalpha.fosun.model.Dividend;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created on 2018/5/10
 *
 * @author huzongpeng
 */
@Slf4j
public class DividendLoader extends AbstractLoader{

    public static List<Dividend> load() {
        log.info("start to process");
        String path = "/data/";
        String fileName = "股票分红.csv";
        List<String> dividendType = Lists.newArrayList("Special Cash", "Final", "Regular Cash", "Pro Rata", "Interim", "Entitlement", "Stock Dividend", "Stock Reform-Entitlement");
        List<Dividend> list = Lists.newArrayList();

        String pathStr = path + fileName;
        List<Map<String, String>> lines = readCsv(pathStr);
        List<Dividend> stockDividendList = lines.stream().filter(map -> dividendType.contains(map.get("Type"))).map(map -> {
            Dividend dividend = new Dividend();
            dividend.setCode(map.get("Symbol"));
            dividend.setTradingDate(convertDate(map.get("Date")));
            dividend.setDividend(convertToDouble(map.get("Amount")));
            dividend.setType(map.get("Type"));
            return dividend;
        }).collect(Collectors.toList());
        list.addAll(stockDividendList);

        fileName = "债券付息.csv";
        pathStr = path + fileName;
        lines = readCsv(pathStr);
        List<Dividend> bondDividend = lines.stream().map(map -> {
            Dividend dividend = new Dividend();
            dividend.setCode(map.get("ISIN"));
            dividend.setTradingDate(convertDate(map.get("Date")));
            dividend.setDividend(convertToDouble(map.get("Interest")) / convertToDouble(map.get("Principal")));
            return dividend;
        }).collect(Collectors.toList());
        list.addAll(bondDividend);
        log.info("finish processing");
        return list;
    }
}
