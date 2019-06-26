package com.xywy.component.test;

import com.xywy.component.datarequest.neworkWrapper.IDataResponse;

import java.util.Map;

/**
 * 测试网络接口
 */
public final class ServiceProviderTest {


    public static final String REQUEST_URL = "http://test.api.wws.xywy.com/api.php/";
    public static final String MODULE_NAME = "qyyl/";
    public static final String SIGNKEY = "JtLtqyYrf6qe2aNt";

    private ServiceProviderTest() {
    }

    public static void getLoginData(String userName, String password, final IDataResponse iHttpResponse, String flag) {

        String method = TestNamespace.Login;
        Map<String, String> bundle = DataRequestTool.buildCommonBundle();
        bundle.put("api", "785");
        bundle.put("account", userName);
        bundle.put("passwd", password);
        bundle.put("version", "1.0");
        bundle.put("sign", DataRequestTool.getSigFromMap(bundle, SIGNKEY));
        DataRequestTool.get(REQUEST_URL, method, bundle, iHttpResponse, LoginEntity.class, flag);
    }

    public static void bookingDoctor(final IDataResponse iHttpResponse, String flag) {
        Map<String, String> bundle = DataRequestTool.buildCommonBundle();
        bundle.put("doctor_id", "2596415");
        bundle.put("api", "806");
        bundle.put("version", "1.0");
        bundle.put("sign", DataRequestTool.getSigFromMap(bundle, SIGNKEY));
        DataRequestTool.post(REQUEST_URL, TestNamespace.Family_doctor, bundle, iHttpResponse, BookingEntity.class, "");
    }
}
