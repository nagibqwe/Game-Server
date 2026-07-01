package common.luckydraw;

import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Week_welfare_Bean;
import com.data.bean.Cfg_Week_welfare_reward_Bean;
import com.data.container.Cfg_Week_welfare_Container;
import com.data.container.Cfg_Week_welfare_reward_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.luckydraw.script.ILuckyDrawWeekScript;
import com.game.luckydraw.structs.LuckyDrawRecord;
import com.game.luckydraw.structs.LuckyDrawWeekData;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.EntityState;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.LuckyDrawMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaozhaoguang
 * @desc 一周福利幸运抽奖的脚本
 * @date Created on 2020/8/20 17:32
 **/
public class LuckyDrawWeekScript  implements ILuckyDrawWeekScript {

    private static final Logger logger = LogManager.getLogger(LuckyDrawWeekScript.class);

    //幸运抽奖的抽奖记录的最大数量
    private static final int CN_LUCKY_DRAW_RECORD_MAX = 20;
    //使用保底幸运几率的抽奖次数
    private static final int CN_LUCKY_DRAW_TRANSFER_PROBABILITY_TIMES = 10;

    @Override
    public int getId() {
        return ScriptEnum.LuckyDrawWeekScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }


    /**
     * 打开面板请求
     * @param player
     * @param msg
     */
    @Override
    public void onReqLuckyDrawPanelHandler(Player player, LuckyDrawMessage.ReqOpenLuckyDrawPanel msg) {
        onRefreshLuckyDrawData(player,false);
    }

    /**
     * 刷新抽奖数据
     * @param player
     * @param isFunctionVariable  如果是false,表示是玩家的请求,如果true,则表示是FunctionVariable的驱动
     */
    @Override
    public void onRefreshLuckyDrawData(Player player,boolean isFunctionVariable){
        //0.判断数据,并进行初始化
        if(!checkLuckDrawValid()){
            initServerData();
        }
        LuckyDrawWeekData wd = Manager.luckyDrawManager.getWeekData();
        LuckyDrawMessage.ResOpenLuckyDrawPanelResult.Builder res = LuckyDrawMessage.ResOpenLuckyDrawPanelResult.newBuilder();

        //1.填充已经获取的奖励次数
        res.setAwardtimes(player.getLuckyDrawWeekTimes());

        //2.是否领取抽奖卷
        //2.1判断角色身上是否有奖卷记录
        if(player.getLuckyDrawWeekAwardVolumes().isEmpty()){
            initPlayerData(player);
        }
        //是否需要同步到客户端
        boolean needSynClient = false;
        //2.2填充奖卷领取情况的记录
        for(Integer id : player.getLuckyDrawWeekAwardVolumes().keySet()){
            Cfg_Week_welfare_Bean bean = Cfg_Week_welfare_Container.GetInstance().getValueByKey(id);
            LuckyDrawMessage.getDrawVolumeInfo.Builder dv = LuckyDrawMessage.getDrawVolumeInfo.newBuilder();
            dv.setId(id);
            dv.setMaxCount(bean.getCondition().get(bean.getCondition().size()-1));
            dv.setProgress(Manager.controlManager.deal().getFuncProgress(player,bean.getCondition()));
            dv.setIsGet(player.getLuckyDrawWeekAwardVolumes().get(id));
            //2.2.1 假如还没有领取
            if(!dv.getIsGet()){
                //2.2.2 如果进度已经达到了,就同步到客户端
                if(dv.getMaxCount() <= dv.getProgress()){
                    needSynClient = true;
                }
            }
            res.addGetVolumes(dv);
        }

        if(!isFunctionVariable) {
            //3.设置玩家打开当前Panel的标记
            wd.getPlayerOpenPanels().put(player.getId(), true);
            needSynClient = true;
        }

        //4.同步到客户端
        if(needSynClient) {
            MessageUtils.send_to_player(player, LuckyDrawMessage.ResOpenLuckyDrawPanelResult.MsgID.eMsgID_VALUE, res.build().toByteArray());
        }
    }




    /**
     * 抽奖处理
     * @param player
     * @param msg
     */
    @Override
    public void onReqLuckyDrawHandler(Player player, LuckyDrawMessage.ReqLuckyDraw msg) {
        //0.判断当前是否抽奖日
        if(!checkLuckyDrawDay()){
           // MessageUtils.notify_player(player,Notify.ERROR,"今天不是幸运抽奖日!");
            SendLuckyDrawReturnCode(player,-1,-1,-1);
            return;
        }
        //1.判断当前数据是否正确
        if(!checkLuckDrawValid()){
            logger.error("抽奖失败,请检查配置表,服务器奖池中数据为空!" + " PlayerID:" + player.getId());
           // MessageUtils.notify_player(player,Notify.ERROR,"抽奖异常,没有找到奖励物品!");
            SendLuckyDrawReturnCode(player,-2,-1,-1);
            return;
        }

        //2.开始抽奖
        //2.1 判断当前是否为幸运抽奖 判断本次是否为幸运抽奖的次数
        boolean isLucky = (player.getLuckyDrawWeekTimes() == Global.Week_Welfare_Luck_Probability_Limit-1);

        //2.2 进行抽奖获得奖品等级
        Integer drawType = GetLuckyDrawType(isLucky);
        if(drawType < 0){
            logger.error("抽奖失败,请检查配置表:drawType:"+drawType + " PlayerID:" + player.getId());
            //MessageUtils.notify_player(player,Notify.ERROR,"抽奖异常,没有找到奖励物品!");
            SendLuckyDrawReturnCode(player,-3,-1,-1);
            return;
        }

        //2.3 进行抽奖,在某个奖品等级中,随机选择一个物品.
        Integer idx = GetLuckyDrawRewardIndex(player,drawType);
        if(idx < 0){
            logger.error("奖励索引配置失败,请检查配置表:drawType:"+drawType+" itemIndex:" + idx + " PlayerID:" + player.getId());
            //MessageUtils.notify_player(player,Notify.ERROR,"抽奖异常,没有找到奖励物品!");
            SendLuckyDrawReturnCode(player,-4,-1,-1);
            return;
        }

        //2.4 判断索引的抽奖等级是否存在
        Cfg_Week_welfare_reward_Bean bean = GetRewardBean(drawType);
        if(bean == null){
            logger.error("没有找到抽奖类型,请检查配置表:drawType:"+drawType+" itemIndex:" + idx + " PlayerID:" + player.getId());
            //MessageUtils.notify_player(player,Notify.ERROR,"抽奖异常,没有找到奖励物品!");
            SendLuckyDrawReturnCode(player,-5,-1,-1);
            return;
        }

        //3:开始处理物品信息
        //3.1 把索引转换为物品列表
        ReadArray<Integer> ia = bean.getRewardPool().get(idx);
        List<Item> items = Item.createItems(ia,1);

        //3.2查看奖励物品是否正确
        if(items == null){
            logger.error("没有在配置表中找到响应的奖励物品:drawType:"+drawType+" itemIndex:" + idx + " PlayerID:" + player.getId());
            //MessageUtils.notify_player(player,Notify.ERROR,"抽奖异常,没有找到奖励物品!");
            SendLuckyDrawReturnCode(player,-6,-1,-1);
            return;
        }

        //3.3查看背包空间是否充足
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
            //MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            SendLuckyDrawReturnCode(player,-7,-1,-1);
            return;
        }


        //4.开始些标记:
        //4.0 扣除抽奖卷
        if(!Manager.backpackManager.manager().removeItemOrCurrency(player,26,1,IDConfigUtil.getLogId(),ItemChangeReason.LuckyDrawWeekDec)){
            logger.error("扣除抽奖卷失败! PlayerID:" + player.getId());
            //MessageUtils.notify_player(player,Notify.ERROR,"抽奖异常,扣除抽奖卷失败!");
            SendLuckyDrawReturnCode(player,-8,-1,-1);
            return ;
        }
        //4.1设置增加抽奖次数
        if(isLucky) {
            player.setLuckyDrawWeekTimes(0);
        }
        else{
            player.setLuckyDrawWeekTimes(player.getLuckyDrawWeekTimes() + 1);
        }

        //4.2创建抽奖记录
        LuckyDrawRecord record = new LuckyDrawRecord();
        record.setAwardType(drawType);
        record.setRoleName(player.getName());
        record.setItemModelID(ia.get(0));
        record.setNum(ia.get(1));
        record.setBind(ia.get(2)==1);
        record.setTime(TimeUtils.Time());

        LuckyDrawWeekData data = Manager.luckyDrawManager.getWeekData();

        //4.2.1: 日志添加到角色信息中
        if(!data.getRoleRecords().containsKey(player.getId())){
            data.getRoleRecords().put(player.getId(),new ArrayList<>());
        }
        List<LuckyDrawRecord> records = data.getRoleRecords().get(player.getId());
        if(records == null){
            records = new ArrayList<>();
            data.getRoleRecords().put(player.getId(),records);
        }
        if(records.size() >= CN_LUCKY_DRAW_RECORD_MAX){
            records.remove(0);
        }
        records.add(record);

        //4.2.2: 获得二等奖以上,日志添加到全局列表中
        if(drawType <= 2) {
            records = data.getServerRecords();

            if (records.size() >= CN_LUCKY_DRAW_RECORD_MAX) {
                records.remove(0);
            }
            records.add(record);
        }

        //5.1开始发奖
        List<Item> itemList = Item.clone(items);
        Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.LuckyDrawWeekGet, IDConfigUtil.getLogId());

        //5.2.设置玩家打开当前Panel的标记--防止断线重连被删除掉
        data.getPlayerOpenPanels().put(player.getId(),true);

        //6.同步给用户,抽奖记录
        //6.1 发送抽奖结果
        SendLuckyDrawReturnCode(player,1,drawType,idx);

        //6.2 分发抽奖记录,当奖励是二等奖以上的话,就群发,如果不是则只发给自己
        LuckyDrawMessage.ResDrawnRecord.Builder rdr = LuckyDrawMessage.ResDrawnRecord.newBuilder();
        rdr.setRecord(newMsgRecordFrom(record));

        if(drawType <= 2) {
            //6.2.1:获得二等奖以上
            for (Map.Entry<Long, Boolean> e : data.getPlayerOpenPanels().entrySet()) {
                if (e.getValue()) {
                    if (Manager.playerManager.isOnline(e.getKey())) {
                        MessageUtils.send_to_player(e.getKey(), LuckyDrawMessage.ResDrawnRecord.MsgID.eMsgID_VALUE, rdr.build().toByteArray());
                    } else {//如果不在就修改他的值
                        e.setValue(false);
                    }
                }
            }
        }else{
            //6.2.2:获得三等奖
            MessageUtils.send_to_player(player, LuckyDrawMessage.ResDrawnRecord.MsgID.eMsgID_VALUE, rdr.build().toByteArray());
        }
        //记录BI数据
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.LuckyDrawWeekGet, BIActiityTypeEnum.LUCKY_DRAW.getId(), idx);
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.ZFL_DRAW, ItemChangeReason.LuckyDrawWeekGet, drawType);
    }


    /**
     * 获取奖卷信息
     * @param player
     * @param msg
     */
    @Override
    public void onReqGetLuckyDrawVolumeHandler(Player player, LuckyDrawMessage.ReqGetLuckyDrawVolume msg) {

        //0.判断角色身上是否有奖卷领取的记录
        if(player.getLuckyDrawWeekAwardVolumes().isEmpty()){
            initPlayerData(player);
        }

        //1.判断是否已经领取过了
        Boolean isGet = player.getLuckyDrawWeekAwardVolumes().get(msg.getId());
        if(isGet != null && isGet){
            logger.error("玩家已经领取了,不能重复领取.ID: ID=" + msg.getId() + " PlayerID:" + player.getId());
            MessageUtils.notify_player(player,Notify.ERROR,MessageString.JUSTDOIT_REWARD_OVER);
            return;
        }

        Cfg_Week_welfare_Bean bean = Cfg_Week_welfare_Container.GetInstance().getValueByKey(msg.getId());
        //2.请求消息的ID是否存在配置
        if(bean == null){
            logger.error("没有找到领取奖卷对应的条件ID: ID=" + msg.getId() + " PlayerID:" + player.getId());
            MessageUtils.notify_player(player,Notify.ERROR,MessageString.ActivityConditionGetFailed);
            return;
        }

        //3.判断领取条件的进度是否完成
        if(!Manager.controlManager.deal().checkFuncProgress(player, bean.getCondition())){
            logger.error("当前条件不满足,不能够领取!ID: ID=" + msg.getId() + " PlayerID:" + player.getId());
            MessageUtils.notify_player(player,Notify.ERROR,MessageString.ActivityConditionGetFailed);
            return;
        }

        //4.开始处理奖品
        List<Item> items = Item.createItems(bean.getReward(),1);
        //4.1 判断在配置中是否找到奖品
        if(items == null || items.size() == 0){
            logger.error("配置表错误,没有找到奖励物品!ID: ID=" + msg.getId() + " PlayerID:" + player.getId());
            MessageUtils.notify_player(player,Notify.ERROR,MessageString.ActivityConditionGetFailed);
            return;
        }

        //4.2查看背包空间是否充足
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
            logger.error("背包满了!ID: ID=" + items.get(0).getItemModelId() + " PlayerID:" + player.getId());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }

        //5.设置标记
        player.getLuckyDrawWeekAwardVolumes().put(msg.getId(),true);

        //6.开始发奖
        List<Item> itemList = Item.clone(items);
        Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.LuckyDrawWeekGet, IDConfigUtil.getLogId());

        //7.给玩家发消息
        LuckyDrawMessage.getDrawVolumeInfo.Builder gdvi = LuckyDrawMessage.getDrawVolumeInfo.newBuilder();
        gdvi.setIsGet(true);
        gdvi.setId(msg.getId());
        gdvi.setMaxCount(bean.getCondition().get(bean.getCondition().size()-1));
        gdvi.setProgress(Manager.controlManager.deal().getFuncProgress(player,bean.getCondition()));
        LuckyDrawMessage.ResGetLuckyDrawVolumeResult.Builder result = LuckyDrawMessage.ResGetLuckyDrawVolumeResult.newBuilder();
        result.setGetVolume(gdvi);

        MessageUtils.send_to_player(player.getId(), LuckyDrawMessage.ResGetLuckyDrawVolumeResult.MsgID.eMsgID_VALUE,result.build().toByteArray());

        Manager.biManager.getInstance().getScript().biActivity(player, BIActiityTypeEnum.ZFL_privilege, ItemChangeReason.LuckyDrawWeekGet);
    }

    /**
     * 改变奖励索引信息
     * @param player
     * @param msg
     */
    @Override
    public void onReqChangeAwardIndexHandler(Player player, LuckyDrawMessage.ReqChangeAwardIndex msg) {

        if(!checkLuckDrawValid()){
            initServerData();
        }
        LuckyDrawWeekData wd = Manager.luckyDrawManager.getWeekData();

        //1.根据消息请求,把奖励信息整理到一个临时的缓存中.
        HashMap<Integer,List<Integer>> temp = new HashMap<>();
        int level = player.getLevel();
        int errCode = 1;
        for (int i = 0; i <msg.getItemsCount() ; i++) {
            LuckyDrawMessage.awardIndexInfo item = msg.getItems(i);
            Cfg_Week_welfare_reward_Bean bean = GetRewardBean(item.getAwardType());
            if(bean == null){
                //请求的奖励等级无效
                errCode = -1;
                break;
            }
            List<Integer> reqList = item.getIndexesList();
            List<Integer> targetList = new ArrayList<>();;

            for (int j = 0; j < bean.getNum() ; j++) {

                //1.1请求的数据中,替换索引的长度不对.
                if(j > reqList.size()){
                    errCode = -2;
                    break;
                }

                ReadArray<Integer> tmpItem = getRewardItemCfg(bean,reqList.get(j));
                //1.2请求的数据中,有一个索引找不到奖励物品
                if(item == null){
                    errCode = -3;
                    break;
                }

                //1.3请求的数据等级大于角色等级
                if(tmpItem.get(tmpItem.size()-1) > level){
                    errCode = -4;
                    break;
                }

                targetList.add(reqList.get(j));
            }
            if(errCode >= 0) {
                temp.put(item.getAwardType(),targetList);
            }else{
                break;
            }
        }

        //2.设置标记, 根据处理结果,把缓存中的数据替换到角色数据中.
        if(errCode >= 0){
            for (Integer key: temp.keySet()) {
                player.getLuckyDrawWeekCustomAwards().put(key,temp.get(key));
            }
        }

        //3.发送到服务器
        LuckyDrawMessage.ResChangeAwardIndexResult.Builder b = LuckyDrawMessage.ResChangeAwardIndexResult.newBuilder();
        for (Map.Entry<Integer,List<Integer>> e: wd.getAwards().entrySet()) {
            LuckyDrawMessage.awardIndexInfo.Builder ai =LuckyDrawMessage.awardIndexInfo.newBuilder();
            ai.setAwardType(e.getKey());
            List<Integer> awardIndexes =e.getValue();
            List<Integer> playerIndex = player.getLuckyDrawWeekCustomAwards().get(e.getKey());
            //如果角色身上有奖池,那么就使用角色身上的
            if(playerIndex != null) {
                awardIndexes = playerIndex;
            }
            for (int i = 0; i < awardIndexes.size(); i++) {
                ai.addIndexes(awardIndexes.get(i));
            }
            b.addItems(ai);
        }
        b.setRetcode(errCode);
        MessageUtils.send_to_player(player.getId(),LuckyDrawMessage.ResChangeAwardIndexResult.MsgID.eMsgID_VALUE,b.build().toByteArray());
    }

    /**
     * 玩家关闭面板时的请求
     * @param player
     * @param msg
     */
    @Override
    public void onReqCloseLuckyDrawPanelHandler(Player player,LuckyDrawMessage.ReqCloseLuckyDrawPanel msg){
        //正常关闭设置为false
        LuckyDrawWeekData data =  Manager.luckyDrawManager.getWeekData();
        if(data.getPlayerOpenPanels().containsKey(player.getId())) {
            data.getPlayerOpenPanels().put(player.getId(), false);
        }
    }

    /**
     * 玩家进入游戏的操作
     * @param player
     */
    @Override
    public void onPlayerOnline(Player player) {
        if(!EntityState.ReConnect.compare(player.getState())){
            //如果不是重连的情况,就设置为false,
            LuckyDrawWeekData data =  Manager.luckyDrawManager.getWeekData();
            if(data.getPlayerOpenPanels().containsKey(player.getId())) {
                data.getPlayerOpenPanels().put(player.getId(), false);
            }
        }

        //如果上线的天数与下线的天数不同,重新初始化一下角色数据
        if( TimeUtils.getCurDayByTime(player.getOffLineTime()) != TimeUtils.getCurDayByTime(TimeUtils.Time())){
            initPlayerData(player);
        }
        sendPlayerLuckyDrawData(player);
    }

    @Override
    public void zeroClockHandler(Player player) {
        initPlayerData(player);
        sendPlayerLuckyDrawData(player);
    }


    //region 脚本内部私有函数处理

    /**
     * 发送给玩家幸运抽奖数据
     * @param player
     */
    public void sendPlayerLuckyDrawData(Player player) {
        //0.判断数据,并进行初始化
        if(!checkLuckDrawValid()){
            initServerData();
        }
        LuckyDrawWeekData wd = Manager.luckyDrawManager.getWeekData();
        LuckyDrawMessage.ResLuckyDrawOnlineResult.Builder res = LuckyDrawMessage.ResLuckyDrawOnlineResult.newBuilder();

        //1.填充已经获取的奖励次数
        res.setAwardtimes(player.getLuckyDrawWeekTimes());

        //2.对奖池进行处理
        for (Map.Entry<Integer,List<Integer>> e: wd.getAwards().entrySet()) {
            LuckyDrawMessage.awardIndexInfo.Builder ai =LuckyDrawMessage.awardIndexInfo.newBuilder();
            ai.setAwardType(e.getKey());
            List<Integer> awardIndexes = e.getValue();
            List<Integer> playerIndex = player.getLuckyDrawWeekCustomAwards().get(e.getKey());
            //如果角色身上有奖池,那么就使用角色身上的
            if(playerIndex != null) {
                awardIndexes = playerIndex;
            }
            for (int i = 0; i < awardIndexes.size(); i++) {
                ai.addIndexes(awardIndexes.get(i));
            }
            res.addItems(ai);
        }

        //3.是否领取抽奖卷
        //3.1判断角色身上是否有奖卷记录
        if(player.getLuckyDrawWeekAwardVolumes().isEmpty()){
            initPlayerData(player);
        }
        //3.2填充奖卷领取情况的记录
        for(Integer id : player.getLuckyDrawWeekAwardVolumes().keySet()){
            Cfg_Week_welfare_Bean bean = Cfg_Week_welfare_Container.GetInstance().getValueByKey(id);
            LuckyDrawMessage.getDrawVolumeInfo.Builder dv = LuckyDrawMessage.getDrawVolumeInfo.newBuilder();
            dv.setId(id);
            dv.setMaxCount(bean.getCondition().get(bean.getCondition().size()-1));
            dv.setProgress(Manager.controlManager.deal().getFuncProgress(player,bean.getCondition()));
            dv.setIsGet(player.getLuckyDrawWeekAwardVolumes().get(id));
            res.addGetVolumes(dv);
        }

        //4.处理抽奖记录
        List<LuckyDrawRecord> roleRecords =  wd.getRoleRecords().get(player.getId());
        List<LuckyDrawRecord> serverRecords =  wd.getServerRecords();

        //4.1 处理自己的抽奖记录
        if(roleRecords != null) {
            for (LuckyDrawRecord r1 : roleRecords) {
                res.addSelfRecords(newMsgRecordFrom(r1));
            }
        }

        //4.2 处理全服务器的抽奖记录
        if(serverRecords != null) {
            for (LuckyDrawRecord r : serverRecords) {
                res.addRecords(newMsgRecordFrom(r));
            }
        }

        MessageUtils.send_to_player(player,LuckyDrawMessage.ResLuckyDrawOnlineResult.MsgID.eMsgID_VALUE,res.build().toByteArray());
    }



    /**
     * 判断数据是否有效
     * @return
     */
    private boolean checkLuckDrawValid(){
        LuckyDrawWeekData data =  Manager.luckyDrawManager.getWeekData();
        if(data.getAwards().isEmpty()){
            logger.error("当前奖池数据为空,需要重新初始化配置!");
            return false;
        }
        return true;
    }

    /**
     * 初始化服务器数据
     */
    private void initServerData(){
        ConcurrentHashMap<Integer, List<Integer>> awards = Manager.luckyDrawManager.getWeekData().getAwards();
        awards.clear();
        //1.初始化奖池信息.
        //1.1,扫描奖励表的信息
        for (Cfg_Week_welfare_reward_Bean bean : Cfg_Week_welfare_reward_Container.GetInstance().getValuees()) {
            //1.2从奖励列表中获取num个奖励物品,用于当作默认奖励.
            List<Integer> indexes = getDefaults(bean.getRewardPool());
            if (indexes.size() < bean.getNum()) {
                logger.error("week_welfare_reward表错误,奖池中设置的默认奖励数量小于定义的奖励数量:Num:" + bean.getNum() + "  indexes.size():" + indexes.size() + "  id:" + bean.getId());
            }
            awards.put(bean.getType(), indexes);
        }

    }



    /**
     * 初始化角色数据
     * @param player
     */
    private void initPlayerData(Player player){
        HashMap<Integer,Boolean> av = player.getLuckyDrawWeekAwardVolumes();
        av.clear();
        //2.初始化领奖卷的条件信息
        for (Cfg_Week_welfare_Bean bean : Cfg_Week_welfare_Container.GetInstance().getValuees()) {
            av.put(bean.getId(), false);
        }
    }

    /**
     * 获取当前默认的奖励物品
     * @param arr
     * @return
     */
    private List<Integer> getDefaults(ReadIntegerArrayEs arr){
        List<Integer> indexes = new ArrayList<Integer>();
        for(int i = 0;i < arr.size();i++){
            ReadArray<Integer> item = arr.get(i);
            if(item.get(item.size()-1) == 0){
                indexes.add(i);
            }
        }
        return indexes;
    }

    /**
     * 获取奖励等级
     * @return
     */
    private Integer GetLuckyDrawType(boolean isLucky){
        Integer total = 0;
        for (Cfg_Week_welfare_reward_Bean bean : Cfg_Week_welfare_reward_Container.GetInstance().getValuees()) {
            total += bean.getProbability();
        }
        Integer randNum = RandomUtils.random(0,total);
        Integer drawType = -1;
        for (Cfg_Week_welfare_reward_Bean bean : Cfg_Week_welfare_reward_Container.GetInstance().getValuees()) {
            randNum -= (isLucky?bean.getLuckProbability():bean.getProbability());
            if(randNum <= 0){
                drawType = bean.getType();
                break;
            }
        }
        if(drawType < 0){
            logger.error("抽奖失败,没有获得抽奖信息: randomNum:" + randNum + " ;;total:" + total + " drawType:" + drawType);
            drawType = 3;
        }
        return drawType;
    }

    /**
     * 获取奖励的随机索引
     * @param player
     * @param drawType
     * @return
     */
    private Integer GetLuckyDrawRewardIndex(Player player,int drawType){
        List<Integer> rewards = player.getLuckyDrawWeekCustomAwards().get(drawType);
        if(rewards == null || rewards.isEmpty()){
            LuckyDrawWeekData wd = Manager.luckyDrawManager.getWeekData();
            rewards = wd.getAwards().get(drawType);
            if(rewards.isEmpty()){
                initServerData();
                rewards = wd.getAwards().get(drawType);
            }
        }
        if(rewards.isEmpty()){
            logger.error("奖池数据为空,请检查配置文件是否正确:type:"+drawType);
            return 0;
        }
        return rewards.get(RandomUtils.random(0, rewards.size()-1));
    }

    /**
     * 发送抽奖结果
     * @param player
     * @param retCode
     */
    private void SendLuckyDrawReturnCode(Player player,Integer retCode,Integer awardType,Integer index){
        LuckyDrawMessage.ResLuckyDrawResult.Builder result = LuckyDrawMessage.ResLuckyDrawResult.newBuilder();
        result.setRetcode(retCode);
        result.setAwardType(awardType);
        result.setAwardIndex(index);
        MessageUtils.send_to_player(player.getId(), LuckyDrawMessage.ResLuckyDrawResult.MsgID.eMsgID_VALUE,result.build().toByteArray());
    }

    /**
     * 根据奖励等级获取奖励信息配置
     * @param drawType
     * @return
     */
    private Cfg_Week_welfare_reward_Bean GetRewardBean(Integer drawType){
        for (Cfg_Week_welfare_reward_Bean bean : Cfg_Week_welfare_reward_Container.GetInstance().getValuees()) {
            if(bean.getType() == drawType){
                return bean;
            }
        }
        return null;
    }

    /**
     * 判断奖励是否有效
     * @param index
     * @return
     */
    private ReadArray<Integer> getRewardItemCfg(Cfg_Week_welfare_reward_Bean bean, Integer index){
        return bean.getRewardPool().get(index);
    }

    /**
     * 判断今天是否是幸运抽奖日
     * @return
     */
    private boolean checkLuckyDrawDay(){
        int day = TimeUtils.getDayOfWeek(TimeUtils.Time());
        for (int i = 0; i < Global.Week_Welfare_Draw_Limit.size() ; i++) {
            if(day == Global.Week_Welfare_Draw_Limit.get(i)){
                return true;
            }
        }
        return false;
    }

    /**
     * 把LuckyDrawRecord转换为awardRecordInfo
     * @param r
     * @return
     */
    private LuckyDrawMessage.awardRecordInfo.Builder newMsgRecordFrom(LuckyDrawRecord r){
        LuckyDrawMessage.awardRecordInfo.Builder ri = LuckyDrawMessage.awardRecordInfo.newBuilder();
        ri.setAwardType(r.getAwardType());
        ri.setPlayername(r.getRoleName());
        ri.setItemId(r.getItemModelID());
        ri.setItemNum(r.getNum());
        ri.setBind(r.isBind());
        return ri;
    }

    //endregion
}