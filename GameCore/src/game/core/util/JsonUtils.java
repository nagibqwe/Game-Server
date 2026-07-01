package game.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.core.exception.GameException;
import game.core.json.TypeReference;
import java.util.ArrayList;
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
        //设置为true时，属性名称不带双引号九零  一起玩 www.901 75.com
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

    /**
     * java 类转 JSON 字符串
     *
     * @param obj
     * @return
     */
    public static String toJSONString(Object obj, TypeReference<?>... types) {
//        return JSON.toJSONString(obj);
        try {
            if (types.length == 1) {
                JavaType javaType = getInstance().mapper.constructType(types[0].getType());
                return getInstance().mapper.writerFor(javaType).writeValueAsString(obj);
            }
            return getInstance().mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new GameException("can not cast to JSONString.", e);
        }
    }

    /**
     * JSON 字符串 转 java类
     *
     * @param str
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T toJavaObject(String str, Class<T> c) {
//        JSONObject jsonObject = JSON.parseObject(str, features);
//        return JSON.toJavaObject(jsonObject, c);
        try {
            return getInstance().mapper.readerFor(c).readValue(str);
        } catch (Exception e) {
            throw new GameException("can not cast to JSONString.", e);
        }
    }

    /**
     * JSON 字符串 转 java类 （各种容器类型。。。和其他）
     *
     * @param str
     * @param tr
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String str, TypeReference<T> tr) {
//        return (T) JSON.parseObject(String.valueOf(str), tr.getType(), ParserConfig.global, JSON.DEFAULT_PARSER_FEATURE, features);
        JavaType javaType = getInstance().mapper.constructType(tr.getType());
        try {
            return getInstance().mapper.readValue(str, javaType);
        } catch (Exception e) {
            throw new GameException("can not cast to " + tr, e);
        }
    }

    /**
     * JSON 字符串 转 java类
     *
     * @param str
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String str, Class<T> c) {
//        return JSON.parseObject(str, c);
        try {
            return getInstance().mapper.readerFor(c).readValue(str);
        } catch (Exception e) {
            throw new GameException("can not cast to " + c.getName(), e);
        }
    }

    /**
     * 序列化 列表
     *
     * @param str
     * @param c
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String str, Class<T> c) {

//        return JSON.parseArray(str, c);
        try {
            TypeReference<ArrayList<T>> tr = new TypeReference<ArrayList<T>>() {
            };
            JavaType javaType = getInstance().mapper.constructType(tr.getType());
            return getInstance().mapper.readValue(str, javaType);
        } catch (Exception e) {
            throw new GameException("can not cast to " + c.getName(), e);
        }
    }

}
