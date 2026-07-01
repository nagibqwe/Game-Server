package com.game.horse.structs;

import com.data.CfgManager;
import com.data.bean.Cfg_NatureHorse_Bean;
import com.game.nature.structs.Nature;
import com.game.structs.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Administrator
 */
public class Horse extends GameObject {
    /**
     * 玩家当前的坐骑(有换乘低阶坐骑功能需求，此换乘只换坐骑外观造型，数值不换)，如果rideType=1,则此表示骑乘的神兽Id
     * 代表坐骑或神兽的Id(不能改成rideId，要兼容已拥有坐骑的老玩家，改了则从数据库中curLayer解析不到rideId上，然后rideId默认为0，玩家就丢了坐骑)
     * */
    private int curLayer;
    /**
     * 玩家是否骑乘的别人的坐骑
     * */
    private boolean rideOther;
    /**
     * 坐骑当前骑乘状态，0：未骑乘，1：骑乘中
     * */
    private HorseRideStateEnum rideState = HorseRideStateEnum.UnRide;
    /**
     * 坐骑培养，御魂，化形
     * */
    private Nature nature = new Nature();
    /**
     * 被激活的多人坐骑
     * */
    private ConcurrentHashMap<Integer, MultiPlayerHorse> multiPlayerHorseMap = new ConcurrentHashMap<>();
    /**
     * 神兽额外属性Id列表
     * */
    private List<Integer> linkAttIdList = new ArrayList<>();

    /**
     * 坐骑脉轮
     */
    private ConcurrentHashMap<Integer, HorseEquip> equips = new ConcurrentHashMap<>();

    /**
     * 当前出战斗的脉轮
     */
    private int horseEquipactiveId;

    /**
     * 是否自动分解
     */
    private boolean autoDecompose = false;
    
    public ConcurrentHashMap<Integer, MultiPlayerHorse> getMultiPlayerHorseMap() {
	return multiPlayerHorseMap;
    }
    
    public void setMultiPlayerHorseMap(ConcurrentHashMap<Integer, MultiPlayerHorse> multiPlayerHorseMap) {    
        this.multiPlayerHorseMap = multiPlayerHorseMap;
    }

    public int getCurLayer() {
	return curLayer;
    }

    public void setCurLayer(int curLayer) {
	this.curLayer = curLayer;
    }

    public boolean isRideOther() {
        return rideOther;
    }

    public void setRideOther(boolean rideOther) {
        this.rideOther = rideOther;
    }

    public HorseRideStateEnum getRideState() {
        return rideState;
    }

    public void setRideState(HorseRideStateEnum rideState) {
        this.rideState = rideState;
    }

    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }

    public List<Integer> getLinkAttIdList() {
        return linkAttIdList;
    }

    public void setLinkAttIdList(List<Integer> linkAttIdList) {
        this.linkAttIdList = linkAttIdList;
    }
    /**
     * 由于太多地方需要坐骑的模型id，故增加一个接口，减少重复代码，提高代码扩展性
     * */
    public int getHorseModelId() {
        return nature.getCurrentModelId();
    }
    /**
     * 由于太多地方需要坐骑的id即配置表id，故增加一个接口，减少重复代码，提高代码扩展性
     * */
    public int getHorseId() {return nature.getCurrentId();}
    /**
     * 由于太多地方需要坐骑的星数，故增加一个接口，减少重复代码，提高代码扩展性
     * */
    public int getHorseStar() {
        if(0 == nature.getCurrentId()) {
            return 0;
        }
        Cfg_NatureHorse_Bean bean = CfgManager.getCfg_NatureHorse_Container().getValueByKey(nature.getCurrentId());
        return null == bean ? 0 : bean.getStar();
    }
    /**
     * 由于太多地方需要坐骑的阶数，故增加一个接口，减少重复代码，提高代码扩展性
     * */
    public int getHorseSteps() {
        if(0 == nature.getCurrentId()) {
            return 0;
        }
        Cfg_NatureHorse_Bean bean = CfgManager.getCfg_NatureHorse_Container().getValueByKey(nature.getCurrentId());
        return null == bean ? 0 : bean.getSteps();
    }

    public ConcurrentHashMap<Integer, HorseEquip> getEquips() {
        return equips;
    }

    public void setEquips(ConcurrentHashMap<Integer, HorseEquip> equips) {
        this.equips = equips;
    }

    public boolean isAutoDecompose() {
        return autoDecompose;
    }

    public void setAutoDecompose(boolean autoDecompose) {
        this.autoDecompose = autoDecompose;
    }

    public int getHorseEquipactiveId() {
        return horseEquipactiveId;
    }

    public void setHorseEquipactiveId(int horseEquipactiveId) {
        this.horseEquipactiveId = horseEquipactiveId;
    }

    /**
     * 获取总装备评分
     * @return 装备评分
     */
    public int getHorseEquipTotalScore(){
        int score = 0;
        for(HorseEquip e: equips.values()){
            score += e.getScore();
        }
        return score;
    }

    @Override
    public void release() {

    }
}
