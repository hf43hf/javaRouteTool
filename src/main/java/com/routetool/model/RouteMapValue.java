package com.routetool.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: hef
 * @version: 1.0
 * 路由value
 * @Date: Create in 10:07 上午 2021/1/3
 */
public class RouteMapValue implements Serializable {

    private static final long serialVersionUID = -3740180147983931823L;

    private Object springAgentObj;

    private Method method;

    private List<RouteMethodParam> parameters;

    public Object getSpringAgentObj() {
        return springAgentObj;
    }

    public void setSpringAgentObj(Object springAgentObj) {
        this.springAgentObj = springAgentObj;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<RouteMethodParam> getParameters() {
        return parameters;
    }

    public void setParameters(List<RouteMethodParam> parameters) {
        this.parameters = parameters;
    }
}
