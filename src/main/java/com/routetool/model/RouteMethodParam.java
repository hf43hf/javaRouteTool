package com.routetool.model;

import java.io.Serializable;

/**
 * @Author: hef
 * @version: 1.0
 * 路由方法参数
 * @Date: Create in 10:35 上午 2021/1/3
 */
public class RouteMethodParam implements Serializable {

    private static final long serialVersionUID = 1621476081758468580L;

    private String paramName;

    private Class paramType;

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Class getParamType() {
        return paramType;
    }

    public void setParamType(Class paramType) {
        this.paramType = paramType;
    }
}
