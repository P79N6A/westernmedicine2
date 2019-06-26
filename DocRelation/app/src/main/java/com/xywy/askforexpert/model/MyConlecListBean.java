package com.xywy.askforexpert.model;

import java.io.Serializable;
import java.util.List;

/**
 * 收藏列表
 *
 * @author LG
 */
public class MyConlecListBean implements Serializable {
    /**
     * "code": 0, "total": "8", "list": [ { "title":
     * "2015 美国膳食指南咨询委员会(DGAC)饮食建议科学报告", "id": "1", "source": "",
     * "collectiontime": "2015年05月14日  09:20", "total": "3", "url":
     * "http://test.api.app.club.xywy.com/app/1.0/shouce/index.interface.php?c=guide&a=detail&id="
     * }, { "title": "2015 SVS下肢动脉粥样硬化性闭塞性疾病指南：无症状疾病和间歇性跛行的管理", "id": "2",
     * "source": "J Vasc Surg. 2015 Jan 28", "collectiontime":
     * "2015年05月15日  12:32", "total": "2", "url":
     * "http://test.api.app.club.xywy.com/app/1.0/shouce/index.interface.php?c=guide&a=detail&id="
     * }, {
     */
    public String code;
    public String msg;
    public String total;
    public List<ItemList> list;


}
