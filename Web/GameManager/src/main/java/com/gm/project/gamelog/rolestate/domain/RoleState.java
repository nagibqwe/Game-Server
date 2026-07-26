package com.gm.project.gamelog.rolestate.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Журнал снимков персонажей对象 rolestate
 * 
 * @author gm
 * @date 2021-09-07
 */
public class RoleState
{
    private static final long serialVersionUID = 1L;

    /** ID персонажа值 */
    @Excel(name = "ID персонажа值")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /** ID аккаунта值 */
    @Excel(name = "ID аккаунта值")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /** Имя персонажа */
    @Excel(name = "Имя персонажа")
    private String roleName;

    /** Код устройства */
    @Excel(name = "Код устройства")
    private String machineCode;

    /** КаналID */
    @Excel(name = "КаналID")
    private String platUserId;

    /** ОС клиента */
    @Excel(name = "ОС клиента")
    private String clientOS;

    /** Уровень */
    @Excel(name = "Уровень")
    private Long level;

    /** Пол */
    @Excel(name = "Пол")
    private Integer sex;

    /** Класс персонажа */
    @Excel(name = "Класс персонажа")
    private Integer career;

    /** 角色Время создания */
    @Excel(name = "角色Время создания")
    private String createTime;

    /** В сети时长 */
    @Excel(name = "В сети时长")
    private Long onlineTime;

    /** ID сервера создания */
    @Excel(name = "ID сервера создания")
    private Long createsid;

    /** 攻击力 */
    @Excel(name = "攻击力")
    private Long attack;

    /** 更新的最新Время值 */
    @Excel(name = "更新的最新Время值")
    private String lastupdatetime;

    /** 更新的最新秒值 */
    @Excel(name = "更新的最新秒值")
    private Long ts;

    /** Ячеек инвентаря */
    @Excel(name = "Ячеек инвентаря")
    private Long bagcellsnum;

    /** 当前登录IP */
    @Excel(name = "当前登录IP")
    private String ip;

    /** Монеты */
    @Excel(name = "Монеты")
    private Long money;

    /** 元宝 */
    @Excel(name = "元宝")
    private Long gold;

    /** 体力 */
    @Excel(name = "体力")
    private Long iron;

    /** Удалён */
    @Excel(name = "Удалён")
    private Long isDelete;

    /** 总Пополнение获得元宝数 */
    @Excel(name = "总Пополнение获得元宝数")
    private Long rechargeGold;

    /** funcell生成的UUid */
    @Excel(name = "funcell生成的UUid")
    private String funcellUUid;

    /** Канал */
    @Excel(name = "Канал")
    private String platformName;

    /** 坐骑Уровень */
    @Excel(name = "坐骑Уровень")
    private Long horseLayer;

    /** 坐骑Уровень */
    @Excel(name = "坐骑Уровень")
    private Long horseIllusionLevel;

    /** 所有坐骑Id集 */
    @Excel(name = "所有坐骑Id集")
    private String horseIds;

    /** 所有翅膀Id集 */
    @Excel(name = "所有翅膀Id集")
    private String wingIds;

    /** 所有宠物Id集 */
    @Excel(name = "所有宠物Id集")
    private String petIds;

    /** 衣服星级 */
    @Excel(name = "衣服星级")
    private Long clotheStar;

    /** 武器星级 */
    @Excel(name = "武器星级")
    private Long weaponStar;

    /** 穿戴装备总星级 */
    @Excel(name = "穿戴装备总星级")
    private Long equipAllStar;

    /** 玩家的Месяц卡结束День数 */
    @Excel(name = "玩家的Месяц卡结束День数")
    private Long moonCardDay;

    /** 角色ДаНет有充过值 */
    @Excel(name = "角色ДаНет有充过值")
    private Long isRecharge;

    /** Последний вход */
    @Excel(name = "Последний вход")
    private Long lastLoginTime;

    /** 时装身体ID */
    @Excel(name = "时装身体ID")
    private Long fashionBodyId;

    /** 时装武器ID */
    @Excel(name = "时装武器ID")
    private Long fashionWeaponId;

    /** ДаНет激活终身卡 */
    @Excel(name = "ДаНет激活终身卡")
    private Long lifeCard;

    /** 清理Уровень */
    @Excel(name = "清理Уровень")
    private Long clearlevel;

    /** ID аккаунта платформы */
    @Excel(name = "ID аккаунта платформы")
    private String cpUserId;

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public String getRoleName()
    {
        return roleName;
    }
    public void setMachineCode(String machineCode)
    {
        this.machineCode = machineCode;
    }

    public String getMachineCode()
    {
        return machineCode;
    }
    public void setPlatUserId(String platUserId)
    {
        this.platUserId = platUserId;
    }

    public String getPlatUserId()
    {
        return platUserId;
    }
    public void setClientOS(String clientOS)
    {
        this.clientOS = clientOS;
    }

    public String getClientOS()
    {
        return clientOS;
    }
    public void setLevel(Long level)
    {
        this.level = level;
    }

    public Long getLevel()
    {
        return level;
    }
    public void setSex(Integer sex)
    {
        this.sex = sex;
    }

    public Integer getSex()
    {
        return sex;
    }
    public void setCareer(Integer career)
    {
        this.career = career;
    }

    public Integer getCareer()
    {
        return career;
    }
    public void setOnlineTime(Long onlineTime)
    {
        this.onlineTime = onlineTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getOnlineTime()
    {
        return onlineTime;
    }
    public void setCreatesid(Long createsid)
    {
        this.createsid = createsid;
    }

    public Long getCreatesid()
    {
        return createsid;
    }
    public void setAttack(Long attack)
    {
        this.attack = attack;
    }

    public Long getAttack()
    {
        return attack;
    }
    public void setLastupdatetime(String lastupdatetime)
    {
        this.lastupdatetime = lastupdatetime;
    }

    public String getLastupdatetime()
    {
        return lastupdatetime;
    }
    public void setTs(Long ts)
    {
        this.ts = ts;
    }

    public Long getTs()
    {
        return ts;
    }
    public void setBagcellsnum(Long bagcellsnum)
    {
        this.bagcellsnum = bagcellsnum;
    }

    public Long getBagcellsnum()
    {
        return bagcellsnum;
    }
    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getIp()
    {
        return ip;
    }
    public void setMoney(Long money)
    {
        this.money = money;
    }

    public Long getMoney()
    {
        return money;
    }
    public void setGold(Long gold)
    {
        this.gold = gold;
    }

    public Long getGold()
    {
        return gold;
    }
    public void setIron(Long iron)
    {
        this.iron = iron;
    }

    public Long getIron()
    {
        return iron;
    }
    public void setIsDelete(Long isDelete)
    {
        this.isDelete = isDelete;
    }

    public Long getIsDelete()
    {
        return isDelete;
    }
    public void setRechargeGold(Long rechargeGold)
    {
        this.rechargeGold = rechargeGold;
    }

    public Long getRechargeGold()
    {
        return rechargeGold;
    }
    public void setFuncellUUid(String funcellUUid)
    {
        this.funcellUUid = funcellUUid;
    }

    public String getFuncellUUid()
    {
        return funcellUUid;
    }
    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformName()
    {
        return platformName;
    }
    public void setHorseLayer(Long horseLayer)
    {
        this.horseLayer = horseLayer;
    }

    public Long getHorseLayer()
    {
        return horseLayer;
    }
    public void setHorseIllusionLevel(Long horseIllusionLevel)
    {
        this.horseIllusionLevel = horseIllusionLevel;
    }

    public Long getHorseIllusionLevel()
    {
        return horseIllusionLevel;
    }
    public void setHorseIds(String horseIds)
    {
        this.horseIds = horseIds;
    }

    public String getHorseIds()
    {
        return horseIds;
    }
    public void setWingIds(String wingIds)
    {
        this.wingIds = wingIds;
    }

    public String getWingIds()
    {
        return wingIds;
    }
    public void setPetIds(String petIds)
    {
        this.petIds = petIds;
    }

    public String getPetIds()
    {
        return petIds;
    }
    public void setClotheStar(Long clotheStar)
    {
        this.clotheStar = clotheStar;
    }

    public Long getClotheStar()
    {
        return clotheStar;
    }
    public void setWeaponStar(Long weaponStar)
    {
        this.weaponStar = weaponStar;
    }

    public Long getWeaponStar()
    {
        return weaponStar;
    }
    public void setEquipAllStar(Long equipAllStar)
    {
        this.equipAllStar = equipAllStar;
    }

    public Long getEquipAllStar()
    {
        return equipAllStar;
    }
    public void setMoonCardDay(Long moonCardDay)
    {
        this.moonCardDay = moonCardDay;
    }

    public Long getMoonCardDay()
    {
        return moonCardDay;
    }
    public void setIsRecharge(Long isRecharge)
    {
        this.isRecharge = isRecharge;
    }

    public Long getIsRecharge()
    {
        return isRecharge;
    }
    public void setLastLoginTime(Long lastLoginTime)
    {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getLastLoginTime()
    {
        return lastLoginTime;
    }
    public void setFashionBodyId(Long fashionBodyId)
    {
        this.fashionBodyId = fashionBodyId;
    }

    public Long getFashionBodyId()
    {
        return fashionBodyId;
    }
    public void setFashionWeaponId(Long fashionWeaponId)
    {
        this.fashionWeaponId = fashionWeaponId;
    }

    public Long getFashionWeaponId()
    {
        return fashionWeaponId;
    }
    public void setLifeCard(Long lifeCard)
    {
        this.lifeCard = lifeCard;
    }

    public Long getLifeCard()
    {
        return lifeCard;
    }
    public void setClearlevel(Long clearlevel)
    {
        this.clearlevel = clearlevel;
    }

    public Long getClearlevel()
    {
        return clearlevel;
    }
    public void setCpUserId(String cpUserId)
    {
        this.cpUserId = cpUserId;
    }

    public String getCpUserId()
    {
        return cpUserId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("userId", getUserId())
            .append("roleName", getRoleName())
            .append("machineCode", getMachineCode())
            .append("platUserId", getPlatUserId())
            .append("clientOS", getClientOS())
            .append("level", getLevel())
            .append("sex", getSex())
            .append("career", getCareer())
            .append("createTime", getCreateTime())
            .append("onlineTime", getOnlineTime())
            .append("createsid", getCreatesid())
            .append("attack", getAttack())
            .append("lastupdatetime", getLastupdatetime())
            .append("ts", getTs())
            .append("bagcellsnum", getBagcellsnum())
            .append("ip", getIp())
            .append("money", getMoney())
            .append("gold", getGold())
            .append("iron", getIron())
            .append("isDelete", getIsDelete())
            .append("rechargeGold", getRechargeGold())
            .append("funcellUUid", getFuncellUUid())
            .append("platformName", getPlatformName())
            .append("horseLayer", getHorseLayer())
            .append("horseIllusionLevel", getHorseIllusionLevel())
            .append("horseIds", getHorseIds())
            .append("wingIds", getWingIds())
            .append("petIds", getPetIds())
            .append("clotheStar", getClotheStar())
            .append("weaponStar", getWeaponStar())
            .append("equipAllStar", getEquipAllStar())
            .append("moonCardDay", getMoonCardDay())
            .append("isRecharge", getIsRecharge())
            .append("lastLoginTime", getLastLoginTime())
            .append("fashionBodyId", getFashionBodyId())
            .append("fashionWeaponId", getFashionWeaponId())
            .append("lifeCard", getLifeCard())
            .append("clearlevel", getClearlevel())
            .append("cpUserId", getCpUserId())
            .toString();
    }
}
