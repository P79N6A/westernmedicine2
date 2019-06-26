package com.xywy.askforexpert.module.docotorcirclenew.adapter;

import android.content.Context;

import com.xywy.askforexpert.module.docotorcirclenew.model.PublishType;

import static com.xywy.askforexpert.module.docotorcirclenew.model.PublishType.Realname;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/12/21 13:06
 */
public class AdapterFactory {
    /**
     * 实名匿名动态adapter
     * @param context
     * @param publishType
     * @return
     */
    public static BaseUltimateRecycleAdapter newDynanicMsgAdapter(Context context, @PublishType String publishType){
        if (Realname.equals(publishType)){
            return  new CircleRealNameAdapter(context);
        }else {
            return new CircleNotRealNameAdapter(context);
        }
    }
}
