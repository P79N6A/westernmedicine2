package com.xywy.askforexpert.appcommon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.askforexpert.appcommon.activity.WebViewActivity;
import com.xywy.askforexpert.appcommon.old.Constants;
import com.xywy.askforexpert.model.AddNumPerInfo;
import com.xywy.askforexpert.module.discovery.medicine.common.MyConstant;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.PatientManager;
import com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.Patient;
import com.xywy.askforexpert.module.discovery.medicine.util.RSAUtils;
import com.xywy.askforexpert.module.message.msgchat.ChatMainActivity;
import com.xywy.askforexpert.module.websocket.WebSocketApi;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 其他待整理 工具类 从之前杂乱Utils中整理出 该工具类较杂乱 不建议使用该工具类
 * Created by bailiangjin on 16/9/14.
 */
public class YMOtherUtils {
    public Context context;

    public static final int BACK_FINISH = 110;

    private YMOtherUtils() {
        throw new UnsupportedOperationException("T cannot be instantiated");
    }

    public YMOtherUtils(Context context) {
        this.context = context;
        if (context == null) {
            context = YMApplication.getInstance();
        }
    }


    /**
     * 获取activity 下面的meta-data值,区分来自那个平台的包
     *
     * @param context
     * @return
     */
    public static String getApplicationMetaData(Context context) {
        ApplicationInfo applicationInfo = null;
        String str = "";
        try {

            applicationInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (applicationInfo == null) {
            str = "wxwypc";
        } else {
            str = applicationInfo.metaData.getString("UMENG_CHANNEL");
        }
        if (TextUtils.isEmpty(str)) {
            str = "wxwypc";
        }
        return str;

    }

    public static String getString_addnum(List<AddNumPerInfo> week) {
        String[] weeks = new String[]{"周一", "周二", "周三", "周四", "周五", "周六",
                "周日"};
        String[] halfday = new String[]{"上午", "下午", "晚上"};
        StringBuilder sb = new StringBuilder();
        String str_enght = null;

        for (int i = 0; i < week.size(); i++) {
            // int
            // position=3*(Integer.parseInt(week.get(i).getWeek())-1)+(Integer.parseInt(week.get(i).getHalfday())-1);
            String we = weeks[Integer.parseInt(week.get(i).getWeek()) - 1];
            String haf = halfday[Integer.parseInt(week.get(i).getHalfday()) - 1];
            sb.append(we + haf + ",");
        }
        if (sb != null) {
            str_enght = sb.substring(0, sb.lastIndexOf(","));
        }
        return str_enght;
    }


    /**
     * 是否含有表情或者符号
     *
     * @param str
     * @return true 不带有表情和符号 false 带有表情或者符号
     */
    public static boolean checkIsBQZF(String str) {
        String reg = "^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){1,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";
        Pattern p2 = Pattern.compile(reg);
        Matcher m2 = p2.matcher(str);

        return m2.matches();
    }

    /**
     * 判断是否是中文
     *
     * @param name
     * @return
     */
    public static boolean checkNameChese(String name) {

        boolean res = true;

        char[] cTemp = name.toCharArray();

        for (int i = 0; i < name.length(); i++) {

            if (!isChinese(cTemp[i])) {

                res = false;

                break;

            }

        }

        return res;

    }

    public static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;

    }

    /**
     * 只包含汉字，不包含汉字中的标点符号等
     *
     * @param c
     * @return
     */
    public static boolean isChineseOnly(char c) {
        return (c >= 0x4e00) && (c <= 0x9fbb);
    }

    public static int getChineseCount(String str) {
        /**中文字符 */
        int chCharacter = 0;

        /**英文字符 */
        int enCharacter = 0;

        /**空格 */
        int spaceCharacter = 0;

        /**数字 */
        int numberCharacter = 0;

        /**其他字符 */
        int otherCharacter = 0;
        if (null == str || str.equals("")) {
            System.out.println("字符串为空");
            return 0;
        }
        str = str.replaceAll("\\s", "");//可以替换大部分空白字符，不限于空格
        for (int i = 0; i < str.length(); i++) {
            char tmp = str.charAt(i);
//            if ((tmp >= 'A' && tmp <= 'Z') || (tmp >= 'a' && tmp <= 'z')) {
//                enCharacter ++;
//            } else if ((tmp >= '0') && (tmp <= '9')) {
//                numberCharacter ++;
//            } else if (tmp ==' ') {
//                spaceCharacter ++;
//            } else if (isChineseOnly(tmp)) {
//                chCharacter ++;
//            } else {
//                otherCharacter ++;
//            }
            if (isChineseOnly(tmp)) {
                chCharacter++;
            }
        }
        return chCharacter;
    }

    /***
     * 判断是否为json
     *
     * @param json
     * @return ture 为 json
     */
    public static boolean isJsonData(String json) {
        boolean isJson;
        try {
            new JSONObject(json);
            isJson = true;
        } catch (Exception e) {
            isJson = false;
        }
        return isJson;
    }


    /**
     * 跳转到查看处方详情页面
     * stone
     * 2017/11/6 下午1:54
     */
    public static void skip2PrecsciptionDetail(Context context, String prescriptionId) {
        String url = MyConstant.H5_BASE_URL + "prescription/detail?doctorid=" + RSAUtils.encodeUserid(YMApplication.getPID() + "") + "&id=" + prescriptionId;
        WebViewActivity.start((Activity) context, "查看处方", url);
    }

    /**
     * 跳转到聊天页 从患者列表/患者详情/点击最近聊天患者
     * stone
     * 2017/11/6 下午1:54
     */
    public static void skip2ChatPage(Context context, Patient patient) {
        PatientManager.getInstance().setPatient(patient);
        Intent intent0 = new Intent(context, ChatMainActivity.class);
        String username = patient.getHx_user();
        intent0.putExtra("userId", username);
        SharedPreferences sp = context.getSharedPreferences("save_gid", Context.MODE_PRIVATE);
        sp.edit().putString(username, patient.getGroup().getGid()).commit();
        if (patient.getHx_user().contains(Constants.QXYL_USER_HXID_MARK)) {
            intent0.putExtra(ChatMainActivity.IS_HEALTHY_USER_KEY, true);
        }
        if (TextUtils.isEmpty(patient.getHx_user())) {
            username = username.replaceAll("did_", "");
            username = username.replaceAll("uid_", "");
            username = username.replaceAll(Constants.QXYL_USER_HXID_MARK, "");
            intent0.putExtra("username", "用户" + username);
        } else {
            intent0.putExtra("username", patient.getRealName());
        }

        intent0.putExtra("toHeadImge", patient.getPhoto());
        context.startActivity(intent0);
    }

    /**
     * 自动连接websocket如果有必要 stone
     */
    public static void autoConnectWebSocketIfNeed() {
        if (!WebSocketApi.INSTANCE.isConnected()) {
            YMApplication.getInstance().initWebSocket();
        }
//        //TODO 在线诊室 stone 要判断当前医生有木有在线诊室的功能
//        if (!WSApi.INSTANCE.isConnected()) {
//            YMApplication.getInstance().initWebSocketForOnline();
//        }
    }


    /**
     * 设置PopupWindow的全屏遮罩 stone
     */
    public static void addScreenBg(PopupWindow popupWindow, final Activity context) {
        showScreenBg(context);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hiddenScreenBg(context);
            }
        });
    }

    /**
     * 显示全屏遮罩 stone
     */
    public static void showScreenBg(Activity context) {
        final WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.5f;
        context.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    /**
     * 显示全屏遮罩 stone
     */
    public static void hiddenScreenBg(Activity context) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 1f;
        context.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


    /**
     * 开启新的Activity for result  stone
     *
     * @param activity    开启新Activity所在的Activity。
     * @param cls         要开启的新Activity类名。
     * @param bundle      所要传输的信息。
     * @param requestCode 请求码，用来回到启动所在Activity判断之用，必须大于等于零。
     */
    public static void startActivityForResult(Activity activity, Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 带结果返回上一个activity， 配合qStartActivityForResult使用 finish当前页面 stone
     *
     * @param activity 所在的Activity。
     * @param bundle
     */
    public static void backForResult(Activity activity, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    /**
     * 获取 文件里面的json
     *
     * @param id
     * @return
     */
    public static String getRawJson(Activity activity, int id) {
        String json = null;
        InputStream is = activity.getResources().openRawResource(id);
        byte[] buffer;
        try {
            buffer = new byte[is.available()];
            is.read(buffer);
            json = new String(buffer, "utf-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p1 = Pattern.compile("^[0][0-9]{2,3}-[2-9][0-9]{6,7}$");  // 验证带区号的
        return p1.matcher(str).matches();
    }

    /**
     * 未总结处理 stone
     *
     * @param str
     * @return
     */
    public static SpannableString getSpanContent(String str) {
        //创建SpannableString对象,内容不可修改
        SpannableString ss = new SpannableString(str);
        if (!TextUtils.isEmpty(str) && str.startsWith("[")) {
            int end = str.indexOf("]") + 1;
            if (end > 0) {
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#f4aa29")), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ss;

    }


    public static SpannableString getSpanTipContent(String str) {
        //创建SpannableString对象,内容不可修改
        SpannableString ss = new SpannableString(str);
        if (!TextUtils.isEmpty(str) && str.startsWith("[")) {
            int end = str.indexOf("]") + 1;
            if (end > 0) {
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#f4aa29")), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ss;

    }

    /**
     * 处理星级 stone
     *
     * @param view
     * @param star
     */
    public static void handleStar(ImageView view, double star) {
//        switch (star) {
//            case 0:
//                view.setImageResource(R.drawable.xing0);
//                break;
//            case 1:
//                view.setImageResource(R.drawable.xing05);
//                break;
//            case 2:
//                view.setImageResource(R.drawable.xing1);
//                break;
//            case 3:
//                view.setImageResource(R.drawable.xing15);
//                break;
//            case 4:
//                view.setImageResource(R.drawable.xing2);
//                break;
//            case 5:
//                view.setImageResource(R.drawable.xing25);
//                break;
//            case 6:
//                view.setImageResource(R.drawable.xing3);
//                break;
//            case 7:
//                view.setImageResource(R.drawable.xing35);
//                break;
//            case 8:
//                view.setImageResource(R.drawable.xing4);
//            case 9:
//                view.setImageResource(R.drawable.xing45);
//            case 10:
//                view.setImageResource(R.drawable.xing5);
//                break;
//        }
        if (Constants.DOUBLE_0 == star) {
            view.setImageResource(R.drawable.xing0);
        } else if (Constants.DOUBLE_1 == star) {
            view.setImageResource(R.drawable.xing05);
        } else if (Constants.DOUBLE_2 == star) {
            view.setImageResource(R.drawable.xing1);
        } else if (Constants.DOUBLE_3 == star) {
            view.setImageResource(R.drawable.xing15);
        } else if (Constants.DOUBLE_4 == star) {
            view.setImageResource(R.drawable.xing2);
        } else if (Constants.DOUBLE_5 == star) {
            view.setImageResource(R.drawable.xing25);
        } else if (Constants.DOUBLE_6 == star) {
            view.setImageResource(R.drawable.xing3);
        } else if (Constants.DOUBLE_7 == star) {
            view.setImageResource(R.drawable.xing35);
        } else if (Constants.DOUBLE_8 == star) {
            view.setImageResource(R.drawable.xing4);
        } else if (Constants.DOUBLE_9 == star) {
            view.setImageResource(R.drawable.xing45);
        } else if (Constants.DOUBLE_10 == star) {
            view.setImageResource(R.drawable.xing5);
        }
    }


    public static void onlineHandleStar(ImageView view, double star) {
        if (0 == star) {
            view.setImageResource(R.drawable.xing0);
        } else if (star>0.5 && star<1) {
            view.setImageResource(R.drawable.xing05);
        } else if (star>1 && star<1.5) {
            view.setImageResource(R.drawable.xing1);
        } else if (star>1.5 && star<2) {
            view.setImageResource(R.drawable.xing15);
        } else if (star>2 && star<2.5) {
            view.setImageResource(R.drawable.xing2);
        } else if (star>2.5 && star<3) {
            view.setImageResource(R.drawable.xing25);
        } else if (star>3 && star<3.5) {
            view.setImageResource(R.drawable.xing3);
        } else if (star>3.5 && star<4) {
            view.setImageResource(R.drawable.xing35);
        } else if (star>4 && star<4.5) {
            view.setImageResource(R.drawable.xing4);
        } else if (star>4.5 && star<5) {
            view.setImageResource(R.drawable.xing45);
        } else if (5 == star) {
            view.setImageResource(R.drawable.xing5);
        }
    }
}
