package com.xywy.activityrouter;

import android.content.Context;
import android.net.Uri;

/**
 * Created by CaoDongping on 4/8/16.
 */
public interface RouterCallback {
    void notFound(Context context, Uri uri);

    void afterOpen(Context context, Uri uri);

    void error(Context context, Uri uri, Throwable e);
}
