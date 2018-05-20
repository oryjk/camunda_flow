package com.betalpha.fosun.data.loader;

import com.betalpha.fosun.model.StockTransactionRecord;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created on 2018/5/10
 *
 * @author huzongpeng
 */
@Slf4j
public class StockTransactionRecordLoader extends AbstractLoader{

    public static List<StockTransactionRecord> load() {
        log.info("start to process");
        String path = "/data/";
        String fileName = "算例样本-股票.csv";

        String pathStr = path + fileName;
        List<Map<String, String>> lines = readCsv(pathStr);
        List<StockTransactionRecord> list = lines.stream().map(map -> {
            StockTransactionRecord record = new StockTransactionRecord();
            record.setAccount(map.get("\uFEFF账户(出资实体)"));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
            record.setTradingDate(LocalDate.parse(map.get("交易日期"), dateTimeFormatter));
            record.setCode(map.get("股票代码"));
            record.setName(map.get("股票名称"));
            record.setAmount(convertToDouble(map.get("股数").replace(",", "").replace("(", "").replace(")", "")));
            record.setCurrency(map.get("交易货币"));
            record.setPrice(convertToDouble(map.get("价格")));
            if (!map.get("股票总值").contains("-")) {
                record.setTotalValue(convertToDouble(map.get("股票总值").replace(",","").replace("(","").replace(")","")));
            }
            record.setNetValue(convertToDouble(map.get("股票净值").replace(",","").replace("(","").replace(")","")));
            return record;
        }).collect(Collectors.toList());

        log.info("finish processing ");
        return list;
    }
}
