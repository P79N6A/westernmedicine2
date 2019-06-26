package com.xywy.interactor.utils;

public class InteractorUtilGlobal {
    private static boolean mIsUseFakeDataByDefault = false;

    public static boolean getIsUseFakeDataByDefault() {
        return mIsUseFakeDataByDefault;
    }

    public static void setIsUseFakeDataByDefault(boolean value) {
        mIsUseFakeDataByDefault = value;
    }
}