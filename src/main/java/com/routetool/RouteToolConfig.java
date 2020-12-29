package com.routetool;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: hef
 * @version: 1.0
 * 配置文件
 * @Date: Create in 11:49 上午 2020/12/29
 */
@ConfigurationProperties("route-tool")
@Component
public class RouteToolConfig {

    private String basePackage;

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
