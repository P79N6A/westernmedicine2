package com.xywy.askforexpert.module.consult.adapter;

import android.content.Context;

import com.xywy.askforexpert.module.consult.adapter.delegate.ConsultItemDelegate;
import com.xywy.askforexpert.module.consult.adapter.delegate.QuestionAnsweredItemDelegate;
import com.xywy.askforexpert.module.consult.adapter.delegate.QuestionPoolItemDelegate;
import com.xywy.askforexpert.model.consultentity.QuestionsListEntity;
import com.xywy.uilibrary.recyclerview.adapter.XYWYRVMultiTypeBaseAdapter;

/**
 * Created by zhangzheng on 2017/4/26.
 */

public class ConsultListAdapter extends XYWYRVMultiTypeBaseAdapter<QuestionsListEntity> {
    public static final int ITEM_TYPE_NORMAL = 0xf0;

    public ConsultListAdapter(Context context,boolean isFromQuestionLibrary,ImDetailCall imDetailCall) {
        super(context);
        addItemViewDelegate(new ConsultItemDelegate(isFromQuestionLibrary,imDetailCall));
        addItemViewDelegate(new QuestionPoolItemDelegate(isFromQuestionLibrary,imDetailCall));
        addItemViewDelegate(new QuestionAnsweredItemDelegate(isFromQuestionLibrary,imDetailCall));
    }

    public ConsultListAdapter(Context context,boolean isFromQuestionLibrary) {
        super(context);
        addItemViewDelegate(new ConsultItemDelegate(isFromQuestionLibrary));
        addItemViewDelegate(new QuestionPoolItemDelegate(isFromQuestionLibrary));
        addItemViewDelegate(new QuestionAnsweredItemDelegate(isFromQuestionLibrary));
    }

    public interface ImDetailCall{
        void imDetailCall();
    }

}
