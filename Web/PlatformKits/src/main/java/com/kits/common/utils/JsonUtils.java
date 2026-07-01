package com.kits.common.utils;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.parser.Feature;
//import com.alibaba.fastjson.parser.ParserConfig;
//import com.alibaba.fastjson.serializer.SerializerFeature;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @Desc TODO
 * @Date 2020/8/5 11:58
 * @Auth ZUncle
 */
public class JsonUtils {

    final ObjectMapper mapper;

    public JsonUtils() {
        this.mapper = new ObjectMapper();
        //设置为true时，属性名称不带双引号
//        mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
        //反序列化是是否允许属性名称不带双引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //允许单引号包裹key, value
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        ///////////////序列化///////////////////////
        //null的属性不序列化
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ///////////////反序列化//////////////////////
        //当遇到未知属性（没有映射到属性，没有任何setter或者任何可以处理它的handler，是否应该抛出JsonMappingException异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        JsonUtils manager;

        Singleton() {
            this.manager = new JsonUtils();
        }

        JsonUtils getProcessor() {
            return manager;
        }
    }

    public static JsonUtils getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void addAccept(String packStr) {
//        ParserConfig.getGlobalInstance().addAccept(packStr);
    }

    public void setAuto(boolean auto) {
//        ParserConfig.getGlobalInstance().setAutoTypeSupport(auto);
    }

    public static String toJSONStringPretty(Object data, boolean pretty) {
//        return JSON.toJSONString(data, pretty);
        return toJSONString(data);
    }

    /**
     * 1、普通对象的 序列化 和反序列化
     * 2、标准json 和 对象  互转
     * 3、
     *
     * @param data
     * @return
     */

    public static String toJSONStringWriteClassName(Object data) {
//        return JSON.toJSONString(data, SerializerFeature.WriteClassName);
        return toJSONString(data);
    }

    public static String toJSONString(Object obj) {
//        return JSON.toJSONString(obj);
        try {
            return getInstance().mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("can not cast to JSONString.", e);
        }
    }

    public static <T> T toJavaObject(String str, Class<T> c) {
//        JSONObject jsonObject = JSON.parseObject(str, features);
//        return JSON.toJavaObject(jsonObject, c);
        try {
            return getInstance().mapper.readerFor(c).readValue(str);
        } catch (Exception e) {
            throw new RuntimeException("can not cast to JSONString.", e);
        }
    }

    public static <T> T parseObject(String str, TypeReference<T> tr) {
//        return (T) JSON.parseObject(String.valueOf(str), tr.getType(), ParserConfig.global, JSON.DEFAULT_PARSER_FEATURE, features);
        JavaType javaType = getInstance().mapper.constructType(tr.getType());
        try {
            return getInstance().mapper.readValue(str, javaType);
        } catch (Exception e) {
            throw new RuntimeException("can not cast to " + tr, e);
        }
    }

    public static <T> T parseObject(String str, Class<T> c) {
//        return JSON.parseObject(str, c);
        try {
            return getInstance().mapper.readerFor(c).readValue(str);
        } catch (Exception e) {
            throw new RuntimeException("can not cast to " + c.getName(), e);
        }
    }

    public static <T> List<T> parseArray(String str, Class<T> c) {

//        return JSON.parseArray(str, c);
        try {
            TypeReference<List<T>> tr = new TypeReference<List<T>>() {
            };
            JavaType javaType = getInstance().mapper.constructType(tr.getType());
            return getInstance().mapper.readValue(str, javaType);
        } catch (Exception e) {
            throw new RuntimeException("can not cast to " + c.getName(), e);
        }
    }

}
