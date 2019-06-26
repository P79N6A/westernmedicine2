package com.xywy.askforexpert.module.discovery.answer.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.artifex.mupdfdemo.AsyncTask;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.base.YMBaseActivity;
import com.xywy.askforexpert.appcommon.utils.LogUtils;
import com.xywy.askforexpert.model.answer.local.ExamPaper;
import com.xywy.askforexpert.module.discovery.answer.adapter.AnswerSheetListAdapter;
import com.xywy.askforexpert.module.discovery.answer.service.AnswerDataService;
import com.xywy.askforexpert.module.discovery.answer.service.CacheUtils;
import com.xywy.askforexpert.widget.module.answer.AnswerSheetView;

import butterknife.Bind;

/**
 * Created by wangpeng on 16/8/23.
 * describ：
 * revise：
 */
public class AnswerSheetActivity extends YMBaseActivity {
    public static final String STR_ID = "str_id";
    @Bind(R.id.list_sheet)
    ListView listSheet;
    AnswerSheetListAdapter adapter;
    ExamPaper examPaper;
    public AnswerSheetView.SheetOnitemClickCallbackListenner myOnItemClickListenner = new AnswerSheetView.SheetOnitemClickCallbackListenner() {
        @Override
        public void onItemClickCallBack(AdapterView<?> adapterView, View view, int i, long l, int orderInAll) {
            AnswerDetailActivity.start(AnswerSheetActivity.this, examPaper, orderInAll);
            finish();
        }
    };



    public static void startActivity(Context context, String id) {
        Intent intent = new Intent(context, AnswerSheetActivity.class);
        intent.putExtra(STR_ID, id);
        context.startActivity(intent);
    }

    /**
     * 获取页面Layout ResID
     *
     * @return
     */
    @Override
    protected int getLayoutResId() {
        return R.layout.answer_sheet_main_view;
    }

    @Override
    protected void beforeViewBind() {

    }

    /**
     * 初始化子类(具体Activity)UI 与数据无关操作
     *
     * @retun
     */
    @Override
    protected void initView() {

        DataTask task = new DataTask();
        task.execute();
    }

    /**
     * 初始化子类逻辑
     */
    @Override
    protected void initData() {

    }

    private class DataTask extends AsyncTask<Void, ExamPaper, ExamPaper> {


        @Override
        protected ExamPaper doInBackground(Void... params) {
            LogUtils.d("时间设置获取缓存1"+ System.currentTimeMillis() + "");

            String id = getIntent().getStringExtra(STR_ID);
            examPaper = AnswerDataService.INSTANCE.getExamPaper(id);
            if (examPaper == null) {
                examPaper = CacheUtils.getExamPaper(id);
            }

            return examPaper;
        }

        @Override
        protected void onPostExecute(ExamPaper aVoid) {
            titleBarBuilder.setTitleText("答题卡");
            adapter = new AnswerSheetListAdapter(AnswerSheetActivity.this, aVoid.getChapterBeanList(), myOnItemClickListenner);
            listSheet.setAdapter(adapter);
            LogUtils.d("时间设置设置listview2"+System.currentTimeMillis() + "");
        }
    }

}
