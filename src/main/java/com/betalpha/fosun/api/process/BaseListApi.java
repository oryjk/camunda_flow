package com.betalpha.fosun.api.process;

import java.io.Serializable;
import java.util.List;

/**
 * created on 2018/5/16
 *
 * @author huzongpeng
 */
public class BaseListApi<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 5164783702332382580L;
    private List<T> data;

    public BaseListApi(List<T> data) {
        this.data = data;
    }

    public BaseListApi() {
    }

}
