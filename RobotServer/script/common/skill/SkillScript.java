package common.skill;

import com.data.CfgManager;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_Occ_Skill_Bean;
import com.data.bean.Cfg_Skill_Bean;
import com.data.bean.Cfg_Skill_star_levelup_Bean;
import com.data.struct.ReadArray;
import com.game.player.structs.Player;
import com.game.script.ScriptEnum;
import com.game.skill.script.ISkillScript;
import com.game.skill.structs.Skill;
import game.message.SkillMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;

public class SkillScript implements ISkillScript {

    private static final Logger log = LogManager.getLogger(SkillScript.class);

    @Override
    public int getId() {
        return ScriptEnum.SkillScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void initSkillList(Player player, SkillMessage.ResSkillOnline messInfo) {

        for (Integer baseSkillID : messInfo.getSkillIdsList()){
            Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(baseSkillID);
            if (bean == null) {
                log.error("Cfg_Skill_star_levelup_Bean is null {}", baseSkillID);
                continue;
            }
            for (Integer skillID : bean.getSkill_id().getValue()) {
                addSkill(player, skillID,1);
            }
        }
    }


    private void addMentalSkill(Player player, int mentalId, List<Integer> occSkillIds){
        for (int occSkillId : occSkillIds) {
            Cfg_Occ_Skill_Bean skillBean = CfgManager.getCfg_Occ_Skill_Container().getValueByKey(occSkillId);
            if (skillBean == null) {
                log.info("Cfg_Occ_Skill_Bean  为空 "  + 1);
                return;
            }
            for (Integer skillId:skillBean.getSkill_id().getValue()) {
                addSkill(player, skillId, 1);
            }
        }
    }

    @Override
    public void addSkill(Player player, int id, int level) {
        Skill skill = new Skill();
        skill.setId(id);
        skill.setLevel(level);

        Cfg_Skill_Bean bean = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getId());
        if (bean == null) {
            return;
        }
        for (Skill skill1 : player.getSkills()){
            if (skill1.getId() == skill.getId()){
                log.error("技能重复学习 {}",skill.getId());
                return;
            }
        }
        player.getSkills().add(skill);
    }

    private void removeSkill(Player player,int id){
        Iterator<Skill> its = player.getSkills().iterator();
        while (its.hasNext()){
            Skill skill = its.next();
            if(skill.getId() == id){
                its.remove();
                return;
            }
        }
    }



    @Override
    public void updateSkill(Player player, SkillMessage.ResUpdateSkill messInfo) {
        Iterator<Skill> its = player.getSkills().iterator();
        if(messInfo.getType() == 0) {//新增
            while (its.hasNext()){
                Skill skill = its.next();
                if(skill.getId() == messInfo.getSkillID()){
                    log.error("技能重复学习 {}",skill.getId());
                    return;
                }
            }
            addSkill(player, messInfo.getSkillID(), 1);
        }else if(messInfo.getType() == 1){//移除
            while (its.hasNext()){
                Skill skill = its.next();
                if(skill.getId() == messInfo.getSkillID()){
                    its.remove();
                    return;
                }
            }
        }
    }

    public void onResUpSkillStar(Player player, SkillMessage.ResUpSkillStar messInfo){

        int baseSkillID = messInfo.getNewSkillID();
        Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(baseSkillID);
        if (bean == null){
            log.error("Cfg_Skill_star_levelup_Bean is null {}" ,baseSkillID);
            return;
        }
        for (Integer skillID: bean.getSkill_id().getValue()){
            addSkill(player,skillID,1);
        }
        int removeSkillID = messInfo.getOldSkillID();
        if (removeSkillID <=0){
            return;
        }
        Cfg_Skill_star_levelup_Bean oldbean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(removeSkillID);
        if (bean == null){
            log.error("Cfg_Skill_star_levelup_Bean is null {}" ,removeSkillID);
            return;
        }
        for (Integer skillID: oldbean.getSkill_id().getValue()){
            removeSkill(player,skillID);
        }
    }

    @Override
    public void sendReqOneKeyUpSkill(Player player) {
        log.info(player.getInfo()+"请求升级全部技能");
        SkillMessage.ReqUpCell.Builder msg = SkillMessage.ReqUpCell.newBuilder();
//        msg.setCellId(0);
        player.sendMsg(SkillMessage.ReqUpCell.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
