package com.backend.struct.log;

import org.nutz.dao.pager.Pager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LogRecord {

    private Map<String, String> fields = new LinkedHashMap<>();

    private List<Map<String, String>> datas = new ArrayList<>();

    private Pager pager;

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public List<Map<String, String>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, String>> datas) {
        this.datas = datas;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }
}
