package com.betalpha.fosun.api.process;

import java.io.Serializable;

/**
 * Created by WangRui on 15/05/2018.
 */
public class StartSuccess implements Serializable {
    private static final long serialVersionUID = 5919570339647399806L;
    private String processInstanceId;

    public StartSuccess(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
