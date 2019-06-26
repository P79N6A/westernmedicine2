package com.xywy.askforexpert.model.answer.api.paper;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.answer.show.AnswerItem;
import com.xywy.askforexpert.module.discovery.answer.service.ConvertUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 问题 对象
 * Created by bailiangjin on 16/5/9.
 */
public class Question implements Serializable {

    //记分类型
    public static final int COUNT_TYPE_NORMAL = 0;
    public static final int COUNT_TYPE_DECREASE = 1;


    //单选多选类型
    public static final int ANSWER_TYPE_NOT_SUPPORT = -1;
    public static final int ANSWER_TYPE_SINGLE = 1;
    public static final int ANSWER_TYPE_MULTI = 2;
    /**
     * 不支持题型
     */
    public static final int SHOW_TYPE_NOT_SUPPORT = -1;
    /**
     * 普通题型
     */
    public static final int SHOW_TYPE_NORMAL = 1;
    /**
     * 共用题干题型
     */
    public static final int SHOW_TYPE_COMMON_CONTENT = 2;
    /**
     * 共用选项题型
     */
    public static final int SHOW_TYPE_COMMON_SELECT = 3;
    private static final long serialVersionUID = 1L;
    /**
     * 试题id
     */
    @SerializedName("question_id")
    private String id;

    /**
     * 父类id
     */
    @SerializedName("pid")
    private String pid;

    /**
     * 问题题干
     */
    @SerializedName("content")
    private String question;

    /**
     * 补充题干
     */
    @SerializedName("tips")
    private String attachMaterial;

    /**
     * 服务端 返回 选项字符串 |分隔选项
     */
    @SerializedName("select_answer")
    private String answerStr;

    @SerializedName("answer_option")
    private String userAnswerStr;


    @SerializedName("question_score")
    private int score;

    @SerializedName("question_analysis")
    private String analysis;

    @SerializedName("list_order")
    private int order;

    @SerializedName("correct_answer")
    private String correctAnswerStr;


    /**
     * 试题类型 1:普通题 2:共用题干 3:共用备选答案
     */
    @SerializedName("question_type_id")
    private int type;


    @SerializedName("create_time")
    private String create_time;

    @SerializedName("update_time")
    private String update_time;

    @SerializedName("paper_name")
    private String paper_name;


    /**
     * 试题编号
     */
    @SerializedName("common_id_str")
    private String questionNumberStr;


    //node 信息

    /**
     * 试题所属模块分值
     */
    private int node_score;

    /**
     * 结点id
     */
    private String node_id;

    /**
     * 结点name
     */
    private String node_name;

    /**
     * 结点试题描述
     */
    private String node_desc;

    /**
     * 结点顺序
     */
    private int node_order;

    /**
     * 结点试题数量
     */
    private int node_size;


    //本地字段


    private int showType;


    /**
     * 阅读材料（共用题干）
     */
    private String backgroundMaterial;

    /**
     * 试题组分数
     */
    private int groupScore;

    /**
     * 试卷全局顺序
     */
    private int orderInAll;

    /**
     * 节点下顺序
     */
    private int orderInNode;


    /**
     * 是否为错题状态 只有展示错题条目时 该状态为true
     */
    private boolean isWrongState;

    /**
     * 是否已回答 单选
     */
    private boolean isAnswered;

    /**
     * 是否已完成 多选
     */
    private boolean isComplete;

    /**
     * 用户答案
     */
    private Map<String, String> userAnswerMap;


    /***
     * 选项 列表 本地使用
     */
    private List<AnswerItem> answerList;

    /**
     * 当类型为试题组时 子试题List
     */

    @SerializedName("sub")
    private List<Question> questionList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerStr() {
        return answerStr;
    }

    public void setAnswerStr(String answerStr) {
        this.answerStr = answerStr;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCorrectAnswerStr() {
        return correctAnswerStr;
    }

    public void setCorrectAnswerStr(String correctAnswerStr) {
        this.correctAnswerStr = correctAnswerStr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }


    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }

    public int getOrderInNode() {
        return orderInNode;
    }

    public void setOrderInNode(int orderInNode) {
        this.orderInNode = orderInNode;
    }

    public int getOrderInAll() {
        return orderInAll;
    }

    public void setOrderInAll(int orderInAll) {
        this.orderInAll = orderInAll;
    }

    public boolean isWrongState() {
        return isWrongState;
    }

    public void setWrongState(boolean wrongState) {
        isWrongState = wrongState;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }


    public String getUserAnswerStr() {
        if (!TextUtils.isEmpty(userAnswerStr)) {
            return userAnswerStr;
        }
        if (null != getUserAnswerMap()) {
            return ConvertUtils.getAnswerString(getUserAnswerMap());
        }

        LogUtils.e("user answer is null");
        return "";
    }

    public Map<String, String> getUserAnswerMap() {
        if (TextUtils.isEmpty(userAnswerStr)) {
            LogUtils.e("userAnswerMap:" + userAnswerMap);
            return userAnswerMap;
        } else {
            LogUtils.e("userAnswerMap1:" + userAnswerStr);
            return ConvertUtils.getAnswerMap(userAnswerStr);
        }
    }

    public void setUserAnswerMap(Map<String, String> userAnswerMap) {
        if (null == userAnswerMap) {
            return;
        }
        this.userAnswerMap = userAnswerMap;
        setBranchState(userAnswerMap);
    }

    private void setBranchState(Map<String, String> userAnswerMap) {
        if (null != answerList && !answerList.isEmpty()) {
            for (int i = 0; i < answerList.size(); i++) {
                Iterator iterator = userAnswerMap.entrySet().iterator();
                boolean isChecked = false;
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry) iterator.next();
                    String key = entry.getKey();
                    if (i == Integer.parseInt(key)) {
                        isChecked = true;
                    }
                }
                answerList.get(i).setChecked(isChecked);
            }
        }
    }

    private boolean isAnswer(Map<String, String> correctAnswer, int answerInt) {
        if (null == correctAnswer) {
            LogUtils.e("correctAnswerMap is null");
            return false;
        }
        Iterator iterator = correctAnswer.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iterator.next();
            String oneOfAnswerInt = entry.getKey();
            if (String.valueOf(answerInt).equals(oneOfAnswerInt)) {
                return true;
            }
        }
        return false;
    }


    public void addUserAnswer(Integer userAnswer) {
        if (userAnswer < 0) {
            return;
        }
        if (null == userAnswerMap) {
            this.userAnswerMap = new HashMap<>();
        }
        userAnswerMap.put("" + userAnswer, ConvertUtils.getAnswerString(userAnswer));

        setBranchState(userAnswerMap);
    }

    public boolean isContainsAnswer(Integer answer) {
        return null != getUserAnswerMap() && getUserAnswerMap().containsKey(String.valueOf(answer));
    }


    public void removeUserAnswer(Integer answer) {
        if (null == getUserAnswerMap()) {
            LogUtils.e("userAnswerMap is null");
            return;
        }
        if (getUserAnswerMap().containsKey(String.valueOf(answer))) {
            getUserAnswerMap().remove(String.valueOf(answer));
        }
        setBranchState(userAnswerMap);
    }

    public List<AnswerItem> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<AnswerItem> answerList) {
        this.answerList = answerList;
    }

    public Map<String, String> getCorrectAnswer() {
        if (TextUtils.isEmpty(correctAnswerStr)) {
            LogUtils.d("correctAnswerStr:null");
            return null;
        }
        return ConvertUtils.getAnswerMap(correctAnswerStr);

    }


    public int getNode_score() {
        return node_score;
    }

    public void setNode_score(int node_score) {
        this.node_score = node_score;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getNode_desc() {
        return node_desc;
    }

    public void setNode_desc(String node_desc) {
        this.node_desc = node_desc;
    }

    public int getNode_order() {
        return node_order;
    }

    public void setNode_order(int node_order) {
        this.node_order = node_order;
    }

    public int getNode_size() {
        return node_size;
    }

    public void setNode_size(int node_size) {
        this.node_size = node_size;
    }


    public String getBackgroundMaterial() {
        return TextUtils.isEmpty(backgroundMaterial) ? "" : backgroundMaterial;
    }

    public void setBackgroundMaterial(String backgroundMaterial) {
        this.backgroundMaterial = backgroundMaterial;
    }

    public int getGroupScore() {
        return groupScore;
    }

    public void setGroupScore(int groupScore) {
        this.groupScore = groupScore;
    }

    public String getQuestionNumberStr() {
        return questionNumberStr;
    }

    public void setQuestionNumberStr(String questionNumberStr) {
        this.questionNumberStr = questionNumberStr;
    }

    public String getAttachMaterial() {
        return attachMaterial;
    }

    public void setAttachMaterial(String attachMaterial) {
        this.attachMaterial = attachMaterial;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
        List<AnswerItem> answerItemList = null;
        if (null == getAnswerList()) {
            answerItemList = setAnswerList();
        } else {
            answerItemList = getAnswerList();
        }

        if (null != answerItemList && !answerItemList.isEmpty()) {
            for (AnswerItem answerItem : answerItemList) {
                answerItem.setComplete(complete);
            }
        }
    }

    public int getShowType() {

        switch (getType()) {
            case 1:
            case 4:
            case 5:
            case 8:
            case 11:
            case 12:
                return Question.SHOW_TYPE_NORMAL;

            case 2:
            case 6:
            case 7:
            case 10:
            case 13:
            case 15:
                return Question.SHOW_TYPE_COMMON_CONTENT;

            case 3:
            case 9:
            case 14:
                return Question.SHOW_TYPE_COMMON_SELECT;
            default:
                return Question.SHOW_TYPE_NOT_SUPPORT;
        }
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    /**
     * 获取 答案类型 单选or多选
     *
     * @return
     */
    public int getAnswerType() {
        switch (getType()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 12:
            case 13:
            case 14:
                return Question.ANSWER_TYPE_SINGLE;
            case 11:
            case 15:
                return Question.ANSWER_TYPE_MULTI;
            default:
                return Question.ANSWER_TYPE_NOT_SUPPORT;
        }
    }

    public boolean isMulti() {
        return ANSWER_TYPE_MULTI == getAnswerType();
    }

    /**
     * 是否正确
     *
     * @return
     */
    public boolean isRight() {
        if (null == userAnswerMap) {
            LogUtils.e("userAnswerMap is null");
            return false;
        }
        Map<String, String> correctMap = getCorrectAnswer();

        if (null == correctMap) {
            LogUtils.e("correctMap is null");
            return false;
        }

        if (correctMap.size() != userAnswerMap.size()) {
            return false;
        }

        Iterator iterator1 = userAnswerMap.entrySet().iterator();
        while (iterator1.hasNext()) {
            Map.Entry<Integer, String> entry = (Map.Entry) iterator1.next();
            LogUtils.e("userAnswerMap:userAnswer:" + entry.getKey() + "=" + entry.getValue());
        }

        Iterator iterator = correctMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iterator.next();
            LogUtils.e("userAnswerMap:correctMap:" + entry.getKey() + "=" + entry.getValue());
            String oneOfAnswerInt = entry.getKey();
            if (TextUtils.isEmpty(userAnswerMap.get(oneOfAnswerInt))) {
                LogUtils.e("userAnswerMap:答错啦：" + oneOfAnswerInt + ":" + entry.getValue());
                return false;
            }
        }

        LogUtils.e("userAnswerMap:" + "答对啦");

        return true;
    }


    public Question subQuestionFillData(ChapterBean chapterBean, Question superQuestion) {

        switch (getShowType()) {
            case Question.SHOW_TYPE_NORMAL:
                //do nothing
                break;
            case Question.SHOW_TYPE_COMMON_CONTENT:
                setBackgroundMaterial(superQuestion.getQuestion());
                break;
            case Question.SHOW_TYPE_COMMON_SELECT:
                setAnswerStr(superQuestion.getQuestion());
                break;
            default:
                break;
        }

        return fillData(chapterBean);
    }


    /**
     * Question 填充 Chapter数据
     *
     * @param chapterBean
     * @return
     */
    public Question fillData(ChapterBean chapterBean) {

        if (null != chapterBean) {
            //设置node数据
            setNode_id(chapterBean.getId());
            setNode_name(chapterBean.getName());
            setNode_desc(chapterBean.getDesc());

            //LogUtils.e("chapterBean.getOrder():" + chapterBean.getOrder());
            setNode_order(chapterBean.getOrder());
            setNode_score(chapterBean.getTotal());
            setNode_size(chapterBean.getTotal());
        }

        if (null == getAnswerList()) {
            setAnswerList();
        }
        return this;
    }

    private List<AnswerItem> setAnswerList() {
        List<AnswerItem> answerItemList = null;
        //转换试题答案
        String answers = getAnswerStr();
        if (!TextUtils.isEmpty(answers)) {
            String[] answerArray = answers.split("\\|");
            LogUtils.e("answerArray:" + answerArray.length);
            answerItemList = new ArrayList<>();
            for (int i = 0; i < answerArray.length; i++) {
                String answer = answerArray[i];
                AnswerItem answerItem = new AnswerItem();
                answerItem.setContent(answer);
                answerItem.setMulti(isMulti());
                answerItem.setRight(isAnswer(getCorrectAnswer(), i));
                answerItemList.add(answerItem);
            }
            setAnswerList(answerItemList);
        }
        return answerItemList;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }


    public boolean isUserAnsweredAndRight() {
        return (!isMulti() && isAnswered() || isMulti() && isComplete()) && isRight();
    }


    /**
     * 获取试题 或试题组分值
     * @return
     */
    public int getUserScore() {

        if(null==questionList||questionList.isEmpty()){
            return isUserAnsweredAndRight() ? getScore() : 0;
        }else {
            switch (getCountType()) {
                case COUNT_TYPE_NORMAL:
                    return countCommon(questionList);
                case COUNT_TYPE_DECREASE:
                    return countDecrease(questionList);
                default:
                    return countCommon(questionList);
            }
        }

    }


    /**
     * 普通 计算试卷分数
     *
     * @param questionList
     * @return
     */
    private static int countCommon(List<Question> questionList) {
        if (null == questionList || questionList.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Question question : questionList) {
            count+=question.getUserScore();
        }
        return count;
    }


    /**
     * 错题减分的 计算机制
     *
     * @param questionList
     * @return
     */
    private static int countDecrease(List<Question> questionList) {

        if (null == questionList || questionList.isEmpty()) {
            return 0;
        }
        int endScore = 0;
        for (Question question : questionList) {

            if (question.isUserAnsweredAndRight()) {
                //答对加分
                endScore += question.getScore();
            } else {
                //答错 不答减分
                endScore -= question.getScore();
            }
        }
        return endScore > 0 ? endScore : 0;
    }

    /**
     * 获取记分类型
     * @return int  记分类型
     */
    public int getCountType() {
        if (getType() == 15) {
            return COUNT_TYPE_DECREASE;
        } else {
            return COUNT_TYPE_NORMAL;
        }
    }


}
