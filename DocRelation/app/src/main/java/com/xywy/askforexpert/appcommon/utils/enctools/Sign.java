package com.xywy.askforexpert.appcommon.utils.enctools;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.widget.Toast;

import com.xywy.askforexpert.YMApplication;

/**
 * 项目名称：D_Platform
 * 类名称：Sign
 * 类描述：防止二次打包
 * 创建人：shihao
 * 创建时间：2015-6-16 下午4:35:52
 * 修改备注：
 */
public class Sign {
    private Context con;
    private Activity activity;

    public Sign(Context context) {
        con = context;
        activity = (Activity) context;
    }

    // 获取公钥的java代码
    // 验证签名是否一致防止二次打包
    // fcd945e80130f9af93c098a27c77fa2c 测试包签名
    // 1344f9cb5a04dd384ec7d4e4605a3bc8 正式签名
    public void getSingInfo() {
        try {
            String strSign = "1344f9cb5a04dd384ec7d4e4605a3bc8";
            String pn = con.getPackageName();
            PackageInfo packageInfo = con.getPackageManager().getPackageInfo(pn, PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            int i = signs.length;
            Signature sign = signs[0];
            String md5Sign = MD5Util.getMessageDigest(sign.toByteArray());
            // 如果签名不一致关闭程序
            if (!md5Sign.equals(strSign)) {
                Toast.makeText(con, "您下载的应用可能是盗版，为保证安全，请到官方网址http://www.xywy.com下载正版", Toast.LENGTH_LONG).show();
                YMApplication.getInstance().appExit();
//			    Intent intent = new Intent();        
//		        intent.setAction("android.intent.action.VIEW");    
//		        Uri content_url = Uri.parse("http://g.xywy.com/go?k=c577f2d3dc2e3d26bf13ad8f3daea5a5");   
//		        intent.setData(content_url);  
//		        con.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
