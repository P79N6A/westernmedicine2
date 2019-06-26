package com.xywy.im.sdk;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by xugan on 2018/7/16.
 */

public class CommonUtils {
    public static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

    public static byte[] intToByteArray(int value) {
        return int2Bytes(value,2);
    }

    public static byte[] int2Bytes(long value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte)((value >> 8 * i) & 0xff);
        }
        return b;
    }

    /**
     * ByteBuffer 转换 String
     * @param buffer
     * @return
     */
    public static String getString(ByteBuffer buffer)
    {
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try
        {
            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            // charBuffer = decoder.decode(buffer);//用这个的话，只能输出来一次结果，第二次显示为空
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
    }
}
