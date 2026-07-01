package common.robot;

import com.data.CfgManager;
import com.data.Global;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.game.attribute.AttributeUtils;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseLongAttribute;
import com.game.backpack.structs.Equip;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.behavior.structs.type.AttackMoveBehavior;
import com.game.behavior.structs.type.MagicDirMoveBehavior;
import com.game.behavior.structs.type.MagicMoveBehavior;
import com.game.behavior.structs.type.MoveBehavior;
import com.game.buff.structs.Buff;
import com.game.buff.structs.BuffDefine;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.cooldown.structs.CooldownTypes;
import com.game.fight.script.IFightManagerScript;
import com.game.fight.state.FightState;
import com.game.fight.structs.FightEnum;
import com.game.fight.structs.FightFanalEnum;
import com.game.manager.Manager;
import com.game.map.manager.MapGpsUtil;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.pet.structs.Pet;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.robot.ai.RobotAi;
import com.game.robot.script.IRobotAiScript;
import com.game.robot.script.IRobotScript;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.skill.config.FindTargetInfo;
import com.game.skill.config.SkillEvent;
import com.game.skill.config.SkillEventContainer;
import com.game.skill.config.SkillVisual;
import com.game.skill.config.event.*;
import com.game.skill.structs.MentalSkill;
import com.game.skill.structs.Skill;
import com.game.skill.structs.SkillDefine;
import com.game.skill.structs.SkillMagic;
import com.game.structs.*;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.ShapeUtils;
import com.game.utils.Utils;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.core.util.VersionUpdateUtil;
import game.message.CrossFightMessage;
import game.message.FightMessage;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author admin
 */
public class RobotScript implements IScript, IRobotScript {

	protected Logger logger = LogManager.getLogger(RobotScript.class);

	@Override
	public int getId() {
		return ScriptEnum.RobotBaseScript;
	}

	@Override
	public Object call(Object... objects) {
		return null;
	}

	@Override
	public void OnCalcBuffAttribute(Robot robot, Pet pet, PlayerAttributeType type) {
		/**
		 * 计算BUFF的差距
		 */
		BaseLongAttribute att = new BaseLongAttribute(AttributeType.ATTR_MAX);
		BaseIntAttribute buffatt = robot.getPlayerCalculators().get(PlayerAttributeType.BUFF);
		//当前是玩家的BUFF效果
		if (buffatt != null) {
			AttributeUtils.addAttribute(att, buffatt);
		}

		//本身BUFF效果处理
		//宠物的BUFF生效处理
		try {
			Iterator<Buff> iter = robot.getBuffs().iterator();
			while (iter.hasNext()) {
				Buff buff = iter.next();

				Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());

				if (config.getType() != BuffDefine.Type_Attribute) {
					logger.error(buff.getBuffId() + "不是属性加成BUFF！");
					continue;
				}
				for (ReadArray porpert : config.getPorperty().getValuees()) {
					int atttype = (int) porpert.get(0);
					int value = (int) porpert.get(1);
					if (atttype < 1 || atttype > AttributeType.ATTR_MAX) {
						continue;
					}
					att.addAttribute(atttype, value * buff.getOverlap());
				}
			}
		} catch (Exception e) {
			logger.error("计算宠物属性BUFF 计算错误！key=" + pet.getName() + "," + pet.getModelId());
		}

		//计算旧的BUFF
		AttributeUtils.subAttribute(att, robot.getBuffAttr());

		robot.getBuffAttr().clean();
		AttributeUtils.copyAttribute(robot.getBuffAttr(), att);

		long petPowerFight = 0;
		if (pet != null) {
			Manager.petManager.deal().calPetAttribute(pet);
			petPowerFight = Manager.playerAttAttributeManager.deal().calcFightPower(pet.getAttribute());
		}
		AttributeUtils.clean(att);
		AttributeUtils.addAttribute(att, robot.getAttribute());

		Manager.playerAttAttributeManager.deal().CountBaseRobot(robot, petPowerFight);
		if (RobotAi.JJC == robot.getAi() || RobotAi.Help == robot.getAi()) {
			if (robot.getCurHp() > robot.getAttribute().MaxHP()) {
				robot.setCurHp(robot.getAttribute().MaxHP());
				robot.onHpChange(null);
			}
		}

		if (type == PlayerAttributeType.BUFF) {
			try {
				Manager.monsterManager.manager().monsterAttributeChange(att, robot, PlayerAttributeType.BUFF);
			} catch (Exception e) {
				logger.error(e, e);
			}
		}
	}

	@Override
	public Robot OnMake(Player player) {

		Robot robot = new Robot();
		robot.setMakerId(player.getId());

		robot.setName(player.getName());
		robot.setCareer(player.getCareer());
		robot.setLevel(player.getLevel());
		robot.setFightState(0);
		robot.setWingId(player.getNewFashionData().getWingId());
		robot.setSoulArmorId(player.getSoulArmor().getWearId());

		robot.setGuildId(player.getGuildId());
		robot.setGuildName(player.gainGuildName());
		robot.setGuildPost(player.gainGuildPost());
		robot.setCamp(player.getCamp());
		robot.setTitle(player.getTitleData().getWearId());
		robot.setHeight(player.getHeight());
		robot.setFightPower(player.getFightPoint());
		robot.setFashionBodyId(player.getNewFashionData().getBodyID());
		robot.setFashionWeaponId(player.getNewFashionData().getWeaponID());
		robot.setGrade(player.getXsGrade());
		robot.setRollLevel(player.getRollLevel());
		robot.setStateLv(player.getStateVip().getLv());
		//2020/3/9临时需求普通武器切换时也要更新外观

		int xianjiaPart30 = Manager.immortalEquipManager.manager().getImmFacadeForType(player,30);
		if(xianjiaPart30<=0){
			Equip weapon = Manager.equipManager.getEquipByType(player,1);
			robot.setGodWeaponHead(weapon==null?0:weapon.getItemModelId());
		}else{
			robot.setGodWeaponHead(xianjiaPart30);
		}
		int halo =  Manager.immortalEquipManager.manager().getImmFacadeForType(player,32);
		int matrix =  Manager.immortalEquipManager.manager().getImmFacadeForType(player,33);
		robot.setFashionHalo(halo);
		robot.setFashionMatrix(matrix);
		robot.setGodWeaponBody(0);
		robot.setGodWeaponHeraldry(0);
		robot.setSpiritId(player.getSpiritData().getSpiritId());
//		Integer head = Manager.godWeaponManager.deal().getGodWeaponHead(player);
//		Integer body = Manager.godWeaponManager.deal().getGodWeaponBody(player);
//		Integer heraldry = Manager.godWeaponManager.deal().getGodWeaponHeraldry(player);
//		if (head != null && body != null && heraldry != null) {
//			robot.setGodWeaponHead(head);
//			robot.setGodWeaponBody(body);
//			robot.setGodWeaponHeraldry(heraldry);
//		}

		robot.getBuffAttr().clean();
		Iterator<Entry<PlayerAttributeType, BaseIntAttribute>> iter = player.PlayerCalculators().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<PlayerAttributeType, BaseIntAttribute> en = iter.next();
			//只计算 玩家的BUFF的问题
			if (en.getKey() == PlayerAttributeType.BUFF) {
				AttributeUtils.copyAttribute(robot.getBuffAttr(), player.PlayerCalculators().get(PlayerAttributeType.BUFF));
			}
			BaseIntAttribute att = new BaseIntAttribute(AttributeType.ATTR_MAX);
			AttributeUtils.copyAttribute(att, en.getValue());
			robot.getPlayerCalculators().put(en.getKey(), att);
		}
		//TODO 复制技能
		for (Skill skill : player.getSkills().values()) {
			Skill newSk = new Skill();
			newSk.setSkillId(skill.getSkillId());
			newSk.setLevel(skill.getLevel());
			newSk.setIsNormal(skill.isIsNormal());
			robot.getSkills().put(skill.getSkillId(), newSk);
		}
//        robot.getSkilladds().putAll(player.getSkilladds());
//        robot.setSpiritBall(player.getSpiritBall());
		//TODO 复制属性
		BaseIntAttribute att = new BaseIntAttribute(AttributeType.ATTR_MAX);
		AttributeUtils.copyAttribute(att, player.getAttribute());

		//修正一下免得出错了
		if (player.getAttribute().getAdditionValue(AttributeType.ATTR_Speed) == 0) {
			att.setAttribute(AttributeType.ATTR_Speed, 2000);

			logger.error("没有基础移动速度，查看一下吧，", new Exception());
			logger.error(player.nameIdString() + " att:" + player.getAttribute());
		}

		att.calFinalAttackSpeed();
		att.calFinalMoveSpeed();
		att.cleanMaxHP();
		att.addMaxHP(player.getAttribute().MaxHP());
		AttributeUtils.addAttribute(robot.getAttribute(), att);
		robot.getAttribute().cleanMaxHP();
		robot.getAttribute().addMaxHP(player.getAttribute().MaxHP());
		robot.getAttribute().calFinalMoveSpeed();
		//robot.setAttribute(att);
		robot.setCurHp(att.MaxHP());
		robot.getSkillIds().clear();
		//构建机器人的技能
		for (MentalSkill sca : player.getMentalskills().values()) {
			Cfg_Occ_Skill_Bean Cfg_Occ_Skill_Bean = CfgManager.getCfg_Occ_Skill_Container().getValueByKey(sca.getBaseSkillID());
			if (Cfg_Occ_Skill_Bean == null) {
				continue;
			}
			if (Cfg_Occ_Skill_Bean.getMental_ID() >1){
				continue;
			}
			for (int skillId : sca.getSkillID().values()) {
				robot.getSkillIds().add(skillId);
			}
		}
		//TODO 复制地图信息
		MapGpsUtil.CopyGPS(player.getCurGps(), robot.getCurGps());

		robot.setCurHp(robot.getAttribute().MaxHP());
		//开始复制宠物
		Pet pet = Manager.petManager.getBattlePet(player);
		if (pet == null) {
			return robot;
		}

		Pet rpet = new Pet();
		rpet.setOwnerId(robot.getId());
		rpet.setName(pet.getName());
		rpet.setModelId(pet.getModelId());
		rpet.setLevel(pet.getLevel());
		rpet.getSkills().putAll(pet.getSkills());
//        BaseIntAttribute patt = new BaseIntAttribute(AttributeType.AttributeTypeCount);
		AttributeUtils.addAttribute(rpet.getAttribute(), pet.getAttribute());
//        patt.AddMaxHP(pet.getAttribute().MaxHP());
//        rpet.setAttribute(patt);
		rpet.reset();
		rpet.setCamp(player.getCamp());
		rpet.setOwnerName(robot.getName());


		robot.setPet(rpet);
		robot.getPets().put(rpet.getModelId(), rpet);

		robot.setHairId(0);
		robot.setHairFrameId(0);
		robot.setBuchenVfxId(0);
		robot.setStifleFabaoId(player.getStifleData().getNature().getCurrentModelId());
		robot.setHairG(0);
		robot.setHairB(0);
		robot.setHairR(0);

		Manager.petManager.deal().calPetAttribute(rpet);
		//玩家的机器人拷贝
//        RobotManager.getInstance().getRobots().put(robot.getId(), robot);
		return robot;
	}

	@Override
	public Robot OnMake(long roleId) {
		Player player = initPlayer( roleId);
        if (player == null) return null;
		Robot robot = OnMake(player);
        OnCalcRobotAttribute(robot);
		return robot;
	}


	private Player initPlayer(long roleId)
    {
        Player player = Manager.playerManager.getPlayer(roleId);
		if (player == null) {
			logger.error("roleId =" + roleId + "玩家不存在了");
			return null;
		}
		logger.info(player.nameIdString());
		Pet pet = Manager.petManager.getBattlePet(player);
		if (pet != null) {
			Manager.petManager.deal().calPetAttribute(pet);//计算宠物的战力
		}
		Manager.playerAttAttributeManager.deal().initPlayerAttribute(player, false);
        return player;
    }

	private void OnCalcRobotAttribute(Robot robot)
    {
        OnCalcBuffAttribute(robot, robot.getPet(), PlayerAttributeType.BASE);
        robot.getAttribute().calFinalAttackSpeed();
        robot.getAttribute().calFinalMoveSpeed();
    }
    private void OnResetRobotAttributePercent(Robot robot,float Percent)
    {
		int[] types = AttributeType.ATTR_FIX[0];
		int type = types[0];
		for (PlayerAttributeType attributeType: PlayerAttributeType.values()) {
			BaseIntAttribute attr = robot.getPlayerCalculators().get(attributeType);
			if (attr == null) {
				continue;
			}
			int value = attr.getAdditionValue(type);
			value = (int) (value * Percent);
			attr.setAttribute(type,value);
		}
    }

	@Override
	public Robot OnMakeByJJCConfig(int configId) {
		int excelId = configId / 10000;
		Cfg_Jjcrobot_Bean config = CfgManager.getCfg_Jjcrobot_Container().getValueByKey(excelId);
		if (config == null) {
			return null;
		}

		Robot robot = new Robot();
		robot.setModelId(excelId);
		robot.setMakerId(configId);

		StringBuilder name = new StringBuilder();
		name.append(config.getName());
		robot.setName(name.toString());
		robot.setCareer(config.getCareer());
		robot.setLevel(50);
		robot.setFightState(0);
		robot.setWeaponId(config.getWeaponsEquipId());
		robot.setWingId(config.getWingId());

		robot.setGuildId(0);
		robot.setGuildName("");
		robot.setGuildPost(0);
		robot.setCamp(0);
		robot.setTitle(70054);  //策划指定写死的
		robot.setHeight(5f);
		robot.setPicTitle(0);

		robot.setFashionBodyId(config.getFashionId());
		robot.setFashionWeaponId(config.getFashionId());
		robot.setGrade(1);
		// 复制属性
		BaseIntAttribute att = new BaseIntAttribute(AttributeType.ATTR_MAX);
		AttributeUtils.init(config.getAttribute(), att);
		robot.getPlayerCalculators().put(PlayerAttributeType.BASE, att);
		//BUFF属性添加
		robot.getPlayerCalculators().put(PlayerAttributeType.BUFF, new BaseIntAttribute(AttributeType.ATTR_MAX));
		long maxHp = att.getAdditionValue(AttributeType.ATTR_MaxHp);
		att.calFinalAttackSpeed();
		att.calFinalMoveSpeed();
		att.cleanMaxHP();
		att.addMaxHP(maxHp);
		AttributeUtils.addAttribute(robot.getAttribute(), att);
		robot.getAttribute().cleanMaxHP();
		robot.getAttribute().addMaxHP(maxHp);
//        robot.setAttribute(att);
		robot.setCurHp(att.MaxHP());
		robot.setCurHp(robot.getAttribute().MaxHP());
		robot.getAttribute().calFinalMoveSpeed();
		robot.setHairB(0);
		robot.setHairG(0);
		robot.setHairR(0);
		robot.setHairFrameId(0);
		robot.setBuchenVfxId(0);
		robot.setBuchenVfxId(0);
		robot.setStifleFabaoId(0);
		robot.setSpiritId(0);
		robot.setStateLv(5);
		//计算战力
		robot.setFightPower(Manager.playerAttAttributeManager.deal().calcFightPower(robot.getAttribute()));
		// 初始化技能
		for (ReadArray sa : config.getSkill().getValuees()) {
			int skillId = (int) sa.get(0);
			int level = (int) sa.get(1);
			// 用技能等级设置机器人等级,可能不对.
			robot.setLevel(level);
			Cfg_Skill_Bean sc = CfgManager.getCfg_Skill_Container().getValueByKey(skillId);
			if (sc == null) {
				continue;
			}
			if (robot.getSkills().containsKey(skillId)) {
				continue;
			}
			Skill skill = new Skill();
			skill.setSkillId(skillId);
			skill.setLevel(level);
			robot.getSkills().put(skill.getSkillId(), skill);
			robot.getSkillIds().add(skillId);
		}

		return robot;

	}

	@Override
	public Robot OnMakeByRobotConfig(int configId) {
		Cfg_Clonerobot_Bean config = CfgManager.getCfg_Clonerobot_Container().getValueByKey(configId);
		if (config == null) {
			return null;
		}

		Robot robot = new Robot();
		robot.setModelId(configId);
		robot.setMakerId(configId);

		robot.setName(config.getName());
        robot.setLevel(config.getLevel());
        robot.setCareer(config.getCareer());

        robot.setWeaponId(0);
		robot.setFightState(0);
		robot.setWingId(0);
		robot.setGuildId(0);
		robot.setGuildName("");
		robot.setGuildPost(0);
		robot.setCamp(0);
		robot.setTitle(config.getTitle());
		robot.setHeight(5f);
		robot.setPicTitle(0);
		robot.setFashionBodyId(config.getClotheEquipId());
		robot.setFashionWeaponId(config.getWeaponsEquipId());
		robot.setFashionHalo(config.getGuanghuanId());
		robot.setFashionMatrix(config.getZhendaoId());
		robot.setGrade(1);

		BaseIntAttribute att = new BaseIntAttribute(AttributeType.ATTR_MAX);
		AttributeUtils.init(config.getAttribute(), att);
		robot.getPlayerCalculators().put(PlayerAttributeType.BASE, att);
		//BUFF属性添加
		robot.getPlayerCalculators().put(PlayerAttributeType.BUFF, new BaseIntAttribute(AttributeType.ATTR_MAX));
		int maxHp = att.getAdditionValue(AttributeType.ATTR_MaxHp);
		att.calFinalAttackSpeed();
		att.calFinalMoveSpeed();
		att.cleanMaxHP();
		att.addMaxHP(maxHp);
		AttributeUtils.addAttribute(robot.getAttribute(), att);
		robot.getAttribute().cleanMaxHP();
		robot.getAttribute().addMaxHP(maxHp);
		robot.setCurHp(att.MaxHP());
		robot.setCurHp(robot.getAttribute().MaxHP());
        robot.getAttribute().calFinalMoveSpeed();
		//计算战力
		robot.setFightPower(Manager.playerAttAttributeManager.deal().calcFightPower(robot.getAttribute()));
		for (ReadArray sa : config.getSkill().getValuees()) {
			int skillId = (int) sa.get(0);
			int level = (int) sa.get(1);
			Cfg_Skill_Bean sc = CfgManager.getCfg_Skill_Container().getValueByKey(skillId);
			if (sc == null) {
				continue;
			}
			if (robot.getSkills().containsKey(skillId)) {
				continue;
			}
			Skill skill = new Skill();
			skill.setSkillId(skillId);
			skill.setLevel(level);
			robot.getSkills().put(skill.getSkillId(), skill);
			robot.getSkillIds().add(skillId);
		}
		robot.setHairB(0);
		robot.setHairG(0);
		robot.setHairR(0);
		robot.setHairFrameId(0);
		robot.setBuchenVfxId(0);
		robot.setBuchenVfxId(0);
		robot.setStifleFabaoId(0);
		robot.setSpiritId(0);

		return robot;

	}

	@Override
	public Robot OnMake(Player player, float attributePercent, boolean rename) {
		if (player == null) return null;
		Robot robot = OnMake(player);
		OnResetRobotAttributePercent(robot, attributePercent);
		if(rename){
			robot.setName(randomNameByCarceer(robot.getCareer()));
		}
		OnCalcRobotAttribute(robot);
		return robot;
	}

	//    根据职业随机一个名字
	private String randomNameByCarceer(int carceer) {
		Cfg_Robotrandomname_Bean[] beans = CfgManager.getCfg_Robotrandomname_Container().getValuees();
		int sax = 0;
//        ID=职业id*100+技能位置ID（职业0学生，1拳师，2大锤，3剑客，4卡牌，5枪手）（技能位置1-5，1为普通攻击）
		switch (carceer) {
			case 0:
			case 1:
			case 5:
				sax = 3;
				break;
			case 2:
			case 3:
			case 4:
				sax = 2;
				break;
		}
		List<String> names = new ArrayList<>();//名字
		List<String> surnames = new ArrayList<>();//姓氏
		for (Cfg_Robotrandomname_Bean bean : beans) {
			if (bean.getQ_type() == 1) {
				surnames.add(bean.getQ_value());
				continue;
			}
			if (bean.getQ_type() == sax) {
				names.add(bean.getQ_value());
			}
		}
		String name = names.get(org.apache.commons.lang.math.RandomUtils.nextInt(names.size()));
		String surname = surnames.get(org.apache.commons.lang.math.RandomUtils.nextInt(surnames.size()));
		return name + surname;
	}

	//检查机器人生命周期
	private boolean checkRobotLiveTime(Robot robot) {
		MapObject mapObject = Manager.mapManager.getMap(robot.gainMapId());
		if (mapObject != null) {
			if (robot.getLiveTime() != 0 && TimeUtils.Time() > robot.getLiveTime()) {
				Manager.mapManager.manager().onQuitMap(mapObject, robot, true);
				return true;
			}
		}
		return false;
	}

	@Override
	public void tick(Robot robot) {

		long nowTime =  TimeUtils.Time();
		if (robot.BeginWork > 0 && robot.BeginWork > nowTime) {
			return;
		}
		if (checkRobotLiveTime(robot))
			return;
		if (robot.getPet() != null && !robot.getPet().getBuffs().isEmpty()) {
			try {
				Manager.buffManager.deal().tick(robot.getPet(), robot);
			} catch (Exception e) {
				logger.error(e, e);
			}
		}

		//如果是战斗状态检测是否脱战
		if (robot.getLastFight() > nowTime || robot.getLastFight() + Global.Off_War_Time < nowTime){
			robot.setLastFight(0);
			robot.setInBattle(false);
		}
		if (!robot.isInBattle() && !robot.isDie()){
			long roleHp = robot.getAttribute().getAdditionValue(AttributeType.ATTR_RoleHp);
			if (roleHp != 0 && robot.getCurHp() < robot.getAttribute().MaxHP() && !Manager.buffManager.deal().haveJinLiao(robot)) {
				robot.setCurHp(robot.getCurHp() + (long) (robot.getAttribute().MaxHP() * roleHp / 10000F));
				robot.onHpChange(robot);
			}
		}
		if (RobotAi.JJC == robot.getAi()) {
			OnRobotAiJJC(robot);
			return;
		}

		if (RobotAi.Help == robot.getAi()) {
			OnRobotAiHelp(robot);
			return;
		}

		if (RobotAi.CUSTOM == robot.getAi()) {
			OnRobotAiCustom(robot);
		}
	}

	private void OnRobotAiCustom(Robot robot) {
		if (robot.gainRunningAi() == null) {
			return;
		}
		IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.RobotAiCommonScript);
		if (is instanceof IRobotAiScript) {
			IRobotAiScript iabs = (IRobotAiScript) is;

			Player player = Manager.playerManager.getPlayerOnline(robot.getMakerId());
			iabs.OnRobotAi(robot, player, Manager.mapManager.getMap(robot.gainMapId()));
		}
	}

	private void OnRobotAiHelp(Robot robot) {

		// OnPetAi(robot);

		OnFightStateAi(robot);

		OnMagicAi(robot);

		OnBuffAi(robot);

		//TODO 获取攻击目标
		MapObject map = Manager.mapManager.getMap(robot.gainMapId());
		if (map == null) {
			return;
		}
		//受击移动
		if (OnBeAttackMoveAi(robot)) {

			//受击僵直中，检测翻滚躲避
			Hatred target = getCurMainTarget(robot);
			if (target == null) {
				return;
			}

			if (Manager.cooldownManager.isCooldowning(robot, CooldownTypes.RollCD, null)) {
				return;
			}

			Cfg_RollDodge_Bean config = CfgManager.getCfg_RollDodge_Container().getValueByKey(robot.getRollLevel());

			Position pos = target.getTarget().getPursuePos(robot, config.getMax_dis() / 100f);
			if (Utils.isCanMove(map, robot.gainCurPos(), pos)) {
				robot.getAmbs().clear();
				BehaviorManager.CancelAllBehavior(robot);
				robot.changeCurPos(pos, true);

				FightMessage.ResRollMove.Builder msg = FightMessage.ResRollMove.newBuilder();
				msg.setUserID(robot.getId());
				msg.setMoveToX(pos.getX());
				msg.setMoveToY(pos.getY());
				MessageUtils.send_to_roundPlayer(robot, FightMessage.ResRollMove.MsgID.eMsgID_VALUE, msg.build().toByteArray());
				//增加500毫秒霸体时间
//                robot.addFightState(new SuperArmorState(), config.getSuper_armor_time());

				Manager.cooldownManager.addCooldown(robot, CooldownTypes.RollCD, null, config.getCd_time() * 2 / 3);
				Manager.cooldownManager.addCooldown(robot, CooldownTypes.RollMove, null, config.getExecute_time());
			}

			return;
		}
		//TODO 状态检测
		if (EntityState.Dead.compare(robot.getState())) {
			return;
		}

		if (Manager.cooldownManager.isCooldowning(robot, CooldownTypes.RollMove, null)) {
			return;
		}

		searchMonster(robot, map);

		Hatred target = getCloseMainTarget(robot);
		if (target == null) {
			//MoveToMaster(robot);
			return;
		}
		if (target.getTarget() instanceof  Player){
			return;
		}

		//TODO 获取技能
		Skill canUseSkill = getCanUseSkill(robot);
		if (canUseSkill == null) {
			return;
		}
//		logger.info("robot = " + robot.getName() + ",使用技能=" + canUseSkill.getSkillId());

		Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(canUseSkill.getSkillId());
		SkillVisual sv = Manager.skilleventManager.GetSkillVisualBySV(skillBean.getVisualDef());
		if (sv == null)
			return;

		// 看看当前是不是已经在跑引导技能了,ai的引导技能走behavior
		if (sv.isLockTrajact() && robot.getCurSlowSkill() != null) {
			return;
		}

		if (OnPursueAi(robot, target, canUseSkill, sv)) {
			return;
		}

		if (EntityState.Move.compare(robot.getState())) {
			return;
		}
		logger.info(robot.getName() + "使用技能：" + canUseSkill.getSkillId() + " , 目标是：" + target.getTarget().getName());

		// 释放技能
		UseSkill(robot, target.getTarget(), canUseSkill, sv);

	}

	private boolean OnFindMM(Player player, Robot robot) {
		if (Manager.cooldownManager.isCooldowning(robot, CooldownTypes.Move, null)) {
			return false;
		}
		float dis = Utils.getDistance(player.gainCurPos(), robot.gainCurPos());

		if (dis < 5.5f) {
			return false;
		}
		Position pos = player.getPursuePos(robot, 2.5f);
		List<Position> roads = new ArrayList<>();
		roads.add(robot.gainCurPos());
		roads.add(pos);
		robot.setRoads(roads);

		if (!EntityState.Move.compare(robot.getState())) {
			robot.addState(EntityState.Move);
			BehaviorManager.InsertOnlyBehavior(robot, new MoveBehavior(robot));
		}

		robot.getDirs().clear();
		MapMessage.ResMoveTo.Builder moveMsg = MapMessage.ResMoveTo.newBuilder();
		moveMsg.setObjectId(robot.getId());
		for (Position tar : roads) {
			moveMsg.addPosList(MapUtils.getPos(tar));
		}
		MessageUtils.send_to_roundPlayer(player, MapMessage.ResMoveTo.MsgID.eMsgID_VALUE, moveMsg.build().toByteArray(), true);
//        MessageUtils.send_to_player(player, MapMessage.ResMoveTo.MsgID.eMsgID_VALUE, moveMsg.build().toByteArray());

		Manager.cooldownManager.addCooldown(robot, CooldownTypes.Move, null, 333);

		//LOGGER.info("宠物移动cur=" + pet.gainCurPos() + " tar=" + pos);
		return true;
	}

	private boolean OnPetFight(Player player, Robot robot) {
		return true;
	}

	//竞技场机器人
	private void OnRobotAiJJC(Robot robot) {

		// OnPetAi(robot);

		OnFightStateAi(robot);

		OnMagicAi(robot);

		OnBuffAi(robot);

		//TODO 获取攻击目标
		MapObject map = Manager.mapManager.getMap(robot.gainMapId());
		if (map == null) {
			return;
		}

		//受击移动
		if (OnBeAttackMoveAi(robot)) {

			//受击僵直中，检测翻滚躲避
			Hatred target = getCurMainTarget(robot);
			if (target == null) {
				return;
			}

			if (Manager.cooldownManager.isCooldowning(robot, CooldownTypes.RollCD, null)) {
				return;
			}

			Cfg_RollDodge_Bean config = CfgManager.getCfg_RollDodge_Container().getValueByKey(robot.getRollLevel());

			Position pos = target.getTarget().getPursuePos(robot, config.getMax_dis() / 100f);
			if (Utils.isCanMove(map, robot.gainCurPos(), pos)) {
				FightMessage.ResRollMove.Builder msg = FightMessage.ResRollMove.newBuilder();
				msg.setUserID(robot.getId());
				msg.setSelfX(robot.gainX());
				msg.setSelfY(robot.gainY());
				robot.getAmbs().clear();
				BehaviorManager.CancelAllBehavior(robot);
				robot.changeCurPos(pos, true);
				msg.setMoveToX(pos.getX());
				msg.setMoveToY(pos.getY());
				MessageUtils.send_to_roundPlayer(robot, FightMessage.ResRollMove.MsgID.eMsgID_VALUE, msg.build().toByteArray());
				//增加500毫秒霸体时间
//                robot.addFightState(new SuperArmorState(), config.getSuper_armor_time());

				Manager.cooldownManager.addCooldown(robot, CooldownTypes.RollCD, null, config.getCd_time() * 2 / 3);
				Manager.cooldownManager.addCooldown(robot, CooldownTypes.RollMove, null, config.getExecute_time());
			}

			return;
		}

		//TODO 状态检测
		if (EntityState.Dead.compare(robot.getState())) {
			return;
		}

		if (Manager.cooldownManager.isCooldowning(robot, CooldownTypes.RollMove, null)) {
			return;
		}

		searchEnemyJJC(robot, map);

		Hatred target = getCurMainTarget(robot);
		if (target == null) {
			return;
		}

		//TODO 获取技能
		Skill canUseSkill = getCanUseSkill(robot);
		if (canUseSkill == null) {
			return;
		}
		logger.info("robot = " + robot.getName() + ",使用技能=" + canUseSkill.getSkillId());

		Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(canUseSkill.getSkillId());
		SkillVisual sv = Manager.skilleventManager.GetSkillVisualBySV(skillBean.getVisualDef());

		// 看看当前是不是已经在跑引导技能了,ai的引导技能走behavior
		if (sv.isLockTrajact() && robot.getCurSlowSkill() != null) {
			return;
		}

		if (OnPursueAi(robot, target, canUseSkill, sv)) {
			return;
		}

		if (EntityState.Move.compare(robot.getState())) {
			return;
		}
		logger.info(robot.getName() + "使用技能：" + canUseSkill.getSkillId() + " , 目标是：" + target.getTarget().getName());

		// 释放技能
		UseSkill(robot, target.getTarget(), canUseSkill, sv);
	}

	private void UseSkill(Entity attker, Fighter target, Skill skill, SkillVisual sv) {
		// 设置一个除了自己都能打的搜索条件
		attker.changeSkillTargetFilter(ScriptEnum.AiDefaultSkillTargetCheckerCommonScript);
		Position dir = Utils.getDir(attker.gainCurPos(), target.gainCurPos());
		attker.setDir(dir);

		List<Fighter> enemys = getEnemies(attker, target, skill, sv);
		if (0 == enemys.size()) {
			List<Skill> readys = attker.getReadyLists();
			if (!readys.isEmpty()) {
				attker.setUseSkill(readys.remove(0));
			} else {
				attker.setUseSkill(null);
			}
			return;
		}

//		logger.info(attker.getName() + "当前时间：" + TimeUtils.Time() + " cd:" + sv.getCd() + " def:" + sv.getId() + " 攻击数量:" + enemys.size());
		IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);
		is.attack(attker, skill, enemys, null);
		attker.setUseSkill(null);
	}

	private boolean OnPursueAi(Robot robot, Hatred target, Skill skill, SkillVisual sv) {

		//TODO 检测追击
		Fighter defer = target.getTarget();
		if (EntityState.Dead.compare(defer.getState()) || defer.isDie()) {
//            LOGGER.info("[" + defer + "]追击目标死亡 目标=" + defer);
			robot.removeHatred(defer);
			return true;
		}
		if (!defer.isEqualMap(robot)) {
			robot.removeHatred(defer);
			return true;
		}
		float attackDis = getSkillAttackDis(sv);
		float dis = Utils.getDistance(robot.gainCurPos(), defer.gainCurPos());
		//检测攻击距离

		if (dis > attackDis) {

			//战斗不可移动
			if (robot.getCurSlowSkill() != null && !sv.isCanMove()) {
				return false;
			}
			// 追击
			return MoveToTarget(robot, target.getTarget(), attackDis);
		} else {
			if (EntityState.Move.compare(robot.getState())) {
				robot.removeSate(EntityState.Move);
				BehaviorManager.CancelBehaviorByType(robot, BehaviorType.Move);
			}
		}

		return false;
	}

	private boolean MoveToMaster(Robot robot) {
		Player player = PlayerManager.getInstance().getPlayerCache(robot.getMakerId());
		if (player == null)
			return false;
		MapObject masterMap = Manager.mapManager.getMap(player.gainMapId());
		if (!Manager.mapManager.getMap(robot.gainMapId()).equals(masterMap))
			return false;
		float dis = Utils.getDistance(robot.gainCurPos(), player.gainCurPos());
		if (dis > 1f) {
			// 追击
			return MoveToTarget(robot, player, 1f);
		} else {
			if (EntityState.Move.compare(robot.getState())) {
				robot.removeSate(EntityState.Move);
				BehaviorManager.CancelBehaviorByType(robot, BehaviorType.Move);
			}
		}
		return false;
	}

	private boolean MoveToTarget(Robot robot, Fighter target, float attackDis) {

		Fighter defer = target;
		if (Manager.cooldownManager.isCooldowning(robot, CooldownTypes.MonsterPursue, null)) {
			return true;
		}

		MapObject map = Manager.mapManager.getMap(robot.gainMapId());

		Position pursuePos = defer.getPursuePos(robot, attackDis);
		if (!Utils.isCanMove(map, pursuePos)) {
			pursuePos = defer.gainCurPos();
		}
		List<Position> roads;
		if (Utils.isCanMove(map, robot.gainCurPos(), pursuePos)) {
			roads = new ArrayList<>();
			roads.add(robot.gainCurPos());
			roads.add(pursuePos);
		} else {
			roads = MapUtils.findRoads(map, robot.gainCurPos(), pursuePos, 500);
		}

		robot.setRoads(roads);

		if (!EntityState.Move.compare(robot.getState())) {
			robot.addState(EntityState.Move);
			BehaviorManager.InsertOnlyBehavior(robot, new MoveBehavior(robot));

		}
		Manager.mapManager.sendMoveMessage(robot, roads);

		Manager.cooldownManager.addCooldown(robot, CooldownTypes.MonsterPursue, null, 333);
		return true;
	}

	//获取查找事件
	private SkillEvent getFindEvent(SkillVisual sv) {
		for (SkillEvent event : sv.getEventList()) {
			if (event instanceof CommonEvent) {
				continue;
			}
			return event;
		}
		return null;
	}

	//获取攻击目标列表
	private List<Fighter> getEnemies(Entity robot, Fighter defer, Skill skill, SkillVisual sv) {
		List<Fighter> enemys = new ArrayList<>();

		SkillEvent skillEvent = getFindEvent(sv);
		if (skillEvent == null) {
			return enemys;
		}

		if (skillEvent instanceof PlaySelfMoveEvent) {
			Entity mover = (Entity) robot;
			//修改方向
			mover.setDir(Utils.getDir(robot.gainCurPos(), defer.gainCurPos()));
			FightMessage.ResChangeAttackDirRes.Builder msg = FightMessage.ResChangeAttackDirRes.newBuilder();
			msg.setDirX(mover.getDir().getX());
			msg.setDirY(mover.getDir().getY());
			msg.setRoleId(mover.getId());
			MessageUtils.send_to_roundPlayer(mover, FightMessage.ResChangeAttackDirRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());

		} else if (skillEvent instanceof PlaySkillObjectEvent) {
			PlaySkillObjectEvent tempSkillEvent = (PlaySkillObjectEvent) skillEvent;
			SkillVisual sv2 = SkillEventContainer.getInstance().GetSkillVisualBySV(tempSkillEvent.getSkillName());
			if (sv2 == null) {
				logger.error("玩家无这个技能效果配置缺失skillId=" + tempSkillEvent.getSkillName() + "monster" + robot);
				return enemys;
			}
			MapObject map = Manager.mapManager.getMap(robot.gainMapId());

			SkillMagic magic = Manager.skillManager.createMagic(tempSkillEvent.getSkillName());
			magic.setOwnerId(robot.getId());
			magic.setSkillId(skill.getSkillId());
			MapGpsUtil.CopyGPS(robot.getCurGps(), magic.getCurGps());
			magic.setIntType(tempSkillEvent.getPosType());
			magic.setInitTime(TimeUtils.Time());
			//当前世间到召唤事件生效时间
			magic.setStart(TimeUtils.Time());

			for (SkillEvent event : sv2.getEventList()) {
				if (event instanceof PlayHitEvent) {
					magic.getSerials().add(event.getEventID());
					magic.setRadius(event.getHitDis(0));
				}
			}
			//释放一个目标的召唤物
			if (tempSkillEvent.getPosType() == FightFanalEnum.MagicPosType.MainTargetPos.getValue()) {
				Fighter mainTarget = defer;
				enemys.add(defer);
				Position targetPos = null;
				if (mainTarget != null) {
					targetPos = mainTarget.gainCurPos();
					float dis = Utils.getDistance(robot.gainCurPos(), targetPos);
					if (dis < (tempSkillEvent.getMaxDis() * 1.5f) + robot.getRadius()) {
						magic.changeCurPos(targetPos);
					}
				}

				if (targetPos == null) {
					Position dirpos = robot.getDir();
					targetPos = Utils.getCanFightPosByDir(map, robot.gainCurPos(), dirpos, tempSkillEvent.getMaxDis());
					magic.changeCurPos(targetPos);
				}
				//如果是负值则不做设置处理
//                if (fevent.getX() > 0 && fevent.getY() > 0) {
//                    Position dirpos = new Position(fevent.getX(), fevent.getY());
//                    float dis = Utils.getDistance(robot.gainCurPos(), dirpos);
//                    if (dis < (tempSkillEvent.getMaxDis() * 5f) + robot.getRadius()) {
//                        magic.changeCurPos(dirpos);
//                    }
//                }
			}
			if (tempSkillEvent.getMoveType() != 2) {
				//加入， 进入地图
				robot.getMagics().add(magic.getId());
				Manager.mapManager.manager().onEnterMap(magic);
			}

			FightMessage.ResPlaySkillObject.Builder msg = FightMessage.ResPlaySkillObject.newBuilder();
			msg.setPosX(magic.gainX());
			msg.setPosY(magic.gainY());
			msg.setID(magic.getId());
			msg.setOwnerID(robot.getId());
			msg.setVisualDef(tempSkillEvent.getSkillName());
			msg.setMoveSpeed(tempSkillEvent.getMoveSpeed());
			msg.setMpveAddSpeed(tempSkillEvent.getMoveAddSpeed());
			//计算玩家的移动点
			if (tempSkillEvent.getMoveType() == 0) {
				MessageUtils.send_to_roundPlayer(robot, FightMessage.ResPlaySkillObject.MsgID.eMsgID_VALUE, msg.build().toByteArray());
				/**
				 * TODO  需要检查这里的逻辑 解决107技能没有cd的问题
				 * */
//                return enemys;
			} else if (tempSkillEvent.getMoveType() == 1) {
				/**
				 * 定点移动
				 * */
				Position dir = robot.getDir();
				//计算方向
				double x = dir.getX();
				double y = dir.getY();

				if (Float.compare(tempSkillEvent.getFixDirOffsetAngle(), 0f) != 0) {

					double cos = Math.cos(tempSkillEvent.getFixDirOffsetAngle() * Math.PI / 180);
					double sin = Math.sin(tempSkillEvent.getFixDirOffsetAngle() * Math.PI / 180);
					double fsin = Math.sin(-1 * tempSkillEvent.getFixDirOffsetAngle() * Math.PI / 180);

					x = dir.getX() * cos + dir.getY() * sin;
					y = dir.getY() * cos + dir.getX() * fsin;
				}

				MagicMoveBehavior mmb = new MagicMoveBehavior(magic);
				mmb.setaSpeed(tempSkillEvent.getMoveAddSpeed());
				mmb.setSpeed(tempSkillEvent.getMoveSpeed());
				mmb.setBeginTime(TimeUtils.Time());
				mmb.setDirPos(new Position((float) x, (float) y));
				mmb.setLastDis(0);
				BehaviorManager.InsertBehavior(magic, mmb);

				float t = sv2.getCd() / 1000.0f;
				double s = tempSkillEvent.getMoveSpeed() * t + (1 / 2) * tempSkillEvent.getMoveAddSpeed() * Math.pow(t, 2);

				Position pos = Utils.getPosByDir(magic.gainCurPos(), mmb.getDirPos(), (float) s);
				msg.addMovePosList(MapUtils.getPos(magic.gainCurPos()));
				msg.addMovePosList(MapUtils.getPos(pos));
				MessageUtils.send_to_roundPlayer(robot, FightMessage.ResPlaySkillObject.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//                return enemys;
			} else if (tempSkillEvent.getMoveType() == 2) {
				/**
				 * 追踪移动
				 * */
				Fighter mainTarget = defer;
				if (mainTarget == null) {
					return enemys;
				}
				Position dirPos = Utils.getDir(robot.gainCurPos(), mainTarget.gainCurPos());
				MagicDirMoveBehavior mmb = new MagicDirMoveBehavior(magic);
				mmb.setaSpeed(tempSkillEvent.getMoveAddSpeed());
				mmb.setSpeed(tempSkillEvent.getMoveSpeed());
				mmb.setBeginTime(TimeUtils.Time());
				mmb.setDirPos(dirPos);
				mmb.setLastDis(0);
				mmb.setDefer(mainTarget);
				mmb.setXy(mainTarget.gainCurPos().ceilX(), mainTarget.gainCurPos().ceilY());
				mmb.setSkillVisalName(tempSkillEvent.getSkillName());
				BehaviorManager.InsertBehavior(magic, mmb);
				//加入， 进入地图
				robot.getMagics().add(magic.getId());
				Manager.mapManager.manager().onEnterMap(magic);
				msg.addMovePosList(MapUtils.getPos(magic.gainCurPos()));
				msg.addMovePosList(MapUtils.getPos(mainTarget.gainCurPos()));
			}
			MessageUtils.send_to_roundPlayer(robot, FightMessage.ResPlaySkillObject.MsgID.eMsgID_VALUE, msg.build().toByteArray());
		}

		if (ShapeUtils.inAttackArea(skillEvent.getShape(), robot.gainCurPos(), robot.getDir(), defer.gainCurPos())) {
			enemys.add(defer);
		}

//        float attackDis = getSkillAttackDis(sv);
		int number = getSkillAttackCount(sv);
		List<Hatred> hats = new ArrayList<>(robot.getHatreds());
		for (Hatred hat : hats) {
			if (enemys.size() >= number) {
				return enemys;
			}
			Fighter odefer = hat.getTarget();
			if (enemys.contains(odefer)) {
				continue;
			}
			if (odefer.isDie()) {
				robot.removeHatred(hat);
				continue;
			}
			if (ShapeUtils.inAttackArea(skillEvent.getShape(), robot.gainCurPos(), robot.getDir(), odefer.gainCurPos())) {
				enemys.add(odefer);
			}
		}

		return enemys;
	}

	//获取攻击目标列表
	private List<Fighter> getEnemies(Robot robot, Pet pet, Fighter defer, Skill skill, SkillVisual sv) {
		List<Fighter> anemys = new ArrayList<>();

		SkillEvent enent = getFindEvent(sv);
		if (enent == null) {
			return anemys;
		}

		if (ShapeUtils.inAttackArea(enent.getShape(), robot.gainCurPos(), robot.getDir(), defer.gainCurPos())) {
			anemys.add(defer);
		}
//        anemys.add(defer);

		float attackDis = getSkillAttackDis(sv);
		int number = getSkillAttackCount(sv);
		List<Hatred> hats = new ArrayList<>(robot.getHatreds());

		for (Hatred hat : hats) {
			if (anemys.size() >= number) {
				return anemys;
			}
			Fighter odefer = hat.getTarget();
			if (odefer.isDie()) {
				robot.removeHatred(hat);
				continue;
			}
//            float dis = Utils.getDistance(pet.gainCurPos(), odefer.gainCurPos());
//            if (dis <= attackDis) {
//                anemys.add(odefer);
//            }
			if (ShapeUtils.inAttackArea(enent.getShape(), robot.gainCurPos(), robot.getDir(), odefer.gainCurPos())) {
				anemys.add(odefer);
			}
		}

		return anemys;
	}

	//获取攻击个数
	private int getSkillAttackCount(SkillVisual sv) {
		if (sv == null) {
			return 0;
		}

		return sv.getMaxAttack();

	}

	//获取攻击范围
	private float getSkillAttackDis(SkillVisual sv) {
		if (sv == null) {
			return 1f;
		}

		if (sv.getAttackDis() < 1){
			return sv.getMoveDis();
		}
		return sv.getAttackDis();
	}

	//获取怪物攻击技能
	private Skill getCanUseSkill(Robot robot) {

		if (robot.getCurSlowSkill() != null) {

			Skill skill = robot.getCurSlowSkill();
			//引导次数已完成
			if (skill.getSlows().isEmpty()) {
				robot.setCurSlowSkill(null);
				return null;
			}

			if (skill.getStart() + skill.getSlows().get(0).getFpsTime() > TimeUtils.Time()) {
				return null;
			}
			return skill;
		}

		if (Manager.cooldownManager.isCooldowning(robot, CooldownTypes.MonsterAttackCD, null)) {
			return null;
		}

		if (robot.getSkills().isEmpty()) {
			robot.clearHatred();
			return null;
		}

		if (robot.getUseSkill() != null) {
			return robot.getUseSkill();
		}

		List<Skill> readys = robot.getReadyLists();
		if (!readys.isEmpty()) {
			robot.setUseSkill(readys.remove(0));
			return robot.getUseSkill();
		}
		for (Skill skill : robot.getSkills().values()) {

			if (Manager.cooldownManager.isCooldowning(robot, CooldownTypes.MonsterSkill, String.valueOf(skill.getSkillId()))) {
				continue;
			}
			Cfg_Skill_Bean config = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getSkillId());
			if (config == null) {
				continue;
			}
			if (config.getType() != SkillDefine.SkillType_Active) {
				continue;
			}
			readys.add(skill);
		}
		if (readys.isEmpty()) {
			return null;
		}

		Skill skill = readys.remove(0);

		robot.setUseSkill(skill);

		return skill;
	}

	private boolean OnBeAttackMoveAi(Robot robot) {
		if (robot.getAmbs().isEmpty()) {
			return getCurHitType(robot) != SkillDefine.SkillAttackMoveType_None;
		}
		long curTime = TimeUtils.Time();
		List<AttackMoveBehavior> ambs = new ArrayList<>(robot.getAmbs());
		for (AttackMoveBehavior amb : ambs) {
			//时间错乱的抛弃了
			if (amb.getEnd() > curTime + 60000) {
				robot.getAmbs().remove(amb);
				logger.error("时间过了：" + amb.getTarget());
				continue;
			}

			if (amb.getStart() < curTime) {
				//LOGGER.error("取消行为时间=" + TimeUtils.Time() + "下一个开始时间=" + amb.getStart());
				robot.getAmbs().remove(amb);

				//打断怪物的引导技能
				if (robot.getCurSlowSkill() != null) {
					robot.setCurSlowSkill(null);
				}

				robot.addFightState(FightEnum.SkillFreeze);
				BehaviorManager.CancelBehaviorByType(robot, BehaviorType.AttackMove);
				if (amb.getHitType() == SkillDefine.SkillAttackMoveType_Catch) {
					amb.setSpeed(Utils.getDistance(robot.gainCurPos(), amb.getTarget()) / (amb.getEnd() - amb.getStart()) * 1000);
				}
				BehaviorManager.InsertBehavior(robot, amb);
			}

		}
		return true;
	}

	//获取正在执行的受击事件
	private int getCurHitType(Fighter fighter) {
		AttackMoveBehavior move = (AttackMoveBehavior) BehaviorManager.GetBehavior((Entity) fighter, BehaviorType.AttackMove);
		if (move == null) {
			return SkillDefine.SkillAttackMoveType_None;
		}
		return move.getHitType();
	}

	//竞技场搜索敌人
	public void searchEnemyJJC(Robot robot, MapObject map) {
		for (Player player : map.getPlayers().values()) {
			if (!EntityState.Dead.compare(player.getState())) {
				robot.addHatred(player, 0);
				Pet pet = Manager.petManager.getBattlePet(player);
				if (pet != null) {
					robot.addHatred(pet, 0);
				}
			}
		}
	}

	//搜索怪物
	public void searchMonster(Robot robot, MapObject map) {
		for (Monster monster : map.getMonsters().values()) {
			if (!EntityState.Dead.compare(monster.getState())) {
				robot.addHatred(monster, 0);
			}
		}
	}

	private void OnFightStateAi(Robot robot) {
		if (robot.getFightStates().isEmpty()) {
			return;
		}
		long curTime = TimeUtils.Time();
		Iterator<FightState> iter = robot.getFightStates().iterator();
		while (iter.hasNext()) {
			FightState state = iter.next();
			if (state.getOverTime() < curTime) {
				iter.remove();
				state.romove(robot);
				// logger.info("结束状态 state=" + state + robot);
			}
		}
	}

	private void OnMagicAi(Robot robot) {
		long curTime = TimeUtils.Time();
		MapObject map = Manager.mapManager.getMap(robot.gainMapId());

		if (map == null) {
			return;
		}

		if (robot.getMagics().isEmpty()) {
			return;
		}
		//开始攻击
		IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);

		Iterator<Long> iter = robot.getMagics().iterator();
		while (iter.hasNext()) {
			long magicId = iter.next();
			SkillMagic magic = map.getMagic(magicId);
			if (magic == null) {
				iter.remove();
				continue;
			}

			if (curTime < magic.getStart()) {
				continue;
			}
			//开始检测攻击事件
			if (magic.getSerials().isEmpty()) {
				Manager.mapManager.manager().onQuitMap(map, magic, true);
				continue;
			}

			SkillVisual sv = SkillEventContainer.getInstance().GetSkillVisualBySV(magic.getMagicIndex());

			Iterator<Integer> iterator = magic.getSerials().iterator();

			Skill skill = magic.getSkillId() == 0 ? magic.getSkillinfo() : is.getFightSkill(robot, map, magic.getSkillId());
			int i = -1;
			while (iterator.hasNext()) {
				i += 1;
				int serialId = iterator.next();
				SkillEvent se = sv.getEvents().get(serialId);
				if (se instanceof PlayHitEvent) {
					PlayHitEvent event = (PlayHitEvent) se;
					if (onPlayHitEvent(curTime, magic, event, is, robot, skill)) {
						iterator.remove();
					}
					continue;
				}

				if (se instanceof PlaySimpleSkillObjectEvent) {
					PlaySimpleSkillObjectEvent psso = (PlaySimpleSkillObjectEvent) se;
					long beginTime = (long) (i * psso.getHitInterval() * 1000 * SkillVisual.OneFrameTime);
					if (onSimpleSkillObjectEvent(curTime, beginTime, magic, psso, is, robot, skill)) {
						iterator.remove();
					}
				}
			}
		}
	}

	private boolean onPlayHitEvent(long curTime, SkillMagic magic, PlayHitEvent event, IFightManagerScript is, Robot player, Skill skill) {
		if (curTime < magic.getStart() + event.getFpsTime()) {
			return false;
		}
		//开始攻击
		List<Fighter> targets = getMagicTargets(player, magic, event.getFindInfo());
		if (magic.getActionParam().size() > 0) {
			is.doAction(player, magic.getActionParam(), targets);
		}
		is.attack(magic, player, skill, event, targets);
		return true;
	}

	private boolean onSimpleSkillObjectEvent(long curTime, long nextFpsTime, SkillMagic magic, PlaySimpleSkillObjectEvent event, IFightManagerScript is, Robot player, Skill skill) {
		if (curTime < magic.getStart() + nextFpsTime) {
			return false;
		}
		//开始攻击
		List<Fighter> targets = getMagicTargets(player, magic, event.getFindInfo());
		if (magic.getActionParam().size() > 0) {
			is.doAction(player, magic.getActionParam(), targets);
		}
		is.attack(magic, player, skill, event, targets);
		return true;
	}

	//获取攻击目标
	public List<Fighter> getMagicTargets(Robot robot, SkillMagic magic, FindTargetInfo find) {

		List<Fighter> fighter = new ArrayList<>();

		List<Hatred> hats = new ArrayList<>(robot.getHatreds());

		for (Hatred hat : hats) {
			if (fighter.size() > find.getMaxTargetCount()) {
				break;
			}
			Fighter defer = hat.getTarget();
			if (defer.isDie()) {
				robot.removeHatred(hat);
				continue;
			}
			float dis = Utils.getDistance(magic.gainCurPos(), defer.gainCurPos());
			if (dis <= find.getAttackDis()) {
				fighter.add(defer);
			}
		}

		return fighter;
	}

	//获取主目标
	private Hatred getCurMainTarget(Robot monster) {
		if (monster.getHatreds().isEmpty()) {
			return null;
		}
		return monster.getHatreds().get(0);
	}

	private Hatred getCloseMainTarget( Robot monster){
		if (monster.getHatreds().isEmpty()) {
			return null;
		}
		Hatred minHatred =   monster.getHatreds().get(0);
		float mindistance =  Utils.getDistance(monster.gainCurPos(), minHatred.getTarget().gainCurPos());
		for (Hatred hatred : monster.getHatreds()){
			float distance =  Utils.getDistance(monster.gainCurPos(), hatred.getTarget().gainCurPos());
			if (distance < mindistance){
				minHatred = hatred;
				mindistance = distance;
			}
		}
		return minHatred;
	}

	private void OnBuffAi(Robot robot) {

		if (robot.getBuffs().isEmpty()) {
			return;
		}
		Manager.buffManager.deal().tick(robot);
	}

	private void OnPetAi(Robot robot) {

		if (robot.isDie()) {
			return;
		}

		Pet pet = robot.getPet();
		if (pet == null) {
			return;
		}
		MapObject map = Manager.mapManager.getMap(robot.gainMapId());
		if (map == null) {
			return;
		}

		Hatred curMainTarget = getCurMainTarget(robot);

		//无目标
		if (curMainTarget == null) {
			return;
		}

		Skill canUseSkill = getCanUseSkill(robot, pet);
		if (canUseSkill == null) {
			return;
		}
		logger.info(pet.getId() + "使用技能：" + canUseSkill.getSkillId());

		Fighter defer = curMainTarget.getTarget();

		Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(canUseSkill.getSkillId());
		SkillVisual sv = Manager.skilleventManager.GetSkillVisualBySV(skillBean.getVisualDef());

		//TODO 检测追击
		if (EntityState.Dead.compare(defer.getState())) {
//            LOGGER.info("[" + defer + "]追击目标死亡 目标=" + defer);
			robot.removeHatred(defer);
			return;
		}
		if (!defer.isEqualMap(robot)) {
			robot.removeHatred(defer);
			return;
		}
		float attackDis = getSkillAttackDis(sv);
		float dis = Utils.getDistance(pet.gainCurPos(), defer.gainCurPos());
		//检测攻击距离
		if (dis > attackDis + pet.getRadius() + defer.getRadius()) {

			//TODO 追击
			if (Manager.cooldownManager.isCooldowning(pet, CooldownTypes.MonsterPursue, null)) {
				return;
			}

			Position pursuePos = defer.getPursuePos(pet, attackDis);

			List<Position> roads = new ArrayList<>();
			roads.add(pet.gainCurPos());
			roads.add(pursuePos);
			pet.setRoads(roads);

			if (!EntityState.Move.compare(pet.getState())) {
				pet.addState(EntityState.Move);
				BehaviorManager.InsertOnlyBehavior(robot, new MoveBehavior(pet));

			}
			Manager.mapManager.sendMoveMessage(pet, roads);

			Manager.cooldownManager.addCooldown(pet, CooldownTypes.MonsterPursue, null, 333);
			return;
		}

		if (EntityState.Move.compare(pet.getState())) {
			return;
		}

		//TODO 释放技能
		List<Fighter> anemys = getEnemies(robot, pet, defer, canUseSkill, sv);
		Position dir = Utils.getDir(pet.gainCurPos(), defer.gainCurPos());
		pet.setDir(dir);
		IFightManagerScript is = (IFightManagerScript) Manager.scriptManager.GetScriptClass(ScriptEnum.FightManagerBaseScript);
		is.attack(pet, canUseSkill, anemys, null);
		pet.setUseSkill(null);
	}

	private Skill getCanUseSkill(Robot robot, Pet pet) {
		if (pet.getSkills().isEmpty()) {
			return null;
		}
		if (pet.getCurSlowSkill() != null) {
			Skill skill = pet.getCurSlowSkill();
			if (skill.getSlows().isEmpty()) {
				pet.setCurSlowSkill(null);
				return null;
			}
			if (skill.getStart() + skill.getSlows().get(0).getFpsTime() > TimeUtils.Time()) {
				return null;
			}
			return skill;
		}

		if (Manager.cooldownManager.isCooldowning(pet, CooldownTypes.Player_Pet_CD_Skill, null)) {
			return null;
		}

		if (pet.getUseSkill() != null) {
			if (Manager.cooldownManager.isCooldowning(pet, CooldownTypes.Player_Pet_Skill, String.valueOf(pet.getUseSkill().getSkillId()))) {
				pet.setUseSkill(null);
				return null;
			}
			return pet.getUseSkill();
		}

		List<Skill> readys = new ArrayList<>();
		for (Skill skill : pet.getSkills().values()) {
			if (Manager.cooldownManager.isCooldowning(pet, CooldownTypes.Player_Pet_Skill, String.valueOf(skill.getSkillId()))) {
				continue;
			}
			Cfg_Skill_Bean config = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getSkillId());
			if (config.getType() != SkillDefine.SkillType_Active) {
				continue;
			}
			readys.add(skill);
		}
		if (readys.isEmpty()) {
			return null;
		}
		Skill skill = RandomUtils.randomItem(readys);
		pet.setUseSkill(skill);

		return skill;
	}

    private Robot OnMakeRobotHelp(long roleId,float Percent,Long liveTime)
    {
        Player player =   initPlayer( roleId);
        if (player == null) return null;

        Robot robot = OnMake(player);
        if(robot == null) return  null;
        robot.setLiveTime(liveTime);
        OnResetRobotAttributePercent( robot, Percent);
        OnCalcRobotAttribute( robot);
        return robot;
    }

	/**
	 * 召唤机器人助战
	 * @param player
	 * @param roleIDs
	 * @param liveTime
	 * @param attributePercent
	 * @param buffId
	 */
	@Override
	public void OnRobotHelpBattle(Player player, List<Long> roleIDs,Long liveTime,float attributePercent, int buffId) {
		for (long roleID : roleIDs) {
			Robot robot = OnMakeRobotHelp( roleID,attributePercent,liveTime);
			if (robot == null) continue;

			Manager.buffManager.deal().onAddBuff(robot, robot, buffId);

			if (player.playerCrossData.toFightId > 0 && player.playerCrossData.toFightSid > 0) {
				robot.setOwnerId(player.getId());
				String robotdata = VersionUpdateUtil.dataSave(JsonUtils.toJSONString(robot));
				String att = JsonUtils.toJSONString(robot.getPlayerCalculators());
				CrossFightMessage.G2FSynRobotInfoToHelpBattle.Builder msg = CrossFightMessage.G2FSynRobotInfoToHelpBattle.newBuilder();
				msg.setRoleId(player.getId());
				msg.setRobotData(robotdata);
				msg.setAttlist(att);
				ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), CrossFightMessage.G2FSynRobotInfoToHelpBattle.MsgID.eMsgID_VALUE, msg.build().toByteArray());
			} else {
				MapObject map = Manager.mapManager.getMap(player.gainMapId());
				if (map != null) {
					robot.setAi(RobotAi.Help);
					MapGpsUtil.CopyGPS(player.getCurGps(), robot.getCurGps());
					// 随机坐标
					Position randomPos = Utils.getRandomPos(map, robot.getCurGps().getPos(), 5);
					robot.getCurGps().setPos(randomPos);
					robot.setCamp(player.getCamp());
					robot.reset();
					robot.setOwnerId(player.getId());
					robot.onHpChange(robot);
					Manager.mapManager.manager().onEnterMap(robot);
				}
			}
		}
	}
}
