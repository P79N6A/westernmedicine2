package com.xywy.askforexpert.module.discovery.medicine.module.medical.entity;

/**
 * Created by bobby on 17/3/22.
 */

public class SearchResultEntity implements Comparable<SearchResultEntity>{
    private int id;
    private String name;
    private String commonName;
    private long time;
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    @Override
    public int compareTo(SearchResultEntity another) {
        if(this.getTime() - another.getTime()==0){
            return 0;
        }else {
            return this.getTime() - another.getTime()>0?1:-1;
        }
    }

}
