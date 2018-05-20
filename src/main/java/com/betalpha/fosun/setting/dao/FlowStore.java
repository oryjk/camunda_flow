package com.betalpha.fosun.setting.dao;

import org.springframework.stereotype.Service;

/**
 * Created by WangRui on 18/05/2018.
 */
@Service
public class FlowStore {
    private String flowId;//definitionId

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }
}
