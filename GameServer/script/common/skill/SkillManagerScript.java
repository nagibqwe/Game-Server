package common.skill;

import com.data.*;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.manager.BIDefine;
import com.game.chat.structs.Notify;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.Player;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.server.structs.SynToFightType;
import com.game.skill.log.SkillLog;
import com.game.skill.script.ISkillScript;
import com.game.skill.structs.Skill;
import com.game.skill.structs.SkillDefine;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.script.IScript;
import game.core.util.BeanUtil;
import game.core.util.IDConfigUtil;
import game.message.SkillMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;


/**
 * @author lw
 */
public class SkillManagerScript implements IScript, ISkillScript {

    private static final Logger logger = LogManager.getLogger(SkillManagerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.SkillBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    /**
     * 学习添加技能
     *
     * @param player
     * @param skillId
     */
    @Override
    public void addSkill(Player player, int skillId) {
        Cfg_Skill_Bean config = CfgManager.getCfg_Skill_Container().getValueByKey(skillId);
        if (config == null) {
            logger.error("技能无配置 skillId=" + skillId + player + BeanUtil.getStack());
            return;
        }

        Skill skill = player.getSkills().get(skillId);
        if (skill != null) {
            logger.info("重复学习技能skillId=" + skillId + player);
            return;
        }

        skill = new Skill();
        skill.setSkillId(skillId);
        skill.setLevel(1);
        player.getSkills().put(skill.getSkillId(), skill);

        if (config.getIf_get() == 1) {
            Manager.buffManager.deal().onAddBuff(player, player, config.getIf_get_params());
        }
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Skill);

        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);

        writeSkillLog(player, skill, 0, "", IDConfigUtil.getLogId());
        //成长BI
        Manager.biManager.getScript().biGrow(player, GrowType.skill_active.getType(), 0, BIDefine.GrowActive, 0, skillId, skillId);

        if (config.getType() != SkillDefine.SkillType_UnActive) {
            return;
        }
        SkillMessage.ResUpdateSkill.Builder builder = SkillMessage.ResUpdateSkill.newBuilder();
        builder.setSkillID(skillId);
        builder.setType(0);
        MessageUtils.send_to_player(player, SkillMessage.ResUpdateSkill.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        if ( player.playerCrossData.isToFightServer()) {
            Map<Integer, Object> map = new HashMap<>();
            map.put(1, 0);
            map.put(2, skillId);
            Manager.playerManager.managerExt().noticeSynRoleInfoToFight(player, SynToFightType.SkillChange, map, SkillMessage.ResUpdateSkill.MsgID.eMsgID_VALUE, builder.build().toByteString());
        }
    }

    /**
     * 写技能升级log
     */
    private void writeSkillLog(Player player, Skill skill, int action, String consume, long actionId) {
        try {
            SkillLog log = new SkillLog();
            log.setPlayer(player);
            log.setSkillId(skill.getSkillId());
            log.setLevel(skill.getLevel());
            log.setAction(action);
            log.setConsume(consume);
            log.setActionId(actionId);

            LogService.getInstance().execute(log);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    @Override
    public void removeSkill(Player player, int skillId) {

        Skill skill = player.getSkills().get(skillId);
        if (skill == null) {
            return;
        }
        player.getSkills().remove(skillId);

        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);

        writeSkillLog(player, skill, -1, "", IDConfigUtil.getLogId());

        Cfg_Skill_Bean config = CfgManager.getCfg_Skill_Container().getValueByKey(skillId);
        if (config == null) {
            logger.error("技能无配置 skillId=" + skillId + player);
            return;
        }

        if (config.getType() != SkillDefine.SkillType_UnActive) {
            return;
        }
        SkillMessage.ResUpdateSkill.Builder builder = SkillMessage.ResUpdateSkill.newBuilder();
        builder.setSkillID(skillId);
        builder.setType(1);
        MessageUtils.send_to_player(player, SkillMessage.ResUpdateSkill.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        if ( player.playerCrossData.isToFightServer()) {
            Map<Integer, Object> map = new HashMap<>();
            map.put(1, 1);
            map.put(2, skillId);
            Manager.playerManager.managerExt().noticeSynRoleInfoToFight(player, SynToFightType.SkillChange, map, SkillMessage.ResUpdateSkill.MsgID.eMsgID_VALUE, builder.build().toByteString());
        }
    }

    @Override
    public int getSkillAllLevel(Player player) {
      // int num = 0;
      // for (MentalSkill mentalSkill:player.getMentalskills().values()){
      //     Cfg_Occ_Mental_Bean occMental_bean = CfgManager.getCfg_Occ_Mental_Container().getValueByKey(mentalSkill.getMentalID());
      //     if (occMental_bean == null){
      //         logger.info("occMental_bean   == null"  + mentalSkill.getMentalID());
      //         continue;
      //     }
      //     num += occMental_bean.getLevel();
      // }
      // return num;
        //TODO 天赋废除
        return 0;
    }

    /**
     *上线初始化
     * @param player
     */
    public void online(Player player)
    {
        //initCell(player); //初始化技能格子
        initBaseSkill(player);//初始化普攻

        //TODO 老角色上线清理之前的老心法经脉
        repairOldMental(player);

        SkillMessage.ResSkillOnline.Builder msg = SkillMessage.ResSkillOnline.newBuilder();
        msg.setCellLevel( player.getNewSkillData().getCellLevels());
        //技能修复
        repairSkill(player);
        for (Integer baseSkillID : player.getNewSkillData().getSkillIds()){
            msg.addSkillIds(baseSkillID);
        }
        msg.setPlayedSkillStr(player.getNewSkillData().getPlayedSkillStr());
        msg.addAllSkillMeridianList(player.getNewSkillData().getSkillMeridianList());
        msg.setResetMentalTimes(player.getNewSkillData().getResetTimes());
        msg.setSelectMentalType( player.getNewSkillData().getMentalType());
        MessageUtils.send_to_player(player, SkillMessage.ResSkillOnline.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        SkillMessage.ResPassiveSkill.Builder builder = SkillMessage.ResPassiveSkill.newBuilder();
        for (Skill skill : player.getSkills().values()) {
            Cfg_Skill_Bean bean = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getSkillId());
            if (bean == null) {
                continue;
            }
            if (bean.getType() != SkillDefine.SkillType_UnActive) {
                continue;
            }
            builder.addSkillID(skill.getSkillId());
            if (bean.getIf_get() == 1) {
                Manager.buffManager.deal().onAddBuff(player, player, bean.getIf_get_params());
            }
        }
        MessageUtils.send_to_player(player, SkillMessage.ResPassiveSkill.MsgID.eMsgID_VALUE, builder.build().toByteArray());


        //修复基础技能
        repairBaseSkill(player);
    }

    /**
     * 初始化格子
     * @param player
     */
   //private void initCell(Player player){
   //    HashMap<Integer, Integer> cellLevels =   player.getNewSkillData().getCellLevels();
   //    if (cellLevels.size()<=0){
   //        //格子最大6,策划不配置喊写死
   //        for (int i = 1; i < 6;i++){
   //            if (i == 1){
   //                cellLevels.put(i,1);
   //            }else {
   //                cellLevels.put(i,0);
   //            }
   //        }
   //    }
   //}

    /**
     * 初始化基础技能
     * @param player
     * @param
     */
    private void initBaseSkill(Player player){
        //初始化普攻击
        if (player.getLevel() > 1){
            return;
        }
        if (player.getNewSkillData().getSkillIds().size() >0){
            return;
        }
        Cfg_Characters_Bean config = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());
        ReadIntegerArrayEs skillIDArrayEs = config.getOccSkill();
        if (skillIDArrayEs !=null && skillIDArrayEs.size()>0) {
            for (ReadArray<Integer> skillArr : skillIDArrayEs.getValuees()) {
                if (skillArr.get(0) != player.getCareer()) {
                    continue;
                }
                int baseSkillID = skillArr.get(1);
                Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(baseSkillID);
                if (bean == null) {
                    logger.error("Cfg_Skill_star_levelup_Bean is null {}", baseSkillID);
                    continue;
                }
                for (Integer skillID : bean.getSkill_id().getValue()) {
                    addSkill(player, skillID);
                }
                player.getNewSkillData().getSkillIds().add(baseSkillID);
            }
        }
    }

    public void addActivateNewSkill(Player player,ReadIntegerArrayEs skillIDArrayEs){
        if (skillIDArrayEs !=null && skillIDArrayEs.size()>0){
            for (ReadArray<Integer>skillArr: skillIDArrayEs.getValuees()) {
                if (skillArr.get(0) != player.getCareer()){
                    continue;
                }
                int baseSkillID = skillArr.get(1);
                Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(baseSkillID);
                if (bean == null){
                    logger.error("Cfg_Skill_star_levelup_Bean is null {}" ,baseSkillID);
                    continue;
                }
                player.getNewSkillData().getSkillIds().add(baseSkillID);
                for (Integer skillID: bean.getSkill_id().getValue()){
                    addSkill(player,skillID);
                }

                sendResUpSkillStar(player,0,baseSkillID);
            }
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Skill);
        }
    }

    private void sendResUpSkillStar(Player player , int oldSkill,int newSkill){
        SkillMessage.ResUpSkillStar.Builder msg = SkillMessage.ResUpSkillStar.newBuilder();
        msg.setOldSkillID(oldSkill);
        msg.setNewSkillID(newSkill);
        MessageUtils.send_to_player(player, SkillMessage.ResUpSkillStar.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void sendResUpCell(Player player,int level){
        SkillMessage.ResUpCell.Builder msg1 =   SkillMessage.ResUpCell.newBuilder();
        msg1.setLevel(level);
        MessageUtils.send_to_player(player, SkillMessage.ResUpCell.MsgID.eMsgID_VALUE, msg1.build().toByteArray());
    }


    /**
     * 技能升星
     * @param player
     * @param skillID
     */
    public void onReqUpSkillStar(Player player,int skillID){
        Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(skillID);
        if (bean == null){
            logger.error("Cfg_Skill_star_levelup_Bean is null {}" ,skillID);
            return;
        }
       List<Integer> skillList =   player.getNewSkillData().getSkillIds();
        if (!skillList.contains(skillID)){
            logger.error("skillID  没激活 {}" ,skillID);
            return;
        }
        if(bean.getNeed_item() == null || bean.getNeed_item().size()<=0){
            logger.error("升星已达到最大满级 {}" ,skillID);
            return;
        }

        int newSkillID = skillID + 1;
        Cfg_Skill_star_levelup_Bean newbean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(newSkillID);
        if (newbean == null){
            logger.error("Cfg_Skill_star_levelup_Bean is null {}" ,newSkillID);
            return;
        }
        if ( !Manager.backpackManager.manager().onRemoveItem(player, bean.getNeed_item().get(0),
                bean.getNeed_item().get(1), ItemChangeReason.NewSkillUpstarDec, IDConfigUtil.getLogId())){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ImmSoulcompound_NoMaterial);
            return;
        }
        skillList.remove((Object)skillID);
        skillList.add(newSkillID);
        for (Integer skillid : bean.getSkill_id().getValue()){
            removeSkill(player,skillid);
        }
        for (Integer skillid : newbean.getSkill_id().getValue()){
            addSkill(player,skillid);
        }
        sendResUpSkillStar(player,skillID,newSkillID);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Skill);
        Manager.controlManager.operate(player, FunctionVariable.Skill_Star_Count, 0);

        RoleGrowLog.create(player, GrowType.skill_star, 0, newSkillID,  bean.getStar(), newbean.getStar(), null);
    }

    private void addBaseSkill(Player player,int baseSkill) {
        Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(baseSkill);
        if (bean == null){
            logger.error("Cfg_Skill_star_levelup_Bean is null {}" ,baseSkill);
            return;
        }
        List<Integer> skillList =   player.getNewSkillData().getSkillIds();
        if (skillList.contains(baseSkill)){
            logger.error("重复学习  Cfg_Skill_star_levelup {}" ,baseSkill);
            return;
        }
        skillList.add(baseSkill);
        for (Integer skillid : bean.getSkill_id().getValue()){
            addSkill(player,skillid);
        }
    }

    private int  removeBaseSkill(Player player,int baseSkill) {
        Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(baseSkill);
        if (bean == null){
            logger.error("Cfg_Skill_star_levelup_Bean is null {}" ,baseSkill);
            return 0;
        }
        List<Integer> skillList =   player.getNewSkillData().getSkillIds();
        int needRemoveID = findNeedRemoveId(skillList,bean.getOder());
        if (needRemoveID <=0){
            logger.error("未找到对应的baseSkill {} oderID {}" ,baseSkill,bean.getOder());
            return 0 ;
        }
        if (!skillList.contains(needRemoveID)){
            logger.error("baseSkill  未激活 {}" ,baseSkill);
            return 0;
        }
        skillList.remove((Object)needRemoveID);
        for (Integer skillid : bean.getSkill_id().getValue()){
            removeSkill(player,skillid);
        }
        return needRemoveID;
    }

    /**
     * 格子升级
     * @param player
     * @param cellId
     */
    public void onResUpCell(Player player){

        if ( upCell(player)){
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Skill);
            Manager.controlManager.operate(player, FunctionVariable.Akill_Position_Level, 0);
        }

    }

   //private void oneKeyUpCell(Player player,List<Integer> containList, HashMap<Integer,Integer> cellLevelMap,
   //                          HashMap<Integer,Integer> costMoney ){
   //    HashMap<Integer, Integer> cellLevels =  player.getNewSkillData().getCellLevels();
   //    List<Integer> keys = new ArrayList<>();
   //    for (Map.Entry<Integer,Integer> entry : cellLevels.entrySet()){
   //        if (entry.getValue()<1){
   //            continue;
   //        }
   //        if (entry.getValue() >= player.getLevel()){
   //            continue;
   //        }
   //        if (containList.contains(entry.getKey())) {
   //            continue;
   //        }
   //        keys.add(entry.getKey());
   //    }
   //    if (keys.size() <=0){
   //        return;
   //    }
   //    int minLv = 9999;
   //    int minKey = 0;
   //    for (Integer key : keys){
   //        if ( cellLevels.get(key) < minLv){
   //            minLv = cellLevels.get(key);
   //            minKey = key;
   //        }
   //    }
   //    if (minKey <= 0){
   //        return;
   //    }
   //    if (!upCell(player,minKey,cellLevelMap,costMoney)){
   //        containList.add(minKey);
   //    }
   //    oneKeyUpCell(player,containList,cellLevelMap,costMoney);
   //}

    private boolean upCell(Player player){
        int  cellLevels =  player.getNewSkillData().getCellLevels();
        int playerLevel = player.getLevel();
        if (cellLevels  + 1 > playerLevel ){
            logger.error("升级不能超过自身等级  {}",playerLevel);
            return false;
        }
        Cfg_Skill_position_levelup_Bean bean = CfgManager.getCfg_Skill_position_levelup_Container().getValueByKey(cellLevels);
        if (bean == null){
            logger.error("Cfg_Skill_position_levelup_Bean is null  {}",cellLevels);
            return false;
        }
        if (!Manager.currencyManager.manager().onDecItemCoin(player, bean.getMoney(), ItemChangeReason.NewSkillUpCellDec, IDConfigUtil.getLogId(), ItemCoinType.BindMoney)){
            logger.error("货币不足");
            return false;
        }
        cellLevels +=1;
        player.getNewSkillData().setCellLevels(cellLevels);
        sendResUpCell(player,cellLevels);
        RoleGrowLog.create(player, GrowType.skill_up, 0, 0, cellLevels-1, cellLevels, null);
        return true;
    }


    /**
     *激活经脉
     * @param player
     */
    @Override
    public void onReqActivateMeridian(Player player, int meridianID) {
        Cfg_Skill_meridian_new_Bean bean = CfgManager.getCfg_Skill_meridian_new_Container().getValueByKey(meridianID);
        if (bean == null){
            logger.error("getCfg_Skill_meridian_Container is null  {}",meridianID);
            return;
        }
        List<Integer> meridianList =  player.getNewSkillData().getSkillMeridianList();
        if ( meridianList.contains(meridianID)){
            logger.error(" 已激活  {}",meridianID);
            return;
        }

        if (bean.getNeed_parent_id() > 0 && !meridianList.contains(bean.getNeed_parent_id())){
            boolean isCanUpMeridian = false;
            for (Integer parentId : meridianList){
                Cfg_Skill_meridian_new_Bean meridian_new_bean = CfgManager.getCfg_Skill_meridian_new_Container().getValueByKey(parentId);
                if (meridian_new_bean.getMeridian_id() != bean.getMeridian_id()){
                    continue;
                }
                if (meridian_new_bean.getId() > bean.getNeed_parent_id()){
                    isCanUpMeridian = true;
                    break;
                }
            }
            if (!isCanUpMeridian){
                logger.error(" 所需要父经脉ID 未激活  {}",bean.getNeed_parent_id());
                return;
            }
        }

        Cfg_Skill_meridian_pos_Bean pos_bean = CfgManager.getCfg_Skill_meridian_pos_Container().getValueByKey(bean.getType());
        if (pos_bean == null){
            logger.error("Cfg_Skill_meridian_pos_Bean is null  {}",bean.getType());
            return;
        }

        if (pos_bean.getXinfa_id() != player.getNewSkillData().getMentalType()){
            logger.error(" 选在的心法不匹配:{} {} " ,pos_bean.getXinfa_id(),player.getNewSkillData().getMentalType());
            return;
        }

     // int alllevel = 0;
     // int needTypeid = 0;
     // boolean isInOcc = false;
     // ReadArray<Integer> array = Global.Meridian_OCC_ID.get(player.getCareer());
     // for (int i = 1;i<array.size();i++){
     //     if (array.get(i) == player.getNewSkillData().getMentalType()){
     //         needTypeid =  array.get(i);
     //     }
     //     if (array.get(i) == bean.getType()){
     //         isInOcc = true;
     //     }
     // }
     // if (player.getNewSkillData().getMentalType() <= 0 && isInOcc ){
     //     logger.error(" 没有选择心法不能激活心法经脉");
     //     return;
     // }
       // int needOhterLevel = 0;
       // for (Integer id :meridianList){
       //     Cfg_Skill_meridian_Bean meridian_bean = CfgManager.getCfg_Skill_meridian_Container().getValueByKey(id);
       //     if (meridian_bean.getType()  == bean.getNeed_type_id()){
       //         alllevel +=meridian_bean.getLevel();
       //     }
       //     if (meridian_bean.getType() == needTypeid){
       //         needOhterLevel +=meridian_bean.getLevel();
       //     }
       // }
        //if (alllevel < bean.getNeed_type_level()){
        //    logger.error("所需 等级不足 {} ",bean.getNeed_type_level());
        //    return;
        //}

      //  if (isInOcc && needTypeid >0 && bean.getType() != player.getNewSkillData().getMentalType()
      //          && player.getNewSkillData().getMentalType() > 0){
      //      if (needOhterLevel <  Global.Need_meridian_level){
      //          logger.error("所需其他心法等级不足 {} ",needOhterLevel);
      //          return;
      //      }
      //  }

        if (! Manager.currencyManager.manager().onDecItemCoin(player, bean.getNeed_value().get(1),
                ItemChangeReason.MeridianActiveDec, IDConfigUtil.getLogId(), bean.getNeed_value().get(0))){
            logger.error("货币不足");
            return ;
        }

        //删除上一个经脉所学习的技能
        int preMeridianID = meridianID - 1;
        int oldBasSkill = 0;
        if (meridianList.contains(preMeridianID)){
            meridianList.remove((Object)preMeridianID);
            Cfg_Skill_meridian_new_Bean pre_bean = CfgManager.getCfg_Skill_meridian_new_Container().getValueByKey(preMeridianID);
            if(pre_bean == null){
                preMeridianID = 0;
            }
            //删除被动技能
            if (pre_bean != null && pre_bean.getAdd_passive_skill() > 0){
                removeSkill(player,pre_bean.getAdd_passive_skill());
            }
            //删除主动技能
            if (pre_bean !=null && pre_bean.getAdd_skill() > 0){
                removeBaseSkill(player,pre_bean.getAdd_skill());
                oldBasSkill = pre_bean.getAdd_skill();
            }
        }
        //添加技能
        meridianList.add(meridianID);
        if (bean.getAdd_passive_skill() > 0){
            addSkill(player,bean.getAdd_passive_skill());
        }
        if (bean.getAdd_skill() > 0){
            addBaseSkill(player,bean.getAdd_skill());
        }


        //
        if (bean.getAdd_skill()>0 || oldBasSkill > 0){
            sendResUpSkillStar(player,oldBasSkill,bean.getAdd_skill());
        }
        SkillMessage.ResActivateMeridian.Builder msg = SkillMessage.ResActivateMeridian.newBuilder();
        msg.setMeridianID(meridianID);
        MessageUtils.send_to_player(player, SkillMessage.ResActivateMeridian.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Skill);

        RoleGrowLog.create(player, GrowType.skill_channel_up, 0, meridianID, preMeridianID, meridianID, null);
    }

    /**
     * 保存出站技能
     * @param player
     * @param playedSkillStr
     */
    @Override
    public void onReqSaveFightSkill(Player player, String playedSkillStr) {
        player.getNewSkillData().setPlayedSkillStr(playedSkillStr);
    }

    /**
     * 选择心法
     * @param player
     * @param type
     */
    @Override
    public void onSelectMental(Player player, int type) {
        if (player.getNewSkillData().getMentalType() > 0){
            logger.error("选择心法需要先重置");
            return;
        }
        if (type > 2   || type <=0){
            logger.error("选择心法超出范围");
            return;
        }
        //int selectType = 0;
        //for (ReadArray<Integer> array : Global.Meridian_OCC_ID.getValuees()){
        //    if (array.get(0) != player.getCareer()) {
        //        continue;
        //    }
        //    for (int i = 1;i < array.size();i++){
        //        if (  array.get(i) != type){
        //            continue;
        //        }
        //        selectType = type;
        //    }
        //}
        //if (selectType <=0){
        //    logger.error("选择的心法类型不正确 : {}" , type);
        //    return;
        //}
        player.getNewSkillData().setMentalType(type);
        SkillMessage.ResSelectMentalType.Builder msg1 = SkillMessage.ResSelectMentalType.newBuilder();
        msg1.setMentalType(type);
        msg1.setResetMentalTimes(player.getNewSkillData().getResetTimes());
        MessageUtils.send_to_player(player, SkillMessage.ResSelectMentalType.MsgID.eMsgID_VALUE, msg1.build().toByteArray());

        RoleGrowLog.create(player, GrowType.skill_mental_active, 0, type, 0, type, null);
    }


    /**
     * 修复老玩家经脉心法
     * @param player
     */

    private void repairOldMental(Player player){
        if (player.getNewSkillData().getMentalType()  <=2){
            return;
        }
        if (player.getNewSkillData().getRepairTimes() >0){
            return;
        }

        long logid =  IDConfigUtil.getLogId();
        for (int i = 0; i < player.getLevel(); i++) {
            Cfg_Characters_Bean config = CfgManager.getCfg_Characters_Container().getValueByKey(i + 1);
            if (config.getOccSkill() !=null){
                //TODO 升级有可能获得经脉点
                if (config.getLevel_up_money() != null &&  config.getLevel_up_money().size()>0){
                    Manager.currencyManager.manager().onAddItemCoin(player,config.getLevel_up_money().get(0),
                            config.getLevel_up_money().get(1), ItemChangeReason.LevelChangeGet, logid);
                }
            }
        }
        List<Integer> meridianList =  player.getNewSkillData().getSkillMeridianList();
        for (Integer merID :  meridianList){
            resetMeridian(player , merID);
        }
        meridianList.clear();
        player.getNewSkillData().setMentalType(0);
        player.getNewSkillData().setRepairTimes(1);
    }

    /**
     * 重置心法
     * @param player
     */
   public void  onReqResetSelectMental(Player player){
       if (player.getNewSkillData().getMentalType() <=0){
           logger.info("没有设置当前心法 不可重置");
           return;
       }

       //方星宇 喊写死 >=2
       if (player.getNewSkillData().getResetTimes() >=2){
            if ( !Manager.backpackManager.manager().onRemoveItem(player, Global.Reset_meridian_Item.get(0),
                    Global.Reset_meridian_Item.get(1), ItemChangeReason.MeridianRestDec, IDConfigUtil.getLogId())){
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.MyMoneyNotEnough);
                return;
            }
       }

       List<Integer> meridianList =  player.getNewSkillData().getSkillMeridianList();
       for (Integer merID :  meridianList){
           //resetMeridian(player , merID);
           resetNewMeridian(player,merID);
       }

       meridianList.clear();

       player.getNewSkillData().setResetTimes( player.getNewSkillData().getResetTimes()+1);
       player.getNewSkillData().setMentalType(0);
       SkillMessage.ResSelectMentalType.Builder msg1 = SkillMessage.ResSelectMentalType.newBuilder();
       msg1.setMentalType(0);
       msg1.setResetMentalTimes(player.getNewSkillData().getResetTimes());
       MessageUtils.send_to_player(player, SkillMessage.ResSelectMentalType.MsgID.eMsgID_VALUE, msg1.build().toByteArray());

       RoleGrowLog.create(player,GrowType.skill_mental_reset,0, 0, 0,0,null);
   }

    /**
     * 重置经脉
     * @param player
     */
    @Override
    public void onReqResetMeridianSkill(Player player) {
        List<Integer> meridianList =  player.getNewSkillData().getSkillMeridianList();
        if (! Manager.currencyManager.manager().onDecItemCoin(player, Global.Meridian_Rest_Cost.get(1),
                ItemChangeReason.MeridianRestDec, IDConfigUtil.getLogId(),  Global.Meridian_Rest_Cost.get(0))){
            logger.error("货币不足");
            return ;
        }
        for (Integer merID :  meridianList){
            //resetMeridian(player , merID);
            resetNewMeridian(player ,merID);
        }

        meridianList.clear();
        //SkillMessage.ResResetMeridianSkillResult.Builder msg =  SkillMessage.ResResetMeridianSkillResult.newBuilder();
        //MessageUtils.send_to_player(player, SkillMessage.ResResetMeridianSkillResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());


        player.getNewSkillData().setResetTimes( player.getNewSkillData().getResetTimes()+1);
        player.getNewSkillData().setMentalType(0);
        SkillMessage.ResSelectMentalType.Builder msg1 = SkillMessage.ResSelectMentalType.newBuilder();
        msg1.setMentalType(0);
        msg1.setResetMentalTimes(player.getNewSkillData().getResetTimes());
        MessageUtils.send_to_player(player, SkillMessage.ResSelectMentalType.MsgID.eMsgID_VALUE, msg1.build().toByteArray());

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Skill);
        RoleGrowLog.create(player, GrowType.skill_channel_reset, null, 0, 0);
    }
    private void resetMeridian(Player player, int merID){
        Cfg_Skill_meridian_Bean meridian_bean = CfgManager.getCfg_Skill_meridian_Container().getValueByKey(merID);
        if (meridian_bean == null){
            return;
        }
        //删除被动技能
        if ( meridian_bean.getAdd_passive_skill() > 0){
            removeSkill(player,meridian_bean.getAdd_passive_skill());
        }
        //删除主动技能
        long actionId = IDConfigUtil.getLogId();
        if ( meridian_bean.getAdd_skill() > 0){
            int needRemoveID = removeBaseSkill(player,meridian_bean.getAdd_skill());
            Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(needRemoveID);
            if (bean != null ){
                if(bean.getReturn_item() != null && bean.getReturn_item().size() > 0){
                    Item item = Item.createItem(bean.getReturn_item().get(0), bean.getReturn_item().get(1), true);
                    Manager.backpackManager.manager().addItem(player,item,ItemChangeReason.MeridianRestGet,actionId);
                }
            }
        }
        Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.MeridianCoin, meridian_bean.getNeed_value(), ItemChangeReason.MeridianRestGet, actionId);
        resetMeridian(player,merID - 1);
    }

    private void resetNewMeridian(Player player, int merID){

        Cfg_Skill_meridian_new_Bean meridian_bean = CfgManager.getCfg_Skill_meridian_new_Container().getValueByKey(merID);
        if (meridian_bean == null){
            return;
        }
        //删除被动技能
        if ( meridian_bean.getAdd_passive_skill() > 0){
            removeSkill(player,meridian_bean.getAdd_passive_skill());
        }
        //删除主动技能
        long actionId = IDConfigUtil.getLogId();
        if ( meridian_bean.getAdd_skill() > 0){
            int needRemoveID = removeBaseSkill(player,meridian_bean.getAdd_skill());
            Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(needRemoveID);
            if (bean != null ){
                if(bean.getReturn_item() != null && bean.getReturn_item().size() > 0){
                    Item item = Item.createItem(bean.getReturn_item().get(0), bean.getReturn_item().get(1), true);
                    Manager.backpackManager.manager().addItem(player,item,ItemChangeReason.MeridianRestGet,actionId);
                }
            }
        }
        Manager.currencyManager.manager().onAddItemCoin(player,  meridian_bean.getNeed_value().get(0), meridian_bean.getNeed_value().get(1), ItemChangeReason.MeridianRestGet, actionId);
        resetNewMeridian(player,merID - 1);
    }

    /**
     * 获得技能总星级
     * @param player
     * @return
     */
    public int getAllStar(Player player){
        int star = 0;
        List<Integer> skillList =   player.getNewSkillData().getSkillIds();
        for (Integer skillbase :skillList){
            Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(skillbase);
            if (bean == null){
                continue;
            }
            star +=bean.getStar();
        }
        return star;
    }

    /**
     * 获取所有等级
     * @param player
     * @return
     */
    @Override
    public int getAllLevel(Player player) {
        int alllevel =  player.getNewSkillData().getCellLevels();
        return alllevel;
    }

    private int getCellID(int cellPos , int level){
        return 10000 * cellPos + level;
    }




    private int findNeedRemoveId( List<Integer> skillList,int needOder){
        int needRemove = 0;
        for (int id : skillList){
            int oderid =  getOder(id);
            if (oderid != needOder){
                continue;
            }
            needRemove = id;
            break;
        }
        return needRemove;
    }

    private int getOder(int id){
        return id%1000000/1000;
    }

    /**
     * 技能修复 由于策划当时考虑的ID 不够周到 太短了，导致后面无法扩展。。
     * @param player
     */
    private void repairSkill(Player player){

        List<Integer> skillList =  player.getNewSkillData().getSkillIds();
        HashMap<Integer,Integer> skillNeedChangeMap = new HashMap<>();
        for (Integer oldSkillID  : skillList){
                Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(oldSkillID);
                if (bean!=null){
                    continue;
                }
               int needAdd =   oldSkillID - ((player.getCareer()+1)*10000);
               int baseID  = (oldSkillID / 10000) * 1000000;
               int newSkillId = baseID + needAdd;
                skillNeedChangeMap.put(oldSkillID, newSkillId);
        }
        if (skillNeedChangeMap.size() <=0){
            return;
        }
        Iterator<Map.Entry<Integer, Integer>> iter  = skillNeedChangeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Integer> en = iter.next();
            skillList.remove(en.getKey());
            skillList.add(en.getValue());
            iter.remove();
        }
    }


    /**
     * 1.3更新到1.4造成 部分玩家没有学习 基础技能，进行一个上线修复
     * @param player
     */
    private void repairBaseSkill(Player player){
        int skillLevelID = player.getCareer()  == 0 ? 1000000:2000000;
        Cfg_Skill_star_levelup_Bean bean = CfgManager.getCfg_Skill_star_levelup_Container().getValueByKey(skillLevelID);
        if (bean == null){
            logger.error("Cfg_Skill_star_levelup_Bean is null {} " , skillLevelID);
            return;
        }
        for (Integer skillID :  bean.getSkill_id().getValue()){
            if ( player.getSkills().containsKey(skillID)){
                continue;
            }
            addSkill(player,skillID);
        }
    }
}



