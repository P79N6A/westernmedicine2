package com.xywy.easeWrapper.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.xywy.askforexpert.model.consultentity.ConsultChatEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2018/8/28.
 */

public class ImCacheAsyncTask extends AsyncTask<Void,Void,Boolean> {
    private List<ConsultChatEntity> chatList = new ArrayList<>();
    private ConsultChatEntity chat;
    private String url;
    private ImFileCacheUtil imFileCacheUtil = ImFileCacheUtil.getInstance();
    private String IO_TAG;
    private String fileName;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        switch (IO_TAG){
            case "savaData":
                try {
                    imFileCacheUtil.savaData(chatList,fileName);
                } catch (Exception e) {
                    Log.e("imFileCahe","缓存文件失败");
                    e.printStackTrace();
                    return false;
                }
                break;
            case "addData":
                try {
                    imFileCacheUtil.addData(chat,fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "downloadImage":
                break;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean tag) {
        super.onPostExecute(tag);
    }

    public void savaData(List<ConsultChatEntity> chatList,String fileName){
        this.fileName = fileName;
        IO_TAG = "savaData";
        this.chatList.clear();
        this.chatList.addAll(chatList);
    }

    public void addData(ConsultChatEntity chat,String fileName){
        IO_TAG = "addData";
        this.chat = chat;
        this.fileName = fileName;
    }

    public void downloadImage(String url){
        IO_TAG = "downloadImage";
        this.url = url;
    }
}
