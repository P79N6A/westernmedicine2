package com.xywy.im.sdk;

/**
 * Created by xugan on 2018/7/16.
 */

public class Constant {
    /**
     * Client request to connect to Server
     */
    public static int CONNECT = 1;

    /**
     * Server to Client: Connect acknowledgment
     */
    public static int CONNECT_ACK = 2;

    /**
     * Publish message
     */
    public static int PUBLISH = 3;

    /**
     * Publish acknowledgment
     */
    public static int PUB_ACK = 4;

    /**
     * Group Publish message
     */
    public static int GROUP_PUBLISH = 5;

    /**
     * Group Publish acknowledgment
     */
    public static int GROUP_PUB_ACK = 6;

    /**
     * PING request
     */
    public static int PING = 7;

    /**
     *  PING response
     */
    public static int PING_RESP = 8;

    /**
     *  Client or Server is disconnecting
     */
    public static int DISCONNECT = 9;

    /**
     *  invalid packet
     */
    public static int INVALID_PACKET = 10;

    /**
     *  Group Part Publish message
     */
    public static int GROUP_PART_PUBLISH = 11;

    /**
     *  Group Part Publish acknowledgment
     */
    public static int GROUP_PART_PUB_ACK = 12;

    /**
     *  Group Part Publish acknowledgment
     */
    public static int LOGOUT = 20;


    /**
     *  连接成功
     */
    public static int  CONNECTION_ACCEPTED = 0;

    /**
     *  重复建立连接
     */
    public static int  CONNECTION_DUP = 1;

    /**
     *  vhost非法
     */
    public static int  INVALID_VHOST = 2;

    /**
     *  vhost非法
     */
    public static int  USERNAME_OR_PASSWORD_IS_ERROR = 3;


    public static String FAILBACK  = "failback";

    public static String BSID_RTQA  = "xyb";

    /**
     * 占1个byte, 表示连接对应的端，相同的端只允许同时一台设备连接, 取值: 1 .. 127
     * client_id: 1 代表pc端，2：app端， 3：3g端
     */
    public static int CLIENTID = 2;
}
