package common.copyMap.crosscopy;

import com.data.CfgManager;
import com.data.MessageString;
import com.data.bean.*;
import com.data.container.Cfg_Bossnew_drop_Container;
import com.data.container.Cfg_Monster_Container;
import com.game.backpack.structs.Item;
import com.game.buff.structs.Buff;
import com.game.map.script.IMapBaseScript;
import com.game.copymap.scripts.ICopyGatherScript;
import com.game.copymap.structs.FightRoomState;
import com.game.drop.structs.SpecialDropDefine;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.manager.MapGpsUtil;
import com.game.map.manager.MapManager;
import com.game.map.structs.*;
import com.game.monster.structs.Monster;
import com.game.ninedaysfocused.manager.NineDaysFocusedManager;
import com.game.ninedaysfocused.script.INineDaysCloneWar;
import com.game.ninedaysfocused.structs.BossHurtData;
import com.game.ninedaysfocused.structs.NineDaysDefine;
import com.game.ninedaysfocused.structs.NineDaysTask;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.robot.ai.RobotAi;
import com.game.robot.script.IRobotScript;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.Gather;
import com.data.ItemChangeReason;
import com.game.utils.MessageUtils;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 九天争锋
 * Created by CLC 跨服地图脚本
 */
public class NineDaysFoucusedWarCloneScript implements INineDaysCloneWar, IMapBaseScript, ICopyGatherScript {

    private static final Logger LOG = LogManager.getLogger(NineDaysFoucusedWarCloneScript.class);

    public Position RedPos = MapManager.getPos(24.56f, 55.98f);
    public Position BluePos = MapManager.getPos(24.56f, 40.83f);
    private int RedCamp = 13;
    private int BlueCamp = 14;



    @Override
    public int getId() {
        return ScriptEnum.NineDaysFocusedWarCloneActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    //根据机器人等级和职业
    private void createRobot(Player player ,int[] carceers,int lv,MapGps gps){
        //是否要创建机器人
        int length = carceers.length;
        if(length>0){
            Cfg_Clonerobot_Bean[] lists =CfgManager.getCfg_Clonerobot_Container().getValuees();
            IRobotScript is = (IRobotScript) Manager.scriptManager.GetScriptClass(ScriptEnum.RobotBaseScript);
            for(int i = 0;i<length;i++){
                int carceer = carceers[i];
                for(Cfg_Clonerobot_Bean bean : lists){
                    if (bean.getCareer() == carceer && bean.getLevel() == lv) {
                        //创建一个机器人
                        int camp = i%2==0?RedCamp:BlueCamp;
                        Robot robot = is.OnMakeByRobotConfig(bean.getRobotID());
                        MapGpsUtil.CopyGPS(gps, robot.getCurGps());
                        robot.setMakerId(player.getId());
                        robot.setAi(RobotAi.Anger);
                        robot.setCamp(camp);
                        robot.changeCurPos(camp==RedCamp?RedPos:BluePos);
                        if (player.getCamp() != camp)
                            robot.addHatred(player,500);
                        else
                            robot.addHatred(player,0);
                        Manager.mapManager.manager().onEnterMap(robot);
                    }
                }
            }
        }
    }


    @Override
    public boolean onBeginGather(Player player, Gather gather) {
        return false;
    }

    @Override
    public void onGather(Player player, Gather gather) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(gather.getModelId());
        if (null == gatherCfg) {
            LOG.error("配置的没有，怎么初始化的！");
            return;
        }
        //采集任务
        if (gatherCfg.getId()  == 111  || gatherCfg.getId()  == 222)
        {
            NineDaysTask task =  player.getPersonalData().getTaskList().get(gatherCfg.getId());
            if (task!=null)
            {
                if (task.getAlreadyStage()<task.getTargetStage())
                {
                    task.setAlreadyStage(task.getAlreadyStage()+1);
                    sendMessage(player,task);
                }
            }
        }
        //同正营的玩家也将获得奖励，有次数限制
        if (gatherCfg.getDropId() > 0) {
            LOG.info(gather + "被采集了！dropid=" + gatherCfg.getDropId() + player);
            for (Player p : map.getPlayers().values()){
                if (p.getCamp() == player.getCamp()){
                    if (player.getPersonalData().getGatherRewardNum()<5){
                        List<List<Integer>> itemDrops = Manager.dropManager.deal().getItemDrops(player, gatherCfg.getDropId());
                        List<Item> list = Item.createItems(itemDrops, 1);
                        long actionId = IDConfigUtil.getLogId();
                        if (!Manager.backpackManager.manager().addItems(player, list, ItemChangeReason.DropByGatherGet, actionId)) {
                            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                                    MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, list, ItemChangeReason.DropByGatherGet, actionId);
                            // String chatItemInfo = Manager.backpackManager.manager().getChatInfo(list);
                            //FightClientManager.GetInstance().send_to_game(p.getIosession(), CrossServerMessage.F2GFightEnd.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                           // MessageUtils.notify_player(p, Notify.CHAT,"恭喜你获得同阵营玩家采集获得的奖励");
                        }
                        p.getPersonalData().setGatherRewardNum(p.getPersonalData().getGatherRewardNum()+1);
                        //发送采集掉落奖励
                        NineDaysFocusedMessage.F2GSynchrodata.Builder msg_2 =  NineDaysFocusedMessage.F2GSynchrodata.newBuilder();
                        msg_2.setRoleId(p.getId());
                        msg_2.setDropId(gatherCfg.getDropId());
                        msg_2.setBossRewardNum(p.getPersonalData().getBossRewardNum());
                        msg_2.setGatherRewardNum(p.getPersonalData().getGatherRewardNum());
                        FightClientManager.GetInstance().send_to_game(player.getIosession(), NineDaysFocusedMessage.F2GSynchrodata.MsgID.eMsgID_VALUE, msg_2.build().toByteArray());
                    }
                }
            }
        }

        //战车采集后加buff 变身
        if (gatherCfg.getId()  == 11)
        {
//            int mBuffId = Global.MoneyInspireBuff;
//            Manager.buffManager.deal().onAddBuff(player, player, mBuffId);
        }
    }

    @Override
    public void onOutGather(Player player, Gather gather) {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {

        mapObject.setCreate(TimeUtils.Time());
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());
        if (bean == null) {
            LOG.error("找不到九天争锋地图！");
            return;
        }
        mapObject.setPkState(MapDefine.PK_STATE2);

        ConcurrentHashMap<Long, BossHurtData>  hlist = new ConcurrentHashMap<>();
        NineDaysFocusedManager.getInstance().getBossHurtList().put(mapObject.getId(), hlist);

        Cfg_Clone_map_Bean copy = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());
        mapObject.addMapOnceScriptEventTimer(getId(), "OnGameOver", copy.getExist_time());

        mapObject.addMapOnceScriptEventTimer(getId(), "OnRefreshBoss", 500000);

        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTING);

    }

    @Override
    public boolean canEnterMap(Player player, int model, int level)
    {
        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        player.setCamp(player.playerCrossData.fightCampNo);
        if (player.getCamp() == RedCamp) {
            player.changeCurPos(RedPos);
        } else {
            player.changeCurPos(BluePos);
        }
        Manager.mapManager.changeMap(player, map.getId(), player.gainCurPos(), false);
        Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStateCamp, true);
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getZoneModelId());
        synchronized (this) {
            if (!MapParam.getNinthFoucusedParam(map).containsKey(NineDaysDefine.IsRobot)) {
                createRobot(player, player.getMatchRobot(), bean.getMin_lv() + 5, player.getCurGps());
                MapParam.getNinthFoucusedParam(map).put(NineDaysDefine.IsRobot,true);
            }else
            {
                for (Robot r : map.getRobots().values()){
                    if (r.getCamp() != player.getCamp())
                        r.addHatred(player,500);
                    else
                        r.addHatred(player,0);
                }
            }
        }
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {

        if (isQuit) {
            player.setCamp(0, true);
            Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStatePeace, true);//和平模式
        }
    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

        if (!(attacker instanceof Player)) {
            return;
        }
        Player player = (Player) attacker;
        if (monster.getModelId() == 1212 || monster.getModelId() == 111){
            ConcurrentHashMap<Long, BossHurtData> hurtlist = null;
            BossHurtData hurtdata = null;
            if (!NineDaysFocusedManager.getInstance().getBossHurtList().containsKey(mapObject.getId()))
            {
                hurtlist = new ConcurrentHashMap<>();
               // NineDaysFocusedManager.getInstance().getBossHurtList().put(clone.getId(),hurtlist);
            }
            else
            {
                hurtlist =   NineDaysFocusedManager.getInstance().getBossHurtList().get(mapObject.getId());
            }
            if (!hurtlist.containsKey(monster.getId())){
                hurtdata = new BossHurtData();
                hurtdata.setUid(monster.getId());
                hurtdata.setModelId(monster.getModelId());
                hurtdata.setHurtA(0);
                hurtdata.setHurtB(0);
                hurtdata.setMaxH(monster.getAttribute().MaxHP());
                hurtlist.put(hurtdata.getUid(),hurtdata);
            }
            else
            {
                hurtdata =  hurtlist.get(monster.getId());
            }

            if (attacker.getCamp() == RedCamp)
                hurtdata.setHurtA(hurtdata.getHurtA() + damage);
            else
                hurtdata.setHurtB(hurtdata.getHurtB() + damage);

            NineDaysFocusedManager.getInstance().getBossHurtList().put(mapObject.getId(),hurtlist);

            NineDaysFocusedMessage.ResBossHurtInfo.Builder msg =  NineDaysFocusedMessage.ResBossHurtInfo.newBuilder();
            for (BossHurtData h:hurtlist.values()){
                NineDaysFocusedMessage.BossInfo.Builder boss = NineDaysFocusedMessage.BossInfo.newBuilder();
                float huerAPer = (float)h.getHurtA()/h.getMaxH() ;
                float huerBPer = (float)h.getHurtB()/h.getMaxH() ;
                boss.setUid( h.getUid());
                boss.setCampAhurtPer(huerAPer);
                boss.setCampBhurtPer(huerBPer);
                msg.addBosslist(boss);
            }
            for (Player p : mapObject.getPlayers().values()){
                ///发给客户端
                FightClientManager.GetInstance().send_to_game(p.getIosession(),
                        NineDaysFocusedMessage.ResBossHurtInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }

    @Override
    public void onMonsterDie(MapObject mapObject, Monster monster, Fighter attacker) {
        if (!(attacker instanceof Player)) {
            return;
        }
        Player me = (Player) attacker ;
        //刷新任务
        if (monster.getModelId() == 3333 )
        {
            NineDaysTask task = me.getPersonalData().getTaskList().get(monster.getModelId());
            if (task!=null)
            {
                if (task.getAlreadyStage()<task.getTargetStage())
                {
                    task.setAlreadyStage(task.getAlreadyStage()+1);
                    sendMessage(me,task);
                }
            }
        }
        BossHurtData h =  NineDaysFocusedManager.getInstance().getBossHurtList().get(mapObject.getId()).get(monster.getId());
        int camp = 0;
        if (h != null){
            if (h.getHurtA()>h.getHurtB())
                camp = RedCamp;
            else
                camp = BlueCamp;
        }
        if (monster.getModelId() == 111 || monster.getModelId() == 222)
        {
            for (Player p : mapObject.getPlayers().values() ){
                if (p.getCamp() == camp  || p.getId() == me.getId() )
                {
                    if(p.getPersonalData().getBossRewardNum()<5){
                        //给同阵营玩家发奖
                        Cfg_Monster_Bean monsterBean = Cfg_Monster_Container.GetInstance().getValueByKey(monster.getModelId());
                        if (monsterBean == null)
                            return;
                        if (monsterBean.getSpecialdrop() <= 0)
                            return;

                        int dropId = monsterBean.getSpecialdrop();
                        Cfg_Bossnew_drop_Bean bean = Cfg_Bossnew_drop_Container.GetInstance().getValueByKey(dropId);
                        if (bean == null) {
                            LOG.info("击杀boss获得掉落物品失败，没有在Cfg_Bossnew_dropBean中找到相应数据，dropId = " + dropId);
                            return;
                        }

                        //这个itemDropID要发回本服
                         int itemDropID =  bean.getDrop_Ordinary();

                        //跨服还是模拟掉落一下
                        Manager.dropManager.deal().specialDropReward(monster, (Player) attacker, SpecialDropDefine.NINEDAYS_FOUCUSED, false, -1);
                        p.getPersonalData().setBossRewardNum(p.getPersonalData().getBossRewardNum()+1);

                        //发送BOSS掉落奖励
                        NineDaysFocusedMessage.F2GSynchrodata.Builder msg_2 =  NineDaysFocusedMessage.F2GSynchrodata.newBuilder();
                        msg_2.setRoleId(p.getId());
                        msg_2.setDropId(itemDropID);
                        msg_2.setBossRewardNum(p.getPersonalData().getBossRewardNum());
                        msg_2.setGatherRewardNum(p.getPersonalData().getGatherRewardNum());
                        FightClientManager.GetInstance().send_to_game(p.getIosession(),
                                NineDaysFocusedMessage.F2GSynchrodata.MsgID.eMsgID_VALUE, msg_2.build().toByteArray());
                    }
                }
            }
        }
    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {


    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter attacker, Player player) {

        if (!(attacker instanceof Player)) {
            return;
        }
        Player att = (Player) attacker ;
        boolean ischariot = false;
        NineDaysTask task = null;
        for (Buff b : player.getBuffs()){
            if (b.getBuffId() == 111)
            {
                //判断是否击杀的是战车
                task =  att.getPersonalData().getTaskList().get(12);
                ischariot = true;
                break;
            }
        }
        //如果不是战车，那就是击杀普通人
        if (!ischariot){
            task =  att.getPersonalData().getTaskList().get(222);
        }
        if (task!=null)
        {
            if (task.getAlreadyStage()<task.getTargetStage())
            {
                task.setAlreadyStage(task.getAlreadyStage()+1);
                sendMessage(player,task);
            }
        }
        player.changeCurPos(player.getCamp()==RedCamp?RedPos:BluePos);
    }

    public  void OnRobotDie(MapObject map, Robot robot, Fighter attacker){
        if (!(attacker instanceof Player)) {
            return;
        }
        Player att = (Player) attacker ;
        NineDaysTask task =  att.getPersonalData().getTaskList().get(12);
        if (task!=null)
        {
            if (task.getAlreadyStage()<task.getTargetStage())
            {
                task.setAlreadyStage(task.getAlreadyStage()+1);
                sendMessage(att,task);
            }
        }
        //原地复活
        robot.reset();
        robot.onHpChange(robot);
        //robot.changeCurPos(robot.getCamp()==RedCamp?RedPos:BluePos);
        MapMessage.ResRelive.Builder msg = MapMessage.ResRelive.newBuilder();
        msg.setPlayerId(robot.getId());
        msg.setMapId(map.getMapModelId());
        msg.setCurPos(MapUtils.getPos(robot.gainCurPos()));
        MessageUtils.send_to_roundPlayer(robot, MapMessage.ResRelive.MsgID.eMsgID_VALUE,
                msg.build().toByteArray(), true);
    }
    @Override
    public void action(MapObject mapObject, String method, Object[] params) {

        switch (method) {
            case "OnCloneClose":
                onCloneClose(mapObject);
                break;
            case "OnGameOver":
                onGameOver(mapObject);
                break;
            case "OnRefreshBoss":
                onRefreshBoss(mapObject);
                break;


        }
    }

    @Override
    public void removeMap(MapObject mapObject) {

    }

    private void  onRefreshBoss(MapObject mapObject){

    }

    private void onGameOver(MapObject mapObject) {
        mapObject.setPkState(MapDefine.PK_STATE0);//不能PK
        NineDaysFocusedManager.getInstance().getBossHurtList().get(mapObject.getId()).clear();
        NineDaysFocusedManager.getInstance().getBossHurtList().remove(mapObject.getId());
        mapObject.addMapOnceScriptEventTimer(getId(), "OnCloneClose", 20000);

        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTEND); //超时结束
    }

    private void onCloneClose(MapObject mapObject) {
        mapObject.setStop(true);
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTREWARDEND); //结算完成
        LOG.info(mapObject.getName() + "副本关闭");
    }

    private void sendMessage(Player player, NineDaysTask task){
        //发送给玩家
        NineDaysFocusedMessage.ResRefreshTask.Builder msg_0 =  NineDaysFocusedMessage.ResRefreshTask.newBuilder();
        NineDaysFocusedMessage.TaskData.Builder taskdata = NineDaysFocusedMessage.TaskData.newBuilder();
        taskdata.setTaskID(task.getTaskID());
        taskdata.setAlreadyStage(task.getAlreadyStage());
        taskdata.setTargetStage(task.getTargetStage());
        taskdata.setIsGet(false);
        msg_0.setTaskdata(taskdata);
        MessageUtils.send_to_player( player,NineDaysFocusedMessage.ResRefreshTask.MsgID.eMsgID_VALUE, msg_0.build().toByteArray());

        //发回游戏服
        NineDaysFocusedMessage.F2GSynchrotask.Builder msg_1 =  NineDaysFocusedMessage.F2GSynchrotask.newBuilder();
        msg_1.setRoleId(player.getId());
        msg_1.setTaskdata(taskdata);
        FightClientManager.GetInstance().send_to_game(player.getIosession(), NineDaysFocusedMessage.F2GSynchrotask.MsgID.eMsgID_VALUE, msg_1.build().toByteArray());
    }
}
