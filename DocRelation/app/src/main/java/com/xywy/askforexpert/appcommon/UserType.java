package com.xywy.askforexpert.appcommon;

/**
 * 用户类型 stone
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2017/1/6 14:15
 */
public class UserType {

     static  final int none =0; //0
     static  final int login =1 << 0;//已登录 1
     static  final int UnApproved=1 << 1;//未认证 10
     static  final int Approving=1 << 2;//认证中 100
     static  final int Approved=1 << 3;//已认证 1000
     static  final int ApproveFailed=1 << 4;//认证失败 10000
     static  final int Student=1 << 5;//医学生 100000
     static  final int Doctor=1 << 6;//兼职医生/普通医生 1000000 stone
     static  final int Profession=1 << 7;//专家 10000000

    static  final int AllCheckAprove =UnApproved^Approving^Approved^ApproveFailed;//是否需要检测认证状态 11110
    static  final int AllCheckType =Student^Doctor^Profession;//是否需要检测医生类型，11100000


    //此处设置集中常用的用户类型，如登录认证医生、专家，登录用户等等等等
    public static  final int loginUser= login;//登录用户
    public static  final int ApprovedDoctor= UserType.login| UserType.Approved|
            UserType.Doctor| UserType.Profession;//认证医生 11001001
}
