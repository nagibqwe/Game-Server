package common.crossrank;

import com.data.MessageString;
import com.game.crossrank.manager.CrossRankManager;
import com.game.crossrank.scripts.ICrossRank;
import com.game.crossrank.structs.CrossRankFightSort;
import com.game.crossrank.structs.CrossRankLevelSort;
import com.game.db.bean.CrossRankBean;
import com.game.gameserver.manager.GameServerManager;
import com.game.gameserver.structs.ServerInfo;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.servermatch.structs.GameServerInfo;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import game.message.CommonMessage;
import game.message.CrossFightMessage;
import game.message.CrossRankMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cxl on 2020/4/9.
 */
public class CrossRankScript implements ICrossRank {

    private static final Logger logger = LogManager.getLogger(CrossRankScript.class);

    @Override
    public int getId() {
        return ScriptEnum.CrossRankScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    private Object obj = new Object();





   public void crossRankSort(){
       int sublen = 0;
       synchronized(obj) {
           ConcurrentHashMap<Long, CrossRankBean> allLevelRankMap = Manager.crossRankManager.getAllLevelCrossRankBeanMap();
           ConcurrentHashMap<Long, CrossRankBean> allFightRankMap = Manager.crossRankManager.getAllFightCrossRankBeanMap();

           ConcurrentHashMap<Integer, ConcurrentHashMap<Long, CrossRankBean>> serverIdKeyCrossRankMap
                   =  Manager.crossRankManager.getServerIdKeyCrossRankMap();

           if (serverIdKeyCrossRankMap.size() <= 0)
               return;
           allLevelRankMap.clear();
           allFightRankMap.clear();
           for (Integer serverid : serverIdKeyCrossRankMap.keySet()){
               ConcurrentHashMap<Long, CrossRankBean> serverCrossRankData =   serverIdKeyCrossRankMap.get(serverid);
               if (serverCrossRankData.size()<=0)
                   continue;
               List<CrossRankBean> serverCrossRankList = new ArrayList<>(serverCrossRankData.values());
               serverCrossRankList.sort(new CrossRankFightSort());
               sublen = serverCrossRankList.size() >= CrossRankManager.SERVER_MAX ? CrossRankManager.SERVER_MAX : serverCrossRankList.size();
               int i;
               for (i = 0; i < sublen ; i++){
                   CrossRankBean crossRankBean =  serverCrossRankList.get(i);
                   allFightRankMap.put(crossRankBean.getRoleId(),crossRankBean);
               }
               serverCrossRankList.sort(new CrossRankLevelSort());
               sublen = serverCrossRankList.size() >= CrossRankManager.SERVER_MAX ? CrossRankManager.SERVER_MAX : serverCrossRankList.size();
               for (i = 0; i < sublen ; i++){
                   CrossRankBean crossRankBean =  serverCrossRankList.get(i);
                   allLevelRankMap.put(crossRankBean.getRoleId(),crossRankBean);
               }
           }

           //战力排行
           List<CrossRankBean> crossFightRankList = new ArrayList<>(allFightRankMap.values());
           crossFightRankList.sort(new CrossRankFightSort());
           sublen = crossFightRankList.size() >= CrossRankManager.RANK_MAX ? CrossRankManager.RANK_MAX : crossFightRankList.size();
           crossFightRankList =  crossFightRankList.subList(0, sublen);

           //等级排行
           List<CrossRankBean> crossLevelRankList = new ArrayList<>(allLevelRankMap.values());
           crossLevelRankList.sort(new CrossRankLevelSort());
           sublen = crossLevelRankList.size() >= CrossRankManager.RANK_MAX ? CrossRankManager.RANK_MAX : crossLevelRankList.size();
           crossLevelRankList = crossLevelRankList.subList(0, sublen);


           //从新整合 --只保留前50，后面的踢出
           ConcurrentHashMap<Long, CrossRankBean> allCrosRankMap = Manager.crossRankManager.getAllCrosRankMap();
           allCrosRankMap.clear();
           for (CrossRankBean bean : crossFightRankList) {
               allCrosRankMap.put(bean.getRoleId(),bean);
           }
           for (CrossRankBean bean : crossLevelRankList) {
                if (allCrosRankMap.containsKey(bean.getRoleId()))
                    continue;
               allCrosRankMap.put(bean.getRoleId(),bean);
           }
       }
   }

   public void updateSql() {
       ConcurrentHashMap<Long, CrossRankBean> allCrosRankMap  =Manager.crossRankManager.getAllCrosRankMap();
       if (allCrosRankMap.size()<=0)
           return;
       for (CrossRankBean crossRankBean : allCrosRankMap.values()){
           CrossRankBean bean = Manager.crossRankManager.getDao().selectByRoleId(crossRankBean.getRoleId());
           if (bean != null) {
               Manager.crossRankManager.getDao().update(crossRankBean);
           } else {
               Manager.crossRankManager.getDao().insert(crossRankBean);
           }
       }
   }


    public  void onReqG2PSyncCrossRankInfo(ChannelHandlerContext context,CrossRankMessage.ReqG2PSyncCrossRankInfo messInfo){

        synchronized(obj) {
            int serverID = 0;
            ConcurrentHashMap<Long, CrossRankBean> serverCrossData;
            CrossRankBean crossRankBean = null;
            for (CrossRankMessage.CrossRankInfo crossRankInfo : messInfo.getSyncRankInfoListList()) {
                serverID = crossRankInfo.getServerId();
                if ( Manager.crossRankManager.getServerIdKeyCrossRankMap().containsKey(serverID)){
                    serverCrossData = Manager.crossRankManager.getServerIdKeyCrossRankMap().get(serverID);
                    if (serverCrossData.containsKey(crossRankInfo.getRoleId())){
                        crossRankBean =  serverCrossData.get(crossRankInfo.getRoleId());
                    }else {
                        crossRankBean  = new CrossRankBean();
                        serverCrossData.put(crossRankInfo.getRoleId(),crossRankBean);
                    }
                }else {
                    serverCrossData = new ConcurrentHashMap<>();
                    crossRankBean  = new CrossRankBean();
                    Manager.crossRankManager.getServerIdKeyCrossRankMap().put(serverID,serverCrossData);
                    serverCrossData.put(crossRankInfo.getRoleId(), crossRankBean);
                }
                crossRankBean.setRoleId(crossRankInfo.getRoleId());
                crossRankBean.setRoleName(crossRankInfo.getRoleName());
                crossRankBean.setServerId(crossRankInfo.getServerId());
                crossRankBean.setCareer(crossRankInfo.getCareer());
                crossRankBean.setStateVip(crossRankInfo.getStateVip());
                crossRankBean.setLevel(crossRankInfo.getLevel());
                crossRankBean.setFightPower(crossRankInfo.getFightPower());
                crossRankBean.setFashionBodyId( crossRankInfo.getFacade().getFashionBody());
                crossRankBean.setFashionWeaponId(crossRankInfo.getFacade().getFashionWeapon());
                crossRankBean.setWingModel(crossRankInfo.getFacade().getWingId());
                crossRankBean.setFashionHalo(crossRankInfo.getFacade().getFashionHalo());
                crossRankBean.setFashionMatrix(crossRankInfo.getFacade().getFashionMatrix());
                crossRankBean.setSpiritId(crossRankInfo.getFacade().getSpiritId());
            }
            crossRankSort();
        }
    }

    public void onReqG2PCrossRankInfo(ChannelHandlerContext context,CrossRankMessage.ReqG2PCrossRankInfo messInfo)
    {
        long roleId =  messInfo.getRoleId();
        String platSid = context.channel().attr(SessionKey.SERVERPLATID).get();
        List<Integer> rankServerList = ServerMatchManager.deal().getCrossRankServerlist(platSid);

        logger.info("rankServerList   {}   platSid  ", rankServerList ,platSid);
        if (rankServerList == null || rankServerList.size() <=0){
            MessageUtils.notify_player(context, messInfo.getRoleId(), MessageString.CROSS_RANK_FAILD_TITLE);
            logger.error("rankServerList  is null");
            return;
        }
        List<CrossRankBean> crossFightRankList = getReachMatchRankBean(rankServerList,Manager.crossRankManager.getAllFightCrossRankBeanMap());
        if (crossFightRankList == null){
            logger.error("crossFightRankList  is null");
            return;
        }

        crossFightRankList.sort(new CrossRankFightSort());
        int sublen = crossFightRankList.size() >= CrossRankManager.RANK_MAX ? CrossRankManager.RANK_MAX : crossFightRankList.size();
        crossFightRankList =  crossFightRankList.subList(0, sublen);

        List<CrossRankBean> crossLevelRankList =  getReachMatchRankBean(rankServerList,Manager.crossRankManager.getAllLevelCrossRankBeanMap());
        if (crossFightRankList == null){
            logger.error("crossFightRankList  is null");
            return;
        }
        crossLevelRankList.sort(new CrossRankLevelSort());
        sublen = crossLevelRankList.size() >= CrossRankManager.RANK_MAX ? CrossRankManager.RANK_MAX : crossLevelRankList.size();
        crossLevelRankList =  crossLevelRankList.subList(0, sublen);

        CrossRankMessage.ResCrossRankInfo.Builder msg =  CrossRankMessage.ResCrossRankInfo.newBuilder();
        msg.addCrossTypeRankList( getCrossTypeRankInfo(CrossRankManager.LEVEL_RANK,crossLevelRankList));
        msg.addCrossTypeRankList( getCrossTypeRankInfo(CrossRankManager.FIGHT_RANK,crossFightRankList));
        MessageUtils.send_to_player(context, roleId, CrossRankMessage.ResCrossRankInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private CrossRankMessage.CrossTypeRankInfo getCrossTypeRankInfo(int type, List<CrossRankBean> ranklist){
        CrossRankMessage.CrossTypeRankInfo.Builder crossTypeRankInfo =  CrossRankMessage.CrossTypeRankInfo.newBuilder();
        crossTypeRankInfo.setType(type);
        CrossRankMessage.CrossRankInfo.Builder crossRankInfo = null;
        CommonMessage.FacadeAttribute.Builder facade = null;
        int rank = 1;
        for (CrossRankBean crossRankBean :ranklist){
            crossRankInfo =  CrossRankMessage.CrossRankInfo.newBuilder();
            facade =  CommonMessage.FacadeAttribute.newBuilder();
            crossRankInfo.setRoleId(crossRankBean.getRoleId());
            crossRankInfo.setRank(rank);
            crossRankInfo.setRoleName(crossRankBean.getRoleName());
            crossRankInfo.setServerId(crossRankBean.getServerId());
            crossRankInfo.setCareer(crossRankBean.getCareer());
            crossRankInfo.setStateVip(crossRankBean.getStateVip());
            crossRankInfo.setLevel(crossRankBean.getLevel());
            crossRankInfo.setFightPower(crossRankBean.getFightPower());
            facade.setFashionBody(crossRankBean.getFashionBodyId());
            facade.setFashionWeapon(crossRankBean.getFashionWeaponId());
            String rankdata = type == CrossRankManager.LEVEL_RANK?crossRankBean.getLevel()+"": crossRankBean.getFightPower() + "";
            crossRankInfo.setRankData(rankdata);
            facade.setWingId(crossRankBean.getWingModel());
            facade.setFashionHalo(crossRankBean.getFashionHalo());
            facade.setFashionMatrix(crossRankBean.getFashionMatrix());
            facade.setSpiritId(crossRankBean.getSpiritId());
            crossRankInfo.setFacade(facade.build());
            crossTypeRankInfo.addCrossRankList(crossRankInfo.build());
            rank++;

        }
        return crossTypeRankInfo.build();
    }


    private List<CrossRankBean> getReachMatchRankBean(List<Integer> rankMatchServerlist, ConcurrentHashMap<Long, CrossRankBean> allCrosRankMap){
        if (allCrosRankMap.size()<=0){
            logger.error("allCrosRankMap  size  == 0");
            return null;
        }
        List<CrossRankBean> allMatchRankInfo  = new ArrayList<>();
        for (CrossRankBean bean : allCrosRankMap.values()){
                if (rankMatchServerlist.contains(bean.getServerId())){
                    if (bean.getLevel() < 110){ //小于 110级不进入排行
                        continue;
                    }
                    allMatchRankInfo.add(bean);
                }
        }
        return allMatchRankInfo;
    }

    /**
     * 计算跨服世界等级
     */
    @Override
    public void calcAllCrossWorldLv() {

        logger.info("开始计算跨服世界等级");
        List<ServerInfo> list =  GameServerManager.getInstance().getAllGameServer();
        if (list == null){
            logger.info("allgameserver len  0");
            return;
        }
        for (ServerInfo serverInfo : list){
            calcSingleServerWorldLv(serverInfo);
        }
    }

    /**
     * 计算单个服务器
     */
    @Override
    public void calcSingleServerWorldLv(ServerInfo serverInfo){
        String serverkey="";
        int sublen = 0;
        serverkey =  serverInfo.getPlatName()  + "_" + serverInfo.getServerId();
        List<Integer> rankServerList = ServerMatchManager.deal().getCrossRankServerlist(serverkey);
        if (rankServerList == null || rankServerList.size() <=0){
            return;
        }
        List<CrossRankBean> crossLevelRankList =  getReachMatchRankBean(rankServerList,Manager.crossRankManager.getAllLevelCrossRankBeanMap());
        if (crossLevelRankList == null || crossLevelRankList.size() <=0)
            return;

        crossLevelRankList.sort(new CrossRankLevelSort());
        sublen = crossLevelRankList.size() >= CrossRankManager.MAX_CROSS_LEVEL ? CrossRankManager.MAX_CROSS_LEVEL : crossLevelRankList.size();
        crossLevelRankList =  crossLevelRankList.subList(0, sublen);

        int allLevel = 0;
        for (CrossRankBean crossRankBean :crossLevelRankList){
            allLevel +=crossRankBean.getLevel();
        }
        int averageLevel = (int) Math.ceil(allLevel /sublen);
        if (averageLevel <= serverInfo.getServerWorldLv()){
            return;
        }
        serverInfo.setServerWorldLv(averageLevel);
        GameServerInfo info = ServerMatchManager.infos.get(serverkey);
        if (info != null) {
            info.setServerWorldLv(averageLevel);
        }
        CrossRankMessage.P2GCrossWorldLv.Builder msg =  CrossRankMessage.P2GCrossWorldLv.newBuilder();
        msg.setCrossWorldLv(averageLevel);
        MessageUtils.send_to_game(serverInfo.getSession(), CrossRankMessage.P2GCrossWorldLv.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        logger.info("calcCrossWorldLv serverid {} crosslevel {} " ,serverInfo.getServerId(),averageLevel);
    }


    /**
     * 合服后跨服排行榜修复
     * @param serverInfo
     */
    public void onRepairCrossRank(ServerInfo serverInfo){

        for (Integer sid : serverInfo.getSids()){
            if (sid == serverInfo.getServerId()){
                continue;
            }
            //删除被合服的排行榜数据
            if (Manager.crossRankManager.getServerIdKeyCrossRankMap().containsKey(sid)){
                Manager.crossRankManager.getServerIdKeyCrossRankMap().remove(sid);
            }
        }
    }
}
