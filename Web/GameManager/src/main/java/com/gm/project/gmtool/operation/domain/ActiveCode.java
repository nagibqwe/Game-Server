package com.gm.project.gmtool.operation.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 激活码对象 activecode
 * 
 * @author gm
 * @date 2021-09-18
 */
public class ActiveCode
{
    private static final long serialVersionUID = 1L;

    /** 激活码ID */
    private String id;

    /** 激活码 */
    @Excel(name = "激活码")
    private String code;

    /** 激活码类型名 */
    @Excel(name = "激活码类型名")
    private String activeName;

    /** 批次号 */
    @Excel(name = "批次号")
    private String batch;

    /** 物品列表 */
    @Excel(name = "物品列表")
    private String itemList;

    /** 万能码 */
    @Excel(name = "万能码")
    private int param;

    /** 有效开始时间 */

    private String valide_time_begin;

    /** 有效结束时间 */

    private String valide_time_end;

    /** 渠道名 */
    @Excel(name = "渠道名")
    private String plateform_name_big;

    /** 有效区服 */
    @Excel(name = "有效区服")
    private String valide_server_id_list;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间")
    private String create_time;

    /** 激活码使用时间 */
    @Excel(name = "激活码使用时间")
    private String getTime;

    /** 使用者角色id */
    @Excel(name = "使用者角色id")
    private String getPlayerId;

    /** 使用者角色名 */
    @Excel(name = "使用者角色名")
    private String getPlateformAid;

    /** 使用者帐号 */
    @Excel(name = "使用者帐号")
    private String getAccountId;

    /** 使用者平台名 */
    @Excel(name = "使用者平台名")
    private String getPlateformName;

    /** 使用者区服 */
    @Excel(name = "使用者区服")
    private String getServerId;

    /** 平台名（小） */
    @Excel(name = "平台名", readConverterExp = "小=")
    private String plateformNameSmall;

    /** 删除时间 */
    @Excel(name = "删除时间")
    private String deleteTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }

    public String getValide_time_begin() {
        return valide_time_begin;
    }

    public void setValide_time_begin(String valide_time_begin) {
        this.valide_time_begin = valide_time_begin;
    }

    public String getValide_time_end() {
        return valide_time_end;
    }

    public void setValide_time_end(String valide_time_end) {
        this.valide_time_end = valide_time_end;
    }

    public String getPlateform_name_big() {
        return plateform_name_big;
    }

    public void setPlateform_name_big(String plateform_name_big) {
        this.plateform_name_big = plateform_name_big;
    }

    public String getValide_server_id_list() {
        return valide_server_id_list;
    }

    public void setValide_server_id_list(String valide_server_id_list) {
        this.valide_server_id_list = valide_server_id_list;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getGetTime() {
        return getTime;
    }

    public void setGetTime(String getTime) {
        this.getTime = getTime;
    }

    public String getGetPlayerId() {
        return getPlayerId;
    }

    public void setGetPlayerId(String getPlayerId) {
        this.getPlayerId = getPlayerId;
    }

    public String getGetPlateformAid() {
        return getPlateformAid;
    }

    public void setGetPlateformAid(String getPlateformAid) {
        this.getPlateformAid = getPlateformAid;
    }

    public String getGetAccountId() {
        return getAccountId;
    }

    public void setGetAccountId(String getAccountId) {
        this.getAccountId = getAccountId;
    }

    public String getGetPlateformName() {
        return getPlateformName;
    }

    public void setGetPlateformName(String getPlateformName) {
        this.getPlateformName = getPlateformName;
    }

    public String getGetServerId() {
        return getServerId;
    }

    public void setGetServerId(String getServerId) {
        this.getServerId = getServerId;
    }

    public String getPlateformNameSmall() {
        return plateformNameSmall;
    }

    public void setPlateformNameSmall(String plateformNameSmall) {
        this.plateformNameSmall = plateformNameSmall;
    }

    public String getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }

    @Override
    public String toString() {
        return "ActiveCode{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", activeName='" + activeName + '\'' +
                ", batch='" + batch + '\'' +
                ", itemList='" + itemList + '\'' +
                ", param=" + param +
                ", valide_time_begin=" + valide_time_begin +
                ", valide_time_end=" + valide_time_end +
                ", plateform_name_big='" + plateform_name_big + '\'' +
                ", valide_server_id_list='" + valide_server_id_list + '\'' +
                ", create_time='" + create_time + '\'' +
                ", getTime=" + getTime +
                ", getPlayerId=" + getPlayerId +
                ", getPlateformAid='" + getPlateformAid + '\'' +
                ", getAccountId=" + getAccountId +
                ", getPlateformName='" + getPlateformName + '\'' +
                ", getServerId=" + getServerId +
                ", plateformNameSmall='" + plateformNameSmall + '\'' +
                ", deleteTime=" + deleteTime +
                '}';
    }
}
