package com.xywy.askforexpert.module.my.clinic;

import com.xywy.askforexpert.YMApplication;

/**
 * Created by bailiangjin on 2017/5/11.
 */

public class FamilyDoctorService {


    public static int getWeekPrice() {
        int price = 0;
        String weekPrice = YMApplication.famdocinfo.getWeek();

        try {
            price = ((Float) Float.parseFloat(weekPrice)).intValue();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return price;
    }

    public static int getMonthPrice() {
        int price = 0;

        String monthPrice = YMApplication.famdocinfo.getMonth();

        try {
            price = ((Float) Float.parseFloat(monthPrice)).intValue();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return price;
    }


}
