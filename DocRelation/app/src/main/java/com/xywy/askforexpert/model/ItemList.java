package com.xywy.askforexpert.model;

import java.io.Serializable;

public class ItemList implements Serializable {

    /**
     * "title": "带绦虫孕节检查法",
     * "id": "1",
     * "collectiontime": "2015年06月17日  11:15",
     * "total": "5",
     * "url": "http://test.api.app.club.xywy.com/app/1.0/shouce/index.interface.php?c=check&a=detail&id=1",
     * "iscollection": 1,
     * "msg_iscollection": "已收藏"
     */
    public String title;
    public String id;
    public boolean isSeclect;
    public String source;
    public String image;
    public String iscollection;
    public String collectiontime;
    public String total;
    public String url;
    public String msg_iscollection;

    /**
     * 资讯
     * "id": "35080",
     * "title": "男子癌症晚期五次就诊未诊出 医院判赔 38 万",
     * "author": "何欣",
     * "createtime": "2015年06月18日  11:35",
     * "source": "北京晨报",
     * "total": "9",
     * "url": "http://club.xywy.com/zixun/d35080.html?from=h5",
     * "colle": 1,
     * "praise": 1
     */

    public String createtime;
    public String colle;
    public String praise;


    /**
     * "title": "2015 BSACI指南：慢性荨麻疹和血管性水肿的管理",
     * "id": "1249",
     * "source": "Clin Exp Allergy. 2015 Mar;45(3):547-65.",
     * "filesize": "367365",
     * "collectiontime": "2015年06月18日  06:14",
     * "total": "2",
     * "url": "http://test.api.app.club.xywy.com/app/1.0/shouce/index.interface.php?c=guide&a=detail&id=1249",
     * "downloadurl": "http://test.api.app.club.xywy.com/app/1.0/shouce/index.interface.php?c=download&a=download&id=1249&userid=58775291",
     * "ispraise": 1,
     * "msg_ispraise": "已点赞",
     * "iscollection": 1,
     * "msg_iscollection": "已收藏"
     */


    public String filesize;
    public String downloadurl;

    /***
     * {
     * "id": "289",
     * "lid": "0",
     * "createtime": "1441176861",
     * "uid": "18732252"
     * }, 文献
     */
    public String lid;
    public String DBID;


}
