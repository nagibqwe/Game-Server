package com.game.buff.structs;


public class BuffDefine {

    public static final int Type_None = 0; //0：无效果
    public static final int Type_Attribute = 1;// 1：属性
    public static final int Type_HpPool = 2; //2：血池
    public static final int Type_DecHp = 3;//3：掉血 
    public static final int Type_DecAllHpRate = 4;// 4：掉血总值万分比 
    public static final int Type_DecCurHpRate = 5;// 5：掉血当前值万分比 
    public static final int Type_AddHp = 6;// 6：治疗
    public static final int Type_addAllHpRate = 7; //7：治疗总值万分比 
    public static final int Type_addCurHpRate = 8; //7：治疗当前值万分比
    public static final int Type_SuperMan = 9; //7：霸体状态
    public static final int Type_MoneyRate = 10; //7：金币倍率 param1：倍率万分比
    public static final int Type_ExpRate = 11; //7：经验倍率 param1：倍率万分比
    public static final int Type_Guiying = 12; //鬼影buff
    public static final int Type_SuperPveBuff = 13; //击杀周围怪物
    public static final int Type_RoleInvisible = 14;//角色隐身
    public static final int Type_DING = 15;//定身BUFF
    public static final int Type_MiaoKang = 16;//免控BUFF
    public static final int Type_ReDamageFromBoss = 17;// boss收到的伤害按比例施加到玩家身上
    public static final int Type_BigBoom = 18;// 给player或者monster挂一个,结束时候炸周围的人固定伤害
    public static final int Type_PosTriggerBuff = 19;// 位置触发事件的buff
    public static final int Type_TriggerSummonBuff = 20;// 触发召唤物的buff
    public static final int Type_Dizziness = 21;//眩晕的BUFF
    public static final int Type_Bianshen = 22; //变身buff
    public static final int Type_Chuandao= 23; //传道buff
    public static final int Type_Feijian = 24; //飞剑buff
    public static final int Type_jinliao = 25; //禁疗

    //脱战清除枚举
    public static final int LeaveBattleUnClean = 0; //脱战不清除
    public static final int LeaveBattleClean = 1; //脱战清除

    //死亡清除枚举
    public static final int DieUnClean = 0; //死亡不清除
    public static final int DieClean = 1; //死亡清除
    
    //过地图清除枚举
    public static final int ChangeMapUnClean = 0; //不清除
    public static final int ChangeMapClean = 1; //清除
    //计时枚举
    public static final int StoreType_UnStore = 0; //不保存
    public static final int StoreType_OnlineCalc = 1; //在线计时
    public static final int StoreType_offLineCalc = 2; //下线也计时

    public enum BuffExParamType {

        ADD_CONDI(1), // 添加条件
        LOGIC_AREAIN(2), // 位置触发buff时,用于指定触发进入事件时候的行为
        LOGIC_AREAOUT(3), // 位置触发buff时,用于指定触发出范围事件时候的行为
        TRIGGER_CODI(4), // 触发条件
        NORMAL_TRIGGER_ACTION(5),// 通用触发事件 add和action
        ;

        private final int val;

        BuffExParamType(int v) {
            val = v;
        }

        public int value() {
            return val;
        }
    }

    public enum CondiType {

        EXIST_BUFF(1), // 当前拥有某些buff
        ;

        private final int val;

        CondiType(int v) {
            val = v;
        }

        public int value() {
            return val;
        }
    }

    // 一些行为定义
    public enum ACTION_TYPE {

        ADDBUFF(0), // 添加buff行为
        REMOVEBUFF(1), // 删除buff行为
        CREATESUMMON(2), // 创建召唤物
        ;

        public final int v;

        ACTION_TYPE(int v) {
            this.v = v;
        }
    }

}
