package com.game.db.bean;

import game.core.db.BaseBean;

public class ChumBean extends BaseBean {
    private Long id;

    private Long rid1;

    private Long rid2;

    private String name;

    private String anno;

    private Integer lvl;

    private Integer exp;

    private Short freet;

    private Long lastfreshtime;

    private String datas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRid1() {
        return rid1;
    }

    public void setRid1(Long rid1) {
        this.rid1 = rid1;
    }

    public Long getRid2() {
        return rid2;
    }

    public void setRid2(Long rid2) {
        this.rid2 = rid2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public Integer getLvl() {
        return lvl;
    }

    public void setLvl(Integer lvl) {
        this.lvl = lvl;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Short getFreet() {
        return freet;
    }

    public void setFreet(Short freet) {
        this.freet = freet;
    }

    public Long getLastfreshtime() {
        return lastfreshtime;
    }

    public void setLastfreshtime(Long lastfreshtime) {
        this.lastfreshtime = lastfreshtime;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }
}