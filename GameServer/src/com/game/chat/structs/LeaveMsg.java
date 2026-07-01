package com.game.chat.structs;

import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import game.core.util.TimeUtils;
import game.message.ChatMessage.LeaveMessage;

/**
 * @author hewei@haowan123.com
 */
public class LeaveMsg {

    private long sendId;            // 发送人id
    private String sendName;        // 发送人名字
    private int vipLv;              // 发送人VIP等级
    private String condition;       // 聊天文本消息
    private int time;               // 发送时间
    private int career = 0;         //职业
    private int moonandlife = 0;    //月卡并且终身卡
    private int lv = 0;
    private int iconState = 0;
    private int headId = 0;         //发送玩家的头像ID
    private int headRoundId = 0;    //发送玩家的头像框ID
    private int bubbleId = 0;       //气泡id
    private long receiverId;        //接收者id
    private int channel;            //频道
    private int chatSid;            //服务器id

    /**
     * 系统id
     */
    private int systemId;


    private String customHeadPath; //自定义头像路径
    private boolean useCustomHead; // 是否使用自定义头像 1 表示使用

    public long getSendId() {
        return sendId;
    }

    public void setSendId(long sendId) {
        this.sendId = sendId;
    }

    public String getSendName() {
        if (Manager.registerManager.getRoleName(sendId) == null) {
            return sendName;
        }
        return Manager.registerManager.getRoleName(sendId);
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public int getVipLv() {
        return vipLv;
    }

    public void setVipLv(int vipLv) {
        this.vipLv = vipLv;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public int getMoonandlife() {
        return moonandlife;
    }

    public void setMoonandlife(int moonandlife) {
        this.moonandlife = moonandlife;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public int getIconState() {
        return iconState;
    }

    public void setIconState(int iconState) {
        this.iconState = iconState;
    }

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    public int getHeadRoundId() {
        return headRoundId;
    }

    public void setHeadRoundId(int headRoundId) {
        this.headRoundId = headRoundId;
    }

    public int getBubbleId() {
        return bubbleId;
    }

    public void setBubbleId(int bubbleId) {
        this.bubbleId = bubbleId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getChatSid() {
        return chatSid;
    }

    public void setChatSid(int chatSid) {
        this.chatSid = chatSid;
    }

    public LeaveMessage.Builder toBuildInfo() {
        LeaveMessage.Builder msg = LeaveMessage.newBuilder();
        msg.setChater(sendId);
        msg.setChatername(sendName);
        msg.setCondition(condition);
        msg.setTime(time);
        msg.setVipLv(vipLv);
        msg.setCareer(career);
        msg.setMoonandOver(moonandlife);
        msg.setLevel(lv);
        msg.setIconState(iconState);
        //msg.setHeadFrameId(headRoundId);
        //msg.setHeadId(headId);
        msg.setHead(MapUtils.getHead(headId,headRoundId,customHeadPath,useCustomHead));

        msg.setChatBgId(bubbleId);
        msg.setReceiverId(receiverId);
        msg.setChannel(channel);
        if (chatSid != 0) {
            msg.setChaterSid(chatSid);
        }
        msg.setSystemId(this.systemId);
        return msg;
    }
    public  void fromBuildInfo(LeaveMessage msg) {
        this.sendId = msg.getChater();
        this.sendName = msg.getChatername();
        this.condition = msg.getCondition();
        this.time = msg.getTime();
        this.vipLv = msg.getVipLv();
        this.career = msg.getCareer();
        this.moonandlife = msg.getMoonandOver();
        this.lv = msg.getLevel();
        this.iconState = msg.getIconState();

        if(msg.getHead() != null){

            this.headRoundId = msg.getHead().getFashionFrame();
            this.headId = msg.getHead().getFashionHead();
            this.customHeadPath = msg.getHead().getCustomHeadPath();
            this.useCustomHead = msg.getHead().getUseCustomHead();
        }

        this.bubbleId = msg.getChatBgId();
        this.receiverId = msg.getReceiverId();
        this.channel = msg.getChannel();
        if (chatSid != 0) {
            this.chatSid = msg.getChaterSid();
        }
        this.systemId = msg.getSystemId();

    }
//    public static LeaveMsg makeLeaveMsg(PlayerWorldInfo player, String ss, long receiverId, int channel) {
//        LeaveMsg msg = new LeaveMsg();
//        msg.setCondition(ss);
//        msg.setSendId(player.getRoleid());
//        msg.setSendName(player.getRolename());
//        msg.setTime((int) (TimeUtils.Time() / 1000));
//        msg.setVipLv(0);
//        msg.setCareer(player.getCareer());
//        //msg.setMoonandlife(player.moonandOverCard());
//        msg.setLv(player.getLevel());
//      //  msg.setHeadRoundId(player.gethe);
//        msg.setHeadId(player.getFashionHeadId());
//       // msg.setBubbleId(Manager.newFashionManager.deal().getQiPao(player));
//        msg.setReceiverId(receiverId);
//        msg.setChannel(channel);
//        return msg;
//    }


    public static LeaveMsg makeLeaveMsg(Player player,int systemId, String ss, long receiverId, ChatChannel channel) {
        LeaveMsg msg = new LeaveMsg();
        msg.setCondition(ss);
        msg.setSendId(player.getId());
        msg.setSendName(player.getName());
        msg.setTime((int) (TimeUtils.Time() / 1000));
        msg.setVipLv(0);
        msg.setCareer(player.getCareer());
        msg.setMoonandlife(player.moonandOverCard());
        msg.setLv(player.getLevel());

        msg.setHeadRoundId(Manager.newFashionManager.deal().getHeadBox(player));
        msg.setHeadId(Manager.newFashionManager.deal().getHead(player));
        msg.setCustomHeadPath(player.getCustomHeadPath());
        msg.setUseCustomHead(player.isUseCustomHead());


        msg.setBubbleId(Manager.newFashionManager.deal().getQiPao(player));
        msg.setReceiverId(receiverId);
        msg.setChannel(channel.getChannel());
        msg.setSystemId(systemId);
        return msg;
    }

    public static LeaveMsg makeLeaveMsg(long id, String name, int career, int level,int systemId, String ss, long receiverId, ChatChannel channel, int sid, int bubbleId) {
        LeaveMsg msg = new LeaveMsg();
        msg.setCondition(ss);
        msg.setSendId(id);
        msg.setSendName(name);
        msg.setTime((int) (TimeUtils.Time() / 1000));
        msg.setVipLv(0);
        msg.setCareer(career);
        msg.setMoonandlife(0);
        msg.setLv(level);
        msg.setBubbleId(bubbleId);
        msg.setReceiverId(receiverId);
        msg.setChannel(channel.getChannel());
        msg.setChatSid(sid);
        msg.setSystemId(systemId);
        return msg;
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
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
}
