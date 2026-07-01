package com.game.team.structs;

public class TeamConstant {

    public static final int SearchTypeRand = 0;         //周围玩家
    public static final int SearchTypeFriend = 1;       //好友
    public static final int SearchTypeGuild = 2;        //帮会

    public static final int InviteTypeAgree = 0;        //同意邀请
    public static final int InviteTypeRefuse = -1;      //拒绝

    public static final int ApplyTypeAgree = 0;         //同意申请
    public static final int ApplyTypeRefuse = -1;       //拒绝

    public static final int TeamOptTypeUpLeader = 1;    //提升队长
    public static final int TeamOptTypeTickOut = 2;     //踢出队伍
    public static final int TeamOptTypeQuit = 3;        //退出队伍
    public static final int TeamOptTypeBreak = 4;       //解散队伍
    public static final int TeamOptTypeAutoAccept = 5;  //自动接受组队申请
    public static final int TeamOptReqBecomeLeader = 6; //请求成为队长
    public static final int TeamOptRefuceMember = 7;    //拒绝转让队长

    public static final int TEAM_EXP_RATE = 10;     //组队经验加成

    public static final int TEAM_REFUSE_TIME = 2 * 60 * 1000;       //拒绝后申请cd时间

    public static final int TEAM_APPLY_TIME = 2 * 60 * 1000;        //申请接受超时时间

    public static final int TEAM_OFFLINE_TIME = 5 * 60 * 1000;      //离线退队检查时间

    public static final int TEAM_NOTICE_CD = 10000;                 //世界喊话cd时间

    public static final int TEAM_MATCH_TIMEOUT = 2 * 60 * 1000;     //自动匹配超时时间
}
