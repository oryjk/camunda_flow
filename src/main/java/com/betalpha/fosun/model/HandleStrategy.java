package com.betalpha.fosun.model;

import lombok.Data;

@Data
public class HandleStrategy {
    private String id;
    private String handleUser;//处理人
    private String handleDepartment;//处理部门
    private Double accessPercent;//0~1.0 通过

    private Boolean oneVotePower;//一票否决
    private Boolean stayResearch;//继续研究
    private Boolean skipStrategy;//跳过步骤

}
