# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/wangpeng/Desktop/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# 代码混淆压缩比，在0~7之间，默认为5,一般不下需要修改
-optimizationpasses 5

# 混淆时不使用大小写混合，混淆后的类名为小写
# windows下的同学还是加入这个选项吧(windows大小写不敏感)
-dontusemixedcaseclassnames

# 指定不去忽略非公共的库的类
# 默认跳过，有些情况下编写的代码与类库中的类在同一个包下，并且持有包中内容的引用，此时就需要加入此条声明
-dontskipnonpubliclibraryclasses

# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers

# 不做预检验，preverify是proguard的四个步骤之一
# Android不需要preverify，去掉这一步可以加快混淆速度
-dontpreverify

# 有了verbose这句话，混淆后就会生成映射文件
# 包含有类名->混淆后类名的映射关系
# 然后使用printmapping指定映射文件的名称
-verbose
-printmapping priguardMapping.txt

# 指定混淆时采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不改变
-optimizations !code/simplification/artithmetic,!field/*,!class/merging/*

# 保护代码中的Annotation不被混淆
# 这在JSON实体映射时非常重要，比如fastJson
-keepattributes *Annotation*

# 避免混淆泛型
# 这在JSON实体映射时非常重要，比如fastJson
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 需要保留的东西 #
# 保留所有的本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留了继承自Activity、Application这些类的子类
# 因为这些子类有可能被外部调用
# 比如第一行就保证了所有Activity的子类不要被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

# 保留Activity中的方法参数是view的方法，
# 从而我们在layout里面编写onClick就不会影响
-keepclassmembers class * extends android.app.Activity {
    public void * (android.view.View);
}

# 枚举类不能被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留自定义控件(继承自View)不能被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(***);
    *** get* ();
}

# 保留Parcelable序列化的类不能被混淆
-keep class * implements android.os.Parcelable{
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   private static final java.io.ObjectStreamField[] serialPersistentFields;
   !static !transient <fields>;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}

# 对R文件下的所有类及其方法，都不能被混淆
-keep public class com.xywy.askforexpert.R$*{
    public static final int *;
}
-keepclassmembers class **.R$* {
    *;
}

# 对于带有回调函数onXXEvent的，不能混淆
-keepclassmembers class * {
    void *(**On*Event);
}

# 自定义混淆部分 #
# 保留所有实体类

-keep class com.xywy.askforexpert.model.** {
    *;
}

-keep class com.xywy.askforexpert.newdrelation.entity.** {
    *;
}

# 内部类不混淆
-keep class *$* {
    *;
}

# WebView混淆
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient{
    public void *(android.webkit.WebView, java.lang.String);
}

# android support v4包
-dontwarn android.support.v4.**
-dontwarn **CompatHoneycomb
-dontwarn **CompatHoneycombMR2
-dontwarn **CompatCreatorHoneycombMR2
-keep interface android.support.v4.app.** {
    *;
}
-keep class android.support.v4.** {
    *;
}
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

# Gson混淆
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

#支付宝相关防止混淆
#-libraryjars libs/alipaySdk-20161222.jar

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

# umeng混淆
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* { *; }

-keep public class com.umeng.fb.ui.ThreadView { }
-keep class com.umeng.fb.**
-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** { *; }
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage { *; }
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class im.yixin.sdk.api.YXMessage { *; }
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{ *; }

-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** { *; }
-dontwarn com.tencent.**
-keep public class com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.example.R$*{
    public static final int *;
}
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* { *; }
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog { *; }
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* { *; }

-keep class com.sina.** { *; }
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keepattributes Signature

#mupdf
-dontwarn com.artifex.mupdfdemo.**
-keep class com.artifex.mupdfdemo.** { *; }

#photoview
-dontwarn uk.co.senab.photoview.**
-keep class uk.co.senab.photoview.** { *; }

#zxing
-dontwarn com.google.zxing.**
-keep class com.google.zxing.** { *; }

#image-loader
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }

#realm
-dontwarn io.realm.**
-keep class io.realm.** { *; }
-keep public class * extends io.realm.RealmObject

# afinal混淆
-dontwarn net.tsz.afinal.**
-keep class net.tsz.afinal.** { *; }
-keep public class * extends net.tsz.afinal.**
-keep public interface net.tsz.afinal.** {*;}
-keepclasseswithmembers class shzb.zhinaibo.base.** {
     <fields>;
     <methods>;
}

#nineoldandroids
-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** { *; }

#butter knife
-keep public class * implements butterknife.internal.ViewBinder { public <init>(); }
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#解决这个问题Error:Execution failed for task ':app:packageRelease'.
#> Unable to compute hash of /Users/denghan/yimai/YMAS/code/app/build/intermediates/classes-proguard/release/classes.jar
#-dontwarn java.nio.file.Files
#-dontwarn java.nio.file.Path
#-dontwarn java.nio.file.OpenOption
#-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#com.xywy.askforexpert.module.healthrecord


# aar 包

-dontwarn com.xywy.d_platform_n.**
-keep class com.xywy.d_platform_n.** { *; }
-keep class * implements com.xywy.d_platform_n.NumberPicker.Formatter{
    *;
}

# 网络请求
-dontwarn com.xywy.askforexpert.appcommon.net.**
-keep class com.xywy.askforexpert.appcommon.net.** { *; }


#公共View
-dontwarn com.xywy.askforexpert.widget.**
-keep class com.xywy.askforexpert.widget.** { *; }

-dontwarn com.xywy.askforexpert.appcommon.view.**
-keep class com.xywy.askforexpert.appcommon.view.** { *; }



# 环信
-keep class com.hyphenate.** { *; }
-dontwarn com.hyphenate.**
-keep class com.easemob.** { *; }
-keep class org.jivesoftware.** { *; }
-keep class org.apache.** { *; }
-dontwarn com.easemob.**

#2.0.9后的不需要加下面这个keep
#-keep class org.xbill.DNS.** {*;}
#另外，demo中发送表情的时候使用到反射，需要keep SmileUtils,注意前面的包名，
#不要SmileUtils复制到自己的项目下keep的时候还是写的demo里的包名
-keep class com.xywy.easeWrapper.utils.SmileUtils { *; }
#2.0.9后加入语音通话功能，如需使用此功能的api，加入以下keep
-dontwarn ch.imvs.**
-dontwarn org.slf4j.**
-keep class org.ice4j.** { *; }
-keep class net.java.sip.** { *; }
-keep class org.webrtc.voiceengine.** { *; }
-keep class org.bitlet.** { *; }
-keep class org.slf4j.** { *; }
-keep class ch.imvs.** { *; }

# JPush
-dontwarn cn.jpush.**
-keepattributes  EnclosingMethod,Signature
-keep class cn.jpush.** { *; }
-keepclassmembers class ** {
    public void onEvent*(**);
}

#xywy统计
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses
-keep class com.xywy.sdk.stats.**{ *; }

# 统计分析
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep class pl.openrnd.multilevellistview.**

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**
#rx
-dontwarn rx.**
-keep class rx.** { *; }

# JSoup
-dontwarn org.jsoup.**
-keep public class org.jsoup.** {
    public *;
}

# rxjava
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

# LetvPlayer
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepattributes *JavascriptInterface*
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
#-ignorewarnings
-dontwarn com.avdmg.avdsmart.**
-dontwarn com.lecloud.**
-dontwarn com.letv.adlib.**
-dontwarn com.letv.play.**
-dontwarn org.rajawali3d.**
-keep class android.support.** { *; }
-keep class android.volley.** { *; }
-keep class com.summerxia.dateselector.** { *; }
-keep class com.viewpagerindicator.** { *; }
-keep class org.xutils.** { *; }
-keep class com.google.gson.** { *; }
-keep class org.apache.** { *; }
-keep class com.letv.autoapk.dao.** { *; }
-keep class master.flame.danmaku.** { *; }
-keep class org.java_websocket.** { *; }
-keep class pl.droidsonroids.gif.** { *; }
-keep class com.letv.skin.** { *; }
-keep class com.letv.skin.* {
        public <fields>;
        public <methods>;
}


#######################################################umeng
-keep class org.android.** { *; }
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class com.lecloud.lecloudsdkdemo.R$*{
 public static final int *;
 public static final int[] *;
}

-keep class com.squareup.wire.* {
        public <fields>;
        public <methods>;
}

-keep class org.android.agoo.impl.*{
        public <fields>;
        public <methods>;
}

-keep class org.android.agoo.service.* {*;}
-keep class org.android.spdy.**{*;}

##################################################lecloudsdk
-keep public class * extends com.lecloud.js.http.parser.BaseJsParser

-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}
-keepclassmembers class com.letv.controller.PlayProxy{
    public <fields>;
    public <methods>;
}

-keepclassmembers class com.letv.controller.PlayContext{
    public <fields>;
    public <methods>;
}

-keepclassmembers class com.letv.controller.LetvPlayer{
    public <fields>;
    public <methods>;
}
-keepclassmembers class com.lecloud.entity.LiveInfo{
    public <fields>;
    public <methods>;
}

-keepclassmembers class com.lecloud.entity.ActionInfo{
    public <fields>;
    public <methods>;
}
-keep class com.letv.adlib.** { *;}
-keep class android.webkit.** { *;}
-keep class com.lecloud.** {*;}
-keep class com.letvcloud.cmf.** {*;}
-keep class android.os.SystemProperties
-keepclassmembers class android.os.SystemProperties{
    public <fields>;
    public <methods>;
}
-ignorewarnings
-keep class com.letv.play.db.DBHelper{ *; }
-keep class com.letv.controller.** { *;}
-keep class com.letv.proxy.**{ *; }
-keep class com.letv.universal.** { *; }
-keep class com.letv.admodule.** { *; }
-keep class org.rajawali3d.** { *; }
-keep class cn.com.iresearch.mvideotracker.** { *;}
-keep class com.letv.pano.** { *;}
-keepclassmembers class com.letv.pano.PanoRenderer{
    public <fields>;
    public <methods>;
}
-keep class com.letv.socks.library.** { *;}
-keep class com.avdmg.avdsmart.** { *;}
-keep class sun.misc.Unsafe {
    <fields>;
    <methods>;
}

#直播库依赖于主工程buildconfig
-keep class com.xywy.livevideo.*
-keep class com.xywy.livevideo.entity.** { *; }
-keep class com.xywy.livevideo.common_interface.IDataResponse {
    *;
}
-keep class com.xywy.askforexpert.BuildConfig {
    *;
}
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
-keep  class com.xywy.askforexpert.module.main.service.que.model.** {
     *;
 }
#闻康医生助手添加的混淆
-keep class com.xywy.askforexpert.module.discovery.medicine.module.account.beans.** {
     *;
 }

 -keep class com.xywy.askforexpert.module.discovery.medicine.module.account.model.** {
     *;
 }

 -keep class com.xywy.askforexpert.module.discovery.medicine.module.medical.entity.** {
     *;
 }

 -keep class com.xywy.askforexpert.module.discovery.medicine.module.patient.entity.** {
     *;
 }

 -keep class com.xywy.askforexpert.module.discovery.medicine.module.patient.view.** {
     *;
 }

 -keep class com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.entity.** {
     *;
 }

 -keep class com.xywy.askforexpert.module.discovery.medicine.module.personalinfo.model.** {
     *;
 }

 -keep class com.xywy.askforexpert.module.discovery.medicine.module.web.webview.** {
     *;
 }

 -keep class com.xywy.askforexpert.module.discovery.medicine.view.** {
     *;
 }

#极光
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#==================gson && protobuf==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}

#科大讯飞
-keep class com.iflytek.**{*;}
-keepattributes Signature
-keep  class com.xywy.askforexpert.module.drug.bean.** {
     *;
 }
 -dontwarn cn.org.bjca.**
 -keep class cn.org.bjca.**{ *;}
 -keep interface cn.org.bjca.**{ *;}