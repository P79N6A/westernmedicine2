package com.xywy.askforexpert.model.discussDetail;

/**
 * 获奖名单
 * <p>
 * Compiler: Android Studio
 * Project: D_Platform
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2015/12/23 9:16
 */
public class WinningList {
    private String name;
    private String score;

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "WinningList{" +
                "name='" + name + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
