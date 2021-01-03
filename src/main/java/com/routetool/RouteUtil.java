package com.routetool;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.routetool.model.ClientParameter;
import com.routetool.model.RouteMapValue;
import com.routetool.model.RouteMethodParam;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: hef
 * @version: 1.0
 * 路由工具
 * @Date: Create in 10:50 上午 2021/1/3
 */
public class RouteUtil {

    private static final Map<String, RouteMapValue> routeMap = new ConcurrentHashMap<>();

    public static Map<String, RouteMapValue> getRouteMap() {
        return routeMap;
    }

    public static void run(String clientMsg) {
        if (!JSONUtil.isJson(clientMsg)) {
            throw new RuntimeException("参数解析失败");
        }

        ClientParameter clientParameter = JsonUtil.toObject(clientMsg, ClientParameter.class);

        String path = ScanningPackage.routeListToString(ScanningPackage.annotationValueParsing(clientParameter.getPath()));

        RouteMapValue routeMapValue = routeMap.get(path);
        if (routeMapValue == null) {
            throw new RuntimeException("RouteUtil参数解析错误");
        }

        List<RouteMethodParam> parameters = routeMapValue.getParameters();
        if (CollectionUtil.isEmpty(parameters)) {
            runMethod(routeMapValue);
        } else {
            runMethod(routeMapValue, clientParameter.getParamObj());
        }
    }

    /**
     * 执行业务方法
     *
     * @param routeMapValue
     * @return void
     * @Description: TODO(执行业务方法)
     * @author hef
     * @version 1.0
     * @date 2021/1/3 11:14 上午
     */
    private static void runMethod(RouteMapValue routeMapValue) {
        try {
            routeMapValue.getMethod().invoke(routeMapValue.getSpringAgentObj(), null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行业务方法
     *
     * @param routeMapValue
     * @param clientParam
     * @return void
     * @Description: TODO(执行业务方法)
     * @author hef
     * @version 1.0
     * @date 2021/1/3 11:15 上午
     */
    private static void runMethod(RouteMapValue routeMapValue, Object clientParam) {
        /** 解析客户端请求参数 **/
        List args = parseParam(routeMapValue, clientParam);

        try {
            routeMapValue.getMethod().invoke(routeMapValue.getSpringAgentObj(), args.toArray());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析客户端请求参数
     *
     * @param routeMapValue
     * @param clientParam
     * @return java.util.List
     * @Description: TODO()
     * @author hef
     * @version 1.0
     * @date 2021/1/3 1:53 下午
     */
    private static List parseParam(RouteMapValue routeMapValue, Object clientParam) {
        String clientParamJson = JSONUtil.toJsonStr(clientParam);
        Map<String, Object> clientParamMap = JSONUtil.toBean(clientParamJson, HashMap.class);

        List routeParamList = new ArrayList();
        routeMapValue.getParameters().stream().forEach(routeParam -> {
            clientParamMap.keySet().stream().forEach(clientMapKey -> {
                if (routeParam.getParamName().equals(clientMapKey)) {
                    Object clientMapValue = clientParamMap.get(clientMapKey);
                    String clientMapJsonValue = null;
                    if (clientMapValue.getClass().equals(String.class)) {
                        clientMapJsonValue = (String) clientMapValue;
                    } else {
                        clientMapJsonValue = JsonUtil.toJson(clientMapValue);
                    }

                    Object routeParamValue = null;
                    if (!JSONUtil.isJson(clientMapJsonValue)) {
                        routeParamValue = parseParamValue(routeParam.getParamType(), clientMapJsonValue);
                    } else {
                        routeParamValue = JsonUtil.toObject(clientMapJsonValue, routeParam.getParamType());
                    }


                    routeParamList.add(routeParamValue);
                }
            });
        });
        return routeParamList;
    }

    private static Object parseParamValue(Class type, String value) {
        Object result = null;
        if (String.class.equals(type)) {
            result = String.valueOf(value);
        } else if (int.class.equals(type) || Integer.class.equals(type)) {
            result = Integer.parseInt(value);
        } else if (long.class.equals(type) || Long.class.equals(type)) {
            result = Long.parseLong(value);
        }
        return result;
    }
}
