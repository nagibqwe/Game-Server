package common.control;

import com.data.*;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.commercialize.inter.IFCCharge;
import com.game.control.manager.ControlManager;
import com.game.control.script.IControlScript;
import com.game.control.structs.FuncOpenData;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.BooleanDay;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.equip.struct.EquipPart;
import com.game.friend.struct.PlayerRelation;
import com.game.godbook.struct.Amulet;
import com.game.guild.structs.GuildSysConfig;
import com.game.home.structs.HomeTask;
import com.game.manager.Manager;
import com.game.marriage.struct.Marriage;
import com.game.nature.structs.Drug;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.skill.structs.MentalSkill;
import com.game.soulbeast.structs.SoulBeast;
import com.game.statestifle.manager.StateStifleManager;
import com.game.statestifle.structs.PlayerSateStifleData;
import com.game.statestifle.structs.SoulSpiritInfo;
import com.game.structs.GlobalType;
import com.game.task.command.TaskCommand;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import game.core.util.TimeUtils;
import game.message.BackendMessage;
import game.message.CommercializeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lw
 * @doc 控制开关实现
 */
public class ControlScript implements IControlScript {

    private static final Logger logger = LogManager.getLogger("ControlScript");

    @Override
    public int getId() {
        return ScriptEnum.ControlBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void load() {
        Manager.controlManager.getCondFunc().clear();
        boolean save = false;
        Set<Integer> ar;
        int type;
        for (Cfg_FunctionStart_Bean bean : CfgManager.getCfg_FunctionStart_Container().getValuees()) {
            if (!ServerParamUtil.funcs.containsKey(bean.getFunction_id())) {
                ServerParamUtil.funcs.put(bean.getFunction_id(), 1);
                save = true;
            }
            for (ReadArray<Integer> ppt : bean.getStart_variables().getValuees()) {
                type = ppt.get(0);
                if (Manager.controlManager.getCondFunc().containsKey(type)) {
                    ar = Manager.controlManager.getCondFunc().get(type);
                } else {
                    ar = new HashSet<>();
                    Manager.controlManager.getCondFunc().put(type, ar);
                }
                ar.add(bean.getFunction_id());
            }
        }
        if (save) {
            ServerParamUtil.saveFuncs();
        }
    }

    @Override
    public void reload() {
        load();
        syncFunctionToAll();
    }

    @Override
    public void login(Player player) {
        BackendMessage.ResFuncOpenList.Builder resMsg = BackendMessage.ResFuncOpenList.newBuilder();
        for (Cfg_FunctionStart_Bean bean : CfgManager.getCfg_FunctionStart_Container().getValuees()) {
            boolean isOpen = isOpenFunction(player, bean.getFunction_id());
            resMsg.addFuncOpenList(builder(player, bean.getFunction_id(), isOpen));
            Boolean status = player.functionState.get(bean.getFunction_id());
            if ((status == null || !status) && isOpen) {
                functionStartEvent(player, bean.getFunction_id());
            }
            player.functionState.put(bean.getFunction_id(), isOpen);
            if (isOpen && bean.getFunction_id() == FunctionStart.NatureWing && player.getWingStatus() != 1) {
                //老玩家翅膀重新初始化
                Manager.natureManager.deal().initPlayerWing(player);
            }
        }
        MessageUtils.send_to_player(player, BackendMessage.ResFuncOpenList.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    @Override
    public boolean isBackOpen(int funcId) {
        if (!ServerParamUtil.funcs.containsKey(funcId)) {
            return true;
        }
        return ServerParamUtil.funcs.get(funcId) == ControlManager.OPEN_FUNCTION;
    }

    @Override
    public List<FuncOpenData> getBackFuncList() {
        List<FuncOpenData> list = new ArrayList<>();
        int funcId;
        FuncOpenData info;
        for (Cfg_FunctionStart_Bean bean : CfgManager.getCfg_FunctionStart_Container().getValuees()) {
            funcId = bean.getFunction_id();
            info = new FuncOpenData();
            info.setId(funcId);
            info.setOpenState(ServerParamUtil.funcs.getOrDefault(funcId, ControlManager.OPEN_FUNCTION));
            list.add(info);
        }
        return list;
    }

    @Override
    public void changeBackFunc(int funcId, int funcState) {
        boolean isChange = false;
        if (!ServerParamUtil.funcs.containsKey(funcId)) {
            ServerParamUtil.funcs.put(funcId, funcState);
            isChange = true;
        } else if (funcState != ServerParamUtil.funcs.get(funcId)) {
            ServerParamUtil.funcs.put(funcId, funcState);
            isChange = true;
        }

        if (isChange) {
            syncFunctionToAll();
            checkFatherFunClose(funcId);
        }
    }

    @Override
    public boolean changeBackFuncs(List<FuncOpenData> list) {
        boolean isChange = false;
        for (FuncOpenData info : list) {
            int funcId = info.getId();
            int funcState = info.getOpenState();
            if (!ServerParamUtil.funcs.containsKey(funcId)) {
                ServerParamUtil.funcs.put(funcId, funcState);
                isChange = true;
            }
            if (funcState != ServerParamUtil.funcs.get(funcId)) {
                ServerParamUtil.funcs.put(funcId, funcState);
                isChange = true;
            } else {
                logger.error("不该请求过来哦，后台应该check筛掉！funcId=" + funcId);
            }
        }

        if (isChange) {
            syncFunctionToAll();
            return true;
        }
        return false;
    }

    @Override
    public boolean isOpenFunction(Player player, int funcId) {
        if (!isBackOpen(funcId)) {
            return false;
        }

        if (FunctionStart.ServeCrazy == funcId || FunctionStart.GrowthWay == funcId) {
            if (TimeUtils.getOpenServerDay() >= Global.New_server_growup_time) {
                return false;
            }
        }

        if (FunctionStart.ServerActive == funcId) {
            if (TimeUtils.getOpenServerDay() >= Global.New_server_active_exchange) {
                return false;
            }
        }

        if (FunctionStart.ZongPaiStar == funcId) {
            if (TimeUtils.getOpenServerDay() >= Global.New_server_active_time) {
                return false;
            }
        }

        if (FunctionStart.JingJieReach == funcId) {
            if (TimeUtils.getOpenServerDay() >= Global.New_server_active_time) {
                return false;
            }
        }

        if (FunctionStart.PerfectQingYuan == funcId) {
            if (TimeUtils.getOpenServerDay() >= Global.New_server_active_time) {
                return false;
            }
        }

        if (FunctionStart.ZongPaiFight == funcId) {
            if (TimeUtils.getOpenServerDay() >= Global.New_server_active_time) {
                return false;
            }
        }

        if (FunctionStart.FirstCharge == funcId || FunctionStart.ReCharge == funcId) {
            IFCCharge script = (IFCCharge) Manager.commercializeManager.getScript(CommercializeMessage.Commercialize.FCCharge);
            if (script == null || !script.checkFCChargeFunc(player, funcId))
                return false;
        }


        if (FunctionStart.VipInvationNormal == funcId) {
            long rechargeGold = Manager.countManager.getVariant(player, VariantType.RechargeGold);
            if (rechargeGold < Global.Special_vip_recharge.get(0)) {
                return false;
            }
        }

        if (FunctionStart.VipInvationZunGui == funcId) {
            long rechargeGold = Manager.countManager.getVariant(player, VariantType.RechargeGold);
            if (rechargeGold < Global.Special_vip_recharge.get(1)) {
                return false;
            }
        }

        Cfg_FunctionStart_Bean bean = CfgManager.getCfg_FunctionStart_Container().getValueByKey(funcId);

        if (bean == null || bean.getStart_variables() == null) {
            return true;
        }
        //判断狂欢周的功能
        if (bean.getParent_id() == FunctionStart.WeekCrazy || funcId == FunctionStart.WeekCrazy) {
            return Manager.crazyWeekManager.deal().funcIsOpen(funcId) && checkFuncProgress(player, bean.getStart_variables());
        }

        if(bean.getParent_id() == FunctionStart.LoversFight || funcId == FunctionStart.LoversFight){
            if(FunctionStart.LoversPickFight == funcId || FunctionStart.LoversRankRewards == funcId
            || FunctionStart.LoversShop == funcId){
                return checkFuncProgress(player, bean.getStart_variables());
            }
            return Manager.couplefightManager.getScript().funcIsOpen(funcId) && checkFuncProgress(player, bean.getStart_variables());
        }

        return checkFuncProgress(player, bean.getStart_variables());
    }

    /**
     * 检查某一个条件满足就true
     *
     * @param player
     * @param params
     * @return
     */
    @Override
    public boolean checkFuncProgressSomeone(Player player, ReadIntegerArrayEs params) {
        if (params.isEmpty()) {
            return true;
        }
        for (ReadArray<Integer> param : params.getValuees()) {
            if (checkFuncProgress(player, param)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkFuncProgress(Player player, ReadIntegerArrayEs params) {
        for (ReadArray<Integer> param : params.getValuees()) {
            if (!checkFuncProgress(player, param)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkFuncProgress(Player player, ReadArray<Integer> param) {
        if (param.size() == 0) {
            return true;
        } else if (param.size() == 1) {
            return getFuncProgress(player, param) == 1;
        } else {
            return getFuncProgress(player, param) >= param.get(param.size() - 1);
        }
    }

    /**
     * dfs
     *
     * @param player
     * @param param
     * @return
     */
    @Override
    public int getFuncProgress(Player player, ReadArray<Integer> param) {
        if (param == null || param.size() == 0) {
            return 0;
        }
        if (param.get(0) == FunctionVariable.PlayerLevel) {
            return player.getLevel();
        } else if (param.get(0) == FunctionVariable.PlayerTaskID) {
            int result = player.overMainTask(param.get(1)) ? param.get(1) : 0;
            if (result == 0) {
                result = player.getBranchOverList().contains(param.get(1)) ? param.get(1) : 0;
            }
            if (result == 0) {
                HomeTask task = player.getHome().getHomeTask().get(param.get(1));
                result = task == null ? 0 : task.getState() == 2 ? param.get(1) : 0;
            }
            return result;
        } else if (param.get(0) == FunctionVariable.PlayerPower) {
            return player.getFightPoint() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) player.getFightPoint();
        } else if (param.get(0) == FunctionVariable.SkillCountLevel) {
            return Manager.skillManager.deal().getSkillAllLevel(player);
        } else if (param.get(0) == FunctionVariable.ArenaChallenge) {
            return player.getDailyActiveData().getDailyProgress().getOrDefault(DailyActiveDefine.JJC.getValue(), 0);
        } else if (param.get(0) == FunctionVariable.KillBOSS) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.CumulativeLogin) {
            return player.getAccumOnlineDays();
        } else if (param.get(0) == FunctionVariable.ConsumeBindDiamonds) {
            return (int) Manager.countManager.getVariant(player, VariantType.ConsumeBindGold);
        } else if (param.get(0) == FunctionVariable.ConsumeDiamonds) {
            return (int) Manager.countManager.getVariant(player, VariantType.ConsumeGold);
        } else if (param.get(0) == FunctionVariable.ChangeJob) {
            return player.getXsGrade();
        } else if (param.get(0) == FunctionVariable.Joinguild) {
            return player.getGuildId() > 0 ? 1 : 0;
        } else if (param.get(0) == FunctionVariable.CurDayActiveValue) {
            return player.getDailyActiveData().getActiveNum();
        } else if (param.get(0) == FunctionVariable.YZZD) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.KillSpecialBoss) {
            return (int) Manager.countManager.getCount(player, BaseCountType.KillBoss_Times, param.get(1));
        } else if (param.get(0) == FunctionVariable.WornEquip) {
            if (param.size() < 3) {
                return 0;
            }
            return Manager.equipManager.deal().isEquipQuality(player, param.get(1), param.get(2), param.get(3));
        } else if (param.get(0) == FunctionVariable.Getamulet) {
            Amulet amulet = player.getGodBookInfo().get(param.get(1));
            return (amulet != null && amulet.isActivated()) ? 1 : 0;
        } else if (param.get(0) == FunctionVariable.OrderEquip) {
            if (param.get(1) >= player.getEquipParts().size()) {
                return 0;
            }
            EquipPart part = player.getEquipParts().get(param.get(1));
            if (part == null || part.getEquip() == null) {
                return 0;
            }
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getEquip().getItemModelId());
            return bean == null ? 0 : bean.getGrade();
        } else if (param.get(0) == FunctionVariable.AchievementPoints) {
            return (int) Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.Achievement);
        } else if (param.get(0) == FunctionVariable.StateLevel) {
            return player.getStateVip().getLv();
        } else if (param.get(0) == FunctionVariable.EquipStrengthenLevelMax) {
            return player.getEquipParts().stream().map(EquipPart::getLevel).reduce(0, Integer::sum);
        } else if (param.get(0) == FunctionVariable.EquipWashingNum) {
            return Manager.equipManager.deal().equipWashNum(player);
        } else if (param.get(0) == FunctionVariable.GemLevelMax) {
            return player.getEquipParts().stream()
                    .map(n -> n.getGemInfo().getGemIds().stream()
                            .filter(m -> m > 0).map(m -> m % 100).reduce(0, Integer::sum))
                    .reduce(0, Integer::sum);
        } else if (param.get(0) == FunctionVariable.EquipSuitNum) {
            return Manager.equipManager.deal().gainSuitNum(player, param.get(1));
        } else if (param.get(0) == FunctionVariable.WingsActivateID) {
            return player.getWing().getHuaxins().containsKey(param.get(1)) ? 1 : 0;
        } else if (param.get(0) == FunctionVariable.MagicLevel) {
            return player.getMagic().getCurrentId();
        } else if (param.get(0) == FunctionVariable.TalismanLevel) {
            return player.getTalisman().getCurrentId();
        } else if (param.get(0) == FunctionVariable.MeditationTime) {
            return (int) Manager.countManager.getVariant(player, VariantType.AccuMeditation) / 3600;
        } else if (param.get(0) == FunctionVariable.OpenBackpackNum) {
            return player.getBagCellsNum() - Global.Born_Bag_Num.get(0);
        } else if (param.get(0) == FunctionVariable.ShiHaiLevel) {
            return player.getShiHaiData().getCfgId();
        } else if (param.get(0) == FunctionVariable.RiChangJingYanNum) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.RiChangYinBiNum) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.GuildRiChangNum) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.GuildZhouChangNum) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.DaNengYiFuNum) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.WanYaoJuanNum) {
            return player.getSingleTowerData().getCurLayer() - 1;
        } else if (param.get(0) == FunctionVariable.Boundgold) {
            return (int) Manager.countManager.getVariant(player, VariantType.ConsumeBindCoin)
                    + Manager.currencyManager.manager().getCurrencyIntNum(player, ItemCoinType.BindMoney);
        } else if (param.get(0) == FunctionVariable.WingNum) {
            return player.getWing().getCurrentId();
        } else if (param.get(0) == FunctionVariable.HorseNum) {
            return player.getHorse().getHorseSteps();
        } else if (param.get(0) == FunctionVariable.WeaponNum) {
            return player.getWeapon().getCurrentId();
        } else if (param.get(0) == FunctionVariable.ArenaNum) {
            return (int) Manager.countManager.getVariant(player, VariantType.JJCAccumulationCount);
        } else if (param.get(0) == FunctionVariable.ForbiddenAreaNum) {
            return 0;
//            return (int) Manager.countManager.getCount(player, BaseCountType.SYNTHETIC_EQUIP, param.get(1));

        } else if (param.get(0) == FunctionVariable.GhostBattlefieldNum) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.StateTask) {
            return (int) Manager.countManager.getCount(player, BaseCountType.StateTask, player.getStateVip().getLv());
        } else if (param.get(0) == FunctionVariable.ChangeJobFateStar) {
            return 0;
        } else if (param.get(0) == FunctionVariable.ChangeJobclone) {
            return player.getOverGenderTaskIds().contains(param.get(1)) ? 1 : 0;
        } else if (param.get(0) == FunctionVariable.ChangeJobDragonSoul_1) {
            return changeJobDragonSoul(player, 5, 1);
        } else if (param.get(0) == FunctionVariable.ChangeJobDragonSoul_2) {
            return changeJobDragonSoul(player, 5, 2);
        } else if (param.get(0) == FunctionVariable.ChangeJobDragonSoul_3) {
            return changeJobDragonSoul(player, 5, 3);
        } else if (param.get(0) == FunctionVariable.GuildCreate) {
            if (!player.isHaveGuild()) {
                return 0;
            }
            return Manager.guildsManager.getGuildById(player.getGuildId()).getChairMan().getId() == player.getId() ? 1 : 0;
        } else if (param.get(0) == FunctionVariable.GuildOffice) {
            if (!player.isHaveGuild()) {
                return 0;
            }
            return Manager.guildsManager.getGuildById(player.getGuildId()).gainRankCount(GuildSysConfig.TYPE_VICE_MASTER);
        } else if (param.get(0) == FunctionVariable.GuildMembNub) {
            if (!player.isHaveGuild()) {
                return 0;
            }
            return Manager.guildsManager.getGuildById(player.getGuildId()).getMembers().size();
        } else if (param.get(0) == FunctionVariable.GuildLevel) {
            if (!player.isHaveGuild()) {
                return 0;
            }
            return Manager.guildsManager.getGuildById(player.getGuildId()).getConstructions().get(GuildSysConfig.TYPE_BASE);
        } else if (param.get(0) == FunctionVariable.MarrageLevel) {
            Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
            return marriage == null ? 0 : 1;
        } else if (param.get(0) == FunctionVariable.ComposeEquip) {
            //long key = param.get(1) * 10000 + param.get(2) * 100 + param.get(3);
            //return (int) Manager.countManager.getCount(player, BaseCountType.SyntheticEquipStrong, key);
            return Manager.equipManager.deal().getComposeEquip(player, param.get(1), param.get(2), param.get(3));
        } else if (param.get(0) == FunctionVariable.PetLevel) {
            return player.getActivePet().getLevel();
        } else if (param.get(0) == FunctionVariable.PetNum) {
            return (int) Manager.countManager.getVariant(player, VariantType.PetUpDegree);
        } else if (param.get(0) == FunctionVariable.PetSoul) {
            int all = 0;
            for (Drug num : player.getActivePet().getNature().getDrugs().values()) {
                all += num.getUseNumber();
            }
            return all;
        } else if (param.get(0) == FunctionVariable.HorseSoul) {
            int all = 0;
            for (Drug num : player.getHorse().getNature().getDrugs().values()) {
                all += num.getUseNumber();
            }
            return all;
        } else if (param.get(0) == FunctionVariable.LandGiftReceive) {
            return (int) Manager.countManager.getVariant(player, VariantType.LoginGift);
        } else if (param.get(0) == FunctionVariable.ServerStoreDonate) {
            return (int) Manager.countManager.getVariant(player, VariantType.ServerStore);
        } else if (param.get(0) == FunctionVariable.Special_Weapon_level) {
            return 0;
        } else if (param.get(0) == FunctionVariable.PetDevour) {
            return (int) Manager.countManager.getVariant(player, VariantType.PetEat);
        } else if (param.get(0) == FunctionVariable.ImmortalSou) {
            return Manager.immortalSoulManager.getAllOnEquipLevel(player);
        } else if (param.get(0) == FunctionVariable.DanengYifuStar) {
            //TODO gsj:删除此条件
            return 0;
//            return player.getStarCopyData().getStarInfo().values().stream().reduce(0, Integer::sum);
        } else if (param.get(0) == FunctionVariable.MagicWeapon) {
            int level = player.getStifleData().getLevel();
            int star = player.getStifleData().getStar();
            return StateStifleManager.getInstance().deal().getConfigIdByLevelAndStar(level, star);
        } else if (param.get(0) == FunctionVariable.SceneBoss) {
            return (int) Manager.countManager.getCount(player, BaseCountType.MapKillMonsterNum, param.get(1));
        } else if (param.get(0) == FunctionVariable.GateOfHeaven) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.CopyOfHeartDevi) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.Five_lineCopy) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.CopiesOfExperience) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.EquipSmelt) {
            return (int) Manager.countManager.getVariant(player, VariantType.RecycleNum);
        } else if (param.get(0) == FunctionVariable.SoulBeastsNum) {
            List<SoulBeast> soulBeasts = Utils.find(player.getSoulBeastInfo().getSoulBeasts().values(), o -> o.isWork());
            return soulBeasts.size();
        } else if (param.get(0) == FunctionVariable.SoulBeastsID) {
            List<SoulBeast> soulBeasts = Utils.find(player.getSoulBeastInfo().getSoulBeasts().values(), o -> o.isWork());
            for (SoulBeast soulBeast : soulBeasts) {
                int beastId = soulBeast.getBeastId();
                if (beastId >= param.get(1)) {
                    return 1;
                }
            }
            return 0;
        } else if (param.get(0) == FunctionVariable.MarryNum) {
            return (int) Manager.countManager.getVariant(player, VariantType.MarryNum);
        } else if (param.get(0) == FunctionVariable.Marry_intimacy) {
            //结婚对象
            PlayerWorldInfo target = Manager.marriageManager.getMarryTarget(player);
            if (target != null) {
                int intimacy = Manager.friendManager.getFriendIntimacy(player, target.getRoleid());
                return intimacy;
            }
            return 0;
        } else if (param.get(0) == FunctionVariable.Marry_house) {
            return 0;
        } else if (param.get(0) == FunctionVariable.Marry_blessLevel) {
            return 0;
        } else if (param.get(0) == FunctionVariable.Marry_boxNum) {
            return (int) Manager.countManager.getVariant(player, VariantType.MarryBoxNum);
        } else if (param.get(0) == FunctionVariable.Marry_childNum) {
            return player.getChilds().size();
        } else if (param.get(0) == FunctionVariable.State_stifleID) {
            return player.getStifleData().getNature().getHuaxins().get(param.get(1)) == null ? 0 : 1;
        } else if (param.get(0) == FunctionVariable.NatureWingID) {
            return player.getWing().getHuaxins().get(param.get(1)) == null ? 0 : 1;
        } else if (param.get(0) == FunctionVariable.NatureHorseID) {
            return player.getHorse().getNature().getHuaxins().get(param.get(1)) == null ? 0 : 1;
        } else if (param.get(0) == FunctionVariable.PetID) {
            return player.getActivePet().getPets().get(param.get(1)) == null ? 0 : 1;
        } else if (param.get(0) == FunctionVariable.GodWeaponLevelNum) {
            return 0;
        } else if (param.get(0) == FunctionVariable.AddFriends) {
            // return (int) Manager.countManager.getVariant(player, VariantType.AddFriendNum);
            //改为实时好友数量
            PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(player.getId());
            if (playerRelation != null) {
                return playerRelation.getFriends().size();
            }
            return 0;
        } else if (param.get(0) == FunctionVariable.TypeBoss) {
            return (int) Manager.countManager.getCount(player, BaseCountType.KillSpecBoss_Times, param.get(1));
        } else if (param.get(0) == FunctionVariable.ManaStoneShopBuy) {
            return (int) Manager.countManager.getCount(player, BaseCountType.PurShopItem_Times, param.get(1));
        } else if (param.get(0) == FunctionVariable.GuildShopBuy) {
            return (int) Manager.countManager.getCount(player, BaseCountType.PurShopItem_Times, param.get(1));
        } else if (param.get(0) == FunctionVariable.CurDayActiveValueCos) {
            return (int) Manager.countManager.getVariant(player, VariantType.DailyActivePointCost);
        } else if (param.get(0) == FunctionVariable.LingTiEquip) {
            return Manager.equipManager.deal().getSpiritNum(player, param.get(1), param.get(2), param.get(3));
        } else if (param.get(0) == FunctionVariable.New_sever_growup_wear_equip) {
            return Manager.equipManager.deal().getGrowup_wear_equip(player, param.get(1), param.get(2), param.get(3));
        } else if (param.get(0) == FunctionVariable.EXPPrayNum) {
            return (int) Manager.countManager.getVariant(player, VariantType.EXPPrayNum);
        } else if (param.get(0) == FunctionVariable.MoneyPrayNum) {
            return (int) Manager.countManager.getVariant(player, VariantType.MoneyPrayNum);
        } else if (param.get(0) == FunctionVariable.TrueRecharge) {
            return (int) Manager.countManager.getVariant(player, VariantType.RechargeMoney);
        } else if (param.get(0) == FunctionVariable.WorldQuestionNum) {
            return (int) Manager.countManager.getVariant(player, VariantType.WorldAnswerNum);
        } else if (param.get(0) == FunctionVariable.WorldSupportSeek) {
            return (int) Manager.countManager.getVariant(player, VariantType.WorldHelpNum);
        } else if (param.get(0) == FunctionVariable.WorldSupportHelp) {
            return (int) Manager.countManager.getVariant(player, VariantType.WorldBeHelpNum);
        } else if (param.get(0) == FunctionVariable.WelfareDailyGiftNum) {
            return (int) Manager.countManager.getCount(player, BaseCountType.WelfareDailyGiftNum, param.get(1));
        } else if (param.get(0) == FunctionVariable.WelfareCard) {
            return (int) Manager.countManager.getCount(player, BaseCountType.WelfareCard, param.get(1));
        } else if (param.get(0) == FunctionVariable.SkillLevelUp) {
            return (int) Manager.countManager.getVariant(player, VariantType.SkillUpNum);
        } else if (param.get(0) == FunctionVariable.RealmStifleTaskNum) {
            return (int) Manager.countManager.getVariant(player, VariantType.FaBaoTaskNum);
        } else if (param.get(0) == FunctionVariable.BaseLevel) {
            if (!player.isHaveGuild()) {
                return 0;
            }
            return Manager.guildsManager.getGuildById(player.getGuildId()).getConstructions().get(GuildSysConfig.TYPE_BASE);
        } else if (param.get(0) == FunctionVariable.ShopLevel) {
            if (!player.isHaveGuild()) {
                return 0;
            }
            return Manager.guildsManager.getGuildById(player.getGuildId()).getConstructions().get(GuildSysConfig.TYPE_SHOP);
        } else if (param.get(0) == FunctionVariable.StationLevel) {
            if (!player.isHaveGuild()) {
                return 0;
            }
            return Manager.guildsManager.getGuildById(player.getGuildId()).getConstructions().get(GuildSysConfig.TYPE_HOME);
        } else if (param.get(0) == FunctionVariable.XisuiAccomplished) {
            return player.getXsLevel();
        } else if (param.get(0) == FunctionVariable.HorseStarNum) {
            return player.getHorse().getNature().getCurrentId();
        } else if (param.get(0) == FunctionVariable.VipWeekValue) {
            return 0;
        } else if (param.get(0) == FunctionVariable.WornHolyEquip) {
            return Manager.holyEquipManager.deal().getSpiritNum(player, param.get(1), param.get(2), param.get(3));
        } else if (param.get(0) == FunctionVariable.EquipPositionQuality) {
            EquipPart part = player.getEquipParts().get(param.get(1));
            Equip equip = part == null ? null : part.getEquip();
            if (equip == null) {
                return 0;
            }
            Cfg_Equip_Bean equipBean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
            if (equipBean != null) {
                return equipBean.getQuality();
            }
            return 0;
        } else if (param.get(0) == FunctionVariable.RealmStifleTaskCompNum) {
            return (int) Manager.countManager.getVariant(player, VariantType.FaBaoTaskFinishNumWeek);
        } else if (param.get(0) == FunctionVariable.TreasurePopNum) {
            return (int) Manager.countManager.getCount(player, BaseCountType.PTotalTreasureTypeHuntTimes, param.get(1));
        } else if (param.get(0) == FunctionVariable.IntegralShopBuy) {
            return (int) Manager.countManager.getCount(player, BaseCountType.IntegralShopBuyItem, param.get(1));
        } else if (param.get(0) == FunctionVariable.LingtiSealRelief) {
            return player.getSpiritData().getCfgId();
        } else if (param.get(0) == FunctionVariable.StatestifleAddActivated) {
            SoulSpiritInfo sspi = player.getStifleData().getSpiritMap().get(param.get(1));
            if (sspi == null) {
                return 0;
            }
            return sspi.getId();
        } else if (param.get(0) == FunctionVariable.Occ_SkillAdvanced) {
            int baseId = param.get(1) * 100000 + player.getCareer() * 10000;
            MentalSkill mentalSkill = player.getMentalskills().get(baseId);
            if (mentalSkill == null) {
                return 0;
            }
            if (mentalSkill.getSkillID().get(param.get(2)) == null) {
                return 0;
            }
            return (mentalSkill.getSkillID().get(param.get(2)) % 100000) % (param.get(2) * 1000);
        } else if (param.get(0) == FunctionVariable.AuchtionSell) {
            long key = param.get(1) * 100000 + param.get(2) * 1000 + param.get(3);
            return (int) Manager.countManager.getCount(player, BaseCountType.AuctionPut, key);
        } else if (param.get(0) == FunctionVariable.AuchtionBuy) {
            long key = param.get(1) * 100000 + param.get(2) * 1000 + param.get(3);
            return (int) Manager.countManager.getCount(player, BaseCountType.AuctionBuy, key);
        } else if (param.get(0) == FunctionVariable.FirstRechargeReward) {
            return player.getFirstRechargeTime() > 0 ? 1 : 0;
        } else if (param.get(0) == FunctionVariable.BuyInvest) {
            return (int) Manager.countManager.getCount(player, BaseCountType.GrowthFundBuy, player.getGrowthFund().getGear());
        } else if (param.get(0) == FunctionVariable.EquipHolyPower) {
            return Manager.holyEquipManager.getFightPowerMap().get(player.getId()) == null ? 0 : Manager.holyEquipManager.getFightPowerMap().get(player.getId());
        } else if (param.get(0) == FunctionVariable.ComposeEquiponce) {
            return (int) Manager.countManager.getCount(player, BaseCountType.SyntheticEquipStrong, 0);
        } else if (param.get(0) == FunctionVariable.MailRecive) {
            return (int) Manager.countManager.getCount(player, BaseCountType.OneKeyMailRecv, 0);
        } else if (param.get(0) == FunctionVariable.GuildChat) {
            return (int) Manager.countManager.getVariant(player, VariantType.GuildChat);
        } else if (param.get(0) == FunctionVariable.GuildWar) {
            return (int) Manager.countManager.getVariant(player, VariantType.GuildWar);
        } else if (param.get(0) == FunctionVariable.EquipSuitLevel) {
            return Manager.equipManager.deal().gainSuitNum(player, param.get(1), param.get(2));
        } else if (param.get(0) == FunctionVariable.EquipHolyTypeWorn) {
            return Manager.holyEquipManager.deal().getHolyEquipNumForType(player, param.get(1));
        } else if (param.get(0) == FunctionVariable.GroupLeaderpreach) {
            return (int) Manager.countManager.getVariant(player, VariantType.GroupLeaderpreach);
        } else if (param.get(0) == FunctionVariable.CrossSceneBoss) {
            return (int) Manager.countManager.getCount(player, BaseCountType.MapKillMonsterNum, param.get(1));
        } else if (param.get(0) == FunctionVariable.GuildBossFinish) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.GuildCopySupport) {
            return (int) Manager.countManager.getCount(player, BaseCountType.GuildTaskMapSupport, 0);
        } else if (param.get(0) == FunctionVariable.GroupCopymap) {
            return (int) Manager.countManager.getCount(player, BaseCountType.GroupCopyMap, param.get(1));
        } else if (param.get(0) == FunctionVariable.BossFirstBloodreward) {
            Cfg_Boss_FirstBlood_Bean bean = CfgManager.getCfg_Boss_FirstBlood_Container().getValueByKey(param.get(1));
            if (bean == null) return 0;
            return player.getFirstKillData().getOrDefault(bean.getMonster_id(), 0) > 0 ? 1 : 0;
        } else if (param.get(0) == FunctionVariable.SkillEvolutionTotal) {
            return (int) Manager.countManager.getVariant(player, VariantType.UpSkillTuibianCount);
        } else if (param.get(0) == FunctionVariable.StateStifleAddTotal) {
            PlayerSateStifleData stifleData = player.getStifleData();
            SoulSpiritInfo info = stifleData.getSpiritMap().get(param.get(1) / 100);
            if (info == null) return 0;
            int configId = info.getId() * 100 + info.getEvolveLv();
            return configId >= param.get(1) ? configId : 0;
        } else if (param.get(0) == FunctionVariable.GuildTaskTotal) {
            return (int) Manager.countManager.getCount(player, BaseCountType.GuildTaskTotal, 0);
        } else if (param.get(0) == FunctionVariable.MarryTotal) {
            return (int) Manager.countManager.getCount(player, BaseCountType.FinishWedding, param.get(1));
        } else if (param.get(0) == FunctionVariable.Soul_copy_Num) {
            return player.getFlyswordAllInfo().getSwordSoulLayer() - 1;
        } else if (param.get(0) == FunctionVariable.FlyswardActivated) {
            return Manager.huaxinFlySwordManager.deal().getFlySwordActivateState(player, param.get(1));
        } else if (param.get(0) == FunctionVariable.WorldLevelLimit) {
            return GlobalType.getWorldLevel();
        } else if (param.get(0) == FunctionVariable.FlyswardLevelup) {
            return Manager.huaxinFlySwordManager.deal().getFlySwordLevel(player, param.get(1));
        } else if (param.get(0) == FunctionVariable.MagicWeaponRank) {
            return player.getStifleData().getLevel();
        } else if (param.get(0) == FunctionVariable.HorseStarNumRank) {
            return player.getHorse().getHorseSteps();
        } else if (param.get(0) == FunctionVariable.Recharge_Money_Limit) {
            return Manager.rechargeManager.deal().getRechargeNum(player, param.get(1));
        } else if (param.get(0) == FunctionVariable.Recharge_Gift_Limit) {
            return Manager.rechargeManager.deal().getRechargeNumForType(player, param.get(1));
        } else if (param.get(0) == FunctionVariable.Daily_Log_In) {
            return Manager.countManager.getBooleanCountValue(player, BooleanDay.DailyLogin) ? 1 : 0;
        } else if (param.get(0) == FunctionVariable.Sever_Open_Day) {
            return TimeUtils.getOpenServerDay();
        } else if (param.get(0) == FunctionVariable.Recharge_Money_Day) {
            return (int) Manager.rechargeManager.deal().rechargeDay(player);
        } else if (param.get(0) == FunctionVariable.ActiveValueGet) {
            long ap = player.getHistoryCoin().get(ItemCoinType.ActivePoint);
            return ap > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) ap;
        } else if (param.get(0) == FunctionVariable.Limit_direct_shop_condition) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Direct_shop, param.get(1));
        } else if (param.get(0) == FunctionVariable.Daily_Online_Time) {
            return (int) (Manager.countManager.getVariant(player, VariantType.Daily_Online_Time) / 60);
        } else if (param.get(0) == FunctionVariable.Daily_XianJiaXunBao_Times) {
            return (int) Manager.countManager.getVariant(player, VariantType.Daily_XianJiaXunBao_Times);
        } else if (param.get(0) == FunctionVariable.Daily_LeaderPreach_Time) {
            return (int) (Manager.countManager.getVariant(player, VariantType.Daily_LeaderPreach_Time) / 60);
        } else if (param.get(0) == FunctionVariable.Daily_Meditation_Time) {
            return (int) (Manager.countManager.getVariant(player, VariantType.Daily_Meditation_Time) / 60);
        } else if (param.get(0) == FunctionVariable.Daily_SwordSoul_Times) {
            return (int) Manager.countManager.getVariant(player, VariantType.Daily_SwordSoul_Times);
        } else if (param.get(0) == FunctionVariable.Daily_JJC_Times) {
            return (int) Manager.countManager.getVariant(player, VariantType.Daily_JJC_Times);
        } else if (param.get(0) == FunctionVariable.Daily_Kill_Self_Boss_Times) {
            return (int) Manager.countManager.getVariant(player, VariantType.Daily_Kill_Self_Boss_Times);
        } else if (param.get(0) == FunctionVariable.Daily_Kill_UnLimit_Boss_Times) {
            return (int) Manager.countManager.getVariant(player, VariantType.Daily_Kill_UnLimit_Boss_Times);
        } else if (param.get(0) == FunctionVariable.Daily_Kill_WuJIArea_Boss_Times) {
            return (int) Manager.countManager.getVariant(player, VariantType.Daily_Kill_WuJIArea_Boss_Times);
        } else if (param.get(0) == FunctionVariable.Daily_Kill_JingJia_Boss_Times) {
            return (int) Manager.countManager.getVariant(player, VariantType.Daily_Kill_JingJia_Boss_Times);
        } else if (param.get(0) == FunctionVariable.Daily_Kill_VIP_Boss_Times) {
            return (int) Manager.countManager.getVariant(player, VariantType.Daily_Kill_VIP_Boss_Times);
        } else if (param.get(0) == FunctionVariable.Daily_Enter_TJZM_Times) {
            return (int) Manager.countManager.getVariant(player, VariantType.Daily_Enter_TJZM_Times);
        } else if (param.get(0) == FunctionVariable.Daily_ShangJingFunc_Times) {
            return (int) Manager.countManager.getVariant(player, VariantType.Daily_ShangJingFunc_Times);
        } else if (param.get(0) == FunctionVariable.Daily_Active_Value) {
            return (int) Manager.countManager.getVariant(player, VariantType.Daily_Active_Value);
        } else if (param.get(0) == FunctionVariable.Skill_Star_Count) {
            return Manager.skillManager.deal().getAllStar(player);
        } else if (param.get(0) == FunctionVariable.WorldChat) {
            return (int) Manager.countManager.getVariant(player, VariantType.WorldChat);
        } else if (param.get(0) == FunctionVariable.Akill_Position_Level) {
            return Manager.skillManager.deal().getAllLevel(player);
        } else if (param.get(0) == FunctionVariable.Pet_Equip_Inten) {
            return Manager.petManager.deal().calcPetEquipSlotNumber(player);
        } else if (param.get(0) == FunctionVariable.Join_ArenaTop_Num) {
            return (int) Manager.countManager.getVariant(player, VariantType.PeakTotal);
        } else if (param.get(0) == FunctionVariable.LingTiStar) {
            return player.getSpiritData().getOpenStar();
        } else if (param.get(0) == FunctionVariable.GloryShop_Buy) {
            return (int) Manager.countManager.getCount(player, BaseCountType.GloryShopBuyItem, param.get(1));
        } else if (param.get(0) == FunctionVariable.Pet_Equip_Strengthen_Level) {
            return player.getActivePet().getEquipLevelTotal();
        } else if (param.get(0) == FunctionVariable.Pet_Equip_Resolve) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Pet_Equip_Resolve, 0);
        } else if (param.get(0) == FunctionVariable.Kill_Cross_Boss_Num) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.Kill_SoulBeast_Boss_Num) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.EightCity_Reward_Num) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.God_weapon_Strengthen_Level) {
            return player.getWeapon().getCurrentId();
        } else if (param.get(0) == FunctionVariable.Kill_crossfudi_Boss) {
            return (int) Manager.countManager.getVariant(player, VariantType.CrossFudBossKill);
        } else if (param.get(0) == FunctionVariable.Playertitle) {
            return player.getTitleData().getTitleList().containsKey(param.get(1)) ? 1 : 0;
        } else if (param.get(0) == FunctionVariable.VipLevel) {
            return player.getVipLv();
        } else if (param.get(0) == FunctionVariable.WornEquipPower) {
            return Utils.find(player.getEquipParts(), p -> p.getEquipPower() >= param.get(1)).size();
        } else if (param.get(0) == FunctionVariable.AuchtionDevilCardBuy) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.AuchtionDevilCardSell) {
            return (int) Manager.countManager.getCount(player, BaseCountType.Event_TIMES, param.get(0));
        } else if (param.get(0) == FunctionVariable.GetEquipsId) {
            int num = Manager.backpackManager.manager().getItemNum(player, param.get(1));
            num += Manager.equipManager.deal().getEquipNum(player, param.get(1));
            return num;
        } else if(param.get(0) == FunctionVariable.Horse_equip_score){
            return player.getHorse().getEquips().get(param.get(1)).getScore();
        } else if(param.get(0) == FunctionVariable.Horse_equip_pos){
            return Manager.horseManager.deal().isHorseEquipActived(player, param.get(1));
        } else if(param.get(0) == FunctionVariable.Daily_DevilHunt_Times){
            return (int) Manager.countManager.getVariant(player, VariantType.Daily_DevilHunt_Times);
        } else if(param.get(0) == FunctionVariable.Guild_auction_company_business_frequency){
            return (int)Manager.countManager.getCount(player, BaseCountType.Event_TIMES, FunctionVariable.Guild_auction_company_business_frequency);
        } else if(param.get(0) == FunctionVariable.WornHolyEquip_strengthen_Grade){
            return Manager.holyEquipManager.deal().getHolyEquipStrengthenLevel(player);
        } else if(param.get(0) == FunctionVariable.WornHolyEquip_activation_level_cool){
            return Manager.holyEquipManager.deal().getDouxin(player, param.get(1), param.get(2));
        } else if(param.get(0) == FunctionVariable.Holydress_sum){
            return Manager.holyEquipManager.deal().getHolyEquipNum(player);
        }
        logger.error(player + "检查一个无知的类型（" + param.get(0) + "），参数是（" + param.toString() + "）");
        return 0;
    }


    @Override
    public void operateFuncOpen(Player player, int OverType) {
        Set<Integer> funcIds = Manager.controlManager.getCondFunc().get(OverType);
        if (funcIds == null) {
            return;
        }
        BackendMessage.ResSwitchFunction.Builder resMsg = BackendMessage.ResSwitchFunction.newBuilder();
        for (int funcId : funcIds) {
            //已经开放
            if (player.functionState.containsKey(funcId)) {
                if (player.functionState.get(funcId)) {
                    continue;
                }
            }

            if (isOpenFunction(player, funcId)) {
                BackendMessage.FuncOpenInfo.Builder info = BackendMessage.FuncOpenInfo.newBuilder();
                player.functionState.put(funcId, true);
                functionStartEvent(player, funcId);
                info.setId(funcId);
                info.setState(ControlManager.OPEN_FUNCTION);
                resMsg.addSwitchFuncList(info);

                changeFuncCallBack(player, funcId);
            }
        }
        resMsg.setIsnew(true);
        sendSwitchFuncList(player, resMsg);
    }

    @Override
    public void gmFuncOpen(Player player) {
        BackendMessage.ResFuncOpenList.Builder funcMsg = BackendMessage.ResFuncOpenList.newBuilder();
        for (Cfg_FunctionStart_Bean bean : CfgManager.getCfg_FunctionStart_Container().getValuees()) {
            boolean isOpen = Manager.controlManager.deal().isOpenFunction(player, bean.getFunction_id());
            if (!isOpen) {
                functionStartEvent(player, bean.getFunction_id());
            }
            player.functionState.put(bean.getFunction_id(), true);
            BackendMessage.FuncOpenInfo.Builder info = BackendMessage.FuncOpenInfo.newBuilder();
            info.setId(bean.getFunction_id());
            if (player.getFuncRewardState().contains(bean.getFunction_id())) {
                info.setState(ControlManager.OPEN_REWARD_FUNCTION);
            } else {
                info.setState(ControlManager.OPEN_FUNCTION);
            }
            funcMsg.addFuncOpenList(info);
        }
        MessageUtils.send_to_player(player, BackendMessage.ResFuncOpenList.MsgID.eMsgID_VALUE, funcMsg.build().toByteArray());
    }

    @Override
    public void onFuncReward(Player player, int id) {
        Cfg_FunctionOpenTips_Bean bean = CfgManager.getCfg_FunctionOpenTips_Container().getValueByKey(id);
        if (bean == null) {
            logger.error("Cfg_FunctionOpenTips_Bean  为找到  " + id);
            return;
        }
        if (player.getFuncRewardState().contains(bean.getFunction_id())) {
            logger.info("getFuncRewardState  已经被领取   " + bean.getFunction_id());
            return;
        }

        if (!isOpenFunction(player, bean.getFunction_id())) {
            logger.info("isOpenFunction  未开启");
            return;
        }

        player.getFuncRewardState().add(bean.getFunction_id());

        //修改按照职业获取奖励
        List<Item> items = Item.createItems(player.getCareer(),bean.getAward_item(),1);
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.FunctionRewardGet, id);
        } else {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.FunctionRewardGet);
        }

        BackendMessage.ResFuncOpenReward.Builder msg = BackendMessage.ResFuncOpenReward.newBuilder();
        msg.setId(id);
        MessageUtils.send_to_player(player, BackendMessage.ResFuncOpenReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onFuncClose(Player player) {
        BackendMessage.ResSwitchFunction.Builder resMsg = BackendMessage.ResSwitchFunction.newBuilder();

        if (TimeUtils.getOpenServerDay() == Global.New_server_growup_time) {
            resMsg.addSwitchFuncList(builder(FunctionStart.ServeCrazy));
            resMsg.addSwitchFuncList(builder(FunctionStart.GrowthWay));
        }

        if (TimeUtils.getOpenServerDay() == Global.New_server_active_time) {
            resMsg.addSwitchFuncList(builder(FunctionStart.ZongPaiStar));
            resMsg.addSwitchFuncList(builder(FunctionStart.JingJieReach));
            resMsg.addSwitchFuncList(builder(FunctionStart.PerfectQingYuan));
            resMsg.addSwitchFuncList(builder(FunctionStart.ZongPaiFight));
        }

        if (TimeUtils.getOpenServerDay() == Global.New_server_active_exchange) {
            resMsg.addSwitchFuncList(builder(FunctionStart.ServerActive));
        }

        resMsg.setIsnew(false);
        if (resMsg.getSwitchFuncListCount() > 0) {
            MessageUtils.send_to_player(player, BackendMessage.ResSwitchFunction.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
        }
    }

    private void functionStartEvent(Player player, int funcId) {
        Manager.shopManager.limitShop().refresh(player);
        if (funcId == FunctionStart.Realm) {
            Manager.stateVipManager.deal().initStateVip(player);
        } else if (funcId == FunctionStart.GodBook) {
            Manager.godBookManager.deal().initAmulet(player);
        } else if (funcId == FunctionStart.LianQiGem) {
            Manager.gemManager.deal().initGemInfo(player);
        } else if (funcId == FunctionStart.Nature) {
            Manager.natureManager.deal().initPlayerAllNatureFunction(player);
        } else if (funcId == FunctionStart.NatureWing) {
            Manager.natureManager.deal().initPlayerWing(player);
        } else if (funcId == FunctionStart.Mount) {
            Manager.natureManager.deal().initPlayerHorse(player);
        } else if (funcId == FunctionStart.MountEquip) {
            Manager.horseManager.deal().initPlayerHorseEquip(player);
        } else if (funcId == FunctionStart.Pet) {
            Manager.petManager.deal().initPet(player, funcId);
        } else if (funcId == FunctionStart.ServeCrazy) {
            Manager.openServerAcManager.deal().initRevel(player);
        } else if (funcId == FunctionStart.GrowthWay) {
            Manager.openServerAcManager.deal().initGrowUp(player);
        } else if (funcId == FunctionStart.ServerActive) {
            Manager.openServerAcManager.deal().initOpenServerSpec(player);
        } else if (funcId == FunctionStart.RealmStifle) {
            Manager.natureManager.deal().initStifleFabao(player);
        } else if (funcId == FunctionStart.LingTi || funcId == FunctionStart.LingtiFanTai) {
            Manager.equipManager.deal().sendSpiritInfo(player);
        } else if (funcId == FunctionStart.MonsterAF) {
            Manager.soulBeastManager.deal().functionOpen(player);
        } else if (funcId == FunctionStart.GuildBoss) {
            Manager.guildActivityManager.deal().syncGuildBossOCTime(player);
        } else if (funcId == FunctionStart.DailyRechargeForm) {
            Manager.commercializeManager.playerOnline(player, CommercializeMessage.Commercialize.DailyRecharge);
        } else if (funcId == FunctionStart.ResBack) {
            Manager.retrieveResManager.getScript().online(player);
        } else if (funcId == FunctionStart.FlySwordMandate) {
            Manager.huaxinFlySwordManager.swordSoulScript().initHookInfo(player);
        } else if (funcId == FunctionStart.PayNewbie) {
            Manager.rechargeManager.deal().setPayNewbieStart(player);
        } else if (funcId == FunctionStart.FaBaoDrug) {
            Manager.natureManager.deal().initFabaoDrug(player);
        } else if (funcId == FunctionStart.PetProSoul) {
            Manager.natureManager.deal().initPetDrug(player);
        } else if (funcId == FunctionStart.TJLBase) {
            Manager.fallingSkyManager.deal().online(player);
        } else if (funcId == FunctionStart.NatureWeapon) {
            Manager.natureManager.deal().initPlayerWeapon(player);
        }
    }

    private void checkFatherFunClose(int funcId) {
        Cfg_FunctionStart_Bean curBean = CfgManager.getCfg_FunctionStart_Container().getValueByKey(funcId);
        if (curBean == null || curBean.getParent_id() < 10000) {
            return;
        }
        for (Cfg_FunctionStart_Bean cfg_functionStartBean : CfgManager.getCfg_FunctionStart_Container().getValuees()) {
            if (cfg_functionStartBean.getFunction_id() == curBean.getFunction_id()) {
                continue;
            }
            if (cfg_functionStartBean.getParent_id() == curBean.getParent_id()) {
                if (isBackOpen(cfg_functionStartBean.getFunction_id())) {
                    return;
                }
            }
        }
        changeBackFunc(curBean.getParent_id(), ControlManager.CLOSE_FUNCTION);
    }

    private void syncFunctionToAll() {
        for (Player player : Manager.playerManager.getPlayersCache().values()) {
            changePlayerFunc(player);
        }
        ServerParamUtil.saveFuncs();
    }

    public void changePlayerFunc(Player player) {
        BackendMessage.ResSwitchFunction.Builder resMsg = BackendMessage.ResSwitchFunction.newBuilder();
        for (Cfg_FunctionStart_Bean bean : CfgManager.getCfg_FunctionStart_Container().getValuees()) {
            collectPlayerFuncChange(player, bean.getFunction_id(), resMsg);
        }
        resMsg.setIsnew(false);
        sendSwitchFuncList(player, resMsg);
    }


    public void changePlayerFunc(Player player, int[] funcIDs) {
        BackendMessage.ResSwitchFunction.Builder resMsg = BackendMessage.ResSwitchFunction.newBuilder();
        for (int i = 0; i < funcIDs.length; i++) {
            collectPlayerFuncChange(player, funcIDs[i], resMsg);
        }
        resMsg.setIsnew(false);
        sendSwitchFuncList(player, resMsg);
    }

    /**
     * 状态改变
     *
     * @param player
     * @param type
     * @param changeNum
     */
    @Override
    public void operate(Player player, int type, int changeNum) {
        operate(player, changeNum, new int[]{type});
    }

    /**
     * 状态改变
     *
     * @param player
     * @param types
     * @param changeNum
     */
    @Override
    public void operate(Player player, int changeNum, int... types) {
        if (GameServer.getInstance().IsFightServer()) {
            return;
        }
        int type = types[0];
        Cfg_FunctionVariable_Bean bean = CfgManager.getCfg_FunctionVariable_Container().getValueByKey(type);
        if (bean == null) {
            return;
        }

        if (bean.getFunction() != 0) {
            //检查系统开放
            Manager.controlManager.deal().operateFuncOpen(player, type);
        }
        if (bean.getAchievement() != 0) {
            //检查成就
            Manager.achievementManager.deal().checkAchievement(player, type);
        }
        if (bean.getGemstone() != 0) {
            //检查宝石
            Manager.gemManager.deal().checkOpenState(player);
        }
        if (bean.getState() != 0) {
            //检查境界
            Manager.stateVipManager.deal().operateStateVip(player, type);
        }
        if (bean.getSpell() != 0) {
            //天书符咒任务检查
            Manager.godBookManager.deal().checkUpdateAmulet(player, type);
        }
        if (bean.getGrowup() != 0) {
            //成长之路,开服活动
            Manager.openServerAcManager.deal().onReqGrowUpProgress(player, type);
            Manager.openServerAcManager.deal().onReqOpenServerSpecProgress(player, type);
            Manager.openServerAcManager.deal().onAddluckyTaskFenzi(player);
        }
        if (bean.getVipRebate()!=0){
            //V4返利
            Manager .openServerAcManager.v4Rebate().onReqV4RebeteUpProgress(player,type,changeNum);
        }
        if (bean.getFashion() != 0) {
            //时装
            //Manager.fashionManager.deal().taskUpdate(player,type);
            Manager.newFashionManager.deal().activeNewFashion(player, type, changeNum);
        }
        if (bean.getTask() != 0) {
            //检查功能操作类的任务
            Manager.taskManager.deal().functionVariableChange(player, type, changeNum);
//            Manager.taskManager.addCommand(new TaskCommand(player, type, changeNum));
        }
        if (bean.getStifle() != 0) {
            Manager.stateStifleManager.deal().conditionUpdate(player, type);
        }
        if (bean.getNew_active() != 0) {
            Manager.openServerAcManager.deal().checkCompleteActTask(player);
        }
        if (bean.getDirect_shop() != 0) {
            Manager.rechargeManager.discountScript().checkDiscountRecharge(player);
        }
        if (bean.getFallingSky() != 0) {
            Manager.fallingSkyManager.deal().onRefreshUpProgress(player, type, changeNum);
        }
        if (bean.getLuckyDraw() != 0) {//周福利抽奖
            Manager.luckyDrawManager.deal().onRefreshLuckyDrawData(player, true);
        }
        if (bean.getPetEquipUnlock() != 0) {
            Manager.petManager.deal().activePetEquip(player, null);
        }
        if (bean.getHorseEquipUnlock() != 0) {
            Manager.horseManager.deal().activeHorseEquip(player, 0);
        }
        //消耗元宝需要同步到活动排行榜
        if (type == FunctionVariable.ConsumeDiamonds) {
            Manager.rankListManager.deal().addConsumeGoldRank(player, changeNum);
        }
        if (bean.getSoulEquipUnlock() != 0) {
            Manager.soulArmorManager.script().checkActiveSlot(player);
        }
        if (bean.getCross_devil() != 0) {
            Manager.devilSeriesManager.getScript().activeDevilCard(player);
        }
        //仙盟宝箱任务
        if (bean.getGuild_gift() != 0) {
            Manager.guildsManager.manager().incrGiftProgress(player, type, changeNum);
        }
        //完美情缘任务更新进度
        if (bean.getMarry_activity_task() != 0) {
            Manager.marriageManager.activity().onRefreshUpProgress(player, type, true);
        }
        //npc 好友任务更新
        if (bean.getNPC_friend() != 0) {
            Manager.friendManager.deal().onRefreshUpProgress(player);
        }
        //每日功能任务
        if(bean.getToday_function_task() != 0){
            Manager.functionTaskManager.getScript().onRefreshUpProgress(player, changeNum, types);
        }
        //仙盟争霸
        if(bean.getGuild_battle() != 0){
            Manager.openServerAcManager.deal().onRefreshGuildBattle(player, changeNum, types);
        }
    }

    /**
     * 搜集玩家功能改变列表
     *
     * @param player
     * @param funcID
     * @param resMsg
     */
    private void collectPlayerFuncChange(Player player, int funcID, BackendMessage.ResSwitchFunction.Builder resMsg) {
        boolean isOpen = isOpenFunction(player, funcID);
        if (!player.functionState.containsKey(funcID)) {
            //有没有这个开关就一定要发送给客户端
            player.functionState.put(funcID, !isOpen);
        }

        boolean self = player.functionState.get(funcID);
        if (self != isOpen) {
            resMsg.addSwitchFuncList(builder(player, funcID, isOpen));
            //如果检查到已经开启了就处理一些逻辑
            if (isOpen) {
                functionStartEvent(player, funcID);
            }
            player.functionState.put(funcID, isOpen);

            changeFuncCallBack(player, funcID);
        }
    }

    /**
     * 发送给角色功能改变的列表
     *
     * @param player
     * @param resMsg
     */
    private void sendSwitchFuncList(Player player, BackendMessage.ResSwitchFunction.Builder resMsg) {
        if (resMsg.getSwitchFuncListCount() > 0) {
            MessageUtils.send_to_player(player, BackendMessage.ResSwitchFunction.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
        }
    }

    /**
     * 开关改变回调
     *
     * @param player
     * @param funcId
     */
    private void changeFuncCallBack(Player player, int funcId) {
        try {
            if (funcId == FunctionStart.VipInvationNormal || funcId == FunctionStart.VipInvationZunGui) {
                //检测 高级VIP和高级至尊VIP
                Manager.vipManager.deal().checkSpecialVip(player);
            }
        } catch (Exception e) {
            logger.error("开关改变回调失败, funcId:" + funcId + " msg:" + e.getMessage());
        }


    }

    private BackendMessage.FuncOpenInfo.Builder builder(Player player, int funcID, boolean isOpen) {
        BackendMessage.FuncOpenInfo.Builder info = BackendMessage.FuncOpenInfo.newBuilder();
        info.setId(funcID);
        if (player.getFuncRewardState().contains(funcID)) {
            info.setState(ControlManager.OPEN_REWARD_FUNCTION);
        } else {
            info.setState(isOpen ? ControlManager.OPEN_FUNCTION : ControlManager.CLOSE_FUNCTION);
        }
        return info;
    }

    private BackendMessage.FuncOpenInfo.Builder builder(int funcId) {
        BackendMessage.FuncOpenInfo.Builder info = BackendMessage.FuncOpenInfo.newBuilder();
        info.setId(funcId);
        info.setState(ControlManager.CLOSE_FUNCTION);
        return info;
    }

    private int changeJobDragonSoul(Player player, int gender, int steps) {
        int num = 0;
//        for (Integer id : player.getFateStar()) {
//            Cfg_ChangeStar_Bean bean = CfgManager.getCfg_ChangeStar_Container().getValueByKey(id);
//            if (bean == null) {
//                continue;
//            }
//            if (bean.getGender() == gender && bean.getSteps() == steps) {
//                num++;
//            }
//        }
        return num;
    }

}
