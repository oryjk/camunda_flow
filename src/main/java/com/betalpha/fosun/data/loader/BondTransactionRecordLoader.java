package com.betalpha.fosun.data.loader;

import com.betalpha.fosun.model.BondTransactionRecord;
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
public class BondTransactionRecordLoader extends AbstractLoader{

    public static List<BondTransactionRecord> load() {
        log.info("start to process");
        String path = "/data/";
        String fileName = "算例样本-债券.csv";

        String pathStr = path + fileName;
        List<Map<String, String>> lines = readCsv(pathStr);
        List<BondTransactionRecord> list = lines.stream().map(map -> {
            BondTransactionRecord record = new BondTransactionRecord();
            record.setAccount(map.get("\uFEFF账户(出资实体)"));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
            record.setTradingDate(LocalDate.parse(map.get("交易日期"), dateTimeFormatter));
            record.setCode(map.get("债券代码ISIN"));
            record.setName(map.get("债券名称"));
            record.setCoupon(convertToDouble(map.get("票息")));
            if ("卖出".equals(map.get("交易方向"))) {
                record.setAmount(0 - convertToDouble(map.get("数量").replace(",", "").replace("(", "").replace(")", "")));
            } else {
                record.setAmount(convertToDouble(map.get("数量").replace(",", "").replace("(", "").replace(")", "")));
            }
            record.setCurrency(map.get("交易货币"));
            record.setNetPrice(convertToDouble(map.get("价格(净价)")));
            record.setPrice(convertToDouble(map.get("价格(全价)")));
            record.setNetTransactionCost(convertToDouble(map.get("交易成本(净价)").replace(",","").replace("(","").replace(")","")));
            record.setTransactionCost(convertToDouble(map.get("交易成本(全价)").replace(",","").replace("(","").replace(")","")));
            return record;
        }).collect(Collectors.toList());

        log.info("finish processing ");
        return list;
    }
}
