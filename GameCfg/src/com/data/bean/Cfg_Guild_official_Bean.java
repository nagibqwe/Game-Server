/**
 * Auto generated, do not edit it
 *
 * guild_official配置表
 */
package com.data.bean;

	
public class Cfg_Guild_official_Bean{
    /**
     * 官职（1普通成员，2长老，3副会长，4会长）
     */
    private final int level;
    /**
     * 官职（1普通成员，2长老，3副会长，4会长）
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 修改公告（0不能1能）
     */
    private final int canNotice;
    /**
     * 修改公告（0不能1能）
     * @return
     */
    public final int getCanNotice(){
        return canNotice;
    }
    /**
     * 官职人数限制(0无限制）
     */
    private final int num;
    /**
     * 官职人数限制(0无限制）
     * @return
     */
    public final int getNum(){
        return num;
    }
    /**
     * 同意其他玩家进入公会（0不能1能）
     */
    private final int canAgree;
    /**
     * 同意其他玩家进入公会（0不能1能）
     * @return
     */
    public final int getCanAgree(){
        return canAgree;
    }
    /**
     * 踢人（0不能1能）
     */
    private final int canKick;
    /**
     * 踢人（0不能1能）
     * @return
     */
    public final int getCanKick(){
        return canKick;
    }
    /**
     * 升级建筑（0不能1能）
     */
    private final int canUp;
    /**
     * 升级建筑（0不能1能）
     * @return
     */
    public final int getCanUp(){
        return canUp;
    }
    /**
     * 修改申请设置（0不能1能）
     */
    private final int canAlter;
    /**
     * 修改申请设置（0不能1能）
     * @return
     */
    public final int getCanAlter(){
        return canAlter;
    }
    /**
     * 官职任免(0不能1能)
     */
    private final int canSetOfficial;
    /**
     * 官职任免(0不能1能)
     * @return
     */
    public final int getCanSetOfficial(){
        return canSetOfficial;
    }
    /**
     * 喊话招人
     */
    private final int canHan;
    /**
     * 喊话招人
     * @return
     */
    public final int getCanHan(){
        return canHan;
    }
    /**
     * 是否能参加帮会战
     */
    private final int guild_fighting;
    /**
     * 是否能参加帮会战
     * @return
     */
    public final int getGuild_fighting(){
        return guild_fighting;
    }
    /**
     * 跨服公会联赛报名权限
     */
    private final int kuafu_match;
    /**
     * 跨服公会联赛报名权限
     * @return
     */
    public final int getKuafu_match(){
        return kuafu_match;
    }
    /**
     * 语音权限（任命指挥）
     */
    private final int Voice;
    /**
     * 语音权限（任命指挥）
     * @return
     */
    public final int getVoice(){
        return Voice;
    }
    /**
     * 仙盟战领奖权限
（0不能1能）
     */
    private final int warRewardLimit;
    /**
     * 仙盟战领奖权限
（0不能1能）
     * @return
     */
    public final int getWarRewardLimit(){
        return warRewardLimit;
    }

    public Cfg_Guild_official_Bean(int level,int canNotice,int num,int canAgree,int canKick,int canUp,int canAlter,int canSetOfficial,int canHan,int guild_fighting,int kuafu_match,int Voice,int warRewardLimit){
        this.level = level;
        this.canNotice = canNotice;
        this.num = num;
        this.canAgree = canAgree;
        this.canKick = canKick;
        this.canUp = canUp;
        this.canAlter = canAlter;
        this.canSetOfficial = canSetOfficial;
        this.canHan = canHan;
        this.guild_fighting = guild_fighting;
        this.kuafu_match = kuafu_match;
        this.Voice = Voice;
        this.warRewardLimit = warRewardLimit;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("level:").append(level).append(";");
        str.append("canNotice:").append(canNotice).append(";");
        str.append("num:").append(num).append(";");
        str.append("canAgree:").append(canAgree).append(";");
        str.append("canKick:").append(canKick).append(";");
        str.append("canUp:").append(canUp).append(";");
        str.append("canAlter:").append(canAlter).append(";");
        str.append("canSetOfficial:").append(canSetOfficial).append(";");
        str.append("canHan:").append(canHan).append(";");
        str.append("guild_fighting:").append(guild_fighting).append(";");
        str.append("kuafu_match:").append(kuafu_match).append(";");
        str.append("Voice:").append(Voice).append(";");
        str.append("warRewardLimit:").append(warRewardLimit).append(";");
        return str.toString();
    }
}
