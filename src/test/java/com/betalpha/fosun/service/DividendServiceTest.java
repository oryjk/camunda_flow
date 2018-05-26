package com.betalpha.fosun.service;

import com.betalpha.fosun.model.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * created on 2018/5/21
 *
 * @author huzongpeng
 */
@Slf4j
public class DividendServiceTest {

    private static TreeMap<LocalDate, FundHistory> fundHistoryMap = Maps.newTreeMap();

    private static Map<String, Double> bondParValue = Maps.newHashMap();

    private static List<DividendRecord> dividendRecords = Lists.newArrayList();

    static {
        bondParValue.put("US00170F2092 CORP", 50D);
        bondParValue.put("IL0062905489 CORP", 1000D);
    }

    private static void initDividendList() {
        List<LocalDate> tradingDates = TradingDateService.getTradingDates();

        //第一天
        processFirstDay(tradingDates.get(0));

        tradingDates.forEach(tradingDate -> {
            //第一天已处理
            if (tradingDate.equals(tradingDates.get(0))) {
                return;
            }

            FundHistory preFundHistory = fundHistoryMap.get(TradingDateService.getPreTradingDate(tradingDate));
            //前一日持仓
            Map<String, FundHoldingHistory> preHoldingMap = preFundHistory.getFundHoldingHistoryList().stream().collect(Collectors.toMap(FundHoldingHistory::getCode, Function.identity()));
            preHoldingMap.values().stream()
                    .filter(fundHoldingHistory -> SplitService.getSplit(tradingDate, fundHoldingHistory.getCode()) != 1)
                    .forEach(fundHoldingHistory -> {
                DividendRecord dividendRecord = new DividendRecord(
                        tradingDate.toString(),
                        fundHoldingHistory.getCode(),
                        fundHoldingHistory.getName(),
                        SplitService.getType(tradingDate, fundHoldingHistory.getCode()),
                        fundHoldingHistory.getAmount(),
                        fundHoldingHistory.getAmount() * SplitService.getSplit(tradingDate, fundHoldingHistory.getCode()),
                        SplitService.getSplit(tradingDate, fundHoldingHistory.getCode()),
                        0D,
                        TransactionRecordService.getCurrency(fundHoldingHistory.getCode()));
                dividendRecords.add(dividendRecord);
            });
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
            preHoldingMap.values().stream()
                    .filter(fundHoldingHistory -> DividendService.getDividend(tradingDate, fundHoldingHistory.getCode()) != 0)
                    .filter(fundHoldingHistory -> !fundHoldingHistory.getName().contains("债"))
                    .forEach(fundHoldingHistory -> {
                DividendRecord dividendRecord = new DividendRecord(
                        tradingDate.toString(),
                        fundHoldingHistory.getCode(),
                        fundHoldingHistory.getName(),
                        DividendService.getType(tradingDate, fundHoldingHistory.getCode()),
                        fundHoldingHistory.getAmount(),
                        fundHoldingHistory.getAmount(),
                        0D,
                        fundHoldingHistory.getAmount() * bondParValue.getOrDefault(fundHoldingHistory.getCode(), 1D) * DividendService.getDividend(tradingDate, fundHoldingHistory.getCode()),
                        TransactionRecordService.getCurrency(fundHoldingHistory.getCode()));
                dividendRecords.add(dividendRecord);
            });

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

    @Test
    public void getDividendCsv() throws IOException {
        initDividendList();
        generateExcel();
    }

    private void generateExcel() throws IOException {
        File file = new File("Dividend Record.csv");
        file.delete();
        file.createNewFile();
        System.out.println("path:" + file.getAbsolutePath());
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            writer.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
            writer.write("除息日,股票代码,股票名称,股利类型,拆分前股数,拆分比例,拆分后股数,现金红利总额,货币");
            writer.newLine();
            dividendRecords.forEach(dividendRecord -> {
                try {
                    writer.write(
                            dividendRecord.getTradingDate() + ","
                                    + dividendRecord.getCode() + ","
                                    + dividendRecord.getName() + ","
                                    + (dividendRecord.getType() == null ? "" : dividendRecord.getType()) + ","
                                    + doubleToString(dividendRecord.getPreAmount()) + ","
                                    + dividendRecord.getSplitRate() + ","
                                    + doubleToString(dividendRecord.getAmount()) + ","
                                    + doubleToString(dividendRecord.getCash()) + ","
                                    + dividendRecord.getCurrency()
                    );
                    writer.newLine();
                } catch (IOException e) {
                    log.error("write error:", e);
                }
            });
            writer.flush();
        } catch (IOException e) {
            log.error("write file error", e);
        }
    }

    private String doubleToString(Double d) {
        return BigDecimal.valueOf(d).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    static class DividendRecord {
        private String tradingDate;
        private String code;
        private String name;
        private String type;
        private Double preAmount;
        private Double amount;
        private Double splitRate;
        private Double cash;
        private String currency;

        public DividendRecord(String tradingDate, String code, String name, String type, Double preAmount, Double amount, Double splitRate, Double cash, String currency) {
            this.tradingDate = tradingDate;
            this.code = code;
            this.name = name;
            this.type = type;
            this.preAmount = preAmount;
            this.amount = amount;
            this.splitRate = splitRate;
            this.cash = cash;
            this.currency = currency;
        }

        public String getTradingDate() {
            return tradingDate;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public Double getPreAmount() {
            return preAmount;
        }

        public Double getAmount() {
            return amount;
        }

        public Double getSplitRate() {
            return splitRate;
        }

        public Double getCash() {
            return cash;
        }

        public String getCurrency() {
            return currency;
        }
    }
}
