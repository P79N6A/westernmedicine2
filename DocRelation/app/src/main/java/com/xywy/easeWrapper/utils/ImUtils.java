package com.xywy.easeWrapper.utils;

import android.content.Context;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.xywy.askforexpert.R;
import com.xywy.askforexpert.appcommon.utils.CtxtUtils;
import com.xywy.easeWrapper.EaseConstant;

/**
 * Created by bailiangjin on 16/9/1.
 */
public class ImUtils {

    /**
     * 根据消息内容和消息类型获取消息内容提示
     *
     * @param message
     * @param context
     * @return
     */
    public static String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
            case LOCATION: // 位置消息
                if (message.direct() == EMMessage.Direct.RECEIVE) {
                    // 从sdk中提到了ui中，使用更简单不犯错的获取string方法
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_recv");
                    digest = CtxtUtils.getString(R.string.location_recv);
                    digest = CtxtUtils.getString(R.string.location_recv);
                    digest = String.format(digest, message.getFrom());
                    return digest;
                } else {
                    // digest = EasyUtils.getAppResourceString(context,
                    // "location_prefix");
                    digest = CtxtUtils.getString(R.string.location_prefix);
                }
                break;
            case IMAGE: // 图片消息
                digest = CtxtUtils.getString(R.string.picture);
                break;
            case VOICE:// 语音消息
                digest = CtxtUtils.getString(R.string.voice);
                break;
            case VIDEO: // 视频消息
                digest = CtxtUtils.getString(R.string.video);
                break;
            case TXT: // 文本消息
                if (!message.getBooleanAttribute(
                        EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                    digest = txtBody.getMessage();
                } else {
                    EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                    digest = CtxtUtils.getString(R.string.voice_call)
                            + txtBody.getMessage();
                }
                break;
            case FILE: // 普通文件消息
                digest = CtxtUtils.getString(R.string.file);
                break;
            default:
                System.err.println("error, unknow type");
                return "";
        }

        return digest;
    }

}
