package common.player;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.Global;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.data.bean.Cfg_Relive_Bean;
import com.game.backpack.structs.ItemCoinType;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.type.ReviveBehavior;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapDefine;
import com.game.manager.Manager;
import com.game.nature.structs.HuaxinEntity;
import com.game.ninedaysfocused.script.INineDaysCloneWar;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.robot.script.IRobotFightScript;
import com.game.structs.EntityState;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.guild.structs.Guild;
import com.game.jjc.script.IJJCCloneScript;
import com.game.map.structs.MapUtils;
import com.data.MessageString;
import com.game.monster.structs.Monster;
import com.game.monster.structs.MonsterDefine;
import com.game.player.script.IPlayerBattle;
import com.game.robot.struct.Robot;
import com.game.server.GameServer;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.PlayerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author admin
 */
public class PlayerBattleScript implements IScript, IPlayerBattle {

    private static final Logger logger = LogManager.getLogger(PlayerBattleScript.class);

    @Override
    public int getId() {
        return ScriptEnum.PlayerBattleBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void doDie(Player diePlayer, Fighter attacker) {
        try {
            if (diePlayer == null) {
                return;
            }

            //死亡清理战斗CD
            Manager.cooldownManager.removeCooldown(diePlayer, CooldownTypes.SKILL, null);
            Manager.cooldownManager.removeCooldown(diePlayer, CooldownTypes.PUBLIC, null);
            Manager.cooldownManager.removeCooldown(diePlayer, CooldownTypes.SKILL_PUBLIC, null);
            Manager.cooldownManager.removeCooldown(diePlayer, CooldownTypes.RollCD, null);

            if (EntityState.Dead.compare(diePlayer.getState())) {
                diePlayer.clearHatred();
                return;
            }
            //玩家死亡
            MapObject map = Manager.mapManager.getMap(diePlayer.gainMapId());
            attacker = MapUtils.getOwnFighter(map, attacker);

            if (map.getSetting().getIsscript() > 0) {
                Manager.mapManager.base(map.getSetting().getIsscript()).onPlayerDie(map, attacker, diePlayer);
            }

            Cfg_Clone_map_Bean mapBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getZoneModelId());

            diePlayer.setDie();
            logger.info(diePlayer.getName() + "死亡 roleId=" + diePlayer.getId() + "killer=" + attacker);
            if (attacker instanceof Player) {
                Player attackerPlayer = (Player) attacker;
                playerKillPlayer(diePlayer, attackerPlayer);
                if(mapBean != null){
                    Manager.controlManager.operate(attackerPlayer, FunctionVariable.Killpalyer_daily_num, mapBean.getType(), 1);
                }
                Manager.biManager.getScript().biDeath(diePlayer, map.getMapModelId(), map.getName(), map.getType(), mapBean == null ? null : mapBean.getMin_lv(), 1, attackerPlayer.getId(), attackerPlayer.getName(), attackerPlayer.getLevel(), attackerPlayer.getFightPoint(), 0);
            } else if (attacker instanceof Monster) {
                Monster monster = (Monster) attacker;
                monster.setKillcount(monster.getKillcount() + 1);
                //增加怪物的死亡记录数
                if (monster.getMonsterType() != MonsterDefine.MonsterType_Normal) {
                    monster.getPlayerDieTime().put(diePlayer.getId(), TimeUtils.Time());
                }
                Manager.biManager.getScript().biDeath(diePlayer, map.getMapModelId(), map.getName(), map.getType(), mapBean == null ? null : mapBean.getMin_lv(), 2, monster.getModelId(), monster.getName(), monster.getLevel(), monster.getScore(), 0);
            }

            //找回宠物
            Pet pet = Manager.petManager.deal().getBattlePet(diePlayer);
            if (pet != null) {
                Manager.mapManager.manager().onQuitMap(map, pet, true);
                pet.reset();
                pet.addState(EntityState.ExitGame);
                // manager.petManager.deal().OnCallBack(diePlayer, pet);
            }
            //法宝召回
            HuaxinEntity huaxin = diePlayer.getCurHuaxinEntity();

            if (huaxin != null && huaxin.getExcelId() > 0) {
                Manager.mapManager.manager().onQuitMap(map, huaxin, true);
                huaxin.addState(EntityState.ExitGame);
            }
            //死亡更新血量
            if (diePlayer.getTeamId() > 0) {
                //更新队伍信息
                Manager.teamManager.deal().updateHpAndMapKey(diePlayer);
            }
            //计算buff
            Manager.buffManager.deal().onDie(diePlayer);

            //下坐骑
            Manager.horseManager.autoUnRide(diePlayer);

            Cfg_Relive_Bean relive_bean = CfgManager.getCfg_Relive_Container().getValueByKey(map.getSetting().getRelive_type());
            if (relive_bean == null) {
                return;
            }

            BehaviorManager.CancelAllBehavior(diePlayer);
            if (relive_bean.getAuto_relive_time() > 0) {
                ReviveBehavior rb = new ReviveBehavior(diePlayer);
                rb.setReviveTime(TimeUtils.Time() + relive_bean.getAuto_relive_time());
                BehaviorManager.InsertBehavior(diePlayer, rb);
            }

            // 清除助战
            if (Manager.worldHelpManager.getScript().canClear(diePlayer)) {
                Manager.worldHelpManager.getScript().die(diePlayer);
            }
        } catch (Exception ex) {
            logger.error(ex, ex);
        }
    }

    //玩家杀死玩家
    private void playerKillPlayer(Player diePlayer, Player attackerPlayer) {
        Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(diePlayer.gainMapModelId());
        if (mapBean == null) {
            return;
        }
        MapObject map = Manager.mapManager.getMap(diePlayer.gainMapId());
        if (map == null) {
            return;
        }
        Cfg_Mapsetting_Bean mapSet = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (mapSet == null) {
            return;
        }
        MessageUtils.notify_player(diePlayer, Notify.CHAT, MessageString.BeKill_By_Player, TimeUtils.format2string(TimeUtils.Time(), "HH:mm:ss"), attackerPlayer.getName());

        if (mapSet.getGuild_kill() == 1 && diePlayer.isHaveGuild()) {//如果玩家有公会
            boolean isChat = true;
            if (isChat) {
                String ortherGuildName = "";
                if (attackerPlayer.isHaveGuild()) {
                    //manager.guildsManager.getGuildByRoleId(attackerPlayer.getId());
                    ortherGuildName = attackerPlayer.gainGuildName();
                }
                String positionStr = "<t=4>3," + diePlayer.gainMapModelId() + "_" + map.getId() + "," + (int) diePlayer.gainCurPos().getX() + "," + (int) diePlayer.gainCurPos().getY() + "</t>";
                String[] strMess = new String[4];
                String mapId = Manager.mapManager.getChatMapName(diePlayer.gainMapModelId());

//                String str = String.format(ServerStr.getStr(ServerStr.guildKill), diePlayer.getName(), manager.mapManager.getChatMapName( diePlayer.gainMapModelId()), ortherGuildName, attackerPlayer.getName());
                strMess[0] = diePlayer.getName();
                strMess[1] = Manager.mapManager.getChatMapName(diePlayer.gainMapModelId());
                strMess[2] = ortherGuildName;
                strMess[3] = attackerPlayer.getName();
                if (ortherGuildName == null || "".equals(ortherGuildName.trim())) {
//                    str = String.format(ServerStr.getStr(ServerStr.guildKillNotGuild), diePlayer.getName(), manager.mapManager.getChatMapName( diePlayer.gainMapModelId()), attackerPlayer.getName());
                    strMess = new String[3];
                    strMess[0] = diePlayer.getName();
                    strMess[1] = Manager.mapManager.getChatMapName(diePlayer.gainMapModelId());
                    strMess[2] = attackerPlayer.getName();
                    Guild guild = Manager.guildsManager.GetGuildByPlayer(diePlayer);
                    if (guild != null) {
                        MessageUtils.notify_Chat_To_GuildPlayer(diePlayer, guild, false, MessageString.GuildKillNotGuild,guild.getName(), diePlayer.getName(), mapId, attackerPlayer.getName(), positionStr);
                    }
                } else {
                    Guild guild = Manager.guildsManager.GetGuildByPlayer(diePlayer);
                    if (guild != null) {
//                        MessageUtils.notify_Chat_To_GuildPlayer(diePlayer, guild, false, MessageString.GuildKill, diePlayer.getName(), mapId, ortherGuildName, attackerPlayer.getName(), positionStr);
                    }
                }
//                String positionStr = "<t=4>2," + diePlayer.gainMapModelId() + "_5," + (int) diePlayer.gainCurPos().getX() + "," + (int) diePlayer.gainCurPos().getY() + "</t>";
//                str = "<t=0>,," + str + "</t>";
//                <t=4>3,mapId_cloneId,x,y</t>

//                manager.chatManager.sendSystemStrToGuild(diePlayer, str + positionStr, false);
                Guild g = Manager.guildsManager.getGuildById(diePlayer.getGuildId());

//                if (g.getOrgMember().getMap().get(diePlayer.getId()) == GuildSysConfig.TYPE_MASTER || g.getOrgMember().getMap().get(diePlayer.getId()) == GuildSysConfig.TYPE_VICE_MASTER) {
//                    String[] dieGuild = new String[2];
//                    dieGuild[0] = g.getName();
//                    dieGuild[1] = g.getOrgMember().getMap().get(diePlayer.getId()) == GuildSysConfig.TYPE_MASTER ? "1&_" + MessageString.GuildRank_Chairman_Str : "1&_" + MessageString.GuildRank_ViceChairman_Str;
//                    String[] c = new String[dieGuild.length + strMess.length];
//                    System.arraycopy(dieGuild, 0, c, 0, dieGuild.length);
//                    System.arraycopy(strMess, 0, c, dieGuild.length, strMess.length);
////                    String strAll = "";
//                    if (c.length == 6) {
////                        strAll = ServerStr.getStr(ServerStr.GuildKillAll);
//                        MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_MARQUEE, MessageString.GuildKill, c);
//                    } else if (c.length == 5) {
////                        strAll = ServerStr.getStr(ServerStr.GuildKillNotGuildAll);
//                        MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_MARQUEE, MessageString.GuildKillNotGuild, c);
//                    }
////                    String sendStr = StringUtils.mergeString(strAll, c);
////                    manager.chatManager.sendMsg2Channel(diePlayer, sendStr, ChatManager.CHATCHANNEL_SYSTEM);
//                }

            }
        }

        //manager.teamManager.playerDie(diePlayer, attackerPlayer);
        if (mapBean.getPkState() == MapDefine.PK_STATE1) {
            dealPk(diePlayer, attackerPlayer);
        }
    }

    //处理pk
    private void dealPk(Player diePlayer, Player attackerPlayer) {
        //清除关系
        removeHatredPlayer(diePlayer, attackerPlayer.getId());
        removeHatredPlayer(attackerPlayer, diePlayer.getId());

        if (GameServer.getInstance().IsFightServer()) {
            return;
        }
        //自卫成功
        if (attackerPlayer.getPklist().containsKey(diePlayer.getId())) {
            attackerPlayer.getPklist().remove(diePlayer.getId());
        } else {

            //attackerPlayer.setGoodEvil(attackerPlayer.getGoodEvil() + PlayerDefine.PkAddValue);
//            manager.backpackManager.addGoodEvil(attackerPlayer, PlayerDefine.PkAddValue, ItemChangeReason.PkDec, IDConfigUtil.getLogId());
            //红名被杀不加善恶值
//            if (diePlayer.getCurrencys().get(ItemCoinType.GoodEvil) >= PlayerDefine.PkRedName) {
//                return;
//            }

//            int oldvalue = Manager.currencyManager.manager().getCurrencyIntNum(attackerPlayer, ItemCoinType.GoodEvil);
//            if (oldvalue < 1) {//如果没有CD， 则加上CD，　让下一分钟再执行
//                Manager.cooldownManager.addCooldown(attackerPlayer, CooldownTypes.PkCleanCd, null, PlayerDefine.PkCleanTime);
//            }
//            int value = PlayerDefine.PkAddValue;
//            if (oldvalue + value > PlayerDefine.PkGoodEvil) {
//                value = PlayerDefine.PkGoodEvil - oldvalue;
//            }
//            logger.error(diePlayer.nameIdString() + "死亡了，" + attackerPlayer.nameIdString() + " 增加" + value + "的善恶值！");
//            Manager.currencyManager.manager().onAddItemCoin(attackerPlayer, value, ItemChangeReason.PkDec, IDConfigUtil.getLogId(), ItemCoinType.GoodEvil);
//
//            PlayerMessage.ResUpdataPkValue.Builder msg = PlayerMessage.ResUpdataPkValue.newBuilder();
//            msg.setPlayerId(attackerPlayer.getId());
//            msg.setPkValue(Manager.currencyManager.manager().getCurrencyIntNum(attackerPlayer, ItemCoinType.GoodEvil));
//            MessageUtils.send_to_roundPlayer(attackerPlayer, PlayerMessage.ResUpdataPkValue.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void beAttack(Player player, Fighter attacker, long damage) {
        player.setInBattle(true);
        player.setLastFight(TimeUtils.Time());

        MapObject map = Manager.mapManager.getMap(attacker.gainMapId());
        attacker = MapUtils.getOwnFighter(map, attacker);


        player.addHatred(attacker, damage);

        if (!(attacker instanceof Player)) {
            return;
        }
        Player somebady = (Player) attacker;

        boolean islog = true;
        if (player.getEnemys().containsKey(somebady.getId())) {
            islog = false;
        }

        AddHatredPlayer(player, somebady);
        AddHatredPlayer(somebady, player);

        boolean isAdd = true;
        //如果我是红名
        if (Manager.currencyManager.manager().getCurrencyIntNum(player, ItemCoinType.GoodEvil) >= PlayerDefine.PkRedName) {
            isAdd = false;
        }
        //如果是对方的自卫列表中有我，是不能加我的自卫列表的
        if (somebady.getPklist().containsKey(player.getId())) {
            isAdd = false;
        }

        //可以加
        if (isAdd) {
            player.getPklist().put(somebady.getId(), TimeUtils.Time());
        }

        if (islog) {
            logger.info(somebady.nameIdString() + " 攻击了" + player.nameIdString() + " 是否增加自卫：" + isAdd);
        }
    }

    @Override
    public void doDie(Pet pet, Fighter attacker) {

        pet.setDie();
        pet.addState(EntityState.Dead);
        Player player = Manager.playerManager.getPlayerCache(pet.getOwnerId());
        //TODO
        MapUtils.sendDead(attacker, pet);
        try {
            Manager.buffManager.deal().onDie(pet);
        } catch (Exception e) {
            logger.error(e, e);
        }
        if (player == null) {
            return;
        }
        Manager.cooldownManager.addCooldown(player, CooldownTypes.PetRelive, null, Global.Pet_res_time);
    }

    @Override
    public void beAttack(Pet pet, Fighter attacker, long damage) {
        if (attacker.getId() == pet.getOwnerId()) {
            return;
        }
        pet.setInBattle(true);
        Player player = Manager.playerManager.getPlayerCache(pet.getOwnerId());
        if (player == null) {
            return;
        }
        beAttack(player, attacker, damage);
    }

    @Override
    public void doDie(Robot robot, Fighter attacker) {
        robot.setDie();
        BehaviorManager.CancelAllBehavior(robot);

        Manager.cooldownManager.cleanAllCooldown(robot);
        Manager.cooldownManager.cleanAllCooldown(robot.pet);

        MapObject map = Manager.mapManager.getMap(robot.gainMapId());

        logger.error("机器人" + robot + "被" + attacker + "杀死了");
        if (map.getId() > 0 && map.getSetting().getIsscript() > 0) {
            try {
                IScript is = Manager.scriptManager.GetScriptClass(map.getSetting().getIsscript());
                if (is instanceof IJJCCloneScript) {
                    ((IJJCCloneScript) is).OnRobotDie(map, robot, attacker);
                }
                is = Manager.scriptManager.GetScriptClass(map.getSetting().getIsscript());
                if (is instanceof INineDaysCloneWar) {
                    ((INineDaysCloneWar) is).OnRobotDie(map, robot, attacker);
                }
                if(is instanceof IRobotFightScript){
                    ((IRobotFightScript) is).OnRobotDie(map, robot, attacker);
                }
            } catch (Exception e) {
                logger.error(e, e);
            }
        }
    }

    public void AddHatredPlayer(Player player, Player otherPlayer) {
        if (otherPlayer == null) {
            return;
        }
        player.getEnemys().put(otherPlayer.getId(), TimeUtils.Time());
        SendAddHatred(player, otherPlayer);
    }

    public void removeHatredPlayer(Player player, long ortherRoleId) {
        player.getEnemys().remove(ortherRoleId);
        PlayerMessage.ResDelHatred.Builder dmsg = PlayerMessage.ResDelHatred.newBuilder();
        dmsg.setPlayerId(ortherRoleId);
        MessageUtils.send_to_player(player, PlayerMessage.ResDelHatred.MsgID.eMsgID_VALUE, dmsg.build().toByteArray());
    }

    private void SendAddHatred(Player player, Fighter attacker) {
        PlayerMessage.ResAddHatred.Builder msg = PlayerMessage.ResAddHatred.newBuilder();
        msg.setPlayerId(attacker.getId());
        MessageUtils.send_to_player(player, PlayerMessage.ResAddHatred.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
