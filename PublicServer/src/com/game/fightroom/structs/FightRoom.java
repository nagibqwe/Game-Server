package com.game.fightroom.structs;

import com.game.manager.Manager;
import com.game.zone.structs.ZoneTeam;
import io.netty.util.internal.ConcurrentSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 战场
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class FightRoom {

    private long fid;//战场编号
    private int serverId; //战场被开辟在的服务器ID
    private String plat;//平台ID;
    private int modelId;//副本ID号
    private int level = 1;  //副本等级
    private int type;
    private Set<ZoneTeam> team = new ConcurrentSet<>();//玩家信息
    private long ctime;//创建时间
    private long waitTime;//等待时间
    private long endwait;//结束等待时间
    private long fightTime;//战斗时间
    private int rstate;//房间状态
    private long crId;//房主ID
    private String cname;//房主名字

    //下面为房间设置的要求
    private boolean allReadyStart = false;//是否全部准备好后开战
    private int haveNum = 0;
    private int attackValue = 0;//要求战斗力值
    private int minP = 1;//最低要求人数

    private int mapmodelId;

    private int serverGroupId = 0;      //服务器分组id

    int stageId = 0;                    //跨服阶段

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public long getFid() {
        return fid;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }


    public String getPlat() {
        return plat;
    }

    public void setpPlat(String plat) {
        this.plat = plat;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getModelId() {
        return modelId;
    }

    public int getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(int serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getFightTime() {
        return fightTime;
    }

    public void setFightTime(long fightTime) {
        this.fightTime = fightTime;
    }

    public int getRstate() {
        return rstate;
    }
    private static final Logger LOG = LogManager.getLogger("FIghtROOM");

    public void setRstate(int rstate) {
        LOG.info("当前房间 " + getFid() + " 的战斗状态：" + getRstate() + " 房间创建者:" + getCname());
        this.rstate = rstate;
    }

    public boolean isAllReadyStart() {
        return allReadyStart;
    }

    public void setAllReadyStart(boolean allReadyStart) {
        this.allReadyStart = allReadyStart;
    }

    public long getCrId() {
        return crId;
    }

    public void setCrId(long crId) {
        this.crId = crId;
    }

    public int getHaveNum() {
        return haveNum;
    }

    public void setHaveNum(int haveNum) {
        this.haveNum = haveNum;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    public int getMinP() {
        return minP;
    }

    public void setMinP(int minP) {
        this.minP = minP;
    }

    public long getEndwait() {
        return endwait;
    }

    public void setEndwait(long endwait) {
        this.endwait = endwait;
    }


    public int getMapmodelId() {
        return mapmodelId;
    }

    public void setMapmodelId(int mapmodelId) {
        this.mapmodelId = mapmodelId;
    }

    public Set<ZoneTeam> getTeam() {
        return team;
    }

    public void setTeam(Set<ZoneTeam> team) {
        this.team = team;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public boolean hasRoleId(long roleId) {
        for (ZoneTeam zt : getTeam()) {
            if (zt.getPlist().containsKey(roleId)) {
                return true;
            }
        }
        return false;
    }

    public int hasPeoples() {
        int total = 0;
        for (ZoneTeam zt : getTeam()) {
            total += zt.getPlist().size();
        }
        return total;
    }

    public boolean removeRoleId(long roleId) {
        for (ZoneTeam zt : getTeam()) {
            if (zt.getPlist().containsKey(roleId)) {
                zt.getPlist().remove(roleId);
                if (zt.getPlist().size() < 1) {
                    team.remove(zt);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 关闭战斗房间
     */
    public void close() {
        Manager.fightManager.deal().closeFightRoom(this);
    }
}
