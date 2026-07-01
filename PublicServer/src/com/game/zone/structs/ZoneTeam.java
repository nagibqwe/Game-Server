package com.game.zone.structs;

import game.core.util.TimeUtils;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 跨服的组队信息
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class ZoneTeam {

    //服务器ID
    private int sId;
    //平台标识
    private String plat;
    //玩家列表
    private ConcurrentHashMap<Long, TeamPlayerInfo> plist = new ConcurrentHashMap<>();
    //总战斗力
    private long pow = 0;
    //出生的阵营
    private int birthGroup;
    //创建时间
    private long createTime = TimeUtils.Time();

    //在副本中属于对抗的阵营分组
    private int campNo;

    @Override
    public String toString() {
//        return "ZoneTeam{" + "sId=" + sId + ", plat=" + plat + ", pow=" + pow + ", birthGroup=" + birthGroup + '}';
        StringBuilder builder = new StringBuilder();
        builder.append("ZoneTeam{");
        builder.append("sId=");
        builder.append(sId);
        builder.append(", plat=");
        builder.append(plat);
        builder.append(", pow=");
        builder.append(pow);
        builder.append('}');
        return builder.toString();
    }
    

    public int getBirthGroup() {
        return birthGroup;
    }

    public void setBirthGroup(int birthGroup) {
        this.birthGroup = birthGroup;
    }

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public ConcurrentHashMap<Long, TeamPlayerInfo> getPlist() {
        return plist;
    }

    public void setPlist(ConcurrentHashMap<Long, TeamPlayerInfo> plist) {
        this.plist = plist;
    }

    public long getPow() {
        return pow;
    }

    public void setPow(long pow) {
        this.pow = pow;
    }

    public int getCampNo() {
        return campNo;
    }

    public void setCampNo(int campNo) {
        this.campNo = campNo;
    }
    
    public TeamPlayerInfo leader() {
        for (TeamPlayerInfo tpi : plist.values()) {
            if (tpi.isLeader()) {
                return tpi;
            }
        }
        //返回第一个玩家
        return plist.entrySet().iterator().next().getValue();
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    
    
    
}
