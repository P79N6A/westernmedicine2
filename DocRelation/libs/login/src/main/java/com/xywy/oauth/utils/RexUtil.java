package com.xywy.oauth.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aidonglei on 2015/12/10.
 */
public class RexUtil {

    public static boolean isMobile(String mobiles) {
        String telRegex = "(13[0-9]|14[57]|15[012356789]|17[0123456789]|18[0123456789])\\d{8}";

        //Pattern p = Pattern
        //        .compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");
        //Matcher m = p.matcher(mobiles);
        return mobiles.matches(telRegex);
    }

    /**
     * 数字或字母,6-15位
     *
     * @param password
     * @return
     */
    public static boolean passwordMatch(String password) {
        String pwdRex = "^[A-Za-z0-9]{6,15}$";
        Pattern pattern = Pattern.compile(pwdRex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean nicknameMatch(String nickname) {
        String rex = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
        return nickname.matches(rex);
    }
}
