package com.xywy.askforexpert.module.docotorcirclenew.utils;

import com.xywy.askforexpert.appcommon.utils.GsonUtils;
import com.xywy.askforexpert.model.doctor.RealNameItem;
import com.xywy.askforexpert.model.topics.TopicDetailData;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/12/1 16:48
 */

public class ConvertUtil {
    public static List<RealNameItem> convert(List<TopicDetailData.DynamiclistBean> mDatas) {
        List<RealNameItem> results = new ArrayList<>();
        for (TopicDetailData.DynamiclistBean bean : mDatas) {
            results.add(convert(bean, RealNameItem.class));
        }
        return results;
    }

    public static <T, P> P convert(T source, Class<P> type) {
        String jsonStr = GsonUtils.toJson(source);
        P r = GsonUtils.toObj(jsonStr,type);
        return r;
    }
}
