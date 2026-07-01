package common.ranklist;

import com.data.*;
import com.data.bean.*;
import com.game.attribute.BaseIntAttribute;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.db.bean.RankPlayer;
import com.game.equip.struct.EquipPart;
import com.game.equip.struct.EquipWash;
import com.game.equip.struct.SpiritInfo;
import com.game.guild.structs.Guild;
import com.game.immortalequip.structs.ImmortalEquipPart;
import com.game.jjc.structs.JJC;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.player.manager.PlayerManager;
import com.game.player.script.IPlayerAttribute;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.PlayerWorldInfo;
import com.game.ranklist.handler.SyncRankPlayerHandler;
import com.game.ranklist.log.RankListLog;
import com.game.ranklist.manager.RankListManager;
import com.game.ranklist.script.IGuildRankScript;
import com.game.ranklist.script.IRankListScript;
import com.game.ranklist.script.IRankScript;
import com.game.ranklist.structs.RankType;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.thread.SaveServer;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.RankListMessage;
import game.message.RankListMessage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 排行榜的脚本接口
 */
public class RankListScript implements IRankListScript {

    private static final Logger log = LogManager.getLogger(RankListScript.class);
    /**
     * 对比属性常量值
     * 参照rank_compare.xlsx
     */
    private final static int RANK_COMPARE_HORSE = 1;
    private final static int RANK_COMPARE_WING = 2;
    private final static int RANK_COMPARE_EQUIP = 3;
    //    private final static int RANK_COMPARE_MAGIC_WEAPON = 4;
//    private final static int RANK_COMPARE_TALISMAN = 5;
//    private final static int RANK_COMPARE_MAGIC = 6;
    private final static int RANK_COMPARE_WEAPON = 7;
    private final static int RANK_COMPARE_GEM = 8;
    private final static int RANK_COMPARE_STRENGTHEN = 10;
    private final static int RANK_COMPARE_OTHER = 11;

    @Override
    public int getId() {
        return ScriptEnum.RankListBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void tick() {
        int curMin = TimeUtils.getDayOfMin(TimeUtils.Time());
        if (curMin % 15 == 0) {
            sortRank(RankType.LEVEL_RANK);
        }

        if (curMin % 60 == 0) {
            sortAllRank();
            dealRankReward();

            Manager.rankListManager.getTopHallRankScript().sortTopHallRank();
            Manager.rankListManager.getUniverseRankScript().checkUniverseStage();
        }

        //0点同步排行榜BI
        int hour = TimeUtils.getDayOfHour(TimeUtils.Time());
        if (hour == 0) {
            synRankBI();
        }
    }

    @Override
    public void sortAllRank() {
        try {
            for (int type : RankListManager.getRankTypeList()) {
                sortRank(type);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void sortRank(int type) {
        if (!RankListManager.getRankTypeList().contains(type)) {
            return;
        }
        ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(type);
        if (rankMap == null) {
            rankMap = new ConcurrentHashMap<>();
            RankListManager.getTempRankMap().put(type, rankMap);
        }

        //公会排行榜特殊处理
        if (type == RankType.GUILD_RANK) {
            //宗派重新排序
            IGuildRankScript guildRankScript = Manager.rankListManager.getGuildRankScript();
            guildRankScript.sortGuildRank();
            return;
        }

        //名人堂不处理
        if (type == RankType.TOPHALL_RANK) {
            return;
        }

        //天墟战场名人堂不处理
        if (type == RankType.UNIVERSE_RANK) {
            return;
        }

        //等级榜需要刷新经验
        if (type == RankType.LEVEL_RANK) {
            for (long roleId : rankMap.values()) {
                RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(roleId);
                Player player = Manager.playerManager.getPlayerOnline(roleId);
                if (player != null && rankPlayer != null) {
                    long exp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.EXP);
                    rankPlayer.setExp(exp);
                }
            }
        }

        int rankMax = Manager.rankListManager.getRankMax(type);
        IRankScript rankScript = Manager.rankListManager.getRankScript(type);
        List<RankPlayer> rankPlayerList = RankListManager.getRankPlayerMap().values().stream()
                .filter(rankPlayer -> !rankPlayer.getGuildFlag())
                .filter(rankScript::canRank)
                .sorted(rankScript::compareRankPlayer)
                .collect(Collectors.toList());

        rankMap.clear();
        for (int i = 0; i < rankPlayerList.size(); i++) {
            int rank = i + 1;
            rankMap.put(rank, rankPlayerList.get(i).getRoleId());
            if (rank >= rankMax) {
                break;
            }
        }
    }

    private void dealRankReward() {

        ConcurrentHashMap<Integer, Long> rankMap;

        //竞技场排行奖励
        if (Global.JJc_reward_time == TimeUtils.getDayOfHour(TimeUtils.Time())) {
            rankMap = RankListManager.getTempRankMap().get(RankType.ARENA_RANK);
            Manager.jjcManager.deal().sendArenaRankReward(rankMap);
        }

        //开服活动奖励结算
        if (Global.New_server_rewtime == TimeUtils.getDayOfHour(TimeUtils.Time())) {
            for (int type : RankListManager.getRankTypeList()) {
                rankMap = RankListManager.getTempRankMap().get(type);
                Manager.openServerAcManager.deal().onOperateBalance(type, rankMap);
            }
        }
        //每个一个小时广播排行第一的玩家
        for (int type : RankListManager.getRankTypeList()) {
            rankMap = RankListManager.getTempRankMap().get(type);
            Manager.openServerAcManager.deal().onNoticeFirstRank(type, rankMap);
        }
    }


    public void onReqGetAllRankListState(Player player){

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Rank)) {
            log.error("排行榜未激活");
            return;
        }

        ResGetAllRankListStateResult.Builder msg  = ResGetAllRankListStateResult.newBuilder();
        for (Cfg_Rank_base_Bean bean : CfgManager.getCfg_Rank_base_Container().getValuees()){
            if (bean.getIsShow() == 0){
                continue;
            }

            RankListState.Builder builder = RankListState.newBuilder();
            if (!RankListManager.getRankTypeList().contains(bean.getId())) {
                builder.setRankId(bean.getId());
                builder.setState(0);
                msg.addRankListStates(builder.build());
                continue;
            }
            IRankScript rank = Manager.rankListManager.getRankScript(bean.getId());
            if (rank == null) {
                log.error("rankKind错误！rankType:" + bean.getId());
                builder.setRankId(bean.getId());
                builder.setState(0);
                msg.addRankListStates(builder.build());
                continue;
            }
            List<RankInfo.Builder> rankList = rank.getRankInfo();
            if (rankList == null) {
                log.error("不该发生的错误！");
                builder.setRankId(bean.getId());
                builder.setState(0);
                msg.addRankListStates(builder.build());
                continue;
            }
            if (rankList.size()<=0){
                builder.setRankId(bean.getId());
                builder.setState(0);
                msg.addRankListStates(builder.build());
                continue;
            }
            builder.setRankId(bean.getId());
            builder.setState(1);
            msg.addRankListStates(builder.build());
        }
        MessageUtils.send_to_player(player, ResGetAllRankListStateResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 请求排行榜数据
     */
    @Override
    public void OnReqRankInfo(Player player, ReqRankInfo messInfo) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Rank)) {
            return;
        }
        if (!RankListManager.getRankTypeList().contains(messInfo.getRankKind())) {
            return;
        }
        Cfg_Rank_base_Bean bean = CfgManager.getCfg_Rank_base_Container().getValueByKey(messInfo.getRankKind());
        if (bean == null || bean.getIsShow() == 0) {
            return;
        }

        //今日剩余崇拜次数
        int todayRemainWorshipNum = Global.Rank_time - player.getWorshipRoleIdSet().size();
        int rankKind = messInfo.getRankKind();

        ResRankInfo.Builder resMsg = ResRankInfo.newBuilder();
        resMsg.setRankKind(rankKind);
        resMsg.setTodayRemainWorshipNum(todayRemainWorshipNum);
        //根据rankKind获取其排行榜信息数据
        IRankScript rank = Manager.rankListManager.getRankScript(rankKind);
        if (rank == null) {
            log.error("rankKind错误！rankType:" + rankKind);
            return;
        }
        List<RankInfo.Builder> rankList = rank.getRankInfo();
        if (rankList == null) {
            log.error("不该发生的错误！");
            return;
        }

        for (RankInfo.Builder rankInfo : rankList) {

            if (rankInfo.getRank() > bean.getRank_num()) {
                continue;
            }
            //公会排行
            if (ScriptEnum.GuildRankScript == rankKind) {
                if (rankInfo.getRoleId() == player.getGuildId()) {
                    rankInfo.setIsOnline(true);
                }
            }

            PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(rankInfo.getRoleId());
            if (pwi != null) {
                rankInfo.setViplevel(pwi.getPlayerVip());
            }
            rankInfo.setBeWorship(player.getWorshipRoleIdSet().contains(rankInfo.getRoleId()));
            resMsg.addRankInfoList(rankInfo);
        }
        MessageUtils.send_to_player(player, ResRankInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());

//        log.info("请求！rankType{} size={}" , rankKind, resMsg.getRankInfoListCount());

    }

    @Override
    public void OnReqRankPlayerImageInfo(Player player, ReqRankPlayerImageInfo messInfo) {
        long rankPlayerId = messInfo.getRankPlayerId();

        //可能是机器人的，在机器人库查找一下
        if (rankPlayerId < 10000000) {
            JJC jjc = new JJC();
            jjc.setRoleId(rankPlayerId);
            Robot robot = jjc.init();
            //角色ID错误
            if (robot == null) {
                return;
            }

            ResRankPlayerImageInfo.Builder resMsg = ResRankPlayerImageInfo.newBuilder();
            resMsg.setRankPlayerId(rankPlayerId);
            resMsg.setImageInfo(createImageInfo(robot));
            MessageUtils.send_to_player(player, ResRankPlayerImageInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
            return;
        }

        ResRankPlayerImageInfo.Builder resMsg = ResRankPlayerImageInfo.newBuilder();
        resMsg.setRankPlayerId(rankPlayerId);
        RankListMessage.RankPlayerImageInfo.Builder info;
        Player otherPlayer = Manager.playerManager.getPlayerCache(rankPlayerId);
        if (otherPlayer == null) {
            PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(rankPlayerId);
            if (pwi == null) {
                return;
            }
            info = createImageInfo(player, pwi);
        } else {
            info = createImageInfo(player, otherPlayer);
        }
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(rankPlayerId);
        if (rankPlayer != null) {
            info.setBeWorshipedNum(rankPlayer.getBeWorshipedNum());
        }
        resMsg.setImageInfo(info);
        MessageUtils.send_to_player(player, ResRankPlayerImageInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    private void addEuipInfo(Player player, RankListMessage.RankPlayerImageInfo.Builder info) {

        for (EquipPart equipPart : player.getEquipParts()) {
            if (equipPart.getEquip() == null) {
                continue;
            }
            EquipInfo.Builder equipBuilder = EquipInfo.newBuilder();
            equipBuilder.setLevel(equipPart.getLevel());
            equipBuilder.setEquipID(equipPart.getEquip().getItemModelId());
            equipBuilder.setJinlianlevel(equipPart.getGemInfo().getLevel());
            RankListMessage.EquipWash.Builder builder;
            List<RankListMessage.EquipWash> equipWashes = new ArrayList<>();
            builder = RankListMessage.EquipWash.newBuilder();
            for (EquipWash equipWash :  equipPart.getEquipWashs().values()){
                builder.setId(equipWash.getId());
                builder.setPer(equipWash.getPer());
                builder.setValue(equipWash.getValue());
                equipWashes.add(builder.build());
            }
            equipBuilder.addAllEquipWashList(equipWashes);
            equipBuilder.addAllGemIds(equipPart.getGemInfo().getGemIds());
            equipBuilder.addAllJadeIds(equipPart.getGemInfo().getJadeIds());
            equipBuilder.setSuitId( equipPart.getEquip().getSuitId());
            equipBuilder.setIsBind(equipPart.getEquip().isBind()?1:0);
            info.addEquipInfoList(equipBuilder.build());
        }
        RankListMessage.ImmortalEquipInfo.Builder immortalEquipInfo;
        HashMap<Integer, ImmortalEquipInfo.Builder> immortalEquipInfoMap = new HashMap<>();
        for (ImmortalEquipPart part : player.getImmortalEquipPartLisit().values()) {
            if (part.getEquip() == null) {
                continue;
            }
            int suitKey = 0;
            if (player.getCareer() == 0) {
                suitKey = (part.getEquip().getItemModelId() % 5000000 / 10000) + 1;
            } else {
                suitKey = (part.getEquip().getItemModelId() % 5000000 % 100000 / 10000) + 1;
            }
            if (!immortalEquipInfoMap.containsKey(suitKey)) {
                immortalEquipInfo = RankListMessage.ImmortalEquipInfo.newBuilder();
                immortalEquipInfo.setSuitKey(suitKey);
                immortalEquipInfoMap.put(suitKey, immortalEquipInfo);
            }
            immortalEquipInfo = immortalEquipInfoMap.get(suitKey);
            immortalEquipInfo.addImmortalEquipIds(part.getEquip().getItemModelId());
        }
        for (ImmortalEquipInfo.Builder builder : immortalEquipInfoMap.values()) {
            info.addImmortalEquipInfoList(builder.build());
        }
    }

    private RankListMessage.RankPlayerImageInfo.Builder createImageInfo(Player me, Player player) {
        RankListMessage.RankPlayerImageInfo.Builder info = RankListMessage.RankPlayerImageInfo.newBuilder();
        info.setRoleId(player.getId());
        info.setRoleName(player.getName());
        info.setCareer(player.getCareer());
        info.setLevel(player.getLevel());
        info.setBeWorshipedNum(0);
        info.setHorseModel(player.getHorse().getHorseModelId());
        info.setFaBaoModel(player.getStifleData().getNature().getCurrentModelId());
        info.setStateVip(player.getShiHaiData().getCfgId());
        info.setStifleFabaoId(player.getStifleData().getNature().getCurrentModelId());
        info.setFacade(MapUtils.getFacade(player));
        info.setBeWorship(me.getWorshipRoleIdSet().contains(player.getId()));
        info.setSoulId(player.getSoulArmor().getQualityLevel());
        info.setMental(player.getNewSkillData().getMentalType());
        info.setVipLvl(player.getVipLv());
        info.setGuildName(player.getGuildName() == null?"":player.getGuildName());
        Cfg_Pet_Bean pet_bean = CfgManager.getCfg_Pet_Container().getValueByKey(player.getActivePet().getFightPet());
        if (pet_bean != null) {
            info.setFightPetID(pet_bean.getModel());
        }
        addEuipInfo(player, info);
        return info;
    }

    private RankListMessage.RankPlayerImageInfo.Builder createImageInfo(Player me, PlayerWorldInfo pwi) {
        RankListMessage.RankPlayerImageInfo.Builder info = RankListMessage.RankPlayerImageInfo.newBuilder();
        info.setRoleId(pwi.getRoleid());
        info.setRoleName(pwi.getRolename());
        info.setCareer(pwi.getCareer());
        //player.getNewSkillData().getMentalType()
        info.setLevel(pwi.getLevel());
        info.setBeWorshipedNum(0);
        info.setHorseModel(pwi.getHorseId());
        info.setStateVip(pwi.getShiHaiLevel());
        info.setStateVip(pwi.getShiHaiLevel());
        info.setFacade(MapUtils.getFacade(pwi));
        info.setBeWorship(me.getWorshipRoleIdSet().contains(pwi.getRoleid()));
        Player otherPlayer = Manager.playerManager.getPlayer(pwi.getRoleid());
        if (otherPlayer != null) {
            info.setFaBaoModel(otherPlayer.getStifleData().getNature().getCurrentModelId());
            info.setSoulId(otherPlayer.getSoulArmor().getQualityLevel());
            Cfg_Pet_Bean pet_bean = CfgManager.getCfg_Pet_Container().getValueByKey(otherPlayer.getActivePet().getFightPet());
            if (pet_bean != null) {
                info.setFightPetID(pet_bean.getModel());
            }
            addEuipInfo(otherPlayer, info);
        }
        return info;
    }

    private RankListMessage.RankPlayerImageInfo.Builder createImageInfo(Robot robot) {
        RankListMessage.RankPlayerImageInfo.Builder info = RankListMessage.RankPlayerImageInfo.newBuilder();
        info.setRoleId(robot.getMakerId());
        info.setRoleName(robot.getName());
        info.setCareer(robot.getCareer());
        info.setLevel(robot.getLevel());
        info.setBeWorshipedNum(0);
        info.setStateVip(0);
        info.setHorseModel(robot.getMountId());
        info.setFacade(MapUtils.getFacade(robot.getWingId(), robot.getFashionBodyId(), robot.getFashionHalo(), robot.getFashionMatrix(), robot.getFashionWeaponId(), robot.getSpiritId(), robot.getSoulArmorId()));
        return info;
    }

    @Override
    public void OnReqWorship(Player player, ReqWorship messInfo) {

        long worshipPlayerId = messInfo.getWorshipPlayerId();
        //不能崇拜自己
        if (player.getId() == worshipPlayerId) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorshipNotMySelf);
            return;
        }
        //今日崇拜次数已用完！
        if (player.getWorshipRoleIdSet().size() >= Global.Rank_time) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorshipNumOver);
            return;
        }
        //不可重复崇拜同一个人！
        if (player.getWorshipRoleIdSet().contains(worshipPlayerId)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CannotWorshipSamePlayer);
            return;
        }
        RankPlayer worshipPlayer = RankListManager.getRankPlayerMap().get(worshipPlayerId);
        if (worshipPlayer == null) {
            return;
        }
        player.getWorshipRoleIdSet().add(worshipPlayerId);
        worshipPlayer.setBeWorshipedNum(worshipPlayer.getBeWorshipedNum() + 1);
        Manager.saveThreadManager.getOtherServerSave().deal(worshipPlayer, DbSqlName.RANKPLAYER_UPDATE, SaveServer.UPDATE);

        //返回崇拜结果
        ResWorship.Builder resMsg = ResWorship.newBuilder();
        resMsg.setWorshipResult(1);
        resMsg.setWorshipPlayerId(worshipPlayerId);
        resMsg.setTodayRemainWorshipNum(Global.Rank_time - player.getWorshipRoleIdSet().size());
        MessageUtils.send_to_player(player, ResWorship.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());

        //发送崇拜奖励
        List<Item> items = Item.createItems(Global.Rank_praise);
        long actionId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.WorshipRewardGet, actionId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                    , MessageString.System, MessageString.BAGISSPACETOMAIL
                    , MessageString.GetAwardNotEnoughSpaceContent, items, ItemChangeReason.WorshipRewardGet, actionId);
        }
        Manager.controlManager.operate(player, FunctionVariable.Rank_Thumbs_Up, 1);

        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.RANK, ItemChangeReason.WorshipRewardGet);
    }

    @Override
    public void online(Player player) {
        RankListMessage.ResRankRedPointTip.Builder resMsg = RankListMessage.ResRankRedPointTip.newBuilder();
        boolean flag = Global.Rank_time - player.getWorshipRoleIdSet().size() > 0;
        resMsg.setHasWorshipNum(flag);
        MessageUtils.send_to_player(player, RankListMessage.ResRankRedPointTip.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    @Override
    public void OnSyncRankPlayer(RankPlayer syncRankPlayer, int rankType) {
        ConcurrentHashMap<Long, RankPlayer> rankPlayerMap = RankListManager.getRankPlayerMap();

        boolean canEnter = false;
        //类型为0，表示检查所有排行榜
        if (rankType == 0) {
            for (int rankKind : RankListManager.getRankTypeList()) {
                if (canEnterRankList(rankPlayerMap, syncRankPlayer, rankKind)) {
                    canEnter = true;
                }
            }
        } else {
            canEnter = canEnterRankList(rankPlayerMap, syncRankPlayer, rankType);
        }

        if (!canEnter) {
            return;
        }
        //可以登上排行榜的处理
        if (rankPlayerMap.containsKey(syncRankPlayer.getRoleId())) {
            RankPlayer rankPlayer = rankPlayerMap.get(syncRankPlayer.getRoleId());
            Manager.saveThreadManager.getOtherServerSave().deal(rankPlayer, DbSqlName.RANKPLAYER_UPDATE, SaveServer.UPDATE);
        } else {
            rankPlayerMap.put(syncRankPlayer.getRoleId(), syncRankPlayer);
            Manager.saveThreadManager.getOtherServerSave().deal(syncRankPlayer, DbSqlName.RANKPLAYER_INSERT, SaveServer.MERGE);
        }
    }

    /**
     * 判断同步的玩家可否进去排行榜，能上榜则同步到排行榜
     */
    private boolean canEnterRankList(ConcurrentHashMap<Long, RankPlayer> rankPlayerMap, RankPlayer syncRankPlayer, int rankType) {
        if (!RankListManager.getRankTypeList().contains(rankType)) {
            return false;
        }
        boolean canEnter = false;
        try {

            //竞技场外的其他排行榜中要排除掉机器人(roleId <= 500)
            if (syncRankPlayer.getRoleId() <= 500) {
                return false;
            }

            //宗派重新排序
            if (rankType == RankType.GUILD_RANK) {
                IGuildRankScript guildRankScript = Manager.rankListManager.getGuildRankScript();
                guildRankScript.sortGuildRank();
                return RankListManager.getTempRankMap().get(rankType).contains(syncRankPlayer.getRoleId());
            }

            if (syncRankPlayer.getGuildFlag()) {
                return false;
            }

            IRankScript rankScript = Manager.rankListManager.getRankScript(rankType);
            int rankMax = Manager.rankListManager.getRankMax(rankType);
            ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(rankType);
            if (rankMap == null) {
                return false;
            }

            //不在本排行榜内，则同本排行榜上最后一名玩家进行比较
            if (!rankMap.containsValue(syncRankPlayer.getRoleId())) {
                if (!rankScript.canRank(syncRankPlayer)) {
                    return false;
                }

                if (rankMap.size() > 0) {
                    //最后一名玩家的名次等同于rankMap.size的，因为名次是从1开始的
                    int lastRank = rankMap.size();
                    RankPlayer lastRankPlayer = rankPlayerMap.get(rankMap.get(lastRank));
                    int compareResult = rankScript.compareRankPlayer(lastRankPlayer, syncRankPlayer);
                    if ((compareResult == -1 || compareResult == 0) && rankMap.size() >= rankMax) {
                        return false;
                    }
                }
            }

            canEnter = true;

            //将rankMap中的玩家取出放置List中进行重排序(Java List多字段排序)
            boolean added = false;
            List<RankPlayer> rankPlayerList = new ArrayList<>();
            for (int i = 1; i <= rankMax; i++) {
                if (i > rankMap.size()) {
                    break;
                }
                if (!rankMap.containsKey(i)) {
                    continue;
                }
                Long roleId = rankMap.get(i);
                if (roleId.equals(syncRankPlayer.getRoleId())) {
                    rankPlayerList.add(syncRankPlayer);
                    added = true;
                    continue;
                }
                RankPlayer rankPlayer = rankPlayerMap.get(roleId);
                if (rankPlayer != null) {
                    rankPlayerList.add(rankPlayer);
                }
            }
            if (!added) {
                rankPlayerList.add(syncRankPlayer);
            }
            rankPlayerList.sort(rankScript::compareRankPlayer);
            rankMap.clear();
            for (int i = 0; i < rankPlayerList.size(); i++) {
                int rank = i + 1;
                rankMap.put(rank, rankPlayerList.get(i).getRoleId());
                if (rank >= rankMax) {
                    break;
                }
            }
            rankPlayerList.clear();
            if (rankType == RankType.Intimacy_Rank){
                Manager.marriageManager.activity().intimacyRank();
            }
            //检测活动排行榜奖励红点
            Manager.activityRankManager.deal().checkAwardAvailable(syncRankPlayer.getRoleId(), rankType, rankScript.getCompareValue(syncRankPlayer));
        } catch (Exception e) {
            log.error(e, e);
        }

        return canEnter;
    }

    @Override
    public void onZeroClearRank() {

        //0点名人堂阶段检查
//        Manager.rankListManager.getTopHallRankScript().zeroCheckTopHallStage();

        //清理排行榜外的玩家
        clearOutRankPlayer();

        //记录排行榜日志
        zeroWriteRankLog();
    }

    private void clearOutRankPlayer() {
        log.info("凌晨0点开始清理被挤出排行榜外的玩家");
        ConcurrentHashMap<Long, RankPlayer> rankPlayerMap = RankListManager.getRankPlayerMap();
        List<Long> delRankPlayerList = new ArrayList<>();
        for (RankPlayer rankPlayer : rankPlayerMap.values()) {
            boolean canEnter = false;
            for (int rankType : RankListManager.getRankTypeList()) {
                if (canEnterRankList(rankPlayerMap, rankPlayer, rankType)) {
                    canEnter = true;
                }
            }
            if (!canEnter) {
                delRankPlayerList.add(rankPlayer.getRoleId());
                rankPlayer.setWhere(rankPlayer.getRoleId());
                Manager.saveThreadManager.getOtherServerSave().deal(rankPlayer, DbSqlName.RANKPLAYER_DELETEBYROLEID, SaveServer.DELETE);
                log.info("清理玩家roleId=" + rankPlayer.getRoleId() + ", roleName=" + rankPlayer.getRoleName());
            }
        }

        for (Long roleId : delRankPlayerList) {
            rankPlayerMap.remove(roleId);
        }
        log.info("清理排行榜完毕！");
    }

    private void zeroWriteRankLog() {
        log.info("凌晨0点开始记录排行榜日志");
        String curDate = TimeUtils.format2string(TimeUtils.Time(), "yyyy-MM-dd");
        try {
            for (int type : RankListManager.getRankTypeList()) {
                IRankScript script = Manager.rankListManager.getRankScript(type);
                if (script == null) {
                    log.error("IRankScript找不到排行榜脚本，类型：" + type);
                    continue;
                }
                for (RankListMessage.RankInfo.Builder rankInfo : script.getRankInfo()) {
                    RankListLog rankLog = new RankListLog();
                    rankLog.setDate(curDate);
                    rankLog.setRankKind(type);
                    rankLog.setRank(rankInfo.getRank());
                    rankLog.setRoleId(rankInfo.getRoleId());
                    rankLog.setRoleName(rankInfo.getRoleName());
                    rankLog.setRankData(rankInfo.getRankData());

                    PlayerWorldInfo p = Manager.playerManager.getPlayerWorldInfo(rankInfo.getRoleId());
                    if (p != null) {
                        rankLog.setPlatformName(p.getPlat());
                    }

                    LogService.getInstance().execute(rankLog);
                }
            }
        } catch (Exception exception) {
            log.error(exception);
        }
        log.info("排行榜记录完毕！");
    }

    @Override
    public void deleteRankRole(long deleteRoleId) {
        ConcurrentHashMap<Long, RankPlayer> rankPlayerMap = RankListManager.getRankPlayerMap();
        if (!rankPlayerMap.containsKey(deleteRoleId)) {
            return;
        }
        try {
            for (int type : RankListManager.getRankTypeList()) {
                if (type == RankType.GUILD_RANK) {
                    IGuildRankScript guildRank = Manager.rankListManager.getGuildRankScript();
                    guildRank.sortGuildRank();
                    continue;
                }

                IRankScript rankScript = Manager.rankListManager.getRankScript(type);
                ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(type);
                if (!rankMap.containsValue(deleteRoleId)) {
                    continue;
                }

                //从排行榜中移除并重新排序
                List<RankPlayer> rankPlayerList = new ArrayList<>();
                for (Long roleId : rankMap.values()) {
                    //移除删除角色
                    if (roleId == deleteRoleId) {
                        continue;
                    }
                    RankPlayer rankPlayer = rankPlayerMap.get(roleId);
                    rankPlayerList.add(rankPlayer);
                }

                //对rankPlayerList中的RankPlayer重新排序
                rankPlayerList.sort(rankScript::compareRankPlayer);

                //重新排好序后，重新写入rankMap中
                rankMap.clear();
                int rankMax = Manager.rankListManager.getRankMax(type);
                for (int i = 0; i < rankPlayerList.size(); i++) {
                    int rank = i + 1;
                    rankMap.put(rank, rankPlayerList.get(i).getRoleId());
                    if (rank >= rankMax) {
                        break;
                    }
                }
                rankPlayerList.clear();
            }
        } catch (Exception exception) {
            log.error(exception);
        }

        rankPlayerMap.remove(deleteRoleId);
        Manager.rankListManager.getDao().delete(DbSqlName.RANKPLAYER_DELETEBYROLEID.getName(), deleteRoleId);
    }

    @Override
    public void onReqCompareAttr(Player player, RankListMessage.ReqCompareAttr message) {
        if (player.getId() == message.getComparePlayerId()) {
            return;
        }

        RankPlayer self = RankListManager.getRankPlayerMap().get(player.getId());
        RankPlayer target = RankListManager.getRankPlayerMap().get(message.getComparePlayerId());
        if (self == null || target == null) {
            return;
        }

        Player targePlayer = PlayerManager.getInstance().getPlayer(message.getComparePlayerId());
//        int headId = 0;
//        int headFrameId = 0;
//        if (targePlayer !=null){
//            if (targePlayer.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_TYPE)) {
//                headId = targePlayer.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID();
//            }
//            if (targePlayer.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_FRAME_TYPE)) {
//                headFrameId = targePlayer.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID();
//            }
//        }
        RankListMessage.ResRankCompareData.Builder builder = RankListMessage.ResRankCompareData.newBuilder();
        builder.setName(target.getRoleName());
        builder.setPower((int) target.getFightPower());
        builder.setLevel(target.getLevel());
        builder.setCareer(target.getCareer());
//        builder.setHeadFrameId(headFrameId);
//        builder.setHeadId(headId);
        builder.setRoleID(target.getRoleId());


        builder.setHead(MapUtils.getHead(targePlayer));

        for (Cfg_Rank_compare_Bean bean : CfgManager.getCfg_Rank_compare_Container().getValuees()) {
            if (bean.getId() == RANK_COMPARE_OTHER) {
                continue;
            }
            addAttr(bean.getId(), builder, self, target);
        }
        addAttr(RANK_COMPARE_OTHER, builder, self, target);
        MessageUtils.send_to_player(player, RankListMessage.ResRankCompareData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }


    /*****************************private functions*****************************/
    private void addAttr(int type, RankListMessage.ResRankCompareData.Builder builder, RankPlayer self, RankPlayer target) {
        RankListMessage.AttrInfo.Builder attr = RankListMessage.AttrInfo.newBuilder();
        attr.setId(type);
        switch (type) {
            case RANK_COMPARE_HORSE:
                attr.setOwenValue(self.getHorseFightPoint());
                attr.setOtherValue(target.getHorseFightPoint());
                break;
            case RANK_COMPARE_WING:
                attr.setOwenValue(self.getWingFightPoint());
                attr.setOtherValue(target.getWingFightPoint());
                break;
            case RANK_COMPARE_EQUIP:
                attr.setOwenValue(self.getEquipFightPower());
                attr.setOtherValue(target.getEquipFightPower());
                break;
            case RANK_COMPARE_WEAPON:
                attr.setOwenValue(self.getWeaponFightPower());
                attr.setOtherValue(target.getWeaponFightPower());
                break;
            case RANK_COMPARE_GEM:
                attr.setOwenValue(self.getGemFightPower());
                attr.setOtherValue(target.getGemFightPower());
                break;
            case RANK_COMPARE_STRENGTHEN:
                attr.setOwenValue(self.getStrengthenFightPower());
                attr.setOtherValue(target.getStrengthenFightPower());
                break;
            case RANK_COMPARE_OTHER:
                long ownFightPower = 0;
                long otherFightPower = 0;
                for (RankListMessage.AttrInfo.Builder temp : builder.getAttrsBuilderList()) {
                    ownFightPower += temp.getOwenValue();
                    otherFightPower += temp.getOtherValue();
                }
                attr.setOwenValue(self.getFightPower() - ownFightPower);
                attr.setOtherValue(target.getFightPower() - otherFightPower);
                break;
            default:
                log.error("不存在的属性对比类型：" + type);
        }
        builder.addAttrs(attr);
    }

    private void synRankBI() {
        for (int type : RankListManager.getRankTypeList()) {
            synRankBI(type);
        }
    }

    private void synRankBI(int type) {
        ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(type);
        if (rankMap == null) {
            return;
        }
        String roleName = "";
        String rankValue = "";
        for (Map.Entry<Integer, Long> entry : rankMap.entrySet()) {
            int rank = entry.getKey();
            long roleId = entry.getValue();
            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(roleId);
            if (rankPlayer == null) {
                continue;
            }
            if (type == RankType.GUILD_RANK) {
                Guild guild = Manager.guildsManager.getGuildById(roleId);
                if (guild != null) {
                    roleName = guild.getName();
                }
            } else {
                PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(roleId);
                if (pwi != null) {
                    roleName = pwi.getRolename();
                }
            }
            rankValue = String.valueOf(getRankValue(type, rankPlayer));

            int targetType = 0;
            if (type == RankType.GUILD_RANK) {
                targetType = 1;
            }
            Manager.biManager.getScript().biRank(targetType, roleId, roleName, type, rank, rankValue);
        }
    }

    private Object getRankValue(int type, RankPlayer rankPlayer) {
        switch (type) {
            case RankType.LEVEL_RANK:
                return rankPlayer.getLevel();
            case RankType.FIGHT_POWER_RANK:
                return rankPlayer.getFightPower();
            case RankType.HORSE_RANK:
                return rankPlayer.getHorseFightPoint();
            case RankType.WING_RANK:
                return rankPlayer.getWingFightPoint();
            case RankType.EQUIP_RANK:
                return rankPlayer.getEquipFightPower();
            case RankType.MAGIC_WEAPON_RANK:
                return rankPlayer.getMagicWeaponDamage();
            case RankType.TALISMAN_RANK:
                return rankPlayer.getTalismanFightPower();
            case RankType.MAGIC_RANK:
                return rankPlayer.getMagicFightPower();
            case RankType.WEAPON_RANK:
                return rankPlayer.getWeaponFightPower();
            case RankType.GEM_RANK:
                return rankPlayer.getGemFightPower();
            case RankType.OFFLINE_EFFICIENCY_RANK:
                return rankPlayer.getOfflineEfficiency();
            case RankType.EQUIPWASH_RANK:
                return rankPlayer.getEquipWashPer();
            case RankType.EQUIPSTRENGTHEN_RANK:
                return rankPlayer.getEquipStrengthenLv();
            case RankType.GEMLEVEL_RANK:
                return rankPlayer.getGemLv();
            case RankType.EQUIPSTAR_RANK:
                return rankPlayer.getEquipStar();
            case RankType.ARENA_RANK:
                return rankPlayer.getArenaRank();
            case RankType.SHIHAI_RANK:
                return rankPlayer.getShihai();
            case RankType.GUILD_RANK:
                return 0;
            case RankType.CHARM_RANK:
                return rankPlayer.getCharm();
            case RankType.SEND_FLOWER_RANK:
                return rankPlayer.getSendFlower();
            case RankType.TOPHALL_RANK:
                return rankPlayer.getTopHallFightPower();
            case RankType.UNIVERSE_RANK:
                return rankPlayer.getUniverseFightPower();
            case RankType.MONSTOR_RANK:
                return rankPlayer.getMonsterFightPower();
            case RankType.HOLYEQUIP_RANK:
                return rankPlayer.getHolyEquipFightPower();
            case RankType.HORSE_LV_RANK:
                return rankPlayer.getHorseLv();
            case RankType.HORSE_SOUL_LV_RANK:
                return rankPlayer.getHorseSoulLv();
            case RankType.PET_LV_RANK:
                return rankPlayer.getPetLv();
            case RankType.PET_SOUL_LV_RANK:
                return rankPlayer.getPetSoulLv();
            case RankType.COMSUME_GOLD_RANK:
                return rankPlayer.getConsumeGold();
            case RankType.Intimacy_Rank:
                return rankPlayer.getIntimacy();
            default:

        }
        return 0;
    }

    /**
     * 设置装备洗练等级
     */
    public void changeEquipWashData(Player player) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            int totalPer = 0;
            for (EquipPart part : player.getEquipParts()) {
                if (part.getEquip() == null) {
                    continue;
                }
                for (EquipWash wash : part.getEquipWashs().values()) {
                    totalPer += wash.getPer();
                }
            }
            rankPlayer.setEquipWashPer(totalPer);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.EQUIPWASH_RANK));
        }
    }

    /**
     * 排行榜玩家同步
     */
    public void syncRankPlayer(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Rank)) {
            return;
        }

        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, 0));
        }
    }

    /**
     * 设置名字
     */
    public void setName(long id, String name) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(id);
        if (null != rankPlayer) {
            rankPlayer.setName(name);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, 0));
        }
    }

    /**
     * 设置玩家等级
     */
    public void setLevel(Player player, int level) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setLevel(level);
            rankPlayer.setExp(Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.EXP));
            rankPlayer.setLevelUpTime((int) (TimeUtils.Time() / 1000));
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.LEVEL_RANK));
        }
    }

    /**
     * 设置玩家战力
     */
    public void setFightPower(Player player, long fightPower) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setFightPower(fightPower);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.FIGHT_POWER_RANK));
        }
    }

    /**
     * 设置强化战力
     */
    public void setStrengthrenPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setStrengthenFightPower(power);
        }
    }

    /**
     * 设置坐骑id
     */
    public void setHorseId(Player player, int horseId) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setHorseId(horseId);
        }
    }

    /**
     * 设置坐骑战力、坐骑御魂等级、坐骑等级
     */
    public void setHorseRankData(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setHorseFightPoint(power);
            rankPlayer.setHorseLv(player.getHorse().getHorseSteps() * 100 + player.getHorse().getHorseStar());
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.HORSE_LV_RANK));
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.HORSE_RANK));
        }
    }

    /**
     * 设置宠物战力排行、宠物御魂等级排行、宠物等级排行
     */
    public void setPetRankData(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setPetFightPower(power);
            rankPlayer.setPetLv(player.getActivePet().getLevel());
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.PET_LV_RANK));
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.PETASTAR_RANK));
        }
    }

    /**
     * 、宠物御魂战力、
     */
    public void setPetSoulPower(Player player, int power){
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            rankPlayer.setPetSoulLv(power);
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setPetSoulLv(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.PET_SOUL_LV_RANK));
        }
    }
    /**
     * 、坐骑御魂战力、
     */
    public void setHorseSoulPower(Player player,int power){
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            rankPlayer.setHorseSoulLv(power);
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setHorseSoulLv(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.HORSE_SOUL_LV_RANK));
        }
    }


    /**
     * 灵体战力排行
     */
    public void setSpiritPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setSpiritFightPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.SPIRITSTAR_RANK));
        }
    }
    /**
     * 仙甲战力排行
     */
    public void setImmEuiqpPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setImmEquipFightPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.IMEQUIPSTAR_RANK));
        }
    }

    /**
     * 圣装战力排行
     */
    public void setHolyEuiqpPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setHolyEquipFightPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.HOLYEQUIP_RANK));
        }
    }

    /**
     * 神兽战力排行
     */
    public void setMonstorPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setMonsterFightPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.MONSTOR_RANK));
        }
    }

    /**
     * 设置翅膀id
     */
    public void setWingId(Player player, int wingId) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setWingId(wingId);
        }
    }

    /**
     * 设置翅膀战力
     */
    public void setWingPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setWingFightPoint(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.WING_RANK));
        }
    }

    /**
     * 设置装备战力
     */
    public void setEquipPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setEquipFightPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.EQUIP_RANK));
        }
    }

    /**
     * 设置装备总星级
     */
    public void updateEquipStarNum(Player player) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            int totalStar = 0;
            int totalStarGradeNum = 0;
            for (EquipPart part : player.getEquipParts()) {
                if (part.getEquip() == null) {
                    continue;
                }
                Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getEquip().getItemModelId());
                if (bean == null || bean.getGrade() < Global.Sever_Crazy_Limit_Equp) {
                    continue;
                }
                totalStar += bean.getDiamond_Number();
                totalStarGradeNum += bean.getGrade();
            }
            rankPlayer.setEquipStar(totalStar);
            rankPlayer.setEquipStarGradeNum(totalStarGradeNum);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.EQUIPSTAR_RANK));
        }
    }

    /**
     * 设置装备总星级(包含灵体)
     */
    public void updateEquipAllStar(Player player) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            int totalStar = 0;
            for (EquipPart part : player.getEquipParts()) {
                if (part.getEquip() == null) continue;
                Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getEquip().getItemModelId());
                if (bean == null) continue;
                totalStar += bean.getDiamond_Number();
            }
            for (SpiritInfo info : player.getSpiritData().getSpiritInfoMap().values()) {
                for (Integer equipId : info.getEquipList()) {
                    if (equipId == 0) continue;
                    Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equipId);
                    if (bean == null) continue;
                    totalStar += bean.getDiamond_Number();
                }
            }
            rankPlayer.setEquipAllStar(totalStar);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.EQUIPALLSTAR_RANK));
        }
    }


    /**
     * 设置装备强化等级
     */
    public void changeEquipStrengthen(Player player) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            int totalStrengthenLv = 0;
            for (EquipPart part : player.getEquipParts()) {
                if (part.getEquip() == null) {
                    continue;
                }
                totalStrengthenLv += part.getLevel();
            }
            rankPlayer.setEquipStrengthenLv(totalStrengthenLv);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.EQUIPSTRENGTHEN_RANK));
        }
    }

    /**
     * 设置法器战力
     */
    public void setTalismanPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setTalismanFightPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.TALISMAN_RANK));
        }
    }

    /**
     * 设置阵法战力
     */
    public void setMagicPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setMagicFightPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.MAGIC_RANK));
        }
    }

    /**
     * 设置灵压法宝等级
     */
    public void setMagicWeaponDamage(Player player, int damage) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setMagicWeaponDamage(damage);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.MAGIC_WEAPON_RANK));
        }
    }

    /**
     * 设置神兵战力
     */
    public void setWeaponPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setWeaponFightPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.WEAPON_RANK));
        }
    }

    /**
     * 设置宝石总等级
     */
    public void changeGemLv(Player player) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            int totleLv = player.getEquipParts().stream()
                    .map(n -> n.getGemInfo().getGemIds().stream()
                            .filter(m -> m > 0).map(m -> m % 100).reduce(0, Integer::sum))
                    .reduce(0, Integer::sum);
            rankPlayer.setGemLv(totleLv);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.GEMLEVEL_RANK));
        }
    }

    /**
     * 设置宝石战力
     */
    public void setGemPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setGemFightPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.GEM_RANK));
        }
    }

    /**
     * 设置石海层数
     */
    public void setShihaiLayer(Player player, int layer) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setShihai(layer);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.SHIHAI_RANK));
        }
    }

    /**
     * 设置竞技场排名
     */
    public void setArenaRank(Player player, int rank) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setArenaRank(rank);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.ARENA_RANK));
        }
    }

    /**
     * 增加金元宝消耗
     */
    public void addConsumeGoldRank(Player player, int consumeAdd) {
        //如果金元宝消耗排行榜还没开启，则不上榜
        Cfg_RankAwardType_Bean cfg = CfgManager.getCfg_RankAwardType_Container().getValueByKey(11);
        if (cfg.getLink_rank_id() != RankType.COMSUME_GOLD_RANK) {
            log.error("RankAwardType表id为8的排行榜不是金元宝排行");
            return;
        }
        int openDay = TimeUtils.getOpenServerDay();
        if (openDay < cfg.getStart_day() || openDay > cfg.getEnd_day()) {
            return;
        }
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.addConsumeGold(consumeAdd);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.COMSUME_GOLD_RANK));
        }
    }

    /**
     * 魂甲战力排行
     * @param player
     * @param soulFight
     */
    public void setSoulFightRank(Player player,int soulFight){

        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setSoulFight(soulFight);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.SOUL_FIGHT_RANK));
        }


    }

    /**
     * 八卦战力排行
     */
    public void setBaguaPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setBaguaPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.BAGUA_RANK));
        }
    }

    /**
     * 灵魂战力排行
     */
    public void setImmortalsoulPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setImmortalsoulPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.Immortalsoul_RANK));
        }
    }

    /**
     * 魔魂战力排行
     */
    public void setDevilSoulPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setDevilSoulPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.DEVIL_SOUL_RANK));
        }
    }

    /**
     * 设置亲密度
     */
    public void setIntimacy(Player player, int intimacy) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setIntimacy(intimacy);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.Intimacy_Rank));
        }
    }

    /**
     * 坐骑脉轮战力排行
     */
    public void setHorseEquipPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setHorseEquipPower(power);
            Manager.rankListManager.addCommand(new SyncRankPlayerHandler(rankPlayer, RankType.HORSE_EQUIP_RANK));
        }
    }

    /**
     * 剑灵战力
     */
    public void setFlySwordPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setFlySwordPower(power);
            //暂时不计算排行
        }
    }

    /**
     * 仙娃战力
     */
    public void setMarryChildPower(Player player, int power) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (null == rankPlayer) {
            rankPlayer = new RankPlayer();
            initRankPlayer(player,rankPlayer);
        } else {
            rankPlayer.setMarryChildPower(power);
            //暂时不计算排行
        }
    }




    /**********************************private functions***********************************/
    private void initRankPlayer(Player player,RankPlayer syncRankPlayer) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Rank)) {
            return ;
        }

        long syncRoleId = player.getId();
        syncRankPlayer.setRoleId(syncRoleId);
        syncRankPlayer.setName(player.getName());
        syncRankPlayer.setGuildFlag(false);
        syncRankPlayer.setCareer(player.getCareer());
        syncRankPlayer.setCreateTime(player.getCreateTime() * 1000L);
        syncRankPlayer.setCreateSid(player.getCreateServerId());
        syncRankPlayer.setLevel(player.getLevel());
        syncRankPlayer.setFightPower(player.getFightPoint());
        syncRankPlayer.setHorseId(player.getHorse().getHorseModelId());
        syncRankPlayer.setWingId(player.getWing().getCurrentModelId());

//        EquipPart weaponPart = player.getEquipParts().get(EquipDefine.EquipPart_Weapon);
//        Equip weapon = weaponPart.getEquip();
//
//        EquipPart clothesPart = player.getEquipParts().get(EquipDefine.EquipPart_Breastplate);
//        Equip clothes = clothesPart.getEquip();

        syncRankPlayer.setBeWorshipedNum(0);
        syncRankPlayer.setExp(Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.EXP));

        int damage = Manager.stateStifleManager.deal().getAttValue(player);
        syncRankPlayer.setMagicWeaponDamage(damage);

        int totalPer = 0;
        int totalStrengthenLv = 0;
        int totalStar = 0;
        int totalStarGradeNum = 0;
        int totalAllStar = 0;
        for (EquipPart part : player.getEquipParts()) {
            for (EquipWash wash : part.getEquipWashs().values()) {
                totalPer += wash.getPer();
            }
            totalStrengthenLv += part.getLevel();
            if (part.getEquip() == null) continue;
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getEquip().getItemModelId());
            if (bean == null) continue;
            totalAllStar += bean.getDiamond_Number();
            if (bean.getGrade() >= Global.Sever_Crazy_Limit_Equp) {
                totalStar += bean.getDiamond_Number();
                totalStarGradeNum += bean.getGrade();
            }
        }
        for (SpiritInfo info : player.getSpiritData().getSpiritInfoMap().values()) {
            for (Integer equipId : info.getEquipList()) {
                if (equipId == 0) continue;
                Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equipId);
                if (bean == null) continue;
                totalAllStar += bean.getDiamond_Number();
            }
        }
        syncRankPlayer.setEquipWashPer(totalPer);
        syncRankPlayer.setEquipStrengthenLv(totalStrengthenLv);
        syncRankPlayer.setEquipStar(totalStar);
        syncRankPlayer.setEquipStarGradeNum(totalStarGradeNum);
        syncRankPlayer.setEquipAllStar(totalAllStar);

        //宝石总等级
        int totleLv = player.getEquipParts().stream()
                .map(n -> n.getGemInfo().getGemIds().stream()
                        .filter(m -> m > 0).map(m -> m % 100).reduce(0, Integer::sum))
                .reduce(0, Integer::sum);
        syncRankPlayer.setGemLv(totleLv);

        int strengthenPower = Manager.equipManager.deal().calculateStrengthenPower(player);
        syncRankPlayer.setStrengthenFightPower(strengthenPower);

        setFightPower(syncRankPlayer, player, PlayerAttributeType.HORSE);
        setFightPower(syncRankPlayer, player, PlayerAttributeType.WING);
        setFightPower(syncRankPlayer, player, PlayerAttributeType.EQUIP);
        setFightPower(syncRankPlayer, player, PlayerAttributeType.StifleFabao);
        setFightPower(syncRankPlayer, player, PlayerAttributeType.GEM);
        setFightPower(syncRankPlayer, player, PlayerAttributeType.PET);
        setFightPower(syncRankPlayer, player, PlayerAttributeType.Spirit);
        setFightPower(syncRankPlayer, player, PlayerAttributeType.ImmortalEquip);
        setFightPower(syncRankPlayer, player, PlayerAttributeType.HolyEquip);
        setFightPower(syncRankPlayer, player, PlayerAttributeType.SOUL_BEAST);
        setFightPower(syncRankPlayer, player, PlayerAttributeType.SoulEquip);
        setFightPower(syncRankPlayer, player, PlayerAttributeType.Weapon);

        long hookRate = Manager.playerHookManager.deal().getHookRate(player.getId());
        syncRankPlayer.setOfflineEfficiency(hookRate);

        int shihaiLayer = player.getShiHaiData().getCfgId();
        syncRankPlayer.setShihai(shihaiLayer);

        syncRankPlayer.setArenaRank(player.getJjcHistoryMaxRank());

        //初始化宠物等级
        syncRankPlayer.setPetLv(player.getActivePet().getLevel());

        //初始化坐骑等级
        syncRankPlayer.setHorseLv(player.getHorse().getHorseSteps() * 100 + player.getHorse().getHorseStar());
        Manager.rankListManager.addCommand(new SyncRankPlayerHandler(syncRankPlayer, 0));
    }

    private void setFightPower(RankPlayer rankPlayer, Player player, PlayerAttributeType type) {
        IPlayerAttribute script = Manager.playerAttAttributeManager.deal();

        BaseIntAttribute attribute = script.getAttribute(player, type);
        int fightPoint = script.calcFightPower(attribute);
        if (type == PlayerAttributeType.WING) {
            rankPlayer.setWingFightPoint(fightPoint);
        }
        if (type == PlayerAttributeType.GEM) {
            rankPlayer.setGemFightPower(fightPoint);
        }
        if (type == PlayerAttributeType.EQUIP) {
            rankPlayer.setEquipFightPower(fightPoint);
        }
        if (type == PlayerAttributeType.HORSE) {
            rankPlayer.setHorseFightPoint(fightPoint);
        }
        if (type == PlayerAttributeType.PET) {
            rankPlayer.setPetFightPower(fightPoint);
        }
        if (type == PlayerAttributeType.Spirit) {
            rankPlayer.setSpiritFightPower(fightPoint);
        }
        if (type == PlayerAttributeType.ImmortalEquip) {
            rankPlayer.setImmEquipFightPower(fightPoint);
        }
        if (type == PlayerAttributeType.HolyEquip) {
            rankPlayer.setHolyEquipFightPower(fightPoint);
        }
        if (type == PlayerAttributeType.SOUL_BEAST) {
            rankPlayer.setMonsterFightPower(fightPoint);
        }
        if (type == PlayerAttributeType.SoulEquip) {
            rankPlayer.setSoulFight(fightPoint);
        }
        if (type == PlayerAttributeType.Weapon){
            rankPlayer.setWeaponFightPower(fightPoint);
        }
    }
    public void initGuild(long guildId, PlayerWorldInfo pwi, String name) {
        RankPlayer syncRankPlayer = new RankPlayer();
        syncRankPlayer.setRoleId(guildId);
        syncRankPlayer.setName(name);
        syncRankPlayer.setCareer(pwi.getCareer());
        syncRankPlayer.setGuildFlag(true);
        syncRankPlayer.setCreateSid(pwi.getCsid());
        syncRankPlayer.setLevel(pwi.getLevel());
        syncRankPlayer.setFightPower(pwi.getFightPower());
        syncRankPlayer.setBeWorshipedNum(0);

        Manager.rankListManager.addCommand(new SyncRankPlayerHandler(syncRankPlayer, RankType.GUILD_RANK));
    }

    public void buildRankInfo(int index, RankPlayer rankPlayer, boolean isOnline, String data, List<RankListMessage.RankInfo.Builder> rankInfoList,int needShowFightPower) {

//        int fashionHeadId = 0;
//        int fashionHeadFrameId = 0;
        RankListMessage.RankInfo.Builder rankInfo = RankListMessage.RankInfo.newBuilder();
        rankInfo.setRank(index);

        Player otherPlayer = Manager.playerManager.getPlayerCache(rankPlayer.getRoleId());
        if (otherPlayer == null) {
            PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(rankPlayer.getRoleId());
            if (pwi != null){
//                fashionHeadId  = pwi.getFashionHeadId();
//                 fashionHeadFrameId = pwi.getFashionHeadFrameId();
                rankInfo.setHead(MapUtils.getHead(pwi));
            }
        }else {

            rankInfo.setHead(MapUtils.getHead(otherPlayer));
//            if (otherPlayer.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_TYPE)){
//                fashionHeadId =  otherPlayer.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID();
//            }
//            if (otherPlayer.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_FRAME_TYPE)){
//                fashionHeadFrameId = otherPlayer.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID();
//            }
        }

        if (null == rankPlayer.getRoleName()) {
            rankInfo.setRoleName("");
            log.error("rank has no name, info: " + rankInfo);
        } else {
            rankInfo.setRoleName(rankPlayer.getRoleName());
        }
        rankInfo.setRoleId(rankPlayer.getRoleId());
        rankInfo.setCareer(rankPlayer.getCareer());
        rankInfo.setIsOnline(isOnline);
        rankInfo.setLevel(rankPlayer.getLevel());
        rankInfo.setRankData(data);
        rankInfo.setFightPower(needShowFightPower > -1 ? needShowFightPower: rankPlayer.getFightPower());
        rankInfo.setBeWorshipedNum(rankPlayer.getBeWorshipedNum());
//        rankInfo.setHeadFashion(fashionHeadId);
//        rankInfo.setHeadBoxFashion(fashionHeadFrameId);


        rankInfoList.add(rankInfo);
    }
}
