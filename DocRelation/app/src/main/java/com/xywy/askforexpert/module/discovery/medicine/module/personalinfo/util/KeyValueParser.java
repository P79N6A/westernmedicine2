package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.util;

import android.os.Environment;

import com.google.gson.Gson;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.KeyValueGroup;
import com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.KeyValueNode;
import com.xywy.retrofit.demo.BaseRetrofitResponse;
import com.xywy.util.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/4/1 15:57
 * 用于转换源省市数据格式
 */

public class KeyValueParser {

    public  static void parseOneLevel(final String firstStr,Subscriber subscriber){
        parseImpl(new Observable.OnSubscribe<List<KeyValueGroup>>() {
            @Override
            public void call(Subscriber<? super List<KeyValueGroup>> subscriber) {
                if (cacheMap.get(firstStr)!=null){
                    subscriber.onNext(cacheMap.get(firstStr));
                    return;
                }
                List<KeyValueGroup> groups= getGroups(firstStr);
                cacheMap.put(firstStr,groups);
                subscriber.onNext(groups);
            }
        },subscriber);

    }
    public static  Map<String,List<KeyValueGroup>>  cacheMap=new HashMap<>();
    public  static void parseTwoLevel(final String firstStr, final String secondeStr, Subscriber subscriber){
        parseImpl(new Observable.OnSubscribe<List<KeyValueGroup>>() {
            @Override
            public void call(Subscriber<? super List<KeyValueGroup>> subscriber) {
                if (cacheMap.get(firstStr)!=null){
                    subscriber.onNext(cacheMap.get(firstStr));
                    return;
                }
                List<KeyValueGroup> groups= getGroups(firstStr);
                Map<String, List<KeyValueNode>> map= getNodes(secondeStr);
                relate(groups,map,null);
                cacheMap.put(firstStr,groups);
//                DataFactory.parse(PRO_DATA,city,"/provinces.txt");
                subscriber.onNext(groups);
            }
        },subscriber);
    }
    private  static void parseImpl(Observable.OnSubscribe<List<KeyValueGroup>> onSubscribe, Subscriber subscriber){
        Observable.create(onSubscribe)
//                .subscribeOn(Schedulers.computation()) // 指定 subscribe() 发生在 IO 线程
//                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new BaseRetrofitResponse<List<KeyValueGroup>>(subscriber){
                    @Override
                    public void onNext(List<KeyValueGroup> o) {
                        L.e("parseData");
                        super.onNext(o);
                    }
                });
    }
    /**
     * 将父节点与子节点进行关联
     * @param groups
     * @param map
     * @param fileName
     * @return
     */
    private  static List<KeyValueGroup> relate(List<KeyValueGroup> groups, Map<String, List<KeyValueNode>> map, String fileName){

        for (KeyValueGroup p:groups){
            List<KeyValueNode> nodes=map.get(p.getId());
            p.getNodes().addAll(nodes);
        }
        String data=new Gson().toJson(groups);
        save(fileName, data);
        return groups;
    }

    private static void save(String fileName, String data) {
        if (fileName==null)
            return;
        FileWriter fw= null;
        try {
            fw = new FileWriter(Environment.getExternalStorageDirectory().getPath()+fileName);
            fw.write(data);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  static List<KeyValueGroup> getGroups(String jsonStr){
        List<KeyValueGroup> groups=new ArrayList<>();
        try {
            JSONObject object=new JSONObject(jsonStr);
            Iterator<String> iterator=object.keys();

            while (iterator.hasNext()){
                String next =  iterator.next();
                KeyValueGroup pro=new KeyValueGroup(next,object.optString(next));
                groups.add(pro);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return groups;
    }
    public  static Map<String, List<KeyValueNode>> getNodes(String jsonStr){
        Map<String ,List<KeyValueNode>> map=new HashMap<>();
        try {
            JSONObject object=new JSONObject(jsonStr);
            Iterator<String> iterator=object.keys();

            while (iterator.hasNext()){
                String proId =  iterator.next();
                JSONObject city = object.optJSONObject(proId);
                Iterator<String> cityIt=city.keys();
                List<KeyValueNode> cities =new ArrayList<>();
                while (cityIt.hasNext()){
                    String cityId =  cityIt.next();
                    KeyValueNode pro=new KeyValueNode(cityId, city.optString(cityId));
                    cities.add(pro);
                }
                map.put(proId,cities);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }
}
