package com.betalpha.fosun.service;

import com.betalpha.fosun.server.Server;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * created on 2018/5/16
 *
 * @author huzongpeng
 */
@Slf4j
@Service
public class RatingService {

    @Autowired
    private Server server;

    private List<String> highRating = Lists.newArrayList("Aaa", "Aa1", "Aa2", "Aa3", "A1", "A2", "A3", "Baa1", "Baa2", "Baa3",
            "P-1", "P-2", "P-3",
            "AAA", "AA+", "AA", "AA-", "A+", "A", "A-", "BBB+", "BBB", "BBB-",
            "A-1+", "A-1", "A-2", "A-3",
            "F1+", "F1", "F2");

    public String getRating(String isin) {
        String bloombergRating = server.getBloombergRating(isin);
        if (StringUtils.isEmpty(bloombergRating)) {
            return "高收益债券";
        }
        String[] split = bloombergRating.split(",");
        return convertToRating(Lists.newArrayList(split));
    }

    private String convertToRating(List<String> bloombergRating) {
        if (bloombergRating.stream().anyMatch(StringUtils::hasText)) {
            if (bloombergRating.stream().filter(StringUtils::hasText).anyMatch(rating -> !highRating.contains(rating))) {
                return "高收益债券";
            }
            return "高评级债券";
        } else {
            return "高收益债券";
        }
    }
}
