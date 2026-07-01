package com.game.guildactivity.struct;

import com.game.count.structs.BooleanCount;
import com.game.count.structs.VariantType;
import com.game.marriage.struct.MarryTask;

/**
 * @Desc TODO
 * @Date 2021/12/29 11:25
 * @Auth ZUncle
 */
public enum GuildFudAlloc implements BooleanCount {
    Update0(0),
    Update1(1),
    Update2(2),
    Update3(3),
    Update4(4),
    Update5(5),
    Update6(6),
    Update7(7),
    Update8(8),
    Update9(9),
    Update10(10),
    Update11(11),
    Update12(12),
    ;
    final int key;

    GuildFudAlloc(int key) {
        this.key = key;
    }

    @Override
    public long getKey() {
        return key;
    }

    @Override
    public VariantType getVariantType() {
        return VariantType.GuildFudi;
    }

    public static GuildFudAlloc find(int id) {
        for (GuildFudAlloc task : GuildFudAlloc.values()) {
            if (task.getKey() == id) {
                return task;
            }
        }
        return null;
    }
}
