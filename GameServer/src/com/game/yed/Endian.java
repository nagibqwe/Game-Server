package com.game.yed;

/**
 * Created by huhu on 2017/7/24.
 */
// LittleEndian BinaryBuffer.
public enum Endian {
    Little(0),
    Big(1),;

    public final int type;

    Endian(int type) {
        this.type = type;
    }
}
