package com.zzzmode.appopsx.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zl on 2016/11/6.
 */

public class ReflectUtils {
    private static final Map<String, Field> sFieldCache = new HashMap<>();
    private static final Map<String, Method> sMethodCache = new HashMap<>();


    public static PackageOps opsConvert(Object object) {
        String packageName = null;
        int uid = 0;
        List<OpEntry> entries = null;
        Object mPackageName = getFieldValue(object, "mPackageName");
        if (mPackageName instanceof String) {
            packageName = ((String) mPackageName);
        }

        Object mUid = getFieldValue(object, "mUid");
        if (mUid instanceof Integer) {
            uid = ((Integer) mUid);
        }

        Object mEntries = getFieldValue(object, "mEntries");
        if (mEntries instanceof List) {
            List list = (List) mEntries;
            entries=new ArrayList<>();
            for (Object o : list) {
                int mOp=getIntFieldValue(o,"mOp");
                int mMode=getIntFieldValue(o,"mMode");
                long mTime=getLongFieldValue(o,"mTime");
                long mRejectTime=getLongFieldValue(o,"mRejectTime");
                int mDuration=getIntFieldValue(o,"mDuration");
                int mProxyUid=getIntFieldValue(o,"mProxyUid");
                String mProxyPackageName=String.valueOf(getFieldValue(o,"mProxyPackageName"));

                entries.add(new OpEntry(mOp,mMode,mTime,mRejectTime,mDuration,mProxyUid,mProxyPackageName));
            }
        }

        return new PackageOps(packageName, uid, entries);
    }

    private static Object getFieldValue(Object obj, String fieldName) {
        Field field = sFieldCache.get(fieldName);
        if (field == null) {
            try {
                field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                sFieldCache.put(fieldName, field);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (field != null) {
            try {
                return field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static int getIntFieldValue(Object obj, String fieldName){
        Object fieldValue = getFieldValue(obj, fieldName);
        if(fieldValue instanceof Integer){
            return ((Integer) fieldValue);
        }
        return 0;
    }
    private static long getLongFieldValue(Object obj, String fieldName){
        Object fieldValue = getFieldValue(obj, fieldName);
        if(fieldValue instanceof Long){
            return ((Long) fieldValue);
        }
        return 0;
    }



    public static Object getArrayFieldValue(Class cls,String arrayFieldName,int index){
        Field field = sFieldCache.get(arrayFieldName);
        if (field == null) {
            try {
                field = cls.getDeclaredField(arrayFieldName);
                field.setAccessible(true);
                sFieldCache.put(arrayFieldName, field);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (field != null) {
            try {
                Object object= field.get(cls);
                if(object.getClass().isArray()){
                    Object[] array= (Object[]) object;
                    return array[index];
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}