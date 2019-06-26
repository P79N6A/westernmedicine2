package com.xywy.component.datarequest.uploadLog;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/8/15 17:23
 */

//负责Log的写入和上传接口
public interface ILog {
    void writeLog(String content);

    void uploadLog();
}
