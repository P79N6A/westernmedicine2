package com.xywy.askforexpert.sdk.kdxf;

import android.Manifest;
import android.app.Activity;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.xywy.askforexpert.appcommon.utils.PermissionUtils;
import com.xywy.askforexpert.appcommon.utils.others.CommonUtils;
import com.xywy.util.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 科大讯飞的管理类 stone
 */
public class KDXFUtils {

    // 语音听写对象
//    private static SpeechRecognizer mIat;

    // 语音听写UI
    private static RecognizerDialog mIatDialog;


    private static WeakReference<Activity> mWeakReference;


    // 用HashMap存储听写结果
    private static HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();


    /**
     * 开始语音
     *
     * @param activity
     * @param mRecognizerDialogListener
     */
    public static void start(Activity activity, RecognizerDialogListener mRecognizerDialogListener) {

        mIatResults.clear();

        if (!isLiving(activity)) {
            return;
        }

        if (!PermissionUtils.checkPermission(activity, Manifest.permission.RECORD_AUDIO)) {
            CommonUtils.permissionRequestDialog(activity, "请先授予音频权限", 1991);
            return;
        }

        if (mWeakReference == null) {
            mWeakReference = new WeakReference(activity);
        }

        Activity activity1 = mWeakReference.get();

        // 步骤-:设置参数
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
//        if (mIat == null) {
//            mIat = SpeechRecognizer.createRecognizer(activity1, null);
//
//            // 清空参数
//            mIat.setParameter(SpeechConstant.PARAMS, null);
//
//            // 设置听写引擎
//            mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
//
//            // 设置返回结果格式
//            mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
//
//            // 设置语言
//            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//
//            // 设置语言区域
//            mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
//
//            //此处用于设置dialog中不显示错误码信息
//            mIat.setParameter("view_tips_plain", "false");
//
//            // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
//            mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
//
//            // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
//            mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
//
//            // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
//            mIat.setParameter(SpeechConstant.ASR_PTT, "1");
//
//            // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//            // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//            mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//            mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
//        }

        //步骤二:展示对话框
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        if (mIatDialog == null) {
            mIatDialog = new RecognizerDialog(activity1, mInitListener);

            // 清空参数
            mIatDialog.setParameter(SpeechConstant.PARAMS, null);

            // 设置听写引擎
            mIatDialog.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

            // 设置返回结果格式
            mIatDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");

            // 设置语言
            mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");

            // 设置语言区域
            mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");

            //此处用于设置dialog中不显示错误码信息
            mIatDialog.setParameter("view_tips_plain", "false");

            // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
            mIatDialog.setParameter(SpeechConstant.VAD_BOS, "4000");

            // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
            mIatDialog.setParameter(SpeechConstant.VAD_EOS, "1000");

            // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
            mIatDialog.setParameter(SpeechConstant.ASR_PTT, "1");

            // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
            // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
            mIatDialog.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            mIatDialog.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");

        }

        // 显示听写对话框
        mIatDialog.setListener(mRecognizerDialogListener);
        mIatDialog.show();

        //获取字体所在的控件，设置为"",隐藏字体，
        TextView txt = (TextView) mIatDialog.getWindow().getDecorView().findViewWithTag("textlink");
        txt.setText("");

        Toast.makeText(activity1, "请开始说话", Toast.LENGTH_SHORT).show();

    }


    /**
     * 初始化监听器。
     */
    private static InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
//                if (mWeakReference != null) {
//                    Toast mToast = Toast.makeText(mWeakReference.get(), "请开始说话…", Toast.LENGTH_SHORT);
//                    mToast.show();
//                }
            }
        }
    };


    /**
     * 取消
     */
    public static void cancel() {
//        if (null != mIat) {
//            退出时释放连接
//            mIat.cancel();
//            mIat.destroy();
//        }

        if (mIatDialog != null) {
            mIatDialog.dismiss();
            mIatDialog = null;
        }

        if (mWeakReference != null) {
            mWeakReference.clear();
            mWeakReference = null;
        }

    }

    /**
     * 关闭 onpause
     */
//    public static void closeDialog() {
//        //直接在activity中onpause中调用
//        if (isShowing(mIatDialog)) {
//            mIatDialog.dismiss();
//            mIatDialog = null;
//        }
//        mWeakReference.clear();
//        mWeakReference = null;
//
//        //延迟关闭方法,需要判断activity状态
////        if (isShowing(mIatDialog) && isExist_Living(mWeakReference)) {
////            mIatDialog.dismiss();
////            mIatDialog = null;
////        }
////            mWeakReference.clear();
////            mWeakReference = null;
//    }
    private static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }


    public static String printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        return resultBuffer.toString();
    }


    private static boolean isLiving(Activity activity) {

        if (activity == null) {
            L.d("wisely", "activity == null");
            return false;
        }

        if (activity.isFinishing()) {
            L.d("wisely", "activity is finishing");
            return false;
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {//android 4.2
//
//            if (activity.isDestroyed()) {
//                Log.d("wisely", "activity is destroy");
//                return false;
//            }
//        }

        return true;
    }

    /**
     * 判断进度框是否正在显示
     */
    private static boolean isShowing(RecognizerDialog dialog) {
        boolean isShowing = dialog != null
                && dialog.isShowing();
        L.d("wisely", ">------isShow:" + isShowing);
        return isShowing;
    }


    private static boolean isExist_Living(WeakReference<Activity> weakReference) {

        if (weakReference != null) {
            Activity activity = weakReference.get();
            if (activity == null) {
                return false;
            }

            if (activity.isFinishing()) {
                return false;
            }
            return true;
        }

        return false;
    }

}
