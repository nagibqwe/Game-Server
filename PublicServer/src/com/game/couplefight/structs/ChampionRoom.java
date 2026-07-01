package com.game.couplefight.structs;

import com.data.Global;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.fightroom.structs.FightRoom;
import game.message.CouplefightMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * 冠军赛战斗信息
 * @Auther: gouzhongliang
 * @Date: 2021/7/19 11:54
 */
public class ChampionRoom extends CoupleFightRoom{

    private int id;
    /**获胜的队伍*/
    private Long win;
    /**支持信息 玩家-队伍*/
    private transient Map<Long,Long> guess = new HashMap<>();
    /**队伍1支持率*/
    private transient int rate = 50;

    public ChampionRoom(){}

    public ChampionRoom(int id, FightRoom room, CoupleTeam t1, CoupleTeam t2) {
        super(room, t1, t2);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getWin() {
        return win;
    }

    public void setWin(Long win) {
        this.win = win;
    }

    public Map<Long, Long> getGuess() {
        return guess;
    }

    public void setGuess(Map<Long, Long> guess) {
        this.guess = guess;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    /**
     * 进行竞猜
     * @param rid
     * @param teamId
     * @return
     */
    public synchronized int guess(CoupleData data, long rid, long teamId) {
        if(super.getRoomId() != 0){
            return 1;//已经开始， 不能竞猜
        }else if(guess.containsKey(rid)){
            return 2;//已参与竞猜
        }else{
            CoupleTeam t1 = data.getTeams().get(getT1());
            CoupleTeam t2 = data.getTeams().get(getT2());
            if((t1 != null && t1.getId() == teamId) || (t2 != null && t2.getId() == teamId)){
                guess.put(rid, teamId);
                //更新胜率
                int size = guess.size();
                int oneSize = 0;
                for(Long id : guess.values()){
                    if(t1 != null && t1.getId() == id){
                        oneSize++;
                    }
                }
                this.rate = (oneSize + 10) * 100 / (size + 20);
            }
        }
        return 0;
    }

    public CouplefightMessage.ChampionGroup.Builder toProtoInfo(CoupleData data) {
        CouplefightMessage.ChampionGroup.Builder g = CouplefightMessage.ChampionGroup.newBuilder();
        if(getT1() != 0){
            CouplefightMessage.ChampionTeam.Builder t = CouplefightMessage.ChampionTeam.newBuilder();
            CoupleTeam team = data.getTeams().get(getT1());
            t.setNumber(team.getNumber());
            t.setType(win == null ? 0 : win == getT1() ? 1 : 2);
            t.setTeam(team.toProto());
            g.setT1(t);
        }
        if(getT2() != 0){
            CouplefightMessage.ChampionTeam.Builder t = CouplefightMessage.ChampionTeam.newBuilder();
            CoupleTeam team = data.getTeams().get(getT2());
            t.setNumber(team.getNumber());
            t.setType(win == null ? 0 : win == getT2() ? 1 : 2);
            t.setTeam(team.toProto());
            g.setT2(t);
        }
        g.setId(id);
        return g;
    }

    /**
     * 返回竞猜proto
     * @param rid
     * @return
     */
    public CouplefightMessage.GuessInfo.Builder toProtoGuess(CoupleData data, long rid) {
        CouplefightMessage.GuessInfo.Builder res = CouplefightMessage.GuessInfo.newBuilder();
        Long teamId = guess.get(rid);

        int gold = Global.Marry_battle_guess_use.get(1);
        int loseGold = Global.Marry_battle_guess_failed_reward.get(1);
        if(getT1() != 0){
            CoupleTeam team = data.getTeams().get(getT1());
            CouplefightMessage.GuessTeamInfo.Builder gs = CouplefightMessage.GuessTeamInfo.newBuilder();
            if(teamId == null || teamId != team.getId()){
                gs.setGuess(false);
            }else{
                gs.setGuess(true);
            }
            gs.setGold(gold);
            gs.setLoseGold(loseGold);
            int winGold = getWinGoldByRate(rate);
            gs.setWinGold(winGold);
            gs.setRate(rate);
            gs.setTeamId(getT1());
            res.setG1(gs);
        }
        if(getT2() != 0){
            CoupleTeam team = data.getTeams().get(getT2());
            CouplefightMessage.GuessTeamInfo.Builder gs = CouplefightMessage.GuessTeamInfo.newBuilder();
            if(teamId == null || teamId != team.getId()){
                gs.setGuess(false);
            }else{
                gs.setGuess(true);
            }
            gs.setGold(gold);
            gs.setLoseGold(loseGold);
            gs.setWinGold(getWinGoldByRate(100 - rate));
            gs.setRate(100 - rate);
            gs.setTeamId(getT2());
            res.setG2(gs);
        }
        res.setFightId(id);
        return res;
    }

    public int getWinGoldByRate(int rate) {
        ReadIntegerArrayEs es = Global.Marry_battle_guess_success_reward;
        int gold = 0;
        for(ReadArray<Integer> a : es.getValuees()){
            if(rate >= a.get(0) && rate < a.get(1)){
                gold =  a.get(3);
            }
        }
        return gold;
    }
}
