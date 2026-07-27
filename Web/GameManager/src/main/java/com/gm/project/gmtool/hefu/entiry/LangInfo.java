package com.gm.project.gmtool.hefu.entiry;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/9/10 18:14
 */
public class LangInfo {
    /**语言*/
    private String lang;
    /**发送者*/
    private String sender;
    /**角色-标题*/
    private String roleTitle;
    /**角色-内容*/
    private String roleContent;
    /**仙盟-标题*/
    private String guildTitle;
    /**仙盟-内容*/
    private String guildContent;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public String getRoleContent() {
        return roleContent;
    }

    public void setRoleContent(String roleContent) {
        this.roleContent = roleContent;
    }

    public String getGuildTitle() {
        return guildTitle;
    }

    public void setGuildTitle(String guildTitle) {
        this.guildTitle = guildTitle;
    }

    public String getGuildContent() {
        return guildContent;
    }

    public void setGuildContent(String guildContent) {
        this.guildContent = guildContent;
    }
}
