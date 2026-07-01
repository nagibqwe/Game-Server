package common.player;

import com.game.boss.struct.BossTypeConst;
import com.game.client.Client;
import com.game.manager.Manager;
import com.game.nature.struct.NatureType;
import com.game.player.manager.PlayerManager;
import com.game.player.script.IPlayerScript;
import com.game.player.structs.Player;
import com.game.script.ScriptEnum;
import com.game.server.worker.TimeTickWorker;
import com.game.structs.Config;
import com.game.structs.SessionAttribute;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import game.core.message.SMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage;
import game.message.RegisterMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.session.IoSession;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PlayerScript implements IPlayerScript {

    private static final Logger log = LogManager.getLogger(PlayerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.PlayerScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void init() {
        int connectNum = Config.getConnectNum();
        PlayerManager.getInstance().setMaxid(Config.getUserIdBegin());
        if (connectNum > 0) {
            int userIdBegin = Config.getUserIdBegin();
            long maxid = PlayerManager.getInstance().getMaxid();
            for(int i = 0; i < connectNum; ++i){
                maxid++;
                PlayerManager.getInstance().setMaxid(maxid);
                Player p = new Player();
                p.setrUserId(maxid);
                p.setUserId(maxid);
                p.setCareer((int) (p.getrUserId()%2));
                p.init();
                p.setEventType(Config.getEventType());
//                addPlayer(p.getId(), p);
            }
            log.info("配置表中的预设机器人创建连接并登陆游戏");
        }
    }

    public void addPlayer(int num){
        long maxid = PlayerManager.getInstance().getMaxid();
        for(int i=0; i<num; i++){
            maxid++;
            PlayerManager.getInstance().setMaxid(maxid);
            Player p = new Player();
            p.setrUserId(maxid);
            p.setUserId(maxid);
            p.setCareer((int) (p.getrUserId()%2));
            p.init();
            p.setEventType(Config.getEventType());
        }
    }

    @Override
    public int getPlayerCount() {
        synchronized(this){
            int count = 0;
            for(Player p : Manager.playerManager.getPlayers().values()){
                if(p.isRecvedBaseInfo()){
                    count++;
                }
            }
            return count;
        }
    }

    public Player getPlayerByRoleId(long roleId) {
        return Manager.playerManager.getPlayers().get(roleId);
    }

    @Override
    public void addPlayer(long roleId, Player p){
        if(Manager.playerManager.getPlayers().containsKey(roleId)){
            log.error(String.format("添加角色的时候发现了重复的,这个不对!!! 角色roleid:%d userid:%d name:%s 传入id:%d", p.getId(), p.getUserId(), p.getName(), roleId));
        }
        Manager.playerManager.getPlayers().put(roleId, p);
    }

    @Override
    public void removePlayer(long roleId){
        Manager.playerManager.getPlayers().remove(roleId);
    }

    @Override
    public String getRandomName() {
        int num = RandomUtils.random(2, 6);
        return Utils.makeRandomCode(num);
    }

    @Override
    public String getRobotName(long beginId) {
        return "机器人"+beginId;
    }

    @Override
    public void initPlayerBaseInfo(Player player, PlayerMessage.ResPlayerBaseInfo messInfo) {
        // setplayerBaseInfo 会修改userid
        removePlayer(player.getId());
        setPlayerBaseInfo(player, messInfo);
        // key 从userid切换到roleid
        addPlayer(player.getId(), player);

        //请求玩家的功能信息
        player.equipResolveSet();
        player.holyEquipResolveSet();
        //请求法宝信息九 零 一起玩 www.901 75.com
        player.reqNatureInfo(NatureType.STIFLEFFABAO);

        Manager.bossManager.deal().sendReqOpenDreamBoss(player, BossTypeConst.WORLD_BOSS);

        Manager.bossManager.deal().sendReqSuitGemBossPanel(player, 0);
        //机器人太弱，加一个攻击buff
        player.chatGM("&addbuff 16");
        player.chatGM("&addatt 1 10000");
        player.chatGM("&addatt 2 10000");
        player.chatGM("&addatt 3 10000");
        player.chatGM("&addatt  4 10000");
        player.chatGM("&additem 3 1000000");

    }

    /**
     * 设置玩家基本数据
     * @param messInfo
     */
    private void setPlayerBaseInfo(Player player, PlayerMessage.ResPlayerBaseInfo messInfo) {
        player.setUserId(messInfo.getAccountId());
        player.setId(messInfo.getRoleID());
        player.setCareer(messInfo.getOccupation());
        player.setLevel(messInfo.getLevel());
        player.setMapModelId(messInfo.getMapID());
        player.getCurPos().setX(messInfo.getPosX());
        player.getCurPos().setY(messInfo.getPosY());
        player.setStateLevel(messInfo.getStateVip());
        player.setCampNo(messInfo.getCamp());
//        player.setBirth(messInfo.getBirthGroup());
        player.setName(messInfo.getName());
        log.info(player.getInfo() + "登录成功!角色当前地图ID：" + player.getMapModelId());
        player.getSession().setAttribute("roleId", player.getId());
        player.setRecvedBaseInfo(true);
    }

    /**
     * 事件心跳处理
     */
    public void tickEvent() {
        try {
            long time = TimeUtils.Time();
            for (Map.Entry<Long, Player> entry : Manager.playerManager.getPlayers().entrySet()) {
                Player player = entry.getValue();
                if (player == null) {
                    continue;
                }

                //转到同一个线程去执行事务
                Client.getRecvExcutor().addTask(player.getUserId(), new TimeTickWorker(time, player));
                player.timeTick(time);

            }

        } catch (Exception e) {
            log.info(e, e);
        }
    }

    @Override
    public Player getPlayerNoTeam() {
        for(Player player : PlayerManager.getInstance().getPlayers().values()){
            if(player.getTeamId() > 0){
                continue;
            }
            return player;
        }
        return null;
    }

    @Override
    public Player getPlayerNoTeam(int career) {
        for(Player player : PlayerManager.getInstance().getPlayers().values()){
            if(player.getTeamId() > 0){
                continue;
            }
            if(player.getCareer() == career){
                return player;
            }
        }
        return null;
    }
}
