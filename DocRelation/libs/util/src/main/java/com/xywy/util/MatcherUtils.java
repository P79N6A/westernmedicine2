package com.xywy.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by wangjianhua on 15/11/20.
 */
public class MatcherUtils {
    private static final String PATTERN_MOBILE = "^1[0-9]{10}$";

    public static boolean strSplitR(String str) {
        if (str != null) {
            String[] strs = str.split("\\.");
            if (strs.length > 0 && "R".equals(strs[0])) {
                return true;
            } else {
                return false;

            }
        }
        return false;
    }

    public static boolean isUrl(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        String regEx = "[a-zA-z]+://[^\\s]*";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);

        return m.matches();
    }

    public static boolean isValidPhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        Pattern p = Pattern.compile(PATTERN_MOBILE);
        return p.matcher(phone).matches();
    }

    /**
     * 判断是否是中文
     *
     * @param name
     * @return
     */
    public static boolean isChinese(String name) {

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

    private static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;

    }
}
