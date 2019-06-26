package com.xywy.util;

import java.util.Random;

/**
 * Created by wkteam on 2016/9/2.
 */
public class MathUitls {
    public  static int ramdomRange(int range){
       return (int)(new Random().nextFloat()*range);
    }

    //组合数，n中选出k个
    public static int getCnk(int n,int k){
        int value=1;
        value=getFactorial(n-k+1,n)/getFactorial(1,k);
        return value;
    }
    //small到big的阶乘，big>=small
    public static int getFactorial(int small,int big){
        int value=1;

        for(int i=small==0?1:small;i<=big;i++){
            value*=i;
        }

        return value;
    }

    public static float powers(float a,int n){
        float value=1;
        for(int i=0;i<n;i++){
            value*=a;
        }
        return value;
    }

}
