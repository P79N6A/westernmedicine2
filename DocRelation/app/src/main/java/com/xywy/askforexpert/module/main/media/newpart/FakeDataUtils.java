package com.xywy.askforexpert.module.main.media.newpart;

import com.xywy.askforexpert.model.media.MediaTypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bailiangjin on 2016/12/21.
 */

public class FakeDataUtils {
    public static List<MediaTypeBean> LEFT_STR_DATAS = new ArrayList<>();

    static {

        for (int i = 1; i < 100; i++) {
            String name = "左条目" + i;
            MediaTypeBean item = new MediaTypeBean();
            item.setName(name);
            LEFT_STR_DATAS.add(item);
        }
    }

    public static List<String> getRightDataList(String start) {
        List<String> dataList = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            String item = "分类" + start + "条目" + i;
            dataList.add(item);
        }

        return dataList;
    }


}
