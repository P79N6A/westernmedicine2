package com.xywy.askforexpert.module.discovery.answer.service;

import android.text.TextUtils;

import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.answer.api.answered.AnsweredListPageBean;
import com.xywy.askforexpert.model.answer.api.answered.AnsweredPaperBean;
import com.xywy.askforexpert.model.answer.api.answered.WrongBean;
import com.xywy.askforexpert.model.answer.api.paperlist.PaperListGroupBean;
import com.xywy.askforexpert.model.answer.api.paperlist.PaperListItemBean;
import com.xywy.askforexpert.model.answer.api.paperlist.PaperListPageBean;
import com.xywy.askforexpert.model.answer.api.set.SetBean;
import com.xywy.askforexpert.model.answer.api.set.SetPageBean;
import com.xywy.askforexpert.model.answer.show.AnsweredListItem;
import com.xywy.askforexpert.model.answer.show.BaseItem;
import com.xywy.askforexpert.model.answer.show.GroupItem;
import com.xywy.askforexpert.model.answer.show.PaperItem;
import com.xywy.askforexpert.model.answer.show.TestSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by bailiangjin on 16/5/10.
 */
public class ConvertUtils {


    public static List<AnsweredListItem> toAnsweredTestSet(AnsweredListPageBean answeredPageBean) {
        if (null == answeredPageBean) {
            return null;
        }

        List<AnsweredPaperBean> list = answeredPageBean.getData();
        if (null == list || list.isEmpty()) {
            return null;
        }

        List<AnsweredListItem> answeredListItemList = new ArrayList<>();
        for (AnsweredPaperBean answeredPaperBean : list) {
            if (null != answeredPageBean) {

                AnsweredListItem answeredListItem = new AnsweredListItem();
                answeredListItem.setType(AnsweredListItem.TYPE_NORMAL);
                answeredListItem.setName(answeredPaperBean.getPaper_name());
                answeredListItem.setId(answeredPaperBean.getPaper_id());
                answeredListItem.setUpdate_time(answeredPaperBean.getUpdate_time());
                answeredListItemList.add(answeredListItem);

                WrongBean wrongBean = answeredPaperBean.getWrong();
                if (null != wrongBean) {
                    AnsweredListItem wrongItem = new AnsweredListItem();
                    wrongItem.setType(AnsweredListItem.TYPE_WRONG);
                    wrongItem.setName(wrongBean.getPaper_name());
                    wrongItem.setId(wrongBean.getPaper_id());
                    answeredListItemList.add(wrongItem);
                }

            }

        }
        return answeredListItemList;
    }

    /**
     * 转换首页页面数据
     *
     * @param setPageBean
     * @return
     */
    public static List<TestSet> toTestSet(SetPageBean setPageBean) {
        if (null == setPageBean) {
            return null;
        }

        List<SetBean> list = setPageBean.getData();
        if (null == list || list.isEmpty()) {
            return null;
        }
        List<TestSet> testSetList = new ArrayList<>();
        for (SetBean setBean : list) {
            TestSet testSet = new TestSet();
            testSet.setId(setBean.getClass_id());
            testSet.setName(setBean.getClass_name());
            LogUtils.d("imgurl:" + setBean.getImage());
            testSet.setImgUrl(setBean.getImage());
            testSetList.add(testSet);
        }
        return testSetList;
    }

    /**
     * 转换试卷列表页数据 将试卷列表页数据 转换为显示用的试卷列表页数据
     *
     * @param paperListPageBean
     * @return
     */
    public static List<BaseItem> toPaperList(PaperListPageBean paperListPageBean) {

        if (null == paperListPageBean) {
            return null;
        }

        List<PaperListGroupBean> groupList = paperListPageBean.getClassX();
        if (null == groupList || groupList.isEmpty()) {
            return null;
        }
        List<BaseItem> list = new ArrayList<>();

        for (PaperListGroupBean groupBean : groupList) {
            List<PaperListItemBean> itemBeanList = groupBean.getData();

            if (null != itemBeanList && !itemBeanList.isEmpty()) {
                GroupItem groupItem = toGroupItem(groupBean);
                List<PaperItem> paperItemList = new ArrayList<>();
                for (PaperListItemBean itemBean : itemBeanList) {
                    PaperItem paperItem = toPaperItem(itemBean);
                    if (null != paperItem) {
                        paperItemList.add(paperItem);
                    }
                }
                groupItem.setSubList(paperItemList);
                list.add(groupItem);
            }
        }
        return list;
    }

    /**
     * 转换为组条目
     *
     * @param paperListItemBean
     * @return
     */
    public static PaperItem toPaperItem(PaperListItemBean paperListItemBean) {

        if (null == paperListItemBean) {
            return null;
        }
        PaperItem paperItem = new PaperItem(paperListItemBean.getPaper_name());
        paperItem.setPaper_id(paperListItemBean.getPaper_id());
        paperItem.setPaper_name(paperListItemBean.getPaper_name());
        paperItem.setPaper_status(paperListItemBean.getPaper_status());
        paperItem.setClass_id(paperListItemBean.getClass_id());
        paperItem.setPass_score(paperListItemBean.getPass_score());
        paperItem.setTotal_score(paperListItemBean.getTotal_score());
        paperItem.setVersion(paperListItemBean.getUpdate_time());
        paperItem.setAudit_man_id(paperListItemBean.getAudit_man_id());
        return paperItem;
    }

    /**
     * 转换为类条目
     *
     * @param paperListGroupBean
     * @return
     */
    public static GroupItem toGroupItem(PaperListGroupBean paperListGroupBean) {
        if (null == paperListGroupBean) {
            return null;
        }

        GroupItem groupItem = new GroupItem(paperListGroupBean.getClass_name());
        groupItem.setClass_id(paperListGroupBean.getClass_id());
        groupItem.setClass_name(paperListGroupBean.getClass_name());
        groupItem.setPid(paperListGroupBean.getPid());
        groupItem.setClass_level(paperListGroupBean.getClass_level());
        return groupItem;
    }


    /**
     * 将String 类型的答案转为int型答案
     *
     * @param answerStr
     * @return
     */
    public static Map<String, String> getAnswerMap(String answerStr) {

        Map<String, String> answerMap = new HashMap<>();
        String[] answerStringArray = null;
        if (answerStr.contains("|")) {
            answerStringArray = answerStr.split("|");
        } else {
            answerStringArray = new String[1];
            answerStringArray[0] = answerStr;
        }
        char[] items = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (int i = 0; i < items.length; i++) {
            char c = items[i];
            for (String str : answerStringArray) {
                if (String.valueOf(c).equalsIgnoreCase(str)) {
                    answerMap.put(""+i, String.valueOf(c));
                }
            }
        }
        return answerMap;
    }


    /**
     * 将String 类型的答案转为int型答案
     *
     * @param answerStr
     * @return
     */
    public static int getAnswerInt(String answerStr) {

        char[] items = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (int i = 0; i < items.length; i++) {
            char c = items[i];
            if (String.valueOf(c).equalsIgnoreCase(answerStr)) {
                return i;
            }

        }
        return -1;
    }


    /**
     * 将Int 类型的答案转为String型答案(A-Z)
     *
     * @return
     */
    public static String getAnswerString(Map<String, String> answerMap) {

        if (null == answerMap) {
            LogUtils.e("答案为空");
            return "答案为空";
        }

        String answerStr = "";
        Iterator iterator = answerMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iterator.next();
            answerStr += entry.getValue() + "|";
        }
        if (!TextUtils.isEmpty(answerStr) && answerStr.endsWith("|")) {
            answerStr = answerStr.substring(0, answerStr.length() - 1);
            LogUtils.d("answerStr=" + answerStr);
        }
        return answerStr;
    }


    /**
     * 将Int 类型的答案转为String型答案(A-Z)
     *
     * @param answerInt 传入值 0-25之间
     * @return
     */
    public static String getAnswerString(int answerInt) {

        if (answerInt < 0 || answerInt > 25) {
            return "传入值错误 0-25之间 int值";
        }
        char[] items = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char c = items[answerInt];
        return String.valueOf(c);
    }


}
