package com.xywy.component;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xywy.activityrouter.Router;
import com.xywy.routerstub.TestModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView content = (TextView) findViewById(R.id.content);

//        xywymainExtra.setIntExtra("int1,int2".split(","));
//        xywymainExtra.setDoubleExtra("double1,double2".split(","));
//        xywymainExtra.setBooleanExtra("boolean1,boolean2".split(","));
//        xywymainExtra.setByteExtra("byte1,byte2".split(","));
//        xywymainExtra.setCharExtra("chat1,chat2".split(","));
//        xywymainExtra.setFloatExtra("float1,float2".split(","));
//        xywymainExtra.setLongExtra("long1,long2".split(","));
//        xywymainExtra.setShortExtra("short1,short2".split(","));
        StringBuilder sb = new StringBuilder();
        sb.append("int1 = ");
        sb.append(getIntent().getIntExtra("int1",-1));
        sb.append("\n");
        sb.append("int2 = ");
        sb.append(getIntent().getIntExtra("int2",-1));
        sb.append("\n");

        sb.append("double1 = ");
        sb.append(getIntent().getDoubleExtra("double1",Double.MAX_VALUE));
        sb.append("\n");
        sb.append("double2 = ");
        sb.append(getIntent().getDoubleExtra("double2",Double.MAX_VALUE));
        sb.append("\n");

        sb.append("boolean1 = ");
        sb.append(getIntent().getBooleanExtra("boolean1", false));
        sb.append("\n");
        sb.append("boolean2 = ");
        sb.append(getIntent().getBooleanExtra("boolean2", false));
        sb.append("\n");

        sb.append("byte1 = ");
        Byte defaultb = 127;
        sb.append(getIntent().getByteExtra("byte1", defaultb));
        sb.append("\n");
        sb.append("byte2 = ");
        sb.append(getIntent().getByteExtra("byte2", defaultb));
        sb.append("\n");

        sb.append("char1 = ");
        sb.append(getIntent().getCharExtra("char1", 's'));
        sb.append("\n");
        sb.append("char1 = ");
        sb.append(getIntent().getCharExtra("char1", 's'));
        sb.append("\n");

        sb.append("float1 = ");
        sb.append(getIntent().getFloatExtra("float1", Float.MAX_VALUE));
        sb.append("\n");
        sb.append("float2 = ");
        sb.append(getIntent().getFloatExtra("float2", Float.MAX_VALUE));
        sb.append("\n");

        sb.append("long1 = ");
        sb.append(getIntent().getLongExtra("long1", Long.MAX_VALUE));
        sb.append("\n");
        sb.append("long2 = ");
        sb.append(getIntent().getLongExtra("long2", Long.MAX_VALUE));
        sb.append("\n");

        sb.append("short1 = ");
        Short defaults = 1;
        sb.append(getIntent().getShortExtra("short1", defaults));
        sb.append("\n");
        sb.append("short2 = ");
        sb.append(getIntent().getShortExtra("short2", defaults));
        sb.append("\n");

        sb.append("string1 = ");
        sb.append(getIntent().getStringExtra("string1"));
        sb.append("\n");
        sb.append("string2 = ");
        sb.append(getIntent().getStringExtra("string2"));
        sb.append("\n");

        TestModel testModel = (TestModel) getIntent().getSerializableExtra("serializable1");
        if(testModel!=null) {
            sb.append("testModel.id =  ");
            sb.append(testModel.id);
            sb.append(" testModel.name = ");
            sb.append(testModel.name);
        }

        content.setText(sb.toString());

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.open(MainActivity.this, "xywyapp://xywyfun?string1=tofunction");
            }
        });
    }
}
