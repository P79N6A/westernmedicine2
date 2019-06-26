package com.xywy.im.sdk.tool;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xu on 14-12-3.
 */
public class FileCache {
    /**
     * The default buffer size to use.
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private FileCache(){}
    public static FileCache instance = new FileCache();
    public static FileCache getInstance() {
        dir = new File(Environment.getExternalStorageDirectory().getPath()+"/xywy_im_img/");
        return instance;
    }

    private static File dir;
//    public void setDir(String path) {
//        this.dir = new File(dir.getPath()+path);
//    }

    public File getDir(){
        return this.dir;
    }
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);

    public void storeFile(String key, InputStream inputStream) throws IOException {
        File file = new File(this.dir, getFileName(key));
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        copy(inputStream, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void storeByteArray(String key, ByteArrayOutputStream byteStream) throws IOException {
        if(!this.dir.exists()){
            this.dir.mkdirs();
        }
        File file = new File(this.dir, getFileName(key));
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byteStream.writeTo(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public void storeByteArray(byte[] data) throws IOException {
        if(null != data){
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "XYWYIM_Exception-" + time + "-" + timestamp + ".txt";
            String filePath = Environment.getExternalStorageDirectory().getPath()+"/xywy_im_data/";
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File file = new File(filePath);
                if(!file.exists()){
                    file.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(filePath+fileName);
                fos.write(data,0,data.length);
                fos.flush();
                fos.close();
            }
        }
    }

    public void removeFile(String key) {
        File file = new File(this.dir, getFileName(key));
        file.delete();
    }

    public String getCachedFilePath(String key) {
        File file = new File(this.dir, getFileName(key));
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return null;
    }

    public boolean isCached(String key) {
        return getCachedFilePath(key) != null;
    }

    private String getFileName(String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(key.getBytes());
            byte[] m = md5.digest();
            return BinAscii.bin2Hex(m);
        } catch (NoSuchAlgorithmException e) {
            //opps
            System.exit(1);
            return "";
        }
    }

    public void deleteDir(File dir){
        if(null != dir){
            if(dir.isFile()){
                dir.delete();
                return;
            }

            if(dir.isDirectory()){
                File[] childFiles = dir.listFiles();
                if(null != childFiles && childFiles.length == 0){
                    dir.delete();
                    return;
                }

                for (int i = 0; i < childFiles.length; i++) {
                    deleteDir(childFiles[i]);
                }
                dir.delete();
            }
        }
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
