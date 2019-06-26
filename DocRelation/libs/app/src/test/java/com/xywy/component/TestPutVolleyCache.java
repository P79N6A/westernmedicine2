package com.xywy.component;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.xywy.component.datarequest.network.RequestManager;
import com.xywy.component.datarequest.neworkWrapper.BaseData;
import com.xywy.component.datarequest.neworkWrapper.DataRequestWrapper;
import com.xywy.component.datarequest.neworkWrapper.IDataResponse;
import com.xywy.component.datarequest.tool.FileUtils;
import com.xywy.component.no_test.BaseServerInterfaceTest;
import com.xywy.component.test.DataRequestTool;
import com.xywy.component.test.LoginEntity;
import com.xywy.component.test.ServiceProviderTest;

import org.junit.Test;

import java.util.Map;

import static java.lang.System.out;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/7/13 17:30
 */
public class TestPutVolleyCache extends BaseServerInterfaceTest {
    int count=0;
    @Override
    public void setUp() throws Exception {
        super.setUp();
        callback = new IDataResponse() {
            @Override
            public void onResponse(BaseData result) {
                count++;
                out.println("BaseData--------" + new Gson().toJson(result));
                if (result.isIntermediate()){
                    out.println("from cache");
                }else {
                    out.println("from net");
                    signal.countDown();
                }
            }
        };
    }

    @Test
    public void testSyncData() throws Exception {
        RequestManager.getRequestQueue().getCache().clear();
        //构建请求
        Map<String, String> bundle = DataRequestTool.buildCommonBundle();
        bundle.put("api", "785");
        bundle.put("account", "15901121698");
        bundle.put("passwd", "123456");
        bundle.put("version", "1.0");
        bundle.put("sign", DataRequestTool.getSigFromMap(bundle, "JtLtqyYrf6qe2aNt"));
        Request req= new DataRequestWrapper.Builder(DataRequestWrapper.DataRequestMethod.GET, "http://test.api.wws.xywy.com", null)
                .urlMethod("api.php/user/userlogin/index?")
                .header(bundle)
                .build()
                .getRequest();
        //从文件读取初始化返回结果
        String result= FileUtils.readFileByLines(context.getResources().openRawResource(R.raw.init_resp));

        LoginEntity resp=new Gson().fromJson(result,LoginEntity.class);

        RequestManager.putCache(req,resp);
        ServiceProviderTest.getLoginData("15901121698", "123456", callback,"");

        //ServiceProviderTest.bookingDoctor(callback, "");
    }
}