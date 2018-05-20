package com.betalpha.fosun.api.process;

import java.io.Serializable;

/**
 * Created by WangRui on 15/05/2018.
 */
public class CreateSuccess implements Serializable {

    private static final long serialVersionUID = -3819182987124445558L;
    private String id;
    private String name;

    public CreateSuccess(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
