package com.betalpha.fosun.api;

import java.io.Serializable;

public class NodeNameValueMapApi implements Serializable {

    private static final long serialVersionUID = 3412589533198153551L;

    private String name;
    private String value;

    public NodeNameValueMapApi(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
