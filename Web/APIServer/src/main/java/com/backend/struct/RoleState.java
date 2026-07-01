package com.backend.struct;


public class RoleState {
	/**
	 * 账号ID值
	 */
	private String userId;
	/**
	 * 角色ID值
	 */
	private String roleId;
	/**
	 * 角色名
	 */
	private String roleName;
	/**
	 * 等级
	 */
	private int level;
	/**
	 * 性别
	 */
	private int sex;
	/**
	 * 角色职业
	 */
	private int career;
	/**
	 * 创建服ID
	 */
	private int createsid;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 在线时长
	 */
	private int onlineTime;
	/**
	 * 更新的最新时间值
	 */
	private String lastupdatetime;
	/**
	 * 更新的最新秒值
	 */
	private long ts;
	/**
	 * 当前登录IP
	 */
	private String ip;
	/**
	 * 背包格子数量
	 */
	private int bagcellsnum;
	/**
	 * 战斗力
	 */
	private int fightpower;
	/**
	 * 铜币
	 */
	private int money;
	/**
	 * 元宝
	 */
	private int gold;
	/**
	 * 阵营
	 */
	private String groupNo;
	/**
	 * 铁精
	 */
	private int iron;
	/**
	 * 角色总充值获得元宝数
	 */
	private int rechargeGold;
	
	/**
	 * 角色总充值钱数
	 */
	private double amount;
	/**
	 * 充值货币类型
	 */
	private String currency;
	/**
	 * 是否删除
	 */
	private int isDelete;
	/**
	 * funcell生成的UUid
	 */
	private String funcellUUid;
	/**
	 * 渠道
	 */
	private String platformName;
	
	/**
	 * 坐骑阶数
	 */
	private int horseLayer;
	/**
	 * 坐骑幻化等级
	 */
	private int horseIllusionLevel;
	/**
	 * 所持神兵ID
	 */
	private int dragoon;
	/**
	 * 披风阶数
	 */
	private int cloak;
	/**
	 * 装备总星级
	 */
	private int equipMinStar;
	/**
	 * 月卡天数
	 */
	private int moonCardDay;
	/**
	 * 称号符文等级
	 */
	private int titleRuneLv;
	/**
	 * 成就符文Id
	 */
	private int AchRuneId;
	/**
	 * 成就符文等级
	 */
	private int AchRuneLv;
	/**
	 * 披风符文信息
	 */
	private String CloakRuneInfo;
	/**
	 * 是否充值
	 */
	private int isRecharge;
	/**
	 * 最后登录时间
	 */
	private int lastLoginTime;
	/**
	 * 穿戴时装ID值
	 */
	private int wearFashionId;
    /**
     * 机器码
     */
    private String machineCode;
	/**
	 * 客户端操作系统
	 */
	private String clientOS;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getCareer() {
		return career;
	}
	public void setCareer(int career) {
		this.career = career;
	}
	public int getCreatesid() {
		return createsid;
	}
	public void setCreatesid(int createsid) {
		this.createsid = createsid;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getOnlineTime() {
		return onlineTime;
	}
	public void setOnlineTime(int onlineTime) {
		this.onlineTime = onlineTime;
	}
	public String getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(String lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
	public long getTs() {
		return ts;
	}
	public void setTs(long ts) {
		this.ts = ts;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getBagcellsnum() {
		return bagcellsnum;
	}
	public void setBagcellsnum(int bagcellsnum) {
		this.bagcellsnum = bagcellsnum;
	}
	public int getFightpower() {
		return fightpower;
	}
	public void setFightpower(int fightpower) {
		this.fightpower = fightpower;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getIron() {
		return iron;
	}
	public void setIron(int iron) {
		this.iron = iron;
	}
	public int getRechargeGold() {
		return rechargeGold;
	}
	public void setRechargeGold(int rechargeGold) {
		this.rechargeGold = rechargeGold;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public String getFuncellUUid() {
		return funcellUUid;
	}
	public void setFuncellUUid(String funcellUUid) {
		this.funcellUUid = funcellUUid;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public int getHorseLayer() {
		return horseLayer;
	}
	public void setHorseLayer(int horseLayer) {
		this.horseLayer = horseLayer;
	}
	public int getHorseIllusionLevel() {
		return horseIllusionLevel;
	}
	public void setHorseIllusionLevel(int horseIllusionLevel) {
		this.horseIllusionLevel = horseIllusionLevel;
	}
	public int getDragoon() {
		return dragoon;
	}
	public void setDragoon(int dragoon) {
		this.dragoon = dragoon;
	}
	public int getCloak() {
		return cloak;
	}
	public void setCloak(int cloak) {
		this.cloak = cloak;
	}
	public int getEquipMinStar() {
		return equipMinStar;
	}
	public void setEquipMinStar(int equipMinStar) {
		this.equipMinStar = equipMinStar;
	}
	public int getMoonCardDay() {
		return moonCardDay;
	}
	public void setMoonCardDay(int moonCardDay) {
		this.moonCardDay = moonCardDay;
	}
	public int getTitleRuneLv() {
		return titleRuneLv;
	}
	public void setTitleRuneLv(int titleRuneLv) {
		this.titleRuneLv = titleRuneLv;
	}
	public int getAchRuneId() {
		return AchRuneId;
	}
	public void setAchRuneId(int achRuneId) {
		AchRuneId = achRuneId;
	}
	public int getAchRuneLv() {
		return AchRuneLv;
	}
	public void setAchRuneLv(int achRuneLv) {
		AchRuneLv = achRuneLv;
	}
	public String getCloakRuneInfo() {
		return CloakRuneInfo;
	}
	public void setCloakRuneInfo(String cloakRuneInfo) {
		CloakRuneInfo = cloakRuneInfo;
	}
	public int getIsRecharge() {
		return isRecharge;
	}
	public void setIsRecharge(int isRecharge) {
		this.isRecharge = isRecharge;
	}
	public int getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(int lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public int getWearFashionId() {
		return wearFashionId;
	}
	public void setWearFashionId(int wearFashionId) {
		this.wearFashionId = wearFashionId;
	}
    public String getMachineCode() {
        return machineCode;
    }
    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

	public String getClientOS() {
		return clientOS;
	}

	public void setClientOS(String clientOS) {
		this.clientOS = clientOS;
	}
}
