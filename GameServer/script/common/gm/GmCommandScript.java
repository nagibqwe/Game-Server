package common.gm;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_Horse_equip_unlock_Container;
import com.data.container.Cfg_Recharge_daily_total_Container;
import com.data.container.Cfg_State_stifle_Container;
import com.data.script.ScriptConfigManager;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseLongAttribute;
import com.game.backgrand.timer.ServerCloseTimer;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.boss.struct.BossData;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.chum.struct.ChumPrivilege;
import com.game.commercialize.inter.ICommercialize;
import com.game.commercialize.struct.DailyRechargeActivity;
import com.game.commercialize.struct.DailyRechargeStage;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.db.bean.ShopBean;
import com.game.equip.struct.EquipPart;
import com.game.fightserver.manager.FightClientManager;
import com.game.friend.struct.PlayerRelation;
import com.game.gm.script.IGmScript;
import com.game.guild.structs.Guild;
import com.game.guildbattle.structs.GuildBattle;
import com.game.guildbattle.structs.GuildBattleWin;
import com.game.heart.manager.HeartManager;
import com.game.immortalequip.manager.ImmortalEquipManager;
import com.game.immortalequip.structs.ImmortalEquipPart;
import com.game.mail.manager.MailManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.Area;
import com.game.map.structs.MapDefine;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.openserverac.manager.OpenServerAcManager;
import com.game.peak.timer.PeakGmEvent;
import com.game.pet.structs.Pet;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.SavePlayerLevel;
import com.game.ranklist.manager.RankListManager;
import com.game.ranklist.structs.RankType;
import com.game.recharge.structs.Recharge;
import com.game.recharge.structs.RechargeDefine;
import com.game.recharge.structs.RechargeItemInfo;
import com.game.robot.ai.RobotAi;
import com.game.robot.manager.RobotManager;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.server.impl.MapServer;
import com.game.setting.struct.FeedBackInfo;
import com.game.soulanimalforest.manager.SoulAnimalForestCrossManager;
import com.game.structs.*;
import com.game.task.structs.*;
import com.game.team.structs.TeamInfo;
import com.game.title.structs.TitleData;
import com.game.utils.*;
import com.game.welfare.script.IGrowthFundScript;
import com.game.welfare.script.IUpdateNoticeScript;
import com.game.welfare.struct.DayCheckIn;
import com.game.welfare.struct.RetrieveResData;
import com.game.yed.YedMgr;
import common.activity.*;
import game.core.command.CommandProcessor;
import game.core.map.Position;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.util.*;
import game.message.*;
import game.message.CrossServerMessage.G2FGMdeal;
import game.message.CrossServerMessage.P2GGMCMDResult;
import game.message.MapMessage.ResMapPlayer;
import game.message.MapMessage.ResPetDisappear;
import game.message.MapMessage.ResPlayerDisappear;
import game.message.MapMessage.ResRoundObjs;
import game.message.ZoneMessage.ReqEnterZone;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author admin
 */
public class GmCommandScript implements IScript, IGmScript {

    private static final Logger log = LogManager.getLogger(GmCommandScript.class);

    //использованияgmкомандаttвремя，если интервал между последовательными вводами меньше5секунд，то активируются питомец и кавалерия
    private static long useGMttTime = 0;

    private boolean isTaskInit = false;


    @Override
    public int getId() {
        return ScriptEnum.GmComandBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean RunGmCmd(Player player, String str) {
        log.info("GMВызван скрипт GM-команд:" + str);
        try {
            String[] command = str.split(" ");
            if (player != null) { //внутриигроваяGMкоманда
                log.info("roleId:" + player.getId() + " name:" + player.getName() + " внутриигроваяGMкоманда" + str);
                boolean isSuccess = true;
                switch (command[0].toLowerCase()) {
                    case "&redpacket":
                        redpacket(player, command[1], command);
                        break;
                    case "&aliengem":
                        alienGem(player, command[1], command);
                        break;
                    case "&functiontask":
                        functionTask(player, command[1], command);
                        break;
                    case "&wuyou":
                        wuyou(player, command[1], command);
                        break;
                    case "&couple":
                        couple(player, command[1], command);
                        break;
                    case "&nature":
                        nature(player, command[1], command);
                        break;
                    case "&devil":
                        devilSeries(player, command[1], command);
                        break;
                    case "&cleanbagua":
                        cleanbagua(player);
                        break;
                    case "&cleanActivity":
                        Manager.activityManager.deal().cleanActivity(8);
                        break;

                    case "&yytest":
                        String content1 = MessageString.GuildBattleMailContext
                                + "@_@" + "" + "@_@" + "houlao" + "@_@" + 1 + "@_@" + 0 + "@_@" + 0;
                        Manager.mailManager.sendMailToPlayer(player.getId(), MailType.AuctionMail, MessageString.Auction, MessageString.GuildBattleMailTitle, content1);
                        // Manager.playerAttAttributeManager.deal().calPlayerAttributs(player, PlayerAttributeType.Immortalsoul);
                        break;
                    case "&cmd":
                        // JumpGridScript.testBuild();

                        FZTBActivityScript.testBuild();
                        break;
                    case "&actactivereqaward": {
                        Integer reqNum = Integer.parseInt(command[1]);
                        GetActiveActivityScript.testAward(player, reqNum);
                        break;
                    }
                    case "&entercfud": {
                        Integer city = Integer.parseInt(command[1]);
                        Integer type = command.length >= 3 ? Integer.parseInt(command[2]) : 0;
                        GuildCrossFudMessage.ReqCrossFudEnter.Builder et = GuildCrossFudMessage.ReqCrossFudEnter.newBuilder();
                        et.setCityId(city);
                        et.setType(type);
                        Manager.crossFudManager.deal().reqCrossFudEnterHandler(player, et.build());
                    }
                    break;
                    case "&enterpeak":
                        Manager.peakManager.deal().reqEnterPeakMatch(player);
                        break;
                    case "&quitpeak":
                        Manager.peakManager.deal().reqCancelPeakMatch(player);
                        break;
                    case "&openpeak":
                        Manager.peakManager.deal().start();
                        break;
                    case "&closepeak":
                        Manager.peakManager.deal().close();
                        break;
                    case "&resetbossdata":
                        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BossManagerBaseScript);
                        is.call("resetBossData", Integer.valueOf(command[1]), Integer.valueOf(command[2]));
                        break;
                    case "&clearsoulbag":
                        player.getSoulBeastInfo().getSoulBeastEquipMap().clear();
                        Manager.soulBeastManager.deal().online(player);
                        break;
                    case "&clearmoney":
                        player.getCurrencys().clean();
                        player.getGold().setReaminGold(0);
                        Manager.currencyManager.manager().sendItemCoinInfos(player);
                    case "&setlayer":
                        int layer = Integer.parseInt(command[1]);
                        player.getSingleTowerData().setCurLayer(layer);
                        Manager.controlManager.operate(player, FunctionVariable.WanYaoJuanNum, 1);
                        break;
                    case "&setsword":
                        int layer2 = Integer.parseInt(command[1]);
                        player.getFlyswordAllInfo().setSwordSoulLayer(layer2);
                        Manager.controlManager.operate(player, FunctionVariable.Soul_copy_Num, 1);
                    case "&chat":
                        StringBuilder content = new StringBuilder();
                        content.append("2&_" + player.getName());
                        content.append(",");
                        content.append("2&_" + player.getName());
                        MessageUtils.notify_player(player, Notify.CHAT_SYS_MARQUEE, MessageString.LeaderPreachRewardNotice, content.toString());
                        break;
                    case "&dailybegin":
                        dailyBegin(command);
                        //уведомить кросс-сервер
                        G2FGMdeal.Builder toCrossBeginMsg = G2FGMdeal.newBuilder();
                        toCrossBeginMsg.setCmd(command[0].toLowerCase());
                        toCrossBeginMsg.setPara(str);
                        toCrossBeginMsg.setRoleId(player.getId());
                        if (player.playerCrossData.isToFightServer()) {
                            ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, -1, G2FGMdeal.MsgID.eMsgID_VALUE, toCrossBeginMsg.build().toByteArray());
                        }
                        //уведомить публичный сервер
                        Manager.gmCommandManager.sendGMToPublic(player.getId(), str);
                        break;
                    case "&dailyend":
                        dailyEnd(command);
                        //уведомить кросс-сервер
                        G2FGMdeal.Builder toCrossEndMsg = G2FGMdeal.newBuilder();
                        toCrossEndMsg.setCmd(command[0].toLowerCase());
                        toCrossEndMsg.setPara(str);
                        toCrossEndMsg.setRoleId(player.getId());
                        if (player.playerCrossData.isToFightServer()) {
                            ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, -1, G2FGMdeal.MsgID.eMsgID_VALUE, toCrossEndMsg.build().toByteArray());
                        }
                        //уведомить публичный сервер
                        Manager.gmCommandManager.sendGMToPublic(player.getId(), str);
                        break;
                    case "&changejobother": {
                        int career = Integer.parseInt(command[1]);
                        Manager.playerManager.manager().onChangeCareer(player, career);
                        MessageUtils.notify_player(player, Notify.CHAT, "смены профессии GMуспешно принята!");
                    }
                    break;
                    case "&setworldlevel": {
                        // TODO: 2019/5/14  serverParamизменение serverParam отключено, использовать другой способ хранения
                        int level = Integer.parseInt(command[1]);
                        ServerParamUtil.worldLv = level;
                        ServerParamUtil.saveWorldLv();
                        Manager.playerHookManager.deal().worldLvChange();

                        //изменение мирового уровня сервера，уведомить публичный сервер，группировка серверов на публичном сервере использует мировой уровень для определения категории
                        CrossServerMessage.G2PServerWorldLvChange.Builder msg
                                = CrossServerMessage.G2PServerWorldLvChange.newBuilder();
                        msg.setPlat(ServerConfig.getServerPlatform());
                        msg.setServerId(ServerConfig.getServerId());
                        msg.setServerWorldLv(level);
                        MessageUtils.send_to_public(CrossServerMessage.
                                G2PServerWorldLvChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                        MessageUtils.notify_player(player, Notify.CHAT, "Установить мировой уровень " + level + " успешно!");
                    }
                    break;
                    case "&getworldlevel": {
                        MessageUtils.notify_player(player, Notify.CHAT, "мировой уровень:" + GlobalType.getWorldLevel());
                    }
                    break;
                    case "&crossfud": {
                        //уведомить публичный сервер
                        Manager.gmCommandManager.sendGMToPublic(player.getId(), str);
                    }
                    break;
                    case "&addfurniture": {
                        Manager.gmCommandManager.sendGMToPublic(player.getId(), str);
                    }
                    break;
                    case "&checkitem": {
                        if (player == null) {
                            return false;
                        }
                        int modelId = Integer.parseInt(command[1]);
                        //получить предмет, который нужно переместить
                        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(modelId);
                        if (model == null) {
                            return false;
                        }
                        //достигнут ли лимит использования предмета
                        int canuse = model.getDaily_max_use();
                        if (canuse > 0) {
                            log.error(JsonUtils.toJSONString(player.getCounts()));
                            long alreadynum = Manager.countManager.getCount(player, BaseCountType.ITEM_USE, modelId);
                            MessageUtils.notify_player(player, Notify.CHAT, "Текущий предмет：" + modelId + "число использований：" + canuse + " , уже использовано " + alreadynum + " раз;");
                            log.error("Текущий предмет：" + modelId + "число использований：" + canuse + " , уже использовано " + alreadynum + " раз;");
                        } else {
                            MessageUtils.notify_player(player, Notify.CHAT, "Текущий предмет：" + modelId + "число использований：" + canuse);
                            log.error("Текущий предмет：" + modelId + "число использований：" + canuse);
                        }
                    }
                    break;
                    case "&searchid": {
                        try {
                            if (command.length < 2) {
                                return false;
                            }
                            long playerId = Long.parseLong(command[1]);
                            Player on = Manager.playerManager.getPlayerOnline(playerId);
                            if (on != null) {
                                MessageUtils.notify_player(player, Notify.CHAT, command[1] + " игрок онлайн！целевая позиция карта：" + (on.gainMapModelId()) + "линия：" + on.gainLine() + " координаты：" + on.gainCurPos().toString());
                            } else {
                                on = Manager.playerManager.getPlayerCache(playerId);
                                if (on != null) {
                                    MessageUtils.notify_player(player, Notify.CHAT, command[1] + " игрок не в сети！игрок недавно вышел из сети， целевая позиция карта：" + (on.gainMapModelId()) + "линия：" + on.gainLine() + " координаты：" + on.gainCurPos().toString());
                                } else {
                                    MessageUtils.notify_player(player, Notify.CHAT, command[1] + " игрок не в сети！");
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                    break;
                    case "&searchname": {
                        if (command.length < 2) {
                            return false;
                        }
                        List<Player> pl = new ArrayList<>();
                        pl.addAll(Manager.playerManager.getPlayersCache().values());
                        for (Player on : pl) {

                            if (on.getName().equalsIgnoreCase(command[1])) {
                                if (on.getIsOnline() > 0) {
                                    MessageUtils.notify_player(player, Notify.CHAT, command[1] + " игрок онлайн！целевая позиция карта：" + (on.gainMapModelId()) + "линия：" + on.gainLine() + " координаты：" + on.gainCurPos().toString());
                                } else {
                                    MessageUtils.notify_player(player, Notify.CHAT, command[1] + " игрок не в сети！игрок недавно вышел из сети， целевая позиция карта：" + (on.gainMapModelId()) + "линия：" + on.gainLine() + " координаты：" + on.gainCurPos().toString());
                                }
                                return false;
                            }
                        }
                        MessageUtils.notify_player(player, Notify.CHAT, command[1] + " игрок не в сети！");
                    }
                    break;
                    case "&additem":
                        addItem(player, command);
                        break;
                    case "&additems":
                        addItems(player, command);
                        break;
                    case "&addbag":
                        addbag(player, command);
                        break;
                    case "&decitem":
                        decItem(player, command);
                        break;
                    case "&addequips":
                        addEquips(player, command);
                        break;
                    case "&drop":
                        drop(player, command);
                        break;
                    case "&droptn":
                        droptn(player, command);
                        break;
                    case "&addcointest":
                        for (int i = 1; i < 28; i++) {
                            String[] tempCommand = new String[3];
                            tempCommand[0] = "&additem";
                            tempCommand[1] = i + "";
                            tempCommand[2] = 1 + "";
                            addItem(player, tempCommand);
                        }
                        break;
                    case "&querycoin":
                        querycoin(player, command);
                        break;
                    case "&tp":
                        titleprocess(player, command);
                        break;
                    case "&addtitle":
                        addtitle(player, command);
                        break;
                    case "&querytitle":
                        querytitle(player, command);
                        break;
                    case "&setlevel":
                        setLevel(player, command);
                        break;
                    case "&addexp":
                        addExp(player, command);
                        break;
                    case "&bagclearup":
                        Manager.backpackManager.manager().bagClearUp(player, true);
                        break;
                    case "&storeclearup":
                        Manager.storeManager.storeClearUp(player, true);
                        break;
                    case "&setstiflelevel":
                        int level = Integer.valueOf(command[1]);
                        int star = Integer.valueOf(command[2]);
                        int configId = Manager.stateStifleManager.deal().getConfigIdByLevelAndStar(level, star);
                        Cfg_State_stifle_Bean bean = Cfg_State_stifle_Container.GetInstance().getValueByKey(configId);
                        if (bean != null) {
                            player.getStifleData().setLevel(level);
                            Manager.controlManager.operate(player, FunctionVariable.MagicWeapon, 0);
                            player.getStifleData().setStar(star);
                            Manager.stateStifleManager.deal().openPanel(player);
                        }
                        break;
                    case "&close":
                        SessionUtils.closeSession(player.getIosession(), String.format("GM &close roleid:%d userid:%d", player.getId(), player.getUserId()));
                        break;
                    case "&serverclose":
                        GameServer.getInstance().getMainThread().addTimerEvent(new ServerCloseTimer(5, 30));
                        break;
                    case "&addmonster":
                        addMonster(player, command);
                        break;
                    case "&addgroundbuff":
                        addgroundbuff(player, command);
                        break;
                    case "&reloadgamedata":
                        reLoadGameData();
                        break;
                    case "&configreload":
                        Manager.gmCommandManager.sendGMToPublic(player.getId(), str);
                        isSuccess = configReload(command);
                        break;
                    case "&scriptreload":
                        isSuccess = ScriptManager.getInstance().reload(Integer.parseInt(command[1]));
                        break;
                    case "&addbuff":
                        addBuff(player, command);
                        break;
                    case "&attribute": {

                        StringBuilder sb = new StringBuilder();
                        for (Map.Entry<PlayerAttributeType, BaseIntAttribute> entry : player.PlayerCalculators().entrySet()) {
                            sb.append(entry.getKey().getName()).append(":").append(entry.getValue()).append("\n");
                        }
                        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_WORLD, 0, sb.toString(), 0);
                        for (PlayerAttributeType attributeType : PlayerAttributeType.values()) {
                            BaseIntAttribute at = Manager.playerAttAttributeManager.deal().getAttribute(player, attributeType);
                            log.info("Основной атрибут id={} type={} att={}", player.getId(), attributeType, at);
                        }
                        BaseLongAttribute attribute = new BaseLongAttribute(AttributeType.ATTR_MAX);
                        Manager.playerAttAttributeManager.deal().sumAttribute(player, attribute);
                        log.info("Суммарные атрибуты id={} All={}", player.getId(), attribute);
                    }
                    break;
                    case "&removebuff":
                        removeBuff(player, command);
                        break;
                    case "&enterguildmap":
                        enterGuildMap(player);
                        break;
                    case "&entermap":
                        enterMap(player, command);
                        break;
                    case "&enterline":
                        enterLine(player, command);
                        break;
                    case "&setpkvalue":
                        setPkValue(player, command);
                        break;
                    case "&getposinfo":
                        getPosInfo(player, command);
                        break;
                    case "&kill":
                        killRound(player, command);
                        break;
                    case "&killme":
                        killself(player);
                        break;
                    case "&relive":
                        relive(player, command);
                        break;
                    case "&attack":
                        attackRound(player, command);
                        break;
                    case "&cleancd":
                        player.getCooldowns().clear();
                        break;
                    case "&getopenareaday":
                        log.error(TimeUtils.getOpenAreaDay());
                        break;
                    case "&ishide":
                        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "hideme:" + player.isHideMe() + "  hideOther:" + player.isHideOther());
                        break;
                    case "&hideme":
                        hideme(player);
                        break;
                    case "&hideother":
                        hideOther(player);
                        break;
                    case "&getpet":
                        getPet(player, Integer.parseInt(command[1]));
                        break;
                    case "&addskill": {
                        int skillId = Integer.parseInt(command[1]);
                        Cfg_Skill_Bean config = CfgManager.getCfg_Skill_Container().getValueByKey(skillId);
                        if (config != null) {
                            Manager.skillManager.addSkill(player, skillId);
                        }
                    }
                    break;
                    case "&removeskill": {
                        int skillId = Integer.parseInt(command[1]);
                        Manager.skillManager.removeSkill(player, skillId);
                    }
                    break;
                    case "&openjjc":
                        Manager.jjcManager.deal().OnReqOpenJJC(player, null);
                        break;
                    case "&challengejjc":
                        JJCMessage.ReqChallenge.Builder mess = JJCMessage.ReqChallenge.newBuilder();
                        mess.setTargetID(Long.parseLong(command[1]));
                        Manager.jjcManager.deal().OnReqChallenge(player, mess.build());
                        break;
                    case "&robot":
                        Robot robot = RobotManager.getInstance().deal().OnMake(player);
                        robot.setAi(RobotAi.JJC);
                        Manager.mapManager.manager().onEnterMap(robot);
                        break;
                    case "&reloadai":
                        YedMgr.getInstance().ReLoad();
                        break;
                    case "&curmap":
                        MapObject curmap = Manager.mapManager.getMap(player.gainMapId());

                        int rands = MapUtils.getRoundPlayer(curmap, player.gainCurPos()).size();

                        int lines = Manager.mapManager.getWorldMaps().get(curmap.getMapModelId()).size();
                        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "Игроков на текущей карте：" + curmap.getPlayers().size() + " текущийline:" + player.gainLine() + ";игроков вокруг：" + rands + ";всего линий：" + lines
                                + " ;общее число монстров на текущей карте：" + curmap.getMonsters().size());
                        break;
                    case "&maplines":
                        maplines(player, command);

                        break;
                    case "&addgather":
                        addGather(player, command);
                        break;
                    case "&fuckme":
                        MapObject map = Manager.mapManager.getMap(player.gainMapId());
                        List<Monster> monsters = MapUtils.getRoundMonster(map, player.gainCurPos());
                        for (Monster monster : monsters) {
                            monster.addHatred(player, 1);
                        }
                        break;
                    case "&tomorrow":
                        if (command.length == 2) {
                            float input = Float.parseFloat(command[1]);
                            if (input < 0) {
                                input = 0;
                            }
                            int hour = (int) input;
                            int min = (int) (input * 100) % 100 % 60;
                            TimeUtils.setTime(TimeUtils.getTodayBeginTime() + (24 + hour) * 3600 * 1000 + min * 60 * 1000);
                        } else {
                            TimeUtils.setTime(TimeUtils.Time() + 24 * 3600 * 1000);
                        }
                        break;
                    case "&getcity": //захватить город
                        getcity(player, Integer.parseInt(command[1]));
                        break;
                    case "&skiptime":
                        int skip = 0;
                        if (command[1].contains("*")) {
                            String[] args = command[1].split("\\*");
                            int temp = 1;
                            for (String ar : args) {
                                temp *= Integer.parseInt(ar);
                            }
                            skip = temp == 1 ? 0 : temp;
                        } else {
                            skip = Integer.parseInt(command[1]);
                        }
                        if (skip > 0) {
                            TimeUtils.setTime(TimeUtils.Time() + skip * 60 * 1000);
                            String cmdstr = TimeUtils.format2string(TimeUtils.Time(), "yyyy-MM-dd_HH:mm:ss");
                            cmdstr = "&settime " + cmdstr;
                            sendTpWorldScript(player, cmdstr);
                        }
                        MessageUtils.notify_player(player, Notify.CHAT, "Текущее время сервера：" + TimeUtils.format2string(TimeUtils.Time()));
                        break;
                    case "&entercross"://войти на кросс-сервер
                        entercross(player, command);
                        break;
                    case "&findaround": //переместиться к ближайшему игроку
                        findaround(player, command);
                        break;
                    case "&reloadmap":  //перезагрузить конфигурацию карты
                        if (command.length == 2) {
                            int mapID = Integer.parseInt(command[1]);
                            Manager.mapCfgManager.reloadMap(mapID);
                        } else {
                            Manager.mapCfgManager.reloadMaps();
                        }
                        break;
                    case "&activecode":
                        Manager.activeCodeManager.useActiveCode(player, command[1]);
                        break;
                    case "&setattribute":
                        setAttribute(player, command);
                        break;
                    case "&addatt":
                        if (!ServerConfig.isTestServer()) {
                            return false;
                        }
                        addAttribute(player, command);
                        break;
                    case "&changejob":
                        int level_changeJob = Integer.parseInt(command[1]);
                        changeJob(player, level_changeJob);
                        break;
                    case "&anytask":
                        anyTask(player, command);
                        break;
                    case "&overtask":
                        newFinishTask(player, command);
                        break;
                    case "&savebossdierecord":
                        Manager.bossManager.saveBossDieRecord();
                        break;
                    case "&clearbackpack":
                        player.getBackpackItems().clear();
                        break;
                    case "&invitedotheraround":
                        invitedOtherAround(player, command);
                        break;
                    case "&getmaplines":
                        getMapLines(player, command);
                        break;
                    case "&lucky":
                        lucky(player, command);
                        break;
                    case "&addhorselayer":
                        int addLayer = Integer.parseInt(command[1]);
                        if (addLayer <= 0) {
                            log.error("addLayerошибка данных！");
                            break;
                        }
                        addHorseLayer(player, addLayer);
                        break;
                    case "&sethorsestar":
                        int illusionLevel = Integer.parseInt(command[1]);
                        if (illusionLevel <= 0) {
                            log.error("illusionLevelошибка данных！");
                            break;
                        }
                        setHorseStarLv(player, illusionLevel);
                        break;
                    case "&tt":
                        tt(player);
                        break;
                    case "&refreshshop":
                        Manager.shopManager.reset();
                        break;
                    case "&showshop":
                        showshop(player, command);
                        break;
                    case "&me":
                        sendMeInfo(player);
                        break;
                    case "&sendmail": //&sendmail mailTitle mailContent mailAttach (ex: sendMail Награда за убийство монстра Ха-ха，поздравляем, монстр убит хозяином，однако хозяин действительно беспощаден。 1020,10,1;5001,5,1;50001,1,1)
                        sendMailToPlayer(player, command);
                        break;

                    case "&mail": //&sendmail mailTitle mailContent mailAttach (ex: sendMail Награда за убийство монстра Ха-ха，поздравляем, монстр убит хозяином，однако хозяин действительно беспощаден。 1020,10,1;5001,5,1;50001,1,1)
                        sendMailToPlayer1(player, command);
                        break;
                    case "&settime":
                        if (!ServerConfig.isTestServer()) {
                            return false;
                        }
                        if (setTime(command)) {
                            sendTpWorldScript(player, str);
                        }
                        break;

                    case "&createguild":
                        if (player.getLevel() < 100) {
                            String[] levelCMD = {"&setlevel", "100"};
                            setLevel(player, levelCMD);
                        }
                        if (player.getVipLv() < Global.Create_Guild_Vip_Limit) {
                            Cfg_Vip_Bean bean1 = CfgManager.getCfg_Vip_Container().getValueByIndex(Global.Create_Guild_Vip_Limit);
                            Manager.vipManager.deal().addVipExp(player, bean1.getVipLevelUp(), ItemChangeReason.GM, IDConfigUtil.getLogId());
                        }
                        Manager.currencyManager.manager().onAddItemCoin(player, Global.GuildCreateMoney.get(0), Global.GuildCreateMoney.get(1), ItemChangeReason.GM, 1);
                        Item vipItem = Item.createItem(1373, 1, false);
                        Manager.backpackManager.manager().addItem(player, vipItem, ItemChangeReason.GM, 1);
                        Manager.vipManager.pearl().activeVipPearl(player, 1);
                        Manager.guildsManager.manager().createGuild(player, command[1], 1, "1111");
                        break;
                    case "&gettime": {
                        //int num = TimeUtils.getOpenAreaDay();
                        int num = TimeUtils.getOpenServerDay();
                        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "Текущее число дней с открытия сервера：" + num + " дней,время открытия сервера" + ServerConfig.getServerOpenTime() + ",текущее системное время:" + TimeUtils.NowToString() + ",текущее системное время в миллисекундах:" + TimeUtils.Time());
                    }
                    break;
                    case "&sethp":
                        if (!player.isDie()) {
                            player.setCurHp(Integer.parseInt(command[1]));
                            player.onHpChange(null);
                        }
                        break;
                    case "&flushshengmi":
                        break;
                    //очистить количество входов в подземелье
                    case "&clearclonetime": {
                        if (!ServerConfig.isTestServer()) {
                            return false;
                        }
                        List<Integer> overscript = new ArrayList<>();
                        //запись быстрого прохождения
                        Manager.countManager.setVariant(player, VariantType.Copymap_Challenge_RaidLevel, 0);
                        MessageUtils.notify_player(player, Notify.CHAT, "Количество очищенных флагов подземелья：" + overscript.size() + "！");
                    }
                    break;
                    case "&clearshengmi":
                        break;
                    case "&num":
                        String s1 = "Игроков онлайн:" + Manager.playerManager.getOnLinePlayerNum()
                                + " Игроков в кэше：" + Manager.playerManager.getPlayersCache().size();
                        MessageUtils.notify_player(player, Notify.CHAT, MessageString.WANGNENGTISHI, s1);
                        System.out.println(s1);
                        break;
                    case "&time":
                        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(TimeUtils.Time()));
//                        Manager.chatManager.deal().sendSystemStrToPlayer(player, "Текущее время сервера：" + date);
                        MessageUtils.notify_player(player, Notify.CHAT, MessageString.WANGNENGTISHI, "Текущее время сервера：" + date);
                        break;
                    case "&opstime"://просмотреть время открытия сервера
                        MessageUtils.notify_player(player, Notify.CHAT, MessageString.WANGNENGTISHI, "Текущее время открытия сервера：" + ServerConfig.getServerOpenTime());
                        break;
                    case "&setopstime"://установить время открытия сервера
                    {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            String newOpsTime = sdf.format(new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").parse(command[1].trim()));
                            ServerConfig.setOpenTime(newOpsTime);
                            Manager.biManager.getScript().BiServer_op(103);
                            ServerParamUtil.serverOpenTime = newOpsTime;
                            ServerParamUtil.saveServerOpenTime();

                            CrossServerMessage.G2PServerOpentimeChange.Builder msg = CrossServerMessage.G2PServerOpentimeChange.newBuilder();
                            msg.setServerId(ServerConfig.getServerId());
                            msg.addAllServerIdsList(ServerConfig.getServerIdList());
                            msg.setServeropentime(newOpsTime);
                            msg.setPlat(ServerConfig.getServerPlatform());
                            MessageUtils.send_to_public(CrossServerMessage.G2PServerOpentimeChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());

                        } catch (Exception e) {
                            log.error(e.getMessage());
                            MessageUtils.notify_player(player, Notify.CHAT, MessageString.WANGNENGTISHI, "Неверный формат времени，правильный пример:2017-05-25_11:00:00");
                        }

                        MessageUtils.notify_player(player, Notify.CHAT, MessageString.WANGNENGTISHI, "Текущее время открытия сервера：" + ServerConfig.getServerOpenTime());
                        Manager.functionTaskManager.getScript().init();
                        Manager.functionTaskManager.getScript().online(player);
                    }
                    break;
                    case "&setopstimenow": {
                        long now = System.currentTimeMillis();
                        String newOpsTime = TimeUtils.format2string(now);
                        try {
                            ServerConfig.setOpenTime(newOpsTime);
                            Manager.biManager.getScript().BiServer_op(103);
                            ServerParamUtil.serverOpenTime = newOpsTime;
                            ServerParamUtil.saveServerOpenTime();
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            MessageUtils.notify_player(player, Notify.CHAT, MessageString.WANGNENGTISHI, "Неверный формат времени，правильный пример:2017-05-25_11:00:00");
                        }
                        MessageUtils.notify_player(player, Notify.CHAT, MessageString.WANGNENGTISHI, "Текущее время открытия сервера：" + ServerConfig.getServerOpenTime());
                    }
                    break;
                    case "&queryrecharge":
                        queryrecharge(player, command);
                        break;
                    case "&recharge":
                        recharge(player, command);
                        break;
                    case "&rechargeid":
                        rechargeId(player, command);
                        break;
                    case "&business":
                        business(player, command);
                        break;
                    case "&queryitem":
                        queryitem(player, command);
                        break;
                    case "&any":
                        any(player, command);
                        break;
                    case "&chum":
                        chum(player, command);
                        break;
                    case "&lp":
                        lp(player, command);
                    case "&bi":
                        bi(player, command);
                        break;
                    case "&rr":
                        // восстановление ресурсов
                        rr(player, command);
                        break;
                    case "&testfirstkill":
                        int testfirstkillvalue = Integer.parseInt(command[1]);
                        BackendMessage.ResFirstKillOpenState.Builder builder = BackendMessage.ResFirstKillOpenState.newBuilder();
                        builder.setIsOpen(testfirstkillvalue > 0);
                        MessageUtils.send_to_player(player, BackendMessage.ResFirstKillOpenState.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                        break;
                    case "&hatrednum":
                        hatrednum(player, command);
                        break;
                    case "&addguildexp":
                        addGuildExp(player, command);
                        break;
                    case "&other":
                        other(player, command);
                        break;
                    case "&cell":
                        cell(player, command);
                        break;
                    case "&gm":
                        boolean isGm = (command.length == 1);
                        player.setGM(isGm);
                        break;
                    case "&clearcount":
                        if (!ServerConfig.isTestServer()) {
                            return false;
                        }
                        player.getCounts().clear();
                        player.getDailyActiveData().getDailyProgress().clear();
                        Manager.marriageManager.manager().online(player);
                        break;
                    case "&cleanservercount":
                        ServerParamUtil.counts.clear();
                        break;
                    case "&marry":
                        String name = command[1];
                        Player target = Utils.findOne(Manager.playerManager.getPlayersCache().values(), p -> p.getName().equals(name));
                        Manager.friendManager.deal().addFriend(player, target.getId());
                        Manager.friendManager.deal().addFriend(target, player.getId());
                        Manager.friendManager.deal().addIntimacy(player, target.getId(), 500);
                        Manager.backpackManager.manager().addItem(target, 1, 1314, true, 0, ItemChangeReason.GMGet, -1);
                        Manager.marriageManager.manager().reqMarriage(target, player.getId(), 2, false, "helloworld");
                        Manager.marriageManager.manager().reqBeMarriage(player, target.getId(), true);
                        break;
                    case "&clearquittime":
                        player.setQuitGuildTime(0);
                        break;
                    case "&reloadactivity":
                        Manager.activityManager.deal().load();
                        break;
                    case "&isforbid":
                        HeartManager.isForbid = (command.length > 2);
                        break;
                    case "&setchanneclinfo": {
                        GlobalType.isOutMoreRead = true;
                        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, str + "Подробный вывод соединений успешно включён!！", 0);
                    }
                    break;
                    case "&closechannelinfo": {
                        GlobalType.isOutMoreRead = false;
                        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, str + "Подробный вывод соединений успешно отключён!！", 0);
                    }
                    break;
                    case "&opennetstream":
                        GlobalType.isOutNetStreamInfo = true;
                        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, str + "Статистика сетевого трафика успешно включена!！", 0);
                        break;
                    case "&closenetstream":
                        GlobalType.isOutNetStreamInfo = false;
                        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, str + "Статистика сетевого трафика успешно отключена!！", 0);
                        break;
                    case "&setout": {
                        GlobalType.isOutNetMess = true;
                        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, str + "Вывод сетевой информации успешно включён!！", 0);
                    }
                    break;
                    case "&closeout": {
                        GlobalType.isOutNetMess = false;
                        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, str + "Вывод сетевой информации успешно отключён!！", 0);
                    }
                    break;
                    case "&addqinmi": {
                        int add = Integer.parseInt(command[1]);
                        addIntimacy(player, add);
                    }
                    break;
                    case "&cleancard": {
                        cleanCard(command, player);
                    }
                    break;
                    case "&recoveractivity": //&recoveractivity beginActId_endActId
                        String[] actArr = command[1].split(Symbol.XIAHUAXIAN_REG);
                        int beginActId = Integer.parseInt(actArr[0]);
                        int endActId = Integer.parseInt(actArr[1]);
                        for (int i = beginActId; i <= endActId; i++) {
                            Manager.activityManager.getConfigDao().recoverActivity(i);
                        }
                        break;
                    case "&shopid": {
                        int id = Integer.parseInt(command[1]);
                        Cfg_Shop_Maket_Bean csb = CfgManager.getCfg_Shop_Maket_Container().getValueByKey(id);
                        if (csb == null) {
                            MessageUtils.notify_player(player, Notify.CHAT, "Такого товара нет");
                        } else {
                            String itemName = Manager.backpackManager.manager().getName(csb.getItemID());
                            String coinName = Manager.backpackManager.manager().getName(csb.getCurrencyID());
                            log.error("Магазинid=" + id + " продаёт " + itemName + "(" + csb.getItemID() + ")" + coinName + "(" + csb.getCurrencyID() + ")" + csb.getPrice() + " шт.");
                            MessageUtils.notify_player(player, Notify.CHAT, "Магазинid=" + id + " продаёт " + itemName + "(" + csb.getItemID() + ")" + coinName + "(" + csb.getCurrencyID() + ")" + csb.getPrice() + " шт.");
                        }
                    }
                    break;
                    //тест синхронизации данных игрока текущего запроса
                    case "&testfight": {
                        CrossFightMessage.roleAtt.Builder ratt = CrossFightMessage.roleAtt.newBuilder();
                        ratt.setRoleId(player.getId());
                        Manager.playerManager.managerExt().OnSynPlayerInfoToFight(1900, player, 1, 1000, ratt.build(),
                                1, 0, new ArrayList<>(), false);
                    }
                    break;
                    case "&outfight":
                        if (player == null) {
                            isSuccess = false;
                        } else {
                            player.playerCrossData.setToFightServer(false);
                            player.playerCrossData.toFightId = 0;
                            player.playerCrossData.toFightSid = 0;
                            player.playerCrossData.toZoneModelId = 0;
                            player.playerCrossData.isReqFight = false;
                            player.playerCrossData.crossState = CrossState.PCS_LOCAL;
                            //сменить карту
                            MessageUtils.notify_player(player, Notify.SHOWBOX, "Сервер кросс-серверной битвы отключён，пожалуйста, войдите в активность заново！");
                            //вернуться на исходную карту
                            Manager.copyMapManager.outZone(player);
                        }
                        break;
                    case "&setpublictime": {
                        Manager.gmCommandManager.sendGMToPublic(player.getId(), str);
                        setTime(command);
                        G2FGMdeal.Builder mm = G2FGMdeal.newBuilder();
                        mm.setCmd("&setfighttime");
                        mm.setPara(str);
                        mm.setRoleId(player.getId());
                        ConnectFightManager.GetInstance().send_to_allFight(G2FGMdeal.MsgID.eMsgID_VALUE, mm.build().toByteArray());
                    }
                    break;
                    case "&addpeakscore": {
                        int score = Integer.parseInt(command[1]);
                        Manager.peakManager.addCommand(new PeakGmEvent(player.getId(), score));
                        Manager.gmCommandManager.sendGMToPublic(player.getId(), str);
                    }
                    case "&entercopymap":
                        enterCopyMap(player, command);
                        break;
                    case "&quitcopymap":
                        quitCopyMap(player);
                        break;
                    case "&getpublictime": {
                        Manager.gmCommandManager.sendGMToPublic(player.getId(), str);
                        int num = TimeUtils.getOpenAreaDay();
                        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "Текущее число дней с открытия сервера：" + num + " дней,время открытия сервера" + ServerConfig.getServerOpenTime() + ",текущее системное время:" + TimeUtils.NowToString() + ",текущее системное время в миллисекундах:" + TimeUtils.Time());
                        G2FGMdeal.Builder mm = G2FGMdeal.newBuilder();
                        mm.setCmd("&getfighttime");
                        mm.setPara(str);
                        mm.setRoleId(player.getId());
                        ConnectFightManager.GetInstance().send_to_allFight(G2FGMdeal.MsgID.eMsgID_VALUE, mm.build().toByteArray());
                    }
                    break;
                    case "&publicscript":
                    case "&publicreload":
                        Manager.gmCommandManager.sendGMToPublic(player.getId(), str);
                        break;
                    case "&setfighttime":
                    case "&fightscript":
                    case "&fightreload":
                    case "&getfighttime": {
                        G2FGMdeal.Builder mm = G2FGMdeal.newBuilder();
                        mm.setCmd(command[0].toLowerCase());
                        mm.setPara(str);
                        mm.setRoleId(player.getId());
                        if (player.playerCrossData.isToFightServer()) {
                            ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, -1, G2FGMdeal.MsgID.eMsgID_VALUE, mm.build().toByteArray());
                        } else {
                            MessageUtils.notify_player(player, Notify.CHAT, "Сейчас не на кросс-сервере！");
                        }
                    }
                    break;
                    case "&setfsidtime":
                    case "&getfsidtime": {
                        G2FGMdeal.Builder mm = G2FGMdeal.newBuilder();
                        mm.setCmd(command[0].toLowerCase());
                        mm.setPara(str);
                        mm.setRoleId(player.getId());
                        ConnectFightManager.GetInstance().send_to_allFight(G2FGMdeal.MsgID.eMsgID_VALUE, mm.build().toByteArray());
                    }
                    break;
                    case "&highnotify":
                        MessageUtils.notify_allOnlinePlayer(Notify.HIGH_ORDER_NOTIFY, MessageString.C_KP_FINDGUILD_ERRNULL);
                        break;
                    case "&addequip":
//                        &addequip id lv pz zz1T_zz1V,zz2T_zz2V,zz3T_zz3V sid1,sid2,sid3 add1T_add1V,add2T_add2V,add3T_add3V,bind
                        isSuccess = addAppointEquip(player, command);
                        break;
                    case "&clearmybag": {
                        //очистить мой рюкзак
                        isSuccess = clearmybag(player);
                    }
                    break;
                    case "&clearmystore": {
                        //очистить моё хранилище
                        isSuccess = clearmystore(player);
                    }
                    break;
                    case "&cleanbag":
                        //игрокId
                        long roleId = Long.parseLong(command[1]);
                        if (Manager.playerManager.isOnline(roleId)) {
                            Player player1 = Manager.playerManager.getPlayerCache(roleId);
                            boolean isS = cleanBag(player1.getBackpackItems());
                            if (isS) {
                                log.error("Очистка данных успешна  roleId：" + roleId);
                                isSuccess = true;
                            } else {
                                isSuccess = false;
                                log.error("Очистка данных не удалась  roleId：" + roleId);
                            }
                        }
                        break;
                    case "&cleargiftbag":
                        break;
                    case "&addactive": {
                        int dailyId = Integer.valueOf(command[1]);
                        int count = Integer.valueOf(command[2]);
                        DailyActiveDefine daily = DailyActiveDefine.find(dailyId);
                        Manager.dailyActiveManager.deal().addDailyProgress(player, daily, count);
                        isSuccess = true;
                    }
                    break;
                    //для робота добавить одним нажатием все предметы на персонаже
                    case "&onekeyatt":
                        oneKeyAddPlayerAtt(player);
                        break;
                    case "&holiday":
                        holidayAction(player, command);
                        break;
                    case "&share":
                        shareAction(player, command);
                        break;
                    case "&resetdreamboss":
                        int bossId = Integer.parseInt(command[1]);
                        BossData data = ServerParamUtil.BossDataMap.get(bossId);
                        if (data != null) {
                            data.setDieNum(0);
                            ServerParamUtil.saveWorldBoss();
                        } else {
                            log.error("GMмонстр, указанный в GM-команде, не найден，bossId=" + bossId);
                        }
                        break;

                    case "&upmorale": //"&upmorale num" сколько раз выполнить усиление за золото
                        int upNum = Integer.parseInt(command[1]);
                        for (int i = 0; i < upNum; ++i) {
                            Manager.copyMapManager.logic().onReqUpMorale(player, 0);
                        }
                        break;
                    case "&clearequipskill": {
//                        long playerId = Long.parseLong(command[1]);
//                        Player on = manager.playerManager.getOnLinePlayer(playerId);
//                        if (player == null) {
//                            log.error("Начинается очистка книг навыков игрока игрок не в сети：");
//                            isSuccess = false;
//                            break;
//                        }
                        HashMap<Long, List<Integer>> playerList = new HashMap<>();
                        List<Integer> skillList = new ArrayList<>();
                        skillList.add(10073);
                        skillList.add(10074);
                        skillList.add(10075);
                        skillList.add(10078);
                        skillList.add(10079);
                        skillList.add(10093);
                        skillList.add(10094);
                        skillList.add(10095);
                        skillList.add(10096);
                        skillList.add(10099);
                        skillList.add(10100);
                        skillList.add(10101);
                        skillList.add(10102);
                        skillList.add(10104);
                        skillList.add(10105);
                        skillList.add(10071);
                        playerList.put(4612067932507611717L, skillList);
                        List<Integer> skillList1 = new ArrayList<>();
                        skillList1.add(10071);
                        playerList.put(4612067933183639244L, skillList1);

                        List<Integer> skillList2 = new ArrayList<>();
                        skillList2.add(10071);
                        skillList2.add(10081);
                        skillList2.add(10086);
                        skillList2.add(10091);
                        playerList.put(4612067934730952541L, skillList2);

                        List<Integer> skillList7 = new ArrayList<>();
                        skillList7.add(10081);
                        playerList.put(461206793474867994L, skillList7);

                        List<Integer> skillList3 = new ArrayList<>();
                        skillList3.add(10071);
                        skillList3.add(10073);
                        skillList3.add(10075);
                        skillList3.add(10079);
                        skillList3.add(10080);
                        skillList3.add(10081);
                        skillList3.add(10083);
                        skillList3.add(10085);
                        skillList3.add(10086);
                        skillList3.add(10089);
                        skillList3.add(10093);
                        skillList3.add(10095);
                        skillList3.add(10096);
                        skillList3.add(10100);
                        skillList3.add(10101);
                        skillList3.add(10105);
                        playerList.put(4612067937759630172L, skillList3);

                        List<Integer> skillList4 = new ArrayList<>();
                        skillList4.add(10071);
                        playerList.put(4612349413321475544L, skillList4);

                        List<Integer> skillList5 = new ArrayList<>();
                        skillList5.add(10071);
                        playerList.put(4612630893937760264L, skillList5);

                        List<Integer> skillList6 = new ArrayList<>();
                        skillList6.add(10081);
                        skillList6.add(10086);
                        playerList.put(4612630895292642373L, skillList6);

                        List<Integer> skillList8 = new ArrayList<>();
                        skillList8.add(10102);
                        playerList.put(689713816061020578L, skillList8);

                        if (!playerList.containsKey(player.getId())) {
                            log.error("Начинается очистка книг навыков игрока:нет данных очистки для этого игрока");
                            return false;
                        }

                        List<Integer> skillLists = playerList.get(player.getId());

//                int minId = 10001;
//                int maxId = 10105;
                        //получить рюкзак игрока
                        Iterator<Entry<Integer, Item>> it = player.getBackpackItems().entrySet().iterator();
                        while (it.hasNext()) {
                            Entry<Integer, Item> entry = it.next();
                            Item item = entry.getValue();
                            if (entry.getValue() instanceof Equip) {
                                Equip e = (Equip) item;
//                                if (e.getSids().isEmpty()) {
//                                    continue;
//                                }
//                                Iterator<Integer> its1 = e.getSids().iterator();
//                                while (its1.hasNext()) {
//                                    int sid = its1.next();
//                                    if (skillLists.contains(sid)) {
//                                        log.error("Начинается очистка книг навыков игрока удалить навык с экипировки в рюкзаке：" + sid);
//                                        its1.remove();
//                                    }
//                                }
                            } else if (skillLists.contains(item.getItemModelId())) {
                                log.error("Начинается очистка книг навыков игрока удалить книгу навыка из рюкзака：" + item.getItemModelId() + " количество：" + item.realNum());
                                it.remove();
                            }
                        }
                        //получить хранилище игрока
                        Iterator<Entry<Integer, Item>> it1 = player.getStoreItems().entrySet().iterator();
                        while (it1.hasNext()) {
                            Entry<Integer, Item> entry = it1.next();
                            Item item = entry.getValue();
                            if (entry.getValue() instanceof Equip) {
                                Equip e = (Equip) item;
//                                if (e.getSids().isEmpty()) {
//                                    continue;
//                                }
//                                Iterator<Integer> its2 = e.getSids().iterator();
//                                while (its2.hasNext()) {
//                                    int sid = its2.next();
//                                    if (skillLists.contains(sid)) {
//                                        log.error("Начинается очистка книг навыков игрока удалить навык с экипировки в рюкзаке：" + sid);
//                                        its2.remove();
//                                    }
//                                }
                            } else if (skillLists.contains(item.getItemModelId())) {
                                log.error("Начинается очистка книг навыков игрока удалить книгу навыка из рюкзака：" + item.getItemModelId() + " количество：" + item.realNum());
                                it1.remove();
                            }
                        }
                        break;
                    }
                    case "&sethook":
                        int time = Integer.parseInt(command[1]);
                        player.getHookInfo().setHookTime(time * 60);
                        break;
                    case "&openall":
                        isSuccess = openAll(player);
                        break;
                    case "&resetfudi":
                        Manager.guildActivityManager.deal().rank(1, 61 * 60 * 1000, true);
                        isSuccess = true;
                        break;
                    case "&fudi":
                        if (ServerParamCommon.isFuDiForeverTitle())
                            MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "Рейтинг благословенной земли уже завершён");
                        else
                            MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "Рейтинг благословенной земли ещё не завершён");
                        break;
                    case "&fudipower":
                        isSuccess = fudipower(player, command);
                        break;
                    case "&setstatevip":
                        isSuccess = setStateVip(player, command);
                        break;
                    case "&getstatevip":
                        MessageUtils.notify_player(player, Notify.CHAT, "Ступень:" + player.getStateVip().getLv());
                        break;
                    case "&activeflysword":
                        Manager.huaxinFlySwordManager.deal().onReqUseHuxin(player, Integer.parseInt(command[1]), 1, false);
                        break;
                    case "&feedback":
                        isSuccess = sendFeedBack(command);
                        break;
                    case "&rank":
                        Manager.rankListManager.deal().sortAllRank();
                        isSuccess = true;
                        break;
                    case "&getrank":
                        getRank(player, command);
                        isSuccess = true;
                        break;
                    case "&jjcrank":
                        ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(RankType.ARENA_RANK);
                        Manager.jjcManager.deal().sendArenaRankReward(rankMap);
                        isSuccess = true;
                        break;
                    case "&exprate":
                        for (Map.Entry<PlayerAttributeType, BaseIntAttribute> entry : player.PlayerCalculators().entrySet()) {
                            int rate = entry.getValue().getAdditionValue(AttributeType.ATTR_MonserExp);
                            if (entry.getValue().getAdditionValue(AttributeType.ATTR_MonserExp) != 0) {
                                log.info(String.format("Система：%s бонус：%s", entry.getKey(), rate));
                            }
                        }
                        float expRate = player.expRateNotHaveAtt();
                        String tips = String.format("buff: %s, прочее：%s", expRate * 100, (player.gainExpRate() - expRate) * 100);
                        log.info(tips);
                        MessageUtils.notify_player(player, Notify.CHAT, tips);
                        isSuccess = true;
                        break;
                    case "&addsoul":
                        int soulID = Integer.parseInt(command[1]);
                        Manager.immortalSoulManager.gmAddSoul(player, soulID, true, 0);
                        break;

                    case "&worldanswerready":
                        Manager.gmCommandManager.sendGMToPublic(player.getId(), str);
                        Manager.scriptManager.GetScriptClass(ScriptEnum.AnswerQuestionsBaseScript).call(1, "activityChangeCallBack");
                    case "&worldanswerstart":
                        Manager.gmCommandManager.sendGMToPublic(player.getId(), str);
                        Manager.scriptManager.GetScriptClass(ScriptEnum.AnswerQuestionsBaseScript).call(2, "activityChangeCallBack");
                        break;
                    case "&worldanswerover":
                        Manager.gmCommandManager.sendGMToPublic(player.getId(), str);
                        Manager.scriptManager.GetScriptClass(ScriptEnum.AnswerQuestionsBaseScript).call(0, "activityChangeCallBack");
                        break;
                    case "&useitem":
                        Item item = Manager.backpackManager.manager().getItemByModelId(player, Integer.parseInt(command[1]));
                        if (item != null) {
                            Manager.backpackManager.manager().useItem(player, item.getId(), Integer.parseInt(command[2]), 0);
                        }
                        isSuccess = true;
                        break;
                    case "&robothelp":
                        OnRobotBattle(player);
                        break;
                    case "&auctoin":
                        auctoinActivePut(player);
                        break;
                    case "&auction":
                        auction(player, command);
                        break;
                    case "&clearxianjia":
                        Manager.treasureHuntXianjiaManager.deal().gmClearXianjiaData(player);
                        break;
                    case "&setmibao":
                        Manager.treasureHuntXianjiaManager.deal().gmSetMibao(player);
                        break;
                    case "&enterweddingmap":
                        Manager.copyMapManager.manager().onReqCopyMapEnter(player, 3001, 0);
                        break;
                    case "&moveto":
                        moveTo(player, command);
                        break;
                    case "&active"://установить значение активности
                        if (command.length < 2) {
                            MessageUtils.notify_player(player, Notify.NORMAL, "Текущая полученная активность:" + Manager.countManager.getVariant(player, VariantType.DailyActivePoint));
                            MessageUtils.notify_player(player, Notify.NORMAL, "Текущий расход активности:" + Manager.countManager.getVariant(player, VariantType.DailyActivePointCost));
                            break;
                        }
                        int type = Integer.parseInt(command[1]);
                        int value = Integer.parseInt(command[2]);
                        if (type == 1) {
                            Manager.countManager.setVariant(player, VariantType.DailyActivePoint, value);
                            Manager.controlManager.operate(player, FunctionVariable.CurDayActiveValue, value);
                        } else if (type == 2) {
                            Manager.countManager.setVariant(player, VariantType.DailyActivePointCost, value);
                            Manager.controlManager.operate(player, FunctionVariable.CurDayActiveValueCos, 0);
                        }
                        break;
                    case "&resetguildtask":
                        player.getGuildTaskPool().setRefreshCount(0);
                        player.getConquerTaskCount().put(2, 0);
                        ConquerTask conquerTask = player.getCurConquerTasks().get(2);
                        player.getGuildTaskPool().getTaskIds().clear();
                        if (conquerTask != null && conquerTask.getModelId() > 0) {
                            player.getGuildTaskPool().getTaskIds().add(conquerTask.getModelId());
                        }
                        Manager.taskManager.guild().refreshGuildTaskPool(player);
                        Manager.taskManager.guild().sendResGuildTaskPool(player);
                        break;
                    case "&joindaily": {
                        int dailyId = Integer.parseInt(command[1]);
                        int cloneId = Integer.parseInt(command[2]);
                        Manager.dailyActiveManager.deal().joinDailyActive(player, dailyId, cloneId, true);
                    }
                    break;
                    case "&guildbattlerate":
                        Manager.guildBattleManager.manager().guildBattleRate();
                        break;
                    case "&guildbattlebegin":
                        Manager.guildBattleManager.manager().guildBattleBegin(72001);
                        break;
                    case "&guildbattleenter":
                        Manager.dailyActiveManager.deal().joinDailyActive(player, 110, 0, true);
                        break;
                    case "&guildbattleend":
                        Manager.guildBattleManager.manager().guildBattleEnd();
                        break;
                    case "&guildbattleopen":
                        MapObject mm = Manager.mapManager.getMap(player.gainMapId());
                        MessageUtils.notify_Map(mm, Notify.FIXED, MessageString.GuildWarStart);
                        for (String id : Global.Guild_War_Born_Air_Wall.getValue()) {
                            MapManager.getInstance().setBlockDoor(mm, id, true);
                        }
                        break;
                    case "&guildbattlewin":
                        if (player.isHaveGuild()) {
                            GuildBattleWin win = new GuildBattleWin();
                            int t = Integer.parseInt(command[1]);
                            int v = Integer.parseInt(command[2]);
                            win.setGuildId(player.getGuildId());
                            win.setType(t);
                            win.setNum(v);
                            win.getRoleIds().add(player.getId());
                            Manager.guildBattleManager.setGuildBattleWin(win);
                        }
                        break;
                    case "&gmtrancamp":
                        MapObject mapObject = null;
                        ConcurrentHashMap<Long, GuildBattle> gbs1 = Manager.guildBattleManager.getGuildbattles();
                        Iterator<Map.Entry<Long, GuildBattle>> iterator1 = gbs1.entrySet().iterator();
                        while (iterator1.hasNext()) {
                            Map.Entry<Long, GuildBattle> entry = iterator1.next();
                            if (entry.getValue().getMapId() == player.gainMapId()) {
                                mapObject = Manager.mapManager.getMap(entry.getValue().getMapId());
                                break;
                            }
                        }

                        Manager.scriptManager.GetScriptClass(ScriptEnum.GuildBatleMapScript).call("gmTranCamp", mapObject);
                        break;
                    case "&gmnotify":
                        MessageUtils.notify_player(player, Notify.FIXED, " У брата Шэна задница болит уже сто дней， три тысячи раз кончил в рот, а он всё возвращается，советую тебе не возвращаться на старый путь，брось разврат и начни честную жизнь заново");
                        break;
                    case "&setanger":
                        Manager.countManager.setCount(player, BaseCountType.UniverseAnger, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue(), Count.RefreshType.CountType_Forever, Integer.parseInt(command[1]));
                        MessageUtils.notify_player(player, Notify.ERROR, "Злость успешно установлена:" + (int) Manager.countManager.getCount(player, BaseCountType.UniverseAnger, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue()));
                        break;
                    case "&activefashion":
                        Manager.newFashionManager.deal().gmSetFashionID(player, Integer.parseInt(command[1]));
                        break;
                    case "&clearfashion":
                        Manager.newFashionManager.deal().clearAllFshionID(player);
                        break;
                    case "&func":
                        ReadArray<Integer> param = new ReadIntegerArray(command[1], "_");
                        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "FunctionVariable:" + param.get(0) + " прогресс: :" + Manager.controlManager.deal().getFuncProgress(player, param) + ",результат проверки::" + Manager.controlManager.deal().checkFuncProgress(player, param));
                        break;
                    case "&clearholy":
                        Manager.holyEquipManager.deal().gmclearHolyEuiqp(player);
                        break;
                    case "&getholyequipnum":
                        Manager.holyEquipManager.deal().getHolyEquipNumForType(player, 1);
                        break;
                    case "&setpoint":
                        int point = Integer.parseInt(command[1]);
                        player.getOpenServerGrowUp().setPoint(player.getOpenServerGrowUp().getPoint() + point);
                        Manager.openServerAcManager.deal().initGrowUp(player);
                        break;
                    case "&addvipexp":
                        addVipExp(player, command);
                        break;
                    case "&updateNoticeGet":
                        IUpdateNoticeScript script = (IUpdateNoticeScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.UpdateNotice);
                        if (script != null) {
                            script.receiveAward(player);
                        }
                        break;
                    case "&setcangbaogetime":
                        Manager.cangbaogeManager.deal().gmSetLotteryTimes(player);
                        break;
                    case "&petequipinten":
                        intenPetEquip(player, Integer.parseInt(command[1]), Integer.parseInt(command[2]), Integer.parseInt(command[3]));
                        break;
                    case "&petequipsoul":
                        soulPetEquip(player, Integer.parseInt(command[1]), Integer.parseInt(command[2]), Integer.parseInt(command[3]));
                        break;
                    case "&getxs":
                        MessageUtils.notify_player(player, Notify.CHAT, "Текущий этап очищения:" + player.getXsGrade() + "  текущий уровень очищения:" + player.getXsLevel());
                        break;
                    case "&horseequip":
                        horseequip(player, command[1], command);
                        break;
                    case "&addkaoshangling":
                        Manager.countManager.addCount(player, BaseCountType.KaoShangLingScore_Horse_Total, DailyActiveDefine.CrossHosreBoss.getValue(), Count.RefreshType.CountType_Forever, Integer.parseInt(command[1]));
                        MessageUtils.notify_player(player, Notify.ERROR, "Орден награды-древний жетон успешно установлен:" + (int) Manager.countManager.getCount(player, BaseCountType.KaoShangLingScore_Horse_Total, DailyActiveDefine.CrossHosreBoss.getValue()));
                    default:
                        // huhu проверить через reflection, есть ли соответствующий метод
                        if (tryReflect(str, player)) {
                            return true;
                        }
                        isSuccess = false;
                        log.error("Неизвестная gmкоманда command:" + command[0]);
                        break;
                }
                if (player != null) {
                    if (isSuccess) {
                        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, str + " game gm success", 0);
                    } else {
                        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, str + " game gm failed", 0);
                    }
                }
            } else { //серверная консольGMкоманда
                log.info("серверная консольGMкоманда");
            }

        } catch (Exception e) {
            log.error(e, e);
        }

        return false;
    }

    private void redpacket(Player player, String type, String[] command) {
        if ("list".equals(type)) {
            Manager.redPacketManager.getScript().OnReqRedpacketList(player, null);
        } else if ("send".equals(type)) {
            RedPacketMessage.ReqSendRedpacket.Builder req = RedPacketMessage.ReqSendRedpacket.newBuilder();
            req.setMaxValue(100);
            req.setMaxNum(50);
            req.setItemType(1);
            req.setNotice("test");
            Manager.redPacketManager.getScript().OnReqSendRedpacket(player, req.build());
        }
    }

    /**
     * Хаотическая пустота->Сокровищница Сюйми
     *
     * @param player
     * @param type
     * @param command
     */
    private void alienGem(Player player, String type, String[] command) {
        //if("create".equals(type)){
        //    int t = Integer.parseInt(command[2]);
        //    Manager.alienGemManager.getScript().createMap(t);
        //}else if("enter".equals(type)){
        //    int t = Integer.parseInt(command[2]);
        //    Manager.alienGemManager.getScript().enterMap(player, t);
        //}else if("close".equals(type)){
        //    Manager.alienGemManager.getScript().close();
        //}
    }

    private void functionTask(Player player, String type, String[] command) {
        if ("online".equals(type)) {
            Manager.functionTaskManager.getScript().online(player);
        }
    }

    /**
     * Безмятежная сокровищница
     *
     * @param player
     * @param type
     * @param command
     */
    private void wuyou(Player player, String type, String[] command) {
        if ("get".equals(type)) {
            Manager.treasureHuntWuyouManager.getScript().reqGetItem(player);
        } else if ("hunt".equals(type)) {
            int num = Integer.parseInt(command[2]);
            Manager.treasureHuntWuyouManager.getScript().reqHunt(player, num);
        } else if ("panel".equals(type)) {
            Manager.treasureHuntWuyouManager.getScript().reqOpenPanel(player);
        } else if ("clear".equals(type)) {
            player.getTreasureHuntWuyouData().setAwardDate(null);
        } else if ("123".equals(type)) {
            Manager.treasureHuntWuyouManager.getScript().reqExtract(player, 0);
        }
    }

    /**
     * Поединок бессмертной пары
     *
     * @param player
     * @param type
     * @param command
     */
    private void couple(Player player, String type, String[] command) {
        if ("apply".equals(type)) {
            Manager.couplefightManager.getScript().apply(player, command[2]);
        } else if ("applyConfirm".toLowerCase().equals(type.toLowerCase())) {
            String confirm = command[2];
            TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
            if (team != null) {
                for (Long id : team.getMembers()) {
                    Player p = Manager.playerManager.getPlayerCache(id);
                    Manager.couplefightManager.getScript().applyConfirm(p, (confirm != null && confirm.equals("1")) ? true : false);
                }
            }
        } else if ("matchStart".equals(type)) {
            Manager.couplefightManager.getScript().matchStart(player);
        } else if ("matchStop".equals(type)) {
            Manager.couplefightManager.getScript().matchStop(player);
        } else if ("matchConfirm".equals(type)) {
            String confirm = command[2];
            TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
            if (team != null) {
                for (Long id : team.getMembers()) {
                    Player p = Manager.playerManager.getPlayerCache(id);
                    Manager.couplefightManager.getScript().matchConfirm(p, (confirm != null && confirm.equals("1")) ? true : false);
                }
            }
        } else if ("trialsInfo".equals(type)) {
            Manager.couplefightManager.getScript().reqCouplefightInfo(player, 1);
        } else if ("groupPreMap".equals(type)) {
            Manager.couplefightManager.getScript().reqGroupPrepareMapEnter(player);
        } else if ("championPreMap".equals(type)) {
            Manager.couplefightManager.getScript().reqCouplefightInfo(player, 35);
        } else if ("online".equals(type)) {
            Manager.couplefightManager.getScript().online(player);
        } else if ("champion".equals(type)) {
            String param = command[2];
            Manager.couplefightManager.getScript().reqCouplefightInfo(player, 30, Integer.parseInt(param));
        } else if ("championTeam".equals(type)) {
            String param = command[2];
            Manager.couplefightManager.getScript().reqCouplefightInfo(player, 33, Integer.parseInt(param));
        } else if ("championGuess".equals(type)) {
            String param = command[2];
            CouplefightMessage.ReqChampionGuess.Builder messInfo = CouplefightMessage.ReqChampionGuess.newBuilder();
            messInfo.setType(Integer.parseInt(param));
            messInfo.setFightId(1);
            messInfo.setTeamId(1);
            Manager.couplefightManager.getScript().reqChampionGuess(player, messInfo.build());
        } else if ("championFansRankList".equals(type)) {
            Manager.couplefightManager.getScript().reqCouplefightInfo(player, 34);
        }
    }

    private void getRank(Player player, String[] command) {
        RankListMessage.ReqRankInfo.Builder messInfo = RankListMessage.ReqRankInfo.newBuilder();
        messInfo.setRankKind(Integer.parseInt(command[1]));
        Manager.rankListManager.deal().OnReqRankInfo(player, messInfo.build());
    }

    private void nature(Player player, String type, String[] command) {
        if ("info".equals(type)) {
            int natureType = Integer.valueOf(command[2]);
            Manager.natureManager.deal().onReqNatureInfo(player, natureType);
        } else if ("levelUp".equals(type)) {
            int natureType = Integer.valueOf(command[2]);
            int itemId = Integer.valueOf(command[3]);
            Manager.natureManager.deal().onReqNatureUpLevel(player, natureType, itemId, false);
        } else if ("drug".equals(type)) {
            int natureType = Integer.valueOf(command[2]);
            int itemId = Integer.valueOf(command[3]);
            Manager.natureManager.deal().onReqNatureDrug(player, natureType, itemId);
        } else if ("huaxing".equals(type)) {
            int natureType = Integer.valueOf(command[2]);
            int itemId = Integer.valueOf(command[3]);
            Manager.natureManager.deal().onReqNatureFashionUpLevel(player, natureType, itemId);
        } else if ("initWeapon".equals(type)) {
            Manager.natureManager.deal().initPlayerWeapon(player);
        }

    }

    /**
     * очистить багуа на экипировке игрока
     *
     * @param player
     */
    private void cleanbagua(Player player) {
        for (ImmortalEquipPart part : player.getImmortalEquipPartLisit().values()) {
            if (part.getPart() >= ImmortalEquipManager.baguaMinPart && part.getPart() <= ImmortalEquipManager.baguaMaxPart) {
                part.setEquip(null);
            }
        }

    }

    /**
     * связано с демонической душой
     *
     * @param player  игрок
     * @param command команда
     */
    private void devilSeries(Player player, String type, String[] command) {
        if ("wear".equals(type)) {
            int campId = Integer.parseInt(command[2]);
            int cardId = Integer.parseInt(command[3]);
            int cellId = Integer.parseInt(command[4]);
            long equipId = Long.parseLong(command[5]);
            Optional<Item> opt = player.getDevilPackItems().values().stream().filter(a -> a.getItemModelId() == equipId ? true : false).findFirst();
            if (opt.isPresent()) {
                Manager.devilSeriesManager.getScript().devilEquipWear(player, campId, cardId, cellId, opt.get().getId());
            }
        } else if ("synthesis".equals(type)) {
            int itemId = Integer.parseInt(command[2]);
            List<Long> ids = getHorseItems(player, command[3], player.getDevilPackItems().values());
            Manager.devilSeriesManager.getScript().devilEquipSynthesis(player, itemId, ids);
        } else if ("up".equals(type)) {
            int campId = Integer.parseInt(command[2]);
            int cardId = Integer.parseInt(command[3]);
            int upType = Integer.parseInt(command[4]);
            Manager.devilSeriesManager.getScript().devilCardUp(player, campId, cardId, upType);
        } else if ("break".equals(type)) {
            int campId = Integer.parseInt(command[2]);
            int cardId = Integer.parseInt(command[3]);
            List<Long> ids = getHorseItems(player, command[4], player.getDevilPackItems().values());
            Manager.devilSeriesManager.getScript().devilCardBreak(player, campId, cardId, ids);
        }
    }

    private void horseequip(Player player, String type, String[] command) {
        if ("wear".equals(type)) {
            int horseAssistant = Integer.parseInt(command[2]);
            int cellId = Integer.parseInt(command[3]);
            long equipId = Long.parseLong(command[4]);
            Optional<Item> opt = player.getHorseEquipPackItems().values().stream().filter(a -> a.getItemModelId() == equipId ? true : false).findFirst();
            if (opt.isPresent()) {
                Manager.horseManager.deal().wearEquip(player, opt.get().getId(), horseAssistant, cellId);
            }
        } else if ("inten".equals(type)) {
            int horseAssistant = Integer.parseInt(command[2]);
            int cellId = Integer.parseInt(command[3]);
            Manager.horseManager.deal().intenHorseEquip(player, horseAssistant, cellId);
        } else if ("soul".equals(type)) {
            int horseAssistant = Integer.parseInt(command[2]);
            int cellId = Integer.parseInt(command[3]);
            Manager.horseManager.deal().soulHorseEquip(player, horseAssistant, cellId);
        } else if ("decompose".equals(type)) {
            long horseAssistant = Long.parseLong(command[2]);
            Optional<Item> opt = player.getHorseEquipPackItems().values().stream().filter(a -> a.getItemModelId() == horseAssistant ? true : false).findFirst();
            if (opt.isPresent()) {
                List<Long> ids = new ArrayList<>();
                ids.add(opt.get().getId());
                Manager.horseManager.deal().horseEquipDecompose(player, ids);
            }
        } else if ("synthesis".equals(type)) {
            int horseAssistant = Integer.parseInt(command[2]);
            int cellId = Integer.parseInt(command[3]);
            List<Long> ids = getHorseItems(player, command[4], player.getHorseEquipPackItems().values());
            Manager.horseManager.deal().horseEquipSynthetic(player, horseAssistant, cellId, ids);
        } else if ("activeinten".equals(type)) {
            int horseAssistant = Integer.parseInt(command[2]);
            int cellId = Integer.parseInt(command[3]);
            Manager.horseManager.deal().horseEquipIntenActive(player, horseAssistant, cellId);
        } else if ("activesoul".equals(type)) {
            int horseAssistant = Integer.parseInt(command[2]);
            int cellId = Integer.parseInt(command[3]);
            Manager.horseManager.deal().horseEquipSoulActive(player, horseAssistant, cellId);
        } else if ("activesite".equals(type)) {
            int horseAssistant = Integer.parseInt(command[2]);
            Manager.horseManager.deal().activeHorseEquip(player, horseAssistant);
        }
    }

    private List<Long> getHorseItems(Player player, String s, Collection<Item> items) {
        String[] id_arr = s.split(",|;");
        List<Integer> modelIds = new ArrayList<>();
        for (String o : id_arr) {
            modelIds.add(Integer.parseInt(o));
        }
        List<Long> ids = new ArrayList<>();
        items.stream().forEach(a -> {
            if (modelIds.contains(a.getItemModelId())) {
                ids.add(a.getId());
                modelIds.remove(Integer.valueOf(a.getItemModelId()));
            }
        });
        return ids;
    }

    private void horseequipadd(Player player) {
        Cfg_Horse_equip_unlock_Container.GetInstance().getValuees();
    }

    private void auction(Player player, String[] command) {
        if (command.length < 2) {
            return;
        }
        int size = command.length;
        String op = command[1];

        switch (op) {
            case "put"://выставить на продажу
                int itemId = size > 2 ? Integer.parseInt(command[2]) : 0;
                int num = size > 3 ? Integer.parseInt(command[3]) : 0;
                int type = size > 4 ? Integer.parseInt(command[4]) : 0;
                String password = size > 5 ? command[5] : null;
                int price = size > 6 ? Integer.parseInt(command[6]) : 0;
                Optional<Item> optional = player.getBackpackItems().values().stream().filter(a -> a.getItemModelId() == itemId).findFirst();
                if (optional.isPresent()) {
                    Item item = optional.get();
                    Manager.auctionManager.manager().auctionPut(player, item.getId(), num, type, password, price);
                } else {
                    log.info("gm не удалось выставить: предмет не существует");
                }
                break;
            case "pur"://мгновенная покупка
                long auctionId = size > 2 ? Long.parseLong(command[2]) : 0;
                password = size > 3 ? command[3] : null;
                Manager.auctionManager.auctionPur(player, auctionId, password);
                break;
            case "auc":
                auctionId = size > 2 ? Long.parseLong(command[2]) : 0;
                price = size > 3 ? Integer.parseInt(command[3]) : 0;
                Manager.auctionManager.auction(player, auctionId, price);
                break;
        }
    }

    private void addVipExp(Player player, String[] command) {
        if (command.length < 2) {
            return;
        }

        int exp = Integer.parseInt(command[1]);

        if (exp <= 0) {
            return;
        }

        Manager.vipManager.deal().addVipExp(player, exp, ItemChangeReason.GM, IDConfigUtil.getLogId());
    }

    private void OnRobotBattle(Player player) {
        int count = 0;
        List<Long> roleIDs = new ArrayList<>();
        for (Player player1 : PlayerManager.getInstance().getPlayersCache().values()) {
            if (count >= 1)
                break;
            if (player1.getId() != player.getId())
                continue;
            roleIDs.add(player1.getId());
            count++;
        }
        if (roleIDs.size() > 0) {
            RobotManager.getInstance().deal().OnRobotHelpBattle(player, roleIDs,
                    TimeUtils.Time() + 30000, 0.4f, Global.Newguild_BOSS_BUFF);
        }
    }

    private void auctoinActivePut(Player player) {

        if (player.getGuildId() == 0)
            return;
        Item item_1 = Item.createItem(2000646, 1, false);
        Item item_2 = Item.createItem(2000646, 1, false);
        List<Item> itemlist = new ArrayList<>();
        itemlist.add(item_1);
        itemlist.add(item_2);

        List<Long> rolelist = new ArrayList<>();
        rolelist.add(player.getId());

        Manager.auctionManager.manager().auctionActivityPut(rolelist, itemlist, 105, player.getGuildId());


    }

    /**
     * используется для тестирования основной сюжетной задачи，не ограничивается какими-либо ограничениями, связанными с заданиями
     *
     * @param player
     * @param command
     */
    private void anyTask(Player player, String[] command) {
        int type = Integer.parseInt(command[1]);
        int modelId = Integer.parseInt(command[2]);
        if (type == Task.MAIN_TASK) {
            player.getCurMainTasks().clear();
            player.getOverMainTaskIDs().clear();
            Manager.taskManager.deal().acceptTask(player, Task.MAIN_TASK, modelId, 0, false);
        } else if (type == Task.BRANCH_TASK) {
            player.getBranchOverList().clear();
            Manager.taskManager.deal().acceptTask(player, Task.BRANCH_TASK, 0, 0, false);
        }
    }

    private boolean sendFeedBack(String[] command) {
        long playerId = Long.parseLong(command[1]);
        if (!Manager.playerManager.getAllPlayerWorldInfo().containsKey(playerId)) {
            log.error("Игрок не существует");
            return false;
        }
        FeedBackInfo info = new FeedBackInfo();
        info.setContent(command[3]);
        info.setType(Integer.parseInt(command[2]));
        info.setTime((int) (TimeUtils.Time() / 1000));
        Manager.settingManager.deal().sendGMFeedback(playerId, info);
        log.error("feedbackкоманда выполнена успешно");
        return true;
    }

    private void buildResTaskFinish(Player player, int taskId, int state) {
        taskMessage.ResTaskFinish.Builder builder = taskMessage.ResTaskFinish.newBuilder();
        builder.setCurrentCount(0);
        builder.setType(Task.GENDER_TASK);
        builder.setFinshType(0);
        builder.setModelId(taskId);
        builder.setState(state);
        if (state == TaskConst.SUCCESS) {
            builder.setSubmitResult(true);
        } else {
            builder.setSubmitResult(false);
        }
        MessageUtils.send_to_player(player, taskMessage.ResTaskFinish.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void changeJob(Player player, int level_changeJob) {
        Cfg_Changejob_Bean bean = CfgManager.getCfg_Changejob_Container().getValueByKey(level_changeJob);
        if (bean == null) {
            return;
        }
        if (player.getXsGrade() == level_changeJob) {
            return;
        }
        player.setXsGrade(level_changeJob);

        Cfg_Task_gender_Bean[] cfg_Task_gender_BeanList = CfgManager.getCfg_Task_gender_Container().getValuees();
        if (cfg_Task_gender_BeanList != null && cfg_Task_gender_BeanList.length > 0) {
            for (int i = 0; i < cfg_Task_gender_BeanList.length; i++) {
                if (cfg_Task_gender_BeanList[i].getGenderClass() == level_changeJob) {
                    //player.getCurGenderTask().initTask(player,cfg_Task_gender_BeanList[i].getId());
                    buildResTaskFinish(player, player.getCurGenderTask().getModelId(), TaskConst.SUCCESS);
                    player.getCurGenderTask().setModelId(0);
                    Manager.taskManager.deal().acceptTask(player, Task.GENDER_TASK, cfg_Task_gender_BeanList[i].getId(), 0, false, true);
                    buildResTaskFinish(player, player.getCurGenderTask().getModelId(), TaskConst.SUCCESS);
                    break;
                }
            }
        }

        Manager.newFashionManager.deal().addFashionID(player, bean.getModel_change());
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Task);
        Manager.controlManager.operate(player, FunctionVariable.ChangeJob, 0);


        PlayerMessage.ResGenderClassChange.Builder builder = PlayerMessage.ResGenderClassChange.newBuilder();
        builder.setGenderClass(player.getXsGrade());
        MessageUtils.send_to_player(player, PlayerMessage.ResGenderClassChange.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * начало активности
     */
    private void dailyBegin(String[] command) {
        if (command.length < 2) {
            return;
        }
        int dailyId = Integer.parseInt(command[1]);
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(dailyId);
        if (bean == null) {
            log.error("&dailybeginвыполнение не удалось，не удалось найти соответствующий id daily_bean!!");
            return;
        }
        try {
            Manager.dailyActiveManager.getGmControl().put(bean.getId(), true);
            Manager.dailyActiveManager.deal().activeBegin(bean);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /**
     * окончание активности
     */
    private void dailyEnd(String[] command) {
        if (command.length < 2) {
            return;
        }
        int dailyId = Integer.parseInt(command[1]);
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(dailyId);
        if (bean == null) {
            log.error("&dailyendвыполнение не удалось，не удалось найти соответствующий id daily_bean!!");
            return;
        }
        try {
            Manager.dailyActiveManager.getGmControl().remove(bean.getId());
            Manager.dailyActiveManager.deal().activeEnd(bean);

        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private boolean fudipower(Player player, String[] command) {
        if (command.length < 2) {
            return false;
        }

        boolean isFuDi = Integer.parseInt(command[1]) == 1;
        String name = "";
        if (command.length > 2)
            name = command[2];
        Manager.rankListManager.getGuildRankScript().gmFuDiPower(player, name, isFuDi);
        return true;
    }

    private boolean setStateVip(Player player, String[] command) {
        if (command.length < 2) {
            return false;
        }
        int lv = Integer.parseInt(command[1]);
        return Manager.stateVipManager.deal().gmStateVipUp(player, lv);

    }

    /**
     * операции, связанные с праздничными активностями
     *
     * @param player  игрок
     * @param command команда
     */
    private void holidayAction(Player player, String[] command) {
        if (!ServerConfig.isTestServer()) {
            return;
        }
//        Manager.activityManager.holidayDeal().gmAction(player, command);
    }

    /**
     * операции, связанные с функцией публикации
     *
     * @param player  игрок
     * @param command команда
     */
    private void shareAction(Player player, String[] command) {
        if (!ServerConfig.isTestServer()) {
            return;
        }
        Manager.shareManager.deal().gmAction(player, command);
    }

    /**
     * через reflection проверить наличие соответствующего метода command(String[] ,Player)
     *
     * @param str
     * @param player
     * @return
     */
    private boolean tryReflect(String str, Player player) {
        if (str.startsWith("&")) {
            str = str.substring(1);
        }

        String[] command = str.split(" ");
        if (command.length < 1) {
            return false;

        }
        try {
            Method m = this.getClass().getDeclaredMethod(command[0], String[].class, Player.class
            );
            if (m
                    != null) {
                m.invoke(this, command, player);
                return true;
            }
        } catch (Exception e) {
            log.error(e);
            if (!(e instanceof NoSuchMethodException)) {
                e.printStackTrace(System.out);
            }
        }

        return false;
    }

    private void queuesize(String[] command, Player player) {
        StringBuilder builder = new StringBuilder();
        builder.append(getCommandProcessorQueueSize());
        builder.append(getMsgQueueSizeInfo());
        builder.append(getMapQueueSizeInfo());
        if (player != null) {
            MessageUtils.notify_player(player, Notify.CHAT, MessageString.WANGNENGTISHI, builder.toString());
        }
    }

    public String getMsgQueueSizeInfo() {
        StringBuilder b = new StringBuilder("msgOrderedQueueSize:\n");
        return b.toString();
    }

    public String getMapQueueSizeInfo() {
        StringBuilder b = new StringBuilder("Информация о карте:\n");
        b.append("cityServerGroup:\n");
        getMapQueueSizeInfo(GameServer.getInstance().getWorldMapServerGroup().getMapServers(), b);
        b.append("zoneServerGroup:\n");
        getMapQueueSizeInfo(GameServer.getInstance().getCopyMapServerGroup().getMapServers(), b);
        return b.toString();
    }

    private void getMapQueueSizeInfo(ConcurrentHashMap<Long, MapServer> maps, StringBuilder b) {
        for (Entry<Long, MapServer> entry : maps.entrySet()) {
            b.append(String.format("mapid:%d size:%d\n", entry.getKey(), entry.getValue().getCurCmdCount()));
        }
    }

    private String getCommandProcessorQueueSize() {
        StringBuilder builder = new StringBuilder("commandProcessorQueueSize:\n");
        Iterator<String> it = CommandProcessor.recordClassName.iterator();
        while (it.hasNext()) {
            String classname = it.next();
            try {
                Class<?> cl = Class.forName(classname);
                Method m = cl.getDeclaredMethod("getInstance");
                CommandProcessor p = (CommandProcessor) m.invoke(null);
                builder.append(String.format("class:%s queue size:%d\n", classname, p.getCurQueueSize()));
            } catch (Exception e) {
                log.error(e, e);
            }
        }
        return builder.toString();
    }

    //очистка данных рюкзака
    private boolean cleanBag(ConcurrentHashMap<Integer, Item> container) {
        List<java.util.Map.Entry<Integer, Item>> list = new ArrayList<>();
        list.addAll(container.entrySet());
        for (int i = 0; i < list.size(); i++) {
            java.util.Map.Entry<Integer, Item> entry = list.get(i);
            Item value = entry.getValue();
            Cfg_Item_Bean valuemodel = CfgManager.getCfg_Item_Container().getValueByKey(value.getItemModelId());
            if (valuemodel == null) {
                return false;
            }
            for (int j = i + 1; j < list.size(); j++) {
                java.util.Map.Entry<Integer, Item> entry2 = list.get(j);
                Item value2 = entry2.getValue();
                if (value.getId() == value2.getId()) {
                    if (value.getId() == value2.getId()) {
                        if (value.getNum() > value2.getNum()) {
                            value.setNum(0);
                        } else {
                            value2.setNum(0);
                        }
                    }
                }
            }
        }
        for (java.util.Map.Entry<Integer, Item> entry : list) {
            if (entry.getValue().getNum() == 0) {
                container.remove(entry.getKey());
            }
        }
        return true;
    }

    private void cleanCard(String[] command, Player player) throws NumberFormatException {
        int cardType = Integer.parseInt(command[1]);
//        long actionId;
        int key;
        if (cardType == 1) {
            key = 99;
            player.setMoonCardDays(0);
//            Manager.vipManager.deal(ScriptEnum.RechargeCheckVipLevelScript).checkCardBuff(player, key);
            MessageUtils.notify_player(player, Notify.CHAT, "Дни месячной карты сброшены до::" + player.MoonCardDays());
//            manager.welfareManager.deal().sendActiveCard(player, 1);
        } else if (cardType == 2) {
            key = 999;
            player.setLifeCard(false);
//            Manager.vipManager.deal(ScriptEnum.RechargeCheckVipLevelScript).checkCardBuff(player, key);
            MessageUtils.notify_player(player, Notify.CHAT, "Пожизненная привилегированная карта отменена::" + player.isLifeCard());
//            manager.welfareManager.deal().sendActiveCard(player, 2);
        }
    }

    //перезагрузить все конфигурации данных
    private void reLoadGameData() {
        log.error("GM перезагрузить все  конфигурации игровых данных");
        Manager.bossManager.manager().reloadSpecialMonsterConfig();
        //заново загрузить кэш заданий
        Manager.taskManager.deal().loadAllCanReceiveTask();

        if (GameServer.getInstance().IsFightServer()) {
            SoulAnimalForestCrossManager.getInstance().manager().crossBossInfoInit();
        }

        try {
            ServerStr.load(ServerConfig.getLangType());
        } catch (IOException ex) {
            log.error(ex, ex);
        }
        log.error("GM перезагрузить все  конфигурации игровых данных over");
    }

    /**
     * перезагрузка таблицы конфигурации
     *
     * @param command
     */
    private boolean configReload(String[] command) {
        try {
            if (command.length < 2) {
                return false;
            }
            String configName = command[1];
            log.info("GM Перезагрузка конфигурации игровых данных::" + configName);
            String reloadName = getJavaClassReloadName(configName);
            boolean isSuccess = ScriptConfigManager.GetInstance().reloadConfigScript(reloadName);
            if (isSuccess) {
                switch (configName) {
                    case "item":
                        ScriptConfigManager.GetInstance().reloadCofigItem();
                        break;
                    case "equip":
                        ScriptConfigManager.GetInstance().reloadCofigItem();
                        break;
                    case "functionstart":
                        Manager.controlManager.deal().reload();
                        break;
                    case "shop_maket":
                        Manager.shopManager.reset();
                    default:
                        break;
                }
            }
            return isSuccess;
        } catch (Exception e) {
            log.error(e, e);
        }
        return false;
    }

    private String getJavaClassReloadName(String configName) {
        char oldChar = configName.charAt(0);
        char newChar = (oldChar + "").toUpperCase().charAt(0);
        String replace = configName.replaceFirst(oldChar + "", newChar + "");
        return "config.Cfg_" + replace + "_Load";
    }

    //обновить картуBUFF
    private void addgroundbuff(Player player, String[] command) {
        int monsterId = Integer.parseInt(command[1]);
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (null == map) {
            log.error("GM Не удалось создать монстра，карта не найдена");
            return;
        }
//        Manager.mapManager.createGroundBuff(map, player.gainCurPos(), monsterId, 1, 0);
    }

    //GMсоздать/обновить монстра
    private void addMonster(Player player, String[] command) {
        int monsterId = Integer.parseInt(command[1]);
        int num = command.length >= 3 ? Integer.parseInt(command[2]) : 1;
        num = num > 10 ? 10 : num;
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (null == map) {
            log.error("GM Не удалось создать монстра，карта не найдена");
            return;
        }
        for (int i = 0; i < num; i++) {
            Manager.monsterManager.createMonster(map, player.gainCurPos(), monsterId);
        }

    }

    private void droptn(Player player, String[] command) {
        StringBuilder sb = new StringBuilder();
        int dropID = Integer.parseInt(command[1]);
        int num = Math.min(Integer.parseInt(command[2]), 1000000);
        sb.append("test:").append(dropID).append(" num:").append(num).append(" -> ");
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < num; i++) {
            List<List<Integer>> drops = Manager.dropManager.deal().getItemDrops(player, dropID);
            int n = drops.size();
            map.put(n, map.getOrDefault(n, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append("/");
        }
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, sb.toString());
    }

    private void drop(Player player, String[] command) {
        StringBuilder sb = new StringBuilder();
        int dropID = Integer.parseInt(command[1]);
        int num = Math.min(Integer.parseInt(command[2]), 1000000);
        sb.append("dropID:").append(dropID).append(" num:").append(num).append(" результат выпадения -> ");
        HashMap<String, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < num; i++) {
            List<List<Integer>> tmp = Manager.dropManager.deal().getItemDrops(player, dropID);
            if (tmp != null && tmp.size() > 0) {
                for (List<Integer> list : tmp) {
                    String str = list.get(0) + "_" + list.get(2);
                    if (map.containsKey(str)) {
                        List<Integer> aa = new ArrayList<>();
                        aa.add(list.get(0));
                        aa.add(list.get(1) + map.get(str).get(1));
                        aa.add(list.get(2));
                        map.put(str, aa);
                    } else
                        map.put(str, list);
                }
            }
        }

        if (map.size() == 0) {
            MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "dropтест команды завершился неудачей，нет выпадения");
            return;
        }

        List<List<Integer>> dropList = new ArrayList<>(map.values());
        for (List<Integer> list : dropList) {
            Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(list.get(0));
            if (model == null)
                sb.append("[Ошибочный предмет：").append(list.get(0));
            else
                sb.append("[ID:").append(list.get(0))
                        .append(" название предмета:").append(model.getName())
                        .append(" количество:").append(list.get(1))
                        .append(" привязка：").append(list.get(2));

            sb.append("] - ");
        }
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, sb.toString());
    }

    private void decItem(Player player, String[] command) {
        int itemId = Integer.parseInt(command[1]);
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(itemId);
        if (model == null) {
            return;
        }
        int num = Integer.valueOf(command[2]);
        if (!Manager.backpackManager.manager().onRemoveItem(player, itemId, num, ItemChangeReason.GMDeductItemDec, IDConfigUtil.getLogId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NotEnounghItem);
        }
    }

    //GMGM-команда добавления предмета
    private void addItem(Player player, String[] command) throws ParseException {
//        if (!ServerConfig.isTestServer()) {
//            return;
//        }
        //&additem предметId количество уровень усиления привязан ли(1привязан) уровень повышения звёзд;
        int itemId = Integer.parseInt(command[1]);
        Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(itemId);
        if (model == null) {
            log.error("gmполучить предмет не существует modelId:" + itemId);
            return;
        }
        long num = command.length >= 3 ? Long.parseLong(command[2]) : model.getMax();
        int gradeNum = command.length >= 4 ? Integer.parseInt(command[3]) : 0;
        boolean bind = command.length >= 5 ? command[4].equals("1") : false;
        int starLv = command.length >= 6 ? Integer.parseInt(command[5]) : 0;
        long losttime = 0;
        if (command.length >= 7) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            Date d = sdf.parse(command[6]);
            losttime = d.getTime();
        }
        List<Item> itemList = Item.createItems(itemId, num, bind, losttime, gradeNum, starLv);
        int msId = Manager.backpackManager.manager().onHasAddSpaces(player, itemList);
        if (msId != 0) {
            MessageUtils.notify_player(player, Notify.ERROR, msId);
            return;
        }
        Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.GM, IDConfigUtil.getLogId());
    }

    private void addbag(Player player, String[] command) throws ParseException {
        int itemBagId = Integer.parseInt(command[1]);
        Cfg_GMitem_Bean cfg_GMitem_Bean = CfgManager.getCfg_GMitem_Container().getValueByKey(itemBagId);
        if (cfg_GMitem_Bean == null) {
            log.error("gmполучить набор предметов не существует itemBagId:" + itemBagId);
            return;
        }
        Manager.backpackManager.manager().openBagCellSuccess(player, Global.Born_Bag_Num.get(1), (byte) 1, 0, 0);
        ReadIntegerArrayEs items = cfg_GMitem_Bean.getItem();
        if (items != null && items.size() >= 0) {
            for (int i = 0; i < items.size(); i++) {
                ReadIntegerArray readArray = (ReadIntegerArray) items.get(i);
                Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(readArray.get(0));
                if (model == null) {
                    log.error("gmполучить предмет не существует modelId:" + readArray.get(0));
                    return;
                }
                long num = 0;
                if (readArray.size() >= 2) {
                    num = readArray.get(1);
                } else {
                    num = model.getMax();
                }
                boolean bind = false;
                List<Item> itemList = Item.createItems(readArray.get(0), num, bind);
                int msId = Manager.backpackManager.manager().onHasAddSpaces(player, itemList);
                if (msId != 0) {
                    MessageUtils.notify_player(player, Notify.ERROR, msId);
                    return;
                }
                Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.GM, IDConfigUtil.getLogId());

            }

        }


    }

    private void addItems(Player player, String[] command) throws ParseException {
        int[] a = new int[]{
                1025,
                1052,
                1053,
                1070,
                1071,
                1072,
                1073,
                1074,
                1075,
                1076,
                1077,
                1078,
                1079,
                1088,
                1089,
                1090,
                1091,
                1092,
                1093,
                1094,
                1095,
                1115,
                1116,
                1117,
                1118,
                1121,
                1122,
                1123,
                1124,
                1125,
                1126,
                1127,
                1128,
                1129,
                1130,
                1131,
                1132,
                1133,
                1134,
                1135,
                1136,
                1137,
                1138,
                1139,
                1301,
                1302,
                1303,
                1304,
                10001,
                10004,
                10009,
                10010,
                10011,
                10012,
                10013,
                10018,
                10014,
                10015,
                10016,
                10026,
                10017,
                10022,
                10023,
                10025,
                10027,
                10024,
                10028,
                10029,
                10030,
                10031,
                10032,
                10033,
                10034,
                10035,
                10036,
                10037,
                15001,
                15002,
                15003,
                15004,
                15005,
                15006,
                15007,
                15008,
                15009,
                15010,
                15011,
                15012,
                15013,
                15014,
                15015,
                15016,
                15017,
                15018,
                15019,
                15020,
                15021,
                15022,
                15023,
                15024,
                16001,
                16004,
                16005,
                16006,
                16007,
                16008,
                16009,
                16010,
                16011,
                16012,
                16013,
                16196,
                17001,
                17002,
                17003,
                17004,
                17005,
                17006,
                17007,
                17008,
                17009,
                17010,
                17011,
                17012,
                17013,
                17014,
                17015,
                17016,
                17017,
                17018,
                17019,
                17020,
                17021,
                19001,
                19004,
                19007,
                19008,
                19009,
                19010,
                19015,
                20001,
                21001,
                60002,
                60003,
                60004,
                60034,
                60016,
                83086,
                83087,
                83088,
                83089,
                83090,
                83091,
                83092,
                83093,
                83094,
                3003001,
                3003002,
                3003003,
                3003004,
                3003005,
                3004001,
                3004002,
                3004003,
                3004004,
                3004005,
                3006001,
                3006002,
                3006003,
                3006004,
                3006005,
                3007101,
                3007102,
                3007103,
                3007104,
                3007105,
                3007201,
                3007202,
                3007203,
                3007204,
                3007205,
                3007301,
                3007302,
                3007303,
                3007304,
                3007305,
                3007401,
                3007402,
                3007403,
                3007404,
                3007405,
                3007501,
                3007502,
                3007503,
                3007504,
                3007505,
                3008501,
                3008502,
                3008503,
                3008504,
                3008505,
                3009501,
                3009502,
                3009503,
                3009504,
                3009505,
                3010501,
                3010502,
                3010503,
                3010504,
                3010505};
        Manager.backpackManager.manager().openBagCellSuccess(player, Global.Born_Bag_Num.get(1), (byte) 1, 0, 0);
        for (int i = 0; i < a.length; i++) {
            Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(a[i]);
            if (model == null) {
                log.error("gmполучить предмет не существует modelId:" + a[i]);
                return;
            }
            long num = model.getMax();
            boolean bind = false;
            List<Item> itemList = Item.createItems(a[i], num, bind);
            int msId = Manager.backpackManager.manager().onHasAddSpaces(player, itemList);
            if (msId != 0) {
                MessageUtils.notify_player(player, Notify.ERROR, msId);
                return;
            }
            Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.GM, IDConfigUtil.getLogId());
        }
    }

    private void addEquips(Player player, String[] command) throws ParseException {
        Manager.backpackManager.manager().openBagCellSuccess(player, Global.Born_Bag_Num.get(1), (byte) 1, 0, 0);
        if (player.getCareer() == 0) {
            addEquipsToPlayer(player, new int[]{
                    4008611,
                    4008612,
                    4008613,
                    4008614,
                    4008615,
                    4008616,
                    4008617,
                    4008618,
                    4008619,
                    4008620,
                    4008621,
                    5010005,
                    5010015,
                    5010025,
                    5010035,
                    5000005,
                    5000015,
                    5000025,
                    5000035,
                    24001,
                    24002,
                    24003,
                    24004,
                    24005,
                    24006,
                    81065,
                    81067

            });
        } else {
            addEquipsToPlayer(player, new int[]{
                    4108611,
                    4108612,
                    4108613,
                    4108614,
                    4108615,
                    4108616,
                    4108617,
                    4108618,
                    4108619,
                    4108620,
                    4108621,
                    5110005,
                    5110015,
                    5110025,
                    5110035,
                    5100005,
                    5100015,
                    5100025,
                    5100035,
                    24007,
                    24008,
                    24009,
                    24010,
                    24011,
                    24012,
                    81066,
                    81068

            });
        }
        addEquipsToPlayer(player, new int[]{
                7010040,
                7010041,
                7010042,
                7010043,
                7010044,
                7010045,
                7010046,
                7010047,
                7010048,
                7010049,
                7010050,
                7010051,
                7010052,
                7010053,
                7010054,
                7010055,
                7010056,
                7010057,
                7010058,
                7010059,
                2003529,
                2003530,
                2003503,
                2003504
        });
    }

    private void addEquipsToPlayer(Player player, int[] eqs) {
        for (int i = 0; i < eqs.length; i++) {
            Cfg_Item_Bean model = CfgManager.getCfg_Item_Container().getValueByKey(eqs[i]);
            if (model == null) {
                log.error("gmполучить предмет не существует modelId:" + eqs[i]);
                return;
            }
            long num = model.getMax();
            boolean bind = false;
            List<Item> itemList = Item.createItems(eqs[i], num, bind);
            int msId = Manager.backpackManager.manager().onHasAddSpaces(player, itemList);
            if (msId != 0) {
                MessageUtils.notify_player(player, Notify.ERROR, msId);
                return;
            }
            Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.GM, IDConfigUtil.getLogId());
        }
    }

    //GMGM-команда установки уровня
    private void setLevel(Player player, String[] command) {
        int oldLevel = player.getLevel();
        long oldExp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.EXP);
        int level = Integer.parseInt(command[1]);
        int nowLevel = Math.min(Global.PlayerMaxLevel, level);
        player.getCurrencys().set(ItemCoinType.EXP, 0);
        long newExp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.EXP);

        player.setLevel(nowLevel);
        Manager.playerManager.manager().playerLevelUp(player, oldLevel, oldExp, nowLevel, newExp, 0, ItemChangeReason.GM, IDConfigUtil.getLogId());
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.BASE);
    }

    //GMGM-команда добавления опыта
    private void addExp(Player player, String[] command) {
        long num = Long.parseLong(command[1]);
        Manager.currencyManager.manager().addEXP(player, num, ItemChangeReason.GM, IDConfigUtil.getLogId());
    }

    /**
     * установить атрибут персонажа
     *
     * @param player
     * @param command
     */
    private void setAttribute(Player player, String[] command) {
        if (command.length < 3) {
            return;
        }
        int type = Integer.parseInt(command[1]);
        int num = Integer.parseInt(command[2]);
        type = type < 0 || type > AttributeType.ATTR_MAX ? 0 : type;
        num = num < 0 ? 0 : num;
        if (num > 10000000) {
            //больше 110 миллионов — не устанавливать，данные слишком велики，это приведёт к переполнению боевой силы
            return;
        }
        player.getAttribute().setAttribute(type, num);
        player.getAttribute().calFinalMoveSpeed();
        player.getAttribute().calFinalMoveSpeed();
        PlayerMessage.ResPlayerAttributeChange.Builder b = PlayerMessage.ResPlayerAttributeChange.newBuilder();
        game.message.PlayerMessage.Attribute.Builder a = game.message.PlayerMessage.Attribute.newBuilder();
        a.setType(type);
        a.setValue(num);
        b.setType(-1);
        b.addChangeList(a);
        if (type == AttributeType.ATTR_AtkSpeed) {
            player.getAttribute().calFinalAttackSpeed();
            a.setType(AttributeType.AttackSpeedFinal);
            a.setValue(player.getAttribute().gainFinalAttackSpeed());
            b.addChangeList(a);
            Manager.playerAttAttributeManager.deal().sendAttackSpeedMessge(player);
        }
        if (type == AttributeType.ATTR_Speed) {
            player.getAttribute().calFinalMoveSpeed();
            a.setType(AttributeType.MoveSpeedFinal);
            a.setValue(player.getAttribute().gainFinalMoveSpeed());
            b.addChangeList(a);
            Manager.playerAttAttributeManager.deal().sendMoveSpeedMessage(player);
        }

        MessageUtils.send_to_player(player, PlayerMessage.ResPlayerAttributeChange.MsgID.eMsgID_VALUE, b.build().toByteArray());
    }

    /**
     * навсегда увеличить атрибут персонажа
     *
     * @param player
     * @param command
     */
    private void addAttribute(Player player, String[] command) {
        if (command.length < 3) {
            return;
        }

        int type = Integer.parseInt(command[1]);
        int num = Integer.parseInt(command[2]);
        Cfg_AttributeAdd_Bean bean = CfgManager.getCfg_AttributeAdd_Container().getValueByKey(type);
        if (bean == null) {
            return;
        }
        if (type > 1000) {
            player.getMedicinesAttributeSys().addSystemAttribute(type, num);
        } else {
            player.getMedicinesAttribute().addAttribute(type, num);
        }
        Manager.playerAttAttributeManager.deal().calcAttribute(player);
    }

    private int checkTaskId() {
        int initTaskId = Global.FristTask;
        if (!isTaskInit) {
            List<Integer> mainTaskIds = new ArrayList<>(200);
            Cfg_Task_Bean main_taskBean;
            do {
                main_taskBean = CfgManager.getCfg_Task_Container().getValueByKey(initTaskId);
                if (main_taskBean == null) {
                    log.error("основное задание пусто task={}", initTaskId);
                    return -2;
                }
                mainTaskIds.add(initTaskId);
                initTaskId = main_taskBean.getPost_task_id();
                if (mainTaskIds.contains(initTaskId)) {
                    log.error("основное задание" + main_taskBean.getTask_id() + " следующее задание повторяется！" + initTaskId);
                    log.error(mainTaskIds);
                    return -1;
                }
            } while (initTaskId > 0);
            isTaskInit = true;
            log.error(mainTaskIds);
        }
        return 0;
    }

    /**
     * @param player
     * @param modelId
     */
    private boolean newToTask(Player player, int modelId) {
        int result = checkTaskId();
        if (result < 0) {
            return false;
        }
        //0.1можно двигаться только вперёд
        Cfg_Task_Bean q_mainBean = CfgManager.getCfg_Task_Container().getValueByKey(modelId);
        if (q_mainBean == null) {
            MessageUtils.notify_player(player, Notify.ERROR, "Это заданиеIDне найдено в системной конфигурации！заданиеid = " + modelId);
            return false;
        }
        //уже выполнено
        if (player.getOverMainTaskIDs().contains(modelId)) {
            log.error(TaskHelp.getPlayerInfo(player) + "при использовании Gm newTotask ，это задание уже завершено:" + modelId);
            return false;
        }

        //текущее задание отсутствует，автоматически принять следующее задание
        if (player.getCurMainTasks().isEmpty()) {
            int size = player.getOverMainTaskIDs().size();
            if (size <= 0) {
                log.error(TaskHelp.getPlayerInfo(player) + "при использовании gm newtotask  нет задания，и список уже завершённых основных заданий также пуст！");
                return false;
            }
            int finalTaskId = player.getOverMainTaskIDs().get(player.getOverMainTaskIDs().size() - 1);
            Cfg_Task_Bean finalMainTaskBean = CfgManager.getCfg_Task_Container().getValueByKey(finalTaskId);
            if (finalMainTaskBean == null) {
                log.error("newtotask   конфигурация основного задания не существует！" + finalTaskId);
                return false;
            }
            int nextTaskId = finalMainTaskBean.getPost_task_id();
            if (nextTaskId == 0 || nextTaskId == -1) {
                log.error(TaskHelp.getPlayerInfo(player) + "при использовании  gm newtotask  ，сейчас задания нет，и задания быть не должно，сначала выполните специальное действие，например, выберите лагерь！");
                return false;
            }
            Manager.taskManager.deal().acceptTask(player, Task.MAIN_TASK, nextTaskId, 0, false);
        }
        if (player.getCurMainTasks().isEmpty()) {
            log.error("Не удалось принять задание！");
            return false;
        }
        if (player.getCurMainTasks().get(0).getModelId() == modelId) {
            log.error("Текущее задание уже является целевым");
            return false;
        }
        Cfg_Task_Bean q_task_mainBean = null;
        int nexttaskId;
        do {
            if (player.getCurMainTasks() == null || player.getCurMainTasks().isEmpty()) {
                log.error("использованияGMНе удалось отправить завершение основного задания，");
                return false;
            }
            MainTask task = player.getCurMainTasks().get(0);
            if (!task.finishTask(player, true)) {
                log.error("использованияGMНе удалось отправить завершение основного задания，");
                return false;
            }
//            log.info("Завершено задание task={}", task.getModelId());
            q_task_mainBean = CfgManager.getCfg_Task_Container().getValueByKey(task.getModelId());
            nexttaskId = q_mainBean.getPost_task_id();
            if (nexttaskId <= 0) {
                break;
            }
//            Manager.taskManager.deal().acceptTask(player, Task.MAIN_TASK, q_mainBean.getPost_task_id(), 0, false);
        } while (q_task_mainBean.getPost_task_id() != modelId);
        return true;
    }


    //изменить ступень ездового животного
    private void addHorseLayer(Player player, int addLayer) {
        /**
         * TODO:в дальнейшем изменить логику этой функции
         * */
//        Horse horse = player.getHorse();
//        int curLayer = horse.getHorseSteps();
//
//        if (curLayer == 0) {
//            log.error("игрок" + player.getName() + "сейчас ещё нет ездового животного！");
//            return;
//        }
//
//        int afterLayer = curLayer + addLayer;
//        if (afterLayer > Manager.horseManager.deal().getHorseHighestLayer() || afterLayer < 0) {
//            log.error("ошибка данных при повышении ступени ездового животного！");
//            return;
//        }
//
//        //гарантировать, что при addLayer>1 повышении сразу на несколько ступеней навыки каждой ступени будут активированы
//        for (int i = curLayer; i < afterLayer; i++) {
//            horse.getHorseStronger().setHorseLayer(i + 1);
//            horse.setCurLayer(i + 1);
////            horse.addSkillActivated(); //если есть новый навык ездового животного для активации，активировать соответствующий навык ездового животного
//        }
////        horse.getHorseStronger().setCurBless(0);
////        horse.getHorseStronger().setCurUpTimes(0);
//        horse.getHorseStronger().setHorseStar(0);
//        horse.setRideState(HorseRideStateEnum.Ride);
//
//        Manager.horseManager.deal().sendHorseInfo(player);
//        Manager.playerAttAttributeManager.deal().calPlayerAttributs(player, PlayerAttributeType.HORSE); //пересчитать бонусные атрибуты ездового животного
//        Manager.achievementManager.achievementCheck(player, FunctionVariable.HorseLv, horse.getHorseStronger().getHorseLayer()); //проверка достижений
////        manager.taskManager.action(player, Task.ACTION_TYPE_FUNCTION, BranchType.HORSEUPLEVEL.getValue(), horse.getHorseStronger().getHorseLayer());
//        Manager.playerManager.savePlayer(player, SavePlayerLevel.RightNow);
    }

    //установить уровень преобразования ездового животного
    private void setHorseStarLv(Player player, int starLv) {
        /**
         * TODO:в дальнейшем изменить логику этой функции
         * */
//        Horse horse = player.getHorse();
//        if (horse.getHorseStronger().getHorseLayer() == 0) {
//            log.error("игрок" + player.getName() + "сейчас ещё нет ездового животного！");
//            return;
//        }
//
//        int afterLevel = horse.getHorseStronger().getHorseStar() + starLv;
//        if (afterLevel > Manager.horseManager.deal().getHorseMaxStarLv()) {
//            MessageUtils.notify_player(player, Notify.ERROR, MessageString.IllusionLevelFull);
//            return;
//        }
//
//        horse.getHorseStronger().setHorseStar(afterLevel);
//        Manager.horseManager.deal().sendHorseInfo(player);
//        Manager.playerAttAttributeManager.deal().calPlayerAttributs(player, PlayerAttributeType.HORSE); //пересчитать бонусные атрибуты ездового животного
//        Manager.achievementManager.achievementCheck(player, FunctionVariable.MountStar, horse.getHorseStronger().getHorseStar()); //проверка достижений
//        Manager.playerManager.savePlayer(player, SavePlayerLevel.RightNow);
    }


    /**
     * имитация входа в подземелье
     *
     * @param player
     * @param command
     */
    private void enterCopyMap(Player player, String[] command) {
        if (command.length < 2) {
            log.error("Неверное количество переданных параметров！");
            return;
        }
        int modelId = Integer.parseInt(command[1]);
        int param = 0;
        if (command.length > 2) {
            param = Integer.parseInt(command[2]);
        }
        ReqEnterZone.Builder messInfo = ReqEnterZone.newBuilder();
        messInfo.setModelId(modelId);
        try {
            Manager.copyMapManager.manager().onReqCopyMapEnter(player, messInfo.build().getModelId(), param);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public void quitCopyMap(Player player) {
        Manager.copyMapManager.manager().onReqCopyMapOut(player);
    }

    private void tt(Player player) {
        if (!ServerConfig.isTestServer()) {
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        int oldLevel = player.getLevel();
        long oldExp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.EXP);
        int nowLevel = Global.PlayerMaxLevel;
        player.getCurrencys().set(ItemCoinType.EXP, 0);
        player.setLevel(nowLevel);
        Manager.playerManager.manager().playerLevelUp(player, oldLevel, oldExp, nowLevel, oldExp, 0, ItemChangeReason.GM, actionId);

        long now = TimeUtils.Time();
        boolean isCanAdd = now - useGMttTime <= 5 * 1000;
        useGMttTime = now;
        if (player.getUsedActiveCodeList().contains("GMTT")) {
            if (!isCanAdd) {
                return;
            }
            //пересчитать атрибуты
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.BASE);

            //установить здоровье на максимум
            player.setCurHp(player.getAttribute().MaxHP());
            player.onHpChange(null);
            return;

        }
        //затем вернуть уровень к исходному
        player.getCurrencys().set(ItemCoinType.EXP, 0);
        player.getUsedActiveCodeList().add("GMTT");

        Manager.backpackManager.manager().openBagCellSuccess(player, Global.Born_Bag_Num.get(1), (byte) 1, 0, actionId);

        //усилить экипировку до 3 звёзд
        Iterator<Item> iter = player.getBackpackItems().values().iterator();
        while (iter.hasNext()) {
            Item item = iter.next();
            if (item instanceof Equip) {
                Equip equip = (Equip) item;
//                equip.calResolveGet();
                equip.setBind(true);
//                EquipMessage.ResEquipUpStarSuccess.Builder msg = EquipMessage.ResEquipUpStarSuccess.newBuilder();
//                msg.setEquipId(equip.getId());
//                msg.setIsBinded(true);
//                MessageUtils.send_to_player(player, EquipMessage.ResEquipUpStarSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//                MessageUtils.send_to_player(player, EquipMessage.ResEquipUpStarSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//                MessageUtils.send_to_player(player, EquipMessage.ResEquipUpStarSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
        /**
         * рефакторинг системы экипировки——в EquipPart добавить приватную переменную Equip，обозначающую экипировку, надетую в этом слоте，null означает, что ничего не надето
         * huangzhaomin 2019/04/30
         * исходный код：
         * for (Equip equip : player.getEquips()) {
         *             if (equip != null) {
         * */
        for (EquipPart equipPart : player.getEquipParts()) {
//            equip.calResolveGet();
            if (equipPart != null && null != equipPart.getEquip()) {
                equipPart.getEquip().setBind(true);
            }
//            EquipMessage.ResEquipUpStarSuccess.Builder msg = EquipMessage.ResEquipUpStarSuccess.newBuilder();
//            msg.setEquipId(equip.getId());
//            msg.setIsBinded(true);
//            MessageUtils.send_to_player(player, EquipMessage.ResEquipUpStarSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//            MessageUtils.send_to_player(player, EquipMessage.ResEquipUpStarSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//            MessageUtils.send_to_player(player, EquipMessage.ResEquipUpStarSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }

        //пересчитать атрибуты
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.EQUIP);

        //установить здоровье на максимум
        player.setCurHp(player.getAttribute().MaxHP());
        player.onHpChange(null);
    }

    //GMдобавитьbuff
    private void addBuff(Player player, String[] command) {
        int buffId = 0;
        Fighter tar = player;
        if (command.length >= 2) {
            buffId = Integer.parseInt(command[1]);
        }
        if (command.length >= 3) {
            MapObject m = MapManager.getInstance().getMap(player.gainMapId());
            if (m != null) {
                long id = Long.parseLong(command[2]);
                Fighter tt = m.getPlayer(id);
                if (tt == null) {
                    tt = m.getMonster(id);
                }
                if (tt != null) {
                    tar = tt;
                }
            }
        }
        log.info("GM добавитьbuff" + buffId);
        Manager.buffManager.deal().onAddBuff(player, tar, buffId);
    }

    private void enterGuildMap(Player player) {
        Manager.guildsManager.manager().reqEnterGuildBase(player);
    }

    //GMвойти на мировую карту
    private void enterMap(Player player, String[] command) {
        int mapModelId = Integer.parseInt(command[1]);
        Position target = null;
        if (command.length == 4) {
            float x = Float.parseFloat(command[2]);
            float y = Float.parseFloat(command[3]);
            target = new Position(x, y);
        }

        log.info(player.nameIdString() + ",GM войти на карту：" + mapModelId, target);
        Cfg_Mapsetting_Bean mapCfg = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapModelId);
        if (null == mapCfg) {
            return;
        }

        if (mapCfg.getType() != MapDefine.WORLD_MAP) {
            return;
        }

        Manager.mapManager.changeMap(player, mapModelId, target, -1, false);
    }

    //GMперейти на линию
    private void enterLine(Player player, String[] command) {
        int line = Integer.parseInt(command[1]);
        player.setGM(true);
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map.getType() != MapDefine.WORLD_MAP) {
            return;
        }
        if (map.getLineId() == line) {
            return;
        }
        log.info("GM войти на карту line：" + line + "modelId:" + player.getModelId());
        Manager.mapManager.changeMap(player, player.getModelId(), line, map.getBrithPos(), -1);
    }

    private void showshop(Player player, String[] command) {
        ShopBean shop = Manager.shopManager.getShopBean(Integer.parseInt(command[1]));
        if (shop == null)
            MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "Товар не найден");
        else
            MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, shop.toString());
    }

    //отправить текущее здоровье главного персонажа、уровень（общий уровень、ступень перерождения）、координаты（x，y，картаid）、опыт
    private void sendMeInfo(Player player) {
        String str = "curHP:" + player.getCurHp() + "；" + "картаid：" + player.gainMapModelId() + " PosX:"
                + player.gainX() + " PosY:" + player.gainY() + ";EXP" + Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.EXP);
        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, str, 0);
    }

    //GmGM-команда отправки письма игроку( sendmail mailTitle mailContent mailAttach (ex: sendMail Награда за убийство монстра Ха-ха，поздравляем, монстр убит хозяином，однако хозяин действительно беспощаден。 1020,10,1;5001,5,1;50001,1,1) )
    private void sendMailToPlayer(Player player, String[] command) {
        if (command.length < 3 || command.length > 4) { //заголовок и содержимое письма обязательны，вложение необязательно。в заголовке и содержимом не должно быть пробелов，
            log.error("Ошибка команды отправки письма！");
            return;
        }
        String mailTitle = command[1];
        String mailContent = command[2];
        List<Item> itemList = null;
        if (command.length == 4) {
            String strAttach = command[3];
            itemList = Item.createItems(strAttach, Symbol.FENHAO, Symbol.DOUHAO);
        }
        Manager.mailManager.sendMailTo(player.getId(), 1, "Gmотправлено GM-командой", mailTitle, mailContent, itemList, 0, ItemChangeReason.GM, 0);
    }

    private void sendMailToPlayer1(Player player, String[] command) {
        String mailTitle = command[1];
        String mailContent = command[2];
        List<Item> itemList = null;
        if (command.length == 4) {
            String strAttach = command[3];
            List<List<Integer>> list = new ArrayList<>();
            for (String str : strAttach.split(Symbol.FENHAO)) {
                String[] str1 = str.split(Symbol.DOUHAO);
                List<Integer> l = new ArrayList<>();
                l.add(Integer.parseInt(str1[0]));
                l.add(Integer.parseInt(str1[1]));
                list.add(l);
            }

            itemList = Item.createItems(Utils.toReadIntegerArrayEsByList(list));
        }
        Manager.mailManager.sendMailTo(player.getId(), 1, "Gmотправлено GM-командой", mailTitle, mailContent, itemList, 0, ItemChangeReason.GM, 0);
    }


    //установить pkзначение
    private void setPkValue(Player player, String[] command) {
        int value = Integer.parseInt(command[1]);
        if (value >= 0) {
            long actionID = IDConfigUtil.getLogId();
//            manager.currencyManager.decGoodEvil(player, player.getGoodEvil(), ItemChangeReason.GM, actionID, ItemCoinType.GoodEvil);
            player.getCurrencys().set(ItemCoinType.GoodEvil, 0);
            Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.GoodEvil, value, ItemChangeReason.GM, actionID);
        }
    }

    //получить текущую позицию
    private void getPosInfo(Player player, String[] command) {
        if (player.isTestPos()) {
            player.setTestPos(false);
        } else {
            player.setTestPos(true);
        }
        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, "координаты：" + player.gainCurPos().toString(), 0);
    }

    //установить время сервера
    private boolean setTime(String[] command) throws ParseException, Exception {
        long setTime = TimeUtils.Time();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Date d = sdf.parse(command[1]);
        if (setTime > d.getTime()) {
            return false;
        }
        TimeUtils.setTime(d.getTime());
        return true;
    }

    //удалитьbuff
    private void removeBuff(Player player, String[] command) {
        int buffID = Integer.parseInt(command[1]);
        Manager.buffManager.deal().onRemoveBuff(player, buffID);
    }

    private void sendTpWorldScript(Player player, String str) {

    }

    //убить окружающих монстров не из своего лагеря
    private void killRound(Player player, String[] command) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        List<Fighter> members = null;
        if (command.length > 1) {
            int modelId = Integer.parseInt(command[1]);
            members = new ArrayList<>();
            Iterator<Monster> monsters = map.getMonsters().values().iterator();
            while (monsters.hasNext()) {
                Monster monster = monsters.next();
                if (null == monster) {
                    continue;
                }
                if (monster.getModelId() != modelId) {
                    continue;
                }
                if (player.getCamp() == monster.getCamp()) {
                    continue;
                }
                members.add(monster);
            }
        } else {
            members = getRandomFighter(player, map);
        }
        if (members.size() <= 0) {
            return;
        }
        for (Fighter beAttacker : members) {
            if (beAttacker.getCamp() == player.getCamp()) {
                continue;
            }
            if (!(beAttacker instanceof Monster)) {
                continue;
            }
            long damage = beAttacker.getCurHp();
            beAttacker.setCurHp(0);
            beAttacker.beAttack(player, null, 1, damage);
            if (beAttacker.getCurHp() <= 0) {
                beAttacker.setCurHp(0);
                beAttacker.doDie(player);
                MapUtils.sendDead(player, beAttacker);
            }
            beAttacker.onHpChange(null);
            MapUtils.sendBuffHp(beAttacker, beAttacker, 100000000);
        }
    }

    private void killself(Player player) {
        player.setCurHp(0);
        player.doDie(player);
        MapUtils.sendDead(player, player);
    }

    private void hatrednum(Player player, String[] command) {
        if (command.length < 2) {
            return;
        }
        String name = command[1];
        Player thisplayer = null;
        for (Entry<Long, Player> entry : Manager.playerManager.getPlayersCache().entrySet()) {
            if (name.compareTo(entry.getValue().getName()) == 0) {
                thisplayer = entry.getValue();
                break;
            }
        }
        if (thisplayer == null) {
            return;
        }
        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, thisplayer.getName() + " размер списка агрессии:：" + thisplayer.getHatreds().size(), 0);
    }

    private void addGuildExp(Player player, String[] command) {
        if (command.length < 2) {
            return;
        }

        if (player.getGuildId() <= 0) {
            return;
        }
        int num = Integer.parseInt(command[1]);


        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        Manager.guildsManager.changeGuildExpCommand(guild, num, ItemChangeReason.GM, player.getId());
    }


    private void other(Player player, String[] command) {
        if (command.length < 2) {
            return;
        }
        String name = command[1];
        Player thisplayer = null;
        for (Entry<Long, Player> entry : Manager.playerManager.getPlayersCache().entrySet()) {
            if (name.compareTo(entry.getValue().getName()) == 0) {
                thisplayer = entry.getValue();
                break;
            }
        }
        if (thisplayer == null) {
            return;
        }
        MapObject map = Manager.mapManager.getMap(thisplayer.gainMapId());
        boolean isBlock = Utils.isCanMove(map, thisplayer.gainCurPos());
        String sessionState = "пусто";
        String str = "игрок:" + thisplayer.getName() + "；curHP:" + thisplayer.getCurHp() + "；" + "картаid："
                + thisplayer.gainMapModelId() + " line:" + thisplayer.gainLine() + " PosX:" + thisplayer.gainX() + " PosY:" + thisplayer.gainY() + ";EXP" + Manager.currencyManager.manager().getCurrencyNum(thisplayer, ItemCoinType.EXP)
                + "；можно ли перемещаться：" + isBlock + " sessionState:" + sessionState + " session:" + thisplayer.getIosession();
        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, str, 0);
    }

    //добавить объект сбора
    private void cell(Player player, String[] command) {
        int cellId = Integer.parseInt(command[1]);
        Item item = Manager.backpackManager.manager().getItemByCellId(player, cellId);
        if (item == null) {
            Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, "Предмет не найден cellId:" + cellId, 0);
            return;
        }
        Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, item.toString(), 0);
    }

    //добавить объект сбора
    private void addGather(Player player, String[] command) {
        int gatherID = Integer.parseInt(command[1]);
        Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(gatherID);
        if (null == gatherCfg) {
            log.error("Инициализация объекта сбора" + gatherID + "не удалась， такой конфигурации нет");
            return;
        }
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        Manager.gatherManager.deal().createGather(map, gatherCfg, player.gainCurPos());
    }

    /**
     * проверить запись пополнения
     */
    private void queryrecharge(Player player, String[] command) {
        String orderId = command[1];
        if (Manager.rechargeManager.getRechargeMap().containsKey(orderId)) {
            MessageUtils.notify_player(player, Notify.CHAT, orderId + "уже существует в таблице пополнений!！");
        } else {
            MessageUtils.notify_player(player, Notify.CHAT, orderId + "не существует в таблице пополнений!！");
        }
    }

    //пополнение
    private void recharge(Player player, String[] command) {
        if (!ServerConfig.isTestServer()) {
            return;
        }

        int num = command.length >= 2 ? Integer.parseInt(command[1]) : 100;
        Recharge recharge = new Recharge();
        recharge.setOrder_no("GM_" + TimeUtils.Time());
        recharge.setGoods_id(123);
        recharge.setGoods_type("1");
        recharge.setGoods_ext("");
        recharge.setGoods_code("0");
        recharge.setTotal_fee(num);
        recharge.setItem_id(0);
        recharge.setGame_money(num);
        recharge.setRole_id(player.getId());
        recharge.setExt_param("gm");
        recharge.setSign_type("1");
        recharge.setSign("tiancheng==");
        recharge.setMoney_type("CNY");
        recharge.setTotalRecharge(num);
        recharge.setTotalVipPower(num);
        Manager.rechargeManager.AddRecharge(null, recharge, JsonUtils.toJSONString(recharge), RechargeDefine.SRC_GM);
    }

    private void rechargeId(Player player, String[] command) {
        if (command.length < 2)
            return;

        if (!ServerConfig.isTestServer()) {
            return;
        }

        int id = Integer.parseInt(command[1]);
        RechargeItemInfo cfg = Manager.rechargeManager.getRechargeItemInfoMap().get(id);
        if (cfg == null) {
            log.error("RechargeItemInfo  не существует  {} ", id);
            return;
        }


        Recharge recharge = new Recharge();
        recharge.setOrder_no("GM_ID_" + TimeUtils.Time());
        recharge.setGoods_id(id);
        recharge.setGoods_type("1");
        recharge.setGoods_ext("");
        recharge.setTotal_fee(Manager.rechargeManager.deal().getMoney(player, cfg, "CNY"));
        recharge.setGoods_code(command[1]);
        recharge.setGoods_name(cfg.getGoods_name());
        recharge.setItem_id(0);
        recharge.setGame_money(0);
        recharge.setRole_id(player.getId());
        recharge.setExt_param("gm");
        recharge.setSign_type("1");
        recharge.setSign("tiancheng==");
        recharge.setMoney_type("CNY");
        Manager.rechargeManager.AddRecharge(null, recharge, JsonUtils.toJSONString(recharge), RechargeDefine.SRC_GM);
    }

    private void lp(Player player, String[] command) {
        if (command.length < 2)
            return;

        String order = command[1];
        if (order.equals("add"))
            Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.ActivePoint, Integer.parseInt(command[2]), ItemChangeReason.GM, IDConfigUtil.getLogId());
        else if (order.equals("enter"))
            Manager.leaderPreachManager.getScript().onReqLeaderPreachEnter(player);
    }

    private void lucky(Player player, String[] command) {
        if (command.length < 2)
            return;
        int add = Integer.parseInt(command[1]);
        player.getOpenServerSpec().addLuckyValue(add);
        OpenServerAcManager.getInstance().deal().playerOnline(player);
    }

    private void rr(Player player, String[] command) {
        if (command.length < 4)
            return;

        String cmd = command[1];
        int type = Integer.parseInt(command[2]);
        long num = Long.parseLong(command[3]);
        if (cmd.equals("last")) {
            // установить данные за вчера
            RetrieveResData data = player.getLastRRD();
            data.setTime(TimeUtils.Time() - GlobalType.MILLIS_PER_DAY);
            data.getMap().put(type, num);
        } else if (cmd.equals("now")) {
            // установить данные за сегодня
            RetrieveResData data = player.getCurRRD();
            data.getMap().put(type, num);
        }
    }

    private void bi(Player player, String[] command) {
        Manager.biManager.getScript().example(player);
    }

    private void chum(Player player, String[] command) {
        if (command.length < 2)
            return;

        String order = command[1];
        if (order.equals("addexp")) {
            Manager.chumManager.getScript().addActiveValue(player, Integer.parseInt(command[2]));
        } else if (order.equals("help")) {
            Manager.chumManager.getScript().helpReward(player, ChumPrivilege.FB_TASK);
        } else if (order.equals("call")) {
            Manager.chumManager.getScript().onReqCallSoul(player);
        }
    }

    private void queryitem(Player player, String[] command) {
        if (command.length < 2)
            return;

        int itemID = Integer.parseInt(command[1]);
        int count = Manager.backpackManager.manager().getItemNum(player, itemID);
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "ID:" + itemID + " Count:" + count);
    }

    /**
     * комплекс коммерциализации
     *
     * @param player
     * @param command
     */
    private void business(Player player, String[] command) {
        if (command.length < 2)
            return;

        DailyRechargeActivity activity = null;
        switch (command[1]) {
            case "gf_times":
                int num = Integer.parseInt(command[2]);
                for (int i = 0; i < num; i++) {
                    ((IGrowthFundScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.GrowthFund)).add();
                }
                break;
            case "dci":
                // перейти к отметке следующего дня
                DayCheckIn checkIn = player.getDayCheckIn();
                checkIn.setFirstTime(checkIn.getFirstTime() - GlobalType.MILLIS_PER_DAY);
                MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "success");
                break;
            case "fc":
                // проверить данные первой/повторной покупки
                MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, JsonUtils.toJSONString(player.getFcCharge()));
                break;
            case "fc_first":
                // первая покупка — перейти к следующему этапу
                player.getFcCharge().getFirst().setStartTime(TimeUtils.Time() - GlobalType.MILLIS_PER_DAY);
                Manager.commercializeManager.playerOnline(player);
                break;
            case "fc_next":
                // повторная покупка — перейти к следующему этапу
                player.getFcCharge().getNext().setStartTime(TimeUtils.Time() - GlobalType.MILLIS_PER_DAY);
                Manager.commercializeManager.playerOnline(player);
                break;
            case "dr_next":
                // переключиться на следующий день
                activity = Manager.commercializeManager.getPlayerDailyAccRecharge(player.getId());
                activity.setLastResetTime(TimeUtils.Time() - GlobalType.MILLIS_PER_DAY);
                for (DailyRechargeStage stage : activity.getStageList()) {
                    stage.setLastModifyTime(0);
                }
                ICommercialize script = Manager.commercializeManager.getScript(CommercializeMessage.Commercialize.DailyRecharge);
                if (script != null)
                    script.onReqCommercialize(player);
                break;
            case "dr":
                // проверить данные ежедневного накопительного пополнения
                activity = Manager.commercializeManager.getPlayerDailyAccRecharge(player.getId());
                if (activity == null)
                    MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "У этого игрока ещё нет данных ежедневного накопительного пополнения!！");
                else
                    MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, JsonUtils.toJSONString(activity));
                break;
            case "dr_reward":
                // получить награду ежедневного накопительного пополнения
                if (command.length >= 3) {
                    int awardId = Integer.parseInt(command[2]);
                    Manager.commercializeManager.dailyRecharge().onReqDailyAccRechargeAward(player, awardId);
                }
                break;
            case "dr_ver":
                // проверить версию ежедневного накопительного пополнения
                int day = TimeUtils.getOpenServerDay();
                Cfg_Recharge_daily_total_Bean cfg = null;
                for (Cfg_Recharge_daily_total_Bean bean : Cfg_Recharge_daily_total_Container.GetInstance().getValuees()) {
                    if (bean.getStartTime() <= day && day < bean.getEndTime()) {
                        cfg = bean;
                        break;
                    }
                }

                if (cfg != null)
                    MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL,
                            JsonUtils.toJSONString("s:" + cfg.getStartTime() + " e:" + cfg.getEndTime()));
                else
                    MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, JsonUtils.toJSONString("Действующие данные не найдены"));
                break;
            case "dr_reset":
                // загрузить копию данных активности из таблицы конфигурации
                activity = Manager.commercializeManager.getPlayerDailyAccRecharge(player.getId());
                Manager.commercializeManager.dailyRecharge().saveDailyAccRecharge(activity);
                business(player, new String[]{"business", "dr_ver"});
                break;
            default:
                break;
        }
    }

    /**
     * динамически корректировать код，выполнять любую команду
     *
     * @param player
     * @param command
     */
    private void any(Player player, String[] command) {
        DayCheckIn checkIn = player.getDayCheckIn();
        checkIn.setLastTime(0);
        checkIn.setFirstTime(checkIn.getFirstTime() - CfgManager.getCfg_Sign_reward_Container().size() * (long) GlobalType.MILLIS_PER_DAY);
        checkIn.getCheckIns().clear();
        checkIn.getCheckIn2s().clear();
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "success");
    }

    //Информация о карте
    private void maplines(Player player, String[] command) {
        String args = "Список линий текущей карты：\n";
        int body = 0;
        for (MapObject map : Manager.mapManager.getWorldMaps().get(player.gainMapModelId())) {
            String cell = map.getLineId() + " линия　" + map.getPlayers().size() + "  чел. \n";
            args += cell;
            body += map.getPlayers().size();

        }
        args += "Всего линий：" + Manager.mapManager.getWorldMaps().get(player.gainMapModelId()).size() + " всего игроков：" + body;
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, args);
    }

    //добавить игроку близость
    private void addIntimacy(Player player, int add) {
        long playerId = player.getId();
        PlayerRelation playerRelation = Manager.friendManager.getPlayerRelation(playerId);
        for (long id : playerRelation.getFriends().keySet()) {
            Manager.friendManager.deal().addIntimacy(player, id, add);
        }
    }

    private void hideme(Player player) {
        if (player.isHideMe()) {
            player.setHideMe(false);
            ResMapPlayer.Builder mapPlayer = ResMapPlayer.newBuilder();
            mapPlayer.setPlayer(MapUtils.getPlayerInfo(player));
            MessageUtils.send_to_roundPlayer(player, ResMapPlayer.MsgID.eMsgID_VALUE, mapPlayer.build().toByteArray(), false);

        } else {
            player.setHideMe(true);
            ResPlayerDisappear.Builder msg = ResPlayerDisappear.newBuilder();
            msg.addPlayerIds(player.getId());
            MessageUtils.send_to_roundPlayer(player, ResPlayerDisappear.MsgID.eMsgID_VALUE, msg.build().toByteArray(), false);
        }
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "hideme:" + player.isHideMe() + "  hideOther:" + player.isHideOther());
    }

    private void hideOther(Player player) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        List<Player> players = MapUtils.getRoundPlayer(map, player.gainCurPos());
        if (player.isHideOther()) {
            player.setHideOther(false);
            ResRoundObjs.Builder roundObjs = ResRoundObjs.newBuilder();

            for (Player other : players) {
                roundObjs.addPlayers(MapUtils.getPlayerInfo(other));
                Pet pet = Manager.petManager.getBattlePet(other);
                if (null != pet) {
                    roundObjs.addPets(MapUtils.getPetInfo(pet));
                }
            }
            MessageUtils.send_to_player(player, ResRoundObjs.MsgID.eMsgID_VALUE, roundObjs.build().toByteArray());

        } else {
            player.setHideOther(true);
            ResPlayerDisappear.Builder msg = ResPlayerDisappear.newBuilder();
            for (Player other : players) {
                if (other.getId() == player.getId()) {
                    continue;
                }
                Pet pet = Manager.petManager.getBattlePet(other);
                if (pet != null) {
                    ResPetDisappear.Builder pmsg = ResPetDisappear.newBuilder();
                    pmsg.setId(pet.getId());
                    MessageUtils.send_to_player(player, ResPetDisappear.MsgID.eMsgID_VALUE, pmsg.build().toByteArray());
                }
                msg.addPlayerIds(other.getId());
            }
            MessageUtils.send_to_player(player, ResPlayerDisappear.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "hideme:" + player.isHideMe() + "  hideOther:" + player.isHideOther());
    }


    private void getcity(Player player, int cityId) {
//        KpInfo union = Manager.kingPalaceManager.getKingPalace(player);
//        if (union == null) {
//            return;
//        }
//        //отрицательное значение очищает все захваченные города
//        if (cityId <= 0) {
//            List<City> citys = Manager.cityManager.getOwnCity(player);
//            for (City c : citys) {
//                KpInfo store = Manager.kingPalaceManager.getKingPalace(c.getUnionId());
//                Cfg_City_warBean config = Manager.gameDataManager.Cfg_City_warContainer.GetValueByKey(c.getId());
//                for (Player player2 : Manager.playerManager.getPlayersCache().values()) {
//                    if (store.getGuildList().contains(player2.getId())) {
//                        Manager.buffManager.deal().onRemoveBuff(player2, config.getCitybuff());
//                    }
//                }
//                c.setUnionId(0);
//            }
//            return;
//        }
//        Cfg_City_warBean config = Manager.gameDataManager.Cfg_City_warContainer.GetValueByKey(cityId);
//        if (config == null) {
//            return;
//        }
//        City city = Manager.cityManager.getCitys().get(cityId);
//        if (city == null) {
//            city = new City();
//            city.setId(config.getId());
//            city.setLevel(config.getLevel());
//            Manager.cityManager.getCitys().put(city.getId(), city);
//        }
//        KpInfo oldUnion = Manager.kingPalaceManager.getKingPalace(city.getUnionId());
//        if (oldUnion != null) {
//            for (Player player2 : Manager.playerManager.getPlayersCache().values()) {
//                if (oldUnion.getGuildList().contains(player2.getId())) {
//                    Manager.buffManager.deal().onRemoveBuff(player2, config.getCitybuff());
//                }
//            }
//        }
//        city.setUnionId(union.getKpId());
//        Manager.cityManager.SynInitCityIdsToWorld();
//        Manager.cityManager.OnLoadCityFlag2WorldMap(cityId);
//
//        for (Player player2 : Manager.playerManager.getPlayersCache().values()) {
//            if (union.getGuildList().contains(player2.getId())) {
//                Manager.buffManager.deal().onAddBuff(player2, player, config.getCitybuff());
//            }
//        }

    }

    //получить окружающих Fighter
    public List<Fighter> getRandomFighter(Fighter attacker, MapObject map) {

        List<Fighter> fighters = new ArrayList<>();
        //набор участников боя
        if (null == map) {
            return fighters;
        }

        Iterator<Area> areas = Manager.mapManager.getRounds(map, attacker.gainCurPos()).iterator();
        Monster monster;

        while (areas.hasNext()) {
            Area area = areas.next();
            if (null == area) {
                continue;
            }
            for (Player player : area.getPlayers()) {
                if (null == player) {
                    continue;
                }
                fighters.add(player);
            }

            Iterator<Monster> monsters = area.getMonsters().values().iterator();
            while (monsters.hasNext()) {
                monster = monsters.next();
                if (null == monster) {
                    continue;
                }
                fighters.add(monster);
            }
        }

        return fighters;
    }

    private void addtitle(Player player, String[] command) {
        int titleId = Integer.parseInt(command[1]);
        Cfg_Title_Bean bean = CfgManager.getCfg_Title_Container().getValueByKey(titleId);
        if (bean == null) {
            MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "Этот титул не найден:" + titleId);
            return;
        }

        Manager.titleManager.deal().useTitleItem(player, titleId, 1, ItemChangeReason.GMGet);
        Manager.titleManager.deal().onReqWearTitle(player, titleId);
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "Титул успешно добавлен:" + titleId);
    }

    private void querytitle(Player player, String[] command) {
        TitleData data = player.getTitleData();
        int now = (int) (TimeUtils.Time() / 1000);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : data.getTitleList().entrySet()) {
            String name = "";
            Cfg_Title_Bean bean = CfgManager.getCfg_Title_Container().getValueByKey(entry.getKey());
            if (bean != null) {
                name = bean.getName();
            }
            sb.append(name).append(":").append(entry.getKey()).append(":").append(entry.getValue() - now).append("/");
        }
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, sb.toString());
    }

    private void querycoin(Player player, String[] command) {
        long v = Manager.currencyManager.manager().getCurrencyNum(player, Integer.parseInt(command[1]));
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, command[1] + " : " + v);
    }

    //прогресс титула
    private void titleprocess(Player player, String[] command) {
//        int process = Integer.parseInt(command[1]);
//        Manager.titleManager.addActiveTitleProcessValue(player, FunctionVariable.ConsumeBindDiamonds, process);
    }

    //атаковать окружающих
    private void attackRound(Player player, String[] command) {
        int hp = Integer.parseInt(command[1]);
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        List<Fighter> members = getRandomFighter(player, map);

        if (members.size() <= 0) {
            return;
        }
        if (hp > 2000000000) {
            hp = 2000000000;
        }
        for (Fighter beAttacker : members) {
            if (beAttacker.getCamp() == player.getCamp()) {
                continue;
            }
            if (!(beAttacker instanceof Monster)) {
                continue;
            }
            beAttacker.setCurHp(beAttacker.getCurHp() - hp);
            if (beAttacker.getCurHp() <= 0) {
                beAttacker.setCurHp(0);
                beAttacker.doDie(player);
                MapUtils.sendDead(player, beAttacker);
            }
            beAttacker.onHpChange(null);
            MapUtils.sendBuffHp(player, beAttacker, hp);
        }
    }

    @Override
    public void OnPublicGMBack(ChannelHandlerContext context, P2GGMCMDResult mess) {
        log.error("public  GM возврат результата выполнения GM-команды！");
        long roleId = mess.getRoleId();
        if (roleId < 1) {
            log.error(" GM Результат выполнения GM-команды:：" + mess.getMessStr());
            return;
        }
        if (mess.getState() == 3) {
            log.error(" GM Результат выполнения GM-команды:：" + mess.getMessStr());
            return;
        }

        Player player = Manager.playerManager.getPlayerOnline(roleId);
        if (player == null) {
            log.error(" GM Результат выполнения GM-команды:：" + mess.getMessStr());
            return;
        }

        MessageUtils.notify_player(player, Notify.CHAT, mess.getMessStr());
    }

    //войти на кросс-сервер
    private void entercross(Player player, String[] command) {
        int cloneId = Integer.parseInt(command[1]);
//        ReqCreateFight.Builder messInfo = ReqCreateFight.newBuilder();
//        messInfo.setZoneModelId(cloneId);
//        manager.scriptManager.call(ScriptEnum.CrossFightScript, "OnReqCreateFight", null, messInfo, player);
//        manager.crossServerManager.crossFightdeal().OnReqCreateFight(null, player, messInfo.build());
    }

    //телепортироваться к ближайшему игроку
    private void findaround(Player player, String[] command) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        Player mdp = null;
        float mindis = 0;
        for (Player p : map.getPlayers().values()) {
            if (p.getId() == player.getId()) {
                continue;
            }
            if (mdp == null) {
                mdp = p;
                mindis = Utils.getDistance(player.gainCurPos(), p.gainCurPos());
                continue;
            }
            float dis = Utils.getDistance(player.gainCurPos(), p.gainCurPos());
            if (dis < mindis) {
                mdp = p;
                mindis = dis;
            }
        }

        if (mdp == null) {
            return;
        }

        if (map == null || map.getType() == MapDefine.COPY_MAP) {
            log.error("moveTo null or copymap");
            return;
        }
        boolean block = Utils.isCanMove(map, mdp.gainCurPos());
        if (!block) {
            log.error("moveTo непроходимая точка：" + mdp.gainCurPos());
            return;
        }

        Manager.mapManager.changeMap(player, map.getMapModelId(), mdp.gainCurPos(), -1, false);

    }

    /**
     * изменение времени, отправляемое игровым сервером на боевой сервер
     *
     * @param context
     */
    @Override
    public void OnFightGM(ChannelHandlerContext context, G2FGMdeal gfgm) {
        long roleId = gfgm.getRoleId();

        String mess = gfgm.getPara();
        String cmd = gfgm.getCmd();
        String[] param = gfgm.getPara().split(" ");
        switch (cmd) {
            case "&dailybegin":
                dailyBegin(param);
                break;
            case "&dailyend":
                dailyEnd(param);
                break;
            case "&getfsidtime":
            case "&getfighttime": {
                mess += "Текущее системное время боевого сервера:" + TimeUtils.NowToString() + ",текущее системное время в миллисекундах:" + TimeUtils.Time();
            }
            break;
            case "&setfsidtime":
            case "&setfighttime": {
                try {
                    if (setTime(param)) {
                        mess += "Текущее системное время боевого сервера:" + TimeUtils.NowToString();
                    } else {
                        mess += "Текущее системное время боевого сервера:" + TimeUtils.NowToString() + "， не удалось установить время боевого сервера！";
                    }
                } catch (Exception e) {
                    log.error("Ошибка установки времени боевого сервера!！");
                    mess += "Текущее системное время боевого сервера:" + TimeUtils.NowToString() + "，исключение при установке времени боевого сервера";
                }
            }
            break;
            case "&fightscript": {
                try {
                    int scriptId = Integer.parseInt(param[1]);
                    boolean is = Manager.scriptManager.reload(scriptId);
                    if (is) {
                        mess += "Скрипт загружен успешно!！";
                        YedMgr.getInstance().cleanFuncsIndex++;
                    } else {
                        mess += "Не удалось загрузить скрипт!！";
                    }
                } catch (Exception e) {
                    log.error(e, e);
                    mess += "При загрузке скрипта возникло исключение!！";
                }
            }
            break;
            default:
                break;
        }
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(roleId);
        ChatMessage.PersonalNotice.Builder msg = ChatMessage.PersonalNotice.newBuilder();
        msg.setType(Notify.CHAT.getValue());
        msg.setContent(MessageString.WANGNENGTISHI);
        ChatMessage.paramStruct.Builder info = ChatMessage.paramStruct.newBuilder();
        String[] tt = mess.split("&_");
        if (tt.length < 2) {
            info.setMark(0);
            info.setParamsValue(mess);
        } else {
            info.setMark(Integer.parseInt(tt[0]));
            info.setParamsValue(tt[1]);
        }
        msg.addValue(info);
        FightClientManager.GetInstance().send_to_player(context, roleIds, ChatMessage.PersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray(), roleId);
    }

    private void relive(Player player, String[] command) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        player.reset();
        MapMessage.ResRelive.Builder msg = MapMessage.ResRelive.newBuilder();
        msg.setPlayerId(player.getId());
        msg.setMapId(map.getMapModelId());
        msg.setCurPos(MapUtils.getPos(player.gainCurPos()));
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResRelive.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
    }


    /**
     * активировать питомца
     *
     * @param player
     * @param petId
     */
    public void getPet(Player player, int petId) {
        //открытие системы
        player.setLevel(800);
        //активировать питомца
        Manager.petManager.deal().petAction(player, 1, petId, true);
        Manager.petManager.deal().petAction(player, 3, petId, true);
    }

    public boolean addAppointEquip(Player player, String[] command) {
        int id = Integer.parseInt(command[1]);
        if (command.length < 2) {
            return false;
        }

        boolean bind = false;
        bind = Integer.parseInt(command[2]) == 1;

        return Manager.equipManager.deal().sendAppointEquip(player, id, bind);
    }

    private void getMapLines(Player player, String[] command) {
        int mapModelId = Integer.parseInt(command[1]);
        StringBuilder builder = new StringBuilder();
        builder.append("Линии:");
        for (MapObject mapObject : Manager.mapManager.getWorldMaps().get(mapModelId)) {
            builder.append("_" + mapObject.getLineId());
        }
        MessageUtils.notify_player(player, Notify.CHAT, builder.toString());
    }

    private void invitedOtherAround(Player player, String[] command) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        List<Area> areas = Manager.mapManager.getRounds(map, player.gainCurPos());
        List<Player> players = new ArrayList<>();
        for (Area area : areas) {
            players.addAll(area.getPlayers());
        }
        for (Player temp : players) {
            if (temp.getId() == player.getId()) {
                continue;
            }
            Manager.horseManager.addOneToMultiPlayerHorse(player.getId(), temp.getId());
        }
    }

    private void newFinishTask(Player player, String[] command) {
        if (command.length < 2) {
            return;
        }
        int targetId = Integer.parseInt(command[command.length == 2 ? 1 : 2]);
        boolean result = newToTask(player, targetId);
        if (player.getCurMainTasks().size() < 1) {
            log.error(player.nameIdString() + "， нет последующих заданий ！");
            return;
        }
        MainTask mainTask = player.getCurMainTasks().get(0);
        if (targetId == mainTask.getModelId() && !result) {
            mainTask.finishTask(player, true);
        }
    }

    //одним нажатием увеличить атрибуты персонажа，например：питомец，несколько предметов，почта，руны，титулы，медали
    private void oneKeyAddPlayerAtt(Player p) {
        //активировать всех питомцев
        for (Cfg_Pet_Bean b : CfgManager.getCfg_Pet_Container().getValuees()) {
            Manager.petManager.deal().petAction(p, 1, b.getId(), true);
        }
        //добавить все предметы,заполнить рюкзак
        for (Cfg_Equip_Bean e : CfgManager.getCfg_Equip_Container().getValuees()) {
            Item item = Item.createItem(e.getId(), 1, true);
            if (Manager.backpackManager.manager().onHasAddSpace(p, item)) {
                Manager.backpackManager.manager().addItem(p, item, ItemChangeReason.Move, -1);
            } else {
                int storeFirstEmptGridId = Manager.storeManager.manager().getStoreFirstEmptGridId(p);
                if (storeFirstEmptGridId > 0) {
                    item.setGridId(storeFirstEmptGridId);
                    p.getStoreItems().put(item.getGridId(), item);
                }
            }
        }
        //отправить несколько писем
        for (int i = 0; i < 50; i++) {
            Manager.mailManager.sendMailTo(p.getId(), 1, "Система", "Тестовое письмо робота", "Системное тестовое письмо робота", null, 0, ItemChangeReason.GM, 0);
        }
    }

    private boolean clearmybag(Player player) {
        player.getBackpackItems().clear();
        Manager.backpackManager.manager().sendItemInfos(player);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        return true;
    }

    private boolean clearmystore(Player player) {
        player.getStoreItems().clear();
        Manager.storeManager.sendStoreItemInfos(player);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        return true;
    }

    private boolean openAll(Player player) {
        Manager.controlManager.deal().gmFuncOpen(player);
        return true;
    }

    //перемещение
    private void moveTo(Player player, String[] command) {
        if (command.length >= 4) {
            int mapID = Integer.parseInt(command[1]);
            float x = Float.parseFloat(command[2]);
            float y = Float.parseFloat(command[3]);
            moveTo(player, mapID, new Position(x, y));
            return;
        }
        float x = Float.parseFloat(command[1]);
        float y = Float.parseFloat(command[2]);
        Position pos = new Position(x, y);
        log.info(player.getName() + "GM перемещение{" + x + "," + y + "}");
        player.changeCurPos(pos, true);
        MapMessage.ResJumpBlock.Builder msg = MapMessage.ResJumpBlock.newBuilder();
        msg.setId(player.getId());
        msg.setTarget(MapUtils.getPos(pos));
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResJumpBlock.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void moveTo(Player player, int mapID, Position pos) {
        MapObject map = Manager.mapManager.getMap(mapID);
        if (map == null || map.getType() == MapDefine.COPY_MAP) {
            log.error("moveTo null or copymap");
            return;
        }
        boolean block = Utils.isCanMove(map, pos);
        if (!block) {
            log.error("moveTo непроходимая точка：" + pos);
            return;
        }
        if (player.gainMapModelId() == mapID) {
            player.changeCurPos(pos, true);
            MapMessage.ResJumpBlock.Builder msg = MapMessage.ResJumpBlock.newBuilder();
            msg.setId(player.getId());
            msg.setTarget(MapUtils.getPos(pos));
            MessageUtils.send_to_roundPlayer(player, MapMessage.ResJumpBlock.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        } else {
            Manager.mapManager.changeMap(player, mapID, pos, map.getType(), false);
        }
    }

    private void getEquip(Player player, int grade, int quality) {
        Cfg_Equip_Bean[] beans = CfgManager.getCfg_Equip_Container().getValuees();
        List<Item> itemList = new ArrayList<>();
        for (Cfg_Equip_Bean bean : beans) {
            if (bean.getPart() > 7) {
                continue;
            }
            if (bean.getGrade() < 3) {
                continue;
            }
            if (!bean.getGender().contains((int) player.getCareer()) && !bean.getGender().contains(9)) {
                continue;
            }
            if (bean.getGrade() != grade && grade != 0) {
                continue;
            }
            if (quality == 0) {
                quality = 10;
            }
            if (bean.getQuality() != quality) {
                continue;
            }
            if (bean.getDiamond_Number() != 5) {
                continue;
            }
            itemList.add(Item.createItem(bean.getId(), 1, false));
        }
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.GM, 0)) {
            MailManager.getInstance().sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                    MessageString.System, "gm", "gm", itemList, ItemChangeReason.GM);
        }
    }

    private void getXianjia(Player player) {
        Cfg_Equip_Bean[] beans = CfgManager.getCfg_Equip_Container().getValuees();
        List<Item> itemList = new ArrayList<>();
        for (Cfg_Equip_Bean bean : beans) {
            if (bean.getPart() < 30 || bean.getPart() > 50) {
                continue;
            }
            if (!bean.getGender().contains((int) player.getCareer())) {
                continue;
            }
            if (bean.getDiamond_Number() != 10) {
                continue;
            }
            itemList.add(Item.createItem(bean.getId(), 1, false));
        }
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.GM, 0)) {
            MailManager.getInstance().sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                    MessageString.System, "gm", "gm", itemList, ItemChangeReason.GM);
        }
    }

    private void getShengzhuang(Player player) {
        Cfg_Equip_Bean[] beans = CfgManager.getCfg_Equip_Container().getValuees();
        HashMap<Integer, Integer> partEquip = new HashMap<>();
        for (Cfg_Equip_Bean bean : beans) {
            if (bean.getPart() < 100) {
                continue;
            }
            if (!bean.getGender().contains((int) player.getCareer())) {
                continue;
            }
            if (!partEquip.containsKey(bean.getPart())) {
                partEquip.put(bean.getPart(), bean.getId());
                continue;
            }
            if (partEquip.get(bean.getPart()) < bean.getId()) {
                partEquip.put(bean.getPart(), bean.getId());
            }
        }
        List<Item> itemList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : partEquip.entrySet()) {
            itemList.add(Item.createItem(entry.getValue(), 1, false));
        }
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.GM, 0)) {
            MailManager.getInstance().sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                    MessageString.System, "gm", "gm", itemList, ItemChangeReason.GM);
        }
    }

    private void getSBEquip(Player player) {
        List<Item> itemList = new ArrayList<>();
        Cfg_SoulBeastsEquip_Bean[] beans = CfgManager.getCfg_SoulBeastsEquip_Container().getValuees();
        for (Cfg_SoulBeastsEquip_Bean bean : beans) {
            if (bean.getQuality() != 9) {
                continue;
            }
            for (int i = 0; i < 3; i++) {
                itemList.add(Item.createItem(bean.getId(), 1, false));
            }
        }
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.GM, 0)) {
            MailManager.getInstance().sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                    MessageString.System, "gm", "gm", itemList, ItemChangeReason.GM);
        }
    }

    private void intenPetEquip(Player player, int assid, int partId, int intenLv) {
        log.info(assid + "_" + partId + "_" + intenLv);
        player.getActivePet().getAssistants().get(assid).getParts().get(partId).setStrengthLv(intenLv);
        //изменение атрибутов игрока
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
        //вернуть результат
        PetMessage.ResPetEquipStrength.Builder pb = PetMessage.ResPetEquipStrength.newBuilder();
        pb.setAssistantId(assid);
        pb.setCellId(partId);
        pb.setStrengthLv(intenLv);
        MessageUtils.send_to_player(player, PetMessage.ResPetEquipStrength.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    private void soulPetEquip(Player player, int assid, int partId, int soulLv) {
        log.info(assid + "_" + partId + "_" + soulLv);
        player.getActivePet().getAssistants().get(assid).getParts().get(partId).setSoulLv(soulLv);
        //изменение атрибутов игрока
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
        //вернуть результат
        PetMessage.ResPetEquipSoul.Builder pb = PetMessage.ResPetEquipSoul.newBuilder();
        pb.setAssistantId(assid);
        pb.setCellId(partId);
        pb.setSoulLv(soulLv);
        MessageUtils.send_to_player(player, PetMessage.ResPetEquipSoul.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }
}
