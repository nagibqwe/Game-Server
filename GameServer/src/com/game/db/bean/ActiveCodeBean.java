/**
 * Auto generated, do not edit it
 * <p>
 * ActiveCode
 */
package com.game.db.bean;

import game.core.db.BaseBean;

public class ActiveCodeBean extends BaseBean {

    private int id; // 激活码ID
    private String code; // 激活码
    private String activeName; // 激活码类型名
    private String batch; // 批次号
    private String itemList; // 物品配置表ID
    private int param; // 参数（默认=0）,1表示万能码
    private String valide_time_begin; // 有效开始时间（NULL表示无）
    private String valide_time_end; // 有效结束时间（NULL表示无）
    private String plateform_name_big; // 平台名（大）
    private String plateform_name_small; // 平台名（小）
    private String valide_server_id_list; // 仅某些服有效（[]表示通用），例如：2、3、4服=[2,3,4]
    private long get_player_id; // 激活码使用角色id
    private long get_server_id;//激活码使用的角色服务器
    private long get_account_id; // 激活码使用者帐号id
    private String get_plateform_aid; // 激活码使用者平台帐号id
    private String get_plateform_name; // 激活码使用者平台名
    private int deleteTime;//是否删除，0可用，>0删除时间

    /**
     * get 激活码ID
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * set 激活码ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get 激活码
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * set 激活码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * get 激活码类型名
     *
     * @return
     */
    public String getActiveName() {
        return activeName;
    }

    /**
     * set 激活码类型名
     */
    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    /**
     * get 批次号
     *
     * @return
     */
    public String getBatch() {
        return batch;
    }

    /**
     * set 批次号
     */
    public void setBatch(String batch) {
        this.batch = batch;
    }

    /**
     * get 物品id
     *
     * @return
     */
    public String getItemList() {
        return itemList;
    }

    /**
     * set 物品id
     *
     * @param itemList
     */
    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    /**
     * get 参数（默认=0）,1表示万能码
     *
     * @return
     */
    public int getParam() {
        return param;
    }

    /**
     * set 参数（默认=0）,1表示万能码
     */
    public void setParam(int param) {
        this.param = param;
    }

    /**
     * get 有效开始时间（NULL表示无）
     *
     * @return
     */
    public String getValide_time_begin() {
        return valide_time_begin;
    }

    /**
     * set 有效开始时间（NULL表示无）
     */
    public void setValide_time_begin(String valide_time_begin) {
        this.valide_time_begin = valide_time_begin;
    }

    /**
     * get 有效结束时间（NULL表示无）
     *
     * @return
     */
    public String getValide_time_end() {
        return valide_time_end;
    }

    /**
     * set 有效结束时间（NULL表示无）
     */
    public void setValide_time_end(String valide_time_end) {
        this.valide_time_end = valide_time_end;
    }

    /**
     * get 平台名（大）
     *
     * @return
     */
    public String getPlateform_name_big() {
        return plateform_name_big;
    }

    /**
     * set 平台名（大）
     */
    public void setPlateform_name_big(String plateform_name_big) {
        this.plateform_name_big = plateform_name_big;
    }

    /**
     * get 平台名（小）
     *
     * @return
     */
    public String getPlateform_name_small() {
        return plateform_name_small;
    }

    /**
     * set 平台名（小）
     */
    public void setPlateform_name_small(String plateform_name_small) {
        this.plateform_name_small = plateform_name_small;
    }

    /**
     * get 仅某些服有效（[]表示通用），例如：2、3、4服=[2,3,4]
     *
     * @return
     */
    public String getValide_server_id_list() {
        return valide_server_id_list;
    }

    /**
     * set 仅某些服有效（[]表示通用），例如：2、3、4服=[2,3,4]
     */
    public void setValide_server_id_list(String valide_server_id_list) {
        this.valide_server_id_list = valide_server_id_list;
    }

    /**
     * get 激活码使用角色id
     *
     * @return
     */
    public long getGet_player_id() {
        return get_player_id;
    }

    /**
     * set 激活码使用角色id
     */
    public void setGet_player_id(long get_player_id) {
        this.get_player_id = get_player_id;
    }

    /**
     * get 激活码使用者帐号id
     *
     * @return
     */
    public long getGet_account_id() {
        return get_account_id;
    }

    /**
     * set 激活码使用者帐号id
     */
    public void setGet_account_id(long get_account_id) {
        this.get_account_id = get_account_id;
    }

    /**
     * get 激活码使用者平台帐号id
     *
     * @return
     */
    public String getGet_plateform_aid() {
        return get_plateform_aid;
    }

    /**
     * set 激活码使用者平台帐号id
     */
    public void setGet_plateform_aid(String get_plateform_aid) {
        this.get_plateform_aid = get_plateform_aid;
    }

    /**
     * get 激活码使用者平台名
     *
     * @return
     */
    public String getGet_plateform_name() {
        return get_plateform_name;
    }

    /**
     * set 激活码使用者平台名
     */
    public void setGet_plateform_name(String get_plateform_name) {
        this.get_plateform_name = get_plateform_name;
    }

    public long getGet_server_id() {
        return get_server_id;
    }

    public void setGet_server_id(long get_server_id) {
        this.get_server_id = get_server_id;
    }

    public int getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(int deleteTime) {
        this.deleteTime = deleteTime;
    }
}
