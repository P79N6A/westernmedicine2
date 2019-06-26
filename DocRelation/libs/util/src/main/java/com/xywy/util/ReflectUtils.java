package com.xywy.util;

import java.lang.reflect.Field;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/24 13:12
 */

public class ReflectUtils {
    public static String getStringField(Object obj, String fieldName) {
        return String.valueOf(getField(obj, fieldName));
    }

    public static Object getField(Object obj, String fieldName) {
        try {
            Class desireClass = obj.getClass(); //返回类定义的公共的内部类,以及从父类、父接口那里继承来的内部类
//            Field[] fields = desireClass.getDeclaredFields();
//            for(Field f : fields){
//               LogUtils.e("字段："+f.getName()+" 类型为："+f.getType().getName()+" 值为："+ f.get(obj));
//            }
            while (desireClass != null) {
                Field[] fields = desireClass.getDeclaredFields();
                for (Field f : fields) {
                    if (f.getName().equals(fieldName)) {
                        f.setAccessible(true);
                        return f.get(obj);
                    }
                }
                desireClass = desireClass.getSuperclass();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
//    public static void setField(Object obj,String fieldName,Object value){
//
//    }
//    public static Object getStaticField(Object obj,String fieldName){
//
//    }
//    public static void SetStaticField(Object obj,String fieldName,Object value){
//
//    }
}
