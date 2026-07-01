package com.game.pet.struct;

import com.game.cooldown.structs.Cooldown;
import com.game.map.structs.BaseNpc;
import com.game.player.structs.Player;
import com.game.skill.structs.Skill;
import com.game.structs.Position;
import com.game.utils.MapUtils;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 宠物
 */
public class Pet extends BaseNpc {

    private final Logger log = LogManager.getLogger(Pet.class);
    /**
     * 主人ID
     */
    private long ownerId;

    /**
     * 主人名字
     */
    private String ownerName;

    /**
     * 宠物打架技能
     */
    private ConcurrentHashMap<Integer, Skill> baseSkills = new ConcurrentHashMap<>();

    /**
     * 宠物带给玩家的手动技能
     */
    private int manualSkill;

    /**
     * 宠物的被动技能
     */
    private List<Integer> passivitySkill = new ArrayList<>();

    /**
     * 技能冷却列表
     */
    private transient ConcurrentHashMap<String, Cooldown> coolDown = new ConcurrentHashMap<>();

    /**
     * 阶数
     */
    private int stage = 1;

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public ConcurrentHashMap<Integer, Skill> getBaseSkills() {
        return baseSkills;
    }

    public void setBaseSkills(ConcurrentHashMap<Integer, Skill> baseSkills) {
        this.baseSkills = baseSkills;
    }

    public int getManualSkill() {
        return manualSkill;
    }

    public void setManualSkill(int manualSkill) {
        this.manualSkill = manualSkill;
    }

    public List<Integer> getPassivitySkill() {
        return passivitySkill;
    }

    public void setPassivitySkill(List<Integer> passivitySkill) {
        this.passivitySkill = passivitySkill;
    }

    public ConcurrentHashMap<String, Cooldown> getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(ConcurrentHashMap<String, Cooldown> coolDown) {
        this.coolDown = coolDown;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    @Override
    protected void onForceStopMove() {

    }

    public void followMaster(Player master){
//        if(MapUtils.getDistance(master.getCurPos(), this.curPos) > 3){
//            super.MoveAi_MoveToPos(master.getCurPos());
           // sendPetMoveMsg(master);
//        }
    }

    public void sendPetMoveMsg(Player master) {
        MapMessage.ReqPetMoveTo.Builder msg = MapMessage.ReqPetMoveTo.newBuilder();
        msg.setCurPos(master.getCurPos().toPosition());
        msg.setMapId(master.getMapModelId());
        List<Position> pPoss = new ArrayList<>();
        if(!master.getRoads().isEmpty()&&master.getRoads().size()>2){
            pPoss = master.getRoads().subList(0,master.getRoads().size()-2);
        }
        for (Position pPos : pPoss) {
            msg.addPosList(pPos.toPosition());
        }
//        log.info("宠物请求移动，寻路点数："+msg.getPosListCount()+",宠物位置："+this.curPos+",角色位置："+master.getCurPos());
        master.sendMsg(MapMessage.ReqPetMoveTo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
