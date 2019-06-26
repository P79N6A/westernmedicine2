package com.xywy.livevideo;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/3/14 10:15
 */
@IntDef({RechargeType.recharge, RechargeType.mypurse})
@Retention(RetentionPolicy.SOURCE)
public @interface RechargeType {
    public static final int recharge = 0;
    public static final int mypurse = 1;
}
