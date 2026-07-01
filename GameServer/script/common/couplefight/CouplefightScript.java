package common.couplefight;

import com.data.FunctionStart;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Marry_battle_reward_Bean;
import com.data.container.Cfg_Marry_battle_reward_Container;
import com.data.struct.ReadIntegerArray;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.copymap.structs.FightRoomState;
import com.game.couplefight.manager.CouplefightManager;
import com.game.couplefight.scripts.ICouplefightScript;
import com.game.couplefight.struct.PreTeam;
import com.game.mail.manager.MailManager;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.structs.ServerStr;
import com.game.team.structs.TeamInfo;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.util.IDConfigUtil;
import game.core.util.StringUtils;
import game.message.CommonMessage;
import game.message.CouplefightMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/7/3 16:29
 */
public class CouplefightScript implements ICouplefightScript {


    static final Logger log = LogManager.getLogger(CouplefightScript.class);

    @Override
    public int getId() {
        return ScriptEnum.CouplefightScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void init() {
//        reqCouplefightInfo(null, 4);
    }

    @Override
    public void online(Player player) {
        if(Manager.couplefightManager.getFuncIds().size() > 0){
            reqCouplefightInfo(player, 5);
            reqCouplefightInfo(player, 1);
        }
    }

    @Override
    public void apply(Player player, String name) {
        //判断活动是否开启
        //判断非法字符
        if(!isNameOK(player, name)){
            sendApplyFail(player, 2);
            return;
        }

        //是否有队伍
        if(player.getTeamId() == 0){
            sendApplyFail(player, 6);
            return;
        }
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if(team == null || team.getMembers().size() != 2){
            sendApplyFail(player, 6);
            return;
        }
        if(team.getLeaderId() != player.getId()){
            sendApplyFail(player, 8);
            return;
        }
        Player p1 = Manager.playerManager.getPlayerOnline(team.getMembers().get(0));
        Player p2 = Manager.playerManager.getPlayerOnline(team.getMembers().get(1));
        if(p1 == null || p2 == null){
            sendApplyFail(player, 6);
            return;
        }
        if(p1.getSex() == p2.getSex()){
            sendApplyFail(player, 3);
            return;
        }
        Player men = null;
        Player women = null;
        if(p1.getSex() == 0){
            women = p1;
            men = p2;
        }else{
            women = p2;
            men = p1;
        }
        PreTeam t = new PreTeam(men.getId(), men.getName(), women.getId(), women.getName(), name);
        Manager.couplefightManager.getPreApplys().put(team.getTeamId(), t);

        //发送确认信息
        CouplefightMessage.ResApplyConfirm.Builder res = CouplefightMessage.ResApplyConfirm.newBuilder();
        res.setName(name);
        byte[] confirmData = res.build().toByteArray();
        MessageUtils.send_to_player(p1, CouplefightMessage.ResApplyConfirm.MsgID.eMsgID_VALUE, confirmData);
        MessageUtils.send_to_player(p2, CouplefightMessage.ResApplyConfirm.MsgID.eMsgID_VALUE, confirmData);
    }

    /**
     * 发送报名返回
     * @param player
     * @param type
     */
    private void sendApplyFail(Player player, int type) {
        CouplefightMessage.ResApply.Builder res = CouplefightMessage.ResApply.newBuilder();
        res.setSuccess(type);
        MessageUtils.send_to_player(player, CouplefightMessage.ResApply.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 判断名称是否非法
     * @param player
     * @param name
     * @return
     */
    private boolean isNameOK(Player player, String name) {
        int lenght = StringUtils.length(name, 2);
        if(name == null || lenght < 2 || lenght > 14){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NameLength_error, "2", "14"); //“对不起，您的名字不符合取名规则，请重新输入。”
            log.error("名称长度错误， name=" + name);
            return false;
        }
        if (Utils.isContainsShielding_symbol(name)) { //屏蔽标点符号检查
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SensitiveNewName); //“对不起，您的名字中包含敏感字，改名失败！”
            log.error("名称包含有屏蔽标点符号! name=" + name);
            return false;
        }
        if (Utils.isForbiddenStr(name)) { //屏蔽字检查
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SensitiveNewName); //“对不起，您的名字中包含敏感字，改名失败！”
            log.error("新角色名包含有屏蔽字!");
            return false;
        }

        if (name.contains("?")) { //屏蔽字检查
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SensitiveNewName); //“对不起，您的名字中包含敏感字，改名失败！”
            log.error("新角色名包含有屏蔽字!");
            return false;
        }
        return true;
    }

    /**
     * 报名确认
     * @param player
     */
    @Override
    public void applyConfirm(Player player, boolean confirm) {
        Long teamId = player.getTeamId();
        PreTeam t = Manager.couplefightManager.getPreApplys().get(teamId);
        if(t == null){
            return;
        }
        if(t.getMid() == player.getId()){
            t.setMconfirm(confirm);
        }else if(t.getWid() == player.getId()){
            t.setWconfirm(confirm);
        }
        if(!confirm){
            Manager.couplefightManager.getPreApplys().remove(teamId);
            //发送拒绝提示
            sendApplyConfirmFail(t, false);
            return;
        }
        //检测是否都已经确认
        if(t.isMconfirm() && t.isWconfirm()){
            Manager.couplefightManager.getPreApplys().remove(teamId);
            CouplefightMessage.G2PReqApply.Builder req = CouplefightMessage.G2PReqApply.newBuilder();
            req.setName(t.getName());
            req.setMan(getPlayerInfoPB(t.getMid()));
            req.setWoman(getPlayerInfoPB(t.getWid()));
            //发送到公共服务器
            MessageUtils.send_to_public(CouplefightMessage.G2PReqApply.MsgID.eMsgID_VALUE,req.build().toByteArray());
        }
    }

    private CouplefightMessage.PlayerInfo getPlayerInfoPB(long rid) {
        PlayerWorldInfo player = Manager.playerManager.getPlayerWorldInfo(rid);
        CouplefightMessage.PlayerInfo.Builder playerInfo = CouplefightMessage.PlayerInfo.newBuilder();
        playerInfo.setId(player.getRoleid());
        playerInfo.setName(player.getRolename());
        playerInfo.setLevel(player.getLevel());
        playerInfo.setPower(player.getFightPower());
        playerInfo.setOccupation(player.getCareer());
        playerInfo.setHead(MapUtils.getHead(player));
        playerInfo.setFacade(MapUtils.getFacade(player));
        return playerInfo.build();
    }

    private void sendApplyConfirmFail(PreTeam t, boolean b) {
        CouplefightMessage.ResApply.Builder res = CouplefightMessage.ResApply.newBuilder();
        res.setSuccess(1);
        byte[] data = res.build().toByteArray();
        Player t1 = Manager.playerManager.getPlayerCache(t.getMid());
        Player t2 = Manager.playerManager.getPlayerCache(t.getWid());
        MessageUtils.send_to_player(t1, CouplefightMessage.ResApply.MsgID.eMsgID_VALUE, data);
        MessageUtils.send_to_player(t2, CouplefightMessage.ResApply.MsgID.eMsgID_VALUE, data);
    }

    @Override
    public void matchStart(Player player) {
        if (player.playerCrossData.toFightServer) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CopyIngNotOperate);
            return;
        }
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if(team == null){
            log.warn("没有队伍或队伍人数不为2");
            sendMatchStartFail(player.getId(), 1);
            return;
        }
        if(team.getLeaderId() != player.getId()){
            log.warn("不是队长");
            sendMatchStartFail(player.getId(), 2);
            return;
        }
        if(team.getMembers().size() != 2){
            log.warn("队伍人数不足");
            sendMatchStartFail(player.getId(), 3);
            return;
        }
        Player p1 = Manager.playerManager.getPlayerCache(team.getMembers().get(0));
        Player p2 = Manager.playerManager.getPlayerCache(team.getMembers().get(1));

        if((!p1.isOnline()) || (!p2.isOnline())){
            sendMatchStartFail(player.getId(), 4);
            return;
        }
        if(p1.getSex() == p2.getSex()){
            log.warn("性别相同");
            sendMatchStartFail(player.getId(), 3);
            return;
        }
        CouplefightMessage.G2PReqMatchStart.Builder req = CouplefightMessage.G2PReqMatchStart.newBuilder();
        if(p1.getSex() == 1){
            req.setMId(p1.getId());
            req.setMpower(p1.getFightPoint());
            req.setWId(p2.getId());
            req.setWpower(p2.getFightPoint());
        }else {
            req.setWId(p1.getId());
            req.setWpower(p1.getFightPoint());
            req.setMId(p2.getId());
            req.setMpower(p2.getFightPoint());
        }
        req.setCaptainId(team.getLeaderId());
        MessageUtils.send_to_public(CouplefightMessage.G2PReqMatchStart.MsgID.eMsgID_VALUE, req.build().toByteArray());
    }

    private void sendMatchStartFail(long captain, int reason) {
        CouplefightMessage.ResMatchStart.Builder res = CouplefightMessage.ResMatchStart.newBuilder();
        res.setSuccess(false);
        res.setReason(reason);
        MessageUtils.send_to_player(captain, CouplefightMessage.ResMatchStart.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    @Override
    public void matchStop(Player player) {
        CouplefightMessage.G2PReqMatchStop.Builder req = CouplefightMessage.G2PReqMatchStop.newBuilder();
        req.setUid(player.getId());
        MessageUtils.send_to_public(CouplefightMessage.G2PReqMatchStop.MsgID.eMsgID_VALUE, req.build().toByteArray());
    }

    @Override
    public void matchConfirm(Player player, boolean confirm) {
        CouplefightMessage.G2PReqMatchConfirm.Builder req = CouplefightMessage.G2PReqMatchConfirm.newBuilder();
        req.setConfirm(confirm);
        req.setUid(player.getId());
        MessageUtils.send_to_public(CouplefightMessage.G2PReqMatchConfirm.MsgID.eMsgID_VALUE, req.build().toByteArray());
    }

    @Override
    public void p2GResFightResult(CouplefightMessage.P2GResFightResult messInfo) {
        int type = messInfo.getType();
        int reason = 0;
        if(type == CouplefightManager.award_select){//海选赛
            reason = ItemChangeReason.CoupleFightTrialsFightGet;
        }else if(type == CouplefightManager.award_group){//小组赛
            reason = ItemChangeReason.CoupleFightGroupsFightGet;
        }else if(type == 3){//冠军赛地榜
            reason = ItemChangeReason.CoupleFightDiFightGet;
            type = CouplefightManager.award_champion;
        }else if(type == 4){//冠军赛天榜
            reason = ItemChangeReason.CoupleFightTianFightGet;
            type = CouplefightManager.award_champion;
        }
        Cfg_Marry_battle_reward_Bean bean = getRewardConfig(type, 0);
        List<Item> items = Item.createItems(bean.getReward_item());
        boolean win = messInfo.getWin();
        int score = messInfo.getScore();
        for(Long uid : messInfo.getRidList()){
            Player p = Manager.playerManager.getPlayerCache(uid);
            if(p != null){
                log.info("玩家{}, id:{}, 获得仙侣对决{}奖励", p.getName(), p.getId(), type == CouplefightManager.award_select ? "海选赛" : type == CouplefightManager.award_group ? "小组赛" : "冠军赛");
                //发送对局奖励
                if(win){
                    //赢了
                    Manager.backpackManager.manager().addItems(p, items, reason, 0);
                }else{
                    //输了
                    Manager.backpackManager.manager().addItems(p, items, reason, 0);
                }
                //发战斗结果信息给玩家
                CouplefightMessage.ResFightResult.Builder result = CouplefightMessage.ResFightResult.newBuilder();
                result.setWin(win);
                result.setScore(score);
                result.setType(type);
                for(Item item : items){
                    CommonMessage.ShowItemInfo.Builder itemInfo = CommonMessage.ShowItemInfo.newBuilder();
                    itemInfo.setCount(item.getNum());
                    itemInfo.setModelId(item.getItemModelId());
                    result.addItem(itemInfo);
                }
                MessageUtils.send_to_player(p, CouplefightMessage.ResFightResult.MsgID.eMsgID_VALUE, result.build().toByteArray());
            }else{
                //玩家不在线
                log.warn("仙侣对决 海选赛奖励 没有找到玩家 id:{}", uid);
            }
        }
    }

    /**
     * 获取奖励配置
     * @param type 类型 1海选赛每场奖励；2小组赛每场奖励；3冠军赛每场奖励；4，海选赛场次奖励；5小组赛排名奖励；6冠军赛(地榜）排名奖励；7冠军赛(天榜）排名奖励
     * @return
     */
    private Cfg_Marry_battle_reward_Bean getRewardConfig(int type, int index) {
        for(Cfg_Marry_battle_reward_Bean bean : Cfg_Marry_battle_reward_Container.GetInstance().getValuees()){
            if(bean.getType() != type){
                continue;
            }
            switch (type){
                case CouplefightManager.award_select:
                case CouplefightManager.award_group:
                case CouplefightManager.award_champion:
                    return bean;
                case CouplefightManager.award_select_count:
                    if(bean.getParm().get(0) >= index){
                        return bean;
                    }
                    break;
                case CouplefightManager.award_group_rank:
                case CouplefightManager.award_champion_di_rank:
                case CouplefightManager.award_champion_tian_rank:
                    int start = bean.getParm().get(0);
                    int end = bean.getParm().get(1);
                    if(index >= start && index <= end){
                        return bean;
                    }
                    break;
            }
        }

        return null;
    }

    @Override
    public void reqCouplefightInfo(Player player, int type, long... params) {
        CouplefightMessage.G2PReqCouplefightInfo.Builder req = CouplefightMessage.G2PReqCouplefightInfo.newBuilder();
        if(player != null){
            req.setRid(player.getId());
        }else{
            req.setRid(0);
        }
        req.setType(type);
        if(params != null){
            for(int i=0;i<params.length;i++){
                req.addParam(params[i]);
            }
        }
        MessageUtils.send_to_public(CouplefightMessage.G2PReqCouplefightInfo.MsgID.eMsgID_VALUE, req.build().toByteArray());
    }

    @Override
    public void reqGroupPrepareMapEnter(Player p) {
        if (p.playerCrossData.toFightServer) {
            MessageUtils.notify_player(p, Notify.ERROR, MessageString.CopyIngNotOperate);
            return;
        }
        reqCouplefightInfo(p, 22);
        updatePlayerInfo(p);
    }

    @Override
    public void reqChampionGuess(Player player, CouplefightMessage.ReqChampionGuess messInfo) {
        //获得需要的金币
        ReadIntegerArray use = Global.Marry_battle_guess_use;
        //判断是否充足
        if(Manager.backpackManager.manager().canDeleteItemNum(player, use.get(0), use.get(1))){
            CouplefightMessage.G2PReqChampionGuess.Builder req = CouplefightMessage.G2PReqChampionGuess.newBuilder();
            req.setType(messInfo.getType());
            req.setRound(-1);
            req.setFightId(messInfo.getFightId());
            req.setTeamId(messInfo.getTeamId());
            req.setRid(player.getId());
            req.setName(player.getName());
            req.setLevel(player.getLevel());
            req.setPower(player.getFightPoint());
            MessageUtils.send_to_public(CouplefightMessage.G2PReqChampionGuess.MsgID.eMsgID_VALUE, req.build().toByteArray());
        }else{
            return;
        }
    }

    @Override
    public void p2GResChampionGuess(long rid) {
        ReadIntegerArray use = Global.Marry_battle_guess_use;
        Player player = Manager.playerManager.getPlayer(rid);
        Manager.backpackManager.manager().onRemoveItem(player, use.get(0), use.get(1), ItemChangeReason.CoupleFightGuessCost, IDConfigUtil.getLogId());
    }

    @Override
    public void p2GResTrialsInfo(CouplefightMessage.P2GResTrialsInfo messInfo) {
        long rid = messInfo.getRid();
        Player player = Manager.playerManager.getPlayerOnline(rid);
        CouplefightMessage.ResTrialsInfo.Builder res = CouplefightMessage.ResTrialsInfo.newBuilder();
        res.setIsApply(messInfo.getIsApply());
        if(messInfo.hasTrials()){
            res.setTrials(messInfo.getTrials());
        }
        CouplefightMessage.TeamInfo t = messInfo.getTeam();
        if(t != null){
            CouplefightMessage.TeamInfo.Builder teamBuild = CouplefightMessage.TeamInfo.newBuilder();
            teamBuild.setId(t.getId());
            teamBuild.setName(t.getName());
            for(CouplefightMessage.PlayerInfo pi : t.getRolesList()){
                CouplefightMessage.PlayerInfo.Builder playerBuild = pi.toBuilder();
                PlayerWorldInfo p = Manager.playerManager.getPlayerWorldInfo(pi.getId());
                playerBuild.setFacade(MapUtils.getFacade(p));
                playerBuild.setOccupation(p.getCareer());
                playerBuild.setHead(MapUtils.getHead(p));
                teamBuild.addRoles(playerBuild);
            }
            res.setTeam(teamBuild);
        }
        MessageUtils.send_to_player(player, CouplefightMessage.ResTrialsInfo.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    @Override
    public void p2GResRankAward(CouplefightMessage.P2GResRankAward messInfo) {
        int type = messInfo.getType();
        int reason = 0;
        int mailContent = 0;
        if(type == CouplefightManager.award_group_rank){
            reason = ItemChangeReason.CoupleFightGroupsRankGet;
            mailContent = MessageString.COUPLEFIGHT_MAIL_GROUPS;
        }else if(type == CouplefightManager.award_champion_di_rank){
            reason = ItemChangeReason.CoupleFightDiRankGet;
            mailContent = MessageString.COUPLEFIGHT_MAIL_DI;
        }else if(type == CouplefightManager.award_champion_tian_rank){
            reason = ItemChangeReason.CoupleFightTianRankGet;
            mailContent = MessageString.COUPLEFIGHT_MAIL_TIAN;
        }else{
            return;
        }
        for(CouplefightMessage.RankAward award : messInfo.getAwardsList()){
            int rank = award.getRank();
            Cfg_Marry_battle_reward_Bean bean = getRewardConfig(type, rank);
            if(bean == null){
                log.error("仙侣对决 排行奖励配置不存在 type:{} rank:{}", type, rank);
                continue;
            }
            //邮件内容
            String content = null;
            if(type == CouplefightManager.award_champion_di_rank || type == CouplefightManager.award_champion_tian_rank){
                int strId = 0;
                if(rank >= 16){
                    strId = MessageString.COUPLEFIGHT_SIXTEEN;
                }else if(rank >= 8){
                    strId = MessageString.COUPLEFIGHT_EIGHT;
                }else if(rank >= 4){
                    strId = MessageString.COUPLEFIGHT_FORE;
                }else if(rank >= 2){
                    strId = MessageString.COUPLEFIGHT_SECOND;
                }else if(rank >= 1){
                    strId = MessageString.COUPLEFIGHT_CHAMPION;
                }
                content = MailManager.linkContext(mailContent, "1&_" + strId);
            }else{
                content = MailManager.linkContext(mailContent, rank);
            }

            //初始化物品
            List<Item> items = Item.createItems(bean.getReward_item());
            for(long rid : award.getRidList()){
                log.info("玩家{} 获得仙侣对决{}赛 {}名奖励", rid, type, rank);
                //发送邮件
                Manager.mailManager.sendMailToPlayer(rid, MessageString.System, MessageString.System,
                        MessageString.COUPLEFIGHT_MAILTITLE, content, items, reason);
            }
        }
    }

    @Override
    public void reqGoToFight(CouplefightMessage.P2FReqGoToFight messInfo) {
        long prefid = messInfo.getPrefid();
        long fid = messInfo.getFid();
        List<Long> r1 = messInfo.getR1List();
        List<Long> r2 = messInfo.getR2List();
        MapObject preMap = Manager.mapManager.getMap(prefid);
        if(preMap != null){
            List<Player> p1 = new ArrayList<>(2);
            List<Player> p2 = new ArrayList<>(2);
            for(Long r : r1){
                Player p = preMap.getPlayer(r);
                if(p != null){
                    p1.add(p);
                }else{
                    log.info("仙侣对决 玩家{}不在准备地图",r);
                }
            }
            for(Long r : r2){
                Player p = preMap.getPlayer(r);
                if(p != null){
                    p2.add(p);
                }else{
                    log.info("仙侣对决 玩家{}不在准备地图",r);
                }
            }

            if(p1.size() == 0 || p2.size() == 0){
                //结束
                CouplefightMessage.F2PResFightResult.Builder res = CouplefightMessage.F2PResFightResult.newBuilder();
                res.setFid(fid);
                res.setType(1);
                if(p1.size() == 0){
                   res.addAllLose(r1);
                   res.addAllWin(r2);
                }else{
                    res.addAllLose(r2);
                    res.addAllWin(r1);
                }
                //发送结束消息
                MessageUtils.send_to_public(CouplefightMessage.F2PResFightResult.MsgID.eMsgID_VALUE, res.build().toByteArray());
                return;
            }

            MapObject map = Manager.mapManager.getMap(fid);
            if (map == null) {
                Map<Integer, List<Player>> ps = new HashMap<>();
                ps.put(1, p1);
                ps.put(2, p2);
                map = Manager.mapManager.createCopyMap(messInfo.getCloneId(), 1, MapManager.CopyMapOwnerSystemId, fid, ps, messInfo.getPrefid(), messInfo.getType(), messInfo.getRound());
                Manager.crossServerManager.getCrossServer().SendFightStateToPublic(map.getId(), FightRoomState.FIGHT_WAIT);
                for(Player p : p1){
                    Manager.mapManager.changeMap(p, map.getId(), map.getBriths().get(0), false);
                }
                for(Player p : p2){
                    Manager.mapManager.changeMap(p, map.getId(), map.getBriths().get(1), false);
                }
            }
        }
    }

    @Override
    public void p2GResGuessResult(CouplefightMessage.P2GResGuessResult messInfo) {
        List<CouplefightMessage.GuessResult> guessResults = messInfo.getGuessList();
        for(CouplefightMessage.GuessResult guess : guessResults){
            long rid = guess.getRid();
            boolean win = guess.getWin();
            int itemType = guess.getItemType();
            int itemNum = guess.getItemNum();
            int content = 0;
            List<Item> items = new ArrayList<>();
            items.add(Item.createItem(itemType, itemNum, false));
            if(win){
                content = MessageString.COUPLEFIGHT_MAIL_GUESS_WIN;
            }else{
                content = MessageString.COUPLEFIGHT_MAIL_GUESS_LOSE;
            }

            Manager.mailManager.sendMailToPlayer(rid, MessageString.System, MessageString.System,
                    MessageString.COUPLEFIGHT_MAILTITLE, content, items, ItemChangeReason.CoupleFightGuessGet);
        }
    }

    /**
     * 向公共服同步玩家数据
     * @param player
     */
    @Override
    public void updatePlayerInfo(Player player){
        CouplefightMessage.G2PSendPlayerInfo.Builder data = CouplefightMessage.G2PSendPlayerInfo.newBuilder();
        CouplefightMessage.PlayerInfo.Builder playerInfo = CouplefightMessage.PlayerInfo.newBuilder();
        playerInfo.setId(player.getId());
        playerInfo.setName(player.getName());
        playerInfo.setLevel(player.getLevel());
        playerInfo.setPower(player.getFightPoint());
        playerInfo.setOccupation(player.getCareer());
        playerInfo.setHead(MapUtils.getHead(player));
        playerInfo.setFacade(MapUtils.getFacade(player));
        data.setPlayer(playerInfo);
        MessageUtils.send_to_public(CouplefightMessage.G2PSendPlayerInfo.MsgID.eMsgID_VALUE, data.build().toByteArray());
    }

    @Override
    public void p2GGetTrialsAward(CouplefightMessage.P2GGetTrialsAward messInfo) {
        Cfg_Marry_battle_reward_Bean bean = Cfg_Marry_battle_reward_Container.GetInstance().getValueByKey(messInfo.getAwardId());
        if(bean != null){
            Player player = Manager.playerManager.getPlayerCache(messInfo.getRid());
            if(player != null){
                List<Item> items = Item.createItems(bean.getReward_item());
                Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CoupleFightTrialsAwardGet, 0);

                CouplefightMessage.ResGetAward.Builder res = CouplefightMessage.ResGetAward.newBuilder();
                res.setId(messInfo.getAwardId());
                MessageUtils.send_to_player(player, CouplefightMessage.ResGetAward.MsgID.eMsgID_VALUE, res.build().toByteArray());
            }else{
                log.warn("玩家不存在rid:{}", messInfo.getRid());
            }
        }else{
            log.warn("仙侣对决 奖励配置不存在id:{}", messInfo.getAwardId());
        }
    }

    @Override
    public void p2GChangeStatus(int status) {
        //设置活动状态
        Manager.couplefightManager.setStatus(status);
        //修改在线玩家功能开启
        Set<Integer> oldIds = Manager.couplefightManager.getFuncIds();
        Set<Integer> funcId = getFuncId(status);
        Manager.couplefightManager.setFuncIds(new HashSet<>(funcId));

        Set<Integer> changes = new HashSet<>();
        for(Integer id : oldIds){
            if(!funcId.contains(id)){
                changes.add(id);
            }
        }
        for(Integer id : funcId){
            if(!oldIds.contains(id)){
                changes.add(id);
            }
        }

        int[] arr = new int[changes.size()];
        int i = 0;
        for(Integer id : changes){
            arr[i] = id;
            i++;
        }

        for (Player player: Manager.playerManager.getOnLines()) {
            Manager.controlManager.deal().changePlayerFunc(player, arr);
        }
    }

    private Set<Integer> getFuncId(int status){
        Set<Integer> ids = new HashSet<>();
        if(status == CouplefightManager.status_close){
            return ids;
        }
        ids.add(FunctionStart.LoversFight);
        if(status >= CouplefightManager.status_apply){
            ids.add(FunctionStart.LoversFreeFight);
        }
        if(status >= CouplefightManager.status_group_pre){
            ids.add(FunctionStart.LoversGroupFight);
        }
        if(status >= CouplefightManager.status_champion_pre){
            ids.add(FunctionStart.LoversTopFight);
        }
        if(status >= CouplefightManager.status_di_pre){
            ids.add(FunctionStart.LoversPickFight);
        }
        return ids;
    }

    @Override
    public boolean funcIsOpen(int funcId) {
        if(Manager.couplefightManager.getFuncIds().contains(funcId)){
            return true;
        }
        return false;
    }

    @Override
    public void p2GPromotion(int type, List<Long> idList) {
        CouplefightMessage.ResPromotionInfo.Builder res = CouplefightMessage.ResPromotionInfo.newBuilder();
        res.setType(type);
        byte[] bs = res.build().toByteArray();
        int msgId = CouplefightMessage.ResPromotionInfo.MsgID.eMsgID_VALUE;
        for(Long id : idList){
            Player player = Manager.playerManager.getPlayerOnline(id);
            if(player != null){
                MessageUtils.send_to_player(player, msgId, bs);
            }
        }
    }

    @Override
    public void p2GTrialsAward(List<CouplefightMessage.TrialsAward> awardList) {
        for(CouplefightMessage.TrialsAward award : awardList){
            long rid = award.getId();
            List<Integer> awardId = award.getAwardIdList();
            List<Item> items = new ArrayList<>();
            for(Integer aid : awardId){
                Cfg_Marry_battle_reward_Bean bean = Cfg_Marry_battle_reward_Container.GetInstance().getValueByKey(aid);
                if(bean != null){
                    items.addAll(Item.createItems(bean.getReward_item()));
                }else{
                    log.warn("仙侣对决 奖励配置不存在id:{}", aid);
                }
            }

            Manager.mailManager.sendMailToPlayer(rid, MessageString.System, MessageString.System,
                    MessageString.COUPLEFIGHT_MAILTITLE, MessageString.COUPLEFIGHT_MAIL_TRIALS_COUNT, items, ItemChangeReason.CoupleFightTrialsAwardGet);
        }
    }

    @Override
    public void tick() {
        //移除过期的报名申请
        Map<Long, PreTeam> ts = Manager.couplefightManager.getPreApplys();
        long time = System.currentTimeMillis();

        for(Map.Entry<Long, PreTeam> es : ts.entrySet()){
            Long key = es.getKey();
            PreTeam v = es.getValue();
            if(v.getTime() + 600000 < time){
                ts.remove(key);
            }
        }
    }

    @Override
    public void reqChampionEnter(Player player) {
        if (player.playerCrossData.toFightServer) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CopyIngNotOperate);
            return;
        }
        reqCouplefightInfo(player, 35);
        updatePlayerInfo(player);
    }

    @Override
    public void playerOffline(Player player) {
        //海选赛匹配取消确认
        if(Manager.couplefightManager.getStatus() == CouplefightManager.status_select){
            matchConfirm(player, false);
        }
    }
}
