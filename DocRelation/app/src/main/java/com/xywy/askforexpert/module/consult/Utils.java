package com.xywy.askforexpert.module.consult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhangzheng on 2017/4/27.
 */

public class Utils {
    private static final SimpleDateFormat HM_DATE_FORMAT = new SimpleDateFormat("HH:mm", Locale.CHINA);

    public static String getFormatData(long time) {
        return HM_DATE_FORMAT.format(new Date(time));
    }
}
