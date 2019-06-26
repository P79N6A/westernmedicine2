package com.xywy.easeWrapper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xywy.easeWrapper.controller.EaseUI;

import java.util.Set;

public class EasePreferenceManager {
    private SharedPreferences.Editor editor;
    private SharedPreferences mSharedPreferences;
    private static final String KEY_AT_GROUPS = "AT_GROUPS"; 
    
    private EasePreferenceManager(){
        mSharedPreferences = EaseUI.getInstance().getContext().getSharedPreferences("EM_SP_AT_MESSAGE", Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }
    private static EasePreferenceManager instance;
    
    public synchronized static EasePreferenceManager getInstance(){
        if(instance == null){
            instance = new EasePreferenceManager();
        }
        return instance;
        
    }
    
    
    public void setAtMeGroups(Set<String> groups) {
        editor.remove(KEY_AT_GROUPS);
        editor.commit();
        editor.putStringSet(KEY_AT_GROUPS, groups);
        editor.commit();
    }
    
    public Set<String> getAtMeGroups(){
        return mSharedPreferences.getStringSet(KEY_AT_GROUPS, null);
    }
    
}
