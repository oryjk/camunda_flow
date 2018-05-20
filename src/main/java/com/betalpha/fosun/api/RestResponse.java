package com.betalpha.fosun.api;

import java.io.Serializable;

/**
 * Created by WangRui on 15/05/2018.
 */
public class RestResponse implements Serializable {

    private static final long serialVersionUID = 8708034923610037841L;
    private String errorCode;
    private Object data;

    public RestResponse(String errorCode, Object data) {
        this.errorCode = errorCode;
        this.data = data;
    }

    public RestResponse(Object data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
