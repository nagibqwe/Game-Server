package common.home;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.*;
import com.data.struct.HomeTaskType;
import com.data.struct.ReadArray;
import com.game.chat.structs.Notify;
import com.game.count.structs.CountBase;
import com.game.count.structs.CountReset;
import com.game.count.structs.CountVariant;
import com.game.home.script.IHomeScript;
import com.game.home.struct.*;
import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import com.game.player.structs.Item;
import com.game.player.structs.ItemCoin;
import com.game.player.structs.SaveDeal;
import com.game.script.struct.ScriptEnum;
import com.game.server.struct.ServerParams;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.HomeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/7/12 18:18
 * @Auth ZUncle
 */
public class HomeScript implements IHomeScript {

    static final Logger logger = LogManager.getLogger(HomeScript.class);

    //访客添加人气
    final int GuestPopularity = 2;

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.HomeScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 初始化家装大赛排名
     */
    @Override
    public void initRank() {
        for (GlobalPlayerWorldInfo player : Manager.globalPlayerManager.getPlayers().values()) {
            if (player.getHouse().getVote() > 0) {
                Manager.homeManager.getRank().add(player);
                continue;
            }
            joinDecorateMatch(player);
        }
        doHouseDecorateRank();
        logger.info("初始化家装大赛数据完成len={}！！！！！！！！！", Manager.homeManager.getRank().size());
    }

    /**
     * 检测家装大赛结束
     */
    @Override
    public void checkRankOver() {
        long time = TimeUtils.Time();
        int dayOfMonth = TimeUtils.getDayOfMonth(time);
        int turn;
        if (dayOfMonth >= 1 && dayOfMonth < 15) {
            turn = 1;
        } else {
            turn = 2;
        }
        if (Manager.homeManager.getTurn() == 0) {
            Manager.homeManager.setTurn(turn);
            Manager.serverManager.server().saveServerParams(ServerParams.HomeRankTurn);
            return;
        }
        //还赛季，开始结算赛季奖励
        int lastTurn = Manager.homeManager.getTurn();
        if (lastTurn == turn) {
            return;
        }
        Manager.homeManager.setTurn(turn);
        Manager.serverManager.server().saveServerParams(ServerParams.HomeRankTurn);

        calcTurnReward(lastTurn);

        initRank();
    }

    /**
     * 发放奖励
     *
     * @param turn
     */
    void calcTurnReward(int turn) {
        HashMap<Integer, Cfg_Social_house_rank_Bean> beans = new HashMap<>();
        for (Cfg_Social_house_rank_Bean bean : CfgManager.getCfg_Social_house_rank_Container().getValuees()) {
            if (bean.getTurn() != turn) {
                continue;
            }
            int start = bean.getRank().get(0);
            int end = bean.getRank().get(1);
            if (end < start) {
                beans.put(0, bean);
                continue;
            }
            for (; start <= end; start++) {
                beans.put(start, bean);
            }
        }
        int rank = 0;
        for (GlobalPlayerWorldInfo player : Manager.homeManager.getRank()) {
            int vote = player.getHouse().getVote();
            player.getHouse().setVote(0);
            Cfg_Social_house_rank_Bean bean = beans.get(++rank);
            if (bean == null) {
                bean = beans.get(0);
            }
            if (bean == null) {
                continue;
            }
            String context = Manager.mailManager.deal().linkContext(MessageString.HouseMatchRankMailContent, rank);
            List<Item> items = Item.createItems(bean.getReward());
            Manager.mailManager.deal().sendMail(player.getId(),
                    MessageString.HouseMatchRankMailTitle,
                    context,
                    items,
                    ItemChangeReason.HouseMatchGet
            );
            logger.info("发放家装大赛奖励 rank={} vote={} player={}", rank, vote, player);
        }
    }

    /**
     * 家装大赛排序方法
     *
     * @param p1
     * @param p2
     * @return
     */
    @Override
    public int compareTo(GlobalPlayerWorldInfo p1, GlobalPlayerWorldInfo p2) {
        if (p2.getHouse().getVote() > p1.getHouse().getVote()) {
            return 1;
        }
        return p2.getHouse().getVote() == p1.getHouse().getVote() ? 0 : -1;
    }

    /**
     * 加入家装大赛
     *
     * @param player
     */
    @Override
    public boolean joinDecorateMatch(GlobalPlayerWorldInfo player) {
        House house = player.getHouse();
        if (house.getDecorate() < Global.Social_house_limit_num) {
            return false;
        }
        if (Manager.homeManager.getRank().contains(player)) {
            return true;
        }
        logger.info("加入加装大赛 player={}", player);

        return Manager.homeManager.getRank().add(player);
    }

    /**
     * 统计总装饰度
     *
     * @param player
     */
    @Override
    public void doCalcHouseDecorate(GlobalPlayerWorldInfo player) {
        House house = player.getHouse();
        Cfg_Social_house_Bean bean = CfgManager.getCfg_Social_house_Container().getValueByKey(house.getLevel());
        int decorate = bean.getDecorate_num();
        for (FurnitureFix furniture : house.getStyle().values()) {
            Cfg_Social_house_furniture_Bean furnitureBean = CfgManager.getCfg_Social_house_furniture_Container().getValueByKey(furniture.getModelId());
            decorate += furnitureBean.getDecorate_num();
        }
        house.setDecorate(decorate);

        joinDecorateMatch(player);

        if (decorate > house.getTupExp()) {
            house.setTupExp(decorate);
            doTupUpLevel(player);
            sendTupInfo(player);
            sendTaskAction(player, HomeTaskType.Tup, house.getTupLevel());
        }
        logger.info("统计家园装饰度 decorate={} player={}", decorate, player);
    }

    boolean doTupUpLevel(GlobalPlayerWorldInfo player) {
        House house = player.getHouse();
        Cfg_Social_house_level_Bean cur = CfgManager.getCfg_Social_house_level_Container().getValueByKey(house.getTupLevel());
        Cfg_Social_house_level_Bean next = CfgManager.getCfg_Social_house_level_Container().getValueByKey(house.getTupLevel() + 1);
        if (next == null) {
            return false;
        }
        if (house.getTupExp() < cur.getDecorate_num()) {
            return false;
        }
        house.setTupLevel(next.getId());
        doTupUpLevel(player);
        return true;
    }

    void sendTupInfo(GlobalPlayerWorldInfo player) {

        HomeMessage.S2GHomeInfo.Builder message = HomeMessage.S2GHomeInfo.newBuilder();
        message.setRoleId(player.getId());
        message.setTupLevel(player.getHouse().getTupLevel());
        message.setExp(player.getHouse().getTupExp());

        MessageUtils.send_to_server(player, HomeMessage.S2GHomeInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 刷新家装大赛
     */
    @Override
    public void doHouseDecorateRank() {
        Manager.homeManager.getRank().sort(this::compareTo);

        logger.info("家装大赛排序");
    }

    /**
     * 设置房屋访问权限
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void G2SAuthHomePem(GlobalPlayerWorldInfo player, HomeMessage.G2SAuthHomePem messInfo) {
        House house = player.getHouse();
        house.setAuthUnFriendEnter(messInfo.getAuthUnFriendEnter());
        house.setAuthUnFriendGift(messInfo.getAuthUnFriendOpt());
        if (messInfo.getHelper() > 0) {
            GlobalPlayerWorldInfo helper = Manager.globalPlayerManager.getPlayers().get(messInfo.getHelper());
            if (helper == null) {
                return;
            }
            house.setHelper(helper.getId());
        }

        logger.info("设置家园权限信息 opt={} player={}", messInfo, player);
    }

    /**
     * 请求进入房屋
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void G2SEnterHome(GlobalPlayerWorldInfo player, HomeMessage.G2SEnterHome messInfo) {

        GlobalPlayerWorldInfo search = Manager.globalPlayerManager.getPlayers().get(messInfo.getRoleId());
        House house = search.getHouse();

        if (search.getId() != player.getId()) {
            //不允许陌生人进入
            if (!house.isAuthUnFriendEnter() && !messInfo.getFriend()) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.HouseUnableEnter);
                return;
            }
        }
        //检测进入权限
        HomeMessage.S2GEnterHome.Builder message = HomeMessage.S2GEnterHome.newBuilder();
        message.setRoleId(player.getId());
        message.setRoomId(search.getId());
        message.setLevel(house.getLevel());
        MessageUtils.send_to_server(player, HomeMessage.S2GEnterHome.MsgID.eMsgID_VALUE, message.build().toByteArray());

        //添加访客记录
        if (player.getId() != search.getId()) {
            long first = Manager.countManager.getVariant(search, CountBase.HouseVisitor, player.getId());
            if (first == 0) {
                VisitorEvent ve = new VisitorEvent();
                ve.setRoleId(player.getId());
                ve.setPopularity(GuestPopularity);
                ve.setTime(TimeUtils.Time());
                house.getVe().add(ve);
                Manager.globalPlayerManager.deal().addCoin(search, ItemCoin.Popularity, GuestPopularity, ItemChangeReason.HouseGiftCost);
            }
            sendTaskAction(player, HomeTaskType.Visitor, 1);
        }

        Manager.countManager.setVariant(search, CountBase.HouseVisitor, CountReset.Day, player.getId(), 1);

        logger.info("请求进入房屋target={} player={}", search.getId(), player);
    }

    void init(GlobalPlayerWorldInfo player) {
        House house = player.getHouse();
        if (house.getLevel() <= 0) {
            //初始化家园
            house.setLevel(1);
            house.setTupLevel(1);
            for (ReadArray<Integer> def : Global.Normal_social_house_furniture.getValuees()) {
                Furniture furniture = house.getStore().getOrDefault(def.get(0), new Furniture());
                furniture.setModelId(def.get(0));
                furniture.setCount(furniture.getCount() + def.get(1));
                house.getStore().put(furniture.getModelId(), furniture);
                //默认家具自动装饰
                furniture.setCount(furniture.getCount() - 1);
                FurnitureFix style = new FurnitureFix();
                style.setId(IDConfigUtil.getLogId());
                style.setModelId(furniture.getModelId());
                style.setPos(new Vector3());
                style.setDir(0);
                house.getStyle().put(style.getId(), style);
            }
        }
        if (house.getDecorate() <= 0) {
            doCalcHouseDecorate(player);
            sendTaskAction(player, HomeTaskType.Decorate, house.getDecorate());
        }
    }

    /**
     * 请求房屋信息
     *
     * @param player
     * @param messInfo
     * @param isMapInfoSyn
     */
    @Override
    public void G2SHomeInfo(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeInfo messInfo, boolean isMapInfoSyn) {

        GlobalPlayerWorldInfo search = player;
        if (messInfo.getRoleId() > 0) {
            search = Manager.globalPlayerManager.getPlayers().get(messInfo.getRoleId());
        }
        init(search);

        House house = search.getHouse();

        HomeMessage.HomeInfo.Builder mHome = mHome(search);
        mHome.setTupReward(messInfo.getTupReward());

        HomeMessage.ResHomeInfo.Builder message = HomeMessage.ResHomeInfo.newBuilder();
        message.setPopularityHistory(search.getPopularity());
        message.setAuthUnFriendEnter(house.isAuthUnFriendEnter());
        message.setAuthUnFriendOpt(house.isAuthUnFriendGift());
        message.setMapInfo(isMapInfoSyn);
        message.setOwner(mHome);
        if (house.getHelper() > 0) {
            GlobalPlayerWorldInfo helper = Manager.globalPlayerManager.getPlayers().get(search.getHouse().getHelper());
            message.setHelper(mRole(helper));
        }
        MessageUtils.send_to_player(player, HomeMessage.ResHomeInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());

        sendTupInfo(player);

        logger.info("获取房屋信息 target={} player={}", search.getId(), player);

    }

    /**
     * 玩家数据序列化
     *
     * @param player
     * @return
     */
    HomeMessage.HomeRole.Builder mRole(GlobalPlayerWorldInfo player) {
        HomeMessage.HomeRole.Builder m = HomeMessage.HomeRole.newBuilder();
        m.setId(player.getId());
        m.setName(player.getRoleName());
        m.setCareer(player.getCareer());
        m.setLevel(player.getLevel());
        m.setServerId(player.getServerId());
        m.setSign(player.getPlayerCommunityInfoSettingInfo().getSign() == null ? "" : player.getPlayerCommunityInfoSettingInfo().getSign());
        m.setScore(player.getHouse().getVote());
        return m;
    }

    HomeMessage.Furniture.Builder mFurniture(Furniture furniture) {
        HomeMessage.Furniture.Builder m = HomeMessage.Furniture.newBuilder();
        m.setModelId(furniture.getModelId());
        m.setCount(furniture.getCount());
        return m;
    }

    HomeMessage.FurnitureCell.Builder mFurniture(FurnitureFix furniture) {
        HomeMessage.Vector3.Builder pos = HomeMessage.Vector3.newBuilder();
        pos.setX(furniture.getPos().getX());
        pos.setY(furniture.getPos().getY());
        pos.setZ(furniture.getPos().getZ());

        HomeMessage.FurnitureCell.Builder m = HomeMessage.FurnitureCell.newBuilder();
        m.setId(furniture.getId());
        m.setModelId(furniture.getModelId());
        m.setPos(pos);
        m.setDir(furniture.getDir());
        return m;
    }

    HomeMessage.HomeInfo.Builder mHome(GlobalPlayerWorldInfo player) {
        House house = player.getHouse();
        HomeMessage.HomeInfo.Builder mHome = HomeMessage.HomeInfo.newBuilder();
        mHome.setOwner(mRole(player));
        mHome.setLevel(house.getLevel());
        mHome.setTupLevel(house.getTupLevel());
        mHome.setTupExp(house.getTupExp());
        mHome.setTupReward(false);
        mHome.setTupRewardExp(calcHookExp(player));

        for (Furniture furniture : house.getStore().values()) {
            mHome.addStore(mFurniture(furniture));
        }
        for (FurnitureFix furniture : house.getStyle().values()) {
            mHome.addCells(mFurniture(furniture));
        }
        return mHome;
    }

    /**
     * 获取家装大赛投票数据
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void G2SHomeTrimMatchScore(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeTrimMatchScore messInfo) {

        sendMatchVote(player);

        logger.info("获取投票数据 player={}", player);
    }


    void sendMatchVote(GlobalPlayerWorldInfo player) {
        HomeMessage.ResHomeTrimMatchScore.Builder message = HomeMessage.ResHomeTrimMatchScore.newBuilder();
        long vote = Manager.countManager.getVariant(player, CountVariant.HouseVote);

        long randomVote = Manager.countManager.getVariant(player, CountVariant.HouseRandomVote);
        message.setScore(Global.Social_house_vote_num - (int) vote);
        message.setRandomScore(Global.Social_house_random_vote_num - (int) randomVote);

        MessageUtils.send_to_player(player, HomeMessage.ResHomeTrimMatchScore.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 获取家装大赛排名
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void G2SHomeTrimRank(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeTrimRank messInfo) {

        HomeMessage.ResHomeTrimRank.Builder message = HomeMessage.ResHomeTrimRank.newBuilder();
        for (GlobalPlayerWorldInfo role : Manager.homeManager.getRank()) {
            message.addRank(mRole(role));
        }

        MessageUtils.send_to_player(player, HomeMessage.ResHomeTrimRank.MsgID.eMsgID_VALUE, message.build().toByteArray());
        logger.info("获取家装大赛排名 player={}", player);
    }

    /**
     * 投票家园
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void G2SHomeTrimVote(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeTrimVote messInfo) {

        GlobalPlayerWorldInfo search = Manager.globalPlayerManager.getPlayers().get(messInfo.getRoleId());
        if (search == null) {
            logger.warn("投票目标为空");
            return;
        }

        if (!Manager.homeManager.getRank().contains(search)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.HouseMatchVoteUnJoin);
            logger.warn("玩家未参赛 player={}", search);
            return;
        }

        //随机投票
        if (messInfo.getType() == 0) {
            long randomVote = Manager.countManager.getVariant(player, CountVariant.HouseRandomVote);
            if (randomVote >= Global.Social_house_random_vote_num) {
                sendMatchVote(player);
                return;
            }
            Manager.countManager.addVariant(player, CountVariant.HouseRandomVote, 1);
        } else {
            long vote = Manager.countManager.getVariant(player, CountVariant.HouseVote);
            if (vote >= Global.Social_house_vote_num) {
                sendMatchVote(player);
                return;
            }
            Manager.countManager.addVariant(player, CountVariant.HouseVote, 1);
        }

        House house = search.getHouse();

        house.setVote(house.getVote() + 1);

        sendMatchVote(player);

        //发放投票奖励
        List<Item> items = Item.createItems(Global.Social_house_random_vote_reward);
        Manager.globalPlayerManager.deal().addItems(player, items, ItemChangeReason.HouseVoteGet);


        logger.info("投票 target={} type={} player={}", search.getId(), messInfo.getType(), player);
        //TODO 刷新排行榜
        doHouseDecorateRank();
    }

    /**
     * 获取访客送礼清单
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void G2SHomeVisitorGiftList(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeVisitorGiftList messInfo) {

        GlobalPlayerWorldInfo search = Manager.globalPlayerManager.getPlayers().get(messInfo.getRoleId());

        sendGiftMenu(player, search);

        logger.info("获取送礼清单 player={}", player);
    }

    void sendGiftMenu(GlobalPlayerWorldInfo player, GlobalPlayerWorldInfo search) {
        HomeMessage.ResHomeVisitorGiftList.Builder message = HomeMessage.ResHomeVisitorGiftList.newBuilder();

        for (Cfg_Social_house_gift_Bean bean : CfgManager.getCfg_Social_house_gift_Container().getValuees()) {

            long max = Manager.countManager.getVariant(player, CountBase.HouseGift, bean.getId());

            HashMap<Long, Long> sendNote = Manager.countManager.getHashValue(player, CountBase.HouseGiftSend, bean.getId());

            long sendCount = sendNote.getOrDefault(search.getId(), 0L);

            HomeMessage.VisitorGift.Builder gift = HomeMessage.VisitorGift.newBuilder();
            gift.setId(bean.getId());
            gift.setMax((int) max);
            gift.setUse((int) sendCount);

            message.addGift(gift);
        }
        MessageUtils.send_to_player(player, HomeMessage.ResHomeVisitorGiftList.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 获取访客记录
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void G2SHomeVisitorNote(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeVisitorNote messInfo) {

        GlobalPlayerWorldInfo search = player;
        if (messInfo.getRoleId() > 0) {
            search = Manager.globalPlayerManager.getPlayers().get(messInfo.getRoleId());
        }

        HomeMessage.ResHomeVisitorNote.Builder message = HomeMessage.ResHomeVisitorNote.newBuilder();

        for (VisitorEvent ve : search.getHouse().getVe()) {
            GlobalPlayerWorldInfo visitor = Manager.globalPlayerManager.getPlayers().get(ve.getRoleId());
            if (visitor == null) {
                continue;
            }
            HomeMessage.HomeVisitor.Builder note = HomeMessage.HomeVisitor.newBuilder();
            note.setRole(mRole(visitor));
            note.setGift(ve.getGift());
            note.setEnergy(ve.getPopularity());
            note.setTime(ve.getTime());
            message.addVisitor(note);
        }
        MessageUtils.send_to_player(player, HomeMessage.ResHomeVisitorNote.MsgID.eMsgID_VALUE, message.build().toByteArray());
        logger.info("获取访客记录target={} player={}", search.getId(), player);
    }


    /**
     * 获取投票对象
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void G2SRandomHomeTrimTarget(GlobalPlayerWorldInfo player, HomeMessage.G2SRandomHomeTrimTarget messInfo) {

        HomeMessage.ResRandomHomeTrimTarget.Builder message = HomeMessage.ResRandomHomeTrimTarget.newBuilder();

        /**
         * 	1.选取当前排行的第一名和第二名为第一次随机投票的双方；
         * 	2.在排行榜前10中随机选择一名玩家和比他排名第一名的玩家作为随机投票的双方；
         * 	3.在排行榜中随机选择一名玩家和比他排名第一名的玩家作为随机投票的双方；
         */
        List<GlobalPlayerWorldInfo> rank = new ArrayList<>(Manager.homeManager.getRank());

        if (player.getHouse().getRandomVote().size() >= rank.size()) {
            player.getHouse().getRandomVote().clear();
        }

        //1-2名
        int max = Math.min(2, rank.size());
        List<GlobalPlayerWorldInfo> from1_2 = rank.subList(0, max);

        from1_2 = Utils.find(from1_2, r -> !player.getHouse().getRandomVote().contains(r.getId()));
        if (from1_2.size() > 0) {
            for (GlobalPlayerWorldInfo role : from1_2) {
                message.addHomeList(mHome(role));
                player.getHouse().getRandomVote().add(role.getId());
            }
            MessageUtils.send_to_player(player, HomeMessage.ResRandomHomeTrimTarget.MsgID.eMsgID_VALUE, message.build().toByteArray());
            logger.info("随机投票1-2名！！！！！！！！！！");
            return;
        }
        if (rank.size() < 3) {
            MessageUtils.send_to_player(player, HomeMessage.ResRandomHomeTrimTarget.MsgID.eMsgID_VALUE, message.build().toByteArray());
            return;
        }

        //2-10名
        max = Math.min(10, rank.size());
        List<GlobalPlayerWorldInfo> from3_10 = rank.subList(2, max);

        from3_10 = Utils.find(from3_10, r -> !player.getHouse().getRandomVote().contains(r.getId()));

        if (from3_10.size() > 0) {
            if (from3_10.size() > 2) {
                int one = Utils.random(0, from3_10.size() - 2);
                from3_10 = from3_10.subList(one, one + 2);
            }
            for (GlobalPlayerWorldInfo role : from3_10) {
                message.addHomeList(mHome(role));
                player.getHouse().getRandomVote().add(role.getId());
            }
            MessageUtils.send_to_player(player, HomeMessage.ResRandomHomeTrimTarget.MsgID.eMsgID_VALUE, message.build().toByteArray());
            logger.info("随机投票2-10名！！！！！！！！！！");
            return;
        }
        if (rank.size() < 11) {
            MessageUtils.send_to_player(player, HomeMessage.ResRandomHomeTrimTarget.MsgID.eMsgID_VALUE, message.build().toByteArray());
            return;
        }

        List<GlobalPlayerWorldInfo> tail = rank.subList(10, rank.size());

        tail = Utils.find(tail, r -> !player.getHouse().getRandomVote().contains(r.getId()));

        if (tail.size() > 0) {
            if (tail.size() > 2) {
                int one = Utils.random(0, tail.size() - 2);
                tail = tail.subList(one, one + 2);
            }
            for (GlobalPlayerWorldInfo role : tail) {
                message.addHomeList(mHome(role));
                player.getHouse().getRandomVote().add(role.getId());
            }
        }

        logger.info("随机投票大于10名！！！！！！！！！！");
        MessageUtils.send_to_player(player, HomeMessage.ResRandomHomeTrimTarget.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 获取访客送礼
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void G2SSendVisitorGift(GlobalPlayerWorldInfo player, HomeMessage.G2SSendVisitorGift messInfo) {

        GlobalPlayerWorldInfo search = Manager.globalPlayerManager.getPlayers().get(messInfo.getRoleId());

        House house = search.getHouse();

        //房主不接受陌生人礼物
        if (!house.isAuthUnFriendGift() && !messInfo.getFriend()) {
            return;
        }
        Cfg_Social_house_gift_Bean bean = CfgManager.getCfg_Social_house_gift_Container().getValueByKey(messInfo.getGiftId());
        if (bean == null) {
            return;
        }
        long count = Manager.countManager.getVariant(player, CountBase.HouseGift, bean.getId());
        if (count >= bean.getDaily_max()) {
            return;
        }
        HashMap<Long, Long> sendNote = Manager.countManager.getHashValue(player, CountBase.HouseGiftSend, bean.getId());
        long sendCount = sendNote.getOrDefault(search.getId(), 0L);
        //每样礼物只能送一次
        if (sendCount > 0) {
            return;
        }
        //送礼计数
        Manager.countManager.setVariant(player, CountBase.HouseGift, CountReset.Day, bean.getId(), count + 1);

        //被送礼计数
        sendNote.put(search.getId(), sendCount + 1);
        Manager.countManager.setVariant(player, CountBase.HouseGiftSend, CountReset.Day, bean.getId(), 0);

        Manager.globalPlayerManager.deal().addCoin(search, ItemCoin.Popularity, bean.getPopularity_num(), ItemChangeReason.HouseGiftCost);

        sendGiftMenu(player, search);

        sendTaskAction(player, HomeTaskType.Gift, 1);

        VisitorEvent ve = new VisitorEvent();
        ve.setRoleId(player.getId());
        ve.setGift(bean.getId());
        ve.setPopularity(bean.getPopularity_num());
        ve.setTime(TimeUtils.Time());
        house.getVe().add(ve);

        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.C_HOME_VICTORELOG2, Utils.getChatTableName(bean.getName()), bean.getPopularity_num());

        logger.info("送礼添加 人气+{} player={} ", bean.getPopularity_num(), search);
    }

    /**
     * 房屋装饰
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void G2SHomeDecorate(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeDecorate messInfo) {

        GlobalPlayerWorldInfo search = Manager.globalPlayerManager.getPlayers().get(messInfo.getTargetId());
        if (search == null) {
            return;
        }
        House house = search.getHouse();
        boolean auth = player.getId() == search.getId() || house.getHelper() == player.getId();
        if (!auth) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.HouseUnableDecorate);
            return;
        }

        HomeMessage.ResHomeDecorate.Builder message = HomeMessage.ResHomeDecorate.newBuilder();
        message.setType(messInfo.getType());
        message.setTargetId(search.getId());


        if (messInfo.getType() == 1) {
            FurnitureFix style = house.getStyle().get(messInfo.getFurniture().getId());
            //移动家具
            if (style != null) {
                style.setPos(new Vector3(messInfo.getFurniture().getPos()));
                style.setDir(messInfo.getFurniture().getDir());

                Furniture furniture = house.getStore().get(style.getModelId());
                message.setStore(furniture.encode());
                logger.info("家具移动 furniture={} target={} player={}", style.getModelId(), search.getId(), player);
            } else {

                Furniture furniture = house.getStore().get(messInfo.getFurniture().getModelId());
                if (furniture == null || furniture.getCount() <= 0) {
//                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.LIANQI_GEM_NEEDITEMNOTENOUGH);
                    logger.info("家具安装失败 furniture={} target={} player={}", messInfo.getFurniture().getModelId(), search.getId(), player);
                    return;
                }
                Cfg_Social_house_furniture_Bean bean = CfgManager.getCfg_Social_house_furniture_Container().getValueByKey(furniture.getModelId());
                furniture.setCount(furniture.getCount() - 1);

                style = new FurnitureFix();
                style.setId(messInfo.getFurniture().getId());
                style.setModelId(furniture.getModelId());
                style.setPos(new Vector3(messInfo.getFurniture().getPos()));
                style.setDir(messInfo.getFurniture().getDir());
                house.getStyle().put(style.getId(), style);
                house.setDecorate(house.getDecorate() + bean.getDecorate_num());
                message.setStore(furniture.encode());
                logger.info("家具安装 furniture={} target={} player={}", style.getModelId(), search.getId(), player);
            }
            message.setFurniture(style.encode());
        } else {
            //拆除
            FurnitureFix furnitureFix = house.getStyle().remove(messInfo.getFurniture().getId());
            if (furnitureFix == null) {
                logger.info("家具拆除失败 furniture={}  target={} player={}", messInfo.getFurniture().getId(), search.getId(), player);
                return;
            }
            Furniture furniture = house.getStore().get(furnitureFix.getModelId());
            furniture.setCount(furniture.getCount() + 1);

            Cfg_Social_house_furniture_Bean bean = CfgManager.getCfg_Social_house_furniture_Container().getValueByKey(furniture.getModelId());
            house.setDecorate(house.getDecorate() - bean.getDecorate_num());

            message.setStore(furniture.encode());
            message.setFurniture(furnitureFix.encode());

            logger.info("家具拆除 furniture={}  target={} player={}", furniture.getModelId(), search.getId(), player);
        }
        MessageUtils.send_to_player(player, HomeMessage.ResHomeDecorate.MsgID.eMsgID_VALUE, message.build().toByteArray());

        for (long roleId : house.getSceneMember()) {
            if (roleId == player.getId()) {
                continue;
            }
            GlobalPlayerWorldInfo scene = Manager.globalPlayerManager.getPlayers().get(roleId);
            MessageUtils.send_to_player(scene, HomeMessage.ResHomeDecorate.MsgID.eMsgID_VALUE, message.build().toByteArray());
        }

        if (messInfo.getType() == 1) {
            sendSceneChange(search);
        }

        doCalcHouseDecorate(search);

        sendTaskAction(search, HomeTaskType.Decorate, house.getDecorate());

        Manager.globalPlayerManager.deal().save2DB(player, SaveDeal.TenMinLater);

        if (messInfo.getType() == 1) {
            joinDecorateMatch(search);
        }
    }

    /**
     * 房屋升级
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void G2SHomeLevelUp(GlobalPlayerWorldInfo player, HomeMessage.G2SHomeLevelUp messInfo) {

        House house = player.getHouse();
        house.setLevel(house.getLevel() + 1);

        HomeMessage.ResHomeLevelUp.Builder message = HomeMessage.ResHomeLevelUp.newBuilder();
        message.setHomeLevel(house.getLevel());

        MessageUtils.send_to_player(player, HomeMessage.ResHomeLevelUp.MsgID.eMsgID_VALUE, message.build().toByteArray());

        for (long roleId : house.getSceneMember()) {
            if (roleId == player.getId()) {
                continue;
            }
            GlobalPlayerWorldInfo scene = Manager.globalPlayerManager.getPlayers().get(roleId);
            MessageUtils.send_to_player(scene, HomeMessage.ResHomeLevelUp.MsgID.eMsgID_VALUE, message.build().toByteArray());
        }

        sendSceneChange(player);

        doCalcHouseDecorate(player);

        Manager.globalPlayerManager.deal().save2DB(player, SaveDeal.RightNow);

        logger.info("家园升级 house={} player={}", house.getLevel(), player);
    }

    /**
     * 发送场景改变
     *
     * @param player
     */
    void sendSceneChange(GlobalPlayerWorldInfo player) {
        HomeMessage.S2FHomeSceneChange.Builder message = HomeMessage.S2FHomeSceneChange.newBuilder();
        message.setRoleId(player.getId());
        message.setLevel(player.getHouse().getLevel());

        MessageUtils.send_all_fight(HomeMessage.S2FHomeSceneChange.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 家园场景同步玩家数据到家园
     *
     * @param messInfo
     */
    @Override
    public void F2SHomePlayerInfo(HomeMessage.F2SHomePlayerInfo messInfo) {

        GlobalPlayerWorldInfo search = Manager.globalPlayerManager.getPlayers().get(messInfo.getHouseId());
        if (search == null) {
            return;
        }

        House house = search.getHouse();
        house.getSceneMember().clear();
        messInfo.getRoleIdList().forEach(id -> house.getSceneMember().add(id));

        if (search.getHorseId() == messInfo.getEnterRole()) {
            return;
        }
//        logger.info("同步场景人数 size={} player={} ", house.getSceneMember().size(), search);

        GlobalPlayerWorldInfo enter = Manager.globalPlayerManager.getPlayers().get(messInfo.getEnterRole());
        if (messInfo.getEnterRole() <= 0 || enter == null) {
            return;
        }
//        logger.info("enterId={} enter={}", messInfo.getEnterRole(), enter);
        //TODO 进入家园同步一次家装信息
        HomeMessage.G2SHomeInfo.Builder req = HomeMessage.G2SHomeInfo.newBuilder();
        req.setRoleId(messInfo.getHouseId());

        G2SHomeInfo(enter, req.build(), true);
    }

    /**
     * 添加家具
     *
     * @param player
     * @param modelId
     * @param count
     * @param changeReason
     */
    @Override
    public void addFurniture(GlobalPlayerWorldInfo player, int modelId, int count, int changeReason) {

        Cfg_Social_house_furniture_Bean bean = CfgManager.getCfg_Social_house_furniture_Container().getValueByKey(modelId);
        if (bean == null) {
            logger.info("添加未知家具 modelId={} player={}", modelId, player);
            return;
        }

        House house = player.getHouse();

        Furniture furniture = house.getStore().getOrDefault(modelId, new Furniture());
        furniture.setModelId(modelId);
        furniture.setCount(furniture.getCount() + count);
        house.getStore().put(furniture.getModelId(), furniture);

        Manager.globalPlayerManager.deal().save2DB(player, SaveDeal.OneMinLater);

        HomeMessage.ResHomeFurnitureUpdate.Builder message = HomeMessage.ResHomeFurnitureUpdate.newBuilder();
        message.setStore(furniture.encode());
        message.setChangeReason(changeReason);

        MessageUtils.send_to_player(player, HomeMessage.ResHomeFurnitureUpdate.MsgID.eMsgID_VALUE, message.build().toByteArray());

        sendTaskAction(player, HomeTaskType.BuyFurniture, 1);
        sendTaskAction(player, HomeTaskType.CollectionSuit, furniture.getModelId(), furniture.getCount());
        sendTaskAction(player, HomeTaskType.CollectionType, furniture.getModelId(), furniture.getCount());

        logger.info("添加家具 modelId={} count={} player={}", modelId, count, player);
    }

    /**
     * 更新玩家数据
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void G2SUpdatePlayerInfo(GlobalPlayerWorldInfo player, HomeMessage.G2SUpdatePlayerInfo messInfo) {
        if (player == null) {
            return;
        }
        init(player);
        player.setExpRate(messInfo.getExpRate());
        player.setPopularity(messInfo.getPopularity());
        player.getHouse().setLevel(messInfo.getHomeLevel());

        Manager.globalPlayerManager.deal().save2DB(player, SaveDeal.TenMinLater);
    }

    long calcHookExp(GlobalPlayerWorldInfo player) {

        Cfg_On_hook_Bean hook = CfgManager.getCfg_On_hook_Container().getValueByKey(player.getLevel());
        if (hook == null) {
            return -1;
        }
        Cfg_Social_house_level_Bean tup = CfgManager.getCfg_Social_house_level_Container().getValueByKey(player.getHouse().getTupLevel());
        if (tup == null) {
            return -1;
        }
        long exp = (long) (tup.getLevelup_pay() * player.getExpRate() * hook.getExp());
        return exp;
    }

    /**
     * 同步任务进度
     *
     * @param player
     * @param type
     * @param params
     */
    void sendTaskAction(GlobalPlayerWorldInfo player, HomeTaskType type, int... params) {
        HomeMessage.S2GActionTask.Builder message = HomeMessage.S2GActionTask.newBuilder();
        message.setRoleId(player.getId());
        message.setType(type.getValue());
        for (int param : params) {
            message.addArgs(param);
        }
        MessageUtils.send_to_server(player, HomeMessage.S2GActionTask.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }
}
