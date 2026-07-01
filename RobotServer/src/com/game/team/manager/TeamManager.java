package com.game.team.manager;

import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.team.structs.Team;
import game.message.TeamMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/8/3 17:30
 */
public class TeamManager {

    private final static Logger log = LogManager.getLogger(TeamManager.class);

    private static TeamManager teamManager = new TeamManager();

    public static TeamManager getInstance(){
        return teamManager;
    }

    /**所有队伍*/
    private Map<Long, Team> teams = new ConcurrentHashMap<>();

    public Map<Long, Team> getTeams() {
        return teams;
    }

    public void setTeams(Map<Long, Team> teams) {
        this.teams = teams;
    }

    public void addTeam(Team team){
        teams.put(team.getId(), team);
    }

    public void removeTeamPlayer(long teamId, Player player) {
        Team team = teams.get(teamId);
        if(team != null){
            synchronized (team){
                team.getPlayers().remove(player.getId());
                log.info("玩家：{} 离开队伍成功 teamID：{}",player.getId(), teamId);
            }
        }
    }

    /**
     * 创建夫妻队伍
     * @param num
     */
    public void createCoupleTeam(int num) {
        List<Player> c0 = new ArrayList<>();
        List<Player> c1 = new ArrayList<>();
        for(Player p : PlayerManager.getInstance().getPlayers().values()){
            if(p.getTeamId() != 0){
                continue;
            }
            //职业
            int career = p.getCareer();
            if(career == 0 && c0.size() < num){
                c0.add(p);
            }else if(career == 1 && c1.size() < num){
                c1.add(p);
            }
        }

        int max = c0.size() > c1.size() ? c1.size() : c0.size();
        //创建队伍
        for(int i=0;i<max;i++){
            c0.get(i).createAutoTeam();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //加入队伍
        for(int i=0;i<max;i++){
            long tid = c0.get(i).getTeamId();
            if(tid > 0){
                c1.get(i).joinTeam(tid);
            }
        }
    }

    /**
     * 输出信息
     */
    public void info() {
        System.out.println(teams);
    }

    /**
     * 根据玩家id获取队伍
     * @param rid
     * @return
     */
    public Team getByPlayer(long rid) {
        for(Team team : teams.values()){
            for(Player player : team.getPlayers().values()){
                if(player.getId() == rid){
                    return team;
                }
            }
        }
        return null;
    }

    public synchronized Team getTeam(long teamId) {
        Team team = teams.get(teamId);
        return team;
    }

    /**
     * 更新队伍
     * @param teamId
     * @param membersList
     */
    public synchronized void updateTeam(long teamId, List<TeamMessage.TeamMember> membersList) {
        Team team = getTeam(teamId);
        if(team == null){
            team = new Team();
            team.setId(teamId);
            teams.put(teamId,team);
            log.info("创建队伍 teamID：{}",teamId);
        }
        for(TeamMessage.TeamMember member : membersList){
            long roleId = member.getRoleId();
            boolean leader = member.getIsLeader();
            Player player = PlayerManager.getInstance().getPlayers().get(roleId);
            if(player != null){
                player.enterTeam(team, leader);
            }
        }
    }

    public void quit(int pid) {
        Player player = PlayerManager.getInstance().getPlayers().get(pid);
        if(player != null){
            player.reqQuitTeam();
        }
    }
}
