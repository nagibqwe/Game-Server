package com.game.player.structs;

import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/7/28 15:22
 * @Auth ZUncle
 */
public class Item {

    /**
     * 道具ID
     */
    int modelId;
    /**
     * 数量
     */
    long count;
    /**
     * 是否绑定
     */
    boolean bind;

    public int getModelId() {
        return modelId;
    }

    public Item setModelId(int modelId) {
        this.modelId = modelId;
        return this;
    }

    public long getCount() {
        return count;
    }

    public Item setCount(long count) {
        this.count = count;
        return this;
    }

    public boolean isBind() {
        return bind;
    }

    public Item setBind(boolean bind) {
        this.bind = bind;
        return this;
    }

    public static List<Item> createItems(ReadIntegerArrayEs es) {
        List<Item> items = new ArrayList<>();
        for (ReadArray<Integer> e : es.getValuees()) {
            Item item = new Item();
            item.setModelId(e.get(0));
            item.setCount(e.get(1));
            item.setBind(e.get(2, 0) == 1);
            items.add(item);
        }
        return items;
    }

    public static List<Item> createItems(int itemId, long count, boolean bind) {
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setModelId(itemId);
        item.setCount(count);
        item.setBind(bind);
        items.add(item);
        return items;
    }
}
