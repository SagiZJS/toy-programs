package jishun.sorm.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {
    public static Object invokeGetter(Object obj, String field) {
        String methodName = String.format("get%s", StringUtils.capitalize(field));
        Method getMethod = null;
        try {
            getMethod = obj.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        try {
            return getMethod.invoke(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void invokeSetter(Object obj, String field, Object fieldValue) {
        invokeSetter(obj, field, field.getClass(), fieldValue);
    }
    
    public static void invokeSetter(Object obj, String field, String fieldTypeName, Object fieldValue) {
        try {
            invokeSetter(obj, field, Class.forName(fieldTypeName), fieldValue);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    

    public static void invokeSetter(Object obj, String field, Class<?> fieldType, Object fieldValue) {
        String methodName = String.format("set%s", StringUtils.capitalize(field));
        
        Method setter = null;
        try {
            setter = obj.getClass().getMethod(methodName, fieldType);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
        try {
            if (setter != null) {
                setter.invoke(obj, fieldValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
