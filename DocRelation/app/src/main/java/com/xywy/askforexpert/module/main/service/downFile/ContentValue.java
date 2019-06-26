package com.xywy.askforexpert.module.main.service.downFile;


public interface ContentValue {

    String SERVICE_TYPE_NAME = "servicetype"; // 通过Intent获取启动服务类型的名字
    String DOWNLOAD_TAG_BY_INTENT = "downloadurl"; // 通过Intent获取url的名字
    String CACHE_DIR = "cacheDir"; // 配置文件中,存储目录名称
    int START_DOWNLOAD_MOVIE = 99; // 启动下载
    int ERROR_CODE = -1; // 出错
    int START_DOWNLOAD_LOADITEM = 10; // 从数据库中装载下载任务
    int START_DOWNLOAD_ALLSUSPEND = 11; // 将数据库中所有的下载状态设置为 暂停

    // 下载状态
    /**
     * 正在下载
     */
    int DOWNLOAD_STATE_DOWNLOADING = 2;
    /**
     * 暂停
     */
    int DOWNLOAD_STATE_SUSPEND = 3;
    /**
     * 等待
     */
    int DOWNLOAD_STATE_WATTING = 4;
    /**
     * 下载失败
     */
    int DOWNLOAD_STATE_FAIL = 5;
    /**
     * 下载成功
     */
    int DOWNLOAD_STATE_SUCCESS = 6;
    /**
     * 开始下载
     */
    int DOWNLOAD_STATE_START = 7;
    /**
     * 任务被删除
     */
    int DOWNLOAD_STATE_DELETE = 8;
    /**
     * 清除所有任务
     */
    int DOWNLOAD_STATE_CLEAR = 9;
    /**
     * 未下载的状态
     */
    int DOWNLOAD_STATE_NONE = 0;
    /**
     * 如果当前状态不在三个之内的下载队列中 讲自身设置为"等待中
     */
    int DOWNLOAD_STATE_EXCLOUDDOWNLOAD = 12;

}
