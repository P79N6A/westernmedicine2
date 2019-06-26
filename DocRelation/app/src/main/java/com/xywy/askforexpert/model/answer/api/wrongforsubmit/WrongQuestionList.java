package com.xywy.askforexpert.model.answer.api.wrongforsubmit;

import android.text.TextUtils;

import com.xywy.askforexpert.model.answer.api.paper.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 构造的 提交错题list参数
 * Created by bailiangjin on 16/5/9.
 */
public class WrongQuestionList implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<WrongQuestion> dataList;

    public List<WrongQuestion> getDataList() {
        return dataList;
    }

    public void setDataList(List<WrongQuestion> dataList) {
        this.dataList = dataList;
    }

    private void addItem(WrongQuestion param) {

        if (null == dataList) {
            dataList = new ArrayList<>();
        }
        dataList.add(param);
    }

    public void addItem(Question question) {

        WrongQuestion wrongQuestion = new WrongQuestion();
        wrongQuestion.setQuestionId(question.getId());
        wrongQuestion.setAnswer(question.getUserAnswerStr());
        if(!TextUtils.isEmpty(question.getUserAnswerStr())){
            addItem(wrongQuestion);
        }
    }

    public void clear() {
        if (null != dataList) {
            dataList.clear();
        }
    }

    public String toJson() {
        if (null == dataList || dataList.isEmpty()) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        sb = sb.append("[");
        for (WrongQuestion item : dataList) {
            sb = sb.append(item.toJson()).append(",");
        }
        String json = sb.toString();
        if (json.endsWith(",")) {
            json = json.substring(0, json.length() - 1);
        }

        json += "]";
        return json;
    }
}
