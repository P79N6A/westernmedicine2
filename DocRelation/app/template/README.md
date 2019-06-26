# 自定义模板

## 简介
1、该目录下存放的为 BaseActivity、BaseAdapter 等自定义的class的模板
2、使用模板创建新的Class 实例可以减少代码书写量、方便简捷。

## 使用说明
1、将template 下的各个模板 拷贝到 Android studio 安装目录下 template目录下对应的 具体的目标路径

2、示例 
以Mac为例：
例如 YMBaseActivity 模板 只需将工程目录下的 YMBaseActivity文件夹 整体拷贝至 
/Applications/Android\ Studio.app/Contents/plugins/android/lib/templates/activities/目录下即可
完成后 YMBaseActivity模板 对应的路径为
/Applications/Android\ Studio.app/Contents/plugins/android/lib/templates/activities/YMBaseActivity 

Windows 拷贝至Android相应的 安装目录（/Contents/plugins/android/lib/templates/）即可 

3、参考博文
[神奇的Android Studio Template](http://blog.csdn.net/lmj623565791/article/details/51592043)

[Android Studio自定义模板 写页面竟然可以如此轻松](http://blog.csdn.net/lmj623565791/article/details/51635533)


4、file_template_setting 导入


选择 File==> Import Setting 选择 file_template_setting_ym.jar 文件 按提示 导入 

导入 后文件模板中会多出一个名为 YMBaseAdapter 的模板 新建继承于YMBaseAdapter 的Adapter时可以使用

5、使用以上模板创建的Activity 和Adapter 分别继承了YMBaseActivity 和YMBaseAdapter
使用方式可以参考：
com.xywy.askforexpert.appcommon.base.DemoActivity.java
和
com.xywy.askforexpert.appcommon.base.DemoAdaper.java



