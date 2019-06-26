package com.xywy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Serialize Tools
 */
public class SerializeTools {

    private SerializeTools() {
        throw new AssertionError();
    }

    /**
     * Deserialization object from file.
     *
     * @param filePath file path
     * @return de-serialized object
     * @throws RuntimeException if an error occurs
     */
    public static Object deserialization(String filePath) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(filePath));
            Object o = in.readObject();
            in.close();
            return o;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.close(in);
        }
    }

    /**
     * Serialize object to file.
     *
     * @param filePath file path
     * @param obj      object
     * @throws RuntimeException if an error occurs
     */
    public static void serialization(String filePath, Object obj) {
        ObjectOutputStream out = null;
        try {

            checkFileAndCreate(filePath);

            out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(obj);
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtils.close(out);
        }
    }

    //判断文件是否存在
    public static void checkFileAndCreate(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                f.createNewFile();
            }

        } catch (Exception e) {
        }

        return;
    }

}
