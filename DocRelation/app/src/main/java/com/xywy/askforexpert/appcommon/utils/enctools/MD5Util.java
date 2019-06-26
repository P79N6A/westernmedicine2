package com.xywy.askforexpert.appcommon.utils.enctools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * MD5加密工具
 *
 * @author 王鹏
 * @2015-4-14上午10:45:00
 */
public class MD5Util {
    /**
     * MD5转码
     *
     * @param inStr
     * @return
     */
    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    // 可逆的加密算法
    public static String KL(String inStr) {
        // String s = new String(inStr);
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }

    // 加密后解密
    public static String JM(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String k = new String(a);
        return k;
    }

    public static final String getMessageDigest(byte[] paramArrayOfByte) {
        char[] arrayOfChar1 = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            int i = arrayOfByte.length;
            char[] arrayOfChar2 = new char[i * 2];
            int j = 0;
            int k = 0;
            while (true) {
                if (j >= i) {
                    return new String(arrayOfChar2);
                }
                int m = arrayOfByte[j];
                int n = k + 1;
                arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
                k = n + 1;
                arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
                j++;
            }
        } catch (Exception localException) {
        }
        return null;
    }

    public static final byte[] getRawDigest(byte[] paramArrayOfByte) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            return arrayOfByte;
        } catch (Exception localException) {
        }
        return null;
    }



    public static String getFileMD5(String filePath) {
        FileInputStream fileInputStream = null;
        MessageDigest md = null;

        try {
            fileInputStream = openFile(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generateHashForFile(fileInputStream, md);
    }



    private static String generateHashForFile(FileInputStream file, MessageDigest md) {
        DigestInputStream fileDigest = new DigestInputStream(file, md);

        try {
            while (fileDigest.read() != -1) ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] digested1 = md.digest();
        String hash = byteToHex(digested1);
        return hash;
    }


    private static FileInputStream openFile(String fileName) throws FileNotFoundException {
        return (new FileInputStream(fileName));
    }

    private static String byteToHex(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        String hash = formatter.toString();
        formatter.close();
        return hash;
    }
}