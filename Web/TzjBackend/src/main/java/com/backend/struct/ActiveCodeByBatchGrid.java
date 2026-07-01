package com.backend.struct;

public class ActiveCodeByBatchGrid {
    private String id;
    /**
     * 激活码
     */
    private String code;
    /**
     * 激活码类型名
     */
    private String activeName;
    /**
     * 批次号
     */
    private String batch;
    /**
     * 物品列表
     */
    private String itemList;
    /**
     * 参数（默认=0）,1表示万能码
     */
    private String param;
    /**
     * 有效开始时间（NULL表示无）
     */
    private String valide_time_begin;
    /**
     * 有效结束时间（NULL表示无）
     */
    private String valide_time_end;
    /**
     * 平台名（大）
     */
    private String plateform_name_big;
    /**
     * 仅某些服有效（[]表示通用），例如：2、3、4服=[2,3,4]
     */
    private String valide_server_id_list;
    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 激活码使用时间
     */
    private String get_time;
    /**
     * 激活码使用角色id
     */
    private String get_player_id;
    /**
     * 激活码使用角色名
     */
    private String get_player_name;
    /**
     * 服务器编号
     */
    private String get_server_id;
    /**
     * 激活码使用者帐号id
     */
    private String get_account_id;
    /**
     * 激活码使用者平台帐号id
     */
    private String get_plateform_aid;
    /**
     * 激活码使用者平台名
     */
    private String get_plateform_name;

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

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
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

    public String getGet_time() {
        return get_time;
    }

    public void setGet_time(String get_time) {
        this.get_time = get_time;
    }

    public String getGet_player_id() {
        return get_player_id;
    }

    public void setGet_player_id(String get_player_id) {
        this.get_player_id = get_player_id;
    }

    public String getGet_player_name() {
        return get_player_name;
    }

    public void setGet_player_name(String get_player_name) {
        this.get_player_name = get_player_name;
    }

    public String getGet_server_id() {
        return get_server_id;
    }

    public void setGet_server_id(String get_server_id) {
        this.get_server_id = get_server_id;
    }

    public String getGet_account_id() {
        return get_account_id;
    }

    public void setGet_account_id(String get_account_id) {
        this.get_account_id = get_account_id;
    }

    public String getGet_plateform_aid() {
        return get_plateform_aid;
    }

    public void setGet_plateform_aid(String get_plateform_aid) {
        this.get_plateform_aid = get_plateform_aid;
    }

    public String getGet_plateform_name() {
        return get_plateform_name;
    }

    public void setGet_plateform_name(String get_plateform_name) {
        this.get_plateform_name = get_plateform_name;
    }
}
