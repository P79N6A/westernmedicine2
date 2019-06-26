package com.xywy.component.datarequest.neworkWrapper;

/**
 * 统一的错误码
 */
public class Errors {

    public static final int BASE_PARSER_ERROR = -1; //基类解析错误
    public static final int BASE_GSON_ERROR = -2;//GSON解析出错
    public static final int BASE_NO_ERROR = 10000; //基类解析错误
    public static final String BASE_NO_MSG = "HAS_NO_MSG";
    public static final String ERROR_MSG = "CATCH_A_EXCEPTION";
}
