package com.betalpha.fosun.model;

import lombok.Data;

import java.util.List;

@Data
public class Node {
    private String id;
    private String preNodeId;
    private List<String> nextNodeIds;
    private InvestmentCommittee investmentCommittee;//节点部门信息
    private NodeProperties nodeProperties;//节点属性-常规
    private HandleStrategy handleStrategy;//节点属性-处理人
}
