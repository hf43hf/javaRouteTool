package com.routetool;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author lixin(1309244704 @ qq.com)
 * @version V1.0
 * @ClassName: JsonUtil
 * @Description: TODO(Json工具类)
 * @date 2018年8月18日 下午5:04:57
 */
public class JsonUtil {

    private static Logger _logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        OBJECT_MAPPER.setDateFormat(sdf);
        OBJECT_MAPPER.setTimeZone(TimeZone.getDefault());
        OBJECT_MAPPER.setSerializationInclusion(Include.ALWAYS);
        // 设置输入时忽略JSON字符串中存在而Java对象实际没有的属性
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            _logger.error(e.getMessage(), e);
            return "";
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            if (Strings.isNotBlank(json)) {
                return OBJECT_MAPPER.readValue(json, clazz);
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(String json, Class<T> clazz, boolean isFilterBlanks) {
        if (isFilterBlanks) {
            OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        }
        return toObject(json, clazz);
    }

    /**
     * 将Json字符串转换成List
     *
     * @param clazz
     * @param json
     * @return
     */
    public static <T> List<T> toObjectList(String json, Class<T> clazz) {
        JavaType javaType = getCollectionType(ArrayList.class, clazz);
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 将JSON字符串转为Map
     *
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(String json) {
        try {
            @SuppressWarnings("unchecked")
            //转成map
            Map<String, Object> maps = OBJECT_MAPPER.readValue(json, Map.class);
            return maps;
        } catch (Exception e) {
            return null;
        }
    }


}
