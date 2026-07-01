package com.game.marriage.struct;

import com.game.count.structs.BooleanCount;
import com.game.count.structs.VariantType;

/**
 * @Desc TODO
 * @Date 2021/1/7 11:33
 * @Auth ZUncle
 */
public enum MarryTask implements BooleanCount {
    None(0),
    AsFriend(1),    //1：和异性成为好友
    Reach520(2),    //2：和一位异性亲密度达到520
    ReachMarry(3),  //3：发送或接收到金玉良缘或以上的求婚
    Marry(4),       //4：成功缔结一次仙缘
    OrderWedding(5),//5：成功预约一场婚礼
    Wedding(6),     //6：成功举办一场婚礼
    All(7),         //7完成所有任务可领取绝版仙娃【小福】
    ;
    final int key;

    MarryTask(int key) {
        this.key = key;
    }

    @Override
    public long getKey() {
        return key;
    }

    @Override
    public VariantType getVariantType() {
        return VariantType.MarryTask;
    }

    public static MarryTask find(int id) {
        for (MarryTask task: MarryTask.values()){
            if (task.getKey() == id){
                return task;
            }
        }
        return None;
    }
}
