package common.activityRank;

import com.data.*;
import com.data.bean.Cfg_RankAwardItem_Bean;
import com.data.bean.Cfg_RankAwardType_Bean;
import com.data.bean.Cfg_Rank_base_Bean;
import com.data.container.Cfg_RankAwardItem_Container;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.mail.manager.MailManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.ranklist.manager.RankListManager;
import com.game.activityRanklist.script.IActivityRankScript;
import com.game.ranklist.script.IRankScript;
import com.game.script.structs.ScriptEnum;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.ActivityRankListMessage;
import game.message.RankListMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityRankScript implements IActivityRankScript {

    private static final Logger log = LogManager.getLogger(ActivityRankScript.class);

    @Override
    public void getActivityRankInfo(Player player, int id) {
        Cfg_RankAwardType_Bean typeBean = CfgManager.getCfg_RankAwardType_Container().getValueByKey(id);
        if (typeBean == null) {
            log.error("排行榜id不存在" + id);
            return;
        }
        int openDay = TimeUtils.getOpenServerDay();
        if (openDay < typeBean.getStart_day() || openDay > typeBean.getEnd_day()) {
            log.error("排行榜今天不开启");
            return;
        }
        int type = typeBean.getLink_rank_id();
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Rank)) {
            log.error("排行榜还未开启");
            return;
        }
        if (!RankListManager.getRankTypeList().contains(type)) {
            log.error("排行榜类型不存在" + type);
            return;
        }
        Cfg_Rank_base_Bean bean = CfgManager.getCfg_Rank_base_Container().getValueByKey(type);
        if (bean == null) {
            return;
        }
        IRankScript rank = Manager.rankListManager.getRankScript(type);
        if (rank == null) {
            log.error("rankKind错误！rankType:" + type);
            return;
        }
        ActivityRankListMessage.ResActivityRankInfo.Builder res = ActivityRankListMessage.ResActivityRankInfo.newBuilder();
        res.setRankKind(id);
        ActivityRankListMessage.ActivityRankInfo.Builder info = ActivityRankListMessage.ActivityRankInfo.newBuilder();
        info.setRank(0);
        info.setRankData(0);

        //这个排行榜中所有的奖励档次
        ArrayList<Cfg_RankAwardItem_Bean> list = new ArrayList<>();
        for (Cfg_RankAwardItem_Bean b : CfgManager.getCfg_RankAwardItem_Container().getValuees()) {
            if (b.getOwner_id() == id) {
                list.add(b);
            }
        }

        //自主领取奖励的排行榜，这里的排名和系统排行榜的排名一致
        if (list.get(0).getAward_type() == 1) {
            for (RankListMessage.RankInfo.Builder rankInfo : rank.getRankInfo()) {
                if (rankInfo.getRoleId() == player.getId()) {
                    info.setRank(rankInfo.getRank());
                    info.setRankData(Long.parseLong(rankInfo.getRankData()));
                    break;
                }
            }
        }
        //活动结束后统一发放奖励的排行榜，新增了一个条件，排名可能要后退。
        //比如我在系统排行榜里面排名第一，但这个排行榜要求第一名需达到1W战力。如果我不到1W战力，那我的排名会变成第2，第一名是空！！！！！！！！
        else {
            //排行榜从前到后的所有玩家
            ArrayList<RankPlayerData> allPlayers = new ArrayList<>();
            rank.getRankInfo().forEach(rankInfo -> allPlayers.add(new RankPlayerData(rankInfo.getRank(), Integer.parseInt(rankInfo.getRankData()), rankInfo.getRoleId(), rankInfo.getCareer())));

            //排名第一的玩家是否都没上榜。如果第一名都没上榜，那么后面所有的玩家都不会上榜
            boolean firstPlayerNotInRank = true;
            //从前到后遍历每一个玩家
            HashMap<Integer,Integer> topMaps = new HashMap<>();
            int rankIndex = 0;
            for (int i = 0; i < allPlayers.size(); i ++) {
                if (allPlayers.get(i).playerId == player.getId()) {
                    info.setRankData(allPlayers.get(i).data);
                }
                boolean find = false;
                //遍历每一个区间要求
                rankIndex++;
                for (Cfg_RankAwardItem_Bean itemBean : list) {
                    //每个玩家只会找到一次合适的区间
                    if (find) {
                        break;
                    }
                    //如果这个区间的要求，玩家还没达到,那么继续看下一个区间
                    if (itemBean.getNeed_value() > allPlayers.get(i).data) {
                        continue;
                    }
                    if(itemBean.getAward_type() == 1){
                        continue;
                    }
                    if (allPlayers.get(i).playerId == player.getId()) {
                        firstPlayerNotInRank = false;
                    }
                    //玩家达到的了这个区间的要求，就把这个玩家的排名设为这个区间的最高排名，然后后面玩家的排名都要往后挪1
                    if (!topMaps.containsKey(itemBean.getNeed_value())){
                        topMaps.put(itemBean.getNeed_value(),itemBean.getTop_rank());
                        allPlayers.get(i).rank = rankIndex > itemBean.getTop_rank() ? rankIndex: itemBean.getTop_rank();
                    }else {
                       int curRank =  topMaps.get(itemBean.getNeed_value()) +1;
                        topMaps.put(itemBean.getNeed_value(),curRank);
                        allPlayers.get(i).rank = curRank;
                    }
                    for (int j = i + 1; j < allPlayers.size(); j ++) {
                        allPlayers.get(j).rank = allPlayers.get(j - 1).rank + 1;
                    }
                    find = true;
                }
            }
            if (!firstPlayerNotInRank) {
                for (RankPlayerData rad : allPlayers) {
                    if (rad.playerId == player.getId()) {
                        info.setRank(rad.rank);
                        info.setRankData(rad.data);
                        break;
                    }
                }
            }
        }
        setAwardInfo(info, player, type);
        res.setRankInfo(info.build());
        MessageUtils.send_to_player(player, ActivityRankListMessage.ResActivityRankInfo.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    @Override
    public void getActivityAward(Player player, int rankType, int awardId) {
        Cfg_RankAwardItem_Bean awardItemBean = CfgManager.getCfg_RankAwardItem_Container().getValueByKey(awardId);
        if (awardItemBean == null) {
            log.error("奖励id不存在");
            return;
        }
        if (awardItemBean.getAward_type() == 0) {
            log.error("该奖励是活动结束之后统一发放的，不是手动领取的");
            return;
        }
        if (player.getActivityRankAwardGet().contains(awardId)) {
            log.error("已经领取过");
            return;
        }
        Cfg_RankAwardType_Bean typeBean = CfgManager.getCfg_RankAwardType_Container().getValueByKey(rankType);
        if (typeBean == null) {
            log.error("排行榜id不存在" + rankType);
            return;
        }
        int openDay = TimeUtils.getOpenServerDay();
        if (openDay < typeBean.getStart_day() || openDay > typeBean.getEnd_day()) {
            log.error("排行榜今天不开启");
            return;
        }
        int type = typeBean.getLink_rank_id();
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Rank)) {
            log.error("排行榜还未开启");
            return;
        }
        if (!RankListManager.getRankTypeList().contains(type)) {
            log.error("排行榜类型不存在" + type);
            return;
        }
        Cfg_Rank_base_Bean bean = CfgManager.getCfg_Rank_base_Container().getValueByKey(type);
        if ( bean == null ) {
            log.error("排行榜不存在或者已关闭" + type);
            return;
        }
        IRankScript rank = Manager.rankListManager.getRankScript(type);
        if (rank == null) {
            log.error("rankKind错误！rankType:" + type);
            return;
        }
        Integer serverTimes = ServerParamUtil.activityRankAwardGetTimes.getOrDefault(awardItemBean.getId(), 0);
        if (awardItemBean.getMax_get_count() != 0 && serverTimes >= awardItemBean.getMax_get_count()) {
            log.error("领取人数达到上限" + type);
            return;
        }
        for (RankListMessage.RankInfo.Builder rankInfo : rank.getRankInfo()) {
            if (rankInfo.getRoleId() == player.getId()) {
                if (Long.parseLong(rankInfo.getRankData()) >= awardItemBean.getNeed_value()) {
                    //达到条件了，领奖
                    getAward(player, awardItemBean, rankType);
                    //BI
                    //计算档位
                    int index = getIndex(awardItemBean);
                    Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.ActivityRank, typeBean.getId(), typeBean.getName()
                            , ItemChangeReason.ActivityRankGet, index, "第" + index + "档", 0, 0);
                }
                else {
                    log.error("当前最低要求" + rankInfo.getRankData() + "与奖励规定的最低要求不一致");
                }
                return;
            }
        }
        log.error("还未上榜,不能领");
    }

    /**
     * 计算档位
     * @param awardItemBean
     * @return
     */
    private int getIndex(Cfg_RankAwardItem_Bean awardItemBean) {
        int type = awardItemBean.getOwner_id();
        int index = 0;
        for(Cfg_RankAwardItem_Bean bean : Cfg_RankAwardItem_Container.GetInstance().getValuees()){
            if(type == bean.getOwner_id()){
                index++;
                if(awardItemBean.getId() == bean.getId()){
                    break;
                }
            }
        }
        return index;
    }

    @Override
    public void tick(long nowDay, long lastCheckDay) {
        if (nowDay == lastCheckDay) {
            return;
        }
        for (Map.Entry<Integer, Integer> entry : Manager.activityRankManager.endDay.entrySet()) {
            //这个排行榜今天不发奖
            if (nowDay - 1 != entry.getValue()) {
                continue;
            }
            //开始算排名

            //这个是该排行榜的奖励
            ArrayList<Cfg_RankAwardItem_Bean> list = Manager.activityRankManager.timeEndRankAward.get(entry.getKey());
            if (list == null) {
                log.error("排行榜奖励配置找不到!rankType:" + entry.getKey());
                continue;
            }
            //这个是该排行榜对应的脚本
            IRankScript rank = Manager.rankListManager.getRankScript(entry.getKey());
            if (rank == null) {
                log.error("rankKind错误！rankType:" + entry.getKey());
                continue;
            }
            log.info("开始发放活动排行榜奖励。rankType:" + entry.getKey());

            //排行榜从前到后的所有玩家
            ArrayList<RankPlayerData> allPlayers = new ArrayList<>();
            ArrayList<RankPlayerData> needRewardPlayers = new ArrayList<>();
            rank.getRankInfo().forEach(rankInfo -> allPlayers.add(new RankPlayerData(rankInfo.getRank(), Integer.parseInt(rankInfo.getRankData()), rankInfo.getRoleId(), rankInfo.getCareer())));

            HashMap<Integer,Integer> topMaps = new HashMap<>();
            //从前到后遍历每一个玩家
            int rankIndex = 0;
            for (int i = 0; i < allPlayers.size(); i ++) {
                boolean find = false;
                //遍历每一个区间要求
                rankIndex++;
                for (Cfg_RankAwardItem_Bean itemBean : list) {
                    //每个玩家只会找到一次区间
                    if (find) {
                        break;
                    }
                    if(itemBean.getAward_type() == 1){
                        continue;
                    }
                    //如果这个区间的要求，玩家还没达到,那么继续看下一个区间
                    if (itemBean.getNeed_value() > allPlayers.get(i).data) {
                        continue;
                    }
                    //玩家达到的了这个区间的要求，就把这个玩家的排名设为这个区间的最高排名，然后后面玩家的排名都要往后挪1
                    if (!topMaps.containsKey(itemBean.getNeed_value())){
                        topMaps.put(itemBean.getNeed_value(),itemBean.getTop_rank());
                        allPlayers.get(i).rank =  rankIndex > itemBean.getTop_rank() ? rankIndex:itemBean.getTop_rank();
                    }else {
                        int curRank =   topMaps.get(itemBean.getNeed_value()) + 1;
                        allPlayers.get(i).rank = curRank;
                        topMaps.put(itemBean.getNeed_value(),curRank);
                    }
                    needRewardPlayers.add(allPlayers.get(i));
                    for (int j = i + 1; j < allPlayers.size(); j ++) {
                        allPlayers.get(j).rank = allPlayers.get(j - 1).rank + 1;
                    }
                    find = true;
                }
            }
            String rankName = "";
            for (Cfg_RankAwardType_Bean type_bean : CfgManager.getCfg_RankAwardType_Container().getValuees()) {
                if (type_bean.getLink_rank_id() == entry.getKey()) {
                    rankName = ServerStr.getChatTableName(type_bean.getName());
                    break;
                }
            }
            //开始发奖
            for (RankPlayerData rad : needRewardPlayers) {
                List<Item> items = getAwardByRank(rad.rank, list, rad.occ);
                if (items != null) {
                    String title = MailManager.linkContext(MessageString.ACTIVITY_RANK_REAWARD_MAIL_TITLE, rankName, rad.rank);
                    Manager.mailManager.sendMailToPlayer(rad.playerId, MailType.SysCommonRewardMail,
                            MessageString.System,
                            MessageString.ACTIVITY_RANK_REAWARD_MAIL_CONTENT,
                            title,
                            items,ItemChangeReason.ActivityRankGet);
                }
            }
        }
    }

    @Override
    public void checkAwardAvailable(long playerId, int rankType, long rankValue) {
        ArrayList<Cfg_RankAwardItem_Bean> list = Manager.activityRankManager.initiativeRankAward.get(rankType);
        //不是主动领取奖励的活动排行榜
        if (list == null) {
            return;
        }
        for (Cfg_RankAwardItem_Bean bean : list) {
            if (rankValue >= bean.getNeed_value()) {
                Player player = Manager.playerManager.getPlayerOnline(playerId);
                //玩家不在线
                if (player == null) {
                    return;
                }
                //玩家已经领取了该奖励
                if (player.getActivityRankAwardGet().contains(bean.getId())) {
                    return;
                }
                //已经领光了
                if (ServerParamUtil.activityRankAwardGetTimes.getOrDefault(bean.getId(), 0) >= bean.getMax_get_count()) {
                    return;
                }
                ActivityRankListMessage.ResRankAwardAvailable.Builder res = ActivityRankListMessage.ResRankAwardAvailable.newBuilder();
                res.setRankKind(bean.getOwner_id());
                MessageUtils.send_to_player(player, ActivityRankListMessage.ResRankAwardAvailable.MsgID.eMsgID_VALUE, res.build().toByteArray());
                return;
            }
        }
    }

    /**根据排名得到奖励*/
    private List<Item> getAwardByRank(int rank, ArrayList<Cfg_RankAwardItem_Bean> list, int occ) {
        for (Cfg_RankAwardItem_Bean bean : list) {
            if (bean.getTop_rank() <= rank && bean.getBottom_rank() >= rank) {
                return Item.createItems(occ, bean.getAward_items(), 1);
            }
        }
        return null;
    }

    /**如果是那种主动领取奖励的排行榜，则填充奖励数据*/
    private void setAwardInfo(ActivityRankListMessage.ActivityRankInfo.Builder res, Player player, int type) {
        Cfg_RankAwardType_Bean type_bean = null;
        for (Cfg_RankAwardType_Bean tb : CfgManager.getCfg_RankAwardType_Container().getValuees()) {
            if (tb.getLink_rank_id() == type) {
                type_bean = tb;
                break;
            }
        }
        if (type_bean == null) {
            log.error(type + "的排行榜在RankAwardType表中找不到！");
            return;
        }
        ArrayList<Cfg_RankAwardItem_Bean> itemBeanList = new ArrayList<>();
        for (Cfg_RankAwardItem_Bean ib : CfgManager.getCfg_RankAwardItem_Container().getValuees()) {
            if (ib.getOwner_id() == type_bean.getId()) {
                //是属于活动结束后统一发放奖励
                if (ib.getAward_type() == 0) {
                    continue;
                }
                //主动领取奖励的
                else {
                    itemBeanList.add(ib);
                }
            }
        }
        if (itemBeanList.isEmpty()) {
            log.error("RankAwardItem表中没有配置" + type + "相关的数据");
            return;
        }
        for (Cfg_RankAwardItem_Bean award : itemBeanList) {
            ActivityRankListMessage.AwardInfo.Builder awardInfo = ActivityRankListMessage.AwardInfo.newBuilder();
            awardInfo.setId(award.getId());
            awardInfo.setResidue(Math.max(0, award.getMax_get_count() - ServerParamUtil.activityRankAwardGetTimes.getOrDefault(award.getId(), 0)));
            awardInfo.setGet(player.getActivityRankAwardGet().contains(award.getId()));
            res.addAward(awardInfo);
        }
    }

    private void getAward(Player player, Cfg_RankAwardItem_Bean award, int rankType) {
        //记录奖励id，不能重复领
        player.addActivityRankAwardGet(award.getId());

        //增加服务器兑换次数并保存
        Integer serverTimes = ServerParamUtil.activityRankAwardGetTimes.getOrDefault(award.getId(), 0);
        serverTimes ++;
        ServerParamUtil.activityRankAwardGetTimes.put(award.getId(), serverTimes);
        ServerParamUtil.saveActivityRankAwardGetData();

        List<Item> items = Item.createItems(player.getCareer(), award.getAward_items(), 1);
        if (! Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.ActivityRankGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.ActivityRankGet);
        }
        ActivityRankListMessage.ResGetRankAward.Builder res = ActivityRankListMessage.ResGetRankAward.newBuilder();
        res.setRankKind(rankType);
        ActivityRankListMessage.AwardInfo.Builder awardInfo = ActivityRankListMessage.AwardInfo.newBuilder();
        awardInfo.setId(award.getId());
        awardInfo.setResidue(Math.max(0, award.getMax_get_count() - serverTimes));
        awardInfo.setGet(true);
        res.setAward(awardInfo);
        MessageUtils.send_to_player(player, ActivityRankListMessage.ResGetRankAward.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    @Override
    public int getId() {
        return ScriptEnum.ActivityRankScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    static class RankPlayerData {
        int rank;
        int data;
        long playerId;
        int occ;

        public RankPlayerData(int rank, int data, long playerId, int occ) {
            this.rank = rank;
            this.data = data;
            this.playerId = playerId;
            this.occ = occ;
        }
    }
}
