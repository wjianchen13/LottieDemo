package com.example.lottiedemo.rich.other;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/12/24
 *     desc  : 对象相关工具类
 * </pre>
 */
public final class ObjectUtils {

    private ObjectUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static Class<?> getGenericClass(@Nullable Object o) {
        if (o != null) {
            try {
                ParameterizedType parameterizedType = (ParameterizedType) o.getClass().getGenericSuperclass();
                return (Class<?>) parameterizedType.getActualTypeArguments()[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 是否内部类
     * @param clazz
     * @return
     */
    public static boolean isInnerClass(Class<?> clazz){
        return clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers());
    }

    public static boolean isInnerClass(Object o){
        return o != null && isInnerClass(o.getClass());
    }

    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return {@code true}: 为空<br>{@code false}: 不为空
     */
    private static boolean isEmptyEx(final Object obj){
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SimpleArrayMap && ((SimpleArrayMap) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        if (obj instanceof LongSparseArray && ((LongSparseArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (obj instanceof android.util.LongSparseArray
                    && ((android.util.LongSparseArray) obj).size() == 0) {
                return true;
            }
        }
        if(obj instanceof JSONObject){
            return ((JSONObject)obj).length() <= 0;
        }
        if(obj instanceof JSONArray){
            return ((JSONArray)obj).length() <= 0;
        }
        return false;
    }

    public static boolean isEmpty(final Object... objects) {
        if(isEmptyEx(objects))
            return true;

        for (Object obj : objects){
            if(isEmptyEx(obj))
                return true;
        }

        return false;
    }

    /**
     * 判断对象是否非空
     *
     * @param objects 对象
     * @return {@code true}: 非空<br>{@code false}: 空
     */
    public static boolean isNotEmpty(final Object... objects) {
        return !isEmpty(objects);
    }

    public static boolean isNull(final Object... objects){
        if(isEmpty(objects))
            return true;

        for (Object obj : objects){
            if (obj == null)
                return true;
        }

        return false;
    }

    public static boolean isNotNull(final Object... objects){
        return !isNull(objects);
    }

    public static boolean isJSONEmpty(String json) {
        return TextUtils.isEmpty(json) || "{}".equals(json);
    }

    public static boolean isJSONObject(String s){
        if (!TextUtils.isEmpty(s) && s.startsWith("{") && s.endsWith("}")){
            try {
                new JSONObject(s);
                return true;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断对象是否相等
     *
     * @param o1 对象1
     * @param o2 对象2
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(Object o1, Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }

    /**
     * 检查对象非空
     *
     * @param object  对象
     * @param message 报错
     * @param <T>     范型
     * @return 非空对象
     */
    public static <T> T requireNonNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * 获取非空或默认对象
     *
     * @param object        对象
     * @param defaultObject 默认值
     * @param <T>           范型
     * @return 非空或默认对象
     */
    public static <T> T getOrDefault(T object, T defaultObject) {
        if (object == null) {
            return defaultObject;
        }
        return object;
    }

    /**
     * 获取对象哈希值
     *
     * @param o 对象
     * @return 哈希值
     */
    public static int hashCode(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    /**
     * 深度克隆
     * @param object
     * @return
     */
    public static Object deepClone(Object object) {
        Object o = null;
        try {
            if (object != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                oos.close();
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bais);
                o = ois.readObject();
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return o;
    }
}
