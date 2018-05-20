package com.betalpha.fosun.api;

import com.betalpha.fosun.model.FusonUser;
import jersey.repackaged.com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class OrganizationStructureApi {
    private String id;
    private String name;//债券委员会,投决会,内审会
    private String vetoPowerUser;//一票否决
    private String level;//公司级别,部门级别
    private List<FusonUser> userGroup;//用户群组
    private List<OrganizationStructureApi> children = Lists.newArrayList();
}
