package com.routetool.model;

import java.io.Serializable;

/**
 * @Author: hef
 * @version: 1.0
 * 客户端请求结构
 * @Date: Create in 10:56 上午 2021/1/3
 */
public class ClientParameter<T> implements Serializable {

    private static final long serialVersionUID = 6648504460936511924L;

    private String path;

    private T paramObj;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public T getParamObj() {
        return paramObj;
    }

    public void setParamObj(T paramObj) {
        this.paramObj = paramObj;
    }
}
