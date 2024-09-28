package com.ruite.app.pet.translator.utils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private static final Gson sGson = new Gson();

    private JsonUtil() {
    }


    /**
     * @param json json字符串
     * @param clz  解析的类型clz
     * @param <T>  类型
     * @return json转出来的对象
     */
    public static <T> T jsonToObject(@NonNull String json, Class<T> clz) {
        return sGson.fromJson(json, clz);
    }

    /**
     * @param json    json字符串
     * @param typeOfT 解析的类型
     * @param <T>     类型
     * @return json转出来的对象
     */
    public static <T> T jsonToObject(@NonNull String json, Type typeOfT) {
        return sGson.fromJson(json, typeOfT);
    }

    /**
     * @param json json对象
     * @param clz  解析的类型clz
     * @param <T>  类型
     * @return json转出来的对象
     */
    public static <T> T jsonToObject(@NonNull JsonElement json, Class<T> clz) {
        return sGson.fromJson(json, clz);
    }

    /**
     * @param json    json对象
     * @param typeOfT 解析的类型
     * @param <T>     类型
     * @return json转出来的对象
     */
    public static <T> T jsonToObject(@NonNull JsonElement json, Type typeOfT) {
        return sGson.fromJson(json, typeOfT);
    }

    /**
     * @param json json字符串
     * @param clz  解析的类型clz
     * @param <T>  类型
     * @return json转出来的集合
     */
    public static <T> List<T> jsonToList(@NonNull String json, Class<T> clz) {
        List<T> result = new ArrayList<>();
        try {
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();

            for (JsonElement element : jsonArray) {
                T t = sGson.fromJson(element, clz);
                result.add(t);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * @param json json字符串
     * @param clz  解析的类型clz
     * @param <T>  类型
     * @return json转出来的集合
     */
    public static <T> List<T> jsonToList(@NonNull JsonElement json, Class<T> clz) {
        JsonArray jsonArray = json.getAsJsonArray();

        List<T> result = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            T t = sGson.fromJson(element, clz);
            result.add(t);
        }

        return result;
    }

    /**
     * @param object 数据结构
     * @return json格式的数据结构
     */
    public static String toJson(@NonNull Object object) {
        return sGson.toJson(object);
    }

    /**
     * 复制对象（深拷贝）
     *
     * @param obj 待复制对象
     * @param clz 对象class
     * @param <T> 对象类型
     * @return 深拷贝复制出来的对象
     */
    public static <T> T copyObj(@NonNull T obj, Class<T> clz) {
        return JsonUtil.jsonToObject(toJson(obj), clz);
    }

    /**
     * 复制集合（深拷贝）
     *
     * @param obj 待复制集合
     * @param clz 对象class
     * @param <T> 对象类型
     * @return 深拷贝复制出来的集合
     */
    public static <T> List<T> copyList(@NonNull T obj, Class<T> clz) {
        return JsonUtil.jsonToList(toJson(obj), clz);
    }

    /**
     * 获取json对象中key对应的值
     *
     * @param object json对象
     * @param key    待取出的key
     * @return json对象中的值
     */
    public static String getString(@NonNull JsonObject object, @NonNull String key) {
        if (object.has(key)) {
            try {
                return object.get(key).getAsString();
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
        }
        return "";
    }

    /**
     * 获取json对象中key对应的值
     *
     * @param object json对象
     * @param key    待取出的key
     * @return json对象中的值
     */
    public static long getLong(@NonNull JsonObject object, @NonNull String key) {
        if (object.has(key)) {
            try {
                return object.get(key).getAsLong();
            } catch (Exception ex) {
                ex.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    /**
     * 获取json对象中key对应的值
     *
     * @param object json对象
     * @param key    待取出的key
     * @return json对象中的值
     */
    public static JsonObject getObject(@NonNull JsonObject object, @NonNull String key) {
        if (object.has(key)) {
            try {
                return object.get(key).getAsJsonObject();
            } catch (Exception ex) {
                ex.printStackTrace();
                return new JsonObject();
            }
        }
        return new JsonObject();
    }


    /**
     * 该类提供格式化JSON字符串的方法。
     * 该类的方法formatJson将JSON字符串格式化，方便查看JSON数据。
     * <p>例如：
     * <p>JSON字符串：["yht","xzj","zwy"]
     * <p>格式化为：
     * <p>[
     * <p>     "yht",
     * <p>     "xzj",
     * <p>     "zwy"
     * <p>]
     *
     * <p>使用算法如下：
     * <p>对输入字符串，追个字符的遍历
     * <p>1、获取当前字符。
     * <p>2、如果当前字符是前方括号、前花括号做如下处理：
     * <p>（1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
     * <p>（2）打印：当前字符。
     * <p>（3）前方括号、前花括号，的后面必须换行。打印：换行。
     * <p>（4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
     * <p>（5）进行下一次循环。
     * <p>3、如果当前字符是后方括号、后花括号做如下处理：
     * <p>（1）后方括号、后花括号，的前面必须换行。打印：换行。
     * <p>（2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
     * <p>（3）打印：当前字符。
     * <p>（4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
     * <p>（5）继续下一次循环。
     * <p>4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
     * <p>5、打印：当前字符。
     *
     * @author yanghaitao
     * @version [版本号, 2014年9月29日]
     */
    public static class JsonFormatTool {
        /**
         * 单位缩进字符串。
         */
        private static final String SPACE = "    ";

        /**
         * 返回格式化JSON字符串。
         *
         * @param json 未格式化的JSON字符串。
         * @return 格式化的JSON字符串。
         */
        public String formatJson(String json) {
            StringBuffer result = new StringBuffer();

            int length = json.length();
            int number = 0;
            char key = 0;

            //遍历输入字符串。
            for (int i = 0; i < length; i++) {
                //1、获取当前字符。
                key = json.charAt(i);

                //2、如果当前字符是前方括号、前花括号做如下处理：
                if ((key == '[') || (key == '{')) {
                    //（1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
                    if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
                        result.append('\n');
                        result.append(indent(number));
                    }

                    //（2）打印：当前字符。
                    result.append(key);

                    //（3）前方括号、前花括号，的后面必须换行。打印：换行。
                    result.append('\n');

                    //（4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                    number++;
                    result.append(indent(number));

                    //（5）进行下一次循环。
                    continue;
                }

                //3、如果当前字符是后方括号、后花括号做如下处理：
                if ((key == ']') || (key == '}')) {
                    //（1）后方括号、后花括号，的前面必须换行。打印：换行。
                    result.append('\n');

                    //（2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                    number--;
                    result.append(indent(number));

                    //（3）打印：当前字符。
                    result.append(key);

                    //（4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
                    if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
                        result.append('\n');
                    }

                    //（5）继续下一次循环。
                    continue;
                }

                //4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
                if ((key == ',')) {
                    result.append(key);
                    result.append('\n');
                    result.append(indent(number));
                    continue;
                }

                //5、打印：当前字符。
                result.append(key);
            }

            return result.toString();
        }

        /**
         * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
         *
         * @param number 缩进次数。
         * @return 指定缩进次数的字符串。
         */
        private String indent(int number) {
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < number; i++) {
                result.append(SPACE);
            }
            return result.toString();
        }
    }

}
