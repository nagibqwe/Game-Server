package com.backend.log.struct;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LogFieldEntity {

    private int crossLogType;

    private String table;

    private int tableType;

    private String classname;

    private String roleId;

    private Set<String> conditions = new HashSet<>();

    private Map<String, String> fields = new LinkedHashMap<>();

    private Map<String, String> datas = new LinkedHashMap<>();

    private Map<String, String> reasons = new LinkedHashMap<>();

    private Map<String, Integer> other = new LinkedHashMap<>();

    public int getCrossLogType() {
        return crossLogType;
    }

    public void setCrossLogType(int crossLogType) {
        this.crossLogType = crossLogType;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getTableType() {
        return tableType;
    }

    public void setTableType(int tableType) {
        this.tableType = tableType;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Set<String> getConditions() {
        return conditions;
    }

    public void setConditions(Set<String> conditions) {
        this.conditions = conditions;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public Map<String, String> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, String> datas) {
        this.datas = datas;
    }

    public Map<String, String> getReasons() {
        return reasons;
    }

    public void setReasons(Map<String, String> reasons) {
        this.reasons = reasons;
    }

    public Map<String, Integer> getOther() {
        return other;
    }

    public void setOther(Map<String, Integer> other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "LogFieldEntity{" +
                "table='" + table + '\'' +
                ", tableType=" + tableType +
                ", classname='" + classname + '\'' +
                ", fields=" + fields +
                ", conditions=" + conditions +
                ", datas=" + datas +
                '}';
    }
}
