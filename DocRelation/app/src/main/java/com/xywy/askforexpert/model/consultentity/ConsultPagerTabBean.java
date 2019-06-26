package com.xywy.askforexpert.model.consultentity;

import com.xywy.uilibrary.fragment.pagerfragment.adapter.BaseTabPageBean;

import java.io.Serializable;

/**
 * Created by zhangzheng on 2017/4/26.
 */

public class ConsultPagerTabBean extends BaseTabPageBean<ConsultPagerTabItemBean> implements Serializable {
    public ConsultPagerTabBean(String title, ConsultPagerTabItemBean data) {
        super(title, data);
    }
}
