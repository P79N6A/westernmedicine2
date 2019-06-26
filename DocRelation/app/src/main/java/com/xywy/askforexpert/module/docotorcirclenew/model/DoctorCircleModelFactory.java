package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.app.Activity;

import com.xywy.askforexpert.module.docotorcirclenew.interfaze.IViewRender;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/29 13:02
 */
public class DoctorCircleModelFactory {
   public static IRecycleViewModel newInstance(IViewRender view, Activity context, @PublishType String publishType, String visitedUserId){
        if (PublishType.Realname.equals(publishType)){
            if (visitedUserId ==null){
                return new RecycleViewRealNameModel(view,context);
            }else {
                return new RecycleViewMyRealNameModel(view,context, visitedUserId);
            }
        }else{
            if (visitedUserId ==null){
                return new RecycleViewNotRealNameModel(view,context);
            }else {
                return new RecycleViewMyNotRealNameModel(view,context, visitedUserId);
            }
        }
    }

    public static IRecycleViewModel newVisitHistoryModel(IViewRender view, Activity context, @PublishType String publishType) {

        return new RecycleViewVisitHistoryModel(view, context, publishType);
    }
}
