package com.betalpha.fosun.model;

import lombok.Data;

import java.util.List;

@Data
public class InvestmentCommittee {//部门
    private String id;
    private String name;//债券委员会,投决会,内审会
    private String parentId;
    private String vetoPowerUser;//一票否决
    private String level;//公司级别,部门级别
    private List<FusonUser> userGroup;//用户群组

}