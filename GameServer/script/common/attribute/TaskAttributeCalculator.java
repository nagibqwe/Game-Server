package common.attribute;

import com.data.CfgManager;
import com.data.bean.Cfg_Changejob_Bean;
import com.data.bean.Cfg_Task_gender_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import com.game.task.structs.GenderTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lw
 */

public class TaskAttributeCalculator implements IAttributeScript {

    private static final Logger log = LogManager.getLogger(TaskAttributeCalculator.class);

    @Override
    public int getId() {
        return ScriptEnum.TaskAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.Task;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();
       // for (Cfg_TaskChapter_Bean bean : CfgManager.getCfg_TaskChapter_Container().getValuees()) {
       //     if(player.getOverMainTaskIDs().contains(bean.getId())){
       //         for (ReadArray<Integer> aii : bean.getFinish_att().getValuees()) {
       //             att.addAttribute(aii.get(0), aii.get(1));
       //         }
       //     }
       // }

        //转职属性加成
        GenderTask task = player.getCurGenderTask();
        Cfg_Task_gender_Bean bean = CfgManager.getCfg_Task_gender_Container().getValueByKey(task.getModelId());
        Cfg_Task_gender_Bean nextBean = CfgManager.getCfg_Task_gender_Container().getValueByKey(task.getModelId() + 1);
        if (bean == null) {
            return att;
        }
        int genderClass = bean.getGenderClass() - 1;
        if (nextBean == null || nextBean.getGenderClass() != bean.getGenderClass()) {
            if (task.isFinish()) {
                genderClass += 1;
            }
        }
        for (int i = 1; i <= genderClass; i++) {
            Cfg_Changejob_Bean changejobBean = CfgManager.getCfg_Changejob_Container().getValueByKey(i);
            if (changejobBean == null) {
                log.error("Cfg_Changejob_Bean转职配置表不存在：" + i);
                continue;
            }
            for (ReadArray<Integer> aii : changejobBean.getContribute_describe().getValuees()) {
                att.addAttribute(aii.get(0), aii.get(1));
            }
        }
        return att;
    }

    @Override
    public BaseSystemIntAttribute getPlayerSystemAttribute(Player player) {
        BaseSystemIntAttribute att = player.PlayerCalSystemCulators().get(getType());
        if (att == null) {
            att = new BaseSystemIntAttribute(AttributeType.SystemAttr_Max);
            player.PlayerCalSystemCulators().put(getType(), att);
        }
        att.clean();
        return att;
    }
}
