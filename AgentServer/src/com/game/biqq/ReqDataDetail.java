package com.game.biqq;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/12/17 14:31
 */
public class ReqDataDetail {
    /**版本号*/
    private String iversion;
    /**应用的唯一标识*/
    private String appid;
    /**用户ip地址*/
    private String userip;
    /**CGI或者ServerIP*/
    private String svrip;
    /**操作时间*/
    private Integer time;
    /**APP所在平台 Qzone为1； QQGame为10*/
    private Integer domain;
    /**应用分区分服时大区的ID*/
    private Integer iworldid;
    /**操作类型 支付类操作为1； 留言发表类为2； 写操作类为3； 读操作类为4； 其它为5*/
    private Integer optype;
    /**操作ID 登录为1；（必需上报） 主动注册为2；（必
     需上报） 接受邀请注册为3； 邀请他人注册是4； 留言为
     6； 留言回复为7； 如有其它类型的留言发表类操作，请填
     8。 用户登出为9；（必需上报） 角色登录为11；（必需上
     报） 创建角色为12；（必需上报） 角色退出为13； （必需
     上报） 其它操作请开发者使用100以上的int型数据上报*/
    private Integer actionid;
    /**操作用户UID*/
    private Long opuid;
    /**操作用户OpenID*/
    private String opopenid;
    /**被操作用户UID*/
    private String touid;
    /**被操作用户OpenID*/
    private String toopenid;
    /**操作用户的等级*/
    private Integer level;
    /**来源*/
    private String source;
    /**单次在线时长（秒）*/
    private Integer onlinetime;

    public String getIversion() {
        return iversion;
    }

    public void setIversion(String iversion) {
        this.iversion = iversion;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUserip() {
        return userip;
    }

    public void setUserip(String userip) {
        this.userip = userip;
    }

    public String getSvrip() {
        return svrip;
    }

    public void setSvrip(String svrip) {
        this.svrip = svrip;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getDomain() {
        return domain;
    }

    public void setDomain(Integer domain) {
        this.domain = domain;
    }

    public Integer getIworldid() {
        return iworldid;
    }

    public void setIworldid(Integer iworldid) {
        this.iworldid = iworldid;
    }

    public Integer getOptype() {
        return optype;
    }

    public void setOptype(Integer optype) {
        this.optype = optype;
    }

    public Integer getActionid() {
        return actionid;
    }

    public void setActionid(Integer actionid) {
        this.actionid = actionid;
    }

    public Long getOpuid() {
        return opuid;
    }

    public void setOpuid(long opuid) {
        this.opuid = opuid;
    }

    public String getOpopenid() {
        return opopenid;
    }

    public void setOpopenid(String opopenid) {
        this.opopenid = opopenid;
    }

    public String getTouid() {
        return touid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }

    public String getToopenid() {
        return toopenid;
    }

    public void setToopenid(String toopenid) {
        this.toopenid = toopenid;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getOnlinetime() {
        return onlinetime;
    }

    public void setOnlinetime(Integer onlinetime) {
        this.onlinetime = onlinetime;
    }

    private static SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("dtEventTime=").append(format.format(new Date()));
        if(iversion != null){
            sb.append("&iversion=").append(iversion);
        }
        if(appid != null){
            sb.append("&appid=").append(appid);
        }
        if(userip != null){
            sb.append("&userip=").append(userip);
        }
        if(svrip != null){
            sb.append("&svrip=").append(svrip);
        }
        if(time != null){
            sb.append("&time=").append(time);
        }
        if(domain != null){
            sb.append("&domain=").append(domain);
        }
        if(iworldid != null){
            sb.append("&iworldid=").append(iworldid);
        }
        if(optype != null){
            sb.append("&optype=").append(optype);
        }
        if(actionid != null){
            sb.append("&actionid=").append(actionid);
        }
        if(opuid != null){
            sb.append("&opuid=").append(opuid);
        }
        if(opopenid != null){
            sb.append("&opopenid=").append(opopenid);
        }
        if(touid != null){
            sb.append("&touid=").append(touid);
        }
        if(toopenid != null){
            sb.append("&toopenid=").append(toopenid);
        }
        if(level != null){
            sb.append("&level=").append(level);
        }
        if(source != null){
            sb.append("&source=").append(source);
        }
        if(onlinetime != null){
            sb.append("&onlinetime=").append(onlinetime);
        }
        return sb.toString();
    }
}
