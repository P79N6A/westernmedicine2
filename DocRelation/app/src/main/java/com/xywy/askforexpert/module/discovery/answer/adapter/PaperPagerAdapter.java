package com.xywy.askforexpert.module.discovery.answer.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.appcommon.utils.html.MessageSpan;
import com.xywy.askforexpert.appcommon.utils.tv.TVUtils;
import com.xywy.askforexpert.model.answer.api.paper.Question;
import com.xywy.askforexpert.model.answer.local.ItemSelectedMsg;
import com.xywy.askforexpert.module.discovery.answer.activity.AnswerDetailActivity;
import com.xywy.askforexpert.module.discovery.answer.activity.AnswerShowPicActivity;
import com.xywy.askforexpert.widget.module.answer.ListViewForScrollView;
import com.xywy.uilibrary.activity.UIHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 试卷 ViewPager 适配器
 * Created by bailiangjin on 16/4/18.
 */
public class PaperPagerAdapter extends PagerAdapter {


    //显示的数据
    private List<Question> dataList = null;

    private List<View> mViewCache = null;

    private LayoutInflater mLayoutInflater = null;
    private Activity context;
    private UIHandler handler;

    private Handler imgHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 200) {

                MessageSpan ms = (MessageSpan) msg.obj;
                Object[] spans = (Object[]) ms.getObj();
                ArrayList<String> picUrlList = new ArrayList<String>();

                for (Object span : spans) {
                    if (span instanceof ImageSpan) {
                        //T.shortToast(context, "点击了图片:"+((ImageSpan) span).getSource());
                        picUrlList.add(((ImageSpan) span).getSource());
                    }
                }
                AnswerShowPicActivity.start(context, picUrlList);
            }
        }

    };


    public PaperPagerAdapter(Activity context, List<Question> dataList, UIHandler handler) {
        this.context = context;
        this.handler = handler;
        this.dataList = dataList;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mViewCache = new LinkedList<>();
    }

    /**
     * 发送已答msg
     *
     * @param position
     * @param question
     * @param handler
     */
    private static void sendAnsweredMsg(int position, Question question, UIHandler handler) {
        android.os.Message msg = new android.os.Message();
        msg.what = AnswerDetailActivity.HANDLER_TAG_ITEM_CLICK;
        msg.obj = new ItemSelectedMsg(question.getId(), question.getOrderInAll(), "" + position);
        handler.sendMessage(msg);
    }

    public void setDataList(List<Question> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return null == dataList ? 0 : dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View contentView = (View) object;
        container.removeView(contentView);
        this.mViewCache.add(contentView);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Question question = dataList.get(position);
        ViewHolder viewHolder = null;
        View convertView = null;
        if (mViewCache.isEmpty()) {
            convertView = this.mLayoutInflater.inflate(R.layout.item_answer_adapter,
                    null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            convertView = (View) ((LinkedList)mViewCache).removeFirst();
            viewHolder = (ViewHolder) convertView.getTag();
        }

        updateData(question, viewHolder, convertView);

        container.addView(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return convertView;
    }

    /**
     * 更新数据
     *
     * @param question
     * @param viewHolder
     * @param convertView
     */
    private void updateData(final Question question, final ViewHolder viewHolder, View convertView) {
        addClickListener(question, viewHolder);

        fillData(question, viewHolder);

    }

    private void fillData(Question question, ViewHolder viewHolder) {
        AnswerBranchAdapter answerBranchAdapter = new AnswerBranchAdapter(context, question.getAnswerList(), question.isAnswered());
        viewHolder.lv_answers.setAdapter(answerBranchAdapter);

        //判断是否为共用题干题 确定使用顶部or底部题目控件
        final TextView tv_question = Question.SHOW_TYPE_COMMON_SELECT == question.getShowType() ? viewHolder.tv_question_bottom : viewHolder.tv_question_top;

        if (TextUtils.isEmpty(question.getQuestion())) {
            tv_question.setText("");
        } else {
            String content = question.getOrder() + "、" + question.getQuestion() + "(" + question.getScore() + "分)";
            TVUtils.setTvWithHtmlContent(tv_question, content, R.drawable.img_load_failed, imgHandler);
        }


        viewHolder.tv_question_top.setVisibility(Question.SHOW_TYPE_COMMON_SELECT == question.getShowType() ? View.GONE : View.VISIBLE);
        viewHolder.tv_question_bottom.setVisibility(Question.SHOW_TYPE_COMMON_SELECT == question.getShowType() ? View.VISIBLE : View.GONE);

        viewHolder.tv_answer.setText(TextUtils.isEmpty(question.getCorrectAnswerStr()) ? "" : "答案:" + question.getCorrectAnswerStr().replace("|",""));
        String analysis = TextUtils.isEmpty(question.getAnalysis()) ? "解析:无" : "解析:" + question.getAnalysis();
        TVUtils.setTvWithHtmlContent(viewHolder.tv_analysis, analysis, R.drawable.img_load_failed_small, imgHandler);
        boolean isAnswerShow = question.isMulti() && question.isComplete() || (!question.isMulti() && question.isAnswered());
        viewHolder.tv_answer.setVisibility(isAnswerShow?View.VISIBLE:View.GONE);
        viewHolder.tv_analysis.setVisibility(isAnswerShow?View.VISIBLE:View.GONE);
    }

    private void addClickListener(final Question question, ViewHolder viewHolder) {
        if (question.isWrongState()) {
            //错题状态 不添加点击监听
        } else {
            //试题状态添加 选项监听
            viewHolder.lv_answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (question.isMulti()) {
                        //多选处理逻辑
                        if (question.isComplete()) {
                            LogUtils.e("question is completed");
                        } else {
                            sendAnsweredMsg(position, question, handler);
                        }
                    } else {
                        //单选处理逻辑
                        if (question.isAnswered()) {
                            LogUtils.d("answered already");
                            //Toast.makeText(context, "已答 只能答一次", Toast.LENGTH_SHORT).show();
                        } else {
                            LogUtils.d("answer for first time");
                            sendAnsweredMsg(position, question, handler);
                        }
                    }

                }
            });
        }
    }

    @Override
    public int getItemPosition(Object object) {
        //Log.e("test","getItemPosition ");
        //return super.getItemPosition(object);
        return POSITION_NONE;
    }

    public final class ViewHolder {
        public final View rootView;
        public final TextView tv_question_top;
        public final TextView tv_question_bottom;
        public final TextView tv_answer;
        public final TextView tv_analysis;
        public final ListViewForScrollView lv_answers;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            lv_answers = (ListViewForScrollView) rootView.findViewById(R.id.lv_answers);
            tv_answer = (TextView) rootView.findViewById(R.id.tv_answer);
            tv_analysis = (TextView) rootView.findViewById(R.id.tv_analysis);
            tv_question_top = (TextView) rootView.findViewById(R.id.tv_question_top);
            tv_question_bottom = (TextView) rootView.findViewById(R.id.tv_question_bottom);
        }
    }

}
