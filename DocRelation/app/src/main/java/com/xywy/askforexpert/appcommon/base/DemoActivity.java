package com.xywy.askforexpert.appcommon.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * YMBaseActivity 的使用示例
 * <p>
 * Created by bailiangjin on 16/7/7.
 */
public class DemoActivity extends YMBaseActivity {

    //TODO:YMBaseActivity 封装方法 简要说明

    //TODO:1、Toast方法 封装 shortToast（str or resId） longToast （str or resId）
    //TODO:2、统一标题栏封装  支持 自定义 扩展 titleBarBuilder.setTitleText("titleStr"); 隐藏 显示 hideCommonBaseTitle 显示：showCommonBaseTitle
    //TODO:3、统一状态栏样式绘制 可不使用 isHideBar() 返回true时 不使用该统一样式 默认返回false
    //TODO:4、统一进度条封装 loading进度条   showProgressDialog(String content) 或者 showProgressDialog()
    //TODO:5、统一 EditText 之外区域点击处理 点击非ditText区域隐藏键盘 dispatchTouchEvent（）
    //TODO:6、统一 Handler 提供 uiHandler.sendmsg(msg) Override handleMsg(Message msg);接收 msg 处理
    //TODO:7、统一提供 ButterKnife 的支持 直接 绑定控件 点击事件即可 Activity中不再需要单独初始化
    //TODO:8、统一提供 页面打点统计的默认支持
    //TODO:9、统一提供 Application级别的 Activity 的维护 管理 无论Activity 是否继承了BaseActivity

    //TODO:其他全局工具类
    //TODO:1、日志工具类 建议使用 LogUtils.d("log String"); 优点：可显示具体类 方法 行数 准确定位
    //TODO:2、图片加载工具类 建议使用 优点：可显示具体类 方法 行数 准确定位
    //TODO: 普通加载效果 ImageLoadUtils.INSTANCE.loadImageView(iv,url,defaultImgResId);
    //TODO: 加载圆角图片 ImageLoadUtils.INSTANCE.loadRoundedImageView(iv,url,defaultImgResId,cornerRadiusDp);
    //TODO: 加载圆形图片 ImageLoadUtils.INSTANCE.loadCircleImageView(iv,url,defaultImgResId);
    //TODO:3、图片加载工具类 建议使用 优点：可显示具体类 方法 行数 准确定位
    //TODO:4、通用Dialog封装 XywyPNDialog 建造者模式 按需设置参数 不设置的使用默认值
    //TODO:5、Gradle 配置关键参数
    //TODO:6、单例写法 建议用枚举实现 代码简洁 安全性好 示例 参见 GsonUtils ImageLoadUtils


    private static final String ID_INTENT_KEY = "ID_INTENT_KEY";

    private String id;


    //TODO: 建议为每个Activity 添加自己的启动调用方法 避免在其他调用处直接使用Intent 方式 增加代码可读性
    /**
     * 建议给每个Activity 添加其自身启动方法 供其他Activity 启动该Activity时调用
     *
     * @param activity
     * @param id
     */
    public static void start(Activity activity, String id) {
        Intent intent = new Intent(activity, DemoActivity.class);
        intent.putExtra(ID_INTENT_KEY, "id");
    }


    @Override
    protected int getLayoutResId() {
        //TODO:返回页面 layoutResId
        return 0;
    }

    @Override
    protected void beforeViewBind() {
        //TODO:intent 传入的参数的初始化赋值
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString(ID_INTENT_KEY);
    }

    @Override
    protected void initView() {
        //TODO:初始化View 建议不包括数据的初始化

    }

    @Override
    protected void initData() {
        //TODO:view的数据初始化
        //示例
        titleBarBuilder.setTitleText("标题栏名称");

        //TODO:初始化网络、数据库 等数据

        //TODO:其他后续逻辑
    }

}
