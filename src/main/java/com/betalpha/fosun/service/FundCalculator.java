package com.betalpha.fosun.service;

import com.betalpha.fosun.model.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * created on 2018/5/11
 *
 * @author huzongpeng
 */
@Service
@Slf4j
public class FundCalculator {

    private static TreeMap<LocalDate, FundHistory> fundHistoryMap = Maps.newTreeMap();

    private static Map<String, Double> bondParValue = Maps.newHashMap();

    static {
        bondParValue.put("US00170F2092 CORP", 50D);
        bondParValue.put("IL0062905489 CORP", 1000D);
    }

    public static List<FundHistory> getFundHistoryList() {
        if (fundHistoryMap.isEmpty()) {
            calculate();
        }
        return Lists.newArrayList(fundHistoryMap.values());
    }

    public static List<FundHoldingHistory> getByCode(String code) {
        List<LocalDate> tradingDates = TradingDateService.getTradingDates();
        TreeMap<LocalDate, FundHoldingHistory> holdingHistoryMap = Maps.newTreeMap();

        tradingDates.forEach(tradingDate -> {
            //前一日持仓
            FundHoldingHistory preHolding = null;
            if (TradingDateService.getPreTradingDate(tradingDate) != null && !holdingHistoryMap.isEmpty()) {
                preHolding = holdingHistoryMap.get(TradingDateService.getPreTradingDate(tradingDate));
            }
            //今日继承的前一日持仓，只继承股数，不继承市值和权重
            FundHoldingHistory todayHolding = null;
            if (preHolding != null) {
                todayHolding = new FundHoldingHistory();
                BeanUtils.copyProperties(preHolding, todayHolding);
                todayHolding.setTradingDate(tradingDate.toString());
                todayHolding.setAmount(preHolding.getAmount() * SplitService.getSplit(tradingDate, code));
            }

            //股票交易记录处理
            //根据股票代码筛选当日交易记录
            List<StockTransactionRecord> stockRecordList = TransactionRecordService.stockTransactionRecordMap.getOrDefault(tradingDate, Lists.newArrayList()).stream().filter(record -> record.getCode().equals(code)).collect(Collectors.toList());
            if (!stockRecordList.isEmpty()) {
                //相同股票合并为一条持仓记录
                double todaySum = stockRecordList.stream().mapToDouble(StockTransactionRecord::getAmount).sum();
                if (todayHolding != null) {
                    todayHolding.setAmount(todayHolding.getAmount() + todaySum);
                    todayHolding.setMarketValue(todayHolding.getAmount() * getClosePrice(tradingDate, code, stockRecordList.get(0).getPrice()));
                } else {
                    todayHolding = new FundHoldingHistory();
                    todayHolding.setTradingDate(tradingDate.toString());
                    todayHolding.setAmount(todaySum);
                    todayHolding.setMarketValue(todayHolding.getAmount() * getClosePrice(tradingDate, code, stockRecordList.get(0).getPrice()));
                }
            }

            //债券交易记录处理
            List<BondTransactionRecord> bondRecordList = TransactionRecordService.bondTransactionRecordMap.getOrDefault(tradingDate, Lists.newArrayList()).stream().filter(record -> record.getCode().equals(code)).collect(Collectors.toList());
            if (!bondRecordList.isEmpty()) {
                double todaySum = bondRecordList.stream().mapToDouble(BondTransactionRecord::getAmount).sum();
                if (todayHolding != null) {
                    todayHolding.setAmount(todayHolding.getAmount() + todaySum);
                    todayHolding.setMarketValue(getRealAmount(code, todayHolding.getAmount()) * getClosePrice(tradingDate, code, bondRecordList.get(0).getPrice()));
                } else {
                    todayHolding = new FundHoldingHistory();
                    todayHolding.setTradingDate(tradingDate.toString());
                    todayHolding.setAmount(todaySum);
                    todayHolding.setMarketValue(getRealAmount(code, todayHolding.getAmount()) * getClosePrice(tradingDate, code, bondRecordList.get(0).getPrice()));
                }

            }

            if (todayHolding == null) {
                return;
            }

            //计算市值
            Double exchangeRate = ExchangeRateService.getExchangeRate(tradingDate, TransactionRecordService.getCurrency(code));
            Price price = PriceService.getPrice(tradingDate, code);
            //获取不到价格则继承昨日市值
            if (price != null) {
                todayHolding.setMarketValue(getRealAmount(code, todayHolding.getAmount()) * price.getClose() * exchangeRate);
            }

            if (preHolding != null) {
                double totalDividend = todayHolding.getAmount() * bondParValue.getOrDefault(code, 1D) * DividendService.getDividend(tradingDate, code) * ExchangeRateService.getExchangeRate(tradingDate, TransactionRecordService.getCurrency(code));
                double stockSellMarketValue = 0;
                double stockBuyMarketValue = 0;
                if (!stockRecordList.isEmpty()) {
                    stockSellMarketValue = Math.abs(stockRecordList.stream().filter(stockTransactionRecord -> stockTransactionRecord.getAmount() < 0).mapToDouble(record -> record.getAmount() * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum());
                    stockBuyMarketValue = stockRecordList.stream().filter(record -> record.getAmount() > 0).mapToDouble(record -> record.getAmount() * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum();
                }
                double bondSell = 0;
                double bondBuy = 0;
                if (!bondRecordList.isEmpty()) {
                    bondSell = Math.abs(bondRecordList.stream().filter(record -> record.getAmount() < 0).mapToDouble(record -> getRealAmount(record.getCode(), record.getAmount()) * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum());
                    bondBuy = bondRecordList.stream().filter(record -> record.getAmount() > 0).mapToDouble(record -> getRealAmount(record.getCode(), record.getAmount()) * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum();
                }
                double buyMarketValue = stockBuyMarketValue + bondBuy;
                double sellMarketValue = stockSellMarketValue + bondSell;
                todayHolding.setDailyReturn((todayHolding.getMarketValue() + totalDividend + sellMarketValue) / (preHolding.getMarketValue() + buyMarketValue) - 1);
                todayHolding.setNav(preHolding.getNav() * (1 + todayHolding.getDailyReturn()));
            } else {
                double stockSellMarketValue = 0;
                double stockBuyMarketValue = 0;
                if (!stockRecordList.isEmpty()) {
                    stockSellMarketValue = Math.abs(stockRecordList.stream().filter(stockTransactionRecord -> stockTransactionRecord.getAmount() < 0).mapToDouble(record -> record.getAmount() * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum());
                    stockBuyMarketValue = stockRecordList.stream().filter(record -> record.getAmount() > 0).mapToDouble(record -> record.getAmount() * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum();
                }
                double bondSell = 0;
                double bondBuy = 0;
                if (!bondRecordList.isEmpty()) {
                    bondSell = Math.abs(bondRecordList.stream().filter(record -> record.getAmount() < 0).mapToDouble(record -> getRealAmount(record.getCode(), record.getAmount()) * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum());
                    bondBuy = bondRecordList.stream().filter(record -> record.getAmount() > 0).mapToDouble(record -> getRealAmount(record.getCode(), record.getAmount()) * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum();
                }
                double buyMarketValue = stockBuyMarketValue + bondBuy;
                double sellMarketValue = stockSellMarketValue + bondSell;
                todayHolding.setNav((todayHolding.getMarketValue() + sellMarketValue) / buyMarketValue);
                todayHolding.setDailyReturn(todayHolding.getNav() - 1);
            }
            if (!todayHolding.getDailyReturn().isNaN()) {
                holdingHistoryMap.put(tradingDate, todayHolding);
            }
        });

        return Lists.newArrayList(holdingHistoryMap.values());
    }

    private static void calculate() {
        List<LocalDate> tradingDates = TradingDateService.getTradingDates();

        //第一天
        processFirstDay(tradingDates.get(0));

        tradingDates.forEach(tradingDate -> {
            //第一天已处理
            if (tradingDate.equals(tradingDates.get(0))){
                return;
            }

            FundHistory preFundHistory = fundHistoryMap.get(TradingDateService.getPreTradingDate(tradingDate));
            //前一日持仓
            Map<String, FundHoldingHistory> preHoldingMap = preFundHistory.getFundHoldingHistoryList().stream().collect(Collectors.toMap(FundHoldingHistory::getCode, Function.identity()));
            //今日继承的前一日持仓，只继承股数，不继承市值和权重
            Map<String, FundHoldingHistory> todayHoldingMap = preHoldingMap.values().stream().map(holdingHistory -> {
                FundHoldingHistory fundHoldingHistory = new FundHoldingHistory();
                BeanUtils.copyProperties(holdingHistory, fundHoldingHistory);
                fundHoldingHistory.setAmount(fundHoldingHistory.getAmount() * SplitService.getSplit(tradingDate, fundHoldingHistory.getCode()));
                fundHoldingHistory.setWeight(null);
                return fundHoldingHistory;
            }).collect(Collectors.toMap(FundHoldingHistory::getCode, Function.identity()));

            //股票交易记录处理
            List<StockTransactionRecord> stockRecordList = TransactionRecordService.stockTransactionRecordMap.get(tradingDate);
            if (stockRecordList != null) {
                //根据股票代码分组当日交易记录
                Map<String, List<StockTransactionRecord>> stockRecordMap = stockRecordList.stream().collect(Collectors.groupingBy(StockTransactionRecord::getCode, Collectors.toList()));
                stockRecordMap.values().forEach(records -> {
                    //相同股票合并为一条持仓记录
                    String code = records.get(0).getCode();
                    double todaySum = records.stream().mapToDouble(StockTransactionRecord::getAmount).sum();
                    if (todayHoldingMap.containsKey(code)) {
                        FundHoldingHistory fundHoldingHistory = todayHoldingMap.get(code);
                        fundHoldingHistory.setAmount(todayHoldingMap.get(fundHoldingHistory.getCode()).getAmount() + todaySum);
                        fundHoldingHistory.setMarketValue(fundHoldingHistory.getAmount() * getClosePrice(tradingDate, fundHoldingHistory.getCode(), records.get(0).getPrice()));
                    } else {
                        FundHoldingHistory fundHoldingHistory = new FundHoldingHistory();
                        fundHoldingHistory.setCode(records.get(0).getCode());
                        fundHoldingHistory.setName(records.get(0).getName());
                        fundHoldingHistory.setAmount(todaySum);
                        fundHoldingHistory.setMarketValue(fundHoldingHistory.getAmount() * getClosePrice(tradingDate, fundHoldingHistory.getCode(), records.get(0).getPrice()));
                        todayHoldingMap.put(code, fundHoldingHistory);
                    }
                });
            }

            //债券交易记录处理
            List<BondTransactionRecord> bondRecordList = TransactionRecordService.bondTransactionRecordMap.get(tradingDate);
            if (bondRecordList != null) {
                Map<String, List<BondTransactionRecord>> bondRecordMap = bondRecordList.stream().collect(Collectors.groupingBy(BondTransactionRecord::getCode, Collectors.toList()));
                bondRecordMap.values().forEach(records -> {
                    String code = records.get(0).getCode();
                    double todaySum = records.stream().mapToDouble(BondTransactionRecord::getAmount).sum();
                    if (todayHoldingMap.containsKey(code)) {
                        FundHoldingHistory fundHoldingHistory = todayHoldingMap.get(code);
                        fundHoldingHistory.setAmount(todayHoldingMap.get(code).getAmount() + todaySum);
                        fundHoldingHistory.setMarketValue(getRealAmount(fundHoldingHistory.getCode(), fundHoldingHistory.getAmount()) * getClosePrice(tradingDate, fundHoldingHistory.getCode(), records.get(0).getPrice()));
                    } else {
                        FundHoldingHistory fundHoldingHistory = new FundHoldingHistory();
                        fundHoldingHistory.setCode(records.get(0).getCode());
                        fundHoldingHistory.setName(records.get(0).getName());
                        fundHoldingHistory.setAmount(todaySum);
                        fundHoldingHistory.setMarketValue(getRealAmount(fundHoldingHistory.getCode(), fundHoldingHistory.getAmount()) * getClosePrice(tradingDate, fundHoldingHistory.getCode(), records.get(0).getPrice()));
                        todayHoldingMap.put(code, fundHoldingHistory);
                    }
                });
            }

            //计算市值
            List<FundHoldingHistory> holdingHistoryList = todayHoldingMap.values().stream().filter(fundHoldingHistory -> fundHoldingHistory.getAmount() > 0).peek(fundHoldingHistory -> {
                Double exchangeRate = ExchangeRateService.getExchangeRate(tradingDate, TransactionRecordService.getCurrency(fundHoldingHistory.getCode()));
                Price price = PriceService.getPrice(tradingDate, fundHoldingHistory.getCode());
                //获取不到价格则继承昨日市值
                if (price != null) {
                    fundHoldingHistory.setMarketValue(getRealAmount(fundHoldingHistory.getCode(), fundHoldingHistory.getAmount()) * price.getClose() * exchangeRate);
                }
            }).collect(Collectors.toList());
            double totalMarketValue = holdingHistoryList.stream().mapToDouble(FundHoldingHistory::getMarketValue).sum();
            List<FundHoldingHistory> fundHoldingHistoryList = todayHoldingMap.values().stream().filter(fundHoldingHistory -> fundHoldingHistory.getAmount() > 0).peek(fundHoldingHistory -> fundHoldingHistory.setWeight(fundHoldingHistory.getMarketValue() / totalMarketValue)).collect(Collectors.toList());

            FundHistory fundHistory = new FundHistory();
            fundHistory.setTradingDate(tradingDate);
            double totalDividend = preHoldingMap.values().stream().mapToDouble(fundHoldingHistory -> fundHoldingHistory.getAmount() * bondParValue.getOrDefault(fundHoldingHistory.getCode(), 1D) * DividendService.getDividend(tradingDate, fundHoldingHistory.getCode()) * ExchangeRateService.getExchangeRate(tradingDate, TransactionRecordService.getCurrency(fundHoldingHistory.getCode()))).sum();
            double stockSellMarketValue = 0;
            double stockBuyMarketValue = 0;
            if (stockRecordList != null) {
                stockSellMarketValue = Math.abs(stockRecordList.stream().filter(stockTransactionRecord -> stockTransactionRecord.getAmount() < 0).mapToDouble(record -> record.getAmount() * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum());
                stockBuyMarketValue = stockRecordList.stream().filter(record -> record.getAmount() > 0).mapToDouble(record -> record.getAmount() * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum();
            }
            double bondSell = 0;
            double bondBuy = 0;
            if (bondRecordList != null) {
                bondSell = Math.abs(bondRecordList.stream().filter(record -> record.getAmount() < 0).mapToDouble(record -> getRealAmount(record.getCode(), record.getAmount()) * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum());
                bondBuy = bondRecordList.stream().filter(record -> record.getAmount() > 0).mapToDouble(record -> getRealAmount(record.getCode(), record.getAmount()) * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum();
            }
            double buyMarketValue = stockBuyMarketValue + bondBuy;
            double sellMarketValue = stockSellMarketValue + bondSell;
            fundHistory.setMarketValue(totalMarketValue);
            fundHistory.setFundHoldingHistoryList(fundHoldingHistoryList);
            fundHistory.setDailyReturn((fundHistory.getMarketValue() + totalDividend + sellMarketValue) / (preFundHistory.getMarketValue() + buyMarketValue) - 1);
            fundHistory.setNav(preFundHistory.getNav() * (1 + fundHistory.getDailyReturn()));
            fundHistoryMap.put(tradingDate, fundHistory);
        });
    }

    //第一天净值默认为1，日收益默认为0，只有股票
    private static void processFirstDay(LocalDate tradingDate) {
        List<StockTransactionRecord> stockRecordList = TransactionRecordService.stockTransactionRecordMap.get(tradingDate);
        //总市值
        double totalMarketValue = stockRecordList.parallelStream().mapToDouble(record -> record.getAmount() * getClosePrice(tradingDate, record.getCode(), record.getPrice())).sum();
        List<FundHoldingHistory> fundHoldingHistoryList = stockRecordList.parallelStream().map(record -> {
            FundHoldingHistory fundHoldingHistory = new FundHoldingHistory();
            fundHoldingHistory.setCode(record.getCode());
            fundHoldingHistory.setName(record.getName());
            fundHoldingHistory.setAmount(record.getAmount());
            fundHoldingHistory.setMarketValue(record.getAmount() * getClosePrice(tradingDate, record.getCode(), record.getPrice()));
            fundHoldingHistory.setWeight(fundHoldingHistory.getMarketValue() / totalMarketValue);
            return fundHoldingHistory;
        }).collect(Collectors.toList());

        FundHistory fundHistory = new FundHistory();
        fundHistory.setTradingDate(tradingDate);
        fundHistory.setMarketValue(totalMarketValue);

        double stockBuyMarketValue = stockRecordList.stream().filter(record -> record.getAmount() > 0).mapToDouble(record -> record.getAmount() * record.getPrice() * ExchangeRateService.getExchangeRate(tradingDate, record.getCurrency())).sum();

        fundHistory.setNav(fundHistory.getMarketValue() / stockBuyMarketValue);
        fundHistory.setDailyReturn(fundHistory.getNav() - 1);
        fundHistory.setFundHoldingHistoryList(fundHoldingHistoryList);
        fundHistoryMap.put(tradingDate, fundHistory);
    }

    //人民币价格为价格*汇率，若是取不到当日价格则取默认价格（交易价格）
    private static Double getClosePrice(LocalDate date, String code, Double defaultValue) {
        Double exchangeRate = ExchangeRateService.getExchangeRate(date, TransactionRecordService.getCurrency(code));
        Price price = PriceService.getPrice(date, code);
        if (price != null) {
            return price.getClose() * exchangeRate;
        }
        return defaultValue * exchangeRate;
    }

    //某些债券算市值前需将数量除以100
    private static Double getRealAmount(String code, Double amount) {
        List<String> codeList = Lists.newArrayList("US01609WAQ50 CORP", "US00507VAJ89 CORP", "USU00568AE27 CORP", "XS1611011922 CORP");
        if (codeList.contains(code)) {
            return amount / 100;
        }
        return amount;
    }


}
