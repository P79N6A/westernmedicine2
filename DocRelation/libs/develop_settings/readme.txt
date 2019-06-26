    一、开发者设置选项，包括项目版本号构建时间，tinydancer 屏幕fps检测，LeakCanary内存泄漏检查，
    手机查看Log，stetho插件在chrome上查看布局、sp、数据库等，
    二、添加到项目步骤，注意模块的地址路径：
     1、在setting.gradle文件中添加:
           include ':libs/develop_settings'
           2、 root项目下的build.gradle文件中修改如下:
                    apply from: 'libs/buildsystem/dependencies.gradle'
                    ext {
                    //develop_setting模块的地址
                        androidCommonDir = "${project.rootDir}/libs";
                    }
        3、在主module中添加如下依赖，注意路径:
        debugCompile project(':libs/develop_settings')
        2、在application中添加如下代码：
         if (BuildConfig.DEBUG){
                    try {
                        //反射初始化app develop setting，检测摇晃手机启动设置页面
                        Class aClass = Class.forName("com.xywy.develop_settings.DevelopSettingManager");
                        Method method = aClass.getMethod("init", Application.class);
                        System.out.println(method.invoke(null, this));
                    } catch (Exception e){
                        //Roboletric测试时leakcanary会有空指针问题
                    }
                }

     三、使用
      1、stetho在chrome地址栏访问chrome://inspect，第一次访问需翻墙，之后就可在chrome上查看布局、sp、数据库等，
