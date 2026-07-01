package com.backend.struct.log.entity.skill;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 技能日志
 */
@Table(name = "skilllog", tableType = TableType.Month)
public class SkillLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int skillId;            //技能id

    @FieldDesc
    private int level;              //等级

    @FieldDesc
    private int action;             //-1移除、0学习、1升级

    @FieldDesc
    private String consume;         //消耗

    @FieldDesc
    private long actionId;          //关联id

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
