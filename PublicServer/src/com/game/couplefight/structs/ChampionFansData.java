package com.game.couplefight.structs;

import com.data.Global;
import game.message.CouplefightMessage;

import java.util.*;

/**
 * 冠军赛粉丝数据
 * @Auther: gouzhongliang
 * @Date: 2021/7/21 16:48
 */
public class ChampionFansData implements Comparator<Fans>{

    private Map<Long, Fans> fans = new HashMap<>();

    private List<Fans> ranks = new ArrayList<>();

    public Map<Long, Fans> getFans() {
        return fans;
    }

    public void setFans(Map<Long, Fans> fans) {
        this.fans = fans;
    }

    /**
     * 数据更新
     * @param obj
     */
    public synchronized void update(Fans obj) {
        Fans f = fans.get(obj.getId());
        int money = Global.Marry_battle_guess_use.get(1);
        if(f != null){
            f.setMoney(f.getMoney() + money);
            f.setLevel(obj.getLevel());
            f.setName(obj.getName());
            f.setPower(obj.getPower());
        }else{
            f = obj;
            f.setMoney(money);
            ranks.add(obj);
        }
        fans.put(f.getId(), f);
    }

    /**
     * 排序
     */
    public synchronized void sort(){
        ranks.sort(this);
    }

    /**
     * 更新数据并排序
     * @param obj
     */
    public synchronized void updateAndSort(Fans obj) {
        update(obj);
        sort();
    }

    @Override
    public int compare(Fans o1, Fans o2) {
        if(o1.getMoney() > o2.getMoney()){
            return -1;
        }else if(o1.getMoney() < o2.getMoney()){
            return 1;
        }
        return 0;
    }

    /**
     * 转proto
     * @param size
     */
    public CouplefightMessage.ResChampionFansRankList.Builder toProto(int size) {
        CouplefightMessage.ResChampionFansRankList.Builder res = CouplefightMessage.ResChampionFansRankList.newBuilder();
        int rank = 0;
        for(Fans f : ranks){
            rank++;
            if(rank > size){
                break;
            }
            CouplefightMessage.FansInfo.Builder fanpt = CouplefightMessage.FansInfo.newBuilder();
            fanpt.setRank(rank);
            fanpt.setName(f.getName());
            fanpt.setLevel(f.getLevel());
            fanpt.setPower(f.getPower());
            fanpt.setMoney(f.getMoney());
            res.addFans(fanpt);
        }
        return res;
    }
}
