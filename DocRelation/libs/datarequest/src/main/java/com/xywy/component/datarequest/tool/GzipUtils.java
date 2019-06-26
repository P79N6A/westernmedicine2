package com.xywy.component.datarequest.tool;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/8/24 17:04
 */
public class GzipUtils {
    public  static byte[] compressLog(byte[] data) {
        if (data == null) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(data);
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] result=out.toByteArray();
            float rate= result.length*100/data.length;
            Log.e("GzipUtils","压缩率为:"+rate+"%");
        return result;
    }
    public static byte[] decompress(byte[] bytes)  {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
