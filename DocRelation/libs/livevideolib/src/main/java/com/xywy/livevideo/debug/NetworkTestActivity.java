package com.xywy.livevideo.debug;

import android.os.Bundle;
import android.widget.Button;

import com.xywy.base.XywyBaseActivity;
import com.xywy.livevideo.common_interface.IDataRequest;
import com.xywy.livevideolib.R;


public class NetworkTestActivity extends XywyBaseActivity {

    IDataRequest mRequest;
    Button request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_test);

        request = (Button) findViewById(R.id.btn_fake);
//        request.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String method = "user/userlogin/index?";
//                Map<String, String> bundle = new HashMap<String, String>();
//                bundle.put("api", "785");
//                bundle.put("version", "1.0");
//                mRequest.post("http://api.wws.xywy.com/api.php/", "api+version",method, bundle, bundle, new IDataResponse<BaseData>() {
//                    @Override
//                    public void onReceived(BaseData o) {
//                        Log.d("response", o.message);
//                    }
//
//                    @Override
//                    public void onError(int code, String message) {
//                        Log.d("response", "error " + message);
//                    }
//                }, BaseData.class, "fake", false);
//            }
//        });
    }
}
