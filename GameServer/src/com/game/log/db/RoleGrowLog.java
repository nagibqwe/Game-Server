package com.game.log.db;

import com.data.bean.Cfg_Equip_Bean;
import com.game.log.grow.BiType;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class RoleGrowLog extends BaseLogBean {
    private static final Logger logger = LogManager.getLogger("RoleGrowLog");
    private long roleId;
    private String type;
    private String typeIds;
    private String detail;
    private String biType;

    public RoleGrowLog(){}

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    public RoleGrowLog(long roleId, GrowType type, String detail){
        this.roleId = roleId;
        if(type != null){
            this.type = type.getDesc();
            this.typeIds = type.getType() + "-" + type.getSubType() + "-" + type.getAct_type();
            this.biType = type.getBiType() == 1 ? "成长":type.getBiType() == 2?"装备":"未知";
        }
        this.detail = detail;
//        this.createDate = new Date();
    }

    public static void create(Player player, GrowType type, int target, int old, int after){
        create(player, type, 0, target, old, after, null);
    }

    public static void create(Player player, GrowType type, int subType, int target, int old, int after, Object detail){
        create(player, type, subType, target, old, after,0, detail);
    }

    public static void create(Player player, GrowType type,Object detail, int afterLevel, int suit){
        create(player, type, 0, 0, 0, afterLevel,suit, detail);
    }

    /**
     * 创建玩家成长日志
     * @param player 玩家
     * @param type 类型
     * @param old 变化前
     * @param after 变化后
     * @param detail 类型对应的详细数据
     * @param target 目标（可以多个）
     */
    private static void create(Player player, GrowType type, int subType, int target, int old, int after, int suit, Object detail){
        if(subType == 0){
            subType = type.getSubType();
        }
        if(type.getBiType() == BiType.grow.getType()){
            Manager.biManager.getScript().biGrow(player, type.getType(), subType, type.getAct_type(), old, after, target);
        }else if(type.getBiType() == BiType.equip.getType()){
            //装备
            int act_type = type.getAct_type(), equip_type = type.getType();
            if(equip_type != 1){
                equip_type += suit;
                suit = 0;
            }
            int part, item_id, star, lev, col, str, bind, gem_num, gem_pos, gem_set, gem_rating, gem_id;
            part = item_id = star = lev = col = str = bind = gem_num = gem_pos = gem_set = gem_rating = gem_id = 0;
            String item_name = "";
            if(detail != null) {
                if(detail instanceof Cfg_Equip_Bean){
                    Cfg_Equip_Bean c = (Cfg_Equip_Bean) detail;
                    item_name = c.getName();
                    item_id = c.getId();
                    part = c.getPart();
                    star = c.getDiamond_Number();
                    lev = c.getLevel();
                    col = c.getQuality();
//                    str = c.getQuality();
                    bind = c.getBind();
                }
            }
            str = after;
            Manager.biManager.getScript().biEquip(player,act_type, equip_type,part,item_id,item_name,star,lev,col,str,bind,suit,gem_num,gem_pos,gem_set,gem_rating,gem_id);
        }
    }

    @Log(fieldType = "bigint", logField = "roleid", index = "0")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(logField = "type", fieldType = "varchar(100)", index = "0")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Log(logField = "typeIds", fieldType = "varchar(100)", index = "0")
    public String getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(String typeIds) {
        this.typeIds = typeIds;
    }

    @Log(logField = "detail", fieldType = "varchar(1000)", index = "0")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
