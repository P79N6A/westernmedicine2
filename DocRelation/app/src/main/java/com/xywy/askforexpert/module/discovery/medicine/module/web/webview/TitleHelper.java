package com.xywy.askforexpert.module.discovery.medicine.module.web.webview;

import com.xywy.askforexpert.R;
import com.xywy.askforexpert.YMApplication;
import com.xywy.util.URLCenter;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class TitleHelper {

    //根据URL更新title
    public static String getTitle(String url) {
        String title = YMApplication.getAppContext().getString(R.string.app_name);
        Iterator<String> iter = URLCenter.urls.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();

            Pattern pat = Pattern.compile(key);
            Matcher mat = pat.matcher(url);
            boolean rs = mat.find();

            if(rs) {
                title = URLCenter.urls.get(key);
                break;
            }
        }

        return title;
    }
}
