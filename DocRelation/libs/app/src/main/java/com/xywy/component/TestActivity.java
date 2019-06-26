package com.xywy.component;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xywy.activityrouter.Router;
import com.xywy.component.datarequest.imageWrapper.ImageLoaderUtils;
import com.xywy.component.datarequest.network.RequestManager;
import com.xywy.component.datarequest.neworkWrapper.BaseData;
import com.xywy.component.datarequest.neworkWrapper.IDataResponse;
import com.xywy.component.datarequest.uploadLog.UploadLogClient;
import com.xywy.component.test.BookingEntity;
import com.xywy.component.test.DataRequestTool;
import com.xywy.component.test.LoginEntity;
import com.xywy.component.test.ServiceProviderTest;
import com.xywy.component.test.TestDBImp;
import com.xywy.component.test.TestModel;
import com.xywy.modifyjson.ModifyJsonUtil;

import step.StepCountActivity;
import step.StepManager;

@SuppressWarnings("PMD")
public class TestActivity extends Activity implements View.OnClickListener {

    ImageView imageView;

    Button walk_count;
    Button router;
    TextView show_walk_count;
    Handler h;
    void test() {
        if (h==null){
            h=new Handler();
        }
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    testNetwork();
                    test();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        StepManager.init(this, "all");//user_id = all代表手机记录数据

        walk_count = (Button) findViewById(R.id.walk_count);
        show_walk_count = (TextView) findViewById(R.id.show_walk_count);

        router = (Button) findViewById(R.id.router);
        router.setOnClickListener(this);
        walk_count.setOnClickListener(this);

        testInsert();
        int count = testCount();

        RequestManager.init(getApplicationContext(),true);
        try{
            getIntent();
            throw new RuntimeException("test attack Log");
        }catch (Exception e){
            UploadLogClient.attack(getIntent(),e);
        }

        testNetwork();

        ImageLoaderUtils.getInstance().init(this);

        imageView = (ImageView) findViewById(R.id.imageView);

        ImageLoaderUtils.getInstance().displayImage("http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg", imageView);

//        modifyJson();
    }

    private void modifyJson() {
        ModifyJsonUtil.modifyDepartment("zhuanjiawang_department.json", this);
        ModifyJsonUtil.modifyPromotionsMenu("promotions_area.json", this, "全国", "全部");
        ModifyJsonUtil.modifyPromotionsMenu("promotions_department.json", this, "全部科室", "全部");
        ModifyJsonUtil.modifyHospital("zhuanjiawang_hospital.json", this);
    }


    private void testInsert() {
        TestModel mode = new TestModel();
        mode.setTime(System.currentTimeMillis());
        mode.setUid(123);
        TestDBImp.getInstance().save(getApplicationContext(), mode);
    }

    private int testCount() {
        return TestDBImp.getInstance().getCount(getApplicationContext());
    }

    private void testNetwork() {

        ServiceProviderTest.getLoginData("15901121698", "123456", new IDataResponse() {
            @Override
            public void onResponse(BaseData obj) {
                if (!DataRequestTool.noError(obj)) {
                    Toast.makeText(getApplicationContext(), obj.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    if (obj.getData() != null && obj.getData() instanceof LoginEntity) {
                        LoginEntity entity = (LoginEntity) obj.getData();
                        int i = 0;
                    }
                }
                //测试 crash log
                if (BuildConfig.DEBUG){
//                    throw new RuntimeException("test crash log");
                }
            }
        }, "");

        ServiceProviderTest.bookingDoctor(new IDataResponse() {
            @Override
            public void onResponse(BaseData obj) {
                if (!DataRequestTool.noError(obj)) {
                    Toast.makeText(getApplicationContext(), obj.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    if (obj.getData() != null && obj.getData() instanceof BookingEntity) {
                        BookingEntity entity = (BookingEntity) obj.getData();
                        int i = 0;
                    }
                }
            }
        }, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.walk_count:
                StepCountActivity.startActivity(this);
                break;

            case R.id.router:
                Router.open(this, "xywyapp://stubmain?id=123");
                break;
            default:
                break;
        }
    }
}
