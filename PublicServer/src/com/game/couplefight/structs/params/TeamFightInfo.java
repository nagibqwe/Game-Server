package com.game.couplefight.structs.params;

import com.data.Global;
import com.data.struct.ReadIntegerArray;
import com.game.couplefight.manager.CouplefightManager;
import com.game.couplefight.structs.CoupleTeam;
import com.game.couplefight.structs.Player;
import com.game.manager.Manager;

import java.util.List;

/**
 * 队伍战斗结束时的数据
 * @Auther: gouzhongliang
 * @Date: 2021/8/5 20:45
 */
public class TeamFightInfo {
    /**队伍*/
    private CoupleTeam team;
    /**输赢*/
    private boolean win;
    /**有效的玩家*/
    private List<Long> valids;
    /**积分变化*/
    private int score;

    public TeamFightInfo(CoupleTeam team, boolean win){
        this(team, win, null);
    }

    public TeamFightInfo(CoupleTeam team, boolean win, List<Long> valids){
        this.team = team;
        this.win = win;
        this.valids = valids;
        ReadIntegerArray ss = null;
        if(Manager.couplefightManager.getStatus() == CouplefightManager.status_group){
            ss = Global.Marry_battle_2_count;
        }else if(Manager.couplefightManager.getStatus() == CouplefightManager.status_select){
            ss = Global.Marry_battle_1_count;
        }
        if(ss != null){
            if(win){
                this.score = ss.get(0);
            }else{
                this.score = ss.get(1);
            }
        }else{
            //冠军赛
            if(win){
                this.score = 2;
            }else{
                this.score = 1;
            }
        }

    }

    public CoupleTeam getTeam() {
        return team;
    }

    public void setTeam(CoupleTeam team) {
        this.team = team;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public List<Long> getValids() {
        return valids;
    }

    public void setValids(List<Long> valids) {
        this.valids = valids;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
