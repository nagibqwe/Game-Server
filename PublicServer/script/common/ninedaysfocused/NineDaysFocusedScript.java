package common.ninedaysfocused;

import com.data.CfgManager;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_map_Bean;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightroom.structs.FightRoom;
import com.game.gameserver.manager.GameServerManager;
import com.game.manager.Manager;
import com.game.ninedaysfocused.script.INineDaysFocused;
import com.game.ninedaysfocused.structs.NineDaysCrossPlayer;
import com.game.ninedaysfocused.structs.NineDaysQueuer;
import com.game.ninedaysfocused.structs.NineDaysSort;
import com.game.script.ScriptEnum;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import com.game.zone.structs.TeamPlayerInfo;
import com.game.zone.structs.ZoneTeam;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.NineDaysFocusedMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 542 on 2019/7/22.
 */
public class NineDaysFocusedScript implements INineDaysFocused {


    private static final Logger log = LogManager.getLogger(NineDaysFocusedScript.class);

    private int waitTime = 1000;//一秒
    private long lastChcekTime = 0;//上次检测时间
    private long lastChcekTime_1 = 0;//检测开放时间
    private int checkOpenRoomTime  = 300;//5分钟检测一次是否需要开房间
    private int RedCamp = 13;
    private int BlueCamp = 14;
    private int roomNeedPlayerNum = 20;


    @Override
    public int getId() {
        return ScriptEnum.NineDaysFocusedScript;
    }
    @Override
    public Object call(Object... objects) {
        return null;
    }



    private void init(){

       // lastChcekTime_1 = TimeUtils.Time();
       // Manager.nineDaysFocusedManager.setOpen(true);
       // for (Integer groupid: ServerMatchManager.serverGroupIDList.values()){
//
       //     if ( !Manager.nineDaysFocusedManager.getPlayerMatchList().containsKey(groupid))
       //     {
       //         ConcurrentHashMap<Long, NineDaysCrossPlayer> list = new ConcurrentHashMap<>();
       //         Manager.nineDaysFocusedManager.getPlayerMatchList().put(groupid,list);
//
       //         ConcurrentHashMap<Long, NineDaysQueuer> list_1 = new ConcurrentHashMap<>();
       //         Manager.nineDaysFocusedManager.getReadyqueue().put(groupid,list_1);
       //         Manager.nineDaysFocusedManager.getP2pqueue().put(groupid,list_1);
       //     }
       // }
    }

    @Override
    public void onStart(){
        init();
    }

    @Override
    public void onOver(){

        Manager.nineDaysFocusedManager.setOpen(false);
    }

    /**
     * 报名匹配
     */
    @Override
    public void onG2PMatchEnterMap(ChannelHandlerContext context, NineDaysFocusedMessage.G2PReqApplyNieDaysFocused messInfo){

        String platServerId = context.channel().attr(SessionKey.SERVERPLATID).get();


        int serverGroupId =  ServerMatchManager.deal().getGroupIDForCurOpenDay(platServerId,DailyActiveDefine.NINE_DAYS_FOCUSED);
        if (serverGroupId <0)
        {
            MessageUtils.notify_player(context,messInfo.getHero().getRoleId(), MessageString.ServerMachtFail);
            log.info("未分配到游戏服配对，基础世界等级未达到  " + platServerId);
            return;
        }
        int isCanPlay = ServerMatchManager.deal().getGroupIDForCurOpenDay(platServerId,DailyActiveDefine.NINE_DAYS_FOCUSED);
        if (isCanPlay < 0)
        {
            log.info("世界等级未达到该活动要求的  平均等级");
            return;
        }
        ConcurrentHashMap<Long, NineDaysCrossPlayer> crossList = Manager.nineDaysFocusedManager.getPlayerMatchList().get(serverGroupId);
        ConcurrentHashMap<Long, NineDaysQueuer> queuerList = Manager.nineDaysFocusedManager.getReadyqueue().get(serverGroupId);
        if (queuerList.containsKey(messInfo.getHero().getRoleId())){
                log.info("已在报名列表中，报名失败");
                return;
        }
        NineDaysCrossPlayer hero = crossList.get(messInfo.getHero().getRoleId());
        if (hero == null) {
            hero = new NineDaysCrossPlayer();
            hero.setRoleID(messInfo.getHero().getRoleId());
            crossList.put(hero.getRoleID(), hero);
        }
        hero.setPlat(context.channel().attr(SessionKey.SERVERPLAT).get());
        hero.setSid(context.channel().attr(SessionKey.SERVERID).get());
        hero.setName(messInfo.getHero().getName());
        hero.setServerName(messInfo.getHero().getServerName());
        hero.setCareer(messInfo.getHero().getCareer());
        hero.setDegree(messInfo.getHero().getDegree());
        hero.setLv(messInfo.getHero().getLv());
        hero.setFightPoint(messInfo.getHero().getFightPoint());
        hero.setWeaponId(messInfo.getHero().getWeaponId());
        hero.setWingId(messInfo.getHero().getWingId());
        hero.setEquipMinStar(messInfo.getHero().getEquipMinStar());
        hero.setFashionBodyId(messInfo.getHero().getFashionBodyId());
        hero.setFashionWeaponId(messInfo.getHero().getFashionWeaponId());

        //TODO加入等待队列
        NineDaysQueuer queuer = new NineDaysQueuer();
        queuer.setRoleID(hero.getRoleID());
        queuer.setDegree(hero.getDegree());
        queuer.setQueuetime(TimeUtils.Time());
        queuerList.put(queuer.getRoleID(), queuer);
        log.error("玩家加入匹配队列：" + hero.getName() + "_" + hero.getRoleID());
    }

    /**
     * 取消报名匹配
     */
    @Override
    public void onG2PCancelMatchEnterMap(ChannelHandlerContext context)
    {

        //CrossjjcMessage.GS2PSCrossJJcCancelEnter messInfo = null;
        String platServerId = context.channel().attr(SessionKey.SERVERPLATID).get();
       // int serverGroupId =  ServerMatchManager.deal().getDefaultGroupID(platServerId);
       // if (serverGroupId <0)
       // {
       //     log.info("当前服务器 世界等级不足够 参加跨服活动  " + platServerId);
       //     return;
       // }
        //Manager.nineDaysFocusedManager.getReadyqueue().get(serverGroupId).remove(messInfo.getRoleId());
        //Manager.nineDaysFocusedManager.getP2pqueue().get(serverGroupId).remove(messInfo.getRoleId());
    }


    @Override
    public void tick(long curTime){

        if (!Manager.nineDaysFocusedManager.getOpen())
            return;

        if ((curTime  - lastChcekTime) < waitTime)
            return;
        Set<Integer> keys=Manager.nineDaysFocusedManager.getReadyqueue().keySet();
        Iterator<Integer> iterator1=keys.iterator();
        while (iterator1.hasNext()) {
            Integer groupID = iterator1.next();
            ConcurrentHashMap<Long, NineDaysQueuer> readyqueue = Manager.nineDaysFocusedManager.getReadyqueue().get(groupID);
            ConcurrentHashMap<Long, NineDaysQueuer> p2pqueue  = Manager.nineDaysFocusedManager.getP2pqueue().get(groupID);
            for (NineDaysQueuer ready : readyqueue.values()) {
                if (curTime - ready.getQueuetime() > 3000) {
                    //等待时间结束进入匹配队列
                    readyqueue.remove(ready.getRoleID());
                    p2pqueue.put(ready.getRoleID(), ready);
                }
            }

            if ( p2pqueue.size() >=20){
                int count = 0;
                ConcurrentHashMap<Long, NineDaysCrossPlayer> playerList =
                        Manager.nineDaysFocusedManager.getPlayerMatchList().get(groupID);
                List <NineDaysCrossPlayer> qlist = new ArrayList<>();
                for (NineDaysQueuer p : p2pqueue.values()) {
                    if (count >=20)
                    {
                        createRoom(qlist,groupID);
                        break;
                    }
                    NineDaysCrossPlayer data = playerList.get(p.getRoleID());
                    p2pqueue.remove(p.getRoleID());
                    count++;
                    if (data == null)
                        continue;
                    qlist.add(data);

                }
            }
            tick_2(curTime,p2pqueue,groupID);
        }

        lastChcekTime = curTime;
    }

    private void tick_2(long curTime, ConcurrentHashMap<Long, NineDaysQueuer> p2pqueue,int groupID){
        long waittime =  (curTime -  lastChcekTime_1)/1000;
        if (waittime <  checkOpenRoomTime )
            return;
        if (p2pqueue.size() >=5)
        {
            ConcurrentHashMap<Long, NineDaysCrossPlayer> playerList = Manager.nineDaysFocusedManager.getPlayerMatchList().get(groupID);
            List <NineDaysCrossPlayer> qlist = new ArrayList<>();
            for (NineDaysQueuer p : p2pqueue.values()) {
                NineDaysCrossPlayer data = playerList.get(p.getRoleID());
                p2pqueue.remove(p.getRoleID());
                if (data==null)
                    continue;
                qlist.add(data);
            }
            createRoom(qlist,groupID);
        }
        else
        {
            return;
        }
        lastChcekTime_1 =  curTime;
    }

    private void createRoom( List<NineDaysCrossPlayer> p2pqueue,int groupID)
    {
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(3000);
        Collections.sort(p2pqueue, new NineDaysSort());
        List<ZoneTeam> zts = new ArrayList<>();
        Map<Integer, ZoneTeam> map = new HashMap<>();
        int camp = 0;
        int count = 0;
        ZoneTeam zt = null;
        for (NineDaysCrossPlayer mt:p2pqueue){
            if (count%2 == 0)
                camp = RedCamp;
            else
                camp = BlueCamp;

           if (map.containsKey(mt.getSid())) {
               zt = map.get(mt.getSid());
           } else {
               zt = new ZoneTeam();
               zt.setPlat(mt.getPlat());
               zt.setsId(mt.getSid());
               zt.setBirthGroup(camp);
               zt.setCampNo(camp);
           }
            TeamPlayerInfo info = new TeamPlayerInfo();
            info.setRoleId(mt.getRoleID());
            info.setLv(mt.getLv());
            info.setCampNo(camp);
            info.setCareer(mt.getCareer());
            info.setName(mt.getName());
            info.setReady(true);
            info.setServerId(mt.getSid());
            zt.getPlist().put(mt.getRoleID(), info);
            map.put(mt.getSid(), zt);
            count++;
        }
        zts.addAll(map.values());

        if (count<roomNeedPlayerNum){
            zts.add(createRobotTeam(roomNeedPlayerNum- count, bean.getMin_lv() + 5,count));
        }

        //创建新的战场
        FightRoom newFr = null;
        try {
            newFr = Manager.fightManager.deal().createFightRoom(bean, zts);
            newFr.setServerGroupId(groupID);
            newFr.setAllReadyStart(true);
            Manager.fightManager.deal().fightStart(newFr, zts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //p2pqueue.sort();
    }


    private ZoneTeam createRobotTeam(int num,int lv,int count){
        ZoneTeam zt = new ZoneTeam();
        List<TeamPlayerInfo> tps = createRobot(num,lv,count);
        for(TeamPlayerInfo ps: tps){
            zt.getPlist().put(ps.getRoleId(), ps);
        }
        return zt;
    }

    //创建多少个机器人
    private List<TeamPlayerInfo> createRobot(int num,int lv,int count){
        List<TeamPlayerInfo> lists = new ArrayList<>();
        int[] career = new int[]{1,3,4,5};
        for(int i = 0;i<num;i++){

            int camp = count%2 == 0?RedCamp:BlueCamp;
            TeamPlayerInfo info = new TeamPlayerInfo();
            info.setRoleId(IDConfigUtil.getId());
            info.setCareer(career[RandomUtils.nextInt(career.length)]);//职业
            info.setRobot(true);
            info.setReady(true);
            info.setCampNo(camp);
            info.setLv(lv);
            //随机一个名字
            info.setName("");
            lists.add(info);
            count++;
        }
        return lists;
    }
}
