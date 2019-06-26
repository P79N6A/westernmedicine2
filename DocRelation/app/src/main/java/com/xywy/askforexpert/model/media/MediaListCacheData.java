package com.xywy.askforexpert.model.media;

import com.xywy.askforexpert.model.AddressBook;

import java.util.List;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/7/28 11:10
 */
public class MediaListCacheData {
    /**
     * hassend : 0
     * hxusername : sid_102197307
     * id : 102197307
     * mobile :
     * photo : http://xs3.op.xywy.com/club.xywy.com/doc/20160722/150_150_08952cc43b5b8b.jpg
     * realname : 处理医患关系
     * type : 3
     */

    private List<AddressBook> medialistcache;

    public List<AddressBook> getMediacachelist() {
        return medialistcache;
    }

    public void setMediacachelist(List<AddressBook> mediacachelist) {
        this.medialistcache = mediacachelist;
    }

}
