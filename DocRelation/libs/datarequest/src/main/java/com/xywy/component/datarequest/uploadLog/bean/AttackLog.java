package com.xywy.component.datarequest.uploadLog.bean;

import android.content.Intent;
import android.os.Bundle;

import java.util.Set;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/8/30 10:35
 */
public class AttackLog {
    private String stackStrace;
    private String action;
    private String mExtras;
    private Set<String> mCategories;
    private String mComponent;
    public void setStackStrace(String stackStrace) {
        this.stackStrace = stackStrace;
    }

    public void setIntent(Intent intent) {
        this.mComponent = intent.getComponent().getClassName();
        this.action = intent.getAction();
        this.mCategories = intent.getCategories();
        Bundle extras=intent.getExtras();
        this.mExtras = extras!=null?extras.toString():null;
    }
}
