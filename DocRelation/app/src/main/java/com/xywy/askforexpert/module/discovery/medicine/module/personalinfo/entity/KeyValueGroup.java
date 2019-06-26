package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/4/1 16:57
 */

public class KeyValueGroup extends  KeyValueNode  {
        private List<KeyValueNode> nodes;

    public KeyValueGroup() {
        super();
    }
    public KeyValueGroup(String index, String name) {
        super(index, name);
        nodes=new ArrayList<>();
    }

    /**
     * name : 省份
     * city : [{"name":"北京市","area":["东城区","西城区","崇文区","宣武区","朝阳区"]}]
     */


    public List<KeyValueNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<KeyValueNode> nodes) {
        this.nodes = nodes;
    }
}
