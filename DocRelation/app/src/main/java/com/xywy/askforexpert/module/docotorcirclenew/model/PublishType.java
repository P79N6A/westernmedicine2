package com.xywy.askforexpert.module.docotorcirclenew.model;

import android.support.annotation.StringDef;

import static com.xywy.askforexpert.module.docotorcirclenew.model.PublishType.Anonymous;
import static com.xywy.askforexpert.module.docotorcirclenew.model.PublishType.Realname;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/11/15 17:20
 */
@StringDef({Realname, Anonymous})
public @interface PublishType {
    public static final String Realname = "1";
    public static final String Anonymous = "2";
}
