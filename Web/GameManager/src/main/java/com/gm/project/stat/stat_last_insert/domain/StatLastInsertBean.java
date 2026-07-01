package com.gm.project.stat.stat_last_insert.domain;
import com.gm.framework.aspectj.lang.annotation.Excel;
public class StatLastInsertBean {
    private static final long serialVersionUID = 1L;



    @Excel(name = "id")
    private Long id;

    @Excel(name = "服务器Id")
    private Integer sid;

    @Excel(name = "表名称")
    private String src_table;

    @Excel(name = "拉取时间")
    private Long src_time;

    @Excel(name = "源日志库Id")
    private Integer src_id ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getSrc_table() {
        return src_table;
    }

    public void setSrc_table(String src_table) {
        this.src_table = src_table;
    }

    public Long getSrc_time() {
        return src_time;
    }

    public void setSrc_time(Long src_time) {
        this.src_time = src_time;
    }

    public Integer getSrc_id() {
        return src_id;
    }

    public void setSrc_id(Integer src_id) {
        this.src_id = src_id;
    }
}
