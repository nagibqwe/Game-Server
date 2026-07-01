package com.game.bi.enums;

public enum EquipType {
    role(1,"角色装备"),
    immortal(2,"仙甲"),
    holy(3,"圣装"),
    soulBeast(4,"神兽装备"),
    role3(5,"灵体3阶"),
    role4(6,"灵体4阶"),
    role5(7,"灵体5阶"),
    role6(8,"灵体6阶"),
    role7(9,"灵体7阶"),
    role8(10,"灵体8阶"),
    role9(11,"灵体9阶"),
    role10(12,"灵体10阶"),
    role11(13,"灵体11阶"),
    role12(14,"灵体12阶"),
    role13(15,"灵体13阶"),
    role14(16,"灵体14阶"),
    role15(17,"灵体15阶"),
    role16(18,"灵体16阶"),
    immortal2(19,"第二套仙甲"),
    immortal3(20,"第三套仙甲"),
    pet(21,"宠物装备"),
    soulArmor(22,"魂甲"),
    horse(23,"坐骑装备"),
    ;
    public int type;
    public String desc;
    private EquipType (int type, String desc){
        this.type = type;
        this.desc = desc;
    }
}
