package com.game.crossfight.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 跨服进入日志
 */
public class CrossCloneEnterLog extends CommonLogBean {

    private final static Logger LOG = LogManager.getLogger(CrossCloneEnterLog.class);

    private long fightId;           //房间ID
    private String platSid;         //服务器ID
    private int campNo;             //阵营
    private int cloneId;            //副本ID
    private String cloneName;       //副本名字
    private int level;              //玩家等级

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        LOG.error(buildSql());
    }

    @Log(logField = "fightId", fieldType = "bigint", index = "0")
    public long getFightId() {
        return fightId;
    }

    public void setFightId(long fightId) {
        this.fightId = fightId;
    }

    @Log(logField = "platSid", fieldType = "varchar(100)", index = "0")
    public String getPlatSid() {
        return platSid;
    }

    public void setPlatSid(String platSid) {
        this.platSid = platSid;
    }

    @Log(logField = "campNo", fieldType = "int", index = "0")
    public int getCampNo() {
        return campNo;
    }

    public void setCampNo(int campNo) {
        this.campNo = campNo;
    }

    @Log(logField = "cloneId", fieldType = "int", index = "0")
    public int getCloneId() {
        return cloneId;
    }

    public void setCloneId(int cloneId) {
        this.cloneId = cloneId;
    }

    @Log(logField = "cloneName", fieldType = "varchar(200)", index = "0")
    public String getCloneName() {
        return cloneName;
    }

    public void setCloneName(String cloneName) {
        this.cloneName = cloneName;
    }

    @Log(logField = "level", fieldType = "int", index = "0")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
