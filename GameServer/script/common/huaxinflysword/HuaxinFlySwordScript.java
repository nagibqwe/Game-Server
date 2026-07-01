package common.huaxinflysword;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_HuaxingFlySword_Advanced_Bean;
import com.data.bean.Cfg_HuaxingFlySword_Bean;
import com.data.bean.Cfg_HuaxingFlySword_levelup_Bean;
import com.data.bean.Cfg_HuaxingFlySword_skill_Bean;
import com.data.struct.ReadArray;
import com.game.bi.manager.BIDefine;
import com.game.chat.structs.Notify;
import com.game.huaxinflysword.script.IHuaxinFlySword;
import com.game.huaxinflysword.structs.FlyswordAllInfo;
import com.game.huaxinflysword.structs.FlyswordData;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.map.manager.MapGpsUtil;
import com.game.nature.structs.HuaxinEntity;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.server.structs.SynToFightType;
import com.game.skill.structs.Skill;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.HorseMessage;
import game.message.HuaxinFlySwordMessage;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 剑灵 系统
 * Created by 542 on 2020/5/21.
 */
public class HuaxinFlySwordScript implements IScript, IHuaxinFlySword {

    private static final Logger log = LogManager.getLogger(HuaxinFlySwordScript.class);

    @Override
    public int getId() {
        return ScriptEnum.HuaxinFlySwordScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    /**
     * 上线初始化
     *
     * @param player
     */
    public void onlineInit(Player player) {

        if (player.getFlyswordAllInfo().getFlyswordDataMap().size() <= 0)
            return;
        if (player.getFlyswordAllInfo().getCurUseFlySwordId() <= 0)
            return;
        if (player.getFlyswordAllInfo().getCurFlySwordSkillId() <= 0)
            return;

        Cfg_HuaxingFlySword_Bean bean = CfgManager.getCfg_HuaxingFlySword_Container().getValueByKey(player.getFlyswordAllInfo().getCurFlySwordSkillId());
        if (bean == null)
            return;

        HuaxinFlySwordMessage.ResOnlineInitHuaxin.Builder msg = HuaxinFlySwordMessage.ResOnlineInitHuaxin.newBuilder();
        HuaxinFlySwordMessage.huaxinInfo.Builder huaxinInfo = null;


        Iterator<Map.Entry<Integer, FlyswordData>> iter = player.getFlyswordAllInfo().getFlyswordDataMap().entrySet().iterator();
        while (iter.hasNext()) {
            huaxinInfo = HuaxinFlySwordMessage.huaxinInfo.newBuilder();
            Map.Entry<Integer, FlyswordData> en = iter.next();
            FlyswordData flyswordData = en.getValue();
            huaxinInfo.addAllModelID(flyswordData.getActivateList());
            huaxinInfo.setStarLevel(flyswordData.getLevel());
            huaxinInfo.setSteps(flyswordData.getSteps());
            huaxinInfo.setType(flyswordData.getType());
            msg.addHuaxinList(huaxinInfo.build());
        }


        //初始化当前使用技能
        HuaxinEntity huaxinEntity = null;
        if (player.getCurHuaxinEntity() == null) {
            huaxinEntity = new HuaxinEntity();
            player.setCurHuaxinEntity(huaxinEntity);
        } else {
            huaxinEntity = player.getCurHuaxinEntity();
        }
        huaxinEntity.setExcelId(player.getFlyswordAllInfo().getCurUseFlySwordId());
        huaxinEntity.setOwnerId(player.getId());
        Skill skill = new Skill();
        skill.setSkillId(bean.getUse_Skill());
        huaxinEntity.getBaseSkills().put(bean.getUse_Skill(), skill);
        if (bean.getNormal_Skill() > 0) {
            skill = new Skill();
            skill.setSkillId(bean.getNormal_Skill());
            huaxinEntity.getBaseSkills().put(bean.getNormal_Skill(), skill);
        }
        msg.setCurUseFlysword(player.getFlyswordAllInfo().getCurUseFlySwordId());
        MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResOnlineInitHuaxin.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //广播
        huaxinFlySwordBroadCast(player);

        //上线进行一次对老玩家已激活的剑灵 进行一次检测
        activateFlySwordSkill(player);

        onlieInitflySwordSkill(player);
    }


    public void onReqUseHuxin(Player player, int id, int type, boolean isClientActive) {

        if (type == 1) {
            //激活
            activeHuaxin(player, id, isClientActive, type);
        } else if (type == 2) {
            //升级
            starLevelUp(player, id, type);
        } else if (type == 3) {
            //切换
            changeHuaxin(player, id, type);
        } else if (type == 4) {
            //升阶
            upSteps(player, id, type);
        }
    }


    /**
     * 升级
     *
     * @param player
     * @param id
     */
    private void starLevelUp(Player player, int id, int type) {

        Cfg_HuaxingFlySword_Bean bean = CfgManager.getCfg_HuaxingFlySword_Container().getValueByKey(id);
        if (bean == null)
            return;

        ConcurrentHashMap<Integer, FlyswordData> flyswordMap = player.getFlyswordAllInfo().getFlyswordDataMap();
        if (!flyswordMap.containsKey(bean.getType())) {
            return;
        }
        FlyswordData flyswordData = flyswordMap.get(bean.getType());
        Cfg_HuaxingFlySword_levelup_Bean huaxingFlySword_levelup_bean = CfgManager.getCfg_HuaxingFlySword_levelup_Container().getValueByKey(flyswordData.getLevel());
        if (huaxingFlySword_levelup_bean == null) {
            return;
        }
        if (huaxingFlySword_levelup_bean.getUp_item() == null || huaxingFlySword_levelup_bean.getUp_item().size() <= 0) {
            log.info("已最大等级  " + flyswordData.getLevel());
            return;
        }

        long actionId = IDConfigUtil.getLogId();
        for (ReadArray<Integer> array : huaxingFlySword_levelup_bean.getUp_item().getValuees()) {
            if (!Manager.backpackManager.manager().canDeleteItemNum(player, array.get(0), array.get(1))) {
                log.error("物品不足");
                return;
            }
        }

        for (ReadArray<Integer> array : huaxingFlySword_levelup_bean.getUp_item().getValuees()) {
            if (!Manager.backpackManager.manager().onRemoveItem(player, array.get(0), array.get(1), ItemChangeReason.HuaxingDec, actionId)) {
                log.error("物品不足");
                return;
            }
        }

        int oldLv = flyswordData.getLevel();
        flyswordData.setLevel(flyswordData.getLevel() + 1);
        sendUseHuaxinResult(player, id, type);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HuaxinFlySword);
        Manager.controlManager.operate(player, FunctionVariable.FlyswardLevelup, 1);
        Manager.biManager.getScript().biGrow(player, GrowType.flySword_level_up.getType(), bean.getType(), BIDefine.GrowLevelUp, oldLv, flyswordData.getLevel(), id);
    }

    /**
     * 激活
     *
     * @param player
     * @param id
     * @param isClientActive
     */
    private void activeHuaxin(Player player, int id, boolean isClientActive, int type) {
        Cfg_HuaxingFlySword_Bean bean = CfgManager.getCfg_HuaxingFlySword_Container().getValueByKey(id);
        if (bean == null)
            return;

        boolean isActive = false;
        if (isClientActive) {//客户端激活
            if (bean.getActive_condition() == 2) {//条件激活
                int curLevel = Manager.controlManager.deal().getFuncProgress(player, bean.getVariable());
                if (curLevel < bean.getVariable().get(1)) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.NEW_OPEN_NO_ACHIVE);
                    return;
                }
                isActive = true;
            } else if (bean.getActive_condition() == 0) {//使用道具激活
                long actionId = IDConfigUtil.getLogId();
                if (!Manager.backpackManager.manager().onRemoveItem(player, bean.getActive_item(), 1, ItemChangeReason.HuaxingDec, actionId)) {
                    log.error("物品不足");
                    return;
                }
                isActive = true;
            }
        } else if (bean.getActive_condition() == 1) {//服务器激活
            isActive = true;
        }
        if (!isActive)
            return;

        ConcurrentHashMap<Integer, FlyswordData> flyworldMap = player.getFlyswordAllInfo().getFlyswordDataMap();
        FlyswordData flyswordData;
        if (!flyworldMap.containsKey(bean.getType())) {
            flyswordData = new FlyswordData();
            flyswordData.setType(bean.getType());
            flyworldMap.put(bean.getType(), flyswordData);
        } else {
            flyswordData = flyworldMap.get(bean.getType());
            if (flyswordData.getActivateList().contains(id)) {
                log.info("已激活  " + id);
                return;
            }
        }
        flyswordData.getActivateList().add(id);

        HuaxinEntity huaxinEntity = null;
        if (player.getCurHuaxinEntity() == null) {
            huaxinEntity = new HuaxinEntity();
            player.setCurHuaxinEntity(huaxinEntity);
        } else {
            huaxinEntity = player.getCurHuaxinEntity();
        }

        setFlySwordSkill(bean, huaxinEntity);
        huaxinEntity.setExcelId(id);
        huaxinEntity.setOwnerId(player.getId());
        // player.setCurUseFlySwordId(id);
        player.getFlyswordAllInfo().setCurUseFlySwordId(id);
        player.getFlyswordAllInfo().setCurFlySwordSkillId(id);
        sendUseHuaxinResult(player, id, type);

        if (player.playerCrossData.isToFightServer()) {
            sendHxDataToFight(player);
        } else {
            huaxinFlySwordBroadCast(player);
        }
        //地图设置
        huaxinEntity.clearHatred();
        huaxinEntity.setCurAttackTargetId(0);
        huaxinEntity.setCurSlowSkill(null);
        MapGpsUtil.CopyGPS(player.getCurGps(), huaxinEntity.getCurGps());

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HuaxinFlySword);
        Manager.controlManager.operate(player, FunctionVariable.FlyswardActivated, 1);
        Manager.biManager.getScript().biGrow(player, GrowType.flySword_active.getType(), bean.getType(), BIDefine.GrowActive, 0, huaxinEntity.getExcelId(), id);
    }


    /**
     * 升阶
     */
    private void upSteps(Player player, int id, int type) {

        Cfg_HuaxingFlySword_Bean bean = CfgManager.getCfg_HuaxingFlySword_Container().getValueByKey(id);
        if (bean == null)
            return;

        ConcurrentHashMap<Integer, FlyswordData> flyswordMap = player.getFlyswordAllInfo().getFlyswordDataMap();
        if (!flyswordMap.containsKey(bean.getType())) {
            return;
        }
        FlyswordData flyswordData = flyswordMap.get(bean.getType());
        Cfg_HuaxingFlySword_Advanced_Bean huaxingFlySword_advanced_bean = CfgManager.getCfg_HuaxingFlySword_Advanced_Container().getValueByKey(flyswordData.getSteps());
        if (huaxingFlySword_advanced_bean == null) {
            return;
        }
        if (huaxingFlySword_advanced_bean.getActive_item() == null || huaxingFlySword_advanced_bean.getActive_item().size() <= 0) {
            log.info("已最大阶  " + flyswordData.getSteps());
            return;
        }
        if (flyswordData.getLevel() < huaxingFlySword_advanced_bean.getLevelmax()) {
            log.info("等级不足够升阶   " + flyswordData.getLevel());
            return;
        }


        long actionId = IDConfigUtil.getLogId();
        for (ReadArray<Integer> array : huaxingFlySword_advanced_bean.getActive_item().getValuees()) {
            if (!Manager.backpackManager.manager().canDeleteItemNum(player, array.get(0), array.get(1))) {
                log.error("物品不足");
                return;
            }
        }

        for (ReadArray<Integer> array : huaxingFlySword_advanced_bean.getActive_item().getValuees()) {
            if (!Manager.backpackManager.manager().onRemoveItem(player, array.get(0), array.get(1), ItemChangeReason.HuaxingDec, actionId)) {
                log.error("物品不足");
                return;
            }
        }
        int oldStage = flyswordData.getSteps();
        flyswordData.setSteps(flyswordData.getSteps() + 1);
        sendUseHuaxinResult(player, id, type);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HuaxinFlySword);
        Manager.biManager.getScript().biGrow(player, GrowType.flySword_stageUp.getType(), bean.getType(), BIDefine.GrowStageUp, oldStage + 100000, flyswordData.getSteps() + 100000, id);


        activateFlySwordSkill(player);
    }

    /**
     * 切换化形
     *
     * @param player
     * @param id
     */
    private void changeHuaxin(Player player, int id, int type) {

        if (player.getFlyswordAllInfo().getCurUseFlySwordId() == id)
            return;

        Cfg_HuaxingFlySword_Bean bean = CfgManager.getCfg_HuaxingFlySword_Container().getValueByKey(id);
        if (bean == null)
            return;

        if (!player.getFlyswordAllInfo().getFlyswordDataMap().containsKey(bean.getType())) {
            log.info("未激活 type " + bean.getType());
            return;
        }
        FlyswordData flyswordData = player.getFlyswordAllInfo().getFlyswordDataMap().get(bean.getType());
        if (!flyswordData.getActivateList().contains(id)) {
            log.info("未激活 id " + id);
            return;
        }

        int maxId = 0;
        for (Integer flyID : flyswordData.getActivateList()) {
            if (flyID > maxId)
                maxId = flyID;
        }

        Cfg_HuaxingFlySword_Bean skillbean = CfgManager.getCfg_HuaxingFlySword_Container().getValueByKey(maxId);
        if (skillbean == null) {
            log.info("HuaxingFlySword配置表未找到 maxskillId " + maxId);
            return;
        }

        HuaxinEntity huaxinEntity = player.getCurHuaxinEntity();
        huaxinEntity.setExcelId(id);
        player.getFlyswordAllInfo().setCurUseFlySwordId(id);
        if (player.getFlyswordAllInfo().getCurFlySwordSkillId() != maxId) {
            setFlySwordSkill(skillbean, huaxinEntity);
        }
        player.getFlyswordAllInfo().setCurFlySwordSkillId(skillbean.getId());
        sendUseHuaxinResult(player, id, type);

        if (player.playerCrossData.isToFightServer()) {
            sendHxDataToFight(player);
        } else {
            huaxinFlySwordBroadCast(player);
        }
    }

    private void setFlySwordSkill(Cfg_HuaxingFlySword_Bean skillbean, HuaxinEntity huaxinEntity) {
        ConcurrentHashMap<Integer, Skill> baseSkills = huaxinEntity.getBaseSkills();
        Skill skill;
        if (!baseSkills.containsKey(skillbean.getUse_Skill())){
            skill = new Skill();
            skill.setSkillId(skillbean.getUse_Skill());
            baseSkills.put(skillbean.getUse_Skill(), skill);
        }
        if (skillbean.getNormal_Skill() > 0){
            if (!baseSkills.containsKey(skillbean.getNormal_Skill())){
                skill = new Skill();
                skill.setSkillId(skillbean.getNormal_Skill());
                baseSkills.put(skillbean.getNormal_Skill(), skill);
            }
        }
    }

    private void sendHxDataToFight(Player player) {

        MapMessage.ResHuaxinFlySwordBroadCast.Builder msg = MapMessage.ResHuaxinFlySwordBroadCast.newBuilder();
        msg.setPlayerId(player.getId());
        msg.setHuxinFlyID(player.getCurHuaxinEntity().getExcelId());
        msg.setUid(player.getCurHuaxinEntity().getId());

        java.util.Map<Integer, Object> map = new HashMap<>();
        map.put(1, player.getCurHuaxinEntity().getExcelId());
        map.put(2, player.getFlyswordAllInfo().getCurFlySwordSkillId());
        map.put(3, player.getCurHuaxinEntity().getId());
        Manager.playerManager.managerExt().noticeSynRoleInfoToFight(player, SynToFightType.HuaxingFlySwordChange, map, HorseMessage.ResChangeRideState.MsgID.eMsgID_VALUE, msg.build().toByteString());
    }


    private void sendUseHuaxinResult(Player player, int id, int type) {
        HuaxinFlySwordMessage.ResUseHuxinResult.Builder msg = HuaxinFlySwordMessage.ResUseHuxinResult.newBuilder();
        msg.setCurUseFlysword(id);
        msg.setType(type);
        MessageUtils.send_to_player(player, HuaxinFlySwordMessage.ResUseHuxinResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void huaxinFlySwordBroadCast(Player player) {

        HuaxinEntity huaxinEntity = player.getCurHuaxinEntity();
        if (huaxinEntity == null)
            return;
        MapMessage.ResHuaxinFlySwordBroadCast.Builder msg = MapMessage.ResHuaxinFlySwordBroadCast.newBuilder();
        msg.setPlayerId(player.getId());
        msg.setHuxinFlyID(huaxinEntity.getExcelId());
        msg.setUid(huaxinEntity.getId());
        msg.setFeijianMaxID(player.getFlyswordAllInfo().getCurFlySwordSkillId());
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResHuaxinFlySwordBroadCast.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public int getFlySwordActivateState(Player player, int id) {
        for (FlyswordData flyswordData : player.getFlyswordAllInfo().getFlyswordDataMap().values()) {
            if (flyswordData.getActivateList().contains(id)) {
                return 1;
            }
        }
        return 0;
    }

    public int getFlySwordLevel(Player player, int type) {
        if (player.getFlyswordAllInfo().getFlyswordDataMap().containsKey(type)) {
            FlyswordData flyswordData = player.getFlyswordAllInfo().getFlyswordDataMap().get(type);
            if (flyswordData != null) {
                return flyswordData.getLevel();
            }
        }
        return 0;
    }

    public void  onlieInitflySwordSkill(Player player){
        List<Integer> flySwordSkill =  player.getFlyswordAllInfo().getFlySwordSkill();
        for (Cfg_HuaxingFlySword_skill_Bean bean :CfgManager.getCfg_HuaxingFlySword_skill_Container().getValuees()){
            if (!flySwordSkill.contains(bean.getPassive_skill())){
                continue;
            }
            HuaxinEntity huaxinEntity =  player.getCurHuaxinEntity();
            Skill skill = new Skill();
            skill.setSkillId(bean.getPassive_skill());
            huaxinEntity.getBaseSkills().put(bean.getPassive_skill(), skill);
        }
    }
    /**
     * 增加剑灵培养的羁绊技能
     * @param player
     */
    private void activateFlySwordSkill(Player player){
        FlyswordAllInfo flyswordAllInfo = player.getFlyswordAllInfo();
        for (Cfg_HuaxingFlySword_skill_Bean bean :CfgManager.getCfg_HuaxingFlySword_skill_Container().getValuees()){
            if (flyswordAllInfo.getFlySwordSkill().contains(bean.getPassive_skill())){
                continue;
            }
            if (bean.getType() == 1){
                activateTypeOne(player,bean,flyswordAllInfo);
            }
            if (bean.getType() == 2){
                activateTypeTwo(player,bean,flyswordAllInfo);
            }
        }
    }

    private void activateTypeOne(Player player, Cfg_HuaxingFlySword_skill_Bean bean,FlyswordAllInfo flyswordAllInfo ){
        int flyType =  bean.getActive_pram().get(0);
        int steps    =  bean.getActive_pram().get(1);
        if (flyswordAllInfo.getFlyswordDataMap().containsKey(flyType)){
            FlyswordData flyswordData =  flyswordAllInfo.getFlyswordDataMap().get(flyType);
            if (flyswordData.getSteps() >= steps){
                //Manager.skillManager.addSkill(player,bean.getPassive_skill());
                HuaxinEntity huaxinEntity =  player.getCurHuaxinEntity();
                Skill skill = new Skill();
                skill.setSkillId(bean.getPassive_skill());
                huaxinEntity.getBaseSkills().put(bean.getPassive_skill(), skill);
                log.info("learn   skill     ----------- {}",bean.getPassive_skill());
                flyswordAllInfo.getFlySwordSkill().add(bean.getPassive_skill());
            }
        }
    }
    private void activateTypeTwo(Player player, Cfg_HuaxingFlySword_skill_Bean bean,FlyswordAllInfo flyswordAllInfo ){
        int needNum  =  bean.getActive_pram().get(0);
        int steps    =  bean.getActive_pram().get(1);
        int count    =  0;
        for (FlyswordData flyswordData : flyswordAllInfo.getFlyswordDataMap().values()){
            if (flyswordData.getSteps() >= steps){
                count++;
            }
        }
        if (count >= needNum){
            //Manager.skillManager.addSkill(player,bean.getPassive_skill());
            HuaxinEntity huaxinEntity =  player.getCurHuaxinEntity();
            Skill skill = new Skill();
            skill.setSkillId(bean.getPassive_skill());
            huaxinEntity.getBaseSkills().put(bean.getPassive_skill(), skill);
            log.info("learn   skill     ----------- {}",bean.getPassive_skill());
            flyswordAllInfo.getFlySwordSkill().add(bean.getPassive_skill());
        }
    }
}
