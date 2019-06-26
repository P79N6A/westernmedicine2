package com.xywy.retrofit.cache;

/**
 * Author: zhangshupeng
 * Email: zhangshupeng@xywy.com
 * Date: 2016/9/8 9:49
 */
public interface FileNameGenerator {

    /** Generates unique file name for image defined by URI */
    String generate(String imageUri);
}
