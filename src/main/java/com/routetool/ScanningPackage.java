package com.routetool;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.routetool.annotation.RouteClass;
import com.routetool.annotation.RouteMethod;
import com.routetool.model.RouteMapValue;
import com.routetool.model.RouteMethodParam;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author: hef
 * @version: 1.0
 * 扫描需要路由的类
 * @Date: Create in 10:26 下午 2020/12/28
 */
@Component
public class ScanningPackage implements ApplicationRunner {

    @Autowired
    private RouteToolConfig routeToolConfig;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        ScanSupport.getClasspath(routeToolConfig.getBasePackage()).stream().forEach(classObj -> {
            boolean hasRouteClass = classObj.isAnnotationPresent(RouteClass.class);
            if (hasRouteClass) {
                RouteClass routeClass = classObj.getAnnotation(RouteClass.class);
                List<String> routeHeadList = annotationValueParsing(routeClass.value());
                StringBuilder routeHead = new StringBuilder().append(routeListToString(routeHeadList));

                Method[] methods = classObj.getMethods();
                Arrays.stream(methods).forEach(method -> {
                    boolean hasRouteMethod = method.isAnnotationPresent(RouteMethod.class);
                    if (hasRouteMethod) {
                        RouteMethod routeMethod = method.getAnnotation(RouteMethod.class);
                        List<String> routeBodyList = annotationValueParsing(routeMethod.value());
                        StringBuilder methodAllRoute = new StringBuilder().append(routeHead);
                        methodAllRoute.append(routeListToString(routeBodyList));

                        String routeKey = methodAllRoute.toString();
                        if (RouteUtil.getRouteMap().containsKey(routeKey)) {
                            throw new RuntimeException("请检查路由url,路由url重复!");
                        }

                        /** 获取方法上的参数 **/
                        List<RouteMethodParam> methodParams = new ArrayList<>();
                        Parameter[] parameters = method.getParameters();
                        Arrays.stream(parameters).forEach(param -> {
                            RouteMethodParam routeMethodParam = new RouteMethodParam();
                            routeMethodParam.setParamName(param.getName());
                            routeMethodParam.setParamType(param.getType());

                            methodParams.add(routeMethodParam);
                        });

                        RouteMapValue routeMapValue = new RouteMapValue();
                        Class<?> className = null;
                        try {
                            className = Class.forName(classObj.getTypeName());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        routeMapValue.setSpringAgentObj(SpringUtil.getBean(className));
                        routeMapValue.setMethod(method);
                        routeMapValue.setParameters(methodParams);
                        RouteUtil.getRouteMap().put(routeKey, routeMapValue);
                    }
                });
            }
        });
    }

    /**
     * 解析路径
     *
     * @param annotationValue
     * @return java.util.List<java.lang.String>
     * @Description: TODO(解析路径)
     * @author hef
     * @version 1.0
     * @date 2020/12/29 5:04 下午
     */
    private List<String> annotationValueParsing(String annotationValue) {
        return Arrays.stream(annotationValue.split("/")).filter(routePath -> Strings.isNotBlank(routePath)).collect(Collectors.toList());
    }

    /**
     * 路径集合转字符串
     *
     * @param
     * @return java.lang.String
     * @Description: TODO(路径集合转字符串)
     * @author hef
     * @version 1.0
     * @date 2020/12/29 5:11 下午
     */
    private String routeListToString(List<String> routeList) {
        return routeList.parallelStream().collect(Collectors.joining("/", "/", ""));
    }
}
