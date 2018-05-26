package com.betalpha.fosun.api.process;

import java.io.Serializable;

/**
 * Created by WangRui on 21/05/2018.
 */
public class Condition implements Serializable {

    private static final long serialVersionUID = 3046589704504474829L;
    private String conditionExpression;
    private String name;

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Condition() {
    }

    public Condition(String conditionExpression, String name) {
        this.conditionExpression = conditionExpression;
        this.name = name;
    }
}
