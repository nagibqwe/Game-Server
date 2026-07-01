package com.game.server;

public enum DbSqlName {

    //邮件
    MAIL_INSERT("mail.insert"),
    Mail_UPDATE("mail.update"),
    MAIL_DELETE_BY_RECEIVER_ID("mail.deleteByReceiverId"),//邮件删除
    MAIL_DELETE_BY_MAILID("mail.deleteByMailId"),//邮件删除

    //婚姻
    MARRIAGE_UPDATE_COUPLE("marriage.updateCouple"),
    MARRIAGE_UPDATE_MARRYTYPE("marriage.updateMarryType"),
    MARRIAGE_UPDATE_WEDDINGTYPE("marriage.updateWeddingType"),
    MARRIAGE_DELETE_BY_MARRIAGEID("marriage.deleteByMarriageId"),

    //婚宴
    WEDDING_INSERT("wedding.insert"),
    WEDDING_UPDATE("wedding.update"),
    WEDDING_DELETE_BY_MARRIAGEID("wedding.deleteByMarriageId"),

    //服务器参数
    SERVERPARAM_INSERT("serverParam.insert"),
    SERVERPARAM_UPDATE("serverParam.update"),

    //活动配置
    ACTIVITYCONFIG_INSERT("activityConfig.insert"),
    ACTIVITYCONFIG_UPDATE("activityConfig.update"),
    ACTIVITYCONFIG_DELETE("activityConfig.delete"),

    //活动数据
    ACTIVITYDATA_INSERT("activityData.insert"),
    ACTIVITYDATA_UPDATE("activityData.update"),
    ACTIVITYDATA_DELETE("activityData.delete"),

    //角色活动数据
    ROLEACTIVITYDATA_INSERT("roleActivityData.insert"),
    ROLEACTIVITYDATA_UPDATE("roleActivityData.update"),

    //用户活动
    USERACTIVITY_INSERT("useractivity.insert"),
    USERACTIVITY_UPDATE("useractivity.update"),


    //玩家排行数据
    RANKPLAYER_INSERT("rankplayer.insert"),
    RANKPLAYER_UPDATE("rankplayer.update"),
    RANKPLAYER_DELETEBYROLEID("rankplayer.deleteByRoleId"),

    //公会
    GUILD_INSERT("guild.insert"),
    GUILD_UPDATE("guild.update"),
    GUILD_DELETE("guild.delete"),

    GUILD_CHANGE_GUILDNAME("guild.changeGuildName"),
    GUILD_CHANGE_CHAIRMANID("guild.changeChairmanId"),

    //公会成员
    GUILDMEMBER_INSERT("guildMember.insert"),
    GUILDMEMBER_UPDATE("guildMember.update"),
    GUILDMEMBER_DELETEMEMBERBYID("guildMember.deleteMemberById"),

    //婚姻
    MARRIAGE_UPDATE("marry.update"),
    MARRIAGE_INSERT("marry.insert"),
    MARRIAGE_DELETE("marry.delete"),
    MARRY_DECLARATION_INSERT_OR_UPDATE("marry.insertOrUpdateDeclaration"),
    MARRY_DECLARATION_DELETE("marry.deleteDeclaration"),

    //巅峰竞技场
    PeekUpdate("peakpk.insertOrUpdate"),

    //好友
    FRIEND_INSERT("friend.insert"),
    FRIEND_UPDATE("friend.update"),

    //世界人物
    PLAYERWORLDINFO_INSERT("PlayerWorldInfo.insert"),
    PLAYERWORLDINFO_UPDATE("PlayerWorldInfo.update"),

    //角色信息
    ROLELOGININFO_INSERT("roleLoginInfo.insert"),
    ROLELOGININFO_UPDATE("roleLoginInfo.update"),

    //红包的数据处理
    REDPACKET_INSERT("redpacket.insert"),
    REDPACKET_UPDATE("redpacket.update"),
    REDPACKET_DELETE("redpacket.delete"),

    //boss死亡记录
    BOSS_DIE_RECORD_INSERT("bossDieRecord.insert"),
    BOSS_DIE_RECORD_SELECTALL("bossDieRecord.selectAll"),
    BOSS_DIE_RECORD_DELETEALL("bossDieRecord.deleteAll"),

    //登录服数据操作
    PLAYER_LOGIN_UPDATE_NAME("rolelogin.updateName"),
    PLAYER_LOGIN_UPDATE_LV("rolelogin.updateLv"),
    PLAYER_LOGIN_UPDATE_CAREER("rolelogin.updateCareer"),
    PLAYER_LOGIN_UPDATE_DELETE("rolelogin.updateDelete"),
    PLAYER_LOGIN_INSERT("rolelogin.insert"),

    //每日累充数据
    DAILY_ACC_INSERT("DailyAccRecharge.insert"),
    DAILY_ACC_UPDATE("DailyAccRecharge.update"),
    DAILY_ACC_DELETE("DailyAccRecharge.delete"),

    // 挚友数据
    CHUM_INSERT("CHUM_INSERT"),
    CHUM_UPDATE("CHUM_UPDATE"),
    CHUM_DELETE("CHUM_DELETE"),

    //竞拍
    AUCTION_INSERT("auction.insert"),
    AUCTION_UPDATE("auction.update"),
    AUCTION_DELETE("auction.delete");

    DbSqlName(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return this.name;
    }
}
