package com.game.equip.struct;
/**
 * 由于equip_inten_main表中会有玩家部位强化数据，宠物强化数据等
 * 故使用该类予以区分表中不同类型的数据
 * 该类中定义的公共静态常量表示部位的大类别，比如玩家，宠物等
 * 每个常量的取值应该是equip_inten_main表中的Type字段的基础值，比如
 * 玩家部位强化数据的Type是1xx，那么这个类中的PLAYER常量取值为100
 * */
public class EquipPartBaseType {
    /**
     * 玩家装备部位
     * */
    public final static int PLAYER = 100;
    /**
     * 宠物装备部位
     * */
    public final static int PET = 200;
}
