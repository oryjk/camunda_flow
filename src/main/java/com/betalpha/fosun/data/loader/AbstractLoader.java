package com.betalpha.fosun.data.loader;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created on 2018/5/10
 *
 * @author huzongpeng
 */
@Slf4j
abstract class AbstractLoader {

    static Double convertToDouble(String value) {
        if (StringUtils.isEmpty(value)) {
            return Double.NaN;
        }
        return Double.valueOf(value);
    }

    static LocalDate convertDate(String date) {
        if (date.contains("/")) {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/M/d"));
        }
        return LocalDate.parse(date);
    }

    static List<Map<String, String>> readCsv(String pathStr) {
        log.info("start to read file path {}", pathStr);
        List<Map<String, String>> linesMap = null;
        try {
            linesMap = new ArrayList<>();
            InputStream is = ExchangeRateLoader.class.getResourceAsStream(pathStr);
            CSVReader reader = new CSVReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String[] head = reader.readNext();
            String[] row = null;
            while ((row = reader.readNext()) != null){
                Map<String, String> lineMap = new HashMap<>();
                for (int fieldIndex = 0; fieldIndex < head.length; ++fieldIndex) {
                    lineMap.put(head[fieldIndex], row[fieldIndex]);
                }
                linesMap.add(lineMap);
            }
        } catch (IOException e) {
            log.error("read csv error", e);
        }
        return linesMap;
    }
}
