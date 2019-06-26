package com.xywy.easeWrapper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.xywy.askforexpert.appcommon.YMUserService;
import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 2018/8/27.
 */

public class ImFileCacheUtil {

    public static String SDCARD_PAHT ;// SD卡路径
    public static String LOCAL_PATH ;// 本地路径,即/data/data/目录下的程序私有目录
    public static String CURRENT_PATH = "";// 当前的路径,如果有SD卡的时候当前路径为SD卡，如果没有的话则为程序的私有目录
    public static String baseDirPath = "";
    public static String imageDirPath = "";
    public static Map<String,String> userphoto = new HashMap<>();
    public boolean IM_CACHE_TAG = true;


    private static ImFileCacheUtil instance;
    public static HashMap<String, Boolean> map = new HashMap<String,Boolean>();

    private ImFileCacheUtil(){

    }

    public static synchronized ImFileCacheUtil getInstance(){
        if(instance==null){
            instance=new ImFileCacheUtil();
        }
        init();
        return instance;
    }

    public static void init()
    {
        SDCARD_PAHT = Environment.getExternalStorageDirectory().getAbsolutePath();// SD卡路径
        LOCAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();// 本地路径,即/data/data/目录下的程序私有目录

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            CURRENT_PATH = SDCARD_PAHT;
        }
        else
        {
            CURRENT_PATH = LOCAL_PATH;
        }
        baseDirPath = CURRENT_PATH+"/ymcache/"+ YMUserService.getCurUserId()+"/";
        baseDir(CURRENT_PATH+"/ymcache/");
        baseDir(baseDirPath);
        imageFileDir(baseDirPath);
    }

    public static void imageFileDir(String path){
        File file = null;
        file = new File(path+"images");
        if (!file.exists()) {
            file.mkdir();
            imageDirPath = file.getPath();
        }
    }

    //顶层缓存文件夹创建
    public static void baseDir(String path){
        File file = null;
        file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    //对应问题缓存文件夹创建
    public File getDir(String dirName){
        File file = null;
        file = new File(baseDirPath+"/"+dirName);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public boolean existsDir(String dirName){
        File file = null;
        file = new File(baseDirPath+"/"+dirName);
        return file.exists();
    }

    //创建问题缓存文件
    public File makeFile(String fileName) {
        File filePath = getDir(fileName);
        File file = null;
        try {
            file = new File(filePath.getAbsolutePath()+"/"+fileName+".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    //创建缓存文件
    public void savaData(List<ConsultChatEntity> chatList,String fileName) throws Exception{
        File file = makeFile(fileName);
        List<ConsultChatEntity> list = chatList;
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));
        Gson gs = new Gson();
        for (ConsultChatEntity s : list) {
            String data = gs.toJson(s);
            bw.write(data);
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }

    public void addData(ConsultChatEntity chat,String fileName) throws Exception{
        File file = makeFile(fileName);
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));
        Gson gs = new Gson();
        String data = gs.toJson(chat);
        bw.write(data);
        bw.newLine();
        bw.flush();
        bw.close();
    }


    public ArrayList<ConsultChatEntity> getData(String fileName) throws Exception{
        File file = makeFile(fileName);
        Gson gs = new Gson();
        BufferedReader br;
        ArrayList<ConsultChatEntity> array = new ArrayList<ConsultChatEntity>();
        br = new BufferedReader(new FileReader(file.getAbsolutePath()));
        String line = null;
        while ((line = br.readLine()) != null) {
            ConsultChatEntity consultChatEntity = gs.fromJson(line,ConsultChatEntity.class);
            array.add(consultChatEntity);
        }
        br.close();
        return array;
    }

    public void removeCahce(File root) {
        File files[] = root.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    removeCahce(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        removeCahce(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    public void deleteDirWihtFile(File dir)
    {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public void downloadImageFile(final String urlPath, final String imageName){
        map.put(imageName,false);
        Thread t=new Thread(){
            File file=new File(imageDirPath+"/"+imageName);
            public void run() {
                //下载图片的路径
                try {
                    //对资源链接
                    URL url=new URL(urlPath);
                    //打开输入流
                    InputStream inputStream=url.openStream();

                    FileOutputStream fileOutputStream=new FileOutputStream(file);
                    int hasRead=0;
                    while((hasRead=inputStream.read())!=-1){
                        fileOutputStream.write(hasRead);
                    }
                    fileOutputStream.close();
                    inputStream.close();
                    map.put(imageName,true);
                } catch (Exception e) {
                    e.printStackTrace();
                    file.delete();
                }
            }
        };
        t.start();
    }

    public boolean imageCache(String imageName){
        File file=new File(imageDirPath);
        File [] files = file.listFiles();
        boolean imageCache = false;
        for (File images : files){
            if (images.getName().equals(imageName)){
                imageCache =  true;
            }
        }
        return imageCache;
    }
    public String getImageCache(String imageName){
        File file=new File(imageDirPath);
        File [] files = file.listFiles();
        String imagepath = "";
        for (File images : files){
            if (images.getName().equals(imageName)){
                imagepath =  images.getAbsolutePath();
            }
        }
        return imagepath;
    }

    public void setUserPhoto(String doctorId,String url) {

        userphoto.put(doctorId,url);
    }
}
