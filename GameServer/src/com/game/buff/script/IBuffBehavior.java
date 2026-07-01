package com.game.buff.script;

import com.game.buff.structs.Buff;
import com.game.structs.Fighter;

/**
 * @Desc TODO buff效果
 * @Date 2020/7/31 16:13
 * @Auth ZUncle
 */
public interface IBuffBehavior {

    /**
     * 增加buff,会在buff被增加到目标身上之前被调用
     *
     * @param source 来源
     * @param target 对象
     * @return
     */
    int add(Buff buff, Fighter source, Fighter target);

    /**
     * buff 叠加
     * @param buff
     * @param owner
     */
    void overlap(Buff buff, Fighter owner);

    /**
     * 移除buff,会在buff从目标身上移除之前被调用
     *
     * @param owner buff拥有者
     * @return
     */
    int remove(Buff buff, Fighter owner);

    /**
     * buff作用 定时处理
     *
     * @param attacker 来源
     * @param owner  buff拥有者
     * @return 作用数值 0 忽略 -1执行删除buff
     */
    int action(Buff buff, Fighter attacker, Fighter owner);

    /**
     * buff 时间到时候的事件触发
     *
     * @param target
     * @return
     */
    int timeout(Buff buff, Fighter target);

}
