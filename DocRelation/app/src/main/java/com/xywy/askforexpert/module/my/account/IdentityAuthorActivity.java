package com.xywy.askforexpert.module.my.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.StatisticalTools;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;

public class IdentityAuthorActivity extends Activity {
    private ImageButton btnDoctor, btnStudent, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.initSystemBar(this);

        setContentView(R.layout.activity_identity_author);
        btnBack = (ImageButton) findViewById(R.id.btn_identity_back);
        btnDoctor = (ImageButton) findViewById(R.id.btn_doctor);
        btnStudent = (ImageButton) findViewById(R.id.btn_student);

        btnDoctor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StatisticalTools.eventCount(IdentityAuthorActivity.this, "Medic");
                btnStudent.setSelected(false);
                Intent intent = new Intent(IdentityAuthorActivity.this,
                        RegisterActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("type", "2");
                intent.putExtra("bundle", bundle);

                startActivityForResult(intent, 1002);
//				finish();
            }
        });

        btnStudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StatisticalTools.eventCount(IdentityAuthorActivity.this, "medico");
                btnDoctor.setSelected(false);
                Intent intent = new Intent(IdentityAuthorActivity.this,
                        RegisterActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("type", "1");
                intent.putExtra("bundle", bundle);

                startActivityForResult(intent, 1001);
//				finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            btnStudent.setSelected(true);
            if (resultCode == 1001) {
                finish();
            }
        }
        if (requestCode == 1002) {
            btnDoctor.setSelected(true);
            if (resultCode == 1002) {
                finish();
            }
        }


    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }
}
