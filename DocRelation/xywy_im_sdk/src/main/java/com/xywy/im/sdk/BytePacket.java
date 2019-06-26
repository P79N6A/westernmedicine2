package com.xywy.im.sdk;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by xugan on 2018/7/17.
 */

public class BytePacket {
    static public void writeInt64(long v, byte[] dst, int pos) {
        ByteBuffer b = ByteBuffer.allocate(8);
        b.order(ByteOrder.BIG_ENDIAN);
        b.putLong(v);
        byte[] t = b.array();
        System.arraycopy(t, 0, dst, pos, t.length);
    }

    static public void writeInt32(int v, byte[] dst, int pos) {
        ByteBuffer b = ByteBuffer.allocate(4);
        b.order(ByteOrder.BIG_ENDIAN);
        b.putInt(v);
        byte[] t = b.array();
        System.arraycopy(t, 0, dst, pos, t.length);
    }

    static public void writeInt16(short v, byte[] dst, int pos) {
        ByteBuffer b = ByteBuffer.allocate(2);
        b.order(ByteOrder.BIG_ENDIAN);
        b.putShort(v);
        byte[] t = b.array();
        System.arraycopy(t, 0, dst, pos, t.length);
    }

    static public long readInt64(byte[] bytes, int pos) {
        ByteBuffer b = ByteBuffer.wrap(bytes, pos, 8);
        b.order(ByteOrder.BIG_ENDIAN);
        return b.getLong();
    }

    static public int readInt32(byte[] bytes, int pos) {
        ByteBuffer b = ByteBuffer.wrap(bytes, pos, 4);
        b.order(ByteOrder.BIG_ENDIAN);
        return b.getInt();
    }

    static public short readInt16(byte[] bytes, int pos) {
        ByteBuffer b = ByteBuffer.wrap(bytes, pos, 2);
        b.order(ByteOrder.BIG_ENDIAN);
        return b.getShort();
    }

}
