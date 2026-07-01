package com.game.db.bean;

import game.core.db.BaseBean;

/**
 * 排行榜玩家信息
 */
public class RankPlayer extends BaseBean {

    private long roleId;                        //角色Id

    private String name;                        //名字，角色名或公会名

    private boolean guildFlag;                  //是否是公会数据

    private byte career;                        //角色职业

    private long createTime;                    //角色创建时间

    private int createSid;                      //角色创建区服

    private int level;                          //角色等级

    private int levelUpTime;                    //上次同步等级和经验排行榜的时间

    private long fightPower;                    //战斗力

    private int horseId;                        //坐骑最高阶

    private int horseFightPoint;                //坐骑带来的战斗力加成

    private int wingId;                         //翅膀最高阶

    private int wingFightPoint;                 //翅膀总战力

    private int beWorshipedNum;                 //被崇拜次数

    private long exp;                           //经验值

    private int equipWashPer;                   //装备洗练等级

    private int equipStrengthenLv;              //装备强化等级

    private int equipFightPower;                //装备、部位的战力之和

    private int gemFightPower;                  //宝石战力

    private int gemLv;                          //宝石总等级

    private int magicWeaponDamage;              //法宝伤害

    private int talismanFightPower;             //法器战力

    private int magicFightPower;                //阵法战力

    private int weaponFightPower;               //神兵战力

    private int strengthenFightPower;           //强化战力

    private long offlineEfficiency;             //脱机效率

    private int shihai;                         //石海层数

    private int charm;                          //魅力值

    private int sendFlower;                     //赠送花的总数

    private int arenaRank;                      //竞技场排名

    private long topHallFightPower;             //名人堂称号战力

    private long universeFightPower;            //天墟战场名人堂战力

    private int equipStar;                      //穿戴装备的星级(6阶以上)

    private int equipStarGradeNum;              //穿戴6阶以上装备件数

    private int equipAllStar;                   //装备灵体总星级

    private int petFightPower;                  //宠物战力

    private int spiritFightPower;               //灵体战力

    private int immEquipFightPower;             //仙甲战力

    private int holyEquipFightPower;            //圣装战力

    private int monsterFightPower;              //神兽战力

    private int petSoulLv;                      //TODO  宠物御魂等级 修改宠物战力

    private int petLv;                          //宠物等级

    private int horseSoulLv;                    //TODO 坐骑御魂等级  修改为坐骑御魂战力

    private int horseLv;                        //坐骑等级

    private int consumeGold;                    //消耗金元宝

    private int soulFight;                      //魂甲战力

    private int baguaPower;                     //八卦战力

    private int immortalsoulPower;              //灵魂战力

    private int devilSoulPower;                 //魔魂战力

    private int horseEquipPower;                //坐骑脉轮（装备）战力

    private int flySwordPower;                  //剑灵战力

    private int marryChildPower;                //仙娃战力

    private int intimacy;                       //亲密度

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public byte getCareer() {
        return career;
    }

    public void setCareer(byte career) {
        this.career = career;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getHorseFightPoint() {
        return horseFightPoint;
    }

    public void setHorseFightPoint(int horseFightPoint) {
        this.horseFightPoint = horseFightPoint;
    }

    public int getCreateSid() {
        return createSid;
    }

    public void setCreateSid(int createSid) {
        this.createSid = createSid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevelUpTime() {
        return levelUpTime;
    }

    public void setLevelUpTime(int levelUpTime) {
        this.levelUpTime = levelUpTime;
    }

    public long getFightPower() {
        return fightPower;
    }

    public void setFightPower(long fightPower) {
        this.fightPower = fightPower;
    }

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public int getWingId() {
        return wingId;
    }

    public void setWingId(int wingId) {
        this.wingId = wingId;
    }

    public int getBeWorshipedNum() {
        return beWorshipedNum;
    }

    public void setBeWorshipedNum(int beWorshipedNum) {
        this.beWorshipedNum = beWorshipedNum;
    }

    public String getRoleName() {
        return this.name;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public int getWingFightPoint() {
        return wingFightPoint;
    }

    public void setWingFightPoint(int wingFightPoint) {
        this.wingFightPoint = wingFightPoint;
    }

    public int getEquipWashPer() {
        return equipWashPer;
    }

    public void setEquipWashPer(int equipWashPer) {
        this.equipWashPer = equipWashPer;
    }

    public int getEquipStrengthenLv() {
        return equipStrengthenLv;
    }

    public void setEquipStrengthenLv(int equipStrengthenLv) {
        this.equipStrengthenLv = equipStrengthenLv;
    }

    public int getEquipFightPower() {
        return equipFightPower;
    }

    public void setEquipFightPower(int equipFightPower) {
        this.equipFightPower = equipFightPower;
    }

    public int getGemFightPower() {
        return gemFightPower;
    }

    public void setGemFightPower(int gemFightPower) {
        this.gemFightPower = gemFightPower;
    }

    public int getGemLv() {
        return gemLv;
    }

    public void setGemLv(int gemLv) {
        this.gemLv = gemLv;
    }

    public int getMagicWeaponDamage() {
        return magicWeaponDamage;
    }

    public void setMagicWeaponDamage(int magicWeaponDamage) {
        this.magicWeaponDamage = magicWeaponDamage;
    }

    public int getMagicFightPower() {
        return magicFightPower;
    }

    public int getTalismanFightPower() {
        return talismanFightPower;
    }

    public void setMagicFightPower(int magicFightPower) {
        this.magicFightPower = magicFightPower;
    }

    public void setTalismanFightPower(int talismanFightPower) {
        this.talismanFightPower = talismanFightPower;
    }

    public int getWeaponFightPower() {
        return weaponFightPower;
    }

    public void setWeaponFightPower(int weaponFightPower) {
        this.weaponFightPower = weaponFightPower;
    }

    public int getStrengthenFightPower() {
        return strengthenFightPower;
    }

    public void setStrengthenFightPower(int strengthenFightPower) {
        this.strengthenFightPower = strengthenFightPower;
    }

    public long getOfflineEfficiency() {
        return offlineEfficiency;
    }

    public void setOfflineEfficiency(long offlineEfficiency) {
        this.offlineEfficiency = offlineEfficiency;
    }

    public int getShihai() {
        return shihai;
    }

    public void setShihai(int shihai) {
        this.shihai = shihai;
    }

    public int getCharm() {
        return charm;
    }

    public void setCharm(int charm) {
        this.charm = charm;
    }

    public int getSendFlower() {
        return sendFlower;
    }

    public void setSendFlower(int sendFlower) {
        this.sendFlower = sendFlower;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getGuildFlag() {
        return guildFlag;
    }

    public void setGuildFlag(boolean guildFlag) {
        this.guildFlag = guildFlag;
    }

    public int getArenaRank() {
        return arenaRank;
    }

    public void setArenaRank(int arenaRank) {
        this.arenaRank = arenaRank;
    }

    public long getTopHallFightPower() {
        return topHallFightPower;
    }

    public void setTopHallFightPower(long topHallFightPower) {
        this.topHallFightPower = topHallFightPower;
    }

    public long getUniverseFightPower() {
        return universeFightPower;
    }

    public void setUniverseFightPower(long universeFightPower) {
        this.universeFightPower = universeFightPower;
    }

    public int getEquipStar() {
        return equipStar;
    }

    public void setEquipStar(int equipStar) {
        this.equipStar = equipStar;
    }

    public int getEquipStarGradeNum() {
        return equipStarGradeNum;
    }

    public void setEquipStarGradeNum(int equipStarGradeNum) {
        this.equipStarGradeNum = equipStarGradeNum;
    }

    public int getEquipAllStar() {
        return equipAllStar;
    }

    public void setEquipAllStar(int equipAllStar) {
        this.equipAllStar = equipAllStar;
    }

    public int getPetFightPower() {
        return petFightPower;
    }

    public void setPetFightPower(int petFightPower) {
        this.petFightPower = petFightPower;
    }

    public int getSpiritFightPower() {
        return spiritFightPower;
    }

    public void setSpiritFightPower(int spiritFightPower) {
        this.spiritFightPower = spiritFightPower;
    }

    public int getImmEquipFightPower() {
        return immEquipFightPower;
    }

    public void setImmEquipFightPower(int immEquipFightPower) {
        this.immEquipFightPower = immEquipFightPower;
    }

    public int getHolyEquipFightPower() {
        return holyEquipFightPower;
    }

    public void setHolyEquipFightPower(int holyEquipFightPower) {
        this.holyEquipFightPower = holyEquipFightPower;
    }

    public int getMonsterFightPower() {
        return monsterFightPower;
    }

    public void setMonsterFightPower(int monsterFightPower) {
        this.monsterFightPower = monsterFightPower;
    }

    public int getPetSoulLv() {
        return petSoulLv;
    }

    public void setPetSoulLv(int petSoulLv) {
        this.petSoulLv = petSoulLv;
    }

    public int getPetLv() {
        return petLv;
    }

    public void setPetLv(int petLv) {
        this.petLv = petLv;
    }

    public int getHorseSoulLv() {
        return horseSoulLv;
    }

    public void setHorseSoulLv(int horseSoulLv) {
        this.horseSoulLv = horseSoulLv;
    }

    public int getHorseLv() {
        return horseLv;
    }

    public void setHorseLv(int horseLv) {
        this.horseLv = horseLv;
    }

    public int getConsumeGold() {
        return consumeGold;
    }

    public void setConsumeGold(int consumeGold) {
        this.consumeGold = consumeGold;
    }

    public void addConsumeGold(int add) {
        this.consumeGold += add;
    }

    public int getSoulFight() {
        return soulFight;
    }

    public void setSoulFight(int soulFight) {
        this.soulFight = soulFight;
    }

    public int getBaguaPower() {
        return baguaPower;
    }

    public void setBaguaPower(int baguaPower) {
        this.baguaPower = baguaPower;
    }

    public int getImmortalsoulPower() {
        return immortalsoulPower;
    }

    public void setImmortalsoulPower(int immortalsoulPower) {
        this.immortalsoulPower = immortalsoulPower;
    }

    public int getDevilSoulPower() {
        return devilSoulPower;
    }

    public void setDevilSoulPower(int devilSoulPower) {
        this.devilSoulPower = devilSoulPower;
    }

    public int getHorseEquipPower() {
        return horseEquipPower;
    }

    public void setHorseEquipPower(int horseEquipPower) {
        this.horseEquipPower = horseEquipPower;
    }

    public int getFlySwordPower() {
        return flySwordPower;
    }

    public void setFlySwordPower(int flySwordPower) {
        this.flySwordPower = flySwordPower;
    }

    public int getMarryChildPower() {
        return marryChildPower;
    }

    public void setMarryChildPower(int marryChildPower) {
        this.marryChildPower = marryChildPower;
    }

    public int getIntimacy() {
        return intimacy;
    }

    public void setIntimacy(int intimacy) {
        this.intimacy = intimacy;
    }
}
