package com.xywy.component.datarequest.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/7/7 10:33
 */
public class FileUtils {
    //获取文件夹大小
    public static long getTotalSizeOfFilesInDir(File file) {
        if (!file.exists()){
            return 0;
        }
        if (file.isFile()){
            return file.length();
        }
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null){
            for (File child : children){
                total += getTotalSizeOfFilesInDir(child);
            }
        }
        return total;
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(File file) {
        try {
          return  readFileByLines(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(InputStream inputStream) {
        BufferedReader reader = null;
        String result = "";
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
                result += tempString + "\n";
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return result;
    }

}
