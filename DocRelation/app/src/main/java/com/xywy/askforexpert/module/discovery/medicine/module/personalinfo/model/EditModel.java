package com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.model;

import android.text.TextUtils;

import com.xywy.util.ContextUtil;
import com.xywy.util.MatcherUtils;
import com.xywy.util.T;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/28 16:40
 */
public class EditModel {
    private static IEditInfo mEditName = new IEditInfo() {
        public boolean checkInput(String str) {
            if (TextUtils.isEmpty(str)) {
                T.showShort(ContextUtil.getAppContext(), "内容不能为空");
            } else if (str.length() < 2 || str.length() > 15) {
                T.showShort(ContextUtil.getAppContext(),
                        "姓名限制2-15个汉字");
            } else if (!MatcherUtils.isChinese(str)) {
                T.showShort(ContextUtil.getAppContext(),
                        "请输入中文字符 ");
            } else {
                return true;
            }
            return false;
        }

        @Override
        public String getHint() {
            return "请输入名字";
        }

        @Override
        public String getTitle() {
            return "修改名字";
        }
    };
    private static IEditInfo mEditGood = new IEditInfo() {
        @Override
        public boolean checkInput(String str) {
            if (TextUtils.isEmpty(str)) {
                T.showShort(ContextUtil.getAppContext(), "内容不能为空");
            } else if (str.length() < 20 || str.length() > 200) {
                T.showShort(ContextUtil.getAppContext(),
                        "字数限制在20~200字以内 ");
            } else {
                return true;
            }
            return false;
        }

        @Override
        public String getHint() {
            return "擅长";
        }

        @Override
        public String getTitle() {
            return "修改擅长";
        }
    };
    private static IEditInfo mEditIntro = new IEditInfo() {
        @Override
        public boolean checkInput(String str) {
            if (TextUtils.isEmpty(str)) {
                T.showShort(ContextUtil.getAppContext(), "内容不能为空");
            } else if (str.length() < 20 || str.length() > 200) {
                T.showShort(ContextUtil.getAppContext(),
                        "字数限制在20~200字以内 ");
            } else {
                return true;
            }
            return false;
        }

        @Override
        public String getHint() {
            return "简介";
        }

        @Override
        public String getTitle() {
            return "修改简介";
        }
    };

    public static String getTitleByType(String type) {
        return getEditor(type).getTitle();
    }

    public static String getHintByType(String type) {
        return getEditor(type).getHint();
    }

    public static IEditInfo getEditor(String type) {
        if (type.equals(EditType.good)) {
            return mEditGood;
        } else if (type.equals(EditType.intro)) {
            return mEditIntro;
        } else if (type.equals(EditType.userName)) {
            return mEditName;
        }
        return mEditName;
    }

    public static boolean checkInput(String type, String content) {
        return getEditor(type).checkInput(content);
    }

    public interface IEditInfo {
        boolean checkInput(String input);

        String getHint();

        String getTitle();
    }
}
