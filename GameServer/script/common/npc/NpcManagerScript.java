package common.npc;

import com.data.CfgManager;
import com.data.bean.Cfg_Task_daily_Bean;
import com.game.monster.script.ITaskEntityIsShow;
import com.game.npc.structs.Npc;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.task.structs.*;
import com.game.utils.MessageUtils;
import game.core.map.IMapObject;
import game.core.script.IScript;
import game.message.MapMessage;
import game.message.MapMessage.ResRoundNpcDisappear;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * @author admin
 */
public class NpcManagerScript implements IScript, ITaskEntityIsShow {

    @Override
    public int getId() {
        return ScriptEnum.NpcManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 检测玩家能否看见这个npc，目前主要是任务检查
     *
     * @param player
     * @param entity
     * @return
     */
    @Override
    public boolean canSee(Player player, IMapObject entity) {
        Npc npc;
        if (entity instanceof Npc) {
            npc = (Npc) entity;
        } else {
            return true;
        }
        HashMap<Integer, List<Integer>> reulst = entity.gainHideTaskIds();
        if (reulst == null) {
            return true;
        } else {
            //主线任务
            if (reulst.containsKey(Task.MAIN_TASK)) {
                if (player.getCurMainTasks().size() > 0) {
                    if (reulst.get(Task.MAIN_TASK).contains(player.getCurMainTasks().get(0).getModelId())) {
                        return true;
                    }
                }
            }
            //日常任务
            if (reulst.containsKey(Task.DAILY_TASK)) {
                for (DailyTask dailyTask : player.getCurDailyTasks().values()) {
                    Cfg_Task_daily_Bean model = CfgManager.getCfg_Task_daily_Container().getValueByKey(dailyTask.getModelId());
                    if (model != null) {
                        if (npc.getModelId() == model.getNpc_id()) {
                            //如果任务完成就放回false，就是不能看见
                            if (!dailyTask.checkFinish(false, player)) {
                                return true;
                            }
                        }
                    }
                }
            }
            //支线任务
            if (reulst.containsKey(Task.BRANCH_TASK)) {
                for (BranchTask branchTask : player.getCurBranchTask()) {
                    if (reulst.get(Task.BRANCH_TASK).contains(branchTask.getModelId())) {
                        return true;
                    }
                }
            }
            //帮会任务
            if (reulst.containsKey(Task.GUILD_TASK)) {
                for (ConquerTask conquerTask : player.getCurConquerTasks().values()) {
                    if (conquerTask.getModelId() == 0) {
                        continue;
                    }
                    if (reulst.get(Task.GUILD_TASK).contains(conquerTask.getModelId())) {
                        return true;
                    }
                }
            }
            //转职任务
            if (reulst.containsKey(Task.GENDER_TASK)) {
                GenderTask genderTask = player.getCurGenderTask();
                if (reulst.get(Task.GENDER_TASK).contains(genderTask.getModelId())) {
                    return true;
                }
            }

            ResRoundNpcDisappear.Builder msg = ResRoundNpcDisappear.newBuilder();
            msg.addNpcIds(npc.getId());
            MessageUtils.send_to_player(player, MapMessage.ResRoundNpcDisappear.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return false;
        }
    }

}
