package com.backend.struct.log.entity.drop;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 特殊掉落日志
 */
@Table(name = "specialdroplog", tableType = TableType.Month)
public class SpecialDropLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int killDropCount;      //击杀掉落次数

    @FieldDesc
    private int rankDropCount;      //排名掉落次数

    @FieldDesc
    private int joinDropCount;      //参与奖掉落次数

    @FieldDesc
    private int bossId;             //击杀boss的配置表id

    @FieldDesc
    private int randomCount;        //玩家随机排名奖励获得额外奖励次数

    @FieldDesc
    private int hasRankCount;       //已经获得该boss排名奖励的次数

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getKillDropCount() {
        return killDropCount;
    }

    public void setKillDropCount(int killDropCount) {
        this.killDropCount = killDropCount;
    }

    public int getRankDropCount() {
        return rankDropCount;
    }

    public void setRankDropCount(int rankDropCount) {
        this.rankDropCount = rankDropCount;
    }

    public int getJoinDropCount() {
        return joinDropCount;
    }

    public void setJoinDropCount(int joinDropCount) {
        this.joinDropCount = joinDropCount;
    }

    public int getBossId() {
        return bossId;
    }

    public void setBossId(int bossId) {
        this.bossId = bossId;
    }

    public int getRandomCount() {
        return randomCount;
    }

    public void setRandomCount(int randomCount) {
        this.randomCount = randomCount;
    }

    public int getHasRankCount() {
        return hasRankCount;
    }

    public void setHasRankCount(int hasRankCount) {
        this.hasRankCount = hasRankCount;
    }
}
