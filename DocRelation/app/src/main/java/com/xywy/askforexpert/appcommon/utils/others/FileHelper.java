package com.xywy.askforexpert.appcommon.utils.others;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xywy.askforexpert.appcommon.net.CommonUrl;
import com.xywy.askforexpert.appcommon.utils.imageutils.DrawableImageLoader.ImageCallback;

import org.apache.http.HttpEntity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class FileHelper {
    /**
     * 判断SD卡上的文件夹是否存在
     */
    public static boolean isFileExist(String fileName, String path) {
        File file = new File(path + File.separator + fileName);
        return file.exists();
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dir
     */
    public static File creatSDDir(String dir) {
        File dirFile = new File(dir + File.separator);
        dirFile.mkdir();
        return dirFile;
    }

    /**
     * 检查文件是否存在
     *
     * @param file
     * @throws IOException
     */
    private static void checkFile(File file) throws IOException {
        boolean exists = file.exists();
        if (exists && !file.isFile()) {
            throw new IOException("File " + file.getPath()
                    + " is actually not a file.");
        }
    }

    /**
     * 创建目录
     *
     * @param filePath
     * @return
     */
    public static void makedirs(File file) throws IOException {
        checkFile(file);
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            if (!parentFile.exists() && !parentFile.mkdirs()) {
                throw new IOException("Creating directories "
                        + parentFile.getPath() + " failed.");
            }
        }
    }

    public static byte[] downFile(String url) throws IOException {
        boolean result = false;
        InputStream inStream = null;
        ByteArrayOutputStream outstream = null;

        byte[] data = null;
        String filePath = "";
        try {
            inStream = HttpHelper.getStream(url);
            if (inStream != null) {
                outstream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 20]; // 用数据装
                int len = -1;
                while ((len = inStream.read(buffer)) != -1) {
                    outstream.write(buffer, 0, len);
                }
                data = outstream.toByteArray();
                outstream.close();
                inStream.close();
                result = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            // MyLog.log("err","loadImageError=" + e.getMessage()+filePath);
        } finally {

            if (outstream != null) {
                outstream.close();
            }
            if (inStream != null) {
                inStream.close();
            }
        }
        return data;

    }

    public static boolean downFile(String url, String dir, String fileName,
                                   ImageCallback callback) throws IOException {
        return downFile(url, dir, fileName, CommonUrl.SUFFIX, callback);

    }

    public static boolean downFile(String u, String dir, String fileName,
                                   String suffix, ImageCallback callback) throws IOException {
        boolean result = false;
        ByteArrayOutputStream outstream = null;
        InputStream inputStream = null;
        String filePath = "";
        long fileSize = 0;
        try {
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            inputStream = conn.getInputStream();
            if (inputStream != null) {
                outstream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 20];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    outstream.write(buffer, 0, len);
                }
            }
            byte[] data = outstream.toByteArray();
            outstream.close();
            inputStream.close();
            filePath = dir + File.separator + fileName + suffix;
            write(new File(filePath), data);
            result = true;
        } catch (Exception e) {
            // TODO: handle exception
            // MyLog.log("err","loadImageError1=" + e.getLocalizedMessage());
        } finally {
            if (outstream != null) {
                outstream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return result;

    }

    public static boolean downFile1(String url, String dir, String fileName,
                                    String suffix, ImageCallback callback) throws IOException {
        boolean result = false;
        InputStream inStream = null;
        ByteArrayOutputStream outstream = null;
        String filePath = "";
        HttpEntity httpEntity;
        long fileSize = 0;
        int is = 0;
        byte[] buffer = null;
        try {
            httpEntity = HttpHelper.getHttpEntity(url);

            fileSize = httpEntity.getContentLength();

            inStream = HttpHelper.getStream(url);
            is = 1;
            if (inStream != null) {
                outstream = new ByteArrayOutputStream();
                buffer = new byte[1024 * 20]; // 用数据装
                int len = -1;

                // int totalLen = 0;
                // long startTime = 0;
                // long endTime = 0;
                // int startPercent = 0;
                // int endPercent = 0;
                is = 11;
                while ((len = inStream.read(buffer)) != -1) {
                    is = 12;
                    try {
                        outstream.write(buffer, 0, len);
                    } catch (Exception s) {
                    }
                    is = 13;
                    // totalLen += len;
                    // endTime = System.currentTimeMillis();
                    // float num = (float) totalLen / (float) fileSize;
                    // int percent = (int) (num * 100);
                    // endPercent = percent;
                    // if ((endTime - startTime > 200 || percent == 100)
                    // && callback != null) {
                    // if (endPercent > startPercent) {
                    // callback.onUpdate(percent, url);
                    // startTime = endTime;
                    // startPercent = endPercent;
                    // }
                    // }
                }
                is = 2;
                byte[] data = outstream.toByteArray();
                outstream.close();
                inStream.close();
                filePath = dir + File.separator + fileName + suffix;
                is = 3;
                write(new File(filePath), data);
                result = true;

            }
        } catch (Exception e) {
            // TODO: handle exception
            // MyLog.log("err","loadImageError1=" +
            // e.toString()+"..."+filePath+is);
        } finally {

            if (outstream != null) {
                outstream.close();
            }
            if (inStream != null) {
                inStream.close();
            }
        }
        return result;

    }

    /**
     * 写入数据到指定目标
     *
     * @param file
     * @param bytes
     * @throws IOException
     */
    public static void write(File file, byte[] bytes) throws IOException {
        write(file, new ByteArrayInputStream(bytes), false);
    }

    /**
     * 写入数据到指定目标
     *
     * @param file
     * @param bytes
     * @param append
     * @throws IOException
     */
    public static void write(File file, byte[] bytes, boolean append)
            throws IOException {
        write(file, new ByteArrayInputStream(bytes), append);
    }

    /**
     * 写入数据到指定目标
     *
     * @param file
     * @param input
     * @throws IOException
     */
    public static void write(File file, InputStream input) throws IOException {
        write(file, input, false);
    }

    /**
     * 写入数据到指定目标
     *
     * @param file
     * @param input
     * @param append
     * @throws IOException
     */
    public static void write(File file, InputStream input, boolean append)
            throws IOException {
        makedirs(file);
        BufferedOutputStream output = null;
        try {
            int contentLength = input.available();
            output = new BufferedOutputStream(
                    new FileOutputStream(file, append));
            while (contentLength-- > 0) {
                output.write(input.read());
            }
        } finally {
            close(input);
            close(output);
        }
    }

    /**
     * 关闭指定对象
     *
     * @param input
     * @param file
     */
    public static void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                closingFailed(e);
            }
        }
    }

    /**
     * 关闭指定对象
     *
     * @param output
     * @param file
     */
    public static void close(OutputStream output) {
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                closingFailed(e);
            }
        }
    }

    /**
     * 关闭指定对象产生异常
     *
     * @param file
     * @param e
     */
    public static void closingFailed(IOException e) {
        throw new RuntimeException(e.getMessage());
    }

    @SuppressWarnings("resource")
    public static Bitmap decodeSampledBitmapFromDescriptor(String path,
                                                           int reqWidth, int inSampleSize) {
        FileInputStream is = null;
        Bitmap bitmap = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            is = new FileInputStream(path);
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(is.getFD(), null, options);
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null,
                    options);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String sPath) {
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) { // 不存在返回 false
            return true;
        } else {
            // 判断是否为文件
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    // 递归
    public static long getFileSize(File f) throws Exception// 取得文件夹大小
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 获取文件夹大小
     *
     * @param sPath 要删除的目录或文件
     * @return 获取文件夹大小。
     */
    public static String GetFolderSize(String sPath) {

        long size = 0;
        try {
            size = getFileSize(new File(sPath));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DecimalFormat df = new DecimalFormat("#.0");
        String fileSizeString = "";
        if (size == 0) {
            fileSizeString = "0B";
        } else if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return true;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        return dirFile.delete();
    }
}
