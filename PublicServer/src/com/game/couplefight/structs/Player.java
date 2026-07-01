package com.game.couplefight.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game.message.CommonMessage;
import game.message.CouplefightMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/7/6 20:57
 */
public class Player {

    /**id*/
    private long id;
    /**name*/
    private String name;
    /**战斗力*/
    private long power;
    /**职业*/
    private int career;
    /**等级*/
    private int level;
    /**预选赛奖励获取记录*/
    private List<Integer> trialsAward = new ArrayList<>();
    /**头像*/
    private int fashionHead; //时装头像
    private int fashionFrame; //时装头像框
    private String customHeadPath; //自定义头像路径
    private boolean useCustomHead; // 是否使用自定义头像 1 表示使用
    /**外观*/
    private int fashionBody;              //时装衣服
    private int fashionWeapon;              //时装武器
    private int fashionHalo;              //时装光环
    private int fashionMatrix;              //时装阵法
    private int wingId;              //翅膀
    private int spiritId;              //灵体阶数
    private int soulArmorId;

    /**玩家的队伍*/
    @JsonIgnore
    private transient CoupleTeam team;
    /**匹配确认状态*/
    @JsonIgnore
    private transient Boolean confirm;

    public Player(){}

    public Player(long id, String name, long power){
        this.id = id;
        this.name = name;
        this.power = power;
    }

    public Player(CouplefightMessage.PlayerInfo info){
        this.id = info.getId();
        update(info);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPower() {
        return power;
    }

    public void setPower(long power) {
        this.power = power;
    }

    public CoupleTeam getTeam() {
        return team;
    }

    public void setTeam(CoupleTeam team) {
        this.team = team;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public List<Integer> getTrialsAward() {
        return trialsAward;
    }

    public void setTrialsAward(List<Integer> trialsAward) {
        this.trialsAward = trialsAward;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }

    public int getFashionHead() {
        return fashionHead;
    }

    public void setFashionHead(int fashionHead) {
        this.fashionHead = fashionHead;
    }

    public int getFashionFrame() {
        return fashionFrame;
    }

    public void setFashionFrame(int fashionFrame) {
        this.fashionFrame = fashionFrame;
    }

    public String getCustomHeadPath() {
        return customHeadPath;
    }

    public void setCustomHeadPath(String customHeadPath) {
        this.customHeadPath = customHeadPath;
    }

    public boolean isUseCustomHead() {
        return useCustomHead;
    }

    public void setUseCustomHead(boolean useCustomHead) {
        this.useCustomHead = useCustomHead;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFashionBody() {
        return fashionBody;
    }

    public void setFashionBody(int fashionBody) {
        this.fashionBody = fashionBody;
    }

    public int getFashionWeapon() {
        return fashionWeapon;
    }

    public void setFashionWeapon(int fashionWeapon) {
        this.fashionWeapon = fashionWeapon;
    }

    public int getFashionHalo() {
        return fashionHalo;
    }

    public void setFashionHalo(int fashionHalo) {
        this.fashionHalo = fashionHalo;
    }

    public int getFashionMatrix() {
        return fashionMatrix;
    }

    public void setFashionMatrix(int fashionMatrix) {
        this.fashionMatrix = fashionMatrix;
    }

    public int getWingId() {
        return wingId;
    }

    public void setWingId(int wingId) {
        this.wingId = wingId;
    }

    public int getSpiritId() {
        return spiritId;
    }

    public void setSpiritId(int spiritId) {
        this.spiritId = spiritId;
    }

    public int getSoulArmorId() {
        return soulArmorId;
    }

    public void setSoulArmorId(int soulArmorId) {
        this.soulArmorId = soulArmorId;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public CouplefightMessage.PlayerInfo.Builder toProto(){
        CouplefightMessage.PlayerInfo.Builder pb = CouplefightMessage.PlayerInfo.newBuilder();
        pb.setId(this.id);
        pb.setName(this.name);
        pb.setPower(this.power);
        pb.setOccupation(this.career);
        pb.setLevel(this.level);
        pb.setHead(toHeadProto());
        pb.setFacade(toFacade());
        return pb;
    }

    private CommonMessage.FacadeAttribute.Builder toFacade() {
        CommonMessage.FacadeAttribute.Builder facade = CommonMessage.FacadeAttribute.newBuilder();
        facade.setFashionBody(this.fashionBody);
        facade.setFashionHalo(this.fashionHalo);
        facade.setFashionMatrix(this.fashionMatrix);
        facade.setFashionWeapon(this.fashionWeapon);
        facade.setSpiritId(this.spiritId);
        facade.setSoulArmorId(this.soulArmorId);
        facade.setWingId(wingId);
        return facade;
    }

    private CommonMessage.HeadAttribute toHeadProto() {
        CommonMessage.HeadAttribute.Builder head = CommonMessage.HeadAttribute.newBuilder();
        head.setCustomHeadPath(this.customHeadPath);
        head.setFashionHead(this.fashionHead);
        head.setFashionFrame(this.fashionFrame);
        head.setUseCustomHead(this.useCustomHead);
        return head.build();
    }

    public void update(CouplefightMessage.PlayerInfo playerInfo) {
        setName(playerInfo.getName());
        setPower(playerInfo.getPower());
        setLevel(playerInfo.getLevel());
        setCareer(playerInfo.getOccupation());

        CommonMessage.HeadAttribute head = playerInfo.getHead();
        if(playerInfo.hasHead()){
            this.fashionFrame = head.getFashionFrame();
            this.fashionHead = head.getFashionHead();
            this.useCustomHead = head.getUseCustomHead();
            this.customHeadPath = head.getCustomHeadPath();
        }
        if(playerInfo.hasFacade()){
            CommonMessage.FacadeAttribute facade = playerInfo.getFacade();
            this.fashionBody = facade.getFashionBody();
            this.fashionHalo = facade.getFashionHalo();
            this.fashionMatrix = facade.getFashionMatrix();
            this.fashionWeapon = facade.getFashionWeapon();
            this.soulArmorId = facade.getSoulArmorId();
            this.spiritId = facade.getSpiritId();
            this.wingId = facade.getWingId();
        }
    }
}
