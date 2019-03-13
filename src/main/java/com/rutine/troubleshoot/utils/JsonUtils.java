package com.rutine.troubleshoot.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 功能说明: json工具类
 * <p>
 * <h3>Here is an example:</h3>
 * <pre>
 *  // 将json通过类型转换成对象
 *    {@link JsonUtils JsonUtils}.fromJson("{\"username\":\"username\", \"password\":\"password\"}", User.class);
 * </pre>
 * <pre>
 *  // 传入转换的引用类型
 *    {@link JsonUtils JsonUtils}.fromJson("[
 *            {\"username\":\"username\", \"password\":\"password\"},
 *            {\"username\":\"username\", \"password\":\"password\"}
 *      ]", new TypeReference&lt;List&lt;User&gt;&gt;);
 * </pre>
 * <pre>
 *  // 将对象转换成json
 *    {@link JsonUtils JsonUtils}.toJson(user);
 * </pre>
 * <pre>
 *  // 将对象转换成json, 可以设置输出属性
 *    {@link JsonUtils JsonUtils}.toJson(user, {@link Include Include.ALWAYS});
 * </pre>
 * <pre>
 *  // 将对象转换成json, 传入配置对象
 *    {@link ObjectMapper ObjectMapper} mapper = new ObjectMapper();
 *  mapper.setSerializationInclusion({@link Include Include.ALWAYS});
 *  mapper.setDateFormat(new {@link SimpleDateFormat SimpleDateFormat}("yyyy-MM-dd HH:mm:ss"));
 *  mapper.configure({@link DeserializationFeature DeserializationFeature}.FAIL_ON_UNKNOWN_PROPERTIES, false);
 *    {@link JsonUtils JsonUtils}.toJson(user, mapper);
 * </pre>
 *
 * @author rutine
 * @version 2.0.0
 * @time Sep 22, 2014 9:17:19 PM
 * @see JsonUtils JsonUtils
 * @see Feature Feature
 * @see ObjectMapper ObjectMapper
 * @see Include include
 * @see IOException IOException
 * @see SimpleDateFormat SimpleDateFormat
 */
public class JsonUtils {
    private static final Object[] EMPTY_OBJECT = new Object[0];

    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper mapper = new ObjectMapper(); // 一个变量对象, 提供性能.


    /**
     * json字符串转换成pojo对象
     * <pre>
     *    {@link JsonUtils JsonUtils}.fromJson("{\"username\":\"username\", \"password\":\"password\"}", User.class);
     * </pre>
     *
     * @param json      json字符串
     * @param pojoClass pojo类型
     * @return 返回pojo对象
     * @throws IOException
     */
    public static <T> T fromJson(String json, Class<T> pojoClass) {
        try {
            return pojoClass.equals(String.class) ? (T) json : mapper.readValue(json, pojoClass);
        } catch (JsonParseException e) {
            logger.warn("1. json转换异常", e);
        } catch (JsonMappingException e) {
            logger.warn("2. json转换异常", e);
        } catch (IOException e) {
            logger.warn("3. json转换异常", e);
        }

        return null;
    }

    /**
     * json转换成集合对象
     * <pre>
     *    {@link JsonUtils JsonUtils}.fromJson("[
     *            {\"username\":\"username\", \"password\":\"password\"},
     *            {\"username\":\"username\", \"password\":\"password\"}
     *      ]", new TypeReference&lt;List&lt;User&gt;&gt;);
     * </pre>
     *
     * @param json          json字符串
     * @param typeReference 指明集合元素对应的类型
     * @return 返回对象
     * @throws IOException
     */
    public static <T> T fromJson(String json, TypeReference<?> typeReference) {
        try {
            return (T) (typeReference.getType().equals(String.class) ? json : mapper.readValue(json, typeReference));
        } catch (JsonParseException e) {
            logger.warn("1. json转换异常", e);
        } catch (JsonMappingException e) {
            logger.warn("2. json转换异常", e);
        } catch (IOException e) {
            logger.warn("3. json转换异常", e);
        }

        return null;
    }

    /**
     * 序列化为json字符串
     * <p>
     * <pre>
     *    {@link JsonUtils JsonUtils}.toJson(user);
     * </pre>
     *
     * @param object 对象
     * @return json字符串
     * @throws IOException
     */
    public static <T> String toJson(T object) {
        try {
            return object instanceof String ? (String) object : mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.warn("序列化为json字符串异常!", e);
        }

        return null;
    }

    /**
     * 序列化为json字符串
     * <p>
     * <pre>
     *    {@link JsonUtils JsonUtils}.toJson(user);
     * </pre>
     *
     * @param object     对象
     * @param dateFormat 序列化日期格式: "yyyy-MM-dd HH:mm:ss"
     * @return 返回json字符串
     * @throws IOException
     */
    public static <T> String toJson(T object, String dateFormat) {
        try {
            return object instanceof String ? (String) object
                    : mapper.writer(new SimpleDateFormat(dateFormat)).writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.warn("序列化为json字符串异常!", e);
        }

        return null;
    }

    /**
     * 序列化为json字符串, 可以设置输出属性
     * <p>
     * <pre>
     *    {@link JsonUtils JsonUtils}.toJson(user, {@link Include Include.ALWAYS});
     * </pre>
     * <p>
     * {@link Include include 枚举对象}
     * <ul>
     * <li>{@link Include Include.ALWAYS 全部列入}</li>
     * <li>{@link Include Include.NON_DEFAULT 字段和对象默认值相同的时候不会列入}</li>
     * <li>{@link Include Include.NON_EMPTY 字段为NULL或者""的时候不会列入}</li>
     * <li>{@link Include Include.NON_NULL 字段为NULL时候不会列入}</li>
     * </ul>
     *
     * @param object  对象
     * @param include 传入一个枚举值, 设置输出属性
     * @return 返回json字符串
     * @throws IOException
     */
    public static <T> String toJson(T object, Include include) {
        if (object instanceof String) {
            return (String) object;
        } else {
            try {
                ObjectMapper customMapper = newMapper(include);
                return customMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                logger.warn("转换json字符串异常!", e);
            }
        }

        return null;
    }

    /**
     * 将对象转换成json, 传入配置对象
     * <p>
     * <pre>
     *    {@link ObjectMapper ObjectMapper mapper = new ObjectMapper()};
     *    mapper.setSerializationInclusion({@link Include Include.ALWAYS});
     *    mapper.setDateFormat(new {@link SimpleDateFormat SimpleDateFormat("yyyy-MM-dd HH:mm:ss")});
     *    mapper.configure({@link DeserializationFeature DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES}, false);
     *    {@link JsonUtils JsonUtils}.toJson(user, mapper);
     * </pre>
     *
     * @param object 对象
     * @param mapper 配置对象
     * @return 返回json字符串
     * @throws IOException
     */
    public static <T> String toJson(T object, ObjectMapper mapper) {
        if (null != mapper) {
            if (object instanceof String) {
                return (String) object;
            } else {
                try {
                    return mapper.writeValueAsString(object);
                } catch (JsonProcessingException e) {
                    logger.warn("转换json字符串异常!", e);
                }
            }
        }

        return null;
    }

    /**
     * 通过Include创建ObjectMapper对象 <br/>
     * <br/>
     * {@link Include include 枚举对象}
     * <ul>
     * <li>{@link Include include.ALWAYS 全部列入}</li>
     * <li>{@link Include include.NON_DEFAULT 字段和对象默认值相同的时候不会列入}</li>
     * <li>{@link Include include.NON_EMPTY 字段为NULL或者""的时候不会列入}</li>
     * <li>{@link Include include.NON_NULL 字段为NULL时候不会列入}</li>
     * </ul>
     *
     * @param include 传入一个枚举值, 设置输出属性
     * @return 返回ObjectMapper对象
     */
    public static ObjectMapper newMapper(Include include) {
        ObjectMapper customMapper = new ObjectMapper();
        // 设置输出时包含属性的风格
        customMapper.setSerializationInclusion(include);
        // 所有日期格式都统一为以下样式
        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        customMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return customMapper;
    }

    /**
     * @param data
     * @param clazz
     * @return
     * @author rutine
     * @time Oct 4, 2012 5:06:47 PM
     */
    public static String toJson(List<?> data, Class<?> clazz) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                try {
                    jsonBuilder.append("{");
                    Class<?> c = Class.forName(clazz.getName());
                    Object o = c.newInstance();
                    o = data.get(i);
                    Field[] fields = c.getDeclaredFields();
                    Method[] methods = c.getDeclaredMethods();

                    if (fields != null && fields.length > 0) {
                        for (int j = 0; j < fields.length; j++) {
                            jsonBuilder.append(fields[j].getName() + ":");
                            for (int t = 0; t < methods.length; t++) {
                                String temp = "get"
                                        + fields[j].getName().toUpperCase().substring(0, 1)
                                        + fields[j].getName().substring(1);

                                logger.debug(" ####################" + temp);

                                if (methods[t].getName().equals(temp)) {
                                    jsonBuilder.append("'");
                                    Object invResult = methods[t].invoke(o, EMPTY_OBJECT);
                                    if (null != invResult) {
                                        jsonBuilder.append(invResult.toString());
                                    }
                                    jsonBuilder.append("'");
                                    if (j < fields.length - 1) jsonBuilder.append(",");
                                }
                            }
                        }
                    }
                    jsonBuilder.append("} ");
                    if (i < data.size() - 1) jsonBuilder.append(",");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        jsonBuilder.append("] ");

        logger.debug(jsonBuilder.toString());

        return jsonBuilder.toString();
    }

    /**
     * 对List集合转换成指定类型的id和name两个属性的字符串
     *
     * @param data      list集合
     * @param pojoCalss 类型
     * @param idField
     * @param nameField
     * @return [[idField: nameField], ...]
     * @author rutine
     * @time Oct 4, 2012 5:34:58 PM
     */
    public static String toJson(List<?> data, Class<?> pojoCalss, String idField, String nameField) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                try {
                    jsonBuilder.append("[");
                    Class<?> c = Class.forName(pojoCalss.getName());
                    Object o = c.newInstance();
                    o = data.get(i);
                    Method[] methods = c.getDeclaredMethods();
                    String idMethod = "get"
                            + idField.toUpperCase().substring(0, 1)
                            + idField.substring(1);
                    String nameMethod = "get"
                            + nameField.toUpperCase().substring(0, 1)
                            + nameField.substring(1);
                    String idValue = "";
                    String nameValue = "";

                    for (int t = 0; t < methods.length; t++) {
                        if (methods[t].getName().equals(idMethod)) {
                            Object invResult = methods[t].invoke(o, EMPTY_OBJECT);
                            if (null != invResult) {
                                idValue = invResult.toString();
                            }
                        }
                        if (methods[t].getName().equals(nameMethod)) {
                            Object invResult = methods[t].invoke(o, EMPTY_OBJECT);
                            if (null != invResult) {
                                nameValue = "'" + invResult.toString() + "'";
                            }
                        }
                    }
                    jsonBuilder.append(idValue);
                    jsonBuilder.append(",");
                    jsonBuilder.append(nameValue);
                    jsonBuilder.append("]");
                    if (i < data.size() - 1) jsonBuilder.append(",");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        jsonBuilder.append("] ");

        logger.debug(jsonBuilder.toString());

        return jsonBuilder.toString();
    }


    /**
     * 根据表列将字符串数组格式化为json对象
     *
     * @param obj
     * @param tableCols
     * @return
     * @author rutine
     * @time Oct 5, 2012 5:47:39 PM
     */
    public static String formatBytableCols(Object[] obj, String[] tableCols) {
        StringBuilder result = new StringBuilder();
        result.append("{");
        if (obj != null && obj.length > 0) {
            for (int i = 0; i < obj.length; i++) {
                try {
                    result.append(tableCols[i]);
                    result.append(":'");
                    if (obj[i] instanceof String) {
                        String s = null;
                        if (obj[i].toString() != null) {
                            s = obj[i].toString().replace("\"", "\\\"")
                                    .replace("'", "\\'").replace("\n", "<br/>")
                                    .replace("\b", "\\b").replace("\f", "\\f")
                                    .replace("\r", "").replace("\t", "");
                        }
                        result.append(s);
                    } else if (obj[i] instanceof Integer) {
                        result.append((Integer) obj[i]);
                    } else if (obj[i] instanceof Long) {
                        result.append((Long) obj[i]);
                    } else if (obj[i] instanceof Double) {
                        result.append((Double) obj[i]);
                    } else if (obj[i] instanceof java.sql.Date) {
                        result.append((java.sql.Date) obj[i]);
                    } else if (obj[i] instanceof Float) {
                        result.append((Float) obj[i]);
                    }
                    result.append("'");

                    if (i < obj.length - 1) {
                        result.append(",");
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
        result.append("}");

        logger.debug(result.toString());

        return result.toString();
    }

    /**
     * 根据表列将集合格式化为json对象
     *
     * @param list
     * @param tableCols
     * @return
     * @author rutine
     * @time Oct 5, 2012 5:49:46 PM
     */
    public static String formatBytableCols(List<Object[]> list, String[] tableCols) {
        StringBuilder result = new StringBuilder();
        result.append("[");
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Object[] obj = (Object[]) list.get(i);
                result.append(formatBytableCols(obj, tableCols));
                if (i < list.size() - 1) result.append(",");
            }
        }
        result.append("]");

        return result.toString();
    }

    /**
     * 创建一个json对象
     *
     * @return {@link ObjectNode}
     * @author rutine
     * @time May 12, 2013 7:12:41 PM
     */
    public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }
}
