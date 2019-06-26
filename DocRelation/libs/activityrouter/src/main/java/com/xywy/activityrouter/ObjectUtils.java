package com.xywy.activityrouter;

import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by shijiazi on 16/11/24.
 */
public class ObjectUtils {

    /*
    将字节转换为对象
     */
    public static Object ByteToObject(byte[] bytes) {
        Object obj = null;
        try {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();
            bi.close();
            oi.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /*
    将对像转换为字节
     */
    public static byte[] ObjectToByte(java.lang.Object obj) {
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }

    public static String ObjectToString(Object obj) {
        String obStr = "";
        byte[] original = ObjectUtils.ObjectToByte(obj);
        if(original!=null) {
            obStr = new String(Base64.encode(original, Base64.DEFAULT));
        }
        return obStr;
    }

    public static Object StringToObject(String str) {
        Object obj = null;
        if(!TextUtils.isEmpty(str)) {
            byte[] after = Base64.decode(str.getBytes(), Base64.DEFAULT);
            obj = ObjectUtils.ByteToObject(after);
        }
        return obj;
    }
}
