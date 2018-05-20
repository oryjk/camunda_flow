package com.betalpha.fosun.api.process;

import java.io.Serializable;

/**
 * Created by WangRui on 15/05/2018.
 */

public class StartParameter implements Serializable {

    private static final long serialVersionUID = -5953385420094156478L;
    /**
     * definition key/id
     */
    private String processKey;
    private String isinId;
    private String userId;

    public StartParameter(String processKey, String isinId, String userId) {
        this.processKey = processKey;
        this.isinId = isinId;
        this.userId = userId;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getIsinId() {
        return isinId;
    }

    public void setIsinId(String isinId) {
        this.isinId = isinId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
