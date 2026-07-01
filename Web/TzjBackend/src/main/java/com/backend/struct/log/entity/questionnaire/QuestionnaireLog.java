package com.backend.struct.log.entity.questionnaire;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

@Table(name = "questionnairelog", tableType = TableType.Year)
public class QuestionnaireLog implements IConvertor {

    @FieldDesc(selectKey = true)
    private long userId;        //用户id

    @FieldDesc(selectKey = true)
    private long roleId;        //角色id

    @FieldDesc
    private String data;        //答题数据

    @FieldDesc
    private String time;        //时间

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
