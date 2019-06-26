    一、添加静态代码检查功能
    1、使用submodule导入本项目
     2、root项目下的build.gradle文件中修改如下:
     //添加统一jar包依赖
       apply from: 'buildsystem/dependencies.gradle'
       ext {
       //android_common项目地址
           androidCommonDir = "${project.rootDir}/libs";
       }
       apply from: 'buildsystem/rootProjectConfig.gradle'
       buildscript {
       //一定要加否则在buildscript中找不到依赖
           apply from: 'buildsystem/dependencies.gradle'
           repositories {
               jcenter()
               mavenCentral()
               //添加插件仓库地址
               maven { url 'https://plugins.gradle.org/m2/' }
           }
           dependencies {
               classpath gradlePlugins.android
               classpath gradlePlugins.jacoco
               classpath gradlePlugins.dexcount
               // NOTE: Do not place your application dependencies here; they belong
               // in the individual module build.gradle files
           }
       }
    3、在需要使用静态代码检测的module模块中的build.gradle文件添加如下：
  apply from: "${androidCommonDir}/buildsystem/commonModuleConfig.gradle"
  apply from: "${androidCommonDir}/code_quality_tools/quality.gradle"
    4、在quality.gradle中可以修改如下两行
   def codeAnalysisSwitch = true;//本行可以开启或关闭静态代码检测
   def stopWhenFail = false;//检测到错误时停止编译

   5.执行check:gradle/app/Tasks/verification/check
     查看html:module下/build/reports/findbugs/findbugs.html
