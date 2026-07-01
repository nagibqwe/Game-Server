package common.soulanimalforest;

import com.data.CfgManager;
import com.data.MessageString;
import com.data.bean.Cfg_Boss_FirstBlood_Bean;
import com.data.bean.Cfg_Bossnew_SoulBeasts_Bean;
import com.data.container.Cfg_Bossnew_SoulBeasts_Container;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightroom.manager.FightManager;
import com.game.fightroom.structs.FightRoom;
import com.game.gameserver.manager.GameServerManager;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.soulanimalforest.manager.SoulAnimalForestManager;
import com.game.soulanimalforest.script.ISoulAnimalForestScript;
import com.game.soulanimalforest.struct.BossData;
import com.game.soulanimalforest.struct.BossHaveFollow;
import com.game.soulanimalforest.struct.GroupBossData;
import com.game.soulanimalforest.struct.KilledRecord;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import game.core.util.TimeUtils;
import game.message.BossMessage;
import game.message.CrossServerMessage;
import game.message.OpenServerAcMessage;
import game.message.SoulAnimalForestMessage;
import game.message.SoulAnimalForestMessage.*;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 魂兽森林的实现逻辑
 *
 */
public class SoulAnimalForestManagerScript implements ISoulAnimalForestScript {

    private static final Logger log = LogManager.getLogger(SoulAnimalForestManagerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.SoulAnimalForestManagerScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 初始化分区数据
     * @param gbd
     */
    private GroupBossData initData(GroupBossData gbd) {
        Cfg_Bossnew_SoulBeasts_Bean[] beans = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValuees();
        for (Cfg_Bossnew_SoulBeasts_Bean bean : beans) {
            //是否是显示
            if (bean.getCanShow() == 0) {
                continue;
            }
            //是否是跨服
            if (bean.getCrossSever() == 0) {
                continue;
            }
            int cloneId = bean.getCloneid();
            int configId = bean.getID();
            BossData bossData = null;
            switch (bean.getType()) {
                case 1: {
                    if (gbd.getCrystalTime().containsKey(cloneId)) {
                        bossData = gbd.getCrystalTime().get(cloneId);
                    } else {
                        bossData = new BossData();
                        gbd.getCrystalTime().put(cloneId, bossData);
                    }
                }
                break;
                case 2: {
                    if (gbd.getBeastlyBloodCrystalBirthTime().containsKey(cloneId)) {
                        bossData = gbd.getBeastlyBloodCrystalBirthTime().get(cloneId);
                    } else {
                        bossData = new BossData();
                        gbd.getBeastlyBloodCrystalBirthTime().put(cloneId, bossData);
                    }
                }
                break;
                case 3: {
                    if (gbd.getSoulAnimalForestMonsterTime().containsKey(cloneId)) {
                        bossData = gbd.getSoulAnimalForestMonsterTime().get(cloneId);
                    } else {
                        bossData = new BossData();
                        gbd.getSoulAnimalForestMonsterTime().put(cloneId, bossData);
                    }
                }
                break;
                case 4: {
                    if (gbd.getSoulAnimalDataMap().containsKey(configId)) {
                        bossData = gbd.getSoulAnimalDataMap().get(configId);
                    } else {
                        bossData = new BossData();
                        gbd.getSoulAnimalDataMap().put(configId, bossData);
                    }
                }
                break;
                default:
                    break;
            }
            if (bossData == null) {
                continue;
            }
            bossData.setConfigId(configId);
            bossData.setDieNum(0);
            bossData.setCloneId(cloneId);
            bossData.setMaxNum(bean.getNum() + bossData.getMaxNum());
            bossData.setDieTimes(0);
            bossData.setReBornBaseTime(bean.getInitial_time());
            bossData.setBornTime(TimeUtils.Time());
            bossData.setRebornTime(0);
            //保存数据
           // saveData(gbd.getGroupId(), bossData);
        }
        reflushBossData(gbd);//刷新副本的消息
        return gbd;
    }

    @Override
    public void init() {
        SoulAnimalForestManager.allGroupList =
                ServerMatchManager.deal().getAllReachMatchGroupIDList(DailyActiveDefine.ACTIVITY_SOULANIMALISLAND);
        for (int groupId : SoulAnimalForestManager.allGroupList) {
            if (groupId == 0) {
                continue;
            }
            GroupBossData gbd = new GroupBossData();
            gbd.setGroupId(groupId);
            initData( gbd);
            SoulAnimalForestManager.getGroupBossMap().put(groupId, gbd);
        }
    }

    private void reloadBossData(GroupBossData gbd) {
        for (Cfg_Bossnew_SoulBeasts_Bean bean : Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValuees()) {
            //是否是显示
            if (bean.getCanShow() == 0) {
                continue;
            }
            //是否是跨服
            if (bean.getCrossSever() == 0) {
                continue;
            }
            int cloneId = bean.getCloneid();
            int configId = bean.getID();
            BossData bossData = null;
            boolean save = false;
            switch (bean.getType()) {
                case 1: {
                    if (gbd.getCrystalTime().containsKey(cloneId)) {
                        bossData = gbd.getCrystalTime().get(cloneId);
                        if (bossData.getConfigId() != bean.getID()) {
                            bossData = new BossData();
                            gbd.getCrystalTime().put(cloneId, bossData);
                            save = true;
                        }
                    } else {
                        bossData = new BossData();
                        save = true;
                        gbd.getCrystalTime().put(cloneId, bossData);
                    }
                }
                break;
                case 2: {
                    if (gbd.getBeastlyBloodCrystalBirthTime().containsKey(cloneId)) {
                        bossData = gbd.getBeastlyBloodCrystalBirthTime().get(cloneId);
                    } else {
                        bossData = new BossData();
                        save = true;
                        gbd.getBeastlyBloodCrystalBirthTime().put(cloneId, bossData);
                    }
                }
                break;
                case 3: {
                    if (gbd.getSoulAnimalForestMonsterTime().containsKey(cloneId)) {
                        bossData = gbd.getSoulAnimalForestMonsterTime().get(cloneId);
                    } else {
                        bossData = new BossData();
                        save = true;
                        gbd.getSoulAnimalForestMonsterTime().put(cloneId, bossData);
                    }
                }
                break;
                case 4: {
                    if (gbd.getSoulAnimalDataMap().containsKey(configId)) {
                        bossData = gbd.getSoulAnimalDataMap().get(configId);
                    } else {
                        bossData = new BossData();
                        save = true;
                        gbd.getSoulAnimalDataMap().put(configId, bossData);
                    }
                }
                break;
                default:
                    break;
            }
            if (bossData == null) {
                continue;
            }

            if (!save) {
                continue;
            }

            bossData.setConfigId(configId);
            bossData.setDieNum(0);
            bossData.setCloneId(cloneId);
            //初始化数据
            bossData.setBornTime(TimeUtils.Time());
            bossData.setDieTimes(0);
            bossData.setMaxNum(bean.getNum() + bossData.getMaxNum());
            bossData.setReBornBaseTime(bean.getInitial_time());
            bossData.setRebornTime(0);
            //保存数据
            //saveData(gbd.getGroupId(), bossData);
        }
        reflushBossData(gbd);
    }

    @Override
    public void reloadBossData() {
        GroupBossData zeroGbd = SoulAnimalForestManager.getGroupBossMap().get(0);
        if (zeroGbd == null) {
            zeroGbd = new GroupBossData();
            zeroGbd.setGroupId(0);
            SoulAnimalForestManager.getGroupBossMap().put(0, zeroGbd);
        }
        reloadBossData(zeroGbd);
        reflushBossData(zeroGbd);
        /**
         * 重新加载所有分组的数据
         */


        for (int groupId :SoulAnimalForestManager.allGroupList ) {
            if (groupId == 0) {
                continue;
            }

            GroupBossData gbd = SoulAnimalForestManager.getGroupBossMap().get(groupId);
            if (gbd == null) {
                gbd = new GroupBossData();
                gbd.setGroupId(groupId);
                SoulAnimalForestManager.getGroupBossMap().put(groupId, gbd);
            }
            reloadBossData(gbd);
            reflushBossData(gbd);
        }
        //发送change给战斗服
        SendBossInfoToFightServer(null);
    }


    /**
     * 重置内存缓存数据
     */
    private void reflushBossData(GroupBossData gdb) {
        synchronized (gdb.getSoulAnimalDataListMap()) {
            gdb.getSoulAnimalDataListMap().clear();
            Iterator<Entry<Integer, BossData>> iter = gdb.getSoulAnimalDataMap().entrySet().iterator();
            while (iter.hasNext()) {
                Entry<Integer, BossData> en = iter.next();
                BossData db = en.getValue();
                if (gdb.getSoulAnimalDataListMap().containsKey(db.getCloneId())) {
                    gdb.getSoulAnimalDataListMap().get(db.getCloneId()).add(db);
                } else {
                    List<BossData> dbs = new ArrayList<>();
                    dbs.add(db);
                    gdb.getSoulAnimalDataListMap().put(db.getCloneId(), dbs);
                }
            }
        }
    }

    private void makeGroupInfo(crossGroupBossInfo.Builder groupInfo, GroupBossData gbd) {

        Set<Integer> cloneIds = gbd.getCrystalTime().keySet();
        for (int cloneModelId : cloneIds) {
            crossCloneBossInfo.Builder info = crossCloneBossInfo.newBuilder();
            BossData bd = gbd.getCrystalTime().get(cloneModelId);
            crossBossInfo.Builder one = crossBossInfo.newBuilder();
            if (bd != null) {
                one.setBornTime(bd.getBornTime());
                one.setDieNum(bd.getDieNum());
                one.setDieTime(bd.getDieTimes());
                one.setMaxNum(bd.getMaxNum());
                one.setModelConfigId(bd.getConfigId());
                one.setNextTime(bd.getRebornTime());
                one.setRebornTime(bd.getReBornBaseTime());
                one.setFightRoomId(bd.getFightId());
                info.setSoulAnimalInfo(one);
            }
            bd = gbd.getBeastlyBloodCrystalBirthTime().get(cloneModelId);
            crossBossInfo.Builder two = crossBossInfo.newBuilder();
            if (bd != null) {
                two.setBornTime(bd.getBornTime());
                two.setDieNum(bd.getDieNum());
                two.setDieTime(bd.getDieTimes());
                two.setMaxNum(bd.getMaxNum());
                two.setModelConfigId(bd.getConfigId());
                two.setNextTime(bd.getRebornTime());
                two.setRebornTime(bd.getReBornBaseTime());
                two.setFightRoomId(bd.getFightId());
                info.setCloneSoulXue(two);
            }
            bd = gbd.getSoulAnimalForestMonsterTime().get(cloneModelId);
            crossBossInfo.Builder three = crossBossInfo.newBuilder();
            if (bd != null) {
                three.setBornTime(bd.getBornTime());
                three.setDieNum(bd.getDieNum());
                three.setDieTime(bd.getDieTimes());
                three.setMaxNum(bd.getMaxNum());
                three.setModelConfigId(bd.getConfigId());
                three.setNextTime(bd.getRebornTime());
                three.setRebornTime(bd.getReBornBaseTime());
                three.setFightRoomId(bd.getFightId());
                info.setCloneShouwei(three);
            }
            List<BossData> dbs = gbd.getSoulAnimalDataListMap().get(cloneModelId);
            if (dbs == null) {
                continue;
            }
            for (BossData four : dbs) {
                crossBossInfo.Builder ff = crossBossInfo.newBuilder();
                ff.setBornTime(four.getBornTime());
                ff.setDieNum(four.getDieNum());
                ff.setDieTime(four.getDieTimes());
                ff.setMaxNum(four.getMaxNum());
                ff.setModelConfigId(four.getConfigId());
                ff.setNextTime(four.getRebornTime());
                ff.setRebornTime(four.getReBornBaseTime());
                ff.setFightRoomId(four.getFightId());
                info.addBossList(ff);
            }
            info.setCloneModelId(cloneModelId);
            groupInfo.addCloneBossInfo(info);
        }
    }

    /**
     * 发送列表消息给战斗服，用于更新副本中的BOSS出生
     */
    private void SendBossInfoToFightServer(ChannelHandlerContext session) {
        log.info("同步魂兽森林的数据了！");
        P2FResSoulAnimalForestBossInfo.Builder msg = P2FResSoulAnimalForestBossInfo.newBuilder();

        GroupBossData gbd = SoulAnimalForestManager.getGroupBossMap().get(0);
        if (gbd != null) {
            crossGroupBossInfo.Builder groupInfo = crossGroupBossInfo.newBuilder();
            groupInfo.setGroupId(0);
            makeGroupInfo(groupInfo, gbd);
            msg.addGroupInfo(groupInfo);
        }

        for (int groupId :SoulAnimalForestManager.allGroupList) {
            gbd = SoulAnimalForestManager.getGroupBossMap().get(groupId);
            if (gbd != null) {
                crossGroupBossInfo.Builder groupInfo = crossGroupBossInfo.newBuilder();
                groupInfo.setGroupId(groupId);
                makeGroupInfo(groupInfo, gbd);
                msg.addGroupInfo(groupInfo);
            }
        }

        if (session == null) {
            Manager.gameServerManager.send_all_FightGame(P2FResSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        } else {
            MessageUtils.send_to_game(session, P2FResSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private void tickGroupBossData(long curTime, GroupBossData gbd) {
        if (gbd == null) {
            return;
        }
        //计算水晶的更新
        List<BossData> crystalList = new ArrayList<>(gbd.getCrystalTime().values());
        for (BossData bossData : crystalList) {
            if (bossData.getRebornTime() < 1) {
                continue;
            }
            if (curTime > bossData.getRebornTime()) {
                bossData.setRebornTime(bossData.getRebornTime() + bossData.getReBornBaseTime() * 1000);
                bossData.setBornTime(0);
                bossData.setRebornTime(0);
                //通知战斗服应该刷新副本了
                log.info(bossData.getConfigId() + " 开始进行刷新了！， 副本=" + bossData.getCloneId() + ", type =1");
                P2FUpdateOneSoulAnimalForestBossInfo.Builder msg = P2FUpdateOneSoulAnimalForestBossInfo.newBuilder();
                msg.setType(1);
                msg.setBossInfo(makeByBossData(bossData));
                msg.setFightRoomId(bossData.getFightId());
                msg.setGroupId(gbd.getGroupId());
                GameServerManager.getInstance().send_all_FightGame(P2FUpdateOneSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                bossData.setDieNum(0);
            }
        }
        crystalList = new ArrayList<>(gbd.getBeastlyBloodCrystalBirthTime().values());
        for (BossData bossData : crystalList) {
            if (bossData.getRebornTime() < 1) {
                continue;
            }
            if (curTime > bossData.getRebornTime()) {
                bossData.setRebornTime(bossData.getRebornTime() + bossData.getReBornBaseTime() * 1000);
                bossData.setBornTime(0);
                bossData.setRebornTime(0);
                //通知战斗服应该刷新副本了
                log.info(bossData.getConfigId() + " 开始进行刷新了！， 副本=" + bossData.getCloneId() + ", type =2");
                P2FUpdateOneSoulAnimalForestBossInfo.Builder msg = P2FUpdateOneSoulAnimalForestBossInfo.newBuilder();
                msg.setType(2);
                msg.setBossInfo(makeByBossData(bossData));
                msg.setFightRoomId(bossData.getFightId());
                msg.setGroupId(gbd.getGroupId());
                GameServerManager.getInstance().send_all_FightGame(P2FUpdateOneSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                bossData.setDieNum(0);
            }
        }


        /**
         * BOSS的刷新规则
         */
        crystalList = new ArrayList<>(gbd.getSoulAnimalDataMap().values());
        for (BossData bossData : crystalList) {
            if (bossData.getRebornTime() < 1) {
                continue;
            }
//             //boss刷新前一分钟通知玩家
            if (curTime < bossData.getRebornTime() && !bossData.isFlushFollow()) {
                int betweenTime = (int) ((bossData.getRebornTime() - curTime) / 1000); //取秒
                if (betweenTime <= 60) {
                    sendSoulAnimalForestBossRefreshTip(bossData.getConfigId());
                    bossData.setFlushFollow(true); //借用此字段表示刷新提示已通知过了
                }
                continue;
            }
            if (curTime > bossData.getRebornTime()) {
                bossData.setBornTime(bossData.getRebornTime());
                bossData.setRebornTime(0);
                bossData.setReBornBaseTime(0);
               // saveData(gbd.getGroupId(), bossData);
                //通知战斗服应该刷新副本了
                P2FUpdateOneSoulAnimalForestBossInfo.Builder msg = P2FUpdateOneSoulAnimalForestBossInfo.newBuilder();
                msg.setType(4);
                msg.setBossInfo(makeByBossData(bossData));
                msg.setFightRoomId(bossData.getFightId());
                msg.setGroupId(gbd.getGroupId());
                GameServerManager.getInstance().send_all_FightGame(P2FUpdateOneSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                bossData.setDieNum(0);
            }
        }
    }

    @Override
    public void tickBossData() {
        long curTime = TimeUtils.Time();
        tickGroupBossData(curTime, SoulAnimalForestManager.getGroupBossMap().get(0));
        for (int groupId :SoulAnimalForestManager.allGroupList ) {
            if (SoulAnimalForestManager.getGroupBossMap().containsKey(groupId)) {
                tickGroupBossData(curTime, SoulAnimalForestManager.getGroupBossMap().get(groupId));
            } else {

                GroupBossData groupBossData =   new GroupBossData();
                groupBossData.setGroupId(groupId);
                SoulAnimalForestManager.getGroupBossMap().put(groupId, initData(groupBossData));
                tickGroupBossData(curTime, SoulAnimalForestManager.getGroupBossMap().get(groupId));
                //有新的分组进来，那么就需要重新同步一次数据给战斗服
                SendBossInfoToFightServer(null);
            }
        }
        tickGroupBossData(curTime, SoulAnimalForestManager.getGroupBossMap().get(0));
    }

    @Override
    public void onG2PFirstKillBossRefreshTime(ChannelHandlerContext session, long roleId) {
        String groupKey = session.channel().attr(SessionKey.SERVERPLATID).get();
        int groupId = ServerMatchManager.deal().getGroupIDForCurOpenDay(groupKey,DailyActiveDefine.ACTIVITY_SOULANIMALISLAND);
        if (groupId < 0){
            return;
        }
        HashMap<Integer, Integer> firstKillBossList = new HashMap<>();
        for (Cfg_Boss_FirstBlood_Bean bean : CfgManager.getCfg_Boss_FirstBlood_Container().getValuees()) {
            if (bean.getBoss_type() != 3) {
                continue;
            }
            firstKillBossList.put(bean.getMonster_id(), bean.getID());
        }
        GroupBossData gbd = SoulAnimalForestManager.getGroupBossMap().get(groupId);
        if (gbd == null) {
            gbd = initData( new GroupBossData());
            gbd.setGroupId(groupId);
        }
        OpenServerAcMessage.ResFirstKillBossInfo.Builder builder = OpenServerAcMessage.ResFirstKillBossInfo.newBuilder();
        for (Cfg_Bossnew_SoulBeasts_Bean bean : Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValuees()) {
            if (bean.getCanShow() == 0 || bean.getCrossSever() == 0) {
                continue;
            }
            if (bean.getType() != 4) {
                continue;
            }
            if (!firstKillBossList.containsKey(bean.getMonsterid())) {
                continue;
            }
            BossData bossData = gbd.getSoulAnimalDataMap().get(bean.getID());
            OpenServerAcMessage.bossKillInfo.Builder info = OpenServerAcMessage.bossKillInfo.newBuilder();
            info.setCfgId(firstKillBossList.get(bean.getMonsterid()));
            info.setReliveTime(bossData.getRebornTime());
            builder.addBossInfo(info);
        }
        MessageUtils.send_to_player(session, roleId, OpenServerAcMessage.ResFirstKillBossInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private crossBossInfo.Builder makeByBossData(BossData bossData) {
        crossBossInfo.Builder info = crossBossInfo.newBuilder();
        info.setBornTime(bossData.getBornTime());
        info.setDieNum(bossData.getDieNum());
        info.setDieTime(bossData.getDieTimes());
        info.setMaxNum(bossData.getMaxNum());
        info.setModelConfigId(bossData.getConfigId());
        info.setNextTime(bossData.getRebornTime());
        info.setRebornTime(bossData.getReBornBaseTime());
        info.setFightRoomId(bossData.getFightId());
        return info;
    }

    private void sendSoulAnimalForestBossRefreshTip(int bossConfigId) {
        ConcurrentHashMap<Long, BossHaveFollow> flist = SoulAnimalForestManager.getBossFollowList().get(bossConfigId);
        if (flist == null) {
            return;
        }
        SoulAnimalForestMessage.P2GResSoulAnimalForestCrossBossRefreshTip.Builder msg = SoulAnimalForestMessage.P2GResSoulAnimalForestCrossBossRefreshTip.newBuilder();
        Iterator<Entry<Long, BossHaveFollow>> iter = flist.entrySet().iterator();
        HashMap<String, List<Long>> roleSids = new HashMap<>();
        while (iter.hasNext()) {
            Entry<Long, BossHaveFollow> en = iter.next();
            BossHaveFollow bhf = en.getValue();
            long roleId = en.getKey();
            String key = bhf.getPlat() + "_" + bhf.getSid();
            List<Long> roleIds = roleSids.get(key);
            if (roleIds == null) {
                roleIds = new ArrayList<>();
                roleSids.put(key, roleIds);
            }
            roleIds.add(roleId);
        }

        msg.setBossId(bossConfigId);
        Iterator<Entry<String, List<Long>>> siter = roleSids.entrySet().iterator();
        while (siter.hasNext()) {
            Entry<String, List<Long>> en = siter.next();

            ChannelHandlerContext session = GameServerManager.getInstance().GetSession(en.getKey());
            if (session == null) {
                continue;
            }
            msg.clearRoleIds();
            msg.addAllRoleIds(en.getValue());
            MessageUtils.send_to_game(session, SoulAnimalForestMessage.P2GResSoulAnimalForestCrossBossRefreshTip.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void onF2PReqCloneMonsterDie(ChannelHandlerContext session, SoulAnimalForestMessage.F2PReqCloneMonsterDie messInfo) {
        int groupId = messInfo.getGroupId();

        GroupBossData gbd = SoulAnimalForestManager.getGroupBossMap().get(groupId);
        if (gbd == null) {
            gbd = initData( new GroupBossData());
            gbd.setGroupId(groupId);
        }

        int cloneId = messInfo.getCloneModelId();
        int type = messInfo.getType();
        int configId = messInfo.getModelConfigId();
        int reBornBaseTime = messInfo.getRebornBaseTime();
        long reBornTime = messInfo.getReBornTime();
        Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(configId);
        if (bean == null) {
            sendMonsterDieResult(session, messInfo, 1);
            log.error("configId =" + configId + "在配置文件中找不到哦！");
            return;
        }
        BossData bossData = null;
        switch (type) {
            case 1:
                bossData = gbd.getCrystalTime().get(cloneId);
                break;
            case 2:
                bossData = gbd.getBeastlyBloodCrystalBirthTime().get(cloneId);
                break;
            case 3:
                bossData = gbd.getSoulAnimalForestMonsterTime().get(cloneId);
                break;
            case 4:
                bossData = gbd.getSoulAnimalDataMap().get(configId);
                break;
            default: {
                sendMonsterDieResult(session, messInfo, 2);
                log.error("configId =" + configId + "在配置文件中找不到哦！");
            }
            break;
        }

        if (bossData == null) {
            return;
        }


        bossData.setDieNum(bossData.getDieNum() + 1);
        if (bossData.getMaxNum() <= bossData.getDieNum()) {
            bossData.setDieTimes(bossData.getDieTimes() + 1);
            bossData.setDieNum(bossData.getMaxNum());
        }

        bossData.setReBornBaseTime(reBornBaseTime);
        bossData.setRebornTime(reBornTime);

        String killer = messInfo.getKiller();
        if (type == 4) {
            KilledRecord kr = new KilledRecord();
            kr.setKilledTime((int) (TimeUtils.Time() / 1000));
            kr.setKiller(killer);
            if (bossData.getKiller().size() > 20) {
                bossData.getKiller().remove(0);
            }
            bossData.getKiller().add(kr);
            bossData.setFlushFollow(false);
            //saveData(groupId, bossData);
        }
        //保存怪物的数据
        //saveData(groupId,bossData);
        log.info("魂兽森林 副本每" + bean.getLayer() + "的boss:" + bean.getMonster_name() + " 的死亡数据更新成功！");
        sendMonsterDieSuccessResult(session, groupId, messInfo, 0, bossData.getRebornTime(), bossData.getReBornBaseTime(), bossData.getDieNum());
    }

    private void sendMonsterDieSuccessResult(ChannelHandlerContext session, int groupId, SoulAnimalForestMessage.F2PReqCloneMonsterDie messInfo, int state, long rebornTime, int reBaseTime, int dieNum) {
        P2FResCloneMonsterDie.Builder msg = P2FResCloneMonsterDie.newBuilder();
        msg.setCloneModelId(messInfo.getCloneModelId());
        msg.setFightId(messInfo.getFightId());
        msg.setModelConfigId(messInfo.getModelConfigId());
        msg.setState(state);
        msg.setType(messInfo.getType());
        msg.setReBornTime(rebornTime);
        msg.setRebornBaseTime(reBaseTime);
        msg.setDieNum(dieNum);
        msg.setGroupId(groupId);
        MessageUtils.send_to_game(session, P2FResCloneMonsterDie.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void sendMonsterDieResult(ChannelHandlerContext session, SoulAnimalForestMessage.F2PReqCloneMonsterDie messInfo, int state) {
        P2FResCloneMonsterDie.Builder msg = P2FResCloneMonsterDie.newBuilder();
        msg.setCloneModelId(messInfo.getCloneModelId());
        msg.setFightId(messInfo.getFightId());
        msg.setModelConfigId(messInfo.getModelConfigId());
        msg.setState(state);
        msg.setType(messInfo.getType());
        msg.setReBornTime(0);
        msg.setRebornBaseTime(0);
        msg.setDieNum(0);
        MessageUtils.send_to_game(session, P2FResCloneMonsterDie.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onF2PReqSoulAnimalForestBossInfo(ChannelHandlerContext session, SoulAnimalForestMessage.F2PReqSoulAnimalForestBossInfo messInfo) {
        SendBossInfoToFightServer(session);//发送最新的BOSS信息给战斗服
    }

    @Override
    public void onG2PReqSoulAnimalForestCrossPanel(ChannelHandlerContext session, SoulAnimalForestMessage.G2PReqSoulAnimalForestCrossPanel messInfo) {
        String groupKey = session.channel().attr(SessionKey.SERVERPLATID).get();
        Integer groupId =  ServerMatchManager.deal().getGroupIDForCurOpenDay(groupKey,DailyActiveDefine.ACTIVITY_SOULANIMALISLAND);
        if (groupId<0){
            MessageUtils.notify_player(session, messInfo.getRoleId(), MessageString.ServerMachtFail);
            return;
        }
        int modelId = 6100+ messInfo.getLevel();
        List<FightRoom> list = FightManager.getInstance().getBravePeakRoom(modelId, groupId);
        FightRoom mine = null;
        if (list.size() > 0) {
            mine = list.get(0);
        }
        int mapPlayerNum = mine == null?0:mine.hasPeoples();
        GroupBossData gbd = SoulAnimalForestManager.getGroupBossMap().get(groupId);
        if (gbd == null) {
            gbd = initData( new GroupBossData());
            gbd.setGroupId(groupId);
        }
        ResSoulAnimalForestCrossPanel.Builder msg = ResSoulAnimalForestCrossPanel.newBuilder();
        msg.setCrystalHaveNum(messInfo.getCrystalHaveNum());
        msg.setLevel(messInfo.getLevel());
        msg.setMaxCount(messInfo.getMaxCount());
        msg.setRemainCount(messInfo.getRemainCount());
        msg.setRemainRankCount(messInfo.getRemainRankCount());
        msg.setMaxRankCount(messInfo.getMaxRankCount());
        msg.setCrystalBloodHaveNum(messInfo.getCrystalBloodHaveNum());
        msg.setMapPlayerNum(mapPlayerNum);
        HashMap<Integer, Integer> shouwei = new HashMap<>();
        for (Cfg_Bossnew_SoulBeasts_Bean bean : Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValuees()) {
            //是否是显示
            if (bean.getCanShow() == 0) {
                continue;
            }
            //是否是跨服
            if (bean.getCrossSever() == 0) {
                continue;
            }
            //是否是同一层的数据
            if (messInfo.getLevel() != 0 && messInfo.getLevel() != bean.getLayer()) {
                continue;
            }
            BossData bossData = null;
            int cloneid = bean.getCloneid();
            switch (bean.getType()) {
                case 1:
                    bossData = gbd.getCrystalTime().get(cloneid);
                    break;
                case 2:
                    bossData = gbd.getBeastlyBloodCrystalBirthTime().get(cloneid);
                    break;
                case 3: {
                    if (shouwei.containsKey(cloneid)) {
                        continue;
                    }

                    bossData = gbd.getSoulAnimalForestMonsterTime().get(cloneid);
                    if (bossData != null) {
                        shouwei.put(cloneid, 1);
                    }
                }
                break;
                case 4:
                    bossData = gbd.getSoulAnimalDataMap().get(bean.getID());
                    break;
                default:
                    break;
            }

            //如果没有包括得有这块的数据
            if (bossData == null || bean.getType() == 3) {
                continue;
            }

            forestBossInfo.Builder info = forestBossInfo.newBuilder();
            info.setBossId(bossData.getConfigId());
            info.setIsFollowed(false);
            ConcurrentHashMap<Long, BossHaveFollow> flist = SoulAnimalForestManager.getBossFollowList().get(bossData.getConfigId());
            if (flist != null) {
                if (flist.containsKey(messInfo.getRoleId())) {
                    info.setIsFollowed(true);
                }
            }
            info.setNum(bossData.getMaxNum() - bossData.getDieNum());
            int value = (int) ((bossData.getRebornTime() - TimeUtils.Time()) / 1000);
            info.setRefreshTime(value);
            info.setType(bean.getType());
            msg.addBossList(info);
        }
        MessageUtils.send_to_player(session, messInfo.getRoleId(), ResSoulAnimalForestCrossPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 关注魂兽森林的BOSS命令
     *
     * @param session
     * @param messInfo
     */
    @Override
    public void onG2PReqFollowSoulAnimalForestCrossBoss(ChannelHandlerContext session, game.message.SoulAnimalForestMessage.G2PReqFollowSoulAnimalForestCrossBoss messInfo) {
        long roleId = messInfo.getRoleId();
        int configId = messInfo.getBossId();

        boolean isFollow = messInfo.getFollowValue();

        ConcurrentHashMap<Long, BossHaveFollow> flist = SoulAnimalForestManager.getBossFollowList().get(configId);

        if (!isFollow && flist == null) {
            sendFollowResult(session, messInfo, 2);
            return;
        }
        boolean save = false;
        if (flist != null) {
            if (flist.containsKey(messInfo.getRoleId())) {
                if (!isFollow) {
                    flist.remove(roleId);
                    save = true;
                } else {
                    sendFollowResult(session, messInfo, 1);
                    return;
                }
            } else {
                if (isFollow) {
                    BossHaveFollow bhf = new BossHaveFollow();
                    bhf.setOs(messInfo.getOs());
                    bhf.setPlat(messInfo.getPlat());
                    bhf.setSid(messInfo.getSid());
                    flist.put(roleId, bhf);
                    save = true;
                } else {
                    sendFollowResult(session, messInfo, 1);
                    return;
                }
            }
        } else {
            if (isFollow) {
                flist = new ConcurrentHashMap<>();
                ConcurrentHashMap<Long, BossHaveFollow> temp = SoulAnimalForestManager.getBossFollowList().putIfAbsent(configId, flist);
                if (temp != null) {
                    flist = temp;
                }
                BossHaveFollow bhf = new BossHaveFollow();
                bhf.setOs(messInfo.getOs());
                bhf.setPlat(messInfo.getPlat());
                bhf.setSid(messInfo.getSid());
                flist.put(roleId, bhf);
                save = true;
            } else {
                sendFollowResult(session, messInfo, 1);
                return;
            }
        }

        if (save) {
            //SoulAnimalForestFollowBean bean = new SoulAnimalForestFollowBean();
           // bean.setSaf_configId(configId);
           // bean.setFollowValue(JSON.toJSONString(flist));
            //MainServer.getInstance().getSaveSoulAnimalForestFollowThread().dealSoulAnimalForestFollowToDB(bean);
            sendFollowResult(session, messInfo, 0);
        } else {
            sendFollowResult(session, messInfo, 3);
        }

    }

    private void sendFollowResult(ChannelHandlerContext session, G2PReqFollowSoulAnimalForestCrossBoss messInfo, int state) {
        ResFollowSoulAnimalForestCrossBoss.Builder msg = ResFollowSoulAnimalForestCrossBoss.newBuilder();
        msg.setBossId(messInfo.getBossId());
        msg.setFollowValue(messInfo.getFollowValue());
        msg.setState(state);
        MessageUtils.send_to_player(session, messInfo.getRoleId(), ResFollowSoulAnimalForestCrossBoss.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 实现更新水晶， 兽血， 守卫的下一次时间值
     *
     * @param session
     * @param messInfo
     */
    @Override
    public void onF2PUpdateOneSoulAnimalForestBossInfo(ChannelHandlerContext session, SoulAnimalForestMessage.F2PUpdateOneSoulAnimalForestBossInfo messInfo) {
        int groupId = messInfo.getGroupId();

        GroupBossData gbd = SoulAnimalForestManager.getGroupBossMap().get(groupId);
        if (gbd == null) {
            gbd = initData(new GroupBossData());
        }

        log.info("收到BossID=" + messInfo.getBossInfo().getModelConfigId() + "的数据更新!  type= " + messInfo.getType() + " 的反馈！");
        if (messInfo.getType() >= 4) {
            return;
        }

        if (messInfo.getType() < 1) {
            return;
        }
        int type = messInfo.getType();
        int configId = messInfo.getBossInfo().getModelConfigId();
        Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(configId);
        if (bean == null) {
            log.error("configId =" + configId + "在配置文件中找不到哦！", new NullPointerException());
            return;
        }

        BossData bossData = null;
        switch (type) {
            case 1:
                bossData = gbd.getCrystalTime().get(bean.getCloneid());
                break;
            case 2:
                bossData = gbd.getBeastlyBloodCrystalBirthTime().get(bean.getCloneid());
                break;
            case 3:
                bossData = gbd.getSoulAnimalForestMonsterTime().get(bean.getCloneid());
                break;
            default:
                return;
        }

        if (bossData == null) {
            return;
        }

        //如果有房间ID值
        if (bossData.getFightId() != 0) {
            //检查房间是否还在
            FightRoom fr = Manager.fightManager.GetRoomByFightId(bossData.getFightId());
            if (fr == null) {
                bossData.setFightId(0);
                return;
            } else {
                if (bossData.getFightId() != messInfo.getFightRoomId()) {
                    return;
                }
            }
        }

        crossBossInfo info = messInfo.getBossInfo();
        bossData.setDieNum(0);
        bossData.setReBornBaseTime(info.getRebornTime());
        bossData.setRebornTime(info.getNextTime());
    }

    /**
     * 设置魂兽森林副本在那个房间中战斗
     *
     * @param session
     * @param messInfo
     */
    @Override
    public void onF2PSoulAnimalCloneOpen(ChannelHandlerContext session, SoulAnimalForestMessage.F2PSoulAnimalCloneOpen messInfo) {
        int groupId = messInfo.getGroupId();

        GroupBossData gbd = SoulAnimalForestManager.getGroupBossMap().get(groupId);
        if (gbd == null) {
            gbd = initData(new GroupBossData());
            gbd.setGroupId(groupId);
        }
        int cloneId = messInfo.getCloneModelId();
        long fightId = messInfo.getFightRoomId();

        FightRoom fr = Manager.fightManager.GetRoomByFightId(fightId);
        if (fr == null) {
            log.error("房间已经不存在了！");
            return;
        }

        List<Integer> bossIds = messInfo.getBossIdsList();

        BossData bd = gbd.getCrystalTime().get(cloneId);
        bd.setFightId(fightId);
        bd = gbd.getBeastlyBloodCrystalBirthTime().get(cloneId);
        bd.setFightId(fightId);
        for (int bossId : bossIds) {
            bd = gbd.getSoulAnimalDataMap().get(bossId);
            bd.setFightId(fightId);
        }
    }

    @Override
    public void onG2PReqCrossSoulAnimalForestBossKiller(ChannelHandlerContext session, SoulAnimalForestMessage.G2PReqCrossSoulAnimalForestBossKiller messInfo) {
        int bossConfigId = messInfo.getBossConfigId();
        String groupKey = session.channel().attr(SessionKey.SERVERPLATID).get();
        Integer groupId =  ServerMatchManager.deal().getGroupIDForCurOpenDay(groupKey,DailyActiveDefine.ACTIVITY_SOULANIMALISLAND);
        if (groupId<0){
            MessageUtils.notify_player(session, messInfo.getRoleId(), MessageString.ServerMachtFail);
            return;
        }

        GroupBossData gbd = SoulAnimalForestManager.getGroupBossMap().get(groupId);
        if (gbd == null) {
            gbd = initData( new GroupBossData());
            gbd.setGroupId(groupId);
        }
        BossData bd = gbd.getSoulAnimalDataMap().get(bossConfigId);
        if (bd == null) {
            return;
        }
        BossMessage.ResBossKilledInfo.Builder msg = BossMessage.ResBossKilledInfo.newBuilder();
        msg.setBossId(bossConfigId);
        msg.setBossType(5);
        for (KilledRecord kr : bd.getKiller()) {
            BossMessage.BossKilledRecord.Builder info = BossMessage.BossKilledRecord.newBuilder();
            info.setKillTime(kr.getKilledTime());
            info.setKiller(kr.getKiller());
            msg.addKilledRecordList(info.build());
//        msg.setKillerName(bd.getKiller());
        }
        MessageUtils.send_to_player(session, messInfo.getRoleId(), BossMessage.ResBossKilledInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }


    public void gmRefreshSoulBoss(int groupID,int cloneMapID,int configID,int type)
    {
        if (!SoulAnimalForestManager.allGroupList.contains(groupID)){
            log.info("此组ID不存在  "  + groupID);
            return;
        }
        List<FightRoom> list = FightManager.getInstance().getBravePeakRoom(cloneMapID, groupID);
        if (list.size() > 0) {
            FightRoom  mine = list.get(0);
            if (mine == null)
                return;
            GroupBossData zeroGbd =  SoulAnimalForestManager.getGroupBossMap().get(groupID);
            if (zeroGbd == null){
                log.info("zeroGbd = null  " + groupID );
                return;
            }
            if (configID > 0){
                ConcurrentHashMap<Integer, BossData> crystalList  = new ConcurrentHashMap<>();
                if (type == 1){
                    crystalList = zeroGbd.getCrystalTime();
                }else if (type == 2){
                    crystalList = zeroGbd.getBeastlyBloodCrystalBirthTime();
                }else {
                    crystalList = zeroGbd.getSoulAnimalDataMap();
                }
                BossData  bossData =  zeroGbd.getSoulAnimalDataMap().get(configID);
                if (bossData!=null){
                    bossData.setRebornTime(TimeUtils.Time() + 1000);
                    bossData.setDieNum(bossData.getMaxNum());
                    bossData.setFightId(mine.getFid());
                }
            }else {
                refreshAll(zeroGbd.getCrystalTime(), mine.getFid());
                refreshAll(zeroGbd.getBeastlyBloodCrystalBirthTime(), mine.getFid());
                refreshAll(zeroGbd.getSoulAnimalDataMap(), mine.getFid());
            }
        }
    }

    private void refreshAll(ConcurrentHashMap<Integer, BossData> crystalList,Long fightID)
    {
        for (BossData bossData :crystalList.values()){
            bossData.setRebornTime(TimeUtils.Time() + 1000);
            bossData.setDieNum(bossData.getMaxNum());
            bossData.setFightId(fightID);
        }
    }


    @Override
    public void onF2PMakeBossRefresh(CrossServerMessage.F2PMakeBossRefresh messInfo) {
        int groupId = messInfo.getGroupID();
        List<Integer> bossIDList = messInfo.getBossIDList();
        GroupBossData data = SoulAnimalForestManager.getGroupBossMap().get(groupId);
        List<BossData> crystalList = new ArrayList<>(data.getSoulAnimalDataMap().values());
        for (BossData bossData : crystalList) {
            if (!bossIDList.contains(bossData.getConfigId())) {
                continue;
            }
            if (bossData.getRebornTime() < 1) {
                log.error("跨服通知的Boss处于存活状态，正常情况不会走到这，请查看！");
                continue;
            }
            bossData.setBornTime(bossData.getRebornTime());
            bossData.setRebornTime(0);
            bossData.setReBornBaseTime(0);
            bossData.setDieNum(0);

            P2FUpdateOneSoulAnimalForestBossInfo.Builder msg = P2FUpdateOneSoulAnimalForestBossInfo.newBuilder();
            msg.setGroupId(data.getGroupId());
            msg.setType(4);
            msg.setBossInfo(makeByBossData(bossData));
            msg.setFightRoomId(bossData.getFightId());
            GameServerManager.getInstance().send_all_FightGame(P2FUpdateOneSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

}
