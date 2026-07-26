package com.gm.project.gmtool.operation.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Код активации对象 activecode
 * 
 * @author gm
 * @date 2021-09-18
 */
public class ActiveCode
{
    private static final long serialVersionUID = 1L;

    /** Код активацииID */
    private String id;

    /** Код активации */
    @Excel(name = "Код активации")
    private String code;

    /** Код активацииТип名 */
    @Excel(name = "Код активацииТип名")
    private String activeName;

    /** Номер пакета */
    @Excel(name = "Номер пакета")
    private String batch;

    /** Список предметов */
    @Excel(name = "Список предметов")
    private String itemList;

    /** 万能码 */
    @Excel(name = "万能码")
    private int param;

    /** 有效Время начала */

    private String valide_time_begin;

    /** 有效Время окончания */

    private String valide_time_end;

    /** Канал名 */
    @Excel(name = "Канал名")
    private String plateform_name_big;

    /** 有效Игровой сервер */
    @Excel(name = "有效Игровой сервер")
    private String valide_server_id_list;

    /**
     * Время создания
     */
    @Excel(name = "Время создания")
    private String create_time;

    /** Код активации使用Время */
    @Excel(name = "Код активации使用Время")
    private String getTime;

    /** 使用者ID персонажа */
    @Excel(name = "使用者ID персонажа")
    private String getPlayerId;

    /** 使用者Имя персонажа */
    @Excel(name = "使用者Имя персонажа")
    private String getPlateformAid;

    /** 使用者帐号 */
    @Excel(name = "使用者帐号")
    private String getAccountId;

    /** 使用者Платформа */
    @Excel(name = "使用者Платформа")
    private String getPlateformName;

    /** 使用者Игровой сервер */
    @Excel(name = "使用者Игровой сервер")
    private String getServerId;

    /** Платформа（小） */
    @Excel(name = "Платформа", readConverterExp = "小=")
    private String plateformNameSmall;

    /** УдалитьВремя */
    @Excel(name = "УдалитьВремя")
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
