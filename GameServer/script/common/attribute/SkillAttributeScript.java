package common.attribute;

import com.data.CfgManager;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.attribute.script.IAttributeScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.MentalSkill;
import com.game.skill.structs.Skill;
import com.game.skill.structs.SkillDefine;
import com.game.structs.AttributeType;
import com.game.vip.structs.VipPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lw
 */
public class SkillAttributeScript implements IAttributeScript {

    private static final Logger log = LogManager.getLogger(SkillAttributeScript.class);

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.Skill;
    }

    private int Vip_PowerPra = 37;//VIP经验加成

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();
        player.getSkilladds().clear();

        List<Skill> skills = sumAllSkill(player);
        for (Skill skill : skills) {
            Cfg_Skill_Bean config = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getSkillId());
            if (config == null) {
                log.error("找不到技能配置skill=" + skill.getSkillId() + player);
                continue;
            }
            if (config.getType() != SkillDefine.SkillType_UnActive) {
                continue;
            }
            for (ReadArray<Integer> porpert : config.getParams().getValuees()) {
                if (porpert.size() < 3) {
                    continue;
                }
                int func = porpert.get(0);
                int type = porpert.get(1);
                int value = porpert.get(2);

                if (func == 1) {
                    //添加属性
                    if (type < 1 || type > AttributeType.ATTR_MAX) {
                        continue;
                    }
                    att.addAttribute(type, value);

                } else if (func == 2) {
                    //加强技能
                    player.getSkilladds().put(type, player.getSkilladds().getOrDefault(type, 0) + value);
                }
            }
        }

        //计算格子等级属性
        int level =  player.getNewSkillData().getCellLevels();
        Cfg_Skill_position_levelup_Bean skill_position_levelup_bean = CfgManager.getCfg_Skill_position_levelup_Container().getValueByKey(level);
        if (skill_position_levelup_bean != null){
            int addpre = 0;
            boolean iscan = Manager.vipManager.power().canFree(player, VipPower.POWER_37);
            if (iscan){
                addpre = Manager.vipManager.power().getVipPowerValue(player, VipPower.POWER_37);
            }
            float pre =  (addpre/10000f)+1;
            if (skill_position_levelup_bean.getAtt()!=null && skill_position_levelup_bean.getAtt().size()>0){
                for (ReadArray<Integer> array : skill_position_levelup_bean.getAtt().getValuees()){
                    att.addAttribute(array.get(0), (int)(array.get(1) *pre));
                }
            }
        }else {
            log.error("Cfg_Skill_position_levelup_Bean ==null {}",level);
        }


        for (Integer skillBaseID : player.getNewSkillData().getSkillIds()){
            Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(skillBaseID);
            if (bean==null){
                continue;
            }
            if (bean.getAtt()!=null && bean.getAtt().size()>0){
                for (ReadArray<Integer> array : bean.getAtt().getValuees()){
                    att.addAttribute(array.get(0), array.get(1));
                }
            }
        }
        //计算经脉属性
        calculateMeridianAtt(player,att);

        return att;
    }
    //计算经脉属性
    private void calculateMeridianAtt(Player player, BaseIntAttribute att){
        List<Integer> meridianList =  player.getNewSkillData().getSkillMeridianList();
        for (Integer merdianID : meridianList){
            Cfg_Skill_meridian_new_Bean bean = CfgManager.getCfg_Skill_meridian_new_Container().getValueByKey(merdianID);
            if (bean == null){
                log.error("Cfg_Skill_meridian_Bean  is null {}", merdianID);
                continue;
            }
            if (bean.getAdd_att()!=null && bean.getAdd_att().size()>0){
                for (ReadArray<Integer> array : bean.getAdd_att().getValuees()){
                    att.addAttribute(array.get(0), array.get(1));
                }
            }
        }
    }

    @Override
    public BaseSystemIntAttribute getPlayerSystemAttribute(Player player) {
        BaseSystemIntAttribute att = player.PlayerCalSystemCulators().get(getType());
        if (att == null) {
            att = new BaseSystemIntAttribute(AttributeType.SystemAttr_Max);
            player.PlayerCalSystemCulators().put(getType(), att);
        }
        att.clean();

        List<Skill> skills = sumAllSkill(player);
        for (Skill skill : skills) {
            Cfg_Skill_Bean config = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getSkillId());
            if (config == null) {
                log.error("找不到技能配置skill=" + skill.getSkillId() + player);
                continue;
            }
            for (ReadArray<Integer> porpert : config.getParams_pre_att().getValuees()) {
                if (porpert.size() < 2) {
                    continue;
                }
                int type = porpert.get(0);
                int value = porpert.get(1);
                att.addSystemAttribute(type, value);
            }
        }
        return att;
    }

    /**
     * 获取玩家技能总表
     * @param player
     * @return
     */
    List<Skill>  sumAllSkill(Player player) {
        //TODO 玩家基础技能
        List<Skill> skills = new ArrayList<>(player.getSkills().values());
        //TODO 添加仙娃技能
        skills.addAll(Manager.marriageManager.manager().sumAllChildSkill(player));
        //TODO 添加神兽技能
        skills.addAll(Manager.soulBeastManager.deal().sumAllChildSkill(player));
        return skills;
    }

    @Override
    public int getId() {
        return ScriptEnum.SkillAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }
}
