package com.game.couplefight;

import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.team.structs.Team;
import com.game.team.manager.TeamManager;
import game.message.CouplefightMessage;
import game.message.TeamMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * 仙侣对决数据管理
 * @Auther: gouzhongliang
 * @Date: 2021/8/3 17:45
 */
public class CoupleManager {

    private Logger log = LogManager.getLogger(CoupleManager.class);

    private static CoupleManager coupleManager = new CoupleManager();
    private CoupleManager(){}
    public static CoupleManager getInstance(){
        return coupleManager;
    }

    /**成功报名的队伍*/
    private Set<Team> teams = new HashSet<>();

    /**小组赛队伍*/
    private Set<Team> groups = new HashSet<>();

    /**冠军赛队伍*/
    private Set<Team> championsDi = new HashSet<>();

    /**冠军赛队伍*/
    private Set<Team> championsTian = new HashSet<>();

    /**匹配中的队伍*/
    private Set<Team> matchs = new HashSet<>();

    /**等待加入仙侣对决队伍的人次*/
    private int joincoupleteam = 0;

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Set<Team> getGroups() {
        return groups;
    }

    public void setGroups(Set<Team> groups) {
        this.groups = groups;
    }

    public Set<Team> getMatchs() {
        return matchs;
    }

    public void setMatchs(Set<Team> matchs) {
        this.matchs = matchs;
    }

    public int getJoincoupleteam() {
        return joincoupleteam;
    }

    public void setJoincoupleteam(int joincoupleteam) {
        this.joincoupleteam = joincoupleteam;
    }

    public void info() {

    }

    /**
     * 报名
     * @param num
     */
    public synchronized void apply(int num) {
        int i=0;
        for(Team team : TeamManager.getInstance().getTeams().values()){
            if(team.getPlayers().size() != 2){
                continue;
            }
            i++;
            if(i > num){
                return;
            }
            if(teams.contains(team)){
                continue;
            }
            CouplefightMessage.ReqApply.Builder req = CouplefightMessage.ReqApply.newBuilder();
            req.setName("team" + i);
            Player p = team.getLeader();
            if(p != null){
                p.sendMsg(CouplefightMessage.ReqApply.MsgID.eMsgID_VALUE, req.build().toByteArray());
            }
        }
    }

    /**
     * 匹配
     */
    public synchronized void match() {
        for(Team team : teams){
            CouplefightMessage.ReqMatchStart.Builder req = CouplefightMessage.ReqMatchStart.newBuilder();
            Player player = team.getLeader();
            if(player != null){
                player.sendMsg(CouplefightMessage.ReqMatchStart.MsgID.eMsgID_VALUE, req.build().toByteArray());
            }
        }
    }

    public synchronized void addMatch(Team team) {
        matchs.add(team);
    }

    public synchronized void addTeam(Team team) {
        teams.add(team);
        log.info("报名成功，队伍数{}", teams.size());
    }

    public void matchSuccess(Team team) {
        matchs.remove(team);
        log.info("匹配成功，team:{}", team);
    }

    public void addGroupTeam(Team team) {
        if(teams.contains(team)){
            groups.add(team);
        }else{
            log.info("队伍不在预选中 team:{}", team);
        }
    }

    public void addChampionDiTeam(Team team) {
        if(groups.contains(team)){
            championsDi.add(team);
        }else{
            log.info("队伍不在小组中 team:{}", team);
        }
    }

    public void addChampionTianTeam(Team team) {
        if(groups.contains(team)){
            championsTian.add(team);
        }else{
            log.info("队伍不在小组中 team:{}", team);
        }
    }

    /**
     * 加入夫妻队伍
     * @param num
     */
    public void joincoupleteam(int num) {
        joincoupleteam = num;
        TeamMessage.ReqGetWaitList.Builder req = TeamMessage.ReqGetWaitList.newBuilder();
        req.setType(0);
        Player player = PlayerManager.getInstance().deal().getPlayerNoTeam();
        if(player != null){
            player.sendMsg(TeamMessage.ReqGetWaitList.MsgID.eMsgID_VALUE, req.build().toByteArray());
        }else{
            log.info("没有未组队的玩家");
        }
    }

    /**
     * 进入准备地图
     */
    public void enterGroup() {
        for(Team t : groups){
            for(Player p : t.getPlayers().values()){
                CouplefightMessage.ReqGroupPrepareMapEnter.Builder req = CouplefightMessage.ReqGroupPrepareMapEnter.newBuilder();
                p.sendMsg(CouplefightMessage.ReqGroupPrepareMapEnter.MsgID.eMsgID_VALUE, req.build().toByteArray());
            }
        }
    }

    public void enterDi(){
        for(Team t : championsDi){
            for(Player p : t.getPlayers().values()){
                CouplefightMessage.ReqChampionEnter.Builder req = CouplefightMessage.ReqChampionEnter.newBuilder();
                p.sendMsg(CouplefightMessage.ReqChampionEnter.MsgID.eMsgID_VALUE, req.build().toByteArray());
            }
        }
    }

    public void enterTian(){
        for(Team t : championsTian){
            for(Player p : t.getPlayers().values()){
                CouplefightMessage.ReqChampionEnter.Builder req = CouplefightMessage.ReqChampionEnter.newBuilder();
                p.sendMsg(CouplefightMessage.ReqChampionEnter.MsgID.eMsgID_VALUE, req.build().toByteArray());
            }
        }
    }

    public void clear() {
        CoupleManager.getInstance().teams.clear();
        CoupleManager.getInstance().groups.clear();
        CoupleManager.getInstance().championsDi.clear();
        CoupleManager.getInstance().championsTian.clear();
        CoupleManager.getInstance().matchs.clear();
    }
}
