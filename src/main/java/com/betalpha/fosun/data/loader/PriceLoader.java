package com.betalpha.fosun.data.loader;

import com.betalpha.fosun.model.Price;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * created on 2018/5/10
 *
 * @author huzongpeng
 */
@Slf4j
public class PriceLoader extends AbstractLoader{

    public static List<Price> load() {
        log.info("start to process");
        String path = "/data/";
        List<String> fileNames = Lists.newArrayList("债券价格.csv", "股票价格.csv");
        List<Price> list = Lists.newArrayList();
        fileNames.forEach(fileName -> {

            String pathStr = path + fileName;
            List<Map<String, String>> lines = readCsv(pathStr);
            List<Price> priceList = Lists.newArrayList();
            for (Map<String, String> map : lines) {
                Price price = new Price();
                price.setCode(map.get("Symbol"));
                if (map.get("Price").equals("NA")) {
                    BeanUtils.copyProperties(priceList.get(priceList.size() - 1), price);
                } else {
                    price.setClose(convertToDouble(map.get("Price")));
                }
                price.setTradingDate(convertDate(map.get("Date")));
                priceList.add(price);
            }
            list.addAll(priceList);
        });
        log.info("finish processing ");
        return list;
    }


}
