package com.game.jjc.structs;

import com.game.db.bean.JJCBean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.robot.script.IRobotScript;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import game.core.util.TimeUtils;
import java.util.List;

/**
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public class JJC extends JJCBean {

    public JJC() {
    }

    public JJC(JJCBean father) {

        this.roleId = father.getRoleId();
        this.score = father.getScore();
        this.career = father.getCareer();
        this.camp = father.getCamp();
        this.records = father.records;
    }


    public void setRecords(List<JJCReport> records) {
        this.records = records;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
        this.time = (int) (TimeUtils.Time() / 1000);
    }

    //初始化
    public Robot init() {
        Robot robot;
        Player player = Manager.playerManager.getPlayerCache(roleId);
        IRobotScript is = (IRobotScript) Manager.scriptManager.GetScriptClass(ScriptEnum.RobotBaseScript);
        if (player != null) {
            //修正一下免得出错了
            if (player.getAttribute().getAdditionValue(AttributeType.ATTR_Speed) == 0) {
                Manager.playerAttAttributeManager.deal().initPlayerAttribute(player, false);
            }
            robot = is.OnMake(player);
            return robot;
        }

        if (roleId > 5000001) {
            robot = is.OnMake(roleId);
        } else {
            robot = is.OnMakeByJJCConfig((int) roleId);
        }
        return robot;
    }
}
