package com.betalpha.fosun.service;

import com.betalpha.fosun.FlowConstants;
import com.betalpha.fosun.server.Server;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private static Map<String, List<Rating>> submitterToRating = Maps.newHashMap();
    private static Map<String, Rating> isinToRating = Maps.newHashMap();

    private List<String> highRating = Lists.newArrayList("Aaa", "Aa1", "Aa2", "Aa3", "A1", "A2", "A3", "Baa1", "Baa2", "Baa3",
            "P-1", "P-2", "P-3",
            "AAA", "AA+", "AA", "AA-", "A+", "A", "A-", "BBB+", "BBB", "BBB-",
            "A-1+", "A-1", "A-2", "A-3",
            "F1+", "F1", "F2");

    public String getRating(String isin) {
        if (!isinToRating.containsKey(isin)) {
            List<String> bloombergRating = server.getBloombergRating(isin);
            cacheData(bloombergRating);
        }
        if (isinToRating.containsKey(isin)) {
            return isinToRating.get(isin).getRating();
        }
        return "";
    }

    public String getSubmitter(String isin) {
        if (!isinToRating.containsKey(isin)) {
            List<String> bloombergRating = server.getBloombergRating(isin);
            cacheData(bloombergRating);
        }
        if (isinToRating.containsKey(isin)) {
            return isinToRating.get(isin).getSubmitter();
        }
        return "";
    }

    public List<String> getSameLevelIsin(String isin) {
        String submitter = getSubmitter(isin);
        String rating = getRating(isin);
        if (!submitter.isEmpty() && !submitterToRating.containsKey(submitter)) {
            List<String> bloombergRating = server.getBloombergRating(isin);
            cacheData(bloombergRating);
        }
        if (submitterToRating.containsKey(submitter)) {
            return submitterToRating.get(submitter).stream().filter(rating1 -> rating1.getRating().equals(rating)).map(Rating::getIsin).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    private String convertToRating(String bloombergRating) {
        List<String> ratings = Lists.newArrayList(bloombergRating.split("\t"));
        ratings.remove(0);
        if (ratings.stream().anyMatch(StringUtils::hasText) && ratings.stream().filter(StringUtils::hasText).allMatch(highRating::contains)) {
            return FlowConstants.Hign_Rate_Bond();
        }
        return FlowConstants.High_Yield_Bond();
    }

    private void cacheData(List<String> ratings) {
        if (ratings.isEmpty()) {
            log.warn("list is empty");
            return;
        }
        String submitter = ratings.remove(0).trim();
        List<Rating> ratingList = ratings.stream().map(line -> {
            List<String> list = Lists.newArrayList(line.split("\t"));
            String isin = list.remove(0);
            Rating rating = new Rating(isin, convertToRating(line), submitter);
            isinToRating.put(isin, rating);
            return rating;
        }).collect(Collectors.toList());
        submitterToRating.put(submitter, ratingList);
    }

    class Rating {
        private String isin;
        private String rating;
        private String submitter;

        Rating(String isin, String rating, String submitter) {
            this.isin = isin;
            this.rating = rating;
            this.submitter = submitter;
        }

        String getRating() {
            return rating;
        }

        String getSubmitter() {
            return submitter;
        }

        String getIsin() {
            return isin;
        }
    }

    public String getMockRating(String isinCode) {
        return isinCode.startsWith("8") ? FlowConstants.Hign_Rate_Bond() : FlowConstants.High_Yield_Bond();
    }
}
