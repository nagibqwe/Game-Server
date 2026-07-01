package com.game.db.bean;

import com.game.jjc.structs.JJCReport;
import game.core.db.BaseBean;
import game.core.json.TypeReference;
import game.core.util.JsonUtils;
import game.core.util.VersionUpdateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class JJCBean extends BaseBean {

    public static final Logger log = LogManager.getLogger(JJCBean.class);

    protected long roleId; // 玩家Id
    protected int camp; //阵营
    protected int career; //职业
    protected int score; //排名
    protected int time; //积分达成时间
    public List<JJCReport> records = new ArrayList<>(); // 玩家战绩

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getCamp() {
        return camp;
    }

    public void setCamp(int camp) {
        this.camp = camp;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getRecords() {
        return  VersionUpdateUtil.dataSave(JsonUtils.toJSONString(records));
    }

    public void setRecords(String records) {
        try {
            this.records = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(records), new TypeReference<ArrayList<JJCReport>>(){});
        } catch (Exception ex) {
            log.error(ex, ex);
        }
    }

}
