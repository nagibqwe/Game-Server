package com.game.server.struct;

/**
 * @Desc TODO
 * @Date 2021/6/9 14:51
 * @Auth ZUncle
 */
public class MessageEvent<K,V> {
    private K key;
    private V val;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getVal() {
        return val;
    }

    public void setVal(V val) {
        this.val = val;
    }
}
