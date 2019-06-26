package com.xywy.askforexpert.module.main.service.document;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

/**
 * 医学文献
 *
 * @author apple
 */
public class PhysicLiteratureActivity extends AppCompatActivity {
    private Activity context;

    private ListView lv_connect;

    private TextView re_la;

    private String[] documents;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physic_doc);

        intview();
        CommonUtils.setToolbar(this, toolbar);
        CommonUtils.initSystemBar(this);
        intData();
        initLisenner();
    }

    private void intview() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lv_connect = (ListView) findViewById(R.id.example_list);
        re_la = (TextView) findViewById(R.id.physic_search);
        lv_connect.setFadingEdgeLength(0);

    }

    private void intData() {

        documents = new String[]{
                "科学引文索引(SCI)",
                "中国科学引文数据库(CSCD)",
                "美国国立医学图书馆Medline数据库(MEDLINE)",
                "中文核心期刊要目总览(北大核心期刊) (PKU)",
                "美国《化学文摘》(Chemical Abstracts) (CA)",
                "中国科技论文统计源期刊(科技核心期刊) (ISTIC)",
                "美国生物学预评数据库(BIOSIS Previews) (BP)",
                "科学引文索引扩展版(SCIE)"};

        lv_connect.setAdapter(new BaseAdapter() {

            @Override
            public long getItemId(int arg0) {
                return 0;
            }

            @Override
            public Object getItem(int arg0) {
                return null;
            }

            @Override
            public int getCount() {
                return documents.length;
            }

            @Override
            public View getView(int arg0, View view, ViewGroup arg2) {
                ViewHoder holer = null;
                if (view == null) {
                    holer = new ViewHoder();
                    view = LayoutInflater.from(context).inflate(R.layout.item_wenxian_connect, null);
                    holer.content = (TextView) view.findViewById(R.id.tv_connect);
                    view.setTag(holer);
                } else {
                    holer = (ViewHoder) view.getTag();
                }

                holer.content.setText(documents[arg0]);
                return view;
            }

            class ViewHoder {
                TextView content;
            }
        });
    }

    private void initLisenner() {
        MyOnclick myOnclick = new MyOnclick();
        re_la.setOnClickListener(myOnclick);
    }

    class MyOnclick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.physic_search:
                    StatisticalTools.eventCount(context, "sousuo1");

//                    Intent intent = new Intent(context, SearchActivity.class);
                    Intent intent = new Intent(context, HistroyDocmentActivity.class);
                    startActivity(intent);
                    break;

                default:
                    break;
            }

        }

    }

    @Override
    protected void onResume() {
        StatisticalTools.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatisticalTools.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
