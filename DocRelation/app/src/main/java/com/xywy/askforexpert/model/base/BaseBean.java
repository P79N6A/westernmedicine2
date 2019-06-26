package com.xywy.askforexpert.model.base;

import javax.annotation.Generated;

/**
 * Compiler: Android Studio
 * Project: DocRelation
 * Author: Jack Fang
 * Email: fangqi@xywy.com
 * Date: 2016/6/20 10:21
 */

/**
 * 数据模型基类
 *
 * @param <D>
 */
@Generated("org.jsonschema2pojo")
public class BaseBean<D> {
    /**
     * code 当作 String 解析，增强解析适应性
     */
    private String code;
    private String msg;
    private String total;
    private D data;
    private D list;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getTotal() {
        return total;
    }

    public D getData() {
        return data;
    }

    public D getList() {
        return list;
    }
}
