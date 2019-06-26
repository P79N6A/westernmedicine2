package com.xywy.im.sdk;

/**
 * Created by xugan on 2018/8/3.
 * WebSockets状态码
 */

public class WebSocketsCode {
    /**
     * 正常关闭; 无论为何目的而创建, 该链接都已成功完成任务
     */
    public static final int CLOSE_NORMAL = 1000;

    /**
     * 终端离开, 可能因为服务端错误, 也可能因为浏览器正从打开连接的页面跳转离开
     */
    public static final int CLOSE_GOING_AWAY = 1001;

    /**
     * 由于协议错误而中断连接
     */
    public static final int CLOSE_PROTOCOL_ERROR = 1002;

    /**
     * 由于接收到不允许的数据类型而断开连接 (如仅接收文本数据的终端接收到了二进制数据)
     */
    public static final int CLOSE_UNSUPPORTED = 1003;

    /**
     * 表示没有收到预期的状态码
     */
    public static final int CLOSE_NO_STATUS = 1005;

    /**
     * 用于期望收到状态码时连接非正常关闭 (也就是说, 没有发送关闭帧)
     */
    public static final int CLOSE_ABNORMAL = 1006;

    /**
     * 由于收到了格式不符的数据而断开连接 (如文本消息中包含了非 UTF-8 数据)
     */
    public static final int UNSUPPORTED_DATA = 1007;

    /**
     * 由于收到不符合约定的数据而断开连接。 这是一个通用状态码, 用于不适合使用 1003 和 1009 状态码的场景
     */
    public static final int POLICY_VIOLATION = 1008;

    /**
     * 由于收到过大的数据帧而断开连接
     */
    public static final int CLOSE_TOO_LARGE = 1009;

    /**
     * 客户端期望服务器商定一个或多个拓展, 但服务器没有处理, 因此客户端断开连接
     */
    public static final int MISSING_EXTENSION = 1010;

    /**
     * 客户端由于遇到没有预料的情况阻止其完成请求, 因此服务端断开连接
     */
    public static final int INTERNAL_ERROR = 1011;

    /**
     * 服务器由于重启而断开连接
     */
    public static final int SERVICE_RESTART = 1012;

    /**
     * 服务器由于临时原因断开连接, 如服务器过载因此断开一部分客户端连接
     */
    public static final int TRY_AGAIN_LATER = 1013;

    /**
     * 表示连接由于无法完成 TLS 握手而关闭 (例如无法验证服务器证书)
     */
    public static final int TLS_HANDSHAKE = 1015;
}
