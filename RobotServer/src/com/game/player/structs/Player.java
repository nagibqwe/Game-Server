package com.game.player.structs;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.bean.*;
import com.data.container.Cfg_Huaxingfabao_Container;
import com.data.struct.ReadArray;
import com.game.aiRunner.AiRunnerMgr;
import com.game.client.Client;
import com.game.cooldown.manager.CooldownManager;
import com.game.cooldown.structs.CooldownTypes;
import com.game.copymap.struct.CopyMapType;
import com.game.equip.struct.Equip;
import com.game.equip.struct.EquipPart;
import com.game.manager.Manager;
import com.game.manager.YedMgr;
import com.game.map.structs.*;
import com.game.nature.struct.Nature;
import com.game.nature.struct.NatureType;
import com.game.pet.struct.Pet;
import com.game.player.ai.AiMoveTo;
import com.game.server.RobotServer;
import com.game.server.worker.SendMessWorker;
import com.game.skill.config.SkillEventContainer;
import com.game.skill.config.SkillVisual;
import com.game.skill.structs.Skill;
import com.game.skill.structs.SkillDefine;
import com.game.structs.AttributeType;
import com.game.structs.Config;
import com.game.structs.Position;
import com.game.task.structs.GuildTask;
import com.game.task.structs.MainTask;
import com.game.team.manager.TeamManager;
import com.game.team.structs.Team;
import com.game.utils.ICallback;
import com.game.utils.MapUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import com.game.yed.YedAI;
import game.core.message.SMessage;
import game.core.util.TimeUtils;
import game.message.ChatMessage.ChatReqCS;
import game.message.ChatMessage.paramStruct;
import game.message.*;
import game.message.EquipMessage.ReqAutoResolveSet;
import game.message.FightMessage.ReqUseSkill;
import game.message.MapMessage.ReqGather;
import game.message.MapMessage.ReqGetMonsterPos;
import game.message.MapMessage.ReqMoveTo;
import game.message.MapMessage.ReqStopMove;
import game.message.TeamMessage.ReqCreateTeam;
import game.message.TeamMessage.ReqTeamOpt;
import game.message.WelfareMessage.ReqExchangeGift;
import game.message.ZoneMessage.ReqEnterZone;
import game.message.backpackMessage.ItemInfo;
import game.message.heartMessage.ReqHeart;
import game.message.heartMessage.ReqReallyHeart;
import game.message.taskMessage.ReqChangeTaskState;
import game.message.taskMessage.ReqTaskFinish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luck
 */
public class Player extends BaseNpc {
    /**
     * 服务器同步范围直径,应该比服务器的要大些,服务器是方形,这里计算可能会用圆形(直接用dis来算)
     */
    public final static double SERVER_SYNC_DIAMETER = 10;
    /**
     * 玩家Pk模式枚举  0为和平，1为组队，2为帮派，3为全体, 4阵营
     */
    public static final int PkStateAll = 3;
    private final static Logger log = LogManager.getLogger(Player.class);
    private final static Logger logger = LogManager.getLogger("flow");
    private static final long refreshTime = 0 * 24 * 60 * 60 * 1000;        //凌晨5点刷新次数
    public final static int MapObject_NPC = 0;
    public final static int MapObject_Monster = 1;
    public final static int MapObject_Gather = 2;
    private final Logger ailogger = YedMgr.getInstance().getAiLogger();
    private final Position randPos = new Position(135, 85);
    private final Map<Integer, Long> attributes = new HashMap<>();
    /**
     * 副本地图信息
     */
    private final List<CloneMap> cloneMaps = new ArrayList<>();
    /**
     * 当前地图NPC
     */
    private final ConcurrentHashMap<Long, BaseNpc> npcs = new ConcurrentHashMap<>();
    /**
     * 背包的道具
     */
    private final ConcurrentHashMap<Long, CellItem> bags = new ConcurrentHashMap<>();
    /**
     * 背包的装备
     */
    private final ConcurrentHashMap<Long, Equip> bagEquips = new ConcurrentHashMap<>();
    /**
     * 身上的装备
     */
    private EquipPart[] bodyEquips = new EquipPart[8];

    /**
     * 装备部位信息同步标识
     */
    private boolean isEquipInit = false;
    /**
     * 熔炼之后回收的装备ID集合
     */
    private List<Long> rmEquipList = new ArrayList<>();
    /**
     * 技能列表
     */
    private final List<Skill> skills = new ArrayList<>();
    /**
     * 可用技能列表
     */
    private final List<Skill> canUseSkills = new ArrayList<>();
    /**
     * 造化系统信息
     */
    private final ConcurrentHashMap<Integer, Nature> natures = new ConcurrentHashMap<>();
    /**
     * 当前主线任务信息
     */
    private MainTask mainTask = new MainTask();
    /**
     * 当前公会任务
     */
    private GuildTask guildTask = new GuildTask();

    private Pet pet = new Pet();
    /**
     * 宠物信息
     */
    private final ConcurrentHashMap<Long, MapPet> pets = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Long, OtherMoveTo> mvlist = new ConcurrentHashMap<>();

    /**
     * BOSS收益次数
     */
    private final ConcurrentHashMap<Integer, Integer> bossCountMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, Boolean> functionState = new ConcurrentHashMap<>();//系统开放列表

    /**
     * 玩家消息列表
     */
    private final List<Integer> messNum = new ArrayList<>();
    // 是否已经收到了角色信息
    private boolean isRecvedBaseInfo;
    private int range = 10;//随机范围大小
    private int pkState = 0;//pk状态值
    private String accessToken = "1";
    private String machineCode = "2";
    private String platformName = "PC";
    private String FuncellUUid = "";
    private long userId;//账号id
    private long rUserId;//机器人账号ID
    private int level;//角色等级
    private int career;//角色职业
    private int stateLevel;//角色境界等级

    private long lastHeartSendTime = 0;
    private long lastHeartLogTime = 0;
    private long lastChatTime;
    private int tomapId = 0;
    private long doTime = TimeUtils.Time();//事件执行时间,单位毫秒
    private List<RegisterMessage.RoleBaseInfo> roleInfoList;
    private boolean isLoginSucess = false;
    /**
     * 角色当前地图信息
     */
    private RegisterMessage.ResPlayerMapInfo mapInfo;
    /**
     * 之前的地图模型ID
     */
    private int oldMapModelId;
    /**
     * 位面怪物刷新波数
     */
    private int planeLoop = 1;
    /**
     * 移动等待时间
     */
    private long moveWaitTime;

    /**
     * 行为等待时间
     */
    private final ConcurrentHashMap<Integer, Map<Integer,Long>> waitMap = new ConcurrentHashMap<>();

    private int createRoleRet = 0;
    /**
     * 事件类型
     */
    private int eventType = EventDefine.Event_RandMove;
    /**
     * 玩家上次事件
     */
    private int lastEventType = EventDefine.Event_RandMove;

    private IoSession session;
    /**
     * 退出游戏时间
     */
    private long quitTime;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Skill curSkill;

    //当前出战宠物id
    private int birth = 0;
    private Position mvTo = null;
    private int mvstate = 0;
    private int newprocess = 1;
    private Position moveTo = new Position();
    private int moveTotal = 0;
    private int gatherTotal = 0;
    private boolean isRead = false;
    //
    private boolean cloneState = false;
    private int moveState = 2;
    private int attackState = 0;
    private YedAI runningAi;
    // TODO
    private boolean reConnect = true;
    private Client client;
    private AiMoveTo movingAi;
    private boolean isheartSend = false;
    private long lastSendtime = System.currentTimeMillis();

    // 当前寻找怪物
    private int curMonsterId = 0;
    private long curMoveTime = 0;


    /**
     * 超出距离
     */
    private boolean overlen = false;
    private transient long lastMoveTime = 0L;

    public Player() {
        for (int i = 0; i < this.bodyEquips.length; i++) {
            this.bodyEquips[i] = new EquipPart();
            this.bodyEquips[i].setType(i);
        }
    }

    public void init() {
        tryReconnect();
    }

    public List<RegisterMessage.RoleBaseInfo> getRoleInfoList() {
        return roleInfoList;
    }

    public void setRoleInfoList(List<RegisterMessage.RoleBaseInfo> info) {
        roleInfoList = info;
        isLoginSucess = roleInfoList != null;
    }

    public MainTask getMainTask() {
        return mainTask;
    }

    public GuildTask getGuildTask() {
        return guildTask;
    }

    public boolean isOverlen() {
        return overlen;
    }

    public void setOverlen(boolean overlen) {
        this.overlen = overlen;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void setMapInfo(RegisterMessage.ResPlayerMapInfo msg) {
        mapInfo = msg;
    }

    public int getPlaneLoop() {
        return planeLoop;
    }

    public void setPlaneLoop(int planeLoop) {
        this.planeLoop = planeLoop;
    }

    public ConcurrentHashMap<Long, CellItem> getBags() {
        return bags;
    }

    public ConcurrentHashMap<Long, Equip> getBagEquips() {
        return bagEquips;
    }

    public EquipPart[] getBodyEquips() {
        return bodyEquips;
    }

    public void setBodyEquips(EquipPart[] bodyEquips) {
        this.bodyEquips = bodyEquips;
    }

    public List<Long> getRmEquipList() {
        return rmEquipList;
    }

    public void setRmEquipList(List<Long> rmEquipList) {
        this.rmEquipList = rmEquipList;
    }

    public boolean isEquipInit() {
        return isEquipInit;
    }

    public void setEquipInit(boolean equipInit) {
        isEquipInit = equipInit;
    }

    public ConcurrentHashMap<Integer, Nature> getNatures() {
        return natures;
    }

    public void setCreateRoleRet(int reason) {
        createRoleRet = reason;
    }

    public int getLastEventType() {
        return lastEventType;
    }

    public void setLastEventType(int lastEventType) {
        this.lastEventType = lastEventType;
    }


    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public int getBirth() {
        return birth;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }

    public List<CloneMap> getCloneMaps() {
        return cloneMaps;
    }

    public void setQuitTime(long quitTime) {
        this.quitTime = quitTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getrUserId() {
        return rUserId;
    }

    public void setrUserId(long rUserId) {
        this.rUserId = rUserId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getFuncellUUid() {
        return FuncellUUid;
    }

    public void setFuncellUUid(String funcellUUid) {
        FuncellUUid = funcellUUid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public int getStateLevel() {
        return stateLevel;
    }

    public void setStateLevel(int stateLevel) {
        this.stateLevel = stateLevel;
    }

    public int getMapModelId() {
        return mapModelId;
    }

    public void setMapModelId(int mapModelId) {
        this.mapModelId = mapModelId;
    }

    public boolean isRecvedBaseInfo() {
        return isRecvedBaseInfo;
    }

    public void setRecvedBaseInfo(boolean recvedBaseInfo) {
        isRecvedBaseInfo = recvedBaseInfo;
    }

    @Override
    protected void onForceStopMove() {
        if (movingAi != null) {
            AiRunnerMgr.getInstance().pushRemove(movingAi);
        }
        mvstate = 0;
    }

    @Override
    public float getSpeed() {
        return (float) getAttributeValue(AttributeType.MoveSpeedFinal) / 100.0f;
    }

    public void setRandPos(float x, float y) {
        randPos.setX(x);
        randPos.setY(y);
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getTomapId() {
        return tomapId;
    }

    public void setTomapId(int tomapId) {
        this.tomapId = tomapId;
    }

    public MapPeople getMapPeopleById(long id) {
        BaseNpc npc = npcs.get(id);
        return (npc instanceof MapPeople) ? (MapPeople) npc : null;
    }

    public void addMapPeople(MapPeople people) {
        npcs.put(people.getId(), people);
    }

    public int getPkState() {
        return pkState;
    }

    public void setPkState(int pkState) {
        this.pkState = pkState;
    }

    public MapMonster getMapMonsterById(long id) {
        BaseNpc npc = npcs.get(id);
        return (npc instanceof MapMonster) ? (MapMonster) npc : null;
    }

    public void addMapMonster(MapMonster monster) {
        npcs.put(monster.getId(), monster);
    }

    public MapNPC getMapNPCById(long id) {
        BaseNpc npc = npcs.get(id);
        return (npc instanceof MapNPC) ? (MapNPC) npc : null;
    }

    public void addMapNPC(MapNPC mapNpc) {
        npcs.put(mapNpc.getId(), mapNpc);
    }

    public void removeNpc(long id) {
        npcs.remove(id);
    }

    public MapGather getMapGatherById(long id) {
        BaseNpc npc = npcs.get(id);
        return (npc instanceof MapGather) ? (MapGather) npc : null;
    }

    public void addMapGather(MapGather mapGather) {
        npcs.put(mapGather.getId(), mapGather);
    }

    public MapPet getMapPetById(long id) {
        BaseNpc npc = npcs.get(id);
        return (npc instanceof MapPet) ? (MapPet) npc : null;
    }

    public void addMapPet(MapPet mapPet) {
        pets.put(mapPet.getId(), mapPet);
    }

    public ConcurrentHashMap<Integer, Integer> getBossCountMap() {
        return bossCountMap;
    }

    public ConcurrentHashMap<Long, BaseNpc> getNpcs() {
        return npcs;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public ConcurrentHashMap<Integer, Map<Integer, Long>> getWaitMap() {
        return waitMap;
    }

    public IoSession getSession() {
        return session;
    }

    public void setSession(IoSession session) {
        this.session = session;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
        log.info(getInfo()+"切换状态》》》"+eventType);
    }

    public void setAttributeValue(int type, long value) {
        attributes.put(type, value);
    }

    public long getMoveWaitTime() {
        return moveWaitTime;
    }

    public void setMoveWaitTime(long moveWaitTime) {
        this.moveWaitTime = moveWaitTime;
    }

    public long getAttributeValue(int type) {
        if (!attributes.containsKey(type)) {
            return 0L;
        }
        return attributes.get(type);
    }

    public void sendHeartToServer(long time) {
        if (lastHeartSendTime != 0 && time - lastHeartSendTime >= 11 * 1000 && isRecvedBaseInfo) {
            //只有等收到消息后再来计时，否则容易出现问题
            if (isheartSend) {
                if (time - lastHeartLogTime >= 10 * 1000) {
                    if (time - lastHeartSendTime > 30000 && RobotServer.getInstance().debugSessionId == session.getId()) {
                        System.out.println(String.format("%d 补发一个心跳消息 超时:%d", getId(), time - lastHeartSendTime));

                        ReqHeart.Builder msg = ReqHeart.newBuilder();
                        msg.setTime((int) (time / 1000));

                        session.setAttribute("补发一个心跳消息");
                        SendMessWorker w = new SendMessWorker(session, new SMessage(ReqHeart.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
                        w.run();
                        ReqReallyHeart.Builder mm = ReqReallyHeart.newBuilder();
                        w = new SendMessWorker(session, new SMessage(ReqReallyHeart.MsgID.eMsgID_VALUE, mm.build().toByteArray()));
                        w.run();
                    }
                    if (time - lastHeartSendTime > 61119 && RobotServer.getInstance().debugSessionId == 0) {
                        RobotServer.getInstance().debugSessionId = session.getId();
                        RobotServer.getInstance().debugRoleId = getId();
                        String info = String.format("%d 超时严重,补发的都没收到回来的,设置一个调试值:%d %s", getId(), time - lastHeartSendTime, session.getLocalAddress());
                        logger.error(info);
                        System.out.println(info);
                    }
                    lastHeartLogTime = time;
                }
                return;
            }
            isheartSend = true;

            //if (!TimeUtils.isIDEEnvironment()) {
                //log.info(this.getInfo() + "心跳:" + time + ", 上次:" + lastHeartSendTime);
            //}

            ReqHeart.Builder msg = ReqHeart.newBuilder();
            msg.setTime((int) (time / 1000));
            sendMsg(ReqHeart.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            ReqReallyHeart.Builder mm = ReqReallyHeart.newBuilder();
            sendMsg(ReqReallyHeart.MsgID.eMsgID_VALUE, mm.build().toByteArray());
        }
    }

    public long getLastHeartSendTime() {
        return lastHeartSendTime;
    }

    public void setlastHeartSendTime(long lastHeartSendTime) {
        isheartSend = false;
        this.lastHeartSendTime = lastHeartSendTime;
    }

    public void setHeart() {
        isheartSend = true;
    }

    //领取激活码
    public void getActiveCode(String code) {
        ReqExchangeGift.Builder msg = ReqExchangeGift.newBuilder();
        msg.setId(code);
        sendMsg(ReqExchangeGift.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void timeTick(long time) {
        try {
            if (quitTime != 0 && time >= quitTime) {
                Manager.registerManager.deal().quitGame(this, false);
                return;
            }
            if (this.getId() == 0) {
                return;
            }
            if (time < doTime) {
                return;
            }
            if (canMove(time)) {
                return;
            }


            int afterTime = 5000;
            switch (eventType) {
                case EventDefine.Event_FINDMONSTER:
                    ReqGetMonsterPos.Builder req = ReqGetMonsterPos.newBuilder();
                    sendMsg(ReqGetMonsterPos.MsgID.eMsgID_VALUE, req.build().toByteArray());
                    afterTime = 5000;
                    break;
                case EventDefine.Event_RandMove:
                    afterTime = randMove(curPos);
                    lastMoveTime = time;
                    break;
                case EventDefine.Event_MoveStop:
                    moveStop();
//                    doEquipSmelt();
//                    checkWearEquip();
                    break;
                case EventDefine.Event_MainTask:
                    afterTime = Manager.taskManager.mainTask().doMainTask(this);

                    break;
                case EventDefine.Event_RandAttackMonster:
                    afterTime = attack(curPos, 0);
                    break;
                case EventDefine.Event_Chat:
                    afterTime = randChat(true);
                    break;
                case EventDefine.Event_AreaMove:
                    afterTime = randMove(randPos);
                    break;
                case EventDefine.Event_AreaAttackMonster:
                    afterTime = attack(randPos, 0);
                    break;
                case EventDefine.Event_CopyMap:
//                    afterTime = finishCopyMap();
                    //随机发送一次聊天
//                    randSendChat();

                    afterTime = findRandomAndAttack(afterTime);
                    break;
                case EventDefine.Event_EnterMap_RandAttackMonster:
                    afterTime = attackMapMonster(curPos, 0);
                    break;
                case EventDefine.Event_RandPK: {
                    //首先切换地图
                    if (this.tomapId > 0 && this.mapModelId != this.tomapId) {
                        chatGM("&entermap " + tomapId);
                        //发送进入地图命令
                        afterTime = 30000;//60秒等待
                    } else if (this.level < 10) {
                        chatGM("&setlevel 35");
                        afterTime = 10 * 1000;
                    } else if (this.getPkState() != PkStateAll) {
                        PlayerMessage.ReqUpdataPkState.Builder msg = PlayerMessage.ReqUpdataPkState.newBuilder();
                        msg.setPkState(PkStateAll);
                        sendMsg(PlayerMessage.ReqUpdataPkState.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                        afterTime = 10 * 1000;//30秒后处理
                    } else {
                        //随机发送一次聊天
                        randSendChat();
                    }
                }
                break;
                case EventDefine.Event_OneKeyAtt: {
                    chatGM("&oneKeyAtt");
                    //回到上次时间内容
                    eventType = lastEventType;
                    afterTime = 15 * 1000;
                    break;
                }
                case EventDefine.Event_Quit: {
                    RegisterMessage.ReqQuit.Builder msg = RegisterMessage.ReqQuit.newBuilder();
                    sendMsg(RegisterMessage.ReqQuit.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                    setId(0);
                    break;
                }
                case EventDefine.Event_CopymapLogic: {//执行副本逻辑
                    afterTime = dealCopyMapLogic();
                    break;
                }

                case EventDefine.Event_Boss_100: {
                    afterTime = bossTest_lookMonster();
                    break;
                }

                case EventDefine.Event_Boss_101: {
                    afterTime = bossTest_lookMonsterMove();
                    break;
                }

                case EventDefine.Event_Boss_102: {
                    afterTime = bossTest_lookMonsterAttack();
                    break;
                }
                //巅峰竞技匹配
                case EventDefine.Event_PeakMatch: {
                    game.message.PeakMessage.ReqEnterPeakMatch.Builder msg = game.message.PeakMessage.ReqEnterPeakMatch.newBuilder();
                    sendMsg(game.message.PeakMessage.ReqEnterPeakMatch.MsgID.eMsgID_VALUE,msg.build().toByteArray());
                    afterTime = 30000;
                    break;
                }


                default:
                    break;
            }

            if (afterTime < 1) {
                afterTime = 500;
            }

            doTime = time + afterTime;
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private int dealCopyMapLogic() {
        int afterTime = 1000;
        int mapId = getMapModelId();
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapId);
        if (bean == null) {
            log.error("Cfg_Clone_map_Bean配置表数据错误，id="+mapId);
            return afterTime;
        }

        switch (bean.getType()){
            case CopyMapType.DreamBoss_CopyMap://世界BOSS

                break;
        }


        return afterTime;
    }

    /**
     * `机器人`是否在运动中
     * @param now
     * @return
     */
    private boolean canMove(long now) {
        long speedTime = now - lastMoveTime;
        lastMoveTime = now;
        if (moveTick(speedTime)) {
            moveStop();
            return false;
        }
        return true;
    }

    private void AiUpdate() {
        if (runningAi == null) {
            return;
        }

        long now = TimeUtils.Time();
        if (runningAi.lastUpdateTime <= 0) {
            runningAi.lastUpdateTime = now;
        }
        runningAi.Update(now - runningAi.lastUpdateTime);
        runningAi.lastUpdateTime = now;
    }

    //采集
    public void gather(MapGather p) {
        Cfg_Gather_Bean gatherbean = CfgManager.getCfg_Gather_Container().getValueByKey(p.getModelId());
        if (gatherbean != null) {
            CooldownManager.getInstance().addCooldown(this, CooldownTypes.AI_GATHER_CD, String.valueOf(gatherbean.getId()), gatherbean.getCollect_time()+ 1000, CooldownManager.REPLACE);
        }
        log.info(this.getInfo() + "请求收集,采集物ID=" + p.getId() + ",采集物："+gatherbean.getName() + ",采集物模型ID=" + p.getModelId());

        ReqGather.Builder msg = ReqGather.newBuilder();
        msg.setId(p.getId());
        sendMsg(ReqGather.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 先在同步过来的对象里面查找对象,找到的话调用action,并将对象传给action; 否则在场景配置里面查找同步范围外的对象,找到后返回对应pos
     *
     * @param t
     * @param modelid 如果是0则随便找一个就行
     * @param mapid
     * @param mindis
     * @param action
     * @return
     */
    public <T extends BaseNpc> int doSomeForTarget(Class<T> t, int modelid, int mapid, double mindis, ICallback action, Position targetpos, boolean isRandom) {
        float error = 9999999;
        targetpos.setX(error);
        Object target = isRandom ? findRandomTarget(t, modelid, mapid, mindis) : findTarget(t, modelid, mapid, mindis);
        if (target == null) {
            ailogger.error(String.format("ai debug role:%d doSomeForTarget 场景找不到可合适的对象 key:%d mapid:%d class:%s", getId(), modelid, mapid, t.getName()));
            return 1;
        }

        if (t.isInstance(target)) {
            targetpos.setX(((BaseNpc) target).getCurPos().getX());
            targetpos.setY(((BaseNpc) target).getCurPos().getY());
            double dis = MapUtils.getDistance(targetpos, curPos);
            if (dis <= mindis) {
                action.Invoke(target);
                return -1;
            }
        } else if (target instanceof MapGather) {
            targetpos.setX(((ByteMapItem) target).getMapX());
            targetpos.setY(((ByteMapItem) target).getMapY());
            double dis = MapUtils.getDistance(targetpos, curPos);
            if (dis <= mindis) {
                action.Invoke(target);
                return -1;
            }
        } else if (target instanceof ByteMapItem) {
            targetpos.setX(((ByteMapItem) target).getMapX());
            targetpos.setY(((ByteMapItem) target).getMapY());
            double dis = MapUtils.getDistance(targetpos, curPos);
            if (dis <= mindis) {
                action.Invoke(target);
                return -1;
            }
        }
        if (targetpos.getX() == error) {
            ailogger.error(String.format("ai debug role:%d doSomeForTarget 场景找的对象类型不对 key:%d mapid:%d class:%s", getId(), modelid, mapid, t.getName()));
            return 2;
        }
        return 0;
    }

    private <T extends BaseNpc> Object findTarget(Class<T> t, int modelid, int mapid, double mindis) {
        Object target = null;
        if (mapModelId == mapid) {
            double lastdis = 10000000;
            // 先从附近同步过来的对象里面找
            for (BaseNpc npc : npcs.values()) {
                if (!t.isInstance(npc)) {
                    continue;
                }
                if (npc.getModelId() == modelid || modelid == 0) {
                    double dis = MapUtils.getDistance(npc.getCurPos(), curPos);
                    if (dis < mindis) {
                        return npc;
                    } else {
                        if (dis < lastdis) {
                            lastdis = dis;
                            target = npc;
                        }
                    }
                }
            }
            if (target == null) {
                // 再从地图配置里面找同步范围外的
                target = findNearTargetInCfg(modelid, t, SERVER_SYNC_DIAMETER);
            }
        }

        return target;
    }

    private <T extends BaseNpc> Object findRandomTarget(Class<T> t, int modelid, int mapid, double mindis) {
        List randomList = new ArrayList();
        if (mapModelId == mapid) {
            double lastdis = 10000000;
            // 先从附近同步过来的对象里面找
            for (BaseNpc npc : npcs.values()) {
                if (!t.isInstance(npc)) {
                    continue;
                }
                if (npc.getModelId() == modelid || modelid == 0) {
                    double dis = MapUtils.getDistance(npc.getCurPos(), curPos);
                    if (dis < mindis) {
                        randomList.add(npc);
                        continue;
                    } else {
                        if (dis < lastdis) {
                            lastdis = dis;
                            randomList.add(npc);
                        }
                    }
                }
            }
            // 再从地图配置里面找同步范围外的
            if (randomList.isEmpty()) {
                List<ByteMapItem> mapTargets = findRandomTargetListInCfg(modelid, t, SERVER_SYNC_DIAMETER);
                if (mapTargets != null&&!mapTargets.isEmpty()) {
                    randomList.addAll(mapTargets);
                }
            }
        }

        if (randomList.isEmpty()) {
            return null;
        }

        return randomList.get(RandomUtils.random(randomList.size()));
    }

    /**
     * 找到给定范围之外的配置里的最近的对象
     *
     * @param modelid
     * @param t
     * @param minoutdis
     * @return
     */
    private List<ByteMapItem> findRandomTargetListInCfg(int modelid, Class<?> t, double minoutdis) {
        ByteMapCfg cfg = Manager.mapCfgManager.getMapCfg(mapModelId);
        if (cfg == null) {
            return null;
        }

        ArrayList<ByteMapItem> targetList = null;
        if (t == MapGather.class) {
            targetList = cfg.getCollectCfg();
        } else if (t == MapMonster.class) {
            targetList = cfg.getMonsterCfg();
        } else if (t == MapNPC.class) {
            targetList = cfg.getNpcCfg();
        }
        if (targetList == null) {
            return null;
        }

//        Position p = new Position();
//        double mindis = 1000000;
//        ByteMapItem ret = null;
//        for (ByteMapItem obj : targetList) {
//            if (obj.getId() == modelid || modelid == 0) {
//                p.setX(obj.getMapX());
//                p.setY(obj.getMapY());
//                double dis = MapUtils.getDistance(p, curPos);
//                if (dis < mindis && minoutdis < dis) {
//                    ret = obj;
//                    mindis = dis;
//                }
//            }
//        }
        return targetList;
    }

    /**
     * 找到给定范围之外的配置里的最近的对象
     *
     * @param modelid
     * @param t
     * @param minoutdis
     * @return
     */
    private ByteMapItem findNearTargetInCfg(int modelid, Class<?> t, double minoutdis) {
        ByteMapCfg cfg = Manager.mapCfgManager.getMapCfg(mapModelId);
        if (cfg == null) {
            return null;
        }

        ArrayList<ByteMapItem> tarlist = null;
        if (t == MapGather.class) {
            tarlist = cfg.getCollectCfg();
        } else if (t == MapMonster.class) {
            tarlist = cfg.getMonsterCfg();
        } else if (t == MapNPC.class) {
            tarlist = cfg.getNpcCfg();
        }
        if (tarlist == null) {
            return null;
        }
        Position p = new Position();
        double mindis = 1000000;
        ByteMapItem ret = null;
        for (ByteMapItem obj : tarlist) {
            if (obj.getId() == modelid || modelid == 0) {
                p.setX(obj.getMapX());
                p.setY(obj.getMapY());
                double dis = MapUtils.getDistance(p, curPos);
                if (dis < mindis && minoutdis < dis) {
                    ret = obj;
                    mindis = dis;
                }
            }
        }
        return ret;
    }

    /**
     * 移动到当前地图某个位置
     */
    public void moveToCurMapPos(int targetMapId, int targetNpcId) {
        int mapDataId = targetMapId;
        int npcId = targetNpcId;
        ByteMapCfg mapCfg = Manager.mapCfgManager.getMapCfg(this.mapModelId);
        if (mapCfg == null) {
            return;
        }
        if (this.mapModelId == mapDataId) {
            Position pos = null;
            for (ByteMapItem npc : mapCfg.getNpcCfg()) {
                if (npc.getId() == npcId) {
                    pos = new Position(npc.getMapX(), npc.getMapY());
                    break;
                }
            }
            if (pos == null) {
                return;
            }
            if (this.curPos.getX() == pos.getX() && this.curPos.getY() == pos.getY()) {
                return;
            }

            moveAi_MoveToPos(pos);
        } else {
            log.error(this.getInfo() + "地图(" + this.mapModelId + ")与目标地图(" + mapDataId + ")不一致");
        }
    }

    /**
     * 移动到某个地图某对象位置
     * @param mapDataId  地图模型ID
     * @param objectId 目标模型ID
     * @param objectType 目标类型
     */
    public void moveToMapObject(int mapDataId, int objectId, int objectType) {
        if(this.getMapModelId() != mapDataId){
            Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(this.getMapModelId());
            if(mapBean == null){
                log.error("没有找到地图数据,ID："+this.getMapModelId());
                return;
            }
            if(mapBean.getType() == 0){//在世界地图中
                reqTransportControl(1,mapDataId);
                return;
            } else {//在副本中,要先退出副本
                reqCopyMapOut();
                return;
            }
        }
        ByteMapCfg endCfg = Manager.mapCfgManager.getMapCfg(mapDataId);
        if (endCfg == null) {
            return;
        }
        List<ByteMapItem> mapItemList = null;
        switch (objectType) {
            case MapObject_NPC:
                mapItemList = endCfg.getNpcCfg();
                break;
            case MapObject_Monster:
                mapItemList = endCfg.getMonsterCfg();
                break;
            case MapObject_Gather:
                mapItemList = endCfg.getCollectCfg();
                break;
            default:
                break;
        }
        if (mapItemList == null) {
            return;
        }
        Position p = null;
        for (ByteMapItem object : mapItemList) {
            if (object.getId() == objectId) {
                p = new Position(object.getMapX(), object.getMapY());
                break;
            }
        }
        if (p == null) {
            log.error(String.format("player debug %d 移动到[%d]地图上的[%d]对象[%d]时在配置里没有找到对象", getId(), mapDataId, objectType, objectId));
            return;
        }
        moveAi_MoveToPos(p);
//        moveToPos(mapdataId, p);
    }

    /**
     * 请求完成主线任务
     */
    private void sendFinishMainTask() {
        ReqTaskFinish.Builder msg = ReqTaskFinish.newBuilder();
        msg.setModelId(mainTask.getNowTaskId());
        msg.setRewardPer(1);
        msg.setTaskId(mainTask.getInstanceId());
        msg.setType(0);
        sendMsg(ReqTaskFinish.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        log.info(this.getInfo() + "提交主线任务，任务id" + this.getMainTask().getInstanceId() + " 任务modelId:" + this.getMainTask().getNowTaskId());
    }

    /**
     * 获取可以使用的技能
     *
     * @return
     */
    private Skill getUseSkill() {
        if (curSkill != null) {
            return curSkill;
        }
        if (!canUseSkills.isEmpty()) {
            curSkill = canUseSkills.remove(0);
            return curSkill;
        }

        Cfg_Skill_Bean bean;
        SkillVisual sv;
        for (Skill skill : skills) {
            bean = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getId());
            if (bean == null) {
                continue;
            }
            sv = SkillEventContainer.getInstance().GetSkillVisualBySV(bean.getVisualDef());
            if (sv == null) {
                continue;
            }

            if (bean.getType() != SkillDefine.SkillType_Active) {
                continue;
            }
            if (skill.getNextUseTime() <= TimeUtils.Time()) {
                canUseSkills.add(skill);
            }
        }

        //检查法宝技能
//        checkFaBaoSkill();

        if (canUseSkills.isEmpty()) {
            return null;
        }
//        curSkill = canUseSkills.remove(0);
        curSkill = canUseSkills.remove(RandomUtils.random(canUseSkills.size()));
        return curSkill;
    }

    public void checkFaBaoSkill() {
        Nature nature = this.natures.get(NatureType.STIFLEFFABAO);
        if (nature == null) {
            return;
        }
        Cfg_Huaxingfabao_Bean fabaoBean = Cfg_Huaxingfabao_Container.GetInstance().getValueByKey(nature.getCurModelId());
        if (fabaoBean == null) {
            return;
        }

        //角色是否存在该技能,存在则跳过检查
        for (Skill skill : this.skills) {
            if (skill.getId() == fabaoBean.getUse_skill()) {
                return;
            }
        }

        Cfg_Skill_Bean skBean = CfgManager.getCfg_Skill_Container().getValueByKey(fabaoBean.getUse_skill());
        if (skBean == null) {
            return;
        }

        SkillVisual sv = SkillEventContainer.getInstance().GetSkillVisualBySV(skBean.getVisualDef());
        if (sv == null) {
            return;
        }

        if (skBean.getType() != SkillDefine.SkillType_Active) {
            return;
        }

        //角色技能列表中没有法宝技能，加上去
        Skill skill = new Skill();
        skill.setId(skBean.getId());
        skill.setLevel(skBean.getLevel());
        this.skills.add(skill);
        this.canUseSkills.add(skill);
    }

    //获取攻击对象
    private void getEnemys(SkillVisual sv, Position move, Position dir, List<Long> enemys, int monsterId) {
        //检测位移技能
        for (BaseNpc npc : npcs.values()) {
            if (enemys.size() >= sv.getMaxAttack()) {
                return;
            }
            if (npc.getCampNo() == this.birth) {
                continue;
            }
            if (!(npc instanceof MapMonster)) {
                continue;
            }
            if (Utils.getDistance(curPos, npc.getCurPos()) <= sv.getAttackDis()) {
                if (monsterId != 0 && npc.getModelId() == monsterId) {
                    enemys.add(npc.getId());
                }

                if (monsterId == 0) {
                    enemys.add(npc.getId());
                }
            }
        }
    }

    /**
     * 攻击目标
     *
     * @param pos
     * @param arg
     * @return
     */
    public int attack(Position pos, int arg) {
        Skill skill = getUseSkill();
        if (skill == null) {
            CooldownManager.getInstance().addCooldown(this, CooldownTypes.AI_DOING_CD, null, 2000, CooldownManager.LLONG);
            return 2000;
        }

        Cfg_Skill_Bean skillBean = CfgManager.getCfg_Skill_Container().getValueByKey(skill.getId());
        //获取技能效果配置
        SkillVisual sv = SkillEventContainer.getInstance().GetSkillVisualBySV(skillBean.getVisualDef());

        //执行事件
        List<Long> enemys = new ArrayList<>();
        Position move = this.curPos;
        Position adir = this.dir;
        getEnemys(sv, move, dir, enemys, arg);

        if (sv.getMaxAttack() > 0 && enemys.isEmpty()) {
            sendGetMonsterPos();
            return 1000;
        }
        long cd = skillBean.getCd() > sv.getCd()+100 ? skillBean.getCd():sv.getCd();
        skill.setNextUseTime(TimeUtils.Time() + cd);

        ReqUseSkill.Builder msg = ReqUseSkill.newBuilder();
        FightMessage.SkillBaseInfo.Builder baseInfo = FightMessage.SkillBaseInfo.newBuilder();
        baseInfo.setSerial(skill.getSerial());
        baseInfo.setSkillID(skill.getId());
        baseInfo.setUserID(id);
        baseInfo.setDirX(adir.getX());
        baseInfo.setDirY(adir.getY());
        msg.setInfo(baseInfo);

        msg.setUsePosX(move.getX());
        msg.setUsePosY(move.getY());

        msg.setCurTargetId(0);
        if (enemys != null && !enemys.isEmpty()) {
            msg.setCurTargetId(enemys.get(0));
        }
//        log.info("===============ReqUseSkill:"+msg.getInfo().getSkillID());
        sendMsg(ReqUseSkill.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        curSkill = null;
        return sv.getCd() + 5000;
    }

    //发送一个请求去最近一个怪物的坐标
    public void sendGetMonsterPos() {
        log.info(this.getInfo()+"请求离角色最近的怪");
        ReqGetMonsterPos.Builder req = ReqGetMonsterPos.newBuilder();
        sendMsg(ReqGetMonsterPos.MsgID.eMsgID_VALUE, req.build().toByteArray());
    }

    //随机聊天
    private int randChat(boolean isMove) {
        if (level < 20) {
            chatGM("&setlevel 20");
            level = 20;
            return 1000;
        }
        long now = TimeUtils.Time();
        int interval = RandomUtils.random(15000, 60000);
        if (now - lastChatTime < interval) {
//            if (isMove) {
//                return randMove(curPos);
//            }
            chat(0, 0, 0, randomNameByCarceer(this.career));
            ;
            return 1000;
        }
//        List<Cfg_serverStrBean> list = Manager.gameDataManager.Cfg_serverStrContainer.getList();
//        int index = RandomUtils.random(0, list.size() - 1);
//        Cfg_serverStrBean bean = list.get(index);
//        if (bean == null) {
//            return 0;
//        }
//        chat(0, 0, 0, bean.getStr());


        lastChatTime = now;
        return 1000;
    }

    //根据职业随机一个名字
    private String randomNameByCarceer(int career) {
        Cfg_Robotrandomname_Bean[] beans = CfgManager.getCfg_Robotrandomname_Container().getValuees();
        int sex = 0;
        switch (career) {
            case 0:
                sex = 2;
                break;
            case 1:
                sex = 3;
                break;
            case 5:
            case 2:
            case 3:
            case 4:
        }
        List<String> names = new ArrayList<>();//名字
        List<String> surnames = new ArrayList<>();//姓氏
        for (Cfg_Robotrandomname_Bean bean : beans) {
            if (bean.getQ_type() == 1) {
                surnames.add(bean.getQ_value());
                continue;
            }
            if (bean.getQ_type() == sex) {
                names.add(bean.getQ_value());
            }
        }
        String name = names.get(org.apache.commons.lang.math.RandomUtils.nextInt(names.size()));
        String surname = surnames.get(org.apache.commons.lang.math.RandomUtils.nextInt(surnames.size()));
        return name + surname;
    }

    //随机发送一次聊天
    private void randSendChat() {
        int value = 6000;
        int offset = RandomUtils.random(10000);
        if (offset < value) {
            randChat(false);
        }
    }

    /**
     * 完成当前地图副本
     */
    private int finishCopyMap() {
        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapModelId);
        if (bean == null) {
            return attack(curPos, 0);
        }
        if (bean.getType() == 1) {
            return attack(curPos, 0);
        }
        if (cloneMaps.isEmpty()) {
            long time = TimeUtils.getTodayPassMillis();
            if ((time >= refreshTime) && (time < refreshTime + 10 * 1000)) {
                //凌晨5点刷新之后，重新请求下副本数据
//                CopyMapMessage.ReqOpenPanel.Builder msg = CopyMapMessage.ReqOpenPanel.newBuilder();
//                msg.setPanelType(-1);
//                sendMsg(CopyMapMessage.ReqOpenPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
            return attack(curPos, 0);
        }
        CloneMap cloneMap = null;
        synchronized (cloneMaps) {
            cloneMap = cloneMaps.get(0);
        }
        if (cloneMap == null) {
            return attack(curPos, 0);
        }
        Cfg_Clone_map_Bean clone = CfgManager.getCfg_Clone_map_Container().getValueByKey(cloneMap.getCloneModelId());
        if (clone == null) {
            return attack(curPos, 0);
        }
        int now = cloneMap.getHasCount();

        reqEnterZone(clone.getId());

        log.info(this.getInfo() + " 进入副本:" + clone.getDuplicate_name());
        if (now > 1) {
            cloneMap.setHasCount(now - 1);
        } else {
            synchronized (cloneMaps) {
                cloneMaps.remove(0);
            }
        }
        return 1000;
    }

    //随机移动
    public int randMove(Position randPoss) {
        long moveSpeed = getAttributeValue(AttributeType.MoveSpeedFinal);
        if (moveSpeed < 100) {
            return 1000;
        }
        Position endPos = MapUtils.getRandomPos(randPoss, range);
        moveAi_MoveToPos(endPos);
        return 5000;
    }

    public void initPet(PetMessage.ResPetList messInfo) {
        this.pet.setModelId(messInfo.getBattlePetId());
//        this.pet.getCurPos().setX(this.curPos.getX());
//        this.pet.getCurPos().setY(this.curPos.getY());
    }

    @Override
    public void moveAi_MoveToPos(Position endpos) {
        super.moveAi_MoveToPos(endpos);
        sendMoveMsg();
        //增加宠物跟随
        if (this.pet.getModelId() > 0) {
            this.pet.followMaster(this);
        }
    }

    public void sendMoveMsg() {
        ReqMoveTo.Builder msg = ReqMoveTo.newBuilder();
        msg.setCurPos(curPos.toPosition());
        msg.setMapId(mapModelId);
        for (Position pPos : roads) {
            msg.addPosList(pPos.toPosition());
        }
        sendMsg(ReqMoveTo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void sendChangeMapMsg(int targetmapid) {
        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapModelId);
        if (bean != null) {
//            if (bean.getMapServerType() == 1) {
//                log.error(String.format("player debug %d CrossMapTran error 当前地图[%d]不能切换bean.getMapServerType() == 1", getId(), mapModelId));
//                return;
//            }
        }

//        if (IsSameRideState && !IsDriver)
//        {
//            //不是司机，不能传送
//            return false;
//        }
        MapMessage.ReqTransportControl.Builder msgb = MapMessage.ReqTransportControl.newBuilder();
        msgb.setMapID(targetmapid);
        msgb.setType(MapUtils.TransportControlType_World);
        msgb.setX(0.0f);
        msgb.setY(0.0f);
        msgb.setParam(0);
        sendMsg(MapMessage.ReqTransportControl.MsgID.eMsgID_VALUE, msgb.build().toByteArray());
    }

    public void moveToPos(int mapid, Position endPos) {
        CooldownManager.getInstance().addCooldown(this, CooldownTypes.AI_DOING_CD, null, 2000, CooldownManager.LLONG);
        if (movingAi != null) {
            movingAi.updateData(this, mapid, endPos);
            return;
        }
        movingAi = new AiMoveTo(this, mapid, endPos);
        AiRunnerMgr.getInstance().push(movingAi);
    }

    @Override
    public void onMoveAiRemove() {
        movingAi = null;
    }

    private boolean isMovingAi() {
        return movingAi != null;
    }

    public void moveStop() {
        ReqStopMove.Builder msg = ReqStopMove.newBuilder();
        msg.setPos(curPos.toPosition());
        msg.setMapId(mapModelId);
        sendMsg(ReqStopMove.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        doTime = TimeUtils.Time() + 2000;
    }

    /**
     * @param type  0 普通 1 世界地图传送 2日常任务传送 3世界boss 4 区域npc传送 5 资源争夺战 城市争夺战传送
     * @param mapId 目标地图ID
     */
    public void reqTransportControl(int type, int mapId) {
        MapMessage.ReqTransportControl.Builder msg = MapMessage.ReqTransportControl.newBuilder();
        msg.setType(type);
        msg.setMapID(mapId);
        msg.setX(0);
        msg.setY(0);
        msg.setParam(0);
        sendMsg(MapMessage.ReqTransportControl.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        log.info(this.getInfo() + "请求传送地图:"+ mapId);
    }

    public void reqEnterZone(int modelId) {
        log.info(this.getInfo()+"请求进入位面副本："+modelId);
        ReqEnterZone.Builder msg = ReqEnterZone.newBuilder();
        msg.setModelId(modelId);
        sendMsg(ReqEnterZone.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void chat(int chattype, int chatchannel, long recRoleId, String condition) {
        ChatReqCS.Builder msg = ChatReqCS.newBuilder();
        msg.setChatchannel(chatchannel);
        msg.setChattype(chattype);
        msg.setCondition(condition);
        msg.setRecRoleId(recRoleId);
        msg.setVoiceLen(0);
        sendMsg(ChatReqCS.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        log.info(this.getInfo() + "请求GM命令：" + condition);
    }

    public void chatGM(String gm) {
        chat(0, 0, this.id, gm);
    }

    public void sendMsg(int messageId, byte[] message) {
        long now = TimeUtils.Time();
        long offset = now - lastSendtime;
        if (offset > 1000) {
            lastSendtime = now;
            messNum.clear();
        } else {
            messNum.add(messageId);
        }
        sendMsg(new SMessage(messageId, message));
    }

    public void sendMsg(SMessage message) {
        if (session == null || session.isClosing()) {
            if (reConnect) {
                tryReconnect();
            }
            return;
        }
        RobotServer.sendMess(session, message);
    }

    private void tryReconnect() {
        if (client == null) {
            client = new Client();
        }
        client.start(Config.getServerIp(), Config.getServerPort(), this);
    }

    public Client getClient(){
        return client;
    }

    /**
     * 玩家属性基本数据
     * @param messInfo
     */
    public void playerAttributeInfo(PlayerMessage.ResPlayerOnLineAttribute messInfo) {
        for (PlayerMessage.Attribute att : messInfo.getAttributeListList()) {
            this.setAttributeValue(att.getType(), att.getValue());
        }
    }

    /**
     * 玩家属性基本数据改变
     * @param messInfo
     */
    public void playerAttributeChange(PlayerMessage.ResPlayerAttributeChange messInfo) {
        for (PlayerMessage.Attribute att : messInfo.getChangeListList()) {
            this.setAttributeValue(att.getType(), att.getValue());
            this.setAttributeValue(att.getType(), att.getValue());
        }
    }

    /**
     * 对象停止移动
     * @param messInfo
     */
    public void stopMove(MapMessage.ResStopMove messInfo) {
        if (this.getId() == messInfo.getObjectId()) {
            setCurPos(new Position(messInfo.getPos().getX(), messInfo.getPos().getY()));
            forceStopMove();
        } else {
            MapPeople people = this.getMapPeopleById(messInfo.getObjectId());
            if (people != null) {
                people.setCurPos(new Position(messInfo.getPos().getX(), messInfo.getPos().getY()));
                people.forceStopMove();
                return;
            }

            MapMonster monster = this.getMapMonsterById(messInfo.getObjectId());
            if (monster != null) {
                monster.setCurPos(new Position(messInfo.getPos().getX(), messInfo.getPos().getY()));
                monster.forceStopMove();
            }

            MapPet pet = this.getMapPetById(messInfo.getObjectId());
            if (pet != null) {
                pet.setCurPos(new Position(messInfo.getPos().getX(), messInfo.getPos().getY()));
                pet.forceStopMove();
            }
        }
    }

    /**
     * 进入地图
     *
     * @param pos
     * @param i
     * @return
     */
    private int attackMapMonster(Position pos, int i) {

        if (this.mapModelId != this.tomapId) {
            // chatGM("&entermap " + tomapId);
            moveToPos(this.tomapId, pos);
            //发送进入地图命令
            return 60000;//60秒等待
        }
        return attack(pos, i);
    }

    public void waitDoTime(int timeout) {
        doTime += timeout;
    }

    /**
     * 寻怪并战斗
     *
     * @return
     */
    public int findAndAttack() {
        Position p = new Position();
        int ret = doSomeForTarget(MapMonster.class, 0, mapModelId, 3, new ICallback() {
            @Override
            public void Invoke(Object... params) {
                if (params[0] instanceof  MapMonster){
                    MapMonster m = (MapMonster) params[0];
                    attack(m.getCurPos(), 0);
                }
            }
        }, p, false);

        if (ret == 0) {//移动到目标
            double dis = MapUtils.getDistance(p, curPos);
            if (dis < 3) {
//                MoveAi_MoveToPos(p);
//                log.info(this.id+",距离较近,dis="+dis+"，移动到目标位置：X="+p.getX()+",Y="+p.getY()+",当前位置：X="+this.curPos.getX()+",Y="+this.curPos.getY()+",当前地图ID:"+this.mapModelId);
//                afterTime = 1000;
            } else {
                //有时候卡地图直接传送到目标
                long now = TimeUtils.Time();
                long offset = now - this.getMoveWaitTime();
                if (this.getMoveWaitTime() == 0) {
                    this.setMoveWaitTime(now);
                } else if (offset > 30 * 000) {
                    log.info(this.getInfo() + "移动超时，超时：" + offset + "s");
                    this.chatGM("&moveto " + p.getX() + " " + p.getY());
                    this.setMoveWaitTime(0);
                    return 5000;
                }
            }

            moveAi_MoveToPos(p);
//            log.info(this.getInfo()+"移动到目标怪：X="+p.getX()+",Y="+p.getY()+",当前位置：X="+this.curPos.getX()+",Y="+this.curPos.getY()+",当前地图ID:"+this.mapModelId);
            return 5000;
        } else if (ret == 1) {//没有找到怪,请求怪物位置，暂时随机移动
            sendGetMonsterPos();
            randMove(curPos);
            return 5000;
        } else if (ret == -1) {
            return 2000;
        }
        return 5000;
    }

    /**
     * 随机寻怪并战斗
     *
     * @param afterTime
     * @return
     */
    private int findRandomAndAttack(int afterTime) {
        Position p = new Position();
        int ret = doSomeForTarget(MapMonster.class, 0, mapModelId, 3, new ICallback() {
            @Override
            public void Invoke(Object... params) {

                if (params[0] instanceof MapMonster){
                    MapMonster m = (MapMonster) params[0];
                    attack(m.getCurPos(), 0);
                }
            }
        }, p, true);

        if (ret == 0) {//移动到目标
            double dis = MapUtils.getDistance(p, curPos);
            if (dis < 3) {
//                MoveAi_MoveToPos(p);
//                log.info(this.id+",距离较近,dis="+dis+"，移动到目标位置：X="+p.getX()+",Y="+p.getY()+",当前位置：X="+this.curPos.getX()+",Y="+this.curPos.getY()+",当前地图ID:"+this.mapModelId);
//                afterTime = 1000;
            } else {
                //有时候卡地图直接传送到目标
                long now = TimeUtils.Time();
                long offset = now - this.getMoveWaitTime();
                if (this.getMoveWaitTime() == 0) {
                    this.setMoveWaitTime(now);
                } else if (offset > 30 * 1000) {
                    log.info(this.getInfo() + "移动超时，超时：" + offset + "s");
                    this.chatGM("&moveto " + p.getX() + " " + p.getY());
                    this.setMoveWaitTime(0);
                    return 5000;
                }
            }

            moveAi_MoveToPos(p);
//            log.info(this.getInfo()+"移动到目标怪：X="+p.getX()+",Y="+p.getY()+",当前位置：X="+this.curPos.getX()+",Y="+this.curPos.getY()+",当前地图ID:"+this.mapModelId);
            afterTime = 5000;
        } else if (ret == 1) {//没有找到怪,请求怪物位置，暂时随机移动
            sendGetMonsterPos();
//            randMove(curPos);
            afterTime = 5000;
        } else if (ret == -1) {
            afterTime = 5000;
        }
        return afterTime;
    }


    /**
     * 检查怪物死亡时下一波是否可以刷新
     *
     * @param monsterId
     */
    public int checkPlaneCloneRefresh(long monsterId) {
        if (this.planeLoop > 3) {
            return 0;
        }
        log.info("对象死亡，当前地图还剩NPC个数:" + this.npcs.values().size());
        if (this.npcs.values().size() == 0) {
            log.info("周围npc都被移除了");
            if (this.planeLoop == 3) {
                return 2;
            }
            return 1;
        }

        for (BaseNpc npc : this.npcs.values()) {
            if (npc instanceof MapMonster) {
                if (npc.getId() == monsterId) {//死的怪物排除
                    continue;
                }
                return 0;
            }
        }
        if (this.planeLoop == 3) {
            return 2;
        }
        return 1;
    }

    /**
     * 位面副本1027请求刷怪
     */
    public void reqFlashMonster() {
        log.info(this.getInfo() + "请求刷怪波数=" + this.planeLoop);
        CopyMapMessage.ReqFlashMonster.Builder msg = CopyMapMessage.ReqFlashMonster.newBuilder();
        msg.setNum(this.planeLoop);
        sendMsg(CopyMapMessage.ReqFlashMonster.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void reqCopyMapOut() {
        log.info(this.getInfo() + "请求退出副本,当前地图ID:" + this.mapModelId);
        CopyMapMessage.ReqCopyMapOut.Builder msg = CopyMapMessage.ReqCopyMapOut.newBuilder();
        sendMsg(CopyMapMessage.ReqCopyMapOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //玩家组队
    public void playerTeam(int type) {
        if (this.getTeamId() == 0) {
            TeamMessage.ReqMatchAll.Builder msg = TeamMessage.ReqMatchAll.newBuilder();
//            msg.setGroupId(0);
            msg.setType(type);
            sendMsg(ReqEnterZone.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            log.info(this.getInfo() + "发起自动匹配队伍 type :" + type + "[-]");
        }
    }

    //玩家创建队伍
    public void createTeam(int type) {
        if (this.teamId != 0) {
            log.info(this.getInfo() + "创建队伍是已经存在队伍 teamId :" + this.teamId + "[-]");
        }
        ReqCreateTeam.Builder msg = ReqCreateTeam.newBuilder();
        msg.setType(type);
        sendMsg(ReqCreateTeam.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //玩家创建队伍
    public void createAutoTeam() {
        if (this.teamId != 0) {
            log.info(this.getInfo() + "创建队伍是已经存在队伍 teamId :" + this.teamId + "[-]");
        }
        log.info("玩家{}申请创建队伍", id);
        ReqCreateTeam.Builder msg = ReqCreateTeam.newBuilder();
        msg.setType(0);
        msg.setAutoAccept(true);
        sendMsg(ReqCreateTeam.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 玩家队伍操作
     * 1; 提升队长
     * 2; 踢出队伍
     * 3; 退出队伍
     * 4; 解散队伍
     *
     * @param opt
     */
    public void operateTeam(int opt) {
        if (this.teamId > 0) {
            ReqTeamOpt.Builder msg = ReqTeamOpt.newBuilder();
            msg.setOpt(opt);
            msg.setTargetId(this.id);
        } else {
            log.info(this.getInfo() + "操作队伍的时候已经不存在");
        }
    }

    /**
     * 申请加入队伍
     * @param tid
     */
    public void joinTeam(long tid) {
        log.info("玩家{}申请加入队伍", id);
        TeamMessage.ReqApplyEnter.Builder msg = TeamMessage.ReqApplyEnter.newBuilder();
        msg.setTeamId(tid);
        sendMsg(TeamMessage.ReqApplyEnter.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void setMvState(int i) {
        mvstate = i;
        gatherTotal = 0;
    }

    public void setOtherStop(MapMessage.ResStopMove messInfo) {
        BaseNpc npc = npcs.get(messInfo.getObjectId());
        if (npc == null) {
            return;
        }
        npc.cleanRoads();
        npc.setCurPos(new Position(messInfo.getPos().getX(), messInfo.getPos().getY()));
        npc.forceStopMove();
    }

    //查找移动的怪物或者人物
    public BaseNpc getMapObject(long objectId) {
        BaseNpc mo = getMapMonsterById(objectId);
        if (mo == null) {
            return getMapPeopleById(objectId);
        }
        return mo;
    }

    public void addMoveToEvent(OtherMoveTo omt) {
        //log.info("加入" + omt.getInstanceId() + "移动事件");
        mvlist.put(omt.getInstanceId(), omt);
    }

    private void moveEvent() {
        long now = TimeUtils.Time();
        Iterator<Entry<Long, OtherMoveTo>> iter = mvlist.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Long, OtherMoveTo> en = iter.next();
            BaseNpc mo = getMapObject(en.getKey());
            if (mo == null) {
                iter.remove();
                continue;
            }
            OtherMoveTo omt = en.getValue();
            if (now > omt.getEndTime()) {
                mo.setCurPos(omt.getPos());
                //log.info("删除" + omt.getInstanceId() + "移动事件");
                iter.remove();
            }
        }
    }

    //加长一下CD处理
    public void skillErrorMoveTo() {
        doTime += RandomUtils.random(2500, 7000);
    }

    public void moveTo(float x, float y) {
        Position dpos = new Position(x, y);
        moveTo = null;
        //重新设置下一次执行任务的时间
        doTime = TimeUtils.Time() + 1000 + 500;
        moveAi_MoveToPos(dpos);
        mvstate = 1;//主要是为了发停止移动
    }

    public void initNature(NatureMessage.ResNatureInfo messInfo) {
        switch (messInfo.getNatureType()) {
            case NatureType.HORSE: {//坐骑
                return;
            }
            case NatureType.STIFLEFFABAO: {//法宝
                initFaBao(messInfo);
                return;
            }
            default:
                break;
        }
    }

    /**
     * 初始化法宝信息
     *
     * @param messInfo
     */
    public void initFaBao(NatureMessage.ResNatureInfo messInfo) {
        Nature nature = this.natures.get(NatureType.STIFLEFFABAO);
        if (nature != null) {//已经有法宝信息
            return;
        }
        nature = new Nature();
        nature.setType(messInfo.getNatureType());
        nature.setCurModelId(messInfo.getNatureInfo().getModelId());
        this.natures.put(NatureType.STIFLEFFABAO, nature);

        //法宝技能检查
        this.checkFaBaoSkill();
    }

    public void reqNatureInfo(int natureType) {
        log.info(this.getInfo() + "请求获取造化系统信息，类型:" + natureType);
        NatureMessage.ReqNatureInfo.Builder msg = NatureMessage.ReqNatureInfo.newBuilder();
        msg.setNatureType(natureType);
        sendMsg(NatureMessage.ReqNatureInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void reqLeaderSitDown(boolean canGet){
        HookMessage.ReqLeaderSitDown.Builder msg = HookMessage.ReqLeaderSitDown.newBuilder();
        msg.setIsTrue(canGet);
        sendMsg(NatureMessage.ReqNatureInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //返回的错误码
    public void systemMessages(int type, List<paramStruct> message) {
        StringBuilder sb = new StringBuilder();
        for (paramStruct s : message) {
            sb.append(s.getMark()).append("、").append(s.getParamsValue()).append(",");
        }
        if (!TimeUtils.isIDEEnvironment()) {
            log.info("系统提示：" + type + " message:" + sb.toString());
        }
        switch (type) {
            case MessageString.Team_PlayerCrossState:
                break;
            case MessageString.Team_MatchFull:
                createTeam(-1);
                break;
            default:
                log.info(this.getInfo()+"收到服务器提示,type="+type+","+sb.toString());
                break;
        }
    }

    //装备分解设置
    public void equipResolveSet() {
        EquipMessage.ReqAutoResolveSet.Builder msg = EquipMessage.ReqAutoResolveSet.newBuilder();
        List<Integer> q = new ArrayList<>();
        q.add(1);
        msg.addAllQualitys(q);
        sendMsg(ReqAutoResolveSet.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //圣装分解设置
    public void holyEquipResolveSet() {
        HolyEquipMessage.ReqSetAutoResolve.Builder msg = HolyEquipMessage.ReqSetAutoResolve.newBuilder();
        msg.setIsAuto(true);
        msg.setQuality(4);
        msg.setGrade(3);
        sendMsg(HolyEquipMessage.ReqSetAutoResolve.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 获取玩家信息
     *
     * @return
     */
    public String getInfo() {
        return new StringBuilder("(uid=").append(this.userId).append("_roleId=").append(this.id).append("_name=").append(this.name).append("_level=").append(this.level).append("_mapId=").append(this.mapModelId).append("_evenType=").append(this.eventType).append("_taskId=").append(this.getMainTask().getNowTaskId()).append(")").toString();
    }

    //luowei

    /**
     * 随机怪物进行攻击
     */
    private int bossTest_lookMonster() {
        ByteMapCfg cfg = Manager.mapCfgManager.getMapCfg(mapModelId);
        if (cfg == null) {
            return 2000;
        }

        List<Cfg_Bossnew_world_Bean> beans = new ArrayList<>();
        for (Cfg_Bossnew_world_Bean bean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {
            if (bean.getClone_map() == mapModelId) {
                beans.add(bean);
            }
        }

        if (beans.size() == 0) {
            return 2000;
        }

        int num = RandomUtils.random(0, beans.size() - 1);

        Position pos = new Position();
        pos.setX(beans.get(num).getPos().get(0));
        pos.setY(beans.get(num).getPos().get(1));

        this.moveAi_MoveToPos(pos);
        curMonsterId = beans.get(num).getID();
        curMoveTime = System.currentTimeMillis();
        eventType = EventDefine.Event_Boss_101;
        return 2000;
    }

    private int bossTest_lookMonsterMove() {
        if (System.currentTimeMillis() - curMoveTime >= 5000) {
            this.chatGM("&moveto " + roads.get(roads.size() - 1).getX() + " " + roads.get(roads.size() - 1).getY());
            roads.clear();
            eventType = EventDefine.Event_Boss_102;
        } else {
            if (MapUtils.getDistance(this.curPos, roads.get(roads.size() - 1)) <= 1.5) {
                roads.clear();
                eventType = EventDefine.Event_Boss_102;
            }
        }
        return 2000;
    }

    private int bossTest_lookMonsterAttack() {
        boolean isExist = false;
        for (BaseNpc m : npcs.values()) {
            if (m.getModelId() == curMonsterId) {
                isExist = true;
                this.chatGM("&clearcount");
                break;
            }
        }

        if (!isExist) {
            curMonsterId = 0;
            eventType = EventDefine.Event_Boss_100;
            return 2000;
        }

        return attack(curPos, 0);
    }

    public ConcurrentHashMap<Integer, Boolean> getFunctionState() {
        return functionState;
    }

    public void setFunctionState(ConcurrentHashMap<Integer, Boolean> functionState) {
        this.functionState = functionState;
    }

    public void functionStateChange(int id, boolean open){
        this.functionState.put(id, open);
        if(id == FunctionStart.LoversFight){

        }
    }

    @Override
    public String toString() {
        return "Player{id:"+id+",name:"+getName()+",career:"+career+"}";
    }

    /**
     * 玩家离开队伍
     */
    public void removeTeam() {
        TeamManager.getInstance().removeTeamPlayer(this.teamId, this);
        this.teamId = 0;
    }

    public void enterTeam(Team team, boolean leader) {
        this.teamId = team.getId();
        team.addPlayer(this, leader);
    }

    public void reqQuitTeam() {
        if(this.teamId > 0){
            TeamMessage.ReqTeamOpt.Builder req = TeamMessage.ReqTeamOpt.newBuilder();
            req.setTargetId(this.teamId);
            req.setOpt(3);
            sendMsg(ReqTeamOpt.MsgID.eMsgID_VALUE, req.build().toByteArray());
        }
    }
}











