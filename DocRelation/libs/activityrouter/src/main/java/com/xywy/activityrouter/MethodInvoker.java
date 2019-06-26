package com.xywy.activityrouter;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by bobby on 16/11/22.
 */
public interface MethodInvoker {
    void invoke(Context context, String target, Bundle bundle);
}
