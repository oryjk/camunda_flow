package com.betalpha.fosun.fund.rest;

import com.betalpha.fosun.model.FundHistory;
import com.betalpha.fosun.model.FundHoldingHistory;
import com.betalpha.fosun.service.FundCalculator;
import com.betalpha.fosun.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * created on 2018/5/11
 *
 * @author huzongpeng
 */
@Slf4j
@Controller
@RequestMapping(value = "/api/fund", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FundController {

    @Autowired
    private RatingService ratingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getFundHistoryList() {
        List<FundHistory> fundHistoryList = FundCalculator.getFundHistoryList();
        return ResponseEntity.ok(fundHistoryList);
    }

    @RequestMapping(value = "/getRating/{isin}", method = RequestMethod.GET)
    public ResponseEntity getRating(@PathVariable("isin") String isin) {
        log.info("start get rating:{}", isin);
        String rating = ratingService.getRating(isin);
        return ResponseEntity.ok(rating);
    }

    @RequestMapping(value = "/stock", method = RequestMethod.POST)
    public ResponseEntity getByCode(String code) {
        log.info("start get rating:{}", code);
        List<FundHoldingHistory> byCode = FundCalculator.getByCode(code);
        return ResponseEntity.ok(byCode);
    }
}
