package com.xywy.routerstub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xywy.activityrouter.ObjectUtils;
import com.xywy.activityrouter.Router;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stub);

        Button clickme = (Button) findViewById(R.id.clickme);
        clickme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                xywymainExtra.setIntExtra("int1,int2".split(","));
//                xywymainExtra.setDoubleExtra("double1,double2".split(","));
//                xywymainExtra.setBooleanExtra("boolean1,boolean2".split(","));
//                xywymainExtra.setByteExtra("byte1,byte2".split(","));
//                xywymainExtra.setCharExtra("chat1,chat2".split(","));
//                xywymainExtra.setFloatExtra("float1,float2".split(","));
//                xywymainExtra.setLongExtra("long1,long2".split(","));
//                xywymainExtra.setShortExtra("short1,short2".split(","));
                TestModel model = new TestModel();
                model.id = 998;
                model.name = "test model";
                StringBuilder sb = new StringBuilder();
                sb.append("xywyapp://xywymain?int1=123&int2=456&double1=111&double2=222&boolean1=true&boolean2=false&" +
                        "byte1=1&byte2=2&char1=a&char2=b&float1=1.0&float2=2.0&long1=888&long2=999&short1=33&short2=44&string1=this is string&string2=this is string2");
                sb.append("&serializable1=");

                sb.append(ObjectUtils.ObjectToString(model));

                Router.open(MainActivity.this, sb.toString());
            }
        });
    }
}
