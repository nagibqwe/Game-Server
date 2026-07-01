/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.zone;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Robotrandomname_Bean;
import com.game.fightroom.structs.FightRoom;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.zone.manager.ZoneManager;
import com.game.zone.script.IZoneScript;
import com.game.zone.structs.*;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.ZoneMessage;
import game.message.ZoneMessage.P2GReqMatchSucceed;
import game.message.ZoneMessage.cloneTeamInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 跨服副本逻辑状态处理
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class ZoneManagerScript implements IZoneScript {

    private static final Logger log = LogManager.getLogger(ZoneManagerScript.class);

    //匹配成功后倒计时30秒
    private static final int countdown = 30;
    
    private static int currMacthB = 0;

    Lock lock = new ReentrantLock();
    
    @Override
    public int getId() {
        return ScriptEnum.ZoneManagerScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void OnTick(long time) {
        //匹配
        if(lock.tryLock()){
                for (int modeId : ZoneManager.ztCache.keySet()) {
                //这里添加匹配的规则如果是多人，boss战等
                Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(modeId);

                if (!ZoneManager.ztCache.get(modeId).isEmpty()) {
                    //小于5人不需要匹配了，有一人到30秒直接全部进入，哈哈。
                    List<ZoneTeam> teams = new ArrayList<>();
                    int pNum = 0;
                    ConcurrentHashMap<Integer, ConcurrentLinkedQueue<ZoneTeam>> copy = new ConcurrentHashMap<>(ZoneManager.ztCache.get(modeId));
                    for (Integer n : copy.keySet()) {
                        if (!copy.get(n).isEmpty()) {
                            pNum += n;
                        }
                    }
                    if (pNum == 0) {
                        continue;
                    }
                    if (pNum < ZoneMapDefine.GM_ZONE_MAP_MAX_NUM) {
                        //查看是否有达到30秒的队伍
                        boolean isMatch = false;
                        for (Integer n : copy.keySet()) {
                            for (ZoneTeam z : copy.get(n)) {
                                if (TimeUtils.Time() - z.getCreateTime() > 30 * 1000) {
                                    isMatch = true;
                                }
                            }
                        }
                        if (isMatch) {
                            for (Integer n : copy.keySet()) {
                                for (ZoneTeam z : copy.get(n)) {
                                    teams.add(z);
                                    ZoneManager.ztCache.get(modeId).get(n).remove(z);
                                }
                            }
                        }
                    } else {
                        teams = matchTeam(modeId, ZoneMapDefine.GM_ZONE_MAP_MAX_NUM, currMacthB);
                    }
                    if (teams == null || teams.isEmpty()) {
                        continue;
                    }
                    int size = 0;
                    for (ZoneTeam zt : teams) {
                        size += zt.getPlist().size();
                    }
                    log.info("匹配成功：人数size = " + size);
                    //查看匹配人数：不够增加机器人
                    if (size < ZoneMapDefine.GM_ZONE_MAP_MAX_NUM) {
                        teams.add(createZoneTeam(ZoneMapDefine.GM_ZONE_MAP_MAX_NUM - size, bean.getMin_lv() + 5));
                    }

                //随机到一组,还不能马上去战斗 还要通知玩家
                    //根据这个副本 创建一个战场吧
                    FightRoom fr = null;
                    try {
                        fr = Manager.fightManager.deal().createFightRoom(bean, teams);
                        //加入计时器
                        ZoneManager.countdown.put(fr.getFid(), TimeUtils.Time() + (countdown + 2) * 1000);
                    } catch (Exception ex) {
                        log.error(ex, ex);
                    }
                    if (fr == null) {
                        log.error("匹配创建战场房间错误，必须解决");
                        break;
                    }
                    sendTeamInfo(fr);
                }
            }
            try{
                
            }catch(Exception e){
                log.error(e,e);
            }finally{
                lock.unlock();
            }
        }else{
            log.error("匹配获取锁失败");
        }
        
        //时间检查
        Set<Long> keySet = new HashSet<>(ZoneManager.countdown.keySet());
        for (Long cdKey : keySet) {
            long cd = ZoneManager.countdown.get(cdKey);
            if (cd < TimeUtils.Time()) {
                //取出房间的Id
                FightRoom fr = Manager.fightManager.getFrcache().get(cdKey);
                if(fr==null){
                    ZoneManager.countdown.remove(cdKey);
                    continue;
                }
                Manager.fightManager.getFrcache().remove(fr.getFid());
                Set<ZoneTeam> zts = fr.getTeam();
                //判断这个队伍里有玩家没装备
                //没准备的玩家队伍踢出匹配
                Iterator it = zts.iterator();
                TeamPlayerInfo noRead = null;
                while (it.hasNext()) {
                    ZoneTeam zt = (ZoneTeam) it.next();
                    for (Long pKey : zt.getPlist().keySet()) {
                        TeamPlayerInfo info = zt.getPlist().get(pKey);
                        if (info.isRobot()) {
                            continue;
                        }
                        if (!info.isReady()) {
                            noRead = info;
                            break;
                        }
                    }
                }
                if (noRead != null) {
                    Iterator it1 = zts.iterator();
                    while (it1.hasNext()) {
                        try {
                            ZoneManager.deal().cancelMatch(noRead.getRoleId(), noRead.getName(), (ZoneTeam) it1.next(), 2);
                        } catch (Exception ex) {
                            log.error(ex, ex);
                        }
                    }
                    it1.remove();
                }
                ZoneManager.countdown.remove(cdKey);
            }
        }
    }

    @Override
    public void sendTeamInfo(FightRoom fr) {

        HashMap<Integer, String> sendPlat = new HashMap<>();
        P2GReqMatchSucceed.Builder builder = P2GReqMatchSucceed.newBuilder();
        builder.setEndTime((int)((fr.getCtime()+countdown*1000>TimeUtils.Time()?(fr.getCtime()+countdown*1000)-TimeUtils.Time():0)/1000));
        builder.setModelId(fr.getModelId());
        builder.setTeamId(fr.getFid());//战斗的id
        boolean isHave = false;
        for (ZoneTeam zt : fr.getTeam()) {
            for (TeamPlayerInfo info : zt.getPlist().values()) {
                //为true就一定有机器人了
                if(!isHave){
                    isHave = info.isReady();
                }
                builder.addInfolist(buildTeamInfo(info));//所有玩家
            }
            if (zt.getsId() != 0 && !zt.getPlat().isEmpty()) {
                sendPlat.put(zt.getsId(), zt.getPlat());
            }
        }
        //没有存玩家链接 先发到各个服务器吧
        for (Integer key : sendPlat.keySet()) {
            ChannelHandlerContext socket = Manager.gameServerManager.GetSession(sendPlat.get(key), key);
            MessageUtils.send_to_game(socket, ZoneMessage.P2GReqMatchSucceed.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
        
//        for (ZoneTeam zt : fr.getTeam()) {
//            ChannelHandlerContext socket = Manager.gameServerManager.GetSession(zt.getPlat(), zt.getsId());
//            MessageUtils.send_to_game(socket, ZoneMessage.P2GReqMatchSucceed.MsgID.eMsgID_VALUE, builder.build().toByteArray());
//        }

    }

    private ZoneTeam createZoneTeam(int num,int lv){
        ZoneTeam zt = new ZoneTeam();
        List<TeamPlayerInfo> tps = createRobot(num,lv);
        for(TeamPlayerInfo ps: tps){
            zt.getPlist().put(ps.getRoleId(), ps);
        }
        return zt;
    }
    
    //创建多少个机器人
    private List<TeamPlayerInfo> createRobot(int num,int lv){
        List<TeamPlayerInfo> lists = new ArrayList<>();
        int[] career = new int[]{1,3,4,5};
        for(int i = 0;i<num;i++){
            TeamPlayerInfo info = new TeamPlayerInfo();
            info.setRoleId(IDConfigUtil.getId());
            
            info.setCareer(career[RandomUtils.nextInt(career.length)]);//职业
            info.setRobot(true);
            info.setReady(true);
            info.setLv(lv);
            //随机一个名字
            info.setName("");
            lists.add(info);
        }
        return lists;
    }
//    根据职业随机一个名字
    private String randomNameByCarceer(int carceer){
        int sax = 0;
//        ID=职业id*100+技能位置ID（职业0学生，1拳师，2大锤，3剑客，4卡牌，5枪手）（技能位置1-5，1为普通攻击）
        switch(carceer){
            case 0:
            case 1:
            case 5:   
                sax = 3;
                break;
            case 2:
            case 3:
            case 4:
                sax = 2;
                break;
        }
        List<String> names = new ArrayList<>();//名字
        List<String> surnames = new ArrayList<>();//姓氏
        for(Cfg_Robotrandomname_Bean bean : CfgManager.getCfg_Robotrandomname_Container().getValuees()){
            if(bean.getQ_type()==1){
                surnames.add(bean.getQ_value());
                continue;
            }
            if(bean.getQ_type()==sax){
                names.add(bean.getQ_value());
            }
        }
        String name = names.get(RandomUtils.nextInt(names.size()));
        String surname = surnames.get(RandomUtils.nextInt(surnames.size()));
        return name+surname;
    }
    
    
    
    @Override
    public cloneTeamInfo.Builder buildTeamInfo(TeamPlayerInfo info) {
        cloneTeamInfo.Builder teamInfo = cloneTeamInfo.newBuilder();
        teamInfo.setCarear(info.getCareer());
        teamInfo.setLeader(false);
        teamInfo.setReady(info.isReady());
        teamInfo.setRoleId(info.getRoleId());
        teamInfo.setRoleName(info.getName());
        teamInfo.setServerId(info.getServerId());
        teamInfo.setLevel(info.getLv());
        teamInfo.setPower(info.getF());
        teamInfo.setIsRobot(info.isRobot());
        return teamInfo;
    }


    /**
     * 游戏服登录的时候同步副本的时间
     *
     * @param context
     */
    @Override
    public void OnNoticeTime(ChannelHandlerContext context) {
//        sendTime(CloneStateDefine.ggzj, context);
//        sendTime(CloneStateDefine.jdsf, context);
//        sendTime(CloneStateDefine.zfxd, context);
//        sendTime(CloneStateDefine.ychd, context);
//        sendTime(CloneStateDefine.CrossResourceWar, context);
//        sendTime(CloneStateDefine.CrossCampWar, context);
    }

    /**
     * 确认玩家的分组信息
     *
     * @param rws
     * @param FightId
     */

    private final HashMap<String, Integer> group = new HashMap<>();
    private final HashMap<String, Integer> grNum = new HashMap<>();


    /**
     * 能进入进来表示前置条件已经通过了,全局匹配
     *
     * @param modeId//确定的副本
     * @param total //总匹配人数
     * @param birthGroup//不管阵营匹配用0表示
     * @return
     */
    public List<ZoneTeam> matchTeam(int modeId, int total, int birthGroup) {
        List<ZoneTeam> completeTeam = new ArrayList<>();
        //已经匹配的人数
        int num = 0;
        //匹配完成
        choose(completeTeam, num, total - num, total, birthGroup, modeId);
        //假如还不够就放回去从新匹配。因为是队列所有放到最后了
        for (ZoneTeam zt : completeTeam) {
            num += zt.getPlist().size();
        }
        if (num != total) {
            for (ZoneTeam zt : completeTeam) {
                int key = zt.getPlist().size();
                if (ZoneManager.ztCache.containsKey(modeId)) {
                    ZoneManager.ztCache.get(modeId).get(key).add(zt);
                } else {
                    ConcurrentLinkedQueue<ZoneTeam> queue = new ConcurrentLinkedQueue<>();
                    queue.add(zt);
                    ConcurrentHashMap<Integer, ConcurrentLinkedQueue<ZoneTeam>> map = new ConcurrentHashMap<>();
                    map.put(key, queue);
                    ZoneManager.ztCache.put(modeId, map);
                }
            }
        } else {
            return completeTeam;
        }
        //可以去开房了
        return null;
    }

    /**
     * 递归找出需要的组合
     *
     * @param zoneTeam //存在的对象
     * @param num 存在的总人数
     * @param remain //从几开始匹配
     * @param total //需要匹配的总人数
     * @param birthGroup //是否需要阵营相同0不管
     * @param modeId//副本Id
     */
    public void choose(List<ZoneTeam> zoneTeam, int num, int remain, int total, int birthGroup, int modeId) {

        if (remain <= 0) {
            return;
        }
        if (ZoneManager.ztCache.get(modeId).get(remain) == null || ZoneManager.ztCache.get(modeId).get(remain).isEmpty()) {
            choose(zoneTeam, num, remain - 1, total, birthGroup, modeId);
        } else {
            ZoneTeam zt = null;
            if (birthGroup == 0) {
                zt = ZoneManager.ztCache.get(modeId).get(remain).poll();
            } else {
                Iterator it = ZoneManager.ztCache.get(modeId).get(remain).iterator();
                while (it.hasNext()) {
                    zt = (ZoneTeam) it.next();
                    if (zt.getBirthGroup() == birthGroup) {
                        it.remove();
                        break;
                    }else{
                        zt = null;
                        break;
                    } 
                }
            }
            if (zt != null) {
                zoneTeam.add(zt);
                num += zt.getPlist().size();
                choose(zoneTeam, num, total - num, total, birthGroup, modeId);
            } else {
                choose(zoneTeam, num, remain - 1, total, birthGroup, modeId);
            }
        }
    }


}
