package com.betalpha.fosun.model;


import lombok.Data;

import java.util.List;

@Data
public class NodeProperties {
    private String id;
    private String bondType;//高收益债和高等级债
    private Double accessPercent;//0~1.0通过设置
    private List<String> permissionsOption;//主体发行人发起，债项发起，开启投票 (e)
}
