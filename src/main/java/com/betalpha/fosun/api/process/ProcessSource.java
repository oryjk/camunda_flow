package com.betalpha.fosun.api.process;

import java.io.Serializable;

/**
 * Created by WangRui on 15/05/2018.
 */

public class ProcessSource implements Serializable {

    private static final long serialVersionUID = 5164783702332382580L;
    private String key;
    private String name;
    private String source;

    public ProcessSource(String key, String name, String source) {
        this.key = key;
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
