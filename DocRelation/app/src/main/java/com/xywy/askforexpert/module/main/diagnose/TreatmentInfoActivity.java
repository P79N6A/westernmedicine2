package com.xywy.askforexpert.module.main.diagnose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.module.main.diagnose.adapter.MainGVAdapter;
import com.xywy.askforexpert.module.my.photo.PhotoActivity;

import java.util.List;

/**
 * 诊断纪录 详情查看
 *
 * @author 王鹏
 * @2015-5-22下午5:00:11
 */
public class TreatmentInfoActivity extends Activity {
    public static List<String> imglist;
    private EditText edit_title;
    private GridView grid;
    private String content;
    private MainGVAdapter adapter;
    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.treatment_info);
        ((TextView) findViewById(R.id.tv_title)).setText("病程记录");
        edit_title = (EditText) findViewById(R.id.edit_title);
        grid = (GridView) findViewById(R.id.gv_shwo);
        content = getIntent().getStringExtra("content");
        imglist = getIntent().getStringArrayListExtra("list");
        adapter = new MainGVAdapter(TreatmentInfoActivity.this, 0);
        tv_content = (TextView) findViewById(R.id.tv_content);
        if (imglist != null) {
            adapter.setData(imglist);
            adapter.isAddShow = false;
            grid.setAdapter(adapter);
        }

        edit_title.setText(content);
        grid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(
                        TreatmentInfoActivity.this,
                        PhotoActivity.class);
                intent.putExtra("statPosition", arg2);
                intent.putExtra("keytype", "treatment_info");
                startActivity(intent);

            }
        });

    }

    public void onClick_back(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                finish();
                break;

            default:
                break;
        }
    }

}
