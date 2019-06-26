package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity;


import com.bigkoo.pickerview.model.IPickerViewData;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/4/1 16:13
 */

public class KeyValueNode implements IPickerViewData {
    protected String id;
    protected String name;

    public KeyValueNode() {
        this(MyConstant.NOT_DEFINED,"未知");
    }
    public KeyValueNode(String index, String name) {
        this.id = index;
        this.name = name;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o!=null&& o instanceof KeyValueNode){
            KeyValueNode t= (KeyValueNode) o;
           return id.equals(t.getId())&&name.equals(t.getName());
        }
        return super.equals(o);
    }
}
