package com.game.behavior.manager;

import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.BehaviorType;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.structs.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 行为管理器， 主要管理几个时间机制的运行处理
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class BehaviorManager {

    private static final Logger log = LogManager.getLogger("BehaviorManager");

    /**
     * 插入排它性行为， 只允许队列中只有一个行为
     *
     * @param self 行为拥有者
     * @param bb 行为
     * @return 返回是否加成功
     */
    public static boolean InsertOnlyBehavior(Entity self, BaseBehavior bb) {
        if (bb != null) {
            //先取消此类型的其它行为
            for( BaseBehavior bbh : self.BehaviorList()){
                if( bbh.getType() == bb.getType()){
                    bbh.Over();
                }
            }
            self.BehaviorList().add(bb);
            return true;
        }
        return false;
    }

    /**
     * 插入行为
     *
     * @param self
     * @param bb
     * @return
     */
    public static boolean InsertBehavior(Entity self, BaseBehavior bb) {
        if (bb != null) {
            self.BehaviorList().add(bb);
            return true;
        }
        return false;
    }

    /**
     * 取消所有行为
     *
     * @param self
     * @return
     */
    public static boolean CancelAllBehavior(Entity self) {

        if (self.BehaviorList().isEmpty()) {
            return false;
        }

        //TD怪不能取消行为
        if (self instanceof Monster) {
            if (((Monster) self).isTdMonster()) {
                return false;
            }
        }

        try {
            for (BaseBehavior bb : self.BehaviorList()) {
                bb.Cancel();
            }
        } catch (Exception e) {
            log.error(self.getName() + "(" + self.getId() + ") 取消了所有的行为失败！");
            return false;
        }

        //log.info(self.getName() + "(" + self.getId() + ") 取消了所有的行为！");
        return true;
    }

    /**
     * 根据类型取消
     *
     * @param self
     * @param bt
     */
    public static void CancelBehaviorByType(Entity self, BehaviorType bt) {
        if (self.BehaviorList().isEmpty()) {
            return;
        }
        //TD怪不能取消行为
        if (self instanceof Monster) {
            if (((Monster) self).isTdMonster()) {
                return;
            }
        }

        for (BaseBehavior bb : self.BehaviorList()) {
            if (bb.getType() == bt) {
                bb.Cancel();
            }
        }
//        LOGGER.info(self.getName() + "(" + self.getId() + ") 取消了类型为" + bt + "的行为！");
    }

    /**
     * 检查是否有此类型的行为
     *
     * @param self
     * @param bt
     * @return
     */
    public static boolean HasBehavior(Entity self, BehaviorType bt) {
        if (self.BehaviorList().isEmpty()) {
            return false;
        }
        for (BaseBehavior bb : self.BehaviorList()) {
            if (bb.getType() == bt) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取某个正在执行的行为
     *
     * @param self
     * @param bt
     * @return
     */
    public static BaseBehavior GetBehavior(Entity self, BehaviorType bt) {
        if (self.BehaviorList().isEmpty()) {
            return null;
        }
        for (BaseBehavior bb : self.BehaviorList()) {
            if (bb.getType() == bt) {
                return bb;
            }
        }
        return null;
    }

}
